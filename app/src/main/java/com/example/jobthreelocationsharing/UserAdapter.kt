package com.example.jobthreelocationsharing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private var userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateData(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val displayNameTxt = itemView.findViewById<TextView>(R.id.displayNameTxt)
        val emailTxt = itemView.findViewById<TextView>(R.id.emailTxt)
        val locationTxt = itemView.findViewById<TextView>(R.id.locationTxt)
        fun bind(user: User) {
            displayNameTxt.text = user.displayName
            emailTxt.text = user.email
            locationTxt.text = user.location
        }
    }
}