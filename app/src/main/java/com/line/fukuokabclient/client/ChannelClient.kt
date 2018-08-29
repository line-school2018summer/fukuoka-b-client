package com.line.fukuokabclient.client

import com.line.fukuokabclient.dto.ChannelDTO
import retrofit2.http.GET
import rx.Observable

interface ChannelClient {
    @GET("chat/public")
    fun getPublucChannel(): Observable<List<ChannelDTO>>
}