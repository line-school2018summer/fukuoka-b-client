package com.line.fukuokabclient

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
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
                        addFriendButton.setOnClickListener {
                            val body = HashMap<String, Long>()
                            body.put("userId", id)
                            body.put("friendId", friendId)
                            userClient.addFriend(body)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe({
                                        Log.d("ADD", "ADDED!")
                                        addFriendButton.visibility = View.INVISIBLE
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