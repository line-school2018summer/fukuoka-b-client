package com.line.fukuokabclient.client
import com.line.fukuokabclient.dto.UserDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface UserClient {
    @GET("/user/{id}")
    fun API(@Path("id") id: Int): Call<UserDTO>
}