package com.line.fukuokabclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.line.fukuokabclient.Adapter.ChatAdapter
import com.line.fukuokabclient.Client.ChannelClient
import com.line.fukuokabclient.Client.Response.ResponseChannelInfo
import com.line.fukuokabclient.Utility.Prefs
import com.line.fukuokabclient.db.ColorDataParser
import com.line.fukuokabclient.db.DbOpenHelper
import com.line.fukuokabclient.dto.MessageDTO
import com.line.fukuokabclient.websocket.WebSocketChatClient
import kotlinx.android.synthetic.main.activity_chat.*
import org.jetbrains.anko.db.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ChatActivity : AppCompatActivity() {
    private var client = WebSocketChatClient(this)
    var mAuth: FirebaseAuth? = null
    var email:String = ""
    var channelId: Long = 0
    var channelName: String = ""
    var userId:Long = 0

    var items = ArrayList<MessageDTO>()
    var messageAdapter: ChatAdapter? = null
    var info: ResponseChannelInfo? = null
    var userMapper: HashMap<Long, Int> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setSupportActionBar(chat_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance()

        info = intent.getParcelableExtra("info")
        channelId = info!!.channel.id!!
        channelName = info!!.channel.name
        userId = Prefs.get(applicationContext)
                .getLong("id", 0)

        btnSendMessage.setOnClickListener {
            if (client.isConnected() && editSendMessage.text.toString().isNotEmpty() ) {
                val message = MessageDTO(null, userId, channelId, editSendMessage.text.toString(), null)
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
        userMapperInit()

        messageAdapter = ChatAdapter(info, info!!.messages, userId, userMapper)
    }

    fun userMapperInit() {
        var dbHelper = DbOpenHelper.getInstance(applicationContext)
        info!!.users.forEach {
            dbHelper.readableDatabase.select("msgColorMap", "count(userId)")
                    .whereArgs("(userId = {id}) and (channelId = {channelId})",
                            "id" to it.id,
                            "channelId" to info!!.channel.id!!).exec {
                        if (parseSingle(IntParser) == 0) {
                            dbHelper.writableDatabase.insert("msgColorMap",
                                    "userId" to it.id,
                                    "channelId" to info!!.channel.id!!,
                                    "colorCode" to "#CE93D8")
//                            "#d4d4d4"
                        }
                    }
        }
        dbHelper.use {
//            update("msgColorMap",
//                    "colorCode" to "#d4d4d4")
//                    .whereArgs("channelId = ${info!!.channel.id!!}").exec()

            select("msgColorMap", "userId", "colorCode" )
                    .whereArgs("channelId = ${info!!.channel.id!!}")
                    .exec {
                        var a = parseList(ColorDataParser())
                        a.forEach {
                            userMapper[it.userId] = it.colorCode
                        }
                    }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onStart() {
        super.onStart()
        email = mAuth?.currentUser?.email ?: throw Exception("Not Logged in")
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
                var intent = Intent(applicationContext, ChannelSettingActivity::class.java)
                intent.apply {
                    putExtra("id", channelId)
                    putExtra("name", channelName)
                    putExtra("token", intent.getStringExtra("token"))
                }
                startActivity(intent)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
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
                    info!!.messages.add(it)
                    messageAdapter!!.notifyDataSetChanged()
                    chat_recycler_view.scrollToPosition(messageAdapter!!.itemCount-1)
                }, {
                    Log.e("hogehoge", "error", it)
                }, {
                    Log.d("hogehoge", "completed")
                })
    }

}
