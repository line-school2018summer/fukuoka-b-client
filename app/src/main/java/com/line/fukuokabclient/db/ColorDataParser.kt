package com.line.fukuokabclient.db

import android.graphics.Color
import org.jetbrains.anko.db.MapRowParser

class ColorDataParser: MapRowParser<ColorData> {
    override fun parseRow(columns: Map<String, Any?>): ColorData {
        return ColorData(columns["channelId"] as Long, columns["userId"] as Long, Color.parseColor(columns["colorCode"] as String))
    }
}