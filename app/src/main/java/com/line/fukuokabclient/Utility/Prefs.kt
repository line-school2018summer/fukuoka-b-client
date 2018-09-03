package com.line.fukuokabclient.Utility

import android.content.Context
import android.content.SharedPreferences

class Prefs(val context: Context) {
    companion object {
        private const val PREFERENCE_FILE_KEY = "com.line.fukuokabclient.PREFERENCE"
        fun get(context: Context): SharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
        fun edit(context: Context): SharedPreferences.Editor = get(context).edit()
    }
}