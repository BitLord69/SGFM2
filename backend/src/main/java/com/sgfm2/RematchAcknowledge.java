package com.sgfm2;

import com.sgfm2.gameengine.GameEngine;

public class RematchAcknowledge {
  private boolean player1;
  private boolean player2;
  private GameEngine gameEngine;

  public RematchAcknowledge() {
    this.player1 = false;
    this.player2 = false;
    this.gameEngine = null;
  }

  public void player1Accept() {
    this.player1 = true;
  }

  public void player2Accept() {
    this.player2 = true;
  }

  public boolean isReadyToGo() {
    return player1 && player2;
  }

  public GameEngine getGameEngine() {
    return gameEngine;
  }

  public void setGameEngine(GameEngine gameEngine) {
    this.gameEngine = gameEngine;
  }
}
