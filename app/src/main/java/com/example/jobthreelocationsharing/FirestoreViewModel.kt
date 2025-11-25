package com.example.jobthreelocationsharing

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    fun saveUser(userId: String, displayName: String, email: String, location: String) {
        val user = hashMapOf(
            "displayName" to displayName,
            "email" to email,
            "location" to location
        )

        usersCollection.document(userId).set(user)
            .addOnSuccessListener {
                // User data saved successfully
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    fun getAllUsers(callback: (List<User>) -> Unit) {
        usersCollection.get()
            .addOnSuccessListener { result ->
                val userList = mutableListOf<User>()
                for (document in result) {
                    val userId = document.id
                    val displayName = document.getString("displayName") ?: ""
                    val email = document.getString("email") ?: ""
                    val location = document.getString("location") ?: ""
                    userList.add(User(userId, displayName, email, location))
                }
                callback(userList)
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    fun updateUser(userId: String, displayName: String, location: String) {
        val user = hashMapOf(
            "displayName" to displayName,
            "location" to location
        )
        // Convert HashMap to Map
        val userMap = user.toMap()
        usersCollection.document(userId).update(userMap)
            .addOnSuccessListener {
                // User data updated successfully
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    fun updateUserLocation(userId: String, location: String) {
        if (userId.isEmpty()) {
            // Handle the case where userId is empty or null
            return
        }
        val user = hashMapOf(
            "location" to location
        )
        val userMap = user.toMap()
        usersCollection.document(userId).update(userMap)
            .addOnSuccessListener {
                // User location updated successfully
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }


    fun getUser(userId: String, callback: (User?) -> Unit) {
        usersCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                callback(user)
            }
            .addOnFailureListener { e ->
                // Handle failure
                callback(null)
            }
    }
    fun getUserLocation(userId: String, callback: (String) -> Unit) {
        usersCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val location = documentSnapshot.getString("location") ?: ""
                callback(location)
            }
            .addOnFailureListener { e ->
                callback("")
            }
    }




}