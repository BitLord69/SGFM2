package com.sgfm2.messages;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.ArrayList;

public class ListGamesMessage {
  private String roomNo;
  private String creator;
  private int pointsToWin;
  private int cardsOnHand;
  private int playersInRoom;
  private transient ArrayList<String> tokens = new ArrayList<>();
  private transient ArrayList<SocketIOClient> clients = new ArrayList<>();

  public ListGamesMessage() {
  }

  public ListGamesMessage(String roomNo, int playersInRoom, int pointsToWin, int cardsOnHand, String creator) {
    this.roomNo = roomNo;
    this.playersInRoom = playersInRoom;
    this.pointsToWin = pointsToWin;
    this.cardsOnHand = cardsOnHand;
    this.creator = creator;
  }

  public ListGamesMessage(String roomNo, int playersInRoom, CreateGameMessage data) {
    this(roomNo, playersInRoom, data.getPointsToWin(), data.getCardsOnHand(), data.getName());
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
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
    return pointsToWin;
  }

  public void setPointsToWin(int pointsToWin) {
    this.pointsToWin = pointsToWin;
  }

  public int getCardsOnHand() {
    return cardsOnHand;
  }

  public void setCardsOnHand(int cardsOnHand) {
    this.cardsOnHand = cardsOnHand;
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

  public ArrayList<SocketIOClient> getClients() {
    return clients;
  }
}
