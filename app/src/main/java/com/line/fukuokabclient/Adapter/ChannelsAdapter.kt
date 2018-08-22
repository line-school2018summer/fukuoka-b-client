package com.line.fukuokabclient.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.line.fukuokabclient.*
import com.line.fukuokabclient.dto.ChannelDTO
import kotlinx.android.synthetic.main.recyclerview_channel.view.*

class ChannelsAdapter(private val channels: List<ChannelDTO>): RecyclerView.Adapter<ChannelsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_channel, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int = channels.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val channel = channels[position]
        holder.bind(channel)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(channel: ChannelDTO) {
            itemView.txt_channel_name.text = channel.name
            itemView.btn_enter_channel.setOnClickListener {
                var intent = Intent(itemView.context, ChatActivity::class.java)
                intent.putExtra("channelId", channel.id)
                itemView.context.startActivity(intent)
            }
        }
    }
}