package com.line.fukuokabclient

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.line.fukuokabclient.Fragments.FriendsFragment
import com.line.fukuokabclient.dto.UserDTO
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FriendsFragment.OnListFragmentInteractionListener {
    override fun onFriendFragmentInteraction(item: UserDTO?) {
        Toast.makeText(applicationContext, "${item!!.name}", Toast.LENGTH_LONG).show()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                switchFragment(FriendsFragment.newInstance(1))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

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

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(main_frame.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
