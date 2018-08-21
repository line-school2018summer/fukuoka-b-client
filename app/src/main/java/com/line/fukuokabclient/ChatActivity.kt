package com.line.fukuokabclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.line.fukuokabclient.dto.MessageDTO
import com.line.fukuokabclient.websocket.WebSocketChatClient
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private var client = WebSocketChatClient(this)
    var mAuth: FirebaseAuth? = null
    var email:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        email = mAuth?.currentUser?.email ?: throw Exception("Not Logged in")
    }

    override fun onResume() {
        super.onResume()

        btnConnect.setOnClickListener {
            client.connect()
            client.topic("/topic/chat.123", messageList)
            client.lifecycle()

            it.isEnabled = false
            btnDisconnect.isEnabled = true
        }

        btnDisconnect.setOnClickListener {
            client.disconnect()
            btnConnect.isEnabled = true
            btnDisconnect.isEnabled = false
        }

        btnSendMessage.setOnClickListener {
            if (editSendMessage.text.toString().isNotEmpty()) {
                val message = MessageDTO(null, 9999,123, editSendMessage.text.toString(), null)
                client.send("/app/chat.123", message.toString())
            }
        }
    }
}
