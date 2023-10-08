package com.yusuf.socketioserver.service;

import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.yusuf.socketioserver.enums.ROOM;
import com.yusuf.socketioserver.enums.UserType;
import com.yusuf.socketioserver.model.Message;
import com.yusuf.socketioserver.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.yusuf.socketioserver.module.Globals.clients;

@Service
@Slf4j
public class SocketIoEventService {
  EnumSet<ROOM> rooms = EnumSet.of(ROOM.SHIRE, ROOM.MORDOR, ROOM.ISENGARD, ROOM.RIVENDELL);
  private final String event = "chat";
  private final String roomParamName = "room";
  private final String userParamName = "username";

  public DataListener<Message> onMessageReceived() {
    return (client, data, ackSender) -> {
      String room = client.getHandshakeData().getSingleUrlParam(roomParamName);
      try {
        ROOM roomEnum = ROOM.valueOf(room);
      } catch (Exception e) {
        throw new RuntimeException("Room requirements not met. Room was "+room);
      }
     // client.getNamespace().getRoomOperations(room).sendEvent(event,data);

      client
          .getNamespace()
          .getRoomOperations(room)
          .getClients()
          .forEach(
              s -> {
                if(!s.getSessionId().equals(client.getSessionId())){
                  s.sendEvent(event, data);
                }

              });


    };
  }

  public ConnectListener onConnected() {
    return client -> {
      log.info("Client connected. SessionId:{}", client.getSessionId());

      Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
      List<String> strings = urlParams.get(roomParamName);
      String room = urlParams.get(roomParamName).get(0);
      String username= urlParams.get(userParamName).get(0);


      log.info("Control room data :{} for sessionId:{}",room,client.getSessionId());
      try {
        ROOM roomEnum = ROOM.valueOf(room);
      } catch (Exception e) {
        throw new RuntimeException("Room requirements not met. Room was "+room);
      }
      client.joinRoom(room);
      User user = new User(client,username,room, UserType.MESSAGE);
      if (clients.stream().noneMatch(s -> s.username().contentEquals(username) && s.room().contentEquals(room))){
        clients.add(user);
        log.info("SessionId:{} successfully connected to room:{}",client.getSessionId(),room);
      }


    };
  }

  public DisconnectListener onDisConnect() {
    return client -> {

      clients.removeIf(s-> s.socketIOClient().getSessionId()==client.getSessionId());
      log.info("Client disconnected. SessionId:{}", client.getSessionId());
    };
  }
}
