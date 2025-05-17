package com.example.learningenglish.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.learningenglish.R
import com.example.learningenglish.model.Grammar

class GrammarAdapter(
    private var grammarList: List<Grammar>,
    private val onItemClick: (Grammar) -> Unit
) : RecyclerView.Adapter<GrammarAdapter.GrammarViewHolder>() {

    class GrammarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvExample: TextView = itemView.findViewById(R.id.tvExample)
        val ivReadStatus: ImageView = itemView.findViewById(R.id.ivReadStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grammar, parent, false)
        return GrammarViewHolder(view)
    }

    override fun onBindViewHolder(holder: GrammarViewHolder, position: Int) {
        val grammar = grammarList[position]
        holder.tvTitle.text = grammar.title
        holder.tvDescription.text = grammar.description
        holder.tvExample.text = grammar.example

        // Change color based on read status
        if (grammar.isRead) {
            holder.tvTitle.setTextColor(Color.parseColor("#4CAF50"))
            holder.ivReadStatus.visibility = View.VISIBLE
        } else {
            holder.tvTitle.setTextColor(Color.parseColor("#2196F3"))
            holder.ivReadStatus.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClick(grammar)
        }
    }

    override fun getItemCount() = grammarList.size

    fun updateData(newList: List<Grammar>) {
        grammarList = newList
        notifyDataSetChanged()
    }
} 