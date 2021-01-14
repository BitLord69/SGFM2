package com.sgfm2;

import com.google.gson.Gson;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;

import com.sgfm2.messages.*;
import com.sgfm2.gameobjects.Player;
import com.sgfm2.gameengine.GameEngine;
import com.sgfm2.gameobjects.GameState;
import com.sgfm2.utils.TextUtil;

public class Server {
  final SocketIOServer server;
  private final ConcurrentHashMap<String, GameEngine> games = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, ListGamesMessage> roomList = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, RematchAcknowledge> rematchRequests = new ConcurrentHashMap<>();

  public Server() {
    Configuration config = new Configuration();
    //config.setHostname("192.168.1.8");
    config.setHostname("localhost");
    config.setPort(9092);
    config.setPingInterval(30);
    server = new SocketIOServer(config);
    final Server localThis = this;

    server.addConnectListener(client -> {
      System.out.printf("Wääääoooww, client with id %s and token %s connected!!!! \n",
          TextUtil.pimpString(client.getSessionId().toString(), TextUtil.LEVEL_INFO),
          TextUtil.pimpString(getToken(client), TextUtil.LEVEL_INFO));
    });

    server.addDisconnectListener(client -> {
      System.out.printf("Client %s disconnected.\n",
          TextUtil.pimpString(client.getSessionId().toString(), TextUtil.LEVEL_INFO));

      List<ListGamesMessage> lgm = roomList.
          values().
          stream().
          filter(r -> r.hasToken(getToken(client))).
          collect(Collectors.toList());

      if (lgm.size() > 0) {
        lgm.get(0).removeToken(getToken(client));

        String room = lgm.get(0).getRoomNo();
        sendEventToRoom("OPPONENT_DISCONNECTED", room, "");
//        if (lgm.get(0).getPlayersInRoom() == 0) {
        games.remove(room);
        roomList.remove(room);
//          System.out.printf("No more players in room %s, removing game!\n",
          System.out.printf("Removing game from room %s!\n",
            TextUtil.pimpString(room, TextUtil.LEVEL_WARNING));
//        }

        removeRematchRequest(client);
      }
    });

    server.addEventListener("CREATE_GAME", CreateGameMessage.class, (client, data, ackSender) -> {
      String room = getToken(client);

      GameEngine gameEngine = getAndSaveNewGameEngine(localThis, data, room);
      ListGamesMessage lgm = new ListGamesMessage(room, 1, data);

      lgm.addClient(client);
      lgm.addToken(getToken(client));
      roomList.put(room, lgm);

      server.getBroadcastOperations().
          getClients().forEach(x -> x.sendEvent("LIST_GAMES" , getGameList(data.getName())));

      client.sendEvent("GAME_UPDATE" , new Gson().toJson(gameEngine.getGameState()));
    });

    server.addEventListener("JOIN_GAME", JoinGameMessage.class, (client, data, ackSender) -> {
      ListGamesMessage lgm = roomList.get(data.getRoomNo());

      lgm.addClient(client);
      lgm.setPlayersInRoom(2);
      lgm.addToken(getToken(client));

      GameEngine gameEngine = games.get(data.getRoomNo());
      gameEngine.setPlayer(new Player(data.getName(), data.getAvatarId()));

      gameEngine.startGame();

      // Send updated list of games to all clients (since this one is not available any longer)
      server.getBroadcastOperations().
          getClients().
          forEach(x -> { x.sendEvent("LIST_GAMES" , getGameList(data.getName())); });

      sendGameUpdateToRoom(gameEngine.getGameState(), data.getRoomNo());
    });

    server.addEventListener("SEND", Message.class, (client, data, ackSender) -> {
      server.getBroadcastOperations().sendEvent("MESSAGE", data);
    });

    server.addEventListener("PLAYED_CARD", String.class, (client, data, ackSender) -> {
      String room = getRoomNoFromClientToken(getToken(client));
      if (!room.isEmpty()) {
        GameEngine gameEngine = games.get(room);
        gameEngine.setPlayedCard(Integer.parseInt(data));
      }
    });

    server.addEventListener("AVAILABLE_GAMES", String.class,
        (client, data, ackSender) -> client.sendEvent("LIST_GAMES",  getGameList(data)));

    server.addEventListener("FORCE_DISCONNECT", String.class,
      (client, data, ackSender) -> {
      client.disconnect();
      System.out.printf("Client %s, with token %s, disconnected in FORCE_DISCONNECT.\n",
          TextUtil.pimpString(client.getSessionId().toString(), TextUtil.LEVEL_INFO),
          TextUtil.pimpString(getToken(client), TextUtil.LEVEL_INFO));
    });

    server.addEventListener("REMOVE_GAME", null, (client, data, ackSender) -> {
      removeGame(getRoomNoFromClientToken(getToken(client)));
    });

    server.addEventListener("REQUEST_REMATCH", RematchMessage.class, (client, data, ackSender) -> {
      GameEngine ge;
      String room = getRoomNoFromClientToken(getToken(client));

      ListGamesMessage lgm = roomList.get(room);
      RematchAcknowledge reReq = rematchRequests.get(room);

      int clientId = lgm.getClientIndex(client);

      // Have the players already played a re-match? If so, clear the previous game
      if (reReq != null && reReq.isReadyToGo()) {
        removeGame(room);
        rematchRequests.remove(room);
        reReq = null;
      }

      if (reReq == null) {
        reReq = new RematchAcknowledge();
        ge = getAndSaveNewGameEngine(localThis, lgm.getGameData(), room);
        reReq.setGameEngine(ge);
        rematchRequests.put(room, reReq);
      } else {
        ge = reReq.getGameEngine();
      }

      switch (clientId) {
        case 0:
          reReq.player1Accept();
          break;

        case 1:
          reReq.player2Accept();
          ge.setPlayer(new Player(data.getName(), data.getAvatarId()));
          break;

        default:
          System.out.println(TextUtil.pimpString("Unknown client!!!!! " + clientId, TextUtil.LEVEL_WARNING));
          return ;
      } // switch

      if (reReq.isReadyToGo()) {
        ge.startGame();
        sendGameUpdateToRoom(ge.getGameState(), room);
        sendEventToRoom("REMATCH_STARTED", room, "");
      }
    });

    server.addEventListener("DENY_REMATCH", null, (client, data, ackSender) -> {
      removeRematchRequest(client);
      sendEventToOpponent(client, "REMATCH_DENIED", "");
    });
  }

  private void removeRematchRequest(SocketIOClient client) {
    String room = getRoomNoFromClientToken(getToken(client));
    rematchRequests.remove(room);
  }

  private void sendEventToOpponent(SocketIOClient client, String event, String data) {
    String room = getRoomNoFromClientToken(getToken(client));

    List<ListGamesMessage> listGamesMessages = roomList.values().
        stream().
        filter(listGamesMessage -> listGamesMessage.getRoomNo().equals(room)).
        collect(Collectors.toList());
    if (listGamesMessages.size() > 0) {
      listGamesMessages.
          get(0).
          getClients().
          stream().
          filter(c -> !client.equals(c)).
          collect(Collectors.toList()).
          forEach(c -> c.sendEvent(event, data));
    }
  }

  private GameEngine getAndSaveNewGameEngine(Server server, CreateGameMessage data, String room) {
    GameEngine gameEngine = new GameEngine(server, data.getCardsOnHand(), data.getPointsToWin(), room, data.getLeague());
    gameEngine.setPlayer(new Player(data.getName(), data.getAvatarId()));
    games.put(room, gameEngine);
    return gameEngine;
  }

  private String getGameList(String username) {
    List<ListGamesMessage> listGamesMessages = roomList.values().
        stream().
        filter(listGamesMessage -> listGamesMessage.getPlayersInRoom() < 2).
        filter(listGamesMessage -> {
          if (username.startsWith("guest")){
            return listGamesMessage.getCreator().startsWith("guest");
          }
          return !listGamesMessage.getCreator().startsWith("guest");
        }).
        collect(Collectors.toList());
    return new Gson().toJson(listGamesMessages);
  }

  private String getRoomNoFromClientToken(String token) {
    List<ListGamesMessage> listGamesMessages = roomList.values().
        stream().
        filter(listGamesMessage -> listGamesMessage.hasToken(token)).
        collect(Collectors.toList());
    if (listGamesMessages.size() > 0) {
      return listGamesMessages.get(0).getRoomNo();
    }

    return null;
  }

  private void sendEventToRoom(String event, String room, String data) {
    List<ListGamesMessage> listGamesMessages = roomList.values().
        stream().
        filter(listGamesMessage -> listGamesMessage.getRoomNo().equals(room)).
        collect(Collectors.toList());
    if (listGamesMessages.size() > 0) {
      listGamesMessages.get(0).getClients().forEach(client -> client.sendEvent(event, data));
    }
  }

  public void sendGameUpdateToRoom(GameState gameState, String roomNo) {
    sendEventToRoom("GAME_UPDATE", roomNo, new Gson().toJson(gameState));
  }

  public void sendMsgToRoom(String message, String room ) {
    sendEventToRoom("MESSAGE", room, message);
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

  public void removeGame(String roomNo) {
    System.out.println(TextUtil.pimpString("In removeGame: " + roomNo, TextUtil.LEVEL_INFO));
    games.remove(roomNo);
  }
}
