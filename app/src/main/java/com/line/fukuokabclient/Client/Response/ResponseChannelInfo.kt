package com.line.fukuokabclient.Client.Response

import android.os.Parcel
import android.os.Parcelable
import com.line.fukuokabclient.dto.ChannelDTO
import com.line.fukuokabclient.dto.UserDTO

data class ResponseChannelInfo (
        val users: List<UserDTO>,
        val channel: ChannelDTO
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(UserDTO),
            parcel.readParcelable(ChannelDTO::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(users)
        parcel.writeParcelable(channel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseChannelInfo> {
        override fun createFromParcel(parcel: Parcel): ResponseChannelInfo {
            return ResponseChannelInfo(parcel)
        }

        override fun newArray(size: Int): Array<ResponseChannelInfo?> {
            return arrayOfNulls(size)
        }
    }
}