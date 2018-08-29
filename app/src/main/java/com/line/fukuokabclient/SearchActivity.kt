package com.line.fukuokabclient

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.client.UserClient
import com.line.fukuokabclient.dto.UserDTO
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Log.d("CREATE!", "CREATE!")
        val gson = GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://172.16.0.31:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        val userClient = retrofit.create(UserClient::class.java)

        fun setName(user: UserDTO) {
            userNameView.text = "name : " + user.name
        }

//        val name =
        searchFromIdButton.setOnClickListener {

            userClient.API(Integer.parseInt(searchIdView.text.toString()))
                    .enqueue(object : Callback<UserDTO> {
                        override fun onFailure(call: Call<UserDTO>, t: Throwable?) {
                            Log.d("result", "failure" + t.toString())

                        }

                        override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    setName(it)
                                    Log.d("name", "alialialiali")

                                    AlertDialog.Builder(this@SearchActivity)
                                            .setTitle("My Data")
                                            .setMessage(it.toString()).show()
                                }
                            } else {
                                Log.d("result", "fail")
                            }
                        }
                    })


            //        setName(name)
        }
    }

}