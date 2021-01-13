package com.sgfm2.messages;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.ArrayList;

public class ListGamesMessage {
  private String roomNo;
  private int playersInRoom;
  private CreateGameMessage cgm;
  private transient ArrayList<String> tokens = new ArrayList<>();
  private transient ArrayList<SocketIOClient> clients = new ArrayList<>();

  public ListGamesMessage() {
  }

  public ListGamesMessage(String roomNo, int playersInRoom) {
    this.roomNo = roomNo;
    this.playersInRoom = playersInRoom;
  }

  public ListGamesMessage(String roomNo, int playersInRoom, CreateGameMessage cgm) {
    this(roomNo, playersInRoom);
    this.cgm = cgm;
  }

  public String getCreator() {
    return cgm.getName();
  }

  public String getRoomNo() {
    return roomNo;
  }

  public void setRoomNo(String roomNo) {
    this.roomNo = roomNo;
  }

  public int getPlayersInRoom() {
    return playersInRoom;
  }

  public void setPlayersInRoom(int playersInRoom) {
    this.playersInRoom = playersInRoom;
  }

  public int getPointsToWin() {
    return cgm.getPointsToWin();
  }

  public int getCardsOnHand() {
    return cgm.getCardsOnHand();
  }

  public void addToken(String token) {
    this.tokens.add(token);
  }

  public boolean hasToken(String token) {
    return tokens.contains(token);
  }

  public void removeToken(String sessionId) {
    boolean b = tokens.remove(sessionId);
    if (b) {
      playersInRoom--;
    }
  }

  public void removeClient(SocketIOClient client) {
    clients.remove(client);
  }

  public void addClient(SocketIOClient client)  {
    clients.add(client);
  }

  public boolean hasClient(SocketIOClient client) {
    return clients.contains(client);
  }

  public int getClientIndex(SocketIOClient client) {
    return clients.indexOf(client);
  }

  public ArrayList<SocketIOClient> getClients() {
    return clients;
  }

  public CreateGameMessage getGameData() {
    return cgm;
  }
}
