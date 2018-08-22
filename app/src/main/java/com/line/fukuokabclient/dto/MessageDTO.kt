package com.line.fukuokabclient.dto

import java.sql.Timestamp

data class MessageDTO (
        var id: Long?,
        var senderId: Long,
        var channelId: Long,
        var content: String,
        var createdAt: Timestamp?
) {
    override fun toString(): String {
        return "{\"senderId\": \"$senderId\", \"channelId\": \"$channelId\", \"content\": \"$content\"}"
    }
}