package com.line.fukuokabclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.Adapter.ChannelsAdapter
import com.line.fukuokabclient.client.ChannelClient
import kotlinx.android.synthetic.main.activity_channel_list.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ChannelListActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_list)

        val id = intent.getLongExtra("id", 0)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView_channels.layoutManager = linearLayoutManager
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        val channelClient = retrofit.create(ChannelClient::class.java)

        channelClient.getPublicChannel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    recyclerView_channels.adapter = ChannelsAdapter(it, id)
                }, {

                })
    }
}
