package com.line.fukuokabclient

import android.graphics.Color
import android.graphics.Color.rgb
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.line.fukuokabclient.Adapter.SelectFriendsRecyclerViewAdapter
import com.line.fukuokabclient.dto.UserDTO
import kotlinx.android.synthetic.main.activity_group_select_users.*
import android.text.style.ForegroundColorSpan
import android.text.SpannableString



class GroupSelectUsersActivity : AppCompatActivity(), SelectFriendsRecyclerViewAdapter.OnListItemSelectedInteractionListener {

    var friends: List<UserDTO> = emptyList()

    var selectedFriends: HashMap<Long, UserDTO> = HashMap()

    override fun onListItemSelectedInteraction(item: UserDTO, selected: Boolean) {
        if (selected) {
            selectedFriends[item.id] = item

            val spanString = SpannableString("OK(${selectedFriends.size})")
            spanString.setSpan(ForegroundColorSpan(rgb(124, 187, 255)), 0, spanString.length, 0) //fix the color to white
            group_select_user_toolbar.menu.findItem(R.id.toolbar_channel_ok).title = spanString
        } else
        {
            selectedFriends.remove(item.id)

            if (selectedFriends.size == 0) {
                val spanString = SpannableString("OK")
                spanString.setSpan(ForegroundColorSpan(rgb(170, 170, 170)), 0, spanString.length, 0) //fix the color to white
                group_select_user_toolbar.menu.findItem(R.id.toolbar_channel_ok).title = spanString
            } else {
                val spanString = SpannableString("OK(${selectedFriends.size})")
                spanString.setSpan(ForegroundColorSpan(rgb(124, 187, 255)), 0, spanString.length, 0) //fix the color to white
                group_select_user_toolbar.menu.findItem(R.id.toolbar_channel_ok).title = spanString
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_select_user, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
