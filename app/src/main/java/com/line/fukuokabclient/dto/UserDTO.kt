package com.line.fukuokabclient.dto

import android.os.Parcel
import android.os.Parcelable

data class UserDTO(
        var id: Long,
        var name: String,
        var userId: String,
        var mail: String
        ) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(userId)
        parcel.writeString(mail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDTO> {
        override fun createFromParcel(parcel: Parcel): UserDTO {
            return UserDTO(parcel)
        }

        override fun newArray(size: Int): Array<UserDTO?> {
            return arrayOfNulls(size)
        }
    }
}