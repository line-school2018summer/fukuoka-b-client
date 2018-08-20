package com.line.fukuokabclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat.*
import org.java_websocket.WebSocket
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.client.StompClient



class ChatActivity : AppCompatActivity() {
//    private val uri = URI("ws://ec2-52-194-226-224.ap-northeast-1.compute.amazonaws.com/chat")
//    private val client = ChatWebSocketClient(this, uri)
    private var stompClient: StompClient = Stomp.over(WebSocket::class.java, "ws://ec2-52-194-226-224.ap-northeast-1.compute.amazonaws.com/chat")
    var mAuth: FirebaseAuth? = null
    var email:String = ""

    enum class Type {
        OPENED, CLOSED, ERROR
    }

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
            stompClient.connect()

            stompClient.topic("/topic/chat.123").subscribe { topicMessage ->
                messageList.append(topicMessage.payload)
            }



            stompClient.lifecycle().subscribe{ lifecycleEvent ->
                when (lifecycleEvent.type) {

                    Type.OPENED -> Log.i(javaClass.simpleName, "WSサーバに接続しました。")

                    Type.ERROR -> Log.i(javaClass.simpleName, "エラーが発生しました。")

                    Type.CLOSED -> Log.i(javaClass.simpleName, "WSサーバから切断しました。")
                }
            }

            it.isEnabled = false
            btnDisconnect.isEnabled = true
        }

        btnDisconnect.setOnClickListener {
            stompClient.disconnect()
            btnConnect.isEnabled = true
            btnDisconnect.isEnabled = false
        }

        btnSendMessage.setOnClickListener {
            // {'senderId':from, 'roomId':'123','text':text}
            val message = "{\"senderId\": \"99999\", \"roomId\": \"123\", \"text\": \"" + editSendMessage.text.toString() + "\"}"
//            client.send(message)
            val messageTest = "{'from': 'me','text':text}"
//            messageList.append(editSendMessage.text.toString())
            stompClient.send("/app/chat.123", message).subscribe()
        }
    }
}
