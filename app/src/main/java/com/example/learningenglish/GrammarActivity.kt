package com.example.learningenglish

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learningenglish.adapter.GrammarAdapter
import com.example.learningenglish.database.GrammarDatabaseHelper
import com.example.learningenglish.model.Grammar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class GrammarActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddGrammar: FloatingActionButton
    private lateinit var dbHelper: GrammarDatabaseHelper
    private lateinit var adapter: GrammarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar)

        // Initialize views
        recyclerView = findViewById(R.id.rvGrammar)
        fabAddGrammar = findViewById(R.id.fabAddGrammar)
        dbHelper = GrammarDatabaseHelper(this)

        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = GrammarAdapter(emptyList()) { grammar ->
            showOptionsDialog(grammar)
        }
        recyclerView.adapter = adapter

        // Load initial data
        loadGrammar()

        // Setup FAB click listener
        fabAddGrammar.setOnClickListener {
            showAddGrammarDialog()
        }
    }

    private fun showOptionsDialog(grammar: Grammar) {
        val options = arrayOf("Đánh dấu đã đọc", "Sửa", "Xóa")
        AlertDialog.Builder(this)
            .setTitle("Tùy chọn")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        grammar.isRead = !grammar.isRead
                        dbHelper.toggleReadStatus(grammar.id, grammar.isRead)
                        loadGrammar()
                    }
                    1 -> showEditGrammarDialog(grammar)
                    2 -> showDeleteConfirmationDialog(grammar)
                }
            }
            .show()
    }

    private fun showEditGrammarDialog(grammar: Grammar) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_grammar, null, false)
        val etTitle = dialogView.findViewById<TextInputEditText>(R.id.etTitle)
        val etDescription = dialogView.findViewById<TextInputEditText>(R.id.etDescription)
        val etExample = dialogView.findViewById<TextInputEditText>(R.id.etExample)

        // Set current values
        etTitle.setText(grammar.title)
        etDescription.setText(grammar.description)
        etExample.setText(grammar.example)

        AlertDialog.Builder(this)
            .setTitle("Sửa ngữ pháp")
            .setView(dialogView)
            .setPositiveButton("Lưu") { _, _ ->
                val title = etTitle.text.toString().trim()
                val description = etDescription.text.toString().trim()
                val example = etExample.text.toString().trim()

                if (title.isNotEmpty() && description.isNotEmpty()) {
                    val updatedGrammar = grammar.copy(
                        title = title,
                        description = description,
                        example = example
                    )
                    dbHelper.updateGrammar(updatedGrammar)
                    loadGrammar()
                    Toast.makeText(this, "Đã cập nhật ngữ pháp", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(grammar: Grammar) {
        AlertDialog.Builder(this)
            .setTitle("Xóa ngữ pháp")
            .setMessage("Bạn có chắc chắn muốn xóa '${grammar.title}'?")
            .setPositiveButton("Xóa") { _, _ ->
                dbHelper.deleteGrammar(grammar.id)
                loadGrammar()
                Toast.makeText(this, "Đã xóa ngữ pháp", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showAddGrammarDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_grammar, null, false)
        val etTitle = dialogView.findViewById<TextInputEditText>(R.id.etTitle)
        val etDescription = dialogView.findViewById<TextInputEditText>(R.id.etDescription)
        val etExample = dialogView.findViewById<TextInputEditText>(R.id.etExample)

        AlertDialog.Builder(this)
            .setTitle("Thêm ngữ pháp mới")
            .setView(dialogView)
            .setPositiveButton("Thêm") { _, _ ->
                val title = etTitle.text.toString().trim()
                val description = etDescription.text.toString().trim()
                val example = etExample.text.toString().trim()

                if (title.isNotEmpty() && description.isNotEmpty()) {
                    val grammar = Grammar(
                        title = title,
                        description = description,
                        example = example
                    )
                    dbHelper.addGrammar(grammar)
                    loadGrammar()
                    Toast.makeText(this, "Đã thêm ngữ pháp mới", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun loadGrammar() {
        val grammarList = dbHelper.getAllGrammar()
        adapter.updateData(grammarList)
    }

    override fun onResume() {
        super.onResume()
        loadGrammar()
    }
}