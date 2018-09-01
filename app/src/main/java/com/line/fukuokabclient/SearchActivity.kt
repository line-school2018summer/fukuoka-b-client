package com.line.fukuokabclient

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.client.UserClient
import com.line.fukuokabclient.dto.UserDTO
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val id = intent.getLongExtra("id", 0)

        Log.d("CREATE!", "CREATE!")
        val gson = GsonBuilder()
                .create()

        val retrofit = Retrofit.Builder()
//                .baseUrl("http://192.168.0.14:8080")
                .baseUrl("http://ec2-52-194-226-224.ap-northeast-1.compute.amazonaws.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        val userClient = retrofit.create(UserClient::class.java)


        fun setName(user: UserDTO) {
            userNameView.text = "name : " + user.name
        }

        fun setInfo(caution: String) {
            userNameView.text = caution
        }

        searchFromIdButton.setOnClickListener {
//
            // getUserFromId
//
//            userClient.getUserById(Integer.parseInt(searchIdView.text.toString()))
//                    .enqueue(object : Callback<UserDTO> {
//                        override fun onFailure(call: Call<UserDTO>, t: Throwable?) {
//                            Log.d("result", "failure" + t.toString())
//
//                        }
//
//                        override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
//                            if (response.isSuccessful) {
//                                response.body()?.let {
//                                    setName(it)
//
//                                    AlertDialog.Builder(this@SearchActivity)
//                                            .setTitle("My Data")
//                                            .setMessage(it.toString()).show()
//                                }
//                            } else {
//                                Log.d("result", "fail")
//                            }
//                        }
//                    })

            userClient.getUserByUserId(searchIdView.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        setName(it)
                        AlertDialog.Builder(this@SearchActivity)
                                .setTitle("My Data")
                                .setMessage(it.toString()).show()

                        addFriendButton.visibility = View.VISIBLE
                        val friendId = it.id
                        val friendName = it.name
                        addFriendButton.setOnClickListener {
                            val body = HashMap<String, Long>()
                            body["userId"] = id
                            body["friendId"] = friendId
                            userClient.addFriend(body)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        Log.d("ADD", "ADDED!")
                                        addFriendButton.visibility = View.INVISIBLE
                                        Toast.makeText(applicationContext, "\"${friendName}\" was added successfully!", Toast.LENGTH_LONG).show()
                                    }, {
                                        Log.d("ADD", "FAILED!${it.message}")
                                    })
                        }
                    }, {
                        setInfo("User Not Found")
                        AlertDialog.Builder(this@SearchActivity)
                                .setTitle("404")
                                .setMessage("User Not Found").show()
//                        Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                    })
            //        setName(name)
        }

    }

}