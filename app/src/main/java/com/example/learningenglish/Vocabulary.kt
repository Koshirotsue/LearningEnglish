package com.example.learningenglish

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learningenglish.adapter.VocabularyAdapter
import com.example.learningenglish.database.VocabularyDatabaseHelper
import com.example.learningenglish.model.Vocabulary
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class VocabularyActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddWord: FloatingActionButton
    private lateinit var dbHelper: VocabularyDatabaseHelper
    private lateinit var adapter: VocabularyAdapter
    private lateinit var sharedPreferences: SharedPreferences // Thêm SharedPreferences

    // Tên cho SharedPreferences và key để kiểm tra trạng thái chèn dữ liệu
    private val PREFS_NAME = "AppPrefs"
    private val KEY_INITIAL_DATA_INSERTED = "initial_data_inserted"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vocabulary)

        // Initialize views
        recyclerView = findViewById(R.id.rvVocabulary)
        fabAddWord = findViewById(R.id.fabAddWord)
        dbHelper = VocabularyDatabaseHelper(this)
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) // Khởi tạo SharedPreferences

        // Chèn dữ liệu ban đầu nếu chưa được chèn
        insertInitialVocabulary()

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = VocabularyAdapter(emptyList()) { vocabulary ->
            // Toggle read status when item is clicked
            vocabulary.isRead = !vocabulary.isRead
            dbHelper.toggleReadStatus(vocabulary.id, vocabulary.isRead)
            loadVocabulary() // Tải lại dữ liệu sau khi cập nhật trạng thái đọc
        }
        recyclerView.adapter = adapter

        // Load initial data to display
        loadVocabulary()

        // Setup FAB click listener
        fabAddWord.setOnClickListener {
            showAddVocabularyDialog()
        }
    }

    // Hàm để chèn dữ liệu từ vựng ban đầu
    private fun insertInitialVocabulary() {
        val isInitialDataInserted = sharedPreferences.getBoolean(KEY_INITIAL_DATA_INSERTED, false)
        if (!isInitialDataInserted) {
            val initialVocabularyList = listOf(
                Vocabulary(word = "Hello", phonetic = "/həˈloʊ/", meaning = "Xin chào", example = "Hello, how are you?"),
                Vocabulary(word = "Goodbye", phonetic = "/ˌɡʊdˈbaɪ/", meaning = "Tạm biệt", example = "Goodbye, see you tomorrow."),
                Vocabulary(word = "Thank you", phonetic = "/ˌθæŋk ˈjuː/", meaning = "Cảm ơn", example = "Thank you for your help."),
                Vocabulary(word = "Please", phonetic = "/pliːz/", meaning = "Làm ơn", example = "Please help me."),
                Vocabulary(word = "Sorry", phonetic = "/ˈsɒri/", meaning = "Xin lỗi", example = "Sorry, I'm late."),
                // Thêm 20 từ vựng mới
                Vocabulary(word = "Yes", phonetic = "/jes/", meaning = "Vâng, có", example = "Yes, I understand."),
                Vocabulary(word = "No", phonetic = "/noʊ/", meaning = "Không", example = "No, I don't agree."),
                Vocabulary(word = "Excuse me", phonetic = "/ɪkˈskjuːs miː/", meaning = "Xin lỗi (khi làm phiền/hỏi đường)", example = "Excuse me, where is the library?"),
                Vocabulary(word = "Good morning", phonetic = "/ˌɡʊd ˈmɔːrnɪŋ/", meaning = "Chào buổi sáng", example = "Good morning, everyone."),
                Vocabulary(word = "Good afternoon", phonetic = "/ˌɡʊd ˈæftərnuːn/", meaning = "Chào buổi chiều", example = "Good afternoon, class."),
                Vocabulary(word = "Good evening", phonetic = "/ˌɡʊd ˈiːvnɪŋ/", meaning = "Chào buổi tối", example = "Good evening, sir."),
                Vocabulary(word = "Good night", phonetic = "/ˌɡʊd ˈnaɪt/", meaning = "Chúc ngủ ngon", example = "Good night, sleep well."),
                Vocabulary(word = "How are you?", phonetic = "/haʊ ər juː?/", meaning = "Bạn khỏe không?", example = "Hi, John. How are you?"),
                Vocabulary(word = "I'm fine, thank you", phonetic = "/aɪm faɪn, θæŋk juː/", meaning = "Tôi khỏe, cảm ơn", example = "How are you? I'm fine, thank you."),
                Vocabulary(word = "What's your name?", phonetic = "/wɒts jər neɪm?/", meaning = "Tên bạn là gì?", example = "Excuse me, what's your name?"),
                Vocabulary(word = "My name is...", phonetic = "/maɪ neɪm ɪz.../", meaning = "Tên tôi là...", example = "What's your name? My name is Sarah."),
                Vocabulary(word = "Nice to meet you", phonetic = "/naɪs tu miːt juː/", meaning = "Rất vui được gặp bạn", example = "Nice to meet you, too."),
                Vocabulary(word = "How old are you?", phonetic = "/haʊ oʊld ər juː?/", meaning = "Bạn bao nhiêu tuổi?", example = "How old are you? I'm 25."),
                Vocabulary(word = "Where are you from?", phonetic = "/wer ər juː frɒm?/", meaning = "Bạn đến từ đâu?", example = "Where are you from? I'm from Vietnam."),
                Vocabulary(word = "I am from...", phonetic = "/aɪ æm frɒm.../", meaning = "Tôi đến từ...", example = "I am from Hanoi."),
                Vocabulary(word = "Do you speak English?", phonetic = "/duː juː spiːk ˈɪŋɡlɪʃ?/", meaning = "Bạn nói tiếng Anh không?", example = "Do you speak English? Yes, a little."),
                Vocabulary(word = "I don't understand", phonetic = "/aɪ doʊnt ˌʌndərˈstænd/", meaning = "Tôi không hiểu", example = "Could you repeat that? I don't understand."),
                Vocabulary(word = "Can you help me?", phonetic = "/kæn juː help miː?/", meaning = "Bạn giúp tôi được không?", example = "Excuse me, can you help me?"),
                Vocabulary(word = "How much is this?", phonetic = "/haʊ mʌtʃ ɪz ðɪs?/", meaning = "Cái này bao nhiêu tiền?", example = "How much is this apple?"),
                Vocabulary(word = "Where is...?", phonetic = "/wer ɪz...?/", meaning = "...ở đâu?", example = "Where is the train station?")
            )

            for (vocabulary in initialVocabularyList) {
                dbHelper.addVocabulary(vocabulary)
            }

            with(sharedPreferences.edit()) {
                putBoolean(KEY_INITIAL_DATA_INSERTED, true)
                apply()
            }

        }
    }

    private fun showAddVocabularyDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_vocabulary, null)
        val etWord = dialogView.findViewById<TextInputEditText>(R.id.etWord)
        val etPhonetic = dialogView.findViewById<TextInputEditText>(R.id.etPhonetic)
        val etMeaning = dialogView.findViewById<TextInputEditText>(R.id.etMeaning)
        val etExample = dialogView.findViewById<TextInputEditText>(R.id.etExample)

        AlertDialog.Builder(this)
            .setTitle("Thêm từ mới")
            .setView(dialogView)
            .setPositiveButton("Thêm") { _, _ ->
                val word = etWord.text.toString().trim()
                val phonetic = etPhonetic.text.toString().trim()
                val meaning = etMeaning.text.toString().trim()
                val example = etExample.text.toString().trim()

                if (word.isNotEmpty() && meaning.isNotEmpty()) {
                    val vocabulary = Vocabulary(
                        word = word,
                        phonetic = phonetic,
                        meaning = meaning,
                        example = example
                    )
                    dbHelper.addVocabulary(vocabulary)
                    loadVocabulary()
                    Toast.makeText(this, "Đã thêm từ mới", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin (Từ và Nghĩa)", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun loadVocabulary() {
        val vocabularyList = dbHelper.getAllVocabulary()
        adapter.updateData(vocabularyList)
    }

    override fun onResume() {
        super.onResume()
        loadVocabulary() // Tải lại dữ liệu khi Activity được resume
    }
}
