package com.line.fukuokabclient

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.Client.APIFactory
import com.line.fukuokabclient.Fragments.ChannelsFragment
import com.line.fukuokabclient.Fragments.FriendsFragment
import com.line.fukuokabclient.Utility.Prefs
import com.line.fukuokabclient.Client.ChannelClient
import com.line.fukuokabclient.Client.UserClient
import com.line.fukuokabclient.Fragments.SettingsFragment
import com.line.fukuokabclient.dto.ChannelDTO
import com.line.fukuokabclient.dto.UserDTO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainActivity : AppCompatActivity(), FriendsFragment.OnListFragmentInteractionListener, ChannelsFragment.OnListFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {

    private var userId: Long = 0
    private var token:String = ""
    var channelClient:ChannelClient? = null
    var userClient:UserClient? = null

    var friends: List<UserDTO> = emptyList()

    override fun onFriendFragmentInteraction(item: UserDTO?) {
        channelClient!!.getPersonalChannel(userId, item!!.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val channelId = it.channel.id!!

                    val info = it
                    channelClient!!.getMessages(channelId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                val intent = Intent(applicationContext, ChatActivity::class.java).apply {
                                    putExtra("messages", it.toTypedArray())
                                    putExtra("info", info)
                                    putExtra("token", intent.getStringExtra("token"))
                                }
                                startActivity(intent)
                            }, {
                                Log.d("foo2", "foo2")
                            })

                    startActivity(intent)
                }, {

                })
        Log.d("foo", "click")
    }

    override fun onChannelsFragmentInteraction(item: ChannelDTO?) {
        channelClient!!.getChannelInfo(item!!.id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val info = it
                    val intent = Intent(applicationContext, ChatActivity::class.java).apply {
                        putExtra("info", info)
                        putExtra("token", intent.getStringExtra("token"))
                    }
                    startActivity(intent)
                }, {
                    Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                })
    }

    override fun onSettingsFragmentInteraction(user: UserDTO?) {
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_friends -> {
                userClient!!.getFriends(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            friends = it
                            switchFragment(FriendsFragment.newInstance(1, friends))
                            my_toolbar.menu.clear()
                            my_toolbar.inflateMenu(R.menu.main_friends_toolbar)
                        }, {
                            Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                        })

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_channels -> {
                channelClient!!.getMyChannels(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            switchFragment(ChannelsFragment.newInstance(1, it))
                            my_toolbar.menu.clear()
                            my_toolbar.inflateMenu(R.menu.main_channel_toolbar)
                        }, {
                            Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                        })
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                userClient!!.getUserById(userId.toInt())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            switchFragment(SettingsFragment.newInstance(it))
                            Log.d("myName", it.name)
                            my_toolbar.menu.clear()
                            my_toolbar.inflateMenu(R.menu.main_settings_toolbar)
                        }, {
                            Log.d("setTextError", it.toString())
                            Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
                        })

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

        token = Prefs.get(applicationContext).getString("token", "")!!

        channelClient = APIFactory.build(token).create(ChannelClient::class.java)
        userClient = APIFactory.build(token).create(UserClient::class.java)

        setSupportActionBar(my_toolbar)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        userClient!!.getFriends(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    friends = it
                    switchFragment(FriendsFragment.newInstance(1, friends))
                }, {
                    Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_LONG).show()
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

    @SuppressLint("LongLogTag")
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.toolbar_add_friend -> {
                var intent = Intent(applicationContext, SearchActivity::class.java)
                intent.putExtra("id", userId)
                intent.putExtra("token", intent.getStringExtra("token"))
                startActivity(intent)
                return true
            }
            R.id.toolbar_add_channel -> {
                var intent = Intent(applicationContext, GroupSelectUsersActivity::class.java)
                intent.apply {
                    putExtra("friends", friends.toTypedArray())
                    putExtra("token", intent.getStringExtra("token"))
                }
                startActivity(intent)
                return true
            }
            R.id.toolbar_update_profile -> {
//                val body = HashMap<String, String>()
//                body["id"] = userId.toString()
//                body["name"] = my_name.text.toString()
//                body["hitokoto"] = my_hitokoto.text.toString()
//                userClient!!.updateProfile(body)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe({
//                            Toast.makeText(applicationContext, "変更が保存されました", Toast.LENGTH_LONG).show()
//                            Log.d("myNameChanged", "SUCCESS")
//                        }, {
//                            Toast.makeText(applicationContext, "変更に失敗しました", Toast.LENGTH_LONG).show()
//                            Log.d("myNameChanged", "FAILED " + it.toString())
//
//                        })
                if(!my_name.isFocusableInTouchMode){

//                    my_name.isEnabled = true
                    my_name.setSelectAllOnFocus(true)
                    my_name.isFocusableInTouchMode = true
                    my_hitokoto.isFocusableInTouchMode = true
                    try {
                        my_name.requestFocus()
                    } catch (e:Exception){
                        Log.e("EditTextError", e.toString())
                    }
                    val ok = SpannableString("OK")
                    ok.setSpan(ForegroundColorSpan(Color.rgb(124, 187, 255)), 0, 2, 0)
                    item.title = ok
                } else {
                    val body = HashMap<String, String>()
                    body["id"] = userId.toString()
                    body["name"] = my_name.text.toString()
                    body["hitokoto"] = my_hitokoto.text.toString()
                    userClient!!.updateProfile(body)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Toast.makeText(applicationContext, "変更が保存されました", Toast.LENGTH_LONG).show()
                                Log.d("myNameChanged", "SUCCESS")
                            }, {
                                Toast.makeText(applicationContext, "変更に失敗しました", Toast.LENGTH_LONG).show()
                                Log.d("myNameChanged", "FAILED " + it.toString())

                            })
//                    my_name.isEnabled = false
                    my_name.setSelectAllOnFocus(false)
                    my_name.isFocusableInTouchMode = false
                    my_hitokoto.isFocusableInTouchMode = false
                    my_name.setTextColor(Color.parseColor("#333333"))
                    my_hitokoto.setTextColor(Color.parseColor("#333333"))
                    item.title = "編集"
                }
                Log.d("nameEditTextEnable", my_name.isEnabled.toString())
                Log.d("nameEditTextFocusable", my_name.isFocusable.toString())
                Log.d("nameEditTextFocusableInTouchMode", my_name.isFocusableInTouchMode.toString())

//                my_hitokoto.text
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}