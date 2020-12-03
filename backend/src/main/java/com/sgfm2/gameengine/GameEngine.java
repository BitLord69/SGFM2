package com.sgfm2.gameengine;

import com.sgfm2.gameobjects.Card;
import com.sgfm2.gameobjects.CardSettings;
import com.sgfm2.gameobjects.Deck;
import com.sgfm2.gameobjects.GameState;

import java.util.List;
import java.util.ArrayList;

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

  public static final byte PLAYER_ONE = 0;
  public static final byte PLAYER_TWO = 1;
  public static final int TIE = 2;

  protected GameState gameState;

  private final Deck deck = new Deck(cardSettings);
  private final int handSize;

  public GameEngine(GameState gameState, int handSize) {
    this.handSize = handSize;
    this.gameState = gameState;
  }

  /**
   *   Sätt startspelare till spelare ett (Hostens spelare)-
   *   Dela ut kort; skicka till klienten och den egna spelaren-
   *   Do... while game not over??
   *      Be startspelaren om ett kort-
   *      Be motspelaren om ett kort-
   *      Avgör vinnaren av rundan-
   *      Kolla om spelet är slut
   *      Byt startspelare -> gameState.changeStartPlayer()
   */
  public void runGame() {
    dealCards();
    do {
      gameState.setRoundWinner(-1);
      int index1 = getCardFromStartPlayer();
      if (index1 == -1) break;

      Card c1 = gameState.getPlayer(gameState.getStartPlayer()).getCard(index1);
      gameState.addPlayedCard(c1);

      renderStartPlayer();

      int index2 = getCardFromSecondPlayer();
      if (index2 == -1) break;

      Card c2 = gameState.getPlayer(gameState.getCurrentPlayer()).getCard(index2);
      gameState.addPlayedCard(c2);
      int winner = getRoundWinner(c1, c2);
      gameState.setRoundWinner(winner);
      redrawGameBoard();
      gameState.clearPlayedCards();
      gameState.changeStartPlayer();
    } while (!isGameOver());

    redrawGameBoard();
  }

  private void getPlayerNameFromClient() {
//    gameState.getPlayer(CLIENT).setName(gameLobby.getPlayerNameFromClient());
  }

  private void redrawGameBoard() {
  }

  private void renderStartPlayer() {
//    if (gameState.getStartPlayer() == HOST) {
//      gameBoard.render(gameState, HOST);
//    } else {
//      gameLobby.renderClient(gameState, CLIENT);
//    }
  }

  /**
   *
   * Är spelare1 först? Begär från den egna spelare direkt
   * Annars, begär kort från klienten via gameLobby
   *
   * @return an integer with the selected card
   */
  public int getCardFromStartPlayer() {
    return gameState.getStartPlayer() == PLAYER_ONE ? getCardFromPlayer1() : getCardFromPlayer2();
  }

  /**
   *  Är spelare1 andraspelaren? Begär från den egna spelare direkt
   *  Annars, begär kort från klienten via gameLobby
   * @return an integer with the selected card
   */
  public int getCardFromSecondPlayer() {
    return gameState.getStartPlayer() == PLAYER_TWO ? getCardFromPlayer1() : getCardFromPlayer2();
  }

   /**
    *     Jämför korten i gameState och avgör vem, om någon, som vann-
    *     Uppdatera gameState och vinsthögen för vinnande spelare
    *     @return -1 = ingen vann annars 0 eller 1 för respektive spelare
   * */
  public int getRoundWinner(Card card1, Card card2) {
    int result = card1.getCurrentPower() - card2.getCurrentPower();
    int winner;

    if (result > 0) { winner = gameState.getStartPlayer() == PLAYER_ONE ? 0 : 1; }
    else if (result < 0) { winner = gameState.getStartPlayer() == PLAYER_ONE ? 1 : 0; }
    else  { winner = TIE; }

    finalizingRound(winner, card1, card2);
    return winner;
  }

  public void finalizingRound(int winner, Card card1, Card card2) {
    gameState.getPlayer(gameState.getStartPlayer()).getCardOnHandAsList().remove(card1);
    gameState.getPlayer(gameState.getCurrentPlayer()).getCardOnHandAsList().remove(card2);
    if (winner >= 0) {
      if (winner == gameState.getStartPlayer()) {
        handleWinnerCardForStartPlayer(winner, card1, card2);
      } else if( winner == gameState.getCurrentPlayer() ) {
        handleWinnerCardForSecondPlayer(winner, card2, card1);
      }

      //Deal new card to LOSER, if TIE both players receive a new card.
      switch (winner) {
        case PLAYER_ONE:
//          gameState = gameLobby.sendCardToClient(new ArrayList<>(Collections.singletonList(deck.getTopCard())), gameState);
          break;

        case PLAYER_TWO:
          gameState.getPlayer(PLAYER_ONE).addCardToHand(deck.getTopCard());
          break;

        case TIE:
          gameState.getPlayer(PLAYER_ONE).addCardToHand(deck.getTopCard());
  //        gameState = gameLobby.sendCardToClient(new ArrayList<>(Collections.singletonList(deck.getTopCard())), gameState);
          break;

        default:
          System.out.println("Wrong winner state!");
          break;
      }
    }
  }

  public void handleWinnerCardForStartPlayer(int winner, Card card1, Card card2){
    if (winner == PLAYER_ONE) {
      handleWinnerCardForPlayer1(card1, card2);
    } else {
      handleWinnerCardForPlayer2(card1, card2);
    }
  }

  public void handleWinnerCardForSecondPlayer(int winner, Card card1, Card card2){
      if (winner == PLAYER_TWO) {
      handleWinnerCardForPlayer2(card1, card2);
    } else {
      handleWinnerCardForPlayer1(card1, card2);
    }
  }

  public void handleWinnerCardForPlayer1(Card card1, Card card2){
    gameState.getPlayer(PLAYER_ONE).addToVictoryPile(card2);
    card1.decreasePower(card2.getCurrentPower());
    gameState.getPlayer(PLAYER_ONE).addCardToHand(card1);
  }

  public void handleWinnerCardForPlayer2(Card card1, Card card2){
//    gameState = gameLobby.addToClientVictoryPile(card2, gameState);
    card1.decreasePower(card2.getCurrentPower());
//    gameState = gameLobby.sendCardToClient(new ArrayList<>(Collections.singletonList(card1)), gameState);
  }

  public boolean isGameOver() {
    boolean playerOneScore = gameState.getPlayer(PLAYER_ONE).getScore() >= gameState.getPointsToWin();
    boolean playerTwoScore = gameState.getPlayer(PLAYER_TWO).getScore() >= gameState.getPointsToWin();
    if (playerOneScore || playerTwoScore || deck.isEmpty()) {
        gameState.setGameWinner(gameState.getPlayer(PLAYER_ONE).getScore() == gameState.getPlayer(PLAYER_TWO).getScore()
                ? TIE : gameState.getPlayer(PLAYER_ONE).getScore() > gameState.getPlayer(PLAYER_TWO).getScore()
                ? PLAYER_ONE : PLAYER_TWO);
        return true;
    }
    return false;
  }

  public int getCardFromPlayer1() {
    //be den lokala spelaren om ett kort
    gameState.setCurrentPlayer(PLAYER_ONE);
//    gameBoard.render(gameState, HOST);
//    return gameBoard.getCard(gameState, HOST);
    return 0;
  }

  public int getCardFromPlayer2() {
    gameState.setCurrentPlayer(PLAYER_TWO);
    return 0;
//    return gameLobby.requestCardFromClient(gameState);
  }

  public void dealCards(){
    dealCardsToHost();
   gameState = dealCardsToClient();
  }

  public boolean dealCardsToHost(){
    return gameState.getPlayer(PLAYER_ONE).addCardsToHand((ArrayList<Card>) deck.getHand(handSize));
  }

  public GameState dealCardsToClient(){
    return null;
//     return gameLobby.sendCardToClient((ArrayList<Card>) deck.getHand(handSize), gameState);
  }
}
