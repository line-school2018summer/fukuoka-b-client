package com.line.fukuokabclient.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import com.line.fukuokabclient.Fragments.ChannelsFragment.OnListFragmentInteractionListener
import com.line.fukuokabclient.R
import com.line.fukuokabclient.dto.ChannelDTO
import com.line.fukuokabclient.dto.UserDTO

import kotlinx.android.synthetic.main.fragment_channels.view.*

/**
 * [RecyclerView.Adapter] that can display a [ChannelDTO] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class ChannelsRecyclerViewAdapter(
        private val mValues: List<ChannelDTO>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<ChannelsRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ChannelDTO
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onChannelsFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_channels, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.name

        if (item.id!!.toInt() % 2 == 0) {
            holder.mGroupIconView.setImageResource(R.drawable.ic_default_group_icon)
        } else {
            holder.mGroupIconView.setImageResource(R.drawable.ic_default_group_icon2)
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : GroupViewHolder(mView) {
        val mGroupIconView: ImageView = mView.group_icon_img as ImageView
        val mIdView: TextView = mView.txt_channel_name

        override fun bind(channel: ChannelDTO) {
            mIdView.text = channel.name
        }
    }
}

open class GroupViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    open fun bind(channel: ChannelDTO){}
}
