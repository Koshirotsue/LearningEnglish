package com.example.learningenglish

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {

    private lateinit var scoreTextMain: TextView
    private lateinit var correctAnswersText: TextView
    private lateinit var incorrectAnswersText: TextView
    private lateinit var percentageText: TextView
    private lateinit var playAgainButton: Button
    private lateinit var homeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizcompleted)

        initializeViews()
        val score = intent.getIntExtra("score", 0)
        val totalQuestions = intent.getIntExtra("total_questions", 1)
        val incorrectAnswers = totalQuestions - score
        val percentage = if (totalQuestions > 0) {
            ((score.toDouble() / totalQuestions) * 100).roundToInt()
        } else {
            0
        }
        displayResults(score, totalQuestions, incorrectAnswers, percentage)

        setupButtonListeners()
    }

    private fun initializeViews() {
        scoreTextMain = findViewById(R.id.score_text_main)
        correctAnswersText = findViewById(R.id.correct_answers_text)
        incorrectAnswersText = findViewById(R.id.incorrect_answers_text)
        percentageText = findViewById(R.id.percentage_text)
        playAgainButton = findViewById(R.id.play_again_button)
        homeButton = findViewById(R.id.home_button)
    }

    private fun displayResults(score: Int, totalQuestions: Int, incorrectAnswers: Int, percentage: Int) {
        scoreTextMain.text = "Bạn đạt: $score/$totalQuestions"
        correctAnswersText.text = score.toString()
        incorrectAnswersText.text = incorrectAnswers.toString()
        percentageText.text = "$percentage%"
    }
    private fun setupButtonListeners() {
        playAgainButton.setOnClickListener {
            val intent = Intent(this, ExerciseListActivity::class.java)
            startActivity(intent)
            finish()
        }
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
