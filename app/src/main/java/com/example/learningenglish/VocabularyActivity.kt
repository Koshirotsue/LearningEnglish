//package com.example.learningenglish
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.learningenglish.adapter.VocabularyAdapter
//import com.example.learningenglish.database.VocabularyDatabaseHelper
//import com.example.learningenglish.model.Vocabulary
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.google.android.material.textfield.TextInputEditText
//
//class VocabularyActivity : AppCompatActivity() {
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var fabAddWord: FloatingActionButton
//    private lateinit var dbHelper: VocabularyDatabaseHelper
//    private lateinit var adapter: VocabularyAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_vocabulary)
//
//        // Initialize views
//        recyclerView = findViewById(R.id.rvVocabulary)
//        fabAddWord = findViewById(R.id.fabAddWord)
//        dbHelper = VocabularyDatabaseHelper(this)
//
//        // Setup RecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        adapter = VocabularyAdapter(emptyList()) { vocabulary ->
//            showOptionsDialog(vocabulary)
//        }
//        recyclerView.adapter = adapter
//
//        // Load initial data
//        loadVocabulary()
//
//        // Setup FAB click listener
//        fabAddWord.setOnClickListener {
//            showAddVocabularyDialog()
//        }
//    }
//
//    private fun showOptionsDialog(vocabulary: Vocabulary) {
//        val options = arrayOf("Đánh dấu đã đọc", "Sửa", "Xóa")
//        AlertDialog.Builder(this)
//            .setTitle("Tùy chọn")
//            .setItems(options) { _, which ->
//                when (which) {
//                    0 -> {
//                        vocabulary.isRead = !vocabulary.isRead
//                        dbHelper.toggleReadStatus(vocabulary.id, vocabulary.isRead)
//                        loadVocabulary()
//                    }
//                    1 -> showEditVocabularyDialog(vocabulary)
//                    2 -> showDeleteConfirmationDialog(vocabulary)
//                }
//            }
//            .show()
//    }
//
//    private fun showEditVocabularyDialog(vocabulary: Vocabulary) {
//        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_vocabulary, null)
//        val etWord = dialogView.findViewById<TextInputEditText>(R.id.etWord)
//        val etPhonetic = dialogView.findViewById<TextInputEditText>(R.id.etPhonetic)
//        val etMeaning = dialogView.findViewById<TextInputEditText>(R.id.etMeaning)
//        val etExample = dialogView.findViewById<TextInputEditText>(R.id.etExample)
//
//        // Set current values
//        etWord.setText(vocabulary.word)
//        etPhonetic.setText(vocabulary.phonetic)
//        etMeaning.setText(vocabulary.meaning)
//        etExample.setText(vocabulary.example)
//
//        AlertDialog.Builder(this)
//            .setTitle("Sửa từ vựng")
//            .setView(dialogView)
//            .setPositiveButton("Lưu") { _, _ ->
//                val word = etWord.text.toString().trim()
//                val phonetic = etPhonetic.text.toString().trim()
//                val meaning = etMeaning.text.toString().trim()
//                val example = etExample.text.toString().trim()
//
//                if (word.isNotEmpty() && meaning.isNotEmpty()) {
//                    val updatedVocabulary = vocabulary.copy(
//                        word = word,
//                        phonetic = phonetic,
//                        meaning = meaning,
//                        example = example
//                    )
//                    dbHelper.updateVocabulary(updatedVocabulary)
//                    loadVocabulary()
//                    Toast.makeText(this, "Đã cập nhật từ vựng", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .setNegativeButton("Hủy", null)
//            .show()
//    }
//
//    private fun showDeleteConfirmationDialog(vocabulary: Vocabulary) {
//        AlertDialog.Builder(this)
//            .setTitle("Xóa từ vựng")
//            .setMessage("Bạn có chắc chắn muốn xóa từ '${vocabulary.word}'?")
//            .setPositiveButton("Xóa") { _, _ ->
//                dbHelper.deleteVocabulary(vocabulary.id)
//                loadVocabulary()
//                Toast.makeText(this, "Đã xóa từ vựng", Toast.LENGTH_SHORT).show()
//            }
//            .setNegativeButton("Hủy", null)
//            .show()
//    }
//
//    private fun showAddVocabularyDialog() {
//        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_vocabulary, null)
//        val etWord = dialogView.findViewById<TextInputEditText>(R.id.etWord)
//        val etPhonetic = dialogView.findViewById<TextInputEditText>(R.id.etPhonetic)
//        val etMeaning = dialogView.findViewById<TextInputEditText>(R.id.etMeaning)
//        val etExample = dialogView.findViewById<TextInputEditText>(R.id.etExample)
//
//        AlertDialog.Builder(this)
//            .setTitle("Thêm từ mới")
//            .setView(dialogView)
//            .setPositiveButton("Thêm") { _, _ ->
//                val word = etWord.text.toString().trim()
//                val phonetic = etPhonetic.text.toString().trim()
//                val meaning = etMeaning.text.toString().trim()
//                val example = etExample.text.toString().trim()
//
//                if (word.isNotEmpty() && meaning.isNotEmpty()) {
//                    val vocabulary = Vocabulary(
//                        word = word,
//                        phonetic = phonetic,
//                        meaning = meaning,
//                        example = example
//                    )
//                    dbHelper.addVocabulary(vocabulary)
//                    loadVocabulary()
//                    Toast.makeText(this, "Đã thêm từ mới", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .setNegativeButton("Hủy", null)
//            .show()
//    }
//
//    private fun loadVocabulary() {
//        val vocabularyList = dbHelper.getAllVocabulary()
//        adapter.updateData(vocabularyList)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        loadVocabulary()
//    }
//}