package com.yusuf.socketioserver.module;

import com.corundumstudio.socketio.SocketIOServer;
import com.yusuf.socketioserver.model.Message;
import com.yusuf.socketioserver.service.SocketIoEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SocketModule {

    private final SocketIOServer socketIOServer;
    private final SocketIoEventService socketIoEventService;



    public SocketModule(SocketIOServer socketIOServer, SocketIoEventService socketIoEventService) {
        this.socketIOServer = socketIOServer;
        this.socketIoEventService = socketIoEventService;
        socketIOServer.addConnectListener(socketIoEventService.onConnected());
        socketIOServer.addDisconnectListener(socketIoEventService.onDisConnect());
        socketIOServer.addEventListener("chat", Message.class,socketIoEventService.onMessageReceived());

    }



}
