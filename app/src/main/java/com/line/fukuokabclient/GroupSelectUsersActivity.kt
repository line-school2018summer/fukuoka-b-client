package com.line.fukuokabclient

import android.graphics.Color
import android.graphics.Color.rgb
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.line.fukuokabclient.Adapter.SelectFriendsRecyclerViewAdapter
import com.line.fukuokabclient.dto.UserDTO
import kotlinx.android.synthetic.main.activity_group_select_users.*
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.Client.ChannelClient
import com.line.fukuokabclient.Utility.Prefs
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class GroupSelectUsersActivity : AppCompatActivity(), SelectFriendsRecyclerViewAdapter.OnListItemSelectedInteractionListener {

    val gson = GsonBuilder()
            .create()
    val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

    val channelClient = retrofit.create(ChannelClient::class.java)

    val blue = ForegroundColorSpan(rgb(124, 187, 255))
    val grey = ForegroundColorSpan(rgb(170, 170, 170))
    var okItem:MenuItem? = null

    var friends: List<UserDTO> = emptyList()


    var selectedFriends: HashMap<Long, UserDTO> = HashMap()

    override fun onListItemSelectedInteraction(item: UserDTO, selected: Boolean) {
        if (selected) {
            selectedFriends[item.id] = item

            val spanString = SpannableString("OK(${selectedFriends.size})")
            spanString.setSpan(blue, 0, spanString.length, 0) //fix the color to white
            okItem!!.title = spanString
        } else
        {
            selectedFriends.remove(item.id)

            if (selectedFriends.size == 0) {
                val spanString = SpannableString("OK")
                spanString.setSpan(grey, 0, spanString.length, 0) //fix the color to white
                okItem!!.title = spanString
            } else {
                val spanString = SpannableString("OK(${selectedFriends.size})")
                spanString.setSpan(blue, 0, spanString.length, 0) //fix the color to white
                okItem!!.title = spanString
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_select_users)

        setSupportActionBar(group_select_user_toolbar)

        friends = intent.getParcelableArrayExtra("friends").toList() as List<UserDTO>
        var friendAdapter = SelectFriendsRecyclerViewAdapter(friends, this)

        group_select_user_recycler_view.layoutManager = LinearLayoutManager(this)
        group_select_user_recycler_view.adapter = friendAdapter
        group_select_user_recycler_view.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_select_user, menu)
        okItem = group_select_user_toolbar.menu.findItem(R.id.toolbar_channel_ok)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.toolbar_channel_ok -> {
                if (selectedFriends.size == 0) return true
                var userIds = selectedFriends.values.map { it.id }.toMutableList()
                userIds.add(Prefs.get(applicationContext).getLong("id", 0))
                channelClient.newGroupChannel(hashMapOf(Pair("userIds", userIds)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d("NEW GROUP", "Success ${it.channel.id}")
                        }, {
                            Log.d("NEW GROUP", "failed ${it.message}")
                        })
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
