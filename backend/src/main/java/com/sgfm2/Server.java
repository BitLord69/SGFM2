package com.sgfm2;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.sgfm2.gameengine.GameEngine;
import com.sgfm2.gameobjects.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Server {

  final SocketIOServer server;
  private int roomNo = 0;
  private HashMap<String, GameEngine> games = new HashMap<>();

  public Server() {
    Configuration config = new Configuration();
    config.setHostname("localhost");
    config.setPort(9092);
    server = new SocketIOServer(config);
    final Server localThis = this;

    server.addConnectListener(new ConnectListener() {
      @Override
      public void onConnect(SocketIOClient client) {
        System.out.println("Wääääoooww, client connected!!!! " + client.getSessionId());
        //Increase roomno 2 clients are present in a room.
        int clients = getNrClientsInRoom(roomNo);
        System.out.println("!clients.size: " + clients);
        if (clients > 1) {
          roomNo++;
          if (clients == 2) {
            games.put(String.valueOf(roomNo), new GameEngine(localThis, 5,10));
          }
        } else {
          // TODO: 2020-12-07 add proper initializers to GameEngine
          games.put(String.valueOf(roomNo), new GameEngine(localThis,5,10));
        }

        client.joinRoom(String.valueOf(roomNo));
        System.out.println("rooms " + client.getAllRooms());

        System.out.println("roomno: " + roomNo);

        server.getBroadcastOperations().sendEvent("message", new Message("Room message", "Welcome to room # " + roomNo + "!"));
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

    server.addEventListener("SEND_PLAYER_NAME", String.class, new DataListener<String>() {
      @Override
      public void onData(SocketIOClient client, String name, AckRequest ackSender) throws Exception {
        System.out.println("SEND_PLAYER_NAME: " + name);
        Set<String> rooms = client.getAllRooms();
        games.get(String.valueOf(rooms.toArray()[1])).setPlayer(new Player(name));
        if (getNrClientsInRoom(1) == 2) {
          System.out.println("Game is ready to start!");
        }

      }
    });
  }

  private int getNrClientsInRoom(int roomNo) {
    BroadcastOperations bcO = server.getRoomOperations(String.valueOf(roomNo));
    Collection<SocketIOClient> clients = bcO.getClients();
    return clients == null ? 0 : clients.size();
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
