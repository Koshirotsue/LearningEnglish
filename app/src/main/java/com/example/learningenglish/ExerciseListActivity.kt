package com.example.learningenglish

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExerciseListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chapterTitles: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_listview)

        chapterTitles = (1..9).map { "Chapter $it" }
        initializeViews()
        setupRecyclerView()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.exercise_recycler_view)
    }
    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = ChapterAdapter(chapterTitles) { chapterTitle ->
            val chapterNumber = chapterTitle.split(" ").last().toIntOrNull() ?: 0

            val intent = Intent(this, QuizActivity::class.java).apply {
                putExtra("CHAPTER_NUMBER", chapterNumber)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
    class ChapterAdapter(
        private val chapters: List<String>,
        private val onItemClick: (String) -> Unit // Lambda for click handling
    ) : RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>() {

        class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val chapterTitleTextView: TextView = itemView.findViewById(R.id.chapter_title_text_view)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false) // Đã sửa lại thành item_chapter
            return ChapterViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
            val chapterTitle = chapters[position]
            holder.chapterTitleTextView.text = chapterTitle
            holder.itemView.setOnClickListener {
                onItemClick(chapterTitle) // Call the lambda when clicked
            }
        }

        override fun getItemCount(): Int {
            return chapters.size
        }
    }
}
