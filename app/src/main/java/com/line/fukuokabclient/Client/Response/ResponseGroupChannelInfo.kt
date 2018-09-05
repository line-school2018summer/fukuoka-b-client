package com.line.fukuokabclient.Client.Response

import com.line.fukuokabclient.dto.ChannelDTO
import com.line.fukuokabclient.dto.UserDTO

data class ResponseGroupChannelInfo (
        val users: List<UserDTO>,
        val channel: ChannelDTO
)