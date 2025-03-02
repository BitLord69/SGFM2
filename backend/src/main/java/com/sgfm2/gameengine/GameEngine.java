package com.sgfm2.gameengine;

import com.google.gson.Gson;
import com.sgfm2.Server;
import com.sgfm2.gameobjects.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
  private final String roomNo;

  private final Server server;

  public GameEngine(Server server, int handSize, int pointsToWin, String roomNo, String league) {
    gameState = new GameState(pointsToWin, league);
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
    card2.setCurrentPower(0);
    gameState.getPlayer(GameState.PLAYER_ONE).addCardToHand(card1);
  }

  public void handleWinnerCardForPlayer2(Card card1, Card card2){
    gameState.getPlayer(GameState.PLAYER_TWO).addToVictoryPile(card2);
    card1.decreasePower(card2.getCurrentPower());
    card2.setCurrentPower(0);
    gameState.getPlayer(GameState.PLAYER_TWO).addCardToHand(card1);
  }

  public boolean isGameOver() {
    boolean playerOneScore = gameState.getPlayer(GameState.PLAYER_ONE).getScore() >= gameState.getPointsToWin();
    boolean playerTwoScore = gameState.getPlayer(GameState.PLAYER_TWO).getScore() >= gameState.getPointsToWin();
    if (playerOneScore || playerTwoScore || deck.getRemainingCardCount() < 2) {
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

  public void dealNewCards(int winner) {
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

  public void setPlayedCard(int card) {
    int winner = -1;
    gameState.setPlayedCard(card);
    if (gameState.getPlayedCards().size() > 1) {
      winner = getRoundWinner();
      gameState.setRoundWinner(winner);
      if (!isGameOver()) {
        server.sendGameUpdateToRoom(gameState, roomNo);
        // Sleep for a short bit so the players have time to see who won the round
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        dealNewCards(winner);
        gameState.changeStartPlayer();
        gameState.clearPlayedCards();
        gameState.setRoundWinner(-1);
        server.sendGameUpdateToRoom(gameState, roomNo);
      } else {
        // If it's not a guest game, save it in the database
        if (!gameState.getPlayer(0).getName().startsWith("guest")) {
          saveGameInDatabase();
        }
        server.sendGameUpdateToRoom(gameState, roomNo);
        //server.removeGame(roomNo);
      }
    } else {
      gameState.changeCurrentPlayer();
      server.sendGameUpdateToRoom(gameState, roomNo);
    }
  }

  private void saveGameInDatabase() {
    HttpRequest request = null;
    try {
      request = HttpRequest
          .newBuilder()
          .uri(new URI("http://localhost:8070/api/game"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(gameState)))
          .build();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    var response = HttpClient.newBuilder().build().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    response.thenApply(HttpResponse::body).join();
  }

  public GameState getGameState() {
    return gameState;
  }
}
