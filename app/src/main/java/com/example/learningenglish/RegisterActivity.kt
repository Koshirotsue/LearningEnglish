package com.example.learningenglish

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtName: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView

    private val apiUrl = "http://10.0.2.2/register.php"  // Sửa lại thành IP đúng nếu bạn chạy trên máy thật

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtPhone = findViewById(R.id.edtPhone)
        edtPassword = findViewById(R.id.edtPassword)
        edtName = findViewById(R.id.edtName)
        btnRegister = findViewById(R.id.btnRegister)
        tvLogin = findViewById(R.id.tvLogin)

        btnRegister.setOnClickListener {
            val phone = edtPhone.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val name = edtName.text.toString().trim()

            if (phone.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                registerUser(name, phone, password)
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerUser(name: String, phone: String, password: String) {
        val queue = Volley.newRequestQueue(this)
        val stringRequest = object : StringRequest(
            Request.Method.POST, apiUrl,
            { response ->
                try {
                    val json = JSONObject(response)
                    val status = json.getString("status")
                    val message = json.getString("message")

                    if (status == "success") {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("REGISTER", "Parse error: $e")
                    Toast.makeText(this, "Lỗi phản hồi từ server", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("REGISTER", "Error: $error")
                Toast.makeText(this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = name
                params["phone"] = phone
                params["pass"] = password
                return params
            }
        }

        queue.add(stringRequest)
    }
}
