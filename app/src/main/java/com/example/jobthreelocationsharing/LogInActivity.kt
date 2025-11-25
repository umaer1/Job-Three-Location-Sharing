package com.example.jobthreelocationsharing

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LogInActivity : AppCompatActivity() {

    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var buttonLogin:Button
    private lateinit var editTextEmail:EditText
    private lateinit var editTextPassword:EditText
    private lateinit var textViewRegister:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)

        buttonLogin = findViewById(R.id.loginBtn)
        editTextEmail = findViewById(R.id.emailEt)
        editTextPassword = findViewById(R.id.passwordEt)
        textViewRegister = findViewById(R.id.registerTxt)

        viewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            viewModel.login(email, password, {
                // Login successful, navigate to next screen
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, { errorMessage ->
                // Display error message to user
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            })
        }

        textViewRegister.setOnClickListener {
            // Navigate to registration screen
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    override fun onStart() {
        super.onStart()
        if(Firebase.auth.currentUser!=null){
            startActivity(Intent(this@LogInActivity, MainActivity::class.java))
            finish()
        }
    }
}