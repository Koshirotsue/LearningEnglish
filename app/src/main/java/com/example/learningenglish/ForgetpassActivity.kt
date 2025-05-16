package com.example.learningenglish

import android.os.Bundle
import android.util.Patterns // Để kiểm tra định dạng email
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var btnSubmit: Button
    private lateinit var auth: FirebaseAuth // Khai báo FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgetpass) // Đảm bảo bạn có layout activity_forgetpass.xml

        edtEmail = findViewById(R.id.edtEmail) // ID này phải khớp với EditText trong XML
        btnSubmit = findViewById(R.id.btnSubmit) // ID này phải khớp với Button trong XML

        // Khởi tạo Firebase Auth
        auth = FirebaseAuth.getInstance()

        btnSubmit.setOnClickListener {
            val email = edtEmail.text.toString().trim()

            if (email.isNotEmpty()) {
                if (isValidEmail(email)) {
                    // Gọi hàm gửi yêu cầu reset mật khẩu
                    resetPassword(email)
                } else {
                    Toast.makeText(this, "Vui lòng nhập địa chỉ email hợp lệ", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập địa chỉ email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Kiểm tra xem chuỗi email có định dạng hợp lệ không.
     * @param email Email cần kiểm tra.
     * @return True nếu email hợp lệ, False nếu không.
     */
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Gửi yêu cầu đặt lại mật khẩu tới Firebase.
     * @param email Địa chỉ email của người dùng.
     */
    private fun resetPassword(email: String) {
        // Sử dụng Firebase Authentication để gửi email reset mật khẩu
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Yêu cầu đặt lại mật khẩu đã được gửi tới email của bạn! Vui lòng kiểm tra hộp thư (kể cả Spam).", Toast.LENGTH_LONG).show()
                    // Bạn có thể đóng Activity này sau khi gửi thành công
                    // finish()
                } else {
                    // Hiển thị thông báo lỗi chi tiết hơn
                    val errorMessage = task.exception?.message ?: "Không rõ lỗi"
                    Toast.makeText(this, "Lỗi khi gửi yêu cầu: $errorMessage", Toast.LENGTH_LONG).show()
                    // Log lỗi để debug nếu cần
                    // Log.e("ForgetPasswordActivity", "Error sending password reset email", task.exception)
                }
            }
    }
}
