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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Server {

  private int roomNo = 0;
  final SocketIOServer server;
  private final HashMap<String, GameEngine> games = new HashMap<>();
  private final HashMap<String, ListGamesMessage> roomList = new HashMap<>();

  public Server() {
    Configuration config = new Configuration();
    config.setHostname("localhost");
    config.setPort(9092);
    config.setPingInterval(30);
    server = new SocketIOServer(config);
    final Server localThis = this;

    server.addConnectListener(client -> {
      System.out.printf("Wääääoooww, client with id %s and token %s connected!!!! \n", client.getSessionId(), getToken(client));
      System.out.println("Connected with token: " + client.getHandshakeData().getSingleUrlParam("token"));
      List<ListGamesMessage> lgm = roomList.values().stream().filter(r -> r.hasClient(getToken(client))).collect(Collectors.toList());
      if (lgm.size() > 0) {
        System.out.println("Client is already connected to game...");
        client.sendEvent("ALREADY_CONNECTED_TO_GAME");
      }
    });

    server.addDisconnectListener(client -> {
      System.out.printf("Client %s disconnected.\n", client.getSessionId());
      List<ListGamesMessage> lgm = roomList.values().stream().filter(r -> r.hasClient(getToken(client))).collect(Collectors.toList());
      if (lgm.size() > 0) {
        String room = lgm.get(0).getRoomNo();
        lgm.get(0).removeClient(getToken(client));

        BroadcastOperations bcO = server.getRoomOperations(String.valueOf(roomNo));
        Collection<SocketIOClient> clients = bcO.getClients();
        clients.forEach(c -> c.sendEvent("OPPONENT_DISCONNECTED"));

        if (lgm.get(0).getPlayersInRoom() == 0) {
          games.remove(room);
          roomList.remove(room);
        }
      }
    });

    server.addEventListener("CREATE_GAME", CreateGameMessage.class, (client, data, ackSender) -> {
      roomNo++;

      // TODO: 2020-12-14 Do we need a new thread here????
      GameEngine gameEngine = new GameEngine(localThis, data.getCardsOnHand(),data.getPointsToWin(), roomNo);
      gameEngine.setPlayer(new Player(data.getName()));
      games.put(String.valueOf(roomNo), gameEngine);

      ListGamesMessage lgm = new ListGamesMessage(String.valueOf(roomNo), 1, data);
      lgm.addClient(getToken(client));
      roomList.put(String.valueOf(roomNo), lgm);

      client.joinRoom(String.valueOf(roomNo));

      server.getBroadcastOperations().
          getClients().
          forEach(x -> { x.sendEvent("LIST_GAMES" , getGameList());
          });

      client.sendEvent("GAME_UPDATE" , new Gson().toJson(gameEngine.getGameState()));
    });

    server.addEventListener("JOIN_GAME", JoinGameMessage.class, (client, data, ackSender) -> {
      ListGamesMessage lgm = roomList.get(data.getRoomNo());
      lgm.setPlayersInRoom(2);
      lgm.addClient(getToken(client));

      client.joinRoom(String.valueOf(data.getRoomNo()));

      GameEngine gameEngine = games.get(data.getRoomNo());
      gameEngine.setPlayer(new Player(data.getName()));

      gameEngine.startGame();

      // Send updated list of games to all clients (since this one is not available any longer)
      server.getBroadcastOperations().
          getClients().
          forEach(x -> { x.sendEvent("LIST_GAMES" , getGameList()); });

      sendGameUpdateToRoom(gameEngine.getGameState(), Integer.parseInt(data.getRoomNo()));
    });

    server.addEventListener("send", Message.class, (client, data, ackSender) -> {
      server.getBroadcastOperations().sendEvent("message", data);
    });

    server.addEventListener("PLAYED_CARD", String.class, (client, data, ackSender) -> {
      System.out.println("Played card: " + client.getHandshakeData().getSingleUrlParam("token"));


      Set<String> rooms = client.getAllRooms();
      System.out.println("client uuid: " + client.getSessionId());
      System.out.println("rooms.size: " + rooms.size());
      System.out.println("rooms: " + rooms);
      GameEngine gameEngine = games.get(String.valueOf(rooms.toArray()[1]));
      gameEngine.setPlayedCard(Integer.parseInt(data));
    });

    server.addEventListener("AVAILABLE_GAMES", String.class,
        (client, data, ackSender) -> client.sendEvent("LIST_GAMES",  getGameList()));

    server.addEventListener("FORCE_DISCONNECT", String.class,
      (client, data, ackSender) -> {
      client.disconnect();
      System.out.printf("Client %s, with token %s, disconnected in FORCE_DISCONNECT.\n", client.getSessionId(), getToken(client));
    });
  }

  private String getGameList() {
    List<ListGamesMessage> listGamesMessages = roomList.values().stream()
        .filter(listGamesMessage -> listGamesMessage.getPlayersInRoom() < 2).collect(Collectors.toList());
    return new Gson().toJson(listGamesMessages);
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

  private String getToken(SocketIOClient client) {
    return client.getHandshakeData().getSingleUrlParam("token");
  }

  public void start() {
    server.start();
  }

  public void stop() {
    server.stop();
  }
}
