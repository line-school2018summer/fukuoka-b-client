package com.line.fukuokabclient.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.line.fukuokabclient.R
import kotlinx.android.synthetic.main.recyclerview_chat.view.*

class ChatAdapter(private val messages: ArrayList<String>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        var messageText: TextView = view.my_message

        fun bind(message: String) {
            messageText.text = message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_chat, parent, false) as View

        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages.get(position)
        holder.bind(message)
        //holder.view.text = messages[position]
    }

    override fun getItemCount(): Int {
        return messages.size
    }


}