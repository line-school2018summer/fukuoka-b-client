package com.line.fukuokabclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.line.fukuokabclient.Adapter.ChatAdapter
import com.line.fukuokabclient.Client.Response.ResponseChannelInfo
import com.line.fukuokabclient.Utility.Prefs
import com.line.fukuokabclient.db.DbOpenHelper
import com.line.fukuokabclient.dto.MessageDTO
import com.line.fukuokabclient.websocket.WebSocketChatClient
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.db.StringParser
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ChatActivity : AppCompatActivity() {
    private var client = WebSocketChatClient(this)
    var mAuth: FirebaseAuth? = null
    var email:String = ""
    var channelId: Long = 0
    var userId:Long = 0
    var balloonColorCode: String = ""

    var items = ArrayList<MessageDTO>()
    var messageAdapter: ChatAdapter? = null
    var info: ResponseChannelInfo? = null
    var userMapper: HashMap<Long, String> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val helper = DbOpenHelper.getInstance(this)

        setSupportActionBar(chat_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()

        if (intent.getParcelableArrayExtra("messages") != null) items = ArrayList(intent.getParcelableArrayExtra("messages").toList()) as ArrayList<MessageDTO>
        info = intent.getParcelableExtra("info")
        channelId = info!!.channel.id!!
        userId = Prefs.get(applicationContext)
                .getLong("id", 0)

        helper.use {
            insert("balloonColor",
                    "senderId" to userId,
                    "channelId" to channelId,
                    "colorCode" to "#d4d4d4"
            )
        }

        balloonColorCode = helper.readableDatabase.select("balloonColor", "colorCode")
                .whereArgs("(senderId = {userId}) and (channelId = {roomId})",
                        "userId" to userId,
                        "roomId" to channelId).parseSingle(StringParser)
        Log.d("color", balloonColorCode)
        btnSendMessage.setOnClickListener {
            if (client.isConnected() && editSendMessage.text.toString().isNotEmpty() ) {
                val message = MessageDTO(null, userId, channelId, editSendMessage.text.toString(), null)
//                balloonColorCode = helper.readableDatabase.select("balloonColor", "colorCode")
//                        .whereArgs("(senderId = {userId}) and (channelId = {roomId})",
//                                "userId" to message.senderId,
//                                "roomId" to message.channelId).toString()
                client.send("/app/chat.$channelId", message.toJson())

                // メッセージ送信後は入力欄を空欄にする
                editSendMessage.setText("")
            }
        }

        this.title = if (info!!.users.size == 2) {
            var title = ""
            info!!.users.forEach {
                if (it.id != userId) title = it.name
            }
            title
        } else {
            info?.channel?.name?: "NO NAME"
        }
        messageAdapter = ChatAdapter(info, items, userId, balloonColorCode)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()
        email = mAuth?.currentUser?.email ?: throw Exception("Not Logged in")

        val helper = DbOpenHelper.getInstance(this)
//        val balloonColorCode: String = helper.readableDatabase.select("balloonColor", "colorCode")
//                .whereArgs("(senderId = {message.senderId) and (channelId = {message.channelId})",
//                        "senderId" to userId,
//                        "channelId" to channelId) as String
    }

    override fun onResume() {
        super.onResume()

        client.connect()
        start()
        client.lifecycle()
    }

    override fun onPause() {
        super.onPause()
        client.disconnect()
        client = WebSocketChatClient(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.chat_settings -> {

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun start() {
        chat_recycler_view.layoutManager = LinearLayoutManager(this)
        chat_recycler_view.adapter = messageAdapter!!

        if (messageAdapter!!.itemCount > 0) chat_recycler_view.scrollToPosition(messageAdapter!!.itemCount - 1)

        client.topic("/topic/chat.$channelId")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("hogehoge", "${items.size}")
                    items.add(it)
                    messageAdapter!!.notifyDataSetChanged()
                    chat_recycler_view.scrollToPosition(messageAdapter!!.itemCount-1)
                }, {
                    Log.e("hogehoge", "error", it)
                }, {
                    Log.d("hogehoge", "completed")
                })
    }



}
