package com.sgfm2;

import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.*;

public class Server {

  final SocketIOServer server;
  private int roomNo = 0;

  public Server() {
    Configuration config = new Configuration();
    //config.setAllowCustomRequests(true);
    config.setHostname("localhost");
    config.setPort(9092);
    server = new SocketIOServer(config);

    server.addConnectListener(new ConnectListener() {
      @Override
      public void onConnect(SocketIOClient socketIOClient) {
        System.out.println("Wääääoooww, client connected!!!!");

        //Increase roomno 2 clients are present in a room.
        if (server.getNamespace("/").getRoomOperations("room-" + roomNo).getClients().size() > 1) roomNo++;

        socketIOClient.joinRoom("room-" + roomNo);

        String data = "hello there! " + roomNo;
        server.getNamespace("/").getRoomOperations("room-" + roomNo).sendEvent("HELLO", data);
      }
    });
  }

  public void start() {
    server.start();
  }

  public void stop() {
    server.stop();
  }
}
