package com.example.learningenglish.model

data class Grammar(
    val id: Long = 0,
    val title: String,
    val description: String,
    val example: String,
    var isRead: Boolean = false
) 