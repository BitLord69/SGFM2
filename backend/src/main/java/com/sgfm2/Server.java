package com.sgfm2;

import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.namespace.Namespace;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;

import java.util.*;

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
      public void onConnect(SocketIOClient client) {
        System.out.println("Wääääoooww, client connected!!!! " + client.getSessionId());

        //Increase roomno 2 clients are present in a room.
        //SocketIONamespace testPlupp = server.getNamespace(Namespace.DEFAULT_NAME);
        BroadcastOperations bcO = server.getRoomOperations("room-" + roomNo);
        Collection<SocketIOClient> clients = null;
        if (bcO != null) {
          clients = bcO.getClients();
          if (clients.size() > 1) roomNo++;
        }

        client.joinRoom("room-" + roomNo);
        System.out.println("rooms " + client.getAllRooms());

        System.out.println("roomno: " + roomNo);
        System.out.println("clients.size: " + clients.size());

//        String data = "hello there! " + roomNo;
//        Packet packet = new Packet(PacketType.EVENT);
//        packet.setName("message");
//        packet.setData(Collections.singletonList("hejhopp"));
//        //server.getRoomOperations("room-" + roomNo).sendEvent("message", "HEJHEJ");
//        server.getBroadcastOperations().sendEvent("message", "HEJHEJ");
//        // client.sendEvent("message", new Object[]{"HEJHEJ"});
//        System.out.println("efter sendEvent");
      }
    });

    server.addDisconnectListener(new DisconnectListener() {
      @Override
      public void onDisconnect(SocketIOClient client) {
        System.out.println("client disconnected " + client.getSessionId());
      }
    });

    server.addEventListener("greeting", String.class, new DataListener<String>() {
      @Override
      public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
        System.out.println("message from client, in addEventListener " + data);
      }
    });
  }

  public void sendMsg() {
    server.getBroadcastOperations().sendEvent("weekendGreeting", "supaaaaaa");
  }

  public void start() {
    server.start();
  }

  public void stop() {
    server.stop();
  }
}
