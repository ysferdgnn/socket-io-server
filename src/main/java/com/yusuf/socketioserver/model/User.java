package com.yusuf.socketioserver.model;

import com.corundumstudio.socketio.SocketIOClient;
import com.yusuf.socketioserver.enums.UserType;
import lombok.Data;


public record User (
     SocketIOClient socketIOClient,
     String username,
     String room,
     UserType type
){}
