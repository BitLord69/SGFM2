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
import com.sgfm2.messages.JoinGameMessage;
import com.sgfm2.messages.ListGamesMessage;
import com.sgfm2.messages.Message;

import java.util.*;
import java.util.stream.Collectors;

public class Server {

  final SocketIOServer server;
  private int roomNo = 0;
  private HashMap<String, GameEngine> games = new HashMap<>();
  private HashMap<String, ListGamesMessage> roomList = new HashMap<>();

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
        System.out.printf("Client %s disconnected.\n", client.getSessionId());
        Object[] rooms = client.getAllRooms().toArray();
        if(rooms.length > 1)  {
          System.out.println("Client room " + client.getAllRooms().toArray()[1]);
        } else {
          System.out.println("Client has no room number!");
        }

        client.disconnect();
      }
    });

    server.addEventListener("CREATE_GAME", String.class, new DataListener<String>() {
      @Override
      public void onData(SocketIOClient client, String name, AckRequest ackSender) throws Exception {
        System.out.println("CREATE_GAME: " + name);
        roomNo++;

        GameEngine gameEngine = new GameEngine(localThis,5,10, roomNo);
        gameEngine.setPlayer(new Player(name));
        games.put(String.valueOf(roomNo), gameEngine);
        roomList.put(String.valueOf(roomNo), new ListGamesMessage(String.valueOf(roomNo), 1, 10, 5, name));
        client.joinRoom(String.valueOf(roomNo));
        System.out.println("rooms " + client.getAllRooms());
        System.out.println("roomno: " + roomNo);

        Packet p = new Packet(PacketType.MESSAGE);
        p.setSubType(PacketType.EVENT);
        p.setName("message");
        p.setData("Welcome to room # " + roomNo + " - waiting for an opponent!");
        client.send(p);

        sendMsgToRoom("Welcome to room # " + roomNo + " - waiting for an opponent!", roomNo);
        sendMsgToRoom(gameEngine.getGameState(), roomNo);

        BroadcastOperations bco = server.getBroadcastOperations();
        bco.getClients().forEach(x -> {
          System.out.println(x.getSessionId());
          x.sendEvent("LIST_GAMES" , getGameList());
        });

        //server.getBroadcastOperations().sendEvent("LIST_GAMES" , getGameList());
        System.out.println("Message should be sent from the server.....");
        //        gameEngine.setPlayer(new Player(name));
      }
    });

    server.addEventListener("JOIN_GAME", JoinGameMessage.class, new DataListener<JoinGameMessage>() {
      @Override
      public void onData(SocketIOClient client, JoinGameMessage data, AckRequest ackSender) throws Exception {
        System.out.println("JOIN_GAME: " + data.getRoomNo());
        ListGamesMessage lgm = roomList.get(data.getRoomNo());
        lgm.setPlayersInRoom(2);
        //roomList.put(data.getRoomNo(), lgm);
        client.joinRoom(String.valueOf(data.getRoomNo()));
        GameEngine gameEngine = games.get(data.getRoomNo());
        gameEngine.setPlayer(new Player(data.getName()));

        sendMsgToRoom("Welcome " + data.getName() + " to room # " + roomNo + " - starting game!", Integer.parseInt(data.getRoomNo()));

        gameEngine.startGame();
        sendMsgToRoom(gameEngine.getGameState(), Integer.parseInt(data.getRoomNo()));
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
        Set<String> rooms = client.getAllRooms();
        GameEngine gameEngine = games.get(String.valueOf(rooms.toArray()[1]));
        gameEngine.setPlayedCard(Integer.parseInt(data));
      }
    });

    server.addEventListener("AVAILABLE_GAMES", String.class, new DataListener<String>() {
      @Override
      public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
        client.sendEvent("LIST_GAMES",  getGameList());
      }
    });
  }

  private String getGameList() {
    List<ListGamesMessage> listGamesMessages = roomList.values().stream()
        .filter(listGamesMessage -> listGamesMessage.getPlayersInRoom() < 2).collect(Collectors.toList());
    return new Gson().toJson(listGamesMessages);
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
