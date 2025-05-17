package com.example.learningenglish.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.learningenglish.model.Grammar

class GrammarDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "grammar.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "grammar"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_EXAMPLE = "example"
        private const val COLUMN_IS_READ = "is_read"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_EXAMPLE TEXT,
                $COLUMN_IS_READ INTEGER DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addGrammar(grammar: Grammar): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, grammar.title)
            put(COLUMN_DESCRIPTION, grammar.description)
            put(COLUMN_EXAMPLE, grammar.example)
            put(COLUMN_IS_READ, if (grammar.isRead) 1 else 0)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllGrammar(): List<Grammar> {
        val grammarList = mutableListOf<Grammar>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val description = getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val example = getString(getColumnIndexOrThrow(COLUMN_EXAMPLE))
                val isRead = getInt(getColumnIndexOrThrow(COLUMN_IS_READ)) == 1

                grammarList.add(Grammar(id, title, description, example, isRead))
            }
        }
        cursor.close()
        return grammarList
    }

    fun updateGrammar(grammar: Grammar) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, grammar.title)
            put(COLUMN_DESCRIPTION, grammar.description)
            put(COLUMN_EXAMPLE, grammar.example)
            put(COLUMN_IS_READ, if (grammar.isRead) 1 else 0)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(grammar.id.toString()))
    }

    fun deleteGrammar(id: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun toggleReadStatus(id: Long, isRead: Boolean) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IS_READ, if (isRead) 1 else 0)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
} 