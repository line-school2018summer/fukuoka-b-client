package com.line.fukuokabclient.Fragments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.line.fukuokabclient.R


import com.line.fukuokabclient.Fragments.ChannelSettingUserFragment.OnListFragmentInteractionListener
import com.line.fukuokabclient.Fragments.dummy.DummyContent.DummyItem
import com.line.fukuokabclient.dto.UserDTO

import kotlinx.android.synthetic.main.fragment_channelsettinguser.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class ChannelSettingUserRecyclerViewAdapter(
        private val mValues: List<UserDTO>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<ChannelSettingUserRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as UserDTO
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_channelsettinguser, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mContentView.text = item.name
        holder.mIconView.setImageResource(R.drawable.default_user_icon)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView = mView.content
        val mIconView: ImageView = mView.user_icon_img as ImageView

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
