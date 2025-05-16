package com.example.learningenglish

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var btnLearn: Button
    private lateinit var btnQuiz: Button
    private lateinit var bntProcess: Button
    private lateinit var btnAch: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLearn = findViewById(R.id.btnLearn)
        btnQuiz = findViewById(R.id.bntQuiz)
        bntProcess = findViewById(R.id.bntProgress)
        btnAch = findViewById(R.id.bntAch)

//       btnLearn.setOnClickListener {
//            val intent = Intent(this, ::class.java)  Danh sách bài học
//            startActivity(intent)
//        }

<<<<<<< HEAD
        btnQuiz.setOnClickListener {
=======
        bthQuiz.setOnClickListener {
>>>>>>> b34dbb131163c1b51de8ba4f857dab99b6cdcb17
            val intent = Intent(this, ExerciseListActivity::class.java)
            startActivity(intent)
        }

        //       btnProcess.setOnClickListener {
//            val intent = Intent(this, ::class.java)  tiến độ học tập
//            startActivity(intent)
//        }

        //       btnAch.setOnClickListener {
//            val intent = Intent(this, ::class.java)  Thành tựu
//            startActivity(intent)
//        }
    }

}