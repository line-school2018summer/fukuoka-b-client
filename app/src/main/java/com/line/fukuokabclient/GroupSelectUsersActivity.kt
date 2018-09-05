package com.line.fukuokabclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.line.fukuokabclient.Adapter.FriendsRecyclerViewAdapter
import com.line.fukuokabclient.Fragments.FriendsFragment
import com.line.fukuokabclient.dto.UserDTO
import kotlinx.android.synthetic.main.activity_group_select_users.*

class GroupSelectUsersActivity : AppCompatActivity(), FriendsFragment.OnListFragmentInteractionListener {
    override fun onFriendFragmentInteraction(item: UserDTO?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_select_users)

        setSupportActionBar(group_select_user_toolbar)

        var friendAdapter = FriendsRecyclerViewAdapter(emptyList(), this, FriendsRecyclerViewAdapter.Mode.SELECT)
    }
}
