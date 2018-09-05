package com.line.fukuokabclient.Client.Response

import android.os.Parcel
import android.os.Parcelable
import com.line.fukuokabclient.dto.ChannelDTO
import com.line.fukuokabclient.dto.UserDTO

data class ResponsePersonalChannelInfo(
        val friend: UserDTO,
        val channel: ChannelDTO
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(UserDTO::class.java.classLoader),
            parcel.readParcelable(ChannelDTO::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(friend, flags)
        parcel.writeParcelable(channel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponsePersonalChannelInfo> {
        override fun createFromParcel(parcel: Parcel): ResponsePersonalChannelInfo {
            return ResponsePersonalChannelInfo(parcel)
        }

        override fun newArray(size: Int): Array<ResponsePersonalChannelInfo?> {
            return arrayOfNulls(size)
        }
    }
}

