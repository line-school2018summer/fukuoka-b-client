package com.line.fukuokabclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.line.fukuokabclient.Adapter.SelectFriendsRecyclerViewAdapter
import com.line.fukuokabclient.dto.UserDTO
import kotlinx.android.synthetic.main.activity_group_select_users.*

class GroupSelectUsersActivity : AppCompatActivity(), SelectFriendsRecyclerViewAdapter.OnListItemSelectedInteractionListener {

    var friends: List<UserDTO> = emptyList()

    var selectedFriends: Map<Long, UserDTO> = HashMap()

    override fun OnListItemSelectedInteraction(item: UserDTO, selected: Boolean) {

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


}
