package com.sgfm2.messages;

public class JoinGameMessage {
  private String name;
  private int avatarId;
  private String roomNo;

  public JoinGameMessage() {
  }

  public JoinGameMessage(String name, int avatarId, String roomNo) {
    this.name = name;
    this.avatarId = avatarId;
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

  public int getAvatarId() {
    return avatarId;
  }
}
