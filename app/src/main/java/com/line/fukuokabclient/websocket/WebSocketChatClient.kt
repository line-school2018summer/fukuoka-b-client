package com.line.fukuokabclient.websocket

import android.app.Activity
import android.util.Log
import android.widget.TextView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.line.fukuokabclient.BuildConfig
import com.line.fukuokabclient.dto.MessageDTO
import org.java_websocket.WebSocket
import rx.Observable
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.client.StompClient

class WebSocketChatClient (val activity: Activity) {
    private var stompClient: StompClient = Stomp.over(WebSocket::class.java, BuildConfig.WSURL)
    private val breakLine = System.lineSeparator()
    enum class Type {
        OPENED, CLOSED, ERROR
    }

    fun connect() = stompClient.connect()

    fun isConnected() = stompClient.isConnected

    fun topic(destination: String, view: TextView) {
        stompClient.topic(destination).subscribe { topicMessage ->
            run {
                val mapper = jacksonObjectMapper()
                val message = mapper.readValue<MessageDTO>(topicMessage.payload)
                activity.runOnUiThread {
                    view.append(message.id.toString() + message.content)
                    view.append(breakLine)
                }
            }
        }
    }

    fun topic(destination: String) :Observable<MessageDTO> {
        return stompClient.topic(destination).map { topicMessage ->
            run {
                val mapper = jacksonObjectMapper()
                Log.d("hogehoge", "${topicMessage.payload}")
                mapper.readValue<MessageDTO>(topicMessage.payload)
            }
        }
    }

    fun lifecycle() {
        stompClient.lifecycle().subscribe{ lifecycleEvent ->
            when (lifecycleEvent.type) {

                Type.OPENED -> Log.i(javaClass.simpleName, "WSサーバに接続しました。")

                Type.ERROR -> Log.i(javaClass.simpleName, "エラーが発生しました。")

                Type.CLOSED -> Log.i(javaClass.simpleName, "WSサーバから切断しました。")
            }
        }
    }

    fun disconnect() = stompClient.disconnect()

    fun send(destination: String, message: String) = stompClient.send(destination, message).subscribe()
}