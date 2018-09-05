package com.line.fukuokabclient.Client

import com.line.fukuokabclient.Client.Response.ResponsePersonalChannelInfo
import com.line.fukuokabclient.dto.ChannelDTO
import com.line.fukuokabclient.dto.MessageDTO
import retrofit2.http.*
import rx.Observable

interface ChannelClient {
    @GET("chat/public")
    fun getPublicChannel(): Observable<List<ChannelDTO>>

    @GET("chat/personal/{userId}/{friendId}")
    fun getPersonalChannel(@Path("userId")userId: Long, @Path("friendId") friendId: Long): Observable<ResponsePersonalChannelInfo>

    @GET("chat/messages/{channelId}")
    fun getMessages(@Path("channelId") channelId: Long): Observable<List<MessageDTO>>

    @Headers("Accept: application/json",
            "Content-type: application/json")
    @POST("chat/group/new")
    fun newGroupChannel(@Body body: HashMap<String, List<Long>>): Observable<ResponsePersonalChannelInfo>
}