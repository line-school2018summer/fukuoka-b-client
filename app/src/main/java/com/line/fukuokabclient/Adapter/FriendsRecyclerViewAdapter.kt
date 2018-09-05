package com.line.fukuokabclient.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import com.line.fukuokabclient.Fragments.FriendsFragment.OnListFragmentInteractionListener
import com.line.fukuokabclient.R
import com.line.fukuokabclient.dto.UserDTO

import kotlinx.android.synthetic.main.fragment_friends.view.*
import kotlinx.android.synthetic.main.fragment_friends_select.view.*

/**
 * [RecyclerView.Adapter] that can display a [UserDTO] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class FriendsRecyclerViewAdapter(
        private val mValues: List<UserDTO>,
        private val mListener: OnListFragmentInteractionListener?,
        private val mode:Mode)
    : RecyclerView.Adapter<UserViewHolder>() {

    enum class Mode {
        LIST, SELECT
    }

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as UserDTO
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onFriendFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        when(mode) {
            Mode.LIST -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.fragment_friends, parent, false)
                return ListViewHolder(view)
            }
            Mode.SELECT -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.fragment_friends_select, parent, false)
                return SelectViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = mValues[position]
        holder.bind(item)
//        holder.bind()
//        holder.mIdView.text = item.id.toString()
//        holder.mContentView.text = item.name
//
//        with(holder.mView) {
//            tag = item
//            setOnClickListener(mOnClickListener)
//        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ListViewHolder(val mView: View) : UserViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }

        override fun bind(user: UserDTO) {
            mIdView.text = user.id.toString()
            mContentView.text = user.name

            with(mView) {
                tag = user
                setOnClickListener {
                    mOnClickListener.onClick(it)
                }
            }
        }
    }

    inner class SelectViewHolder(val mView: View): UserViewHolder(mView) {
        val selectImg: ImageView = mView.image_select
        val nameView: TextView = mView.txt_select_username
        var selected = false

        override fun bind(user: UserDTO) {
            nameView.text = user.name
            with(mView) {
                tag = user
                setOnClickListener {
                    mOnClickListener.onClick(it)
                    if (selected) selectImg.alpha = 0f
                    else selectImg.alpha = 1f
                }
            }
        }
    }
}

open class UserViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    open fun bind(user: UserDTO){}
}
