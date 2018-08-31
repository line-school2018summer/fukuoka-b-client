package com.line.fukuokabclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.line.fukuokabclient.Adapter.ChatAdapter
import com.line.fukuokabclient.dto.MessageDTO
import com.line.fukuokabclient.websocket.WebSocketChatClient
import kotlinx.android.synthetic.main.activity_chat.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ChatActivity : AppCompatActivity() {
    private var client = WebSocketChatClient(this)
    var mAuth: FirebaseAuth? = null
    var email:String = ""
    var channelId: Long = 123
    var senderId:Long = 9999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        mAuth = FirebaseAuth.getInstance()
        channelId = intent.getLongExtra("channelId", 0)
        senderId = intent.getLongExtra("id", 0)
    }

    override fun onStart() {
        super.onStart()
        email = mAuth?.currentUser?.email ?: throw Exception("Not Logged in")
    }

    override fun onResume() {
        super.onResume()

        btnConnect.setOnClickListener {
            client.connect()

//             client.topic("/topic/chat.$channelId", messageList)
//            client.topic("/topic/chat.123", messageList)
            start()
            client.lifecycle()

            it.isEnabled = false
            btnDisconnect.isEnabled = true
        }

        btnDisconnect.setOnClickListener {
            client.disconnect()
            client = WebSocketChatClient(this)
            btnConnect.isEnabled = true
            btnDisconnect.isEnabled = false
        }

        btnSendMessage.setOnClickListener {
            if (editSendMessage.text.toString().isNotEmpty()) {
                val message = MessageDTO(null, senderId, channelId, editSendMessage.text.toString(), null)
                client.send("/app/chat.$channelId", message.toString())

                // メッセージ送信後は入力欄を空欄にする
                editSendMessage.setText("")
            }
        }
    }

    fun start() {
        var items = ArrayList<MessageDTO>()
        val messageAdapter = ChatAdapter(items, senderId)
        chat_recycler_view.layoutManager = LinearLayoutManager(this)
        chat_recycler_view.adapter = messageAdapter


        client.topic("/topic/chat.$channelId")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("hogehoge", "${items.size}")
                    items.add(it)
                    messageAdapter.notifyDataSetChanged()
                }, {
                    Log.e("hogehoge", "error", it)
                }, {
                    Log.d("hogehoge", "completed")
                })
    }



}