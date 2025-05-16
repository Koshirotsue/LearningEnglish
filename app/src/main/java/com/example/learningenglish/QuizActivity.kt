package com.example.learningenglish

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.random.Random



class QuizActivity : AppCompatActivity() {
    lateinit var db: SQLiteDatabase
    lateinit var rs: Cursor
    lateinit var adapter: SimpleCursorAdapter
    private var database: ReadDatabase? = null
    private lateinit var questionTextView: TextView
    private lateinit var explanationTextView: TextView
    private lateinit var optionButton1: Button
    private lateinit var optionButton2: Button
    private lateinit var optionButton3: Button
    private lateinit var optionButton4: Button
    private lateinit var buttonNextQuestion: Button
    private lateinit var pageNumberTextView: TextView

    private lateinit var allQuizQuestions: List<QuizQuestion>
    private lateinit var currentQuizQuestions: List<QuizQuestion>
    private var currentQuestionIndex = 0
    private var score = 0

    private var shuffledOptions: List<String> = listOf()
    private var shuffledCorrectAnswerIndex: Int = -1
    private val activityScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        database = ReadDatabase(this)
        database?.openQuizDatabase()
        var helper = DatabaseHelper(applicationContext)
        db = helper.readableDatabase
        rs = db.rawQuery("SELECT * FROM Question", null)
        initializeViews()

        val chapterNumber = intent.getIntExtra("CHAPTER_NUMBER", 0)
        allQuizQuestions = createQuizQuestions()
        currentQuizQuestions = if (chapterNumber > 0) {
            allQuizQuestions.filter { it.tag == chapterNumber }.shuffled()
        } else {
            allQuizQuestions.shuffled()
        }
        if (currentQuizQuestions.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy câu hỏi cho chương này.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        setupButtonListeners()
        displayQuestion(currentQuestionIndex)
    }
    private fun initializeViews() {
        questionTextView = findViewById(R.id.question_text)
        explanationTextView = findViewById(R.id.explanation_text)
        optionButton1 = findViewById(R.id.Answer1)
        optionButton2 = findViewById(R.id.Answer2)
        optionButton3 = findViewById(R.id.Answer3)
        optionButton4 = findViewById(R.id.Answer4)
        buttonNextQuestion = findViewById(R.id.button_next_question)
        pageNumberTextView = findViewById(R.id.page_number)
    }
    private fun setupButtonListeners() {
        optionButton1.setOnClickListener { checkAnswer(0) }
        optionButton2.setOnClickListener { checkAnswer(1) }
        optionButton3.setOnClickListener { checkAnswer(2) }
        optionButton4.setOnClickListener { checkAnswer(3) }
        buttonNextQuestion.setOnClickListener {
            loadNextQuestion()
        }
    }
    private fun displayQuestion(index: Int) {
        if (index < currentQuizQuestions.size) {
            val currentQuestion = currentQuizQuestions[index]
            questionTextView.text = currentQuestion.question
            shuffledOptions = currentQuestion.options.shuffled()
            shuffledCorrectAnswerIndex = shuffledOptions.indexOf(currentQuestion.correctAnswer)
            optionButton1.text = shuffledOptions.getOrNull(0) ?: "Option A"
            optionButton2.text = shuffledOptions.getOrNull(1) ?: "Option B"
            optionButton3.text = shuffledOptions.getOrNull(2) ?: "Option C"
            optionButton4.text = shuffledOptions.getOrNull(3) ?: "Option D"
            explanationTextView.visibility = View.GONE
            buttonNextQuestion.visibility = View.GONE
            resetButtonColors()
            enableAllButtons()
            pageNumberTextView.text = "${index + 1}/${currentQuizQuestions.size}"

        } else {
            endGame()
        }
    }
    private fun resetButtonColors() {
        val defaultColor = ContextCompat.getColor(this, android.R.color.transparent)
        optionButton1.setBackgroundColor(defaultColor)
        optionButton2.setBackgroundColor(defaultColor)
        optionButton3.setBackgroundColor(defaultColor)
        optionButton4.setBackgroundColor(defaultColor)

    }
    private fun enableAllButtons() {
        optionButton1.isEnabled = true
        optionButton2.isEnabled = true
        optionButton3.isEnabled = true
        optionButton4.isEnabled = true
    }
    private fun checkAnswer(selectedButtonIndex: Int) {
        disableAllButtons()
        val currentQuestion = currentQuizQuestions[currentQuestionIndex]
        explanationTextView.text = currentQuestion.explanation
        explanationTextView.visibility = View.VISIBLE
        buttonNextQuestion.visibility = View.VISIBLE

        if (selectedButtonIndex == shuffledCorrectAnswerIndex) {
            score++
            showCorrectAnswerFeedback(selectedButtonIndex)
        } else {
            showWrongAnswerFeedback(selectedButtonIndex)
        }
    }

    private fun showCorrectAnswerFeedback(selectedIndex: Int) {
        val correctColor = ContextCompat.getColor(this, R.color.correct_answer_color) // Define color in res/colors.xml
        when (selectedIndex) {
            0 -> optionButton1.setBackgroundColor(correctColor)
            1 -> optionButton2.setBackgroundColor(correctColor)
            2 -> optionButton3.setBackgroundColor(correctColor)
            3 -> optionButton4.setBackgroundColor(correctColor)
        }
        Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show() // Changed to English
    }

    private fun showWrongAnswerFeedback(selectedIndex: Int) {
        // Use ContextCompat.getColor for compatibility
        val wrongColor = ContextCompat.getColor(this, R.color.wrong_answer_color) // Define color in res/colors.xml
        val correctColor = ContextCompat.getColor(this, R.color.correct_answer_color)

        when (selectedIndex) {
            0 -> optionButton1.setBackgroundColor(wrongColor)
            1 -> optionButton2.setBackgroundColor(wrongColor)
            2 -> optionButton3.setBackgroundColor(wrongColor)
            3 -> optionButton4.setBackgroundColor(wrongColor)
        }
        // Also show the correct answer
        when (shuffledCorrectAnswerIndex) {
            0 -> optionButton1.setBackgroundColor(correctColor)
            1 -> optionButton2.setBackgroundColor(correctColor)
            2 -> optionButton3.setBackgroundColor(correctColor)
            3 -> optionButton4.setBackgroundColor(correctColor)
        }
        Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show() // Changed to English
    }

    private fun disableAllButtons() {
        optionButton1.isEnabled = false
        optionButton2.isEnabled = false
        optionButton3.isEnabled = false
        optionButton4.isEnabled = false
    }

    private fun loadNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < currentQuizQuestions.size) {
            displayQuestion(currentQuestionIndex)
        } else {
            endGame()
        }
    }

    private fun endGame() {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("score", score)
            putExtra("total_questions", currentQuizQuestions.size) // Pass the actual total number of questions in the filtered list
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }
    fun createQuizQuestions(): List<QuizQuestion> {
        val quizList = mutableListOf<QuizQuestion>()
        while (rs.moveToNext()){
            quizList.add(
                QuizQuestion(
                    question = rs.getString(1),
                    options = listOf(rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)),
                    correctAnswer = rs.getString(3),
                    explanation = rs.getString(2),
                    tag = Random.nextInt(1, 10)
                )
            )
        }
        quizList.add(
            QuizQuestion(
                question = "",
                options = listOf("furniture", "furnitures", "furniturs", "furnituries"),
                correctAnswer = "furniture",
                explanation = "'Furniture' is an uncountable noun, so it does not have a plural form with '-s'.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 2 (Grammar - Basic Verb Tense)
        quizList.add(
            QuizQuestion(
                question = "She _____ to the park yesterday.",
                options = listOf("go", "goes", "went", "going"),
                correctAnswer = "went",
                explanation = "The word 'yesterday' indicates the past simple tense. The past simple of 'go' is 'went'.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 3 (Vocabulary - Adjectives)
        quizList.add(
            QuizQuestion(
                question = "The weather is very _____ today. I should take an umbrella.",
                options = listOf("sunny", "hot", "rainy", "cloudy"),
                correctAnswer = "rainy",
                explanation = "If you need an umbrella, the weather is likely 'rainy'.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 4 (Grammar - Articles)
        quizList.add(
            QuizQuestion(
                question = "He is _____ honest man.",
                options = listOf("a", "an", "the", "no article"),
                correctAnswer = "an",
                explanation = "Use 'an' before words that start with a vowel sound, like 'honest' (the 'h' is silent).",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 5 (Vocabulary - Common Nouns)
        quizList.add(
            QuizQuestion(
                question = "I read a good _____ last night before sleeping.",
                options = listOf("table", "chair", "book", "door"),
                correctAnswer = "book",
                explanation = "You read a 'book'.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 6 (Grammar - Prepositions)
        quizList.add(
            QuizQuestion(
                question = "The cat is _____ the table.",
                options = listOf("in", "on", "at", "under"),
                correctAnswer = "on",
                explanation = "Use 'on' to indicate something is on the surface of something else.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 7 (Vocabulary - Verbs)
        quizList.add(
            QuizQuestion(
                question = "Can you please _____ me your pen?",
                options = listOf("borrow", "lend", "take", "give"),
                correctAnswer = "lend",
                explanation = "To 'lend' means to give something to someone for a short time, expecting it back.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 8 (Grammar - Pronouns)
        quizList.add(
            QuizQuestion(
                question = "This is my book. It is _____.",
                options = listOf("my", "mine", "I", "me"),
                correctAnswer = "mine",
                explanation = "'Mine' is a possessive pronoun used at the end of a sentence or phrase.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 9 (Vocabulary - Adverbs)
        quizList.add(
            QuizQuestion(
                question = "She speaks English very _____.",
                options = listOf("good", "well", "fluent", "quick"),
                correctAnswer = "well",
                explanation = "'Well' is an adverb used to modify the verb 'speaks'.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 10 (Grammar - Comparatives)
        quizList.add(
            QuizQuestion(
                question = "This car is _____ than that one.",
                options = listOf("more expensive", "expensiver", "most expensive", "expensive"),
                correctAnswer = "more expensive",
                explanation = "For longer adjectives like 'expensive', use 'more' for the comparative form.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Mức độ hiểu (Comprehension Level)

        // Question 11 (Vocabulary - Synonyms)
        quizList.add(
            QuizQuestion(
                question = "Choose the word closest in meaning to 'happy'.",
                options = listOf("sad", "angry", "joyful", "tired"),
                correctAnswer = "joyful",
                explanation = "'Joyful' is a synonym for 'happy'.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 12 (Grammar - Present Perfect)
        quizList.add(
            QuizQuestion(
                question = "I _____ this movie three times.",
                options = listOf("see", "saw", "have seen", "am seeing"),
                correctAnswer = "have seen",
                explanation = "Use the present perfect ('have seen') to talk about experiences that happened at an unspecified time in the past.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 13 (Vocabulary - Antonyms)
        quizList.add(
            QuizQuestion(
                question = "Choose the word opposite in meaning to 'ancient'.",
                options = listOf("old", "modern", "historical", "traditional"),
                correctAnswer = "modern",
                explanation = "'Modern' is the opposite of 'ancient'.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 14 (Grammar - Passive Voice)
        quizList.add(
            QuizQuestion(
                question = "The book _____ by millions of people.",
                options = listOf("reads", "is reading", "is read", "read"),
                correctAnswer = "is read",
                explanation = "This sentence is in the passive voice (subject receives the action). The structure is 'be' + past participle ('read').",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 15 (Vocabulary - Phrasal Verbs)
        quizList.add(
            QuizQuestion(
                question = "Could you please _____ the music? It's too loud.",
                options = listOf("turn on", "turn off", "turn up", "turn down"),
                correctAnswer = "turn down",
                explanation = "To 'turn down' means to reduce the volume.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 16 (Grammar - Conditional Sentences - Type 1)
        quizList.add(
            QuizQuestion(
                question = "If it _____, we will stay at home.",
                options = listOf("rain", "rains", "rained", "is raining"),
                correctAnswer = "rains",
                explanation = "In a Type 1 conditional sentence, the 'if' clause uses the present simple tense.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 17 (Vocabulary - Contextual Meaning)
        quizList.add(
            QuizQuestion(
                question = "He had to _____ his trip due to illness.",
                options = listOf("carry on", "put off", "take off", "set up"),
                correctAnswer = "put off",
                explanation = "To 'put off' means to postpone or delay.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 18 (Grammar - Relative Clauses)
        quizList.add(
            QuizQuestion(
                question = "This is the house _____ I was born.",
                options = listOf("which", "who", "where", "that"),
                correctAnswer = "where",
                explanation = "Use 'where' to refer to a place.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 19 (Vocabulary - Idioms)
        quizList.add(
            QuizQuestion(
                question = "He felt _____ after failing the exam.",
                options = listOf("on top of the world", "under the weather", "down in the dumps", "over the moon"),
                correctAnswer = "down in the dumps",
                explanation = "To be 'down in the dumps' means to feel sad or depressed.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 20 (Grammar - Modals)
        quizList.add(
            QuizQuestion(
                question = "You _____ smoke in the hospital.",
                options = listOf("should", "can", "mustn't", "may"),
                correctAnswer = "mustn't",
                explanation = "'Mustn't' is used to express a strong prohibition.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Mức độ ứng dụng (Application Level) & Nâng cao (Advanced Level)

        // Question 21 (Vocabulary - Formal/Informal)
        quizList.add(
            QuizQuestion(
                question = "Choose the most formal word: 'get in touch with'.",
                options = listOf("contact", "call", "text", "chat"),
                correctAnswer = "contact",
                explanation = "'Contact' is a more formal synonym for 'get in touch with'.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 22 (Grammar - Reported Speech)
        quizList.add(
            QuizQuestion(
                question = "She said, 'I am going to the store.' (Reported Speech)",
                options = listOf(
                    "She said she is going to the store.",
                    "She said I was going to the store.",
                    "She said she was going to the store.",
                    "She said she had gone to the store."
                ),
                correctAnswer = "She said she was going to the store.",
                explanation = "When reporting speech, the tense usually shifts back. 'Am going' becomes 'was going'.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 23 (Vocabulary - Abstract Nouns)
        quizList.add(
            QuizQuestion(
                question = "The police are investigating the cause of the _____.",
                options = listOf("accident", "accidental", "accidentally", "accidents"),
                correctAnswer = "accident",
                explanation = "'Accident' is the noun form needed here.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 24 (Grammar - Inversion)
        quizList.add(
            QuizQuestion(
                question = "_____ had I seen such a beautiful sunset.",
                options = listOf("Never", "Ever", "Already", "Just"),
                correctAnswer = "Never",
                explanation = "When 'Never' is placed at the beginning of a sentence for emphasis, inversion is used (auxiliary verb before the subject).",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 25 (Vocabulary - Collocations)
        quizList.add(
            QuizQuestion(
                question = "It's important to _____ a balance between work and life.",
                options = listOf("make", "do", "find", "keep"),
                correctAnswer = "find",
                explanation = "The common collocation is 'find a balance'.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 26 (Grammar - Conditional Sentences - Type 3)
        quizList.add(
            QuizQuestion(
                question = "If you _____ me, I _____ in trouble.",
                options = listOf(
                    "had helped / wouldn't have been",
                    "helped / wouldn't be",
                    "had helped / wouldn't be",
                    "helped / wouldn't have been"
                ),
                correctAnswer = "had helped / wouldn't have been",
                explanation = "This is a Type 3 conditional sentence, used for hypothetical situations in the past. Structure: If + past perfect, would have + past participle.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 27 (Vocabulary - Figurative Language)
        quizList.add(
            QuizQuestion(
                question = "The news hit him like a ton of bricks. This is an example of a _____.",
                options = listOf("simile", "metaphor", "personification", "hyperbole"),
                correctAnswer = "simile",
                explanation = "A simile is a figure of speech that directly compares two different things, usually by employing the words 'like' or 'as'.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 28 (Grammar - Subjunctive Mood)
        quizList.add(
            QuizQuestion(
                question = "It is essential that she _____ the report by tomorrow.",
                options = listOf("finishes", "finish", "finished", "will finish"),
                correctAnswer = "finish",
                explanation = "In clauses following expressions like 'it is essential that', the subjunctive mood is often used, which is the base form of the verb.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 29 (Vocabulary - Advanced Verbs)
        quizList.add(
            QuizQuestion(
                question = "The company decided to _____ its operations in the struggling market.",
                options = listOf("expand", "curtail", "initiate", "reinforce"),
                correctAnswer = "curtail",
                explanation = "To 'curtail' means to reduce or limit something.",
                tag = Random.nextInt(1, 10)
            )
        )

        // Question 30 (Grammar - Participle Clauses)
        quizList.add(
            QuizQuestion(
                question = "_____ by the noise, she couldn't concentrate.",
                options = listOf("Disturbing", "Disturbed", "To disturb", "Having disturb"),
                correctAnswer = "Disturbed",
                explanation = "This is a past participle clause used to give more information about the subject. 'Disturbed' indicates that she was affected by the noise.",
                tag = Random.nextInt(1, 10)
            )
        )
        return quizList
    }
}
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String,
    val tag: Int
)




