package com.example.learningenglish.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learningenglish.R
import com.example.learningenglish.model.Vocabulary

class VocabularyAdapter(
    private var vocabularyList: List<Vocabulary>,
    private val onItemClick: (Vocabulary) -> Unit
) : RecyclerView.Adapter<VocabularyAdapter.VocabularyViewHolder>() {

    class VocabularyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWord: TextView = itemView.findViewById(R.id.tvWord)
        val tvPhonetic: TextView = itemView.findViewById(R.id.tvPhonetic)
        val tvMeaning: TextView = itemView.findViewById(R.id.tvMeaning)
        val tvExample: TextView = itemView.findViewById(R.id.tvExample)
        val ivReadStatus: ImageView = itemView.findViewById(R.id.ivReadStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabularyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vocabulary, parent, false)
        return VocabularyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VocabularyViewHolder, position: Int) {
        val vocabulary = vocabularyList[position]
        holder.apply {
            tvWord.text = vocabulary.word
            tvPhonetic.text = vocabulary.phonetic
            tvMeaning.text = vocabulary.meaning
            tvExample.text = vocabulary.example

            // Change colors and show/hide check icon based on read status
            if (vocabulary.isRead) {
                tvWord.setTextColor(Color.parseColor("#4CAF50")) // Green color for read items
                tvPhonetic.setTextColor(Color.parseColor("#757575"))
                tvMeaning.setTextColor(Color.parseColor("#4CAF50"))
                tvExample.setTextColor(Color.parseColor("#616161"))
                ivReadStatus.visibility = View.VISIBLE
            } else {
                tvWord.setTextColor(Color.parseColor("#2196F3")) // Blue color for unread items
                tvPhonetic.setTextColor(Color.parseColor("#757575"))
                tvMeaning.setTextColor(Color.parseColor("#212121"))
                tvExample.setTextColor(Color.parseColor("#616161"))
                ivReadStatus.visibility = View.GONE
            }

            itemView.setOnClickListener { onItemClick(vocabulary) }
        }
    }

    override fun getItemCount() = vocabularyList.size

    fun updateData(newList: List<Vocabulary>) {
        vocabularyList = newList
        notifyDataSetChanged()
    }
} 