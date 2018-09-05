package com.line.fukuokabclient.client

import com.line.fukuokabclient.dto.ChannelDTO
import com.line.fukuokabclient.dto.MessageDTO
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface ChannelClient {
    @GET("chat/public")
    fun getPublicChannel(): Observable<List<ChannelDTO>>

    @GET("chat/personal/{userId}/{friendId}")
    fun getPersonalChannel(@Path("userId")userId: Long, @Path("friendId") friendId: Long): Observable<ResponsePersonalChannelInfo>

    @GET("chat/messages/{channelId}")
    fun getMessages(@Path("channelId") channelId: Long): Observable<List<MessageDTO>>
}