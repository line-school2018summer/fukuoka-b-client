package com.line.fukuokabclient.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DbOpenHelper(context: Context): ManagedSQLiteOpenHelper(context, "clientDB", null, 1) {
    companion object {
        private var instance: DbOpenHelper? = null

        @Synchronized
        fun getInstance(context: Context?): DbOpenHelper {
            if (instance == null) {
                instance = DbOpenHelper(context!!.getApplicationContext())
            }

            return instance!!
        }

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable("balloonColor", true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "senderId" to TEXT,
                "channelId" to TEXT,
                "colorCode" to TEXT)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        db?.dropTable()
    }


}