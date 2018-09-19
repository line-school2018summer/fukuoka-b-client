package com.line.fukuokabclient.Client

import com.line.fukuokabclient.Client.Response.ResponseChannelInfo
import com.line.fukuokabclient.dto.ChannelDTO
import com.line.fukuokabclient.dto.MessageDTO
import retrofit2.http.*
import rx.Observable

interface ChannelClient {
    @GET("chat/public")
    fun getPublicChannel(): Observable<List<ChannelDTO>>

    @GET("chat/{userId}/channels")
    fun getMyChannels(@Path("userId") userId: Long): Observable<List<ChannelDTO>>

    @GET("chat/personal/{userId}/{friendId}")
    fun getPersonalChannel(@Path("userId")userId: Long, @Path("friendId") friendId: Long): Observable<ResponseChannelInfo>

    @GET("chat/messages/{channelId}")
    fun getMessages(@Path("channelId") channelId: Long): Observable<List<MessageDTO>>

    @Headers("Accept: application/json",
            "Content-type: application/json")
    @POST("chat/group/new")
    fun newGroupChannel(@Body body: HashMap<String, List<Long>>): Observable<ResponseChannelInfo>

    @GET("chat/{channelId}/info")
    fun getChannelInfo(@Path("channelId") channelId: Long): Observable<ResponseChannelInfo>

    @Headers("Accept: application/json",
            "Content-type: application/json")
    @POST("chat/{channelId}/update/name")
    fun updateChannelName(@Path("channelId")channelId: Long, @Body body: HashMap<String, String>): Observable<Void>
}