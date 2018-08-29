package com.line.fukuokabclient.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.line.fukuokabclient.R
import com.line.fukuokabclient.dto.MessageDTO
import kotlinx.android.synthetic.main.recyclerview_chat.view.*

class ChatAdapter(private val messages: ArrayList<MessageDTO>, val senderId: Long): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        var messageText: TextView = view.my_message

        fun bind(message: String) {
            messageText.text = message
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messages[position].senderId == senderId) return 1
        else return 2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var textView:View? = null
        when (viewType) {
            1 ->  textView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_chat, parent, false)
            2 -> textView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_chat2, parent, false)
        }

        return ViewHolder(textView!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages.get(position)
        holder.bind(message.content)
        //holder.view.text = messages[position]
    }

    override fun getItemCount(): Int {
        return messages.size
    }


}