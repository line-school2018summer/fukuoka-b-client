package com.line.fukuokabclient.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DbOpenHelper(context: Context): ManagedSQLiteOpenHelper(context, "clientDB", null, 1) {
    private val MSG_COLOR_MAP: String = "msgColorMap"
    companion object {
        private var instance: DbOpenHelper? = null

        @Synchronized
        fun getInstance(context: Context): DbOpenHelper {
            return instance ?: DbOpenHelper(context.applicationContext)
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.run {
            createTable(MSG_COLOR_MAP, true, columns = *arrayOf(
                "channelId" to INTEGER,
                "userId" to INTEGER,
                "colorCode" to TEXT
            ))
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(MSG_COLOR_MAP, true)
    }

}