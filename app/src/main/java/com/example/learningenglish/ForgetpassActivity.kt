package com.example.learningenglish

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var edtPhone: EditText
    private lateinit var btnSubmit: Button

    private val apiUrl = "http://10.0.2.2:3000/forgotpassword" // API URL cho quên mật khẩu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgetpass)

        edtPhone = findViewById(R.id.edtPhone)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val phone = edtPhone.text.toString().trim()

            if (phone.isNotEmpty()) {
                // Gửi yêu cầu tới server để xử lý quên mật khẩu
                // Ở đây, bạn có thể gọi một API để gửi mã xác thực hoặc email reset mật khẩu
                resetPassword(phone)
            } else {
                Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetPassword(phone: String) {
        // Thực hiện gọi API reset mật khẩu ở đây
        // Ví dụ:
        Toast.makeText(this, "Yêu cầu đặt lại mật khẩu đã được gửi!", Toast.LENGTH_SHORT).show()
    }
}
