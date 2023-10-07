package com.yusuf.socketioserver.service;

import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.yusuf.socketioserver.enums.ROOM;
import com.yusuf.socketioserver.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumSet;

@Service
@Slf4j
public class SocketIoEventService {
  EnumSet<ROOM> rooms = EnumSet.of(ROOM.SHIRE, ROOM.MORDOR, ROOM.ISENGARD, ROOM.RIVENDELL);
  private final String event = "chat";
  private final String roomParamName = "room";

  public DataListener<Message> onMessageReceived() {
    return (client, data, ackSender) -> {
      String room = client.getHandshakeData().getSingleUrlParam(roomParamName);
      try {
        ROOM roomEnum = ROOM.valueOf(room);
      } catch (Exception e) {
        throw new RuntimeException("Room requirements not met");
      }

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
      log.info(
          "Client send data. SessionId:{}, data:{},room:{}",
          client.getSessionId(),
          data.getMessage(),
          room);

      client.getNamespace().getBroadcastOperations().sendEvent(event, data);
    };
  }

  public ConnectListener onConnected() {
    return client -> {
      String room = client.getHandshakeData().getSingleUrlParam(roomParamName);
      if (!rooms.contains(room)) {
        throw new RuntimeException("Room credentials not met");
      }
      client.joinRoom(room);
      log.info("Client connected. SessionId:{}", client.getSessionId());
    };
  }

  public DisconnectListener onDisConnect() {
    return client -> {
      log.info("Client disconnected. SessionId:{}", client.getSessionId());
    };
  }
}
