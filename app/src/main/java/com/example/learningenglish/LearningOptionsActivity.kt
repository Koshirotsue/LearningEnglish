package com.example.learningenglish

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LearningOptionsActivity : AppCompatActivity() {
    private lateinit var btnVocabulary: Button
    private lateinit var btnGrammar: Button
    private lateinit var btnPronunciation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learning_options)

        // Initialize buttons
        btnVocabulary = findViewById(R.id.btnVocabulary)
        btnGrammar = findViewById(R.id.btnGrammar)
        btnPronunciation = findViewById(R.id.btnPronunciation)

        // Set click listeners
        btnVocabulary.setOnClickListener {
            val intent = Intent(this, VocabularyActivity::class.java)
            startActivity(intent)
        }

        btnGrammar.setOnClickListener {
             val intent = Intent(this, GrammarActivity::class.java)
             startActivity(intent)
        }

        btnPronunciation.setOnClickListener {
             val intent = Intent(this, VocabularyActivity::class.java)
             startActivity(intent)
        }
    }
} 