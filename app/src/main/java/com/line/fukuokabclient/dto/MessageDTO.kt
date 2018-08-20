package com.line.fukuokabclient.dto

import java.sql.Timestamp

data class MessageDTO (
        var id: Long?,
        var senderId: Long,
        var roomId: Long,
        var text: String,
        var sendAt: Timestamp?
) {
    override fun toString(): String {
        return super.toString()
    }
}