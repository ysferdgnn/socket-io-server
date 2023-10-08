package com.yusuf.socketioserver.service;

import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.yusuf.socketioserver.enums.ROOM;
import com.yusuf.socketioserver.enums.UserType;
import com.yusuf.socketioserver.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.yusuf.socketioserver.module.Globals.clients;

@Service
@Slf4j
public class UserModuleEventService {
    private final String event = "user";
    private final String roomParamName = "room";
    private final String userParamName = "username";

    public ConnectListener onConnected() {
        return client -> {
            Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
            List<String> strings = urlParams.get(roomParamName);
            String room = urlParams.get(roomParamName).get(0);
            String username= urlParams.get(userParamName).get(0);

            log.info("Client connected. SessionId:{}", client.getSessionId());
            log.info("Control room data :{} for sessionId:{}",room,client.getSessionId());
            try {
                ROOM roomEnum = ROOM.valueOf(room);
            } catch (Exception e) {
                throw new RuntimeException("Room requirements not met. Room was"+room);
            }
            User user = new User(client,username,room, UserType.USER);
            if (clients.stream().noneMatch(s -> s.username().contentEquals(username) && s.room().contentEquals(room))){
                clients.add(user);
                log.info("SessionId:{} successfully connected to room:{}",client.getSessionId(),room);
            }

        };
    }

    public DisconnectListener onDisConnect() {
        return client -> {

            log.info("Client disconnected. SessionId:{}", client.getSessionId());
        };
    }
}
