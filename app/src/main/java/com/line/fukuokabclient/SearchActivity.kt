package com.line.fukuokabclient

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.Utility.Prefs
import com.line.fukuokabclient.Client.UserClient
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SearchActivity : AppCompatActivity() {
    val gson = GsonBuilder()
            .create()

    val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

    val userClient = retrofit.create(UserClient::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(search_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = Prefs.get(applicationContext)
                .getLong("id", 0)

        searchFromIdButton.setOnClickListener {
            userClient.getUserByUserId(searchIdView.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        setName(it.name)
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
                    })
        }
    }

    fun setName(name: String) {
        userNameView.text = name
    }

    fun setInfo(caution: String) {
        userNameView.text = caution
    }
}