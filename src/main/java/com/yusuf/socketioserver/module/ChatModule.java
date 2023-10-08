package com.yusuf.socketioserver.module;

import com.corundumstudio.socketio.SocketIOServer;
import com.yusuf.socketioserver.model.Message;
import com.yusuf.socketioserver.service.SocketIoEventService;
import org.springframework.stereotype.Component;

@Component
public class ChatModule {

    private final SocketIOServer socketIOServer;
    private final SocketIoEventService socketIoEventService;



    public ChatModule(SocketIOServer socketIOServer, SocketIoEventService socketIoEventService) {
        this.socketIOServer = socketIOServer;
        this.socketIoEventService = socketIoEventService;
        socketIOServer.addConnectListener(socketIoEventService.onConnected());
        socketIOServer.addDisconnectListener(socketIoEventService.onDisConnect());
        socketIOServer.addEventListener("chat", Message.class,socketIoEventService.onMessageReceived());

    }



}
