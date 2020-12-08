package com.sgfm2;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.google.gson.Gson;
import com.sgfm2.gameengine.GameEngine;
import com.sgfm2.gameobjects.GameState;
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
      } // onConnect
    });

    server.addDisconnectListener(new DisconnectListener() {
      @Override
      public void onDisconnect(SocketIOClient client) {
        System.out.println("onDisconnected");
      }
    });

    server.addEventListener("CREATE_GAME", String.class, new DataListener<String>() {
      @Override
      public void onData(SocketIOClient client, String name, AckRequest ackSender) throws Exception {
        System.out.println("CREATE_GAME: " + name);
        roomNo++;

        GameEngine gameEngine = new GameEngine(localThis,5,10, roomNo);
        games.put(String.valueOf(roomNo), gameEngine);

        client.joinRoom(String.valueOf(roomNo));
        System.out.println("rooms " + client.getAllRooms());
        System.out.println("roomno: " + roomNo);

        Packet p = new Packet(PacketType.MESSAGE);
        p.setSubType(PacketType.EVENT);
        p.setName("message");
        p.setData("Welcome to room # " + roomNo + " - waiting for an opponent!");
        client.send(p);

        sendMsgToRoom("Welcome to room # " + roomNo + " - waiting for an opponent!", roomNo);
        
        server.getBroadcastOperations().sendEvent("message", "Here is a message from the server");
        System.out.println("Message should be sent from the server.....");
        //        gameEngine.setPlayer(new Player(name));
      }
    });

    server.addEventListener("JOIN_GAME", String.class, new DataListener<String>() {
      @Override
      public void onData(SocketIOClient client, String roomNo, AckRequest ackSender) throws Exception {
        System.out.println("JOIN_GAME: " + roomNo);

        client.joinRoom(String.valueOf(roomNo));
        GameEngine gameEngine = games.get(roomNo);
        gameEngine.setPlayer(new Player(roomNo));

        sendMsgToRoom("Welcome to room # " + roomNo + " - starting game!", Integer.parseInt(roomNo));

        gameEngine.startGame();
      }
    });

    server.addEventListener("send", Message.class, new DataListener<Message>() {
      @Override
      public void onData(SocketIOClient client, Message data, AckRequest ackSender) throws Exception {
        System.out.println("onSend: " + data.toString());
        server.getBroadcastOperations().sendEvent("message", data);
      }
    });

//    server.addEventListener("SEND_PLAYER_NAME", String.class, new DataListener<String>() {
//      @Override
//      public void onData(SocketIOClient client, String name, AckRequest ackSender) throws Exception {
//        System.out.println("SEND_PLAYER_NAME: " + name);
//        Set<String> rooms = client.getAllRooms();
//       GameEngine gameEngine = games.get(String.valueOf(rooms.toArray()[1]));
//       gameEngine.setPlayer(new Player(name));
//
//        if (getNrClientsInRoom(roomNo) == 2) {
//          gameEngine.startGame();
//        }
//      }
//    });

    server.addEventListener("PLAYED_CARD", String.class, new DataListener<String>() {
      @Override
      public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
        System.out.println("PLAYED_CARD: " + data);
        client.sendEvent("GAME_UPDATE", new Gson().toJson("Thanks for the card..." + data));
        // update gamestate with the sent card
        // check if playedCards > 1, in that case calculate roundwinner
        // change currentplayer, return gamestate to room
//        Set<String> rooms = client.getAllRooms();
//        GameEngine gameEngine = games.get(String.valueOf(rooms.toArray()[1]));
//        gameEngine.setPlayedCard(Integer.parseInt(data));
      }
    });
  }

  private int getNrClientsInRoom(int roomNo) {
    BroadcastOperations bcO = server.getRoomOperations(String.valueOf(roomNo));
    Collection<SocketIOClient> clients = bcO.getClients();
    return clients == null ? 0 : clients.size();
  }

  public void sendMsgToRoom(GameState gameState, int roomNo) {
    BroadcastOperations bcO = server.getRoomOperations(String.valueOf(roomNo));
    Collection<SocketIOClient> clients = bcO.getClients();
    clients.forEach(client -> client.sendEvent("GAME_UPDATE", new Gson().toJson(gameState)));
  }

  public void sendMsgToRoom(String message, int roomNo ) {
    BroadcastOperations bcO = server.getRoomOperations(String.valueOf(roomNo));
    Collection<SocketIOClient> clients = bcO.getClients();
    clients.forEach(client -> client.sendEvent("message", message));
  }

  public void start() {
    server.start();
  }

  public void stop() {
    server.stop();
  }
}
