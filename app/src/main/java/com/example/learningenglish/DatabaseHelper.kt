package com.example.learningenglish

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "quiz_data.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
    }
}