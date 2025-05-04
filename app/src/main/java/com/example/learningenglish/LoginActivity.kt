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

class LoginActivity : AppCompatActivity() {

    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    private val apiUrl = "http://10.0.2.2/login.php"  // Sửa lại IP nếu cần thiết

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtPhone = findViewById(R.id.edtPhone)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val phone = edtPhone.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (phone.isNotEmpty() && password.isNotEmpty()) {
                loginUser(phone, password)
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser(phone: String, password: String) {
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
                        val userData = json.getJSONObject("data")
                        // Bạn có thể lưu thông tin người dùng vào SharedPreferences hoặc chuyển sang màn hình chính
                        // Chuyển sang màn hình chính (chẳng hạn MainActivity)
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("LOGIN", "Parse error: $e")
                    Toast.makeText(this, "Lỗi phản hồi từ server", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Log.e("LOGIN", "Error: $error")
                Toast.makeText(this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["phone"] = phone
                params["pass"] = password
                return params
            }
        }

        queue.add(stringRequest)
    }
}
