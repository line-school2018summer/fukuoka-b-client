package com.line.fukuokabclient.Client.Response

import android.os.Parcel
import android.os.Parcelable
import com.line.fukuokabclient.dto.ChannelDTO
import com.line.fukuokabclient.dto.MessageDTO
import com.line.fukuokabclient.dto.UserDTO

data class ResponseChannelInfo (
        val users: List<UserDTO>,
        val channel: ChannelDTO,
        var messages: ArrayList<MessageDTO>
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(UserDTO)!!,
            parcel.readParcelable(ChannelDTO::class.java.classLoader)!!,
            parcel.createTypedArrayList(MessageDTO)!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(users)
        parcel.writeParcelable(channel, flags)
        parcel.writeTypedList(messages)
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