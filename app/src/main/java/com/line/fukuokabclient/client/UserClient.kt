package com.line.fukuokabclient.client
import com.line.fukuokabclient.dto.UserDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface UserClient {
    @GET("/user/{id}")
    fun API(@Path("id") id: Int): Call<UserDTO>

    @GET("/user/id/{mail}")
    fun getUserByMail(@Path("mail") mail: String): Observable<UserDTO>

    @GET("/user/userId/{userId}")
    fun getUserByUserId(@Path("userId") userId: String): Observable<UserDTO>

}