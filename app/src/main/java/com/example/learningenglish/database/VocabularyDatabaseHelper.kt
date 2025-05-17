package com.example.learningenglish.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.learningenglish.model.Vocabulary

class VocabularyDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "vocabulary.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_VOCABULARY = "vocabulary"
        private const val COLUMN_ID = "id"
        private const val COLUMN_WORD = "word"
        private const val COLUMN_PHONETIC = "phonetic"
        private const val COLUMN_MEANING = "meaning"
        private const val COLUMN_EXAMPLE = "example"
        private const val COLUMN_IS_READ = "is_read"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_VOCABULARY (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_WORD TEXT NOT NULL,
                $COLUMN_PHONETIC TEXT,
                $COLUMN_MEANING TEXT NOT NULL,
                $COLUMN_EXAMPLE TEXT,
                $COLUMN_IS_READ INTEGER DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_VOCABULARY")
        onCreate(db)
    }

    fun addVocabulary(vocabulary: Vocabulary): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_WORD, vocabulary.word)
            put(COLUMN_PHONETIC, vocabulary.phonetic)
            put(COLUMN_MEANING, vocabulary.meaning)
            put(COLUMN_EXAMPLE, vocabulary.example)
            put(COLUMN_IS_READ, if (vocabulary.isRead) 1 else 0)
        }
        return db.insert(TABLE_VOCABULARY, null, values)
    }

    fun getAllVocabulary(): List<Vocabulary> {
        val vocabularyList = mutableListOf<Vocabulary>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_VOCABULARY,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_WORD ASC"
        )

        with(cursor) {
            while (moveToNext()) {
                val vocabulary = Vocabulary(
                    id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                    word = getString(getColumnIndexOrThrow(COLUMN_WORD)),
                    phonetic = getString(getColumnIndexOrThrow(COLUMN_PHONETIC)),
                    meaning = getString(getColumnIndexOrThrow(COLUMN_MEANING)),
                    example = getString(getColumnIndexOrThrow(COLUMN_EXAMPLE)),
                    isRead = getInt(getColumnIndexOrThrow(COLUMN_IS_READ)) == 1
                )
                vocabularyList.add(vocabulary)
            }
        }
        cursor.close()
        return vocabularyList
    }

    fun updateVocabulary(vocabulary: Vocabulary): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_WORD, vocabulary.word)
            put(COLUMN_PHONETIC, vocabulary.phonetic)
            put(COLUMN_MEANING, vocabulary.meaning)
            put(COLUMN_EXAMPLE, vocabulary.example)
            put(COLUMN_IS_READ, if (vocabulary.isRead) 1 else 0)
        }
        return db.update(
            TABLE_VOCABULARY,
            values,
            "$COLUMN_ID = ?",
            arrayOf(vocabulary.id.toString())
        )
    }

    fun toggleReadStatus(id: Int, isRead: Boolean): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IS_READ, if (isRead) 1 else 0)
        }
        return db.update(
            TABLE_VOCABULARY,
            values,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun deleteVocabulary(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_VOCABULARY, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
} 