package com.line.fukuokabclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.line.fukuokabclient.Adapter.ChannelsAdapter
import com.line.fukuokabclient.dto.ChannelDTO
import kotlinx.android.synthetic.main.activity_channel_list.*

class ChannelListActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_list)

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView_channels.layoutManager = linearLayoutManager
//        recyclerView_channels.adapter = ChannelsAdapter(List<ChannelDTO>(5))
    }
}
