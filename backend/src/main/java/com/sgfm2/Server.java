package com.sgfm2;

import com.google.gson.Gson;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.sgfm2.gameengine.GameEngine;
import com.sgfm2.gameobjects.GameState;
import com.sgfm2.gameobjects.Player;
import com.sgfm2.messages.CreateGameMessage;
import com.sgfm2.messages.JoinGameMessage;
import com.sgfm2.messages.ListGamesMessage;
import com.sgfm2.messages.Message;

import java.util.*;
import java.util.stream.Collectors;

public class Server {

  final SocketIOServer server;
  private int roomNo = 0;
  private final HashMap<String, GameEngine> games = new HashMap<>();
  private final HashMap<String, ListGamesMessage> roomList = new HashMap<>();

  public Server() {
    Configuration config = new Configuration();
    config.setHostname("localhost");
    config.setPort(9092);
    config.setPingInterval(30);
    server = new SocketIOServer(config);
    final Server localThis = this;

    // onConnect
    server.addConnectListener(client -> System.out.println("Wääääoooww, client connected!!!! " + client.getSessionId()));

    server.addDisconnectListener(client -> {
      System.out.printf("Client %s disconnected.\n", client.getSessionId());
      //client.disconnect();
    });

    server.addEventListener("CREATE_GAME", CreateGameMessage.class, (client, data, ackSender) -> {
      System.out.println("FÖRSTA RADEN I CREATE_GAME");
      roomNo++;

      GameEngine gameEngine = new GameEngine(localThis, data.getCardsOnHand(),data.getPointsToWin(), roomNo);
      gameEngine.setPlayer(new Player(data.getName()));
      games.put(String.valueOf(roomNo), gameEngine);
      roomList.put(String.valueOf(roomNo), new ListGamesMessage(String.valueOf(roomNo), 1, data));

      System.out.println("innan client.joinroom");
      client.joinRoom(String.valueOf(roomNo));
      System.out.println("efter client.joinroom");

//      Packet p = new Packet(PacketType.MESSAGE);
//      p.setSubType(PacketType.EVENT);
//      p.setName("message");
//      p.setData("Welcome to room # " + roomNo + " - waiting for an opponent!");
//      client.send(p);

//      sendMsgToRoom("Welcome to room # " + roomNo + " - waiting for an opponent!", roomNo);
      BroadcastOperations bco = server.getBroadcastOperations();
      System.out.println("getGameList: " + getGameList());
      System.out.println("getGameState: " + gameEngine.getGameState());
          bco.getClients().
          forEach(x -> {
            x.sendEvent("LIST_GAMES" , getGameList());

          });
        bco.getClients().
          forEach(x -> {

            x.sendEvent("GAME_UPDATE" , new Gson().toJson(gameEngine.getGameState()));
          });

      bco.getClients().
              forEach(x -> {

                x.sendEvent("SEND_ROOMNO" , String.valueOf(roomNo));
              });
    });

    server.addEventListener("JOIN_GAME", JoinGameMessage.class, (client, data, ackSender) -> {
      if (data.getRoomNo() == null) {
        System.out.println("null");
        return;
      }
      ListGamesMessage lgm = roomList.get(data.getRoomNo());
      System.out.println("data: " + data.toString());
      lgm.setPlayersInRoom(2);
      client.joinRoom(String.valueOf(data.getRoomNo()));
      GameEngine gameEngine = games.get(data.getRoomNo());
      gameEngine.setPlayer(new Player(data.getName()));

//      sendMsgToRoom("Welcome " + data.getName() + " to room # " + roomNo + " - starting game!", Integer.parseInt(data.getRoomNo()));

      gameEngine.startGame();

      server.getBroadcastOperations().
          getClients().
          forEach(x -> { x.sendEvent("LIST_GAMES" , getGameList()); });

      sendGameUpdateToRoom(gameEngine.getGameState(), Integer.parseInt(data.getRoomNo()));
    });

    server.addEventListener("send", Message.class, (client, data, ackSender) -> {
      server.getBroadcastOperations().sendEvent("message", data);
    });

    server.addEventListener("PLAYED_CARD", String.class, (client, data, ackSender) -> {
      Set<String> rooms = client.getAllRooms();
      System.out.println("rooms: " + rooms);
      System.out.println("data: " + data);
      GameEngine gameEngine = games.get(String.valueOf(rooms.toArray()[1]));
      gameEngine.setPlayedCard(Integer.parseInt(data));
    });

    server.addEventListener("REQUEST_UPDATE", String.class, (client, data, ackSender) -> {
      if (data == null) {
        return;
      }
      client.joinRoom(data);
      GameEngine gameEngine = games.get(data);
      System.out.println("i REQUEST_UPDATE, data: " + data);
      sendGameUpdateToRoom(gameEngine.getGameState(), Integer.parseInt(data));
    });

    server.addEventListener("AVAILABLE_GAMES", String.class,
            (client, data, ackSender) -> client.sendEvent("LIST_GAMES",  getGameList()));

  } // constructor

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

  public void sendGameUpdateToRoom(GameState gameState, int roomNo) {
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
