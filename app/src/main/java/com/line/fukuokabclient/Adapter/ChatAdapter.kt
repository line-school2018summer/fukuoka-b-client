package com.line.fukuokabclient.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.line.fukuokabclient.R

class ChatAdapter(private val messages: ArrayList<String>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(val textView: TextView): RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recyclerview_chat, parent, false) as TextView

        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = messages[position]
    }

    override fun getItemCount(): Int {
        return messages.size
    }


}