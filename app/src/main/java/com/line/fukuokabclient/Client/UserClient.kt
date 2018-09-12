package com.line.fukuokabclient.Client
import com.line.fukuokabclient.dto.UserDTO
import retrofit2.Call
import retrofit2.http.*
import rx.Observable

interface UserClient {
    @GET("/user/{id}")
    fun getUserById(@Path("id") id: Int): Observable<UserDTO>

    @GET("/user/id/{mail}")
    fun getUserByMail(@Path("mail") mail: String): Observable<UserDTO>

    @GET("/user/userId/{userId}")
    fun getUserByUserId(@Path("userId") userId: String): Observable<UserDTO>

    @Headers("Accept: application/json",
            "Content-type: application/json")
    @POST("/user/friend/add")
    fun addFriend(@Body body: HashMap<String, Long>): Observable<Void>

    @GET("/user/{id}/friends")
    fun getFriends(@Path("id") id: Long): Observable<List<UserDTO>>

    @Headers("Accept: application/json",
            "Content-type: application/json")
    @POST("/user/{id}/update")
    fun updateProfile(@Body body: HashMap<String, String>): Observable<Void>
}