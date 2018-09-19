package com.line.fukuokabclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.line.fukuokabclient.Client.APIFactory
import com.line.fukuokabclient.Client.ChannelClient
import com.line.fukuokabclient.Utility.Prefs
import kotlinx.android.synthetic.main.activity_channel_setting.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ChannelSettingActivity : AppCompatActivity() {
    var channelClient: ChannelClient? = null
    var channelId: Long = 0
    var channelName: String = ""
    var token: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_setting)

        group_setting_toolbar.setTitle("グループ設定")
        setSupportActionBar(group_setting_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.getLongExtra("id", 0) != null) {
            channelId = intent.getLongExtra("id", 0)
            Log.d("id", channelId.toString())
        }

        if (intent.getStringExtra("name") != null) {
            channelName = intent.getStringExtra("name")
            Log.d("name", channelName)
        }

        if (intent.getStringExtra("token") != null) {
            token = intent.getStringExtra("token")
        }


        channelClient = APIFactory.build(token).create(ChannelClient::class.java)
        group_name.setText(channelName)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_setting_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.btn_update_group_profile -> {
                val body = HashMap<String, String>()
                body["name"] = group_name.text.toString()
                channelClient!!.updateChannelName(channelId, body)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Toast.makeText(this, "変更が保存されました", Toast.LENGTH_LONG).show()
                            Log.d("groupNameChanged", "SUCCESS")
                        },{
                            Toast.makeText(this, "変更に失敗しました", Toast.LENGTH_LONG).show()
                            Log.d("groupNameChanged", "FAILED " + it.toString())
                        })
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
