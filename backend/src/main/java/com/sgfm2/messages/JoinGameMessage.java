package com.sgfm2.messages;

public class JoinGameMessage {
  private String name, roomNo;

  public JoinGameMessage() {
  }

  public JoinGameMessage(String name, String roomNo) {
    this.name = name;
    this.roomNo = roomNo;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRoomNo() {
    return roomNo;
  }

  public void setRoomNo(String roomNo) {
    this.roomNo = roomNo;
  }

  @Override
  public String toString() {
    return "JoinGameMessage{" +
            "name='" + name + '\'' +
            ", roomNo='" + roomNo + '\'' +
            '}';
  }
}
