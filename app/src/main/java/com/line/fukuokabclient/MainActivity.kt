package com.line.fukuokabclient

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.Fragments.ChannelsFragment
import com.line.fukuokabclient.Fragments.FriendsFragment
import com.line.fukuokabclient.client.ChannelClient
import com.line.fukuokabclient.client.UserClient
import com.line.fukuokabclient.dto.ChannelDTO
import com.line.fukuokabclient.dto.UserDTO
import com.line.fukuokabclient.dummy.DummyContent
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
        Toast.makeText(applicationContext, "${item!!.name}", Toast.LENGTH_LONG).show()
    }

    override fun onChannelsFragmentInteraction(item: ChannelDTO?) {
        var intent = Intent(applicationContext, ChatActivity::class.java)
        intent.putExtra("id", userId)
        intent.putExtra("channelId", item!!.id)
        startActivity(intent)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                userClient.getFriends(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            switchFragment(FriendsFragment.newInstance(1, it))
                        }, {

                        })

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                channelClient.getPublucChannel()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            switchFragment(ChannelsFragment.newInstance(1, it))
                        }, {

                        })
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.getLongExtra("id", 0)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        switchFragment(FriendsFragment.newInstance(1, DummyContent.ITEMS))
    }

    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(main_frame.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
