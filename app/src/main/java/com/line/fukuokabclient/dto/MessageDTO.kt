package com.line.fukuokabclient.dto

import android.os.Parcel
import android.os.Parcelable
import java.sql.Timestamp

data class MessageDTO (
        var id: Long?,
        var senderId: Long,
        var channelId: Long,
        var content: String,
        var createdAt: Timestamp?
) :Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString(),
            Timestamp(parcel.readLong())) {
    }

    override fun toString(): String {
        return "{\"senderId\": \"$senderId\", \"channelId\": \"$channelId\", \"content\": \"$content\"}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeLong(senderId)
        parcel.writeLong(channelId)
        parcel.writeString(content)
        parcel.writeLong(createdAt!!.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageDTO> {
        override fun createFromParcel(parcel: Parcel): MessageDTO {
            return MessageDTO(parcel)
        }

        override fun newArray(size: Int): Array<MessageDTO?> {
            return arrayOfNulls(size)
        }
    }
}