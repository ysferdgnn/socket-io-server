package com.yusuf.socketioserver.module;


import com.corundumstudio.socketio.SocketIOServer;
import com.yusuf.socketioserver.service.SocketIoEventService;
import com.yusuf.socketioserver.service.UserModuleEventService;
import org.springframework.stereotype.Component;

@Component
public class UserModule {

    private final SocketIOServer socketIOServer;


    private final UserModuleEventService userModuleEventService;



    public UserModule(SocketIOServer socketIOServer, UserModuleEventService userModuleEventService) {
        this.socketIOServer = socketIOServer;

        this.userModuleEventService = userModuleEventService;
        socketIOServer.addConnectListener(userModuleEventService.onConnected());
        socketIOServer.addDisconnectListener(userModuleEventService.onDisConnect());



    }


}
