package com.yusuf.socketioserver.schedulers;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.yusuf.socketioserver.enums.UserType;
import com.yusuf.socketioserver.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static com.yusuf.socketioserver.module.Globals.clients;

@Component
@Slf4j
public class UserInfoScheduler {

  private final SocketIOServer socketIOServer;

  public UserInfoScheduler(SocketIOServer socketIOServer) {
    this.socketIOServer = socketIOServer;
  }

  @Scheduled(fixedRate = 1000)
  public void sendUserDataAllClients() {
    clients.forEach(
        client -> {
          socketIOServer
              .getRoomOperations(client.room())
              .sendEvent(
                  "user",
                  clients.stream()
                      .filter(user -> user.type() == UserType.MESSAGE)
                          .filter(user -> user.room().contentEquals(client.room()))
                      .map(User::username)
                      .collect(Collectors.toList()));
        });
  }
}
