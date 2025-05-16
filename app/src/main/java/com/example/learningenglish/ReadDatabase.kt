package com.example.learningenglish

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.io.File
import java.io.FileOutputStream


class ReadDatabase (private val context: Context) {
    companion object{
        private val DB_NAME = "quiz_data.db"
    }
    fun openQuizDatabase(): SQLiteDatabase{
        val dbFile = context.getDatabasePath(DB_NAME)
        val file = File(dbFile.toString())
        if(file.exists()){
            Log.e("quiz_data", "file da ton tai")
        }
        else{
            copyDatabase(dbFile)
        }
        return SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READWRITE)
    }
    private fun copyDatabase(dbFile: File) {
        val openDB = context.assets.open(DB_NAME)
        val outputStream = FileOutputStream(dbFile)
        val buffer = ByteArray(1024)
        while (openDB.read(buffer)>0){
            outputStream.write((buffer))
        }
        outputStream.flush()
        outputStream.close()
        openDB.close()
    }

}