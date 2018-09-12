package com.line.fukuokabclient.Client
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.BuildConfig
import com.line.fukuokabclient.dto.UserDTO
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface APIFactory {
    companion object {
        val gson: Gson = GsonBuilder()
                .create()

        fun build(token: String): Retrofit {
            val authenticatedClient = OkHttpClient().newBuilder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(Interceptor { chain ->
                        chain.proceed(
                                chain.request()
                                        .newBuilder()
                                        .addHeader("Token", token)
                                        .build())
                    })
                    .build()
            val retrofit = Retrofit.Builder()
                    .client(authenticatedClient)
                    .baseUrl(BuildConfig.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()

            return retrofit
        }


    }
}