package com.sgfm2.network;

import com.sgfm2.gameobjects.Card;
import com.sgfm2.gameengine.Game;
import com.sgfm2.gameobjects.GameState;
import com.sgfm2.interfaces.Renderer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends NetworkComHandler {

  public ClientHandler(String ipaddress) throws IOException {
    this(ipaddress, NetworkComHandler.PORT);
  }

  public ClientHandler(String ipaddress, int port) throws IOException {
    super();
    socket = new Socket(ipaddress, port);
    getStreams();
  }

  @Override
  public int getCardFromClient(Renderer renderer, GameState gameState) {
    renderer.render(gameState, Game.CLIENT);
    int card = renderer.getCard(gameState, Game.CLIENT);
    Packet p = new Packet(CommandType.GET_CARD_FROM_CLIENT, new Object[]{card, gameState});
    send(p);
    return card;
  }

  @Override
  public GameState addToClientVictoryPile(Card card, GameState gameState, Renderer renderer) {
    renderer.render(gameState, Game.CLIENT);
    gameState.getPlayer(Game.CLIENT).addToVictoryPile(card);
    Packet p = new Packet(CommandType.ADD_TO_CLIENT_VICTORY_PILE, new Object[]{gameState});
    send(p);
    return gameState;
  }

  @Override
  public GameState sendCardToClient(ArrayList<Card> cardsToClient, GameState gameState) {
    gameState.getPlayer(Game.CLIENT).addCardsToHand(cardsToClient);
    Packet packet = new Packet(CommandType.SEND_CARD_TO_CLIENT, new Object[]{gameState});
    send(packet);
    return gameState;
  }

  public void sendPlayerNameToHost(String name) {
    Packet p = new Packet(CommandType.GET_PLAYER_NAME_FROM_CLIENT, new String[]{name});
    send(p);
  }

  @Override
  public void renderClient(Renderer renderer, GameState gameState, int playerToDraw) {
    renderer.render(gameState, playerToDraw);
  }

  @Override
  public void sendGameOver() {

  }

  @Override
  public String getPlayerNameFromClient() {
    return null;
  }
}
