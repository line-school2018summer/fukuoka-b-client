package com.line.fukuokabclient

import android.app.Activity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.fasterxml.jackson.databind.ObjectMapper
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class ChatWebSocketClient (val activity: Activity, uri: URI): WebSocketClient(uri) {

    private val contextView: TextView by lazy {
        activity.findViewById<TextView>(R.id.messageList)
    }

    private val breakLine = System.lineSeparator()

    override fun onOpen(handshakedata: ServerHandshake?) {
        Log.i(javaClass.simpleName, "WSサーバに接続しました。")
        Log.i(javaClass.simpleName, "スレッド：「${Thread.currentThread().name}」で実行中")

        Toast.makeText(activity.applicationContext, "WSサーバに接続しました。", Toast.LENGTH_LONG).show()
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.i(javaClass.simpleName, "WSサーバから切断しました。reason:${reason}")
        Log.i(javaClass.simpleName, "スレッド：「${Thread.currentThread().name}」で実行中")

        Toast.makeText(activity.applicationContext, "WSサーバから切断しました。reason:${reason}", Toast.LENGTH_LONG).show()
    }

    override fun onMessage(message: String?) {
        val json = ObjectMapper().readTree(message!!)

        activity.runOnUiThread {
            contextView.append("$message")
            contextView.append("$breakLine")
        }
    }

    override fun onError(ex: Exception?) {
        Log.i(javaClass.simpleName, "エラーが発生しました。", ex)
        Log.i(javaClass.simpleName, "スレッド：「${Thread.currentThread().name}」で実行中")
    }

}