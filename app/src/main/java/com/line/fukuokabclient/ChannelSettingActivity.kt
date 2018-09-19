package com.line.fukuokabclient

import android.app.AlertDialog
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.line.fukuokabclient.Client.APIFactory
import com.line.fukuokabclient.Client.ChannelClient
import com.line.fukuokabclient.Client.Response.ResponseChannelInfo
import com.line.fukuokabclient.Dialog.ChannelNameDialogHelper
import com.line.fukuokabclient.Dialog.ColorChangeDialogHelper
import com.line.fukuokabclient.Fragments.ChannelSettingFragment
import com.line.fukuokabclient.Fragments.ChannelSettingUserFragment
import com.line.fukuokabclient.Utility.Prefs
import com.line.fukuokabclient.db.DbOpenHelper
import com.line.fukuokabclient.dto.UserDTO
import kotlinx.android.synthetic.main.activity_channel_setting.*
import kotlinx.android.synthetic.main.fragment_channel_setting.*
import org.jetbrains.anko.db.update
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ChannelSettingActivity : AppCompatActivity(), ChannelSettingFragment.OnFragmentInteractionListener, ChannelSettingUserFragment.OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(user: UserDTO?) {
        showColorChangeDialog(user!!)
    }

    override fun onFragmentInteraction(item: String) {
        when(item) {
            "item1" -> {
                showChannelNameDialog()
            }
            "item2" -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                transaction.replace(channel_setting_fragment_container.id, ChannelSettingUserFragment.newInstance(1, info!!.users))
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }

    var channelClient: ChannelClient? = null
    var token: String = ""
    var info: ResponseChannelInfo? = null

    var channelNameDialog: AlertDialog? = null
    var colorChangeDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_setting)

        group_setting_toolbar.setTitle("グループ設定")
        setSupportActionBar(group_setting_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        token = Prefs.get(applicationContext).getString("token", "")!!

        channelClient = APIFactory.build(token).create(ChannelClient::class.java)

        info = intent.getParcelableExtra("info")

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(channel_setting_fragment_container.id, ChannelSettingFragment.newInstance(info!!.channel.name, ""))
        transaction.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.group_setting_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.btn_update_group_profile -> {
                finish()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun updateChannelName(name: String) {
        if (name != info!!.channel.name) {
            val body = HashMap<String, String>()
            body["name"] = name
            channelClient!!.updateChannelName(info!!.channel.id!!, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Toast.makeText(this, "変更が保存されました", Toast.LENGTH_LONG).show()
                        channel_setting_channel_name.text = name
                        info!!.channel.name = name
                        Log.d("groupNameChanged", "SUCCESS")
                        finish()
                    },{
                        Toast.makeText(this, "変更に失敗しました", Toast.LENGTH_LONG).show()
                        Log.d("groupNameChanged", "FAILED " + it.toString())
                    })
        }
    }

    private fun showChannelNameDialog() {
        if (channelNameDialog == null) {
            channelNameDialog = ChannelNameDialogHelper(this).apply {
                edtName.setText(info!!.channel.name)

                btnCancelOnClick {
                    Log.d("DIALOG", "cancel")
                }

                btnOkOnClick {
                    updateChannelName(edtName.text.toString())
                }
            }.create()

            channelNameDialog?.show()
        } else {
            channelNameDialog?.show()
        }
    }

    private fun showColorChangeDialog(user: UserDTO) {
            colorChangeDialog = ColorChangeDialogHelper(this).apply {
                with(rbSakura) {
                    text = SpannableString(text.toString()).apply {
                        setSpan(ForegroundColorSpan(Color.parseColor("#fedfe1")), 0, text.toString().length, 0)
                    }
                }

                with(rbYama) {
                    text = SpannableString(text.toString()).apply {
                        setSpan(ForegroundColorSpan(Color.parseColor("#d19826")), 0, text.toString().length, 0)
                    }
                }

                with(rbNae) {
                    text = SpannableString(text.toString()).apply {
                        setSpan(ForegroundColorSpan(Color.parseColor("#86c166")), 0, text.toString().length, 0)
                    }
                }

                with(rbAotake) {
                    text = SpannableString(text.toString()).apply {
                        setSpan(ForegroundColorSpan(Color.parseColor("#00896c")), 0, text.toString().length, 0)
                    }
                }

                with(rbSora) {
                    text = SpannableString(text.toString()).apply {
                        setSpan(ForegroundColorSpan(Color.parseColor("#58b2dc")), 0, text.toString().length, 0)
                    }
                }

                isBackgroundTransparent = false

                btnCancelOnClick {
                    Log.d("DIALOG", "cancel")
                }

                btnOkOnClick {
                    var dbHelper = DbOpenHelper.getInstance(applicationContext)
                    var color:String = when (radioGroup.checkedRadioButtonId) {
                        R.id.radio_sakura_fedfe1 -> "#fedfe1"
                        R.id.radio_yamabukicya_d19826 -> "#d19826"
                        R.id.radio_nae_86c166 -> "#86c166"
                        R.id.radio_aotake_00896c -> "#00896c"
                        R.id.radio_sora_58b2dc -> "#58b2dc"
                        else -> "#bef18c"
                    }
                    dbHelper.use {
                        update("msgColorMap",
                        "colorCode" to color)
                        .whereArgs("(channelId = ${info!!.channel.id!!}) and (userId = ${user.id})").exec()
                    }
                }
            }.create()

            colorChangeDialog?.show()
    }
}
