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
    private lateinit var bthQuiz: Button
    private lateinit var bntProcess: Button
    private lateinit var btnAch: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLearn = findViewById(R.id.btnLearn)
        bthQuiz = findViewById(R.id.bntQuiz)
        bntProcess = findViewById(R.id.bntProgress)
        btnAch = findViewById(R.id.bntAch)

//       btnLearn.setOnClickListener {
//            val intent = Intent(this, ::class.java)  Danh sách bài học
//            startActivity(intent)
//        }

        bthQuiz.setOnClickListener {
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