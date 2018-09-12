package com.line.fukuokabclient.Client

import com.line.fukuokabclient.dto.UserDTO
import retrofit2.http.GET
import rx.Observable

interface APIClient {
    @GET("/test")
    fun getStatus(): Observable<UserDTO>
}