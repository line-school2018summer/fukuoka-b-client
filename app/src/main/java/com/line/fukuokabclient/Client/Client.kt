package com.line.fukuokabclient.Client
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import rx.Observable
import java.util.concurrent.TimeUnit

interface Client {
    companion object {
        public val gson: Gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setLenient()
                .create()

        fun build(token: String): Client {
            val authenticatedClient = OkHttpClient().newBuilder()
                    .readTimeout(0, TimeUnit.SECONDS)
                    .addInterceptor(Interceptor { chain ->
                        chain.proceed(
                                chain.request()
                                        .newBuilder()
                                        .header("Authorization", "Bearer $token")
                                        .build())
                    })
                    .build()
            val retrofit = Retrofit.Builder()
                    .client(authenticatedClient)
                    .baseUrl(BuildConfig.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()

            return retrofit.create(Client::class.java)
        }
    }

//    @Headers("Content-Type: application/json")
//    @GET("/status")
//    fun getStatus(): Observable<Status>
}