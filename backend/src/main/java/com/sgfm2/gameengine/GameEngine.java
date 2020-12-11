package com.sgfm2.gameengine;

import com.sgfm2.Server;
import com.sgfm2.gameobjects.*;

import java.util.ArrayList;
import java.util.List;

public class GameEngine  {

  private final List<CardSettings> cardSettings = new ArrayList<>() {{
    add(new CardSettings(1, "Mutated worm", 8));
    add(new CardSettings(2, "Irate rat", 8));
    add(new CardSettings(3, "Orange menace", 8));
    add(new CardSettings(4, "Sleepy Joe", 10));
    add(new CardSettings(5, "Angry teacher", 10));
    add(new CardSettings(6, "Screaming toddler", 8));
    add(new CardSettings(7, "Space nerd", 8));
    add(new CardSettings(8, "Anonymous hacker", 4));
    add(new CardSettings(9, "Radiated zombie", 4));
    add(new CardSettings(10, "Super Galaxy Face Melter", 2));
  }};


  public static final int TIE = 2;

  private final GameState gameState;

  private final Deck deck = new Deck(cardSettings);
  private final int handSize;
  private final int roomNo;

  private final Server server;

  public GameEngine(Server server, int handSize, int pointsToWin, int roomNo) {
    gameState = new GameState(pointsToWin);
    this.handSize = handSize;
    this.server = server;
    this.roomNo = roomNo;
  }

  public void startGame() {
    dealCards();
    server.sendGameUpdateToRoom(gameState, roomNo);
  }

  public void setPlayer (Player player) {
    gameState.setPlayer(player);
  }

   /**
    *     Jämför korten i gameState och avgör vem, om någon, som vann-
    *     Uppdatera gameState och vinsthögen för vinnande spelare
    *     @return -1 = ingen vann annars 0 eller 1 för respektive spelare
   * */
  public int getRoundWinner() {
    int result = gameState.powerDifference();
    int winner;

    if (result > 0) { winner = gameState.getStartPlayer() == GameState.PLAYER_ONE ? 0 : 1; }
    else if (result < 0) { winner = gameState.getStartPlayer() == GameState.PLAYER_ONE ? 1 : 0; }
    else  { winner = TIE; }

    finalizingRound(winner);
    return winner;
  }


  private int getSecondPlayer() {
    return gameState.getStartPlayer() == GameState.PLAYER_ONE ? GameState.PLAYER_TWO : GameState.PLAYER_ONE;
  }

  public void finalizingRound(int winner) {
    Card card1 = gameState.getPlayedCards().get(0);
    Card card2 = gameState.getPlayedCards().get(1);
    gameState.getPlayer(gameState.getStartPlayer()).getCardOnHandAsList().remove(card1);
    gameState.getPlayer(getSecondPlayer()).getCardOnHandAsList().remove(card2);
    if (winner >= 0) {
      if (winner == gameState.getStartPlayer()) {
        handleWinnerCardForStartPlayer(winner, card1, card2);
      } else if( winner == gameState.getCurrentPlayer() ) {
        handleWinnerCardForSecondPlayer(winner, card2, card1);
      }

      //Deal new card to LOSER, if TIE both players receive a new card.
      switch (winner) {
        case GameState.PLAYER_ONE:
          gameState.getPlayer(GameState.PLAYER_TWO).addCardToHand(deck.getTopCard());
          break;

        case GameState.PLAYER_TWO:
          gameState.getPlayer(GameState.PLAYER_ONE).addCardToHand(deck.getTopCard());
          break;

        case TIE:
          gameState.getPlayer(GameState.PLAYER_ONE).addCardToHand(deck.getTopCard());
          gameState.getPlayer(GameState.PLAYER_TWO).addCardToHand(deck.getTopCard());
          break;

        default:
          System.out.println("Wrong winner state!");
          break;
      }
    }
  }

  public void handleWinnerCardForStartPlayer(int winner, Card card1, Card card2){
    if (winner == GameState.PLAYER_ONE) {
      handleWinnerCardForPlayer1(card1, card2);
    } else {
      handleWinnerCardForPlayer2(card1, card2);
    }
  }

  public void handleWinnerCardForSecondPlayer(int winner, Card card2, Card card1){
      if (winner == GameState.PLAYER_TWO) {
      handleWinnerCardForPlayer2(card2, card1);
    } else {
      handleWinnerCardForPlayer1(card2, card1);
    }
  }

  public void handleWinnerCardForPlayer1(Card card1, Card card2){
    gameState.getPlayer(GameState.PLAYER_ONE).addToVictoryPile(card2);
    card1.decreasePower(card2.getCurrentPower());
    gameState.getPlayer(GameState.PLAYER_ONE).addCardToHand(card1);
  }

  public void handleWinnerCardForPlayer2(Card card1, Card card2){
    gameState.getPlayer(GameState.PLAYER_TWO).addToVictoryPile(card2);
    card1.decreasePower(card2.getCurrentPower());
    gameState.getPlayer(GameState.PLAYER_TWO).addCardToHand(card1);
  }

  public boolean isGameOver() {
    boolean playerOneScore = gameState.getPlayer(GameState.PLAYER_ONE).getScore() >= gameState.getPointsToWin();
    boolean playerTwoScore = gameState.getPlayer(GameState.PLAYER_TWO).getScore() >= gameState.getPointsToWin();
    if (playerOneScore || playerTwoScore || deck.isEmpty()) {
      gameState.setGameWinner(gameState.getPlayer(GameState.PLAYER_ONE).getScore() == gameState.getPlayer(GameState.PLAYER_TWO).getScore()
              ? TIE : gameState.getPlayer(GameState.PLAYER_ONE).getScore() > gameState.getPlayer(GameState.PLAYER_TWO).getScore()
              ? GameState.PLAYER_ONE : GameState.PLAYER_TWO);
      return true;
    }
    return false;
  }

  public void dealCards() {
    gameState.getPlayer(GameState.PLAYER_ONE).addCardsToHand((ArrayList<Card>) deck.getHand(handSize));
    gameState.getPlayer(GameState.PLAYER_TWO).addCardsToHand((ArrayList<Card>) deck.getHand(handSize));
  }

  public void setPlayedCard(int card) {
    int numberOfRounds;
    gameState.setPlayedCard(card);
    if (gameState.getPlayedCards().size() > 1) {
      getRoundWinner();
      isGameOver();
      gameState.changeStartPlayer();
      gameState.clearPlayedCards();
    } else {
      gameState.changeCurrentPlayer();
    }
    server.sendGameUpdateToRoom(gameState, roomNo);
  }

  public GameState getGameState() {
    return gameState;
  }
}
