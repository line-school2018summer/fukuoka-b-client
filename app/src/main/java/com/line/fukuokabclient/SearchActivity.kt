package com.line.fukuokabclient

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.Client.APIFactory
import com.line.fukuokabclient.Utility.Prefs
import com.line.fukuokabclient.Client.UserClient
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class SearchActivity : AppCompatActivity() {
    var token: String = ""
    var userClient:UserClient? = null
    var request = HashMap<String, Long>()
    var friendName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(search_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = Prefs.get(applicationContext)
                .getLong("id", 0)
        token = Prefs.get(applicationContext).getString("token", "")!!

        userClient = APIFactory.build(token).create(UserClient::class.java)

        searchFromIdButton.setOnClickListener {
            userClient!!.getUserByUserId(searchIdView.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        setName(it.name)

                        addFriendButton.visibility = View.VISIBLE
                        request["friendId"] = it.id
                        friendName = it.name

                    }, {
                        setInfo("User Not Found")
                        AlertDialog.Builder(this@SearchActivity)
                                .setTitle("404")
                                .setMessage("User Not Found").show()
                    })
        }

        addFriendButton.setOnClickListener {
            request["userId"] = id
            userClient!!.addFriend(request)
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
    }

    fun setName(name: String) {
        userNameView.text = name
    }

    fun setInfo(caution: String) {
        userNameView.text = caution
    }
}