package com.sgfm2;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.namespace.Namespace;

import java.util.Collection;

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
//        SocketIONamespace testPlupp = server.getNamespace(Namespace.DEFAULT_NAME);
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
        server.getBroadcastOperations().sendEvent("message", new Message("Room message", "Welcome to room # " + roomNo + "!"));
//        client.sendEvent("message", new Message("", "Welcome to the chat!"));
        System.out.println("efter sendEvent");
      } // onConnect
    });

    server.addDisconnectListener(new DisconnectListener() {
      @Override
      public void onDisconnect(SocketIOClient client) {
        System.out.println("onDisconnected");
      }
    });

    server.addEventListener("send", Message.class, new DataListener<Message>() {

      @Override
      public void onData(SocketIOClient client, Message data, AckRequest ackSender) throws Exception {
        System.out.println("onSend: " + data.toString());
        server.getBroadcastOperations().sendEvent("message", data);
      }
    });
  }

  public void sendMsg() {
    System.out.println("Before sending the weekend message!");
    server.getBroadcastOperations().sendEvent("message", new Message("Helg!", "Nu är det helg!"));
  }

  public void start() {
    server.start();
  }

  public void stop() {
    server.stop();
  }
}
