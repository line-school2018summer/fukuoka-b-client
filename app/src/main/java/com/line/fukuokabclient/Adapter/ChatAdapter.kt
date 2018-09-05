package com.line.fukuokabclient.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.line.fukuokabclient.Client.Response.ResponseChannelInfo
import com.line.fukuokabclient.R
import com.line.fukuokabclient.dto.MessageDTO
import com.line.fukuokabclient.Utility.DateUtils
import kotlinx.android.synthetic.main.recyclerview_chat.view.*
import kotlinx.android.synthetic.main.recyclerview_chat2.view.*

class ChatAdapter(private val messages: ArrayList<MessageDTO>, val senderId: Long): RecyclerView.Adapter<ViewHolder>() {
    var info: ResponseChannelInfo? = null
    var userMapper: HashMap<Long, String> = HashMap()

    constructor(info: ResponseChannelInfo?, messages: ArrayList<MessageDTO>, senderId: Long): this(messages, senderId) {
        this.info = info
        info?.users?.forEach {
            userMapper[it.id] = it.name
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].senderId == senderId) return 1
        else return 2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder:ViewHolder? = null
        when (viewType) {
            1 ->  viewHolder = MyMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_chat, parent, false))
            2 -> viewHolder = OtherMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_chat2, parent, false))
        }

        return viewHolder!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages.get(position)
        holder.bind(message)
        //holder.view.text = messages[position]
    }

    override fun getItemCount(): Int {
        return messages.size
    }


    inner class MyMessageViewHolder(view: View): ViewHolder(view) {
        private var messageText: TextView = view.my_message
        private var timeText: TextView = view.my_message_time

        override fun bind(message: MessageDTO) {
            messageText.text = message.content
            timeText.text = DateUtils.fromMillisToTimeString(message.createdAt!!)
        }
    }

    inner class OtherMessageViewHolder(view: View): ViewHolder(view) {
        private var messageText: TextView = view.other_message
        private var timeText: TextView = view.other_message_time
        private var nameText: TextView = view.other_user_name

        override fun bind(message: MessageDTO) {
            messageText.text = message.content
            timeText.text = DateUtils.fromMillisToTimeString(message.createdAt!!)
            nameText.text = userMapper[message.senderId]
        }
    }

}

open class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    open fun bind(message: MessageDTO){}
}