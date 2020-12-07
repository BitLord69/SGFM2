package com.sgfm2.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {
  private int pointsToWin;
  private ArrayList<Player> players = new ArrayList<>();
  private final ArrayList<Card> playedCards = new ArrayList<>();
  private int startPlayer = 0;
  private int currentPlayer = 0;
  private boolean isLocalGame = false;

  // -1: game still going,
  //  0: host (player 1) won,
  //  1: client (player 2) won,
  //  2: game over, a tie
  private int gameWinner = -1;
  private int roundWinner = -1;

  public GameState() {
  }

  public GameState(int pointsToWin, ArrayList<Player> players, boolean isLocalGame) {
    this.players = players;
    this.pointsToWin = pointsToWin;
    this.isLocalGame = isLocalGame;
  }

  public boolean isLocalGame() {
    return isLocalGame;
  }

  public int getCurrentPlayer() {
    return currentPlayer;
  }

  public void setCurrentPlayer(int currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public boolean clearPlayedCards() {
    return playedCards.removeAll(playedCards);
  }

  public boolean addPlayedCard(Card playedCard) {
    return playedCards.add(playedCard);
  }

  public ArrayList<Card> getPlayedCards() {
    return playedCards;
  }

  public int changeStartPlayer() {
    if(this.startPlayer == 1){
      this.startPlayer = 0;
    } else {
      this.startPlayer = 1;
    }
    return this.startPlayer;
  }

  public Player getPlayer(int index){
    return players.get(index);
  }

  public void setPlayer(Player player) {
    players.add(player);
  }

  public int getStartPlayer() {
    return startPlayer;
  }

  public int getPointsToWin() {
    return pointsToWin;
  }

  public int getGameWinner() {
    return gameWinner;
  }

  public void setGameWinner(int gameWinner) {
    this.gameWinner = gameWinner;
  }

  public boolean isGameOver(){
    return getGameWinner() > -1;
  }

  public int getRoundWinner() {
    return roundWinner;
  }

  public void setRoundWinner(int roundWinner) {
    this.roundWinner = roundWinner;
  }

  public boolean isRoundOver() {
    return getRoundWinner() > -1;
  }
}