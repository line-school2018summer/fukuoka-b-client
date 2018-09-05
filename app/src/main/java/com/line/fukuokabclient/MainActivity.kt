package com.line.fukuokabclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.Fragments.ChannelsFragment
import com.line.fukuokabclient.Fragments.FriendsFragment
import com.line.fukuokabclient.Utility.Prefs
import com.line.fukuokabclient.client.ChannelClient
import com.line.fukuokabclient.client.UserClient
import com.line.fukuokabclient.dto.ChannelDTO
import com.line.fukuokabclient.dto.MessageDTO
import com.line.fukuokabclient.dto.UserDTO
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity(), FriendsFragment.OnListFragmentInteractionListener, ChannelsFragment.OnListFragmentInteractionListener {

    private var userId: Long = 0
    val gson = GsonBuilder().create()
    val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
    val channelClient = retrofit.create(ChannelClient::class.java)
    val userClient = retrofit.create(UserClient::class.java)

    override fun onFriendFragmentInteraction(item: UserDTO?) {
        channelClient.getPersonalChannel(userId, item!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val channelId = it.channel.id!!

                    val info = it
                    channelClient.getMessages(channelId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                val intent = Intent(applicationContext, ChatActivity::class.java).apply {
                                    putExtra("channelId", channelId)
                                    putExtra("messages", it.toTypedArray())
                                    putExtra("info", info)
                                }
                                startActivity(intent)
                            }, {

                            })

                    startActivity(intent)
                }, {

                })
    }

    override fun onChannelsFragmentInteraction(item: ChannelDTO?) {
        var intent = Intent(applicationContext, ChatActivity::class.java)
        intent.putExtra("channelId", item!!.id)
        startActivity(intent)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_friends -> {
                userClient.getFriends(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            switchFragment(FriendsFragment.newInstance(1, it))
                        }, {

                        })

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_channels -> {
                channelClient.getPublicChannel()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            switchFragment(ChannelsFragment.newInstance(1, it))
                        }, {

                        })
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = Prefs.get(applicationContext)
                .getLong("id", 0)

        setSupportActionBar(my_toolbar)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        userClient.getFriends(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    switchFragment(FriendsFragment.newInstance(1, it))
                }, {

                })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_friends_toolbar, menu)
        return true
    }

    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(main_frame.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.toolbar_add_friend -> {
                var intent = Intent(applicationContext, SearchActivity::class.java)
                intent.putExtra("id", userId)
                startActivity(intent)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
