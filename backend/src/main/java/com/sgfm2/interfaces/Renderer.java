package com.sgfm2.interfaces;

import com.sgfm2.gameobjects.GameState;

public interface Renderer {
  void continueGame();
  String getPlayerName();
  void render(GameState gameState, int playerToDraw);
  int getCard(GameState gameState, int playerToDraw);
}
