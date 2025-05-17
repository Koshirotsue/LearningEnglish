package com.example.learningenglish.model

data class Vocabulary(
    val id: Int = 0,
    val word: String,
    val phonetic: String,
    val meaning: String,
    val example: String,
    var isRead: Boolean = false
) 