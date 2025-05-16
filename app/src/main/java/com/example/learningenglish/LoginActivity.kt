package com.example.learningenglish

import android.content.Intent
import android.os.Bundle
import android.util.Log // Keep Log if you use it for debugging, otherwise it can be removed
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var tvForgetPass: TextView // TextView for "Forget Password"

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Ensure this layout file exists and is correct

        // Initialize UI elements
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
        tvForgetPass = findViewById(R.id.tvForgetpass) // Make sure this ID matches your activity_login.xml

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Login button click listener
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                            // Navigate to MainActivity after successful login
                            startActivity(Intent(this, MainActivity::class.java))
                            finish() // Finish LoginActivity so user can't go back with back button
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        // Register TextView click listener
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Forget Password TextView click listener
        tvForgetPass.setOnClickListener {
            // Start ForgetPasswordActivity when "Forget Password" is clicked
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }
    }
}
