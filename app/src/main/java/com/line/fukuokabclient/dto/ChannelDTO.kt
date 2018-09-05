package com.line.fukuokabclient.dto

import android.os.Parcel
import android.os.Parcelable

data class ChannelDTO(
        var id: Long?,
        var name: String,
        var type: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChannelDTO> {
        override fun createFromParcel(parcel: Parcel): ChannelDTO {
            return ChannelDTO(parcel)
        }

        override fun newArray(size: Int): Array<ChannelDTO?> {
            return arrayOfNulls(size)
        }
    }
}