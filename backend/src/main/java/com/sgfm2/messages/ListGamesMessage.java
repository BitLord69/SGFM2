package com.sgfm2.messages;

import java.util.ArrayList;

public class ListGamesMessage {
  private String roomNo;
  private ArrayList<String> clients = new ArrayList<>();
  private int playersInRoom;
  private int pointsToWin;
  private int cardsOnHand;
  private String creator;

  public ListGamesMessage() {
  }

  public ListGamesMessage(String roomNo, int playersInRoom, int pointsToWin, int cardsOnHand, String creator) {
    this.roomNo = roomNo;
    this.playersInRoom = playersInRoom;
    this.pointsToWin = pointsToWin;
    this.cardsOnHand = cardsOnHand;
    this.creator = creator;
  }

  public String getRoom(String token) {
    return !clients.contains(token) ? null : roomNo;
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

  public void addClient(String token) {
    this.clients.add(token);
  }

  public boolean hasClient(String token) {
    return clients.contains(token);
  }

  public void removeClient(String sessionId) {
    boolean b = clients.remove(sessionId);
    if (b) {
      playersInRoom--;
    }
  }
}
