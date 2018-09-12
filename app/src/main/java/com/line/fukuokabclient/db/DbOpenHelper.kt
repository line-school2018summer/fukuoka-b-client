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
                "senderId" to INTEGER + PRIMARY_KEY,
                "channelId" to INTEGER + PRIMARY_KEY,
                "colorCode" to TEXT)

//        db?.insert("balloonColor",
//                "id" to 1,
//                "senderId" to 1,
//                "channelId" to 1,
//                "colorCode" to "#d4d4d4")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable("balloonColor", ifExists = true)
    }


}