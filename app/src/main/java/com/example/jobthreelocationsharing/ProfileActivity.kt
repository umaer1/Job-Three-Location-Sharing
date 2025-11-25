package com.example.jobthreelocationsharing

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthenticationViewModel
    private lateinit var firestoreViewModel: FirestoreViewModel
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var buttonUpdateProfile: Button
    private lateinit var textViewEmail: EditText
    private lateinit var editTextNewLocation: EditText
    private lateinit var editTextNewName: EditText
    private lateinit var logoutBtn: ImageButton
    private lateinit var homeBtn: ImageButton
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        buttonUpdateProfile = findViewById(R.id.updateBtn)
        homeBtn = findViewById(R.id.homeBtn)
        logoutBtn = findViewById(R.id.logoutBtn)
        textViewEmail = findViewById(R.id.emailEt)
        editTextNewLocation = findViewById(R.id.locationEt)
        editTextNewName = findViewById(R.id.nameEt)
        authViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)
        firestoreViewModel = ViewModelProvider(this).get(FirestoreViewModel::class.java)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
        homeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        loadUserInfo()

        buttonUpdateProfile.setOnClickListener {
            val newName = editTextNewName.text.toString()
            val newLocation = editTextNewLocation.text.toString()

            updateProfile(newName, newLocation)
        }
    }

    private fun loadUserInfo() {
        val currentUser = authViewModel.getCurrentUser()
        if (currentUser != null) {
            textViewEmail.setText(currentUser.email)
            // Fetch user's display name from Firestore
            firestoreViewModel.getUser(currentUser.uid) { user ->
                if (user != null) {
                    editTextNewName.setText(user.displayName)
                    // Fetch user's location from Firestore
                    firestoreViewModel.getUserLocation(currentUser.uid) { location ->
                        if (location.isNotEmpty()) {
                            editTextNewLocation.setText(location)
                        }
                    }
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity
                }
            }
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            finish() // Close the activity
        }
    }

    private fun updateProfile(newName: String, newLocation: String) {
        val currentUser = authViewModel.getCurrentUser()
        if (currentUser != null) {
            val userId = currentUser.uid
            firestoreViewModel.updateUser(userId, newName, newLocation)
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        } else {
            // Handle the case where the current user is null
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
    }
}