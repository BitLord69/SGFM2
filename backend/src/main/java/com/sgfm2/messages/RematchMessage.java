package com.sgfm2.messages;

public class RematchMessage
{
  private String name;
  private int avatarId;

  public RematchMessage() {
  }

  public RematchMessage(String name, int avatarId) {
    this.name = name;
    this.avatarId = avatarId;
  }

  public String getName() {
    return name;
  }

  public int getAvatarId() {
    return avatarId;
  }

  @Override
  public String toString() {
    return "RematchMessage{" +
        "name='" + name + '\'' +
        ", avatarId=" + avatarId +
        '}';
  }
}
