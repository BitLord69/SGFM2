package com.sgfm2.network;

import com.sgfm2.gameobjects.Card;
import com.sgfm2.gameobjects.GameState;
import com.sgfm2.interfaces.Renderer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerHandler extends NetworkComHandler {

  private final ServerSocket serverSocket;

  public ServerHandler() throws IOException {
    super();
    serverSocket = new ServerSocket(PORT);

  }
  public ServerHandler(int port) throws IOException {
    super();
    serverSocket = new ServerSocket(port);
  }

  public void startServer() throws IOException {
    System.out.println("Waiting for player to connect...");
    socket = serverSocket.accept();
    getStreams();
  }

  @Override
  public void close() {
    super.close();
    try {
      serverSocket.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public String getPlayerNameFromClient() {
    Packet packet = new Packet(CommandType.GET_PLAYER_NAME_FROM_CLIENT, null);
    send(packet);
    packet = receive();
    return (String) packet.getParams()[0];
  }

  @Override
  public int getCardFromClient(Renderer renderer, GameState gameState) {
    int card = -1;

    Packet packet = new Packet(CommandType.GET_CARD_FROM_CLIENT, new GameState[]{gameState});
    send(packet);
    packet = receive();
    if (packet != null) {
      card = (int) packet.getParams()[0];
      gameState = (GameState) packet.getParams()[1];
    }
    return card;
  }

  @Override
  public GameState addToClientVictoryPile(Card card, GameState gameState, Renderer renderer) {
    Packet packet = new Packet(CommandType.ADD_TO_CLIENT_VICTORY_PILE,new Object[]{card, gameState});
    packet.cardsInPlayedCards = gameState.getPlayedCards().size();
    send(packet);
    packet = receive();
    return (GameState) packet.getParams()[0];
  }

  @Override
  public GameState sendCardToClient(ArrayList<Card> cardsToClient, GameState gameState) {
    Packet packet = new Packet(CommandType.SEND_CARD_TO_CLIENT, new Object[]{cardsToClient, gameState});
    send(packet);

    packet = receive();
    return (GameState) packet.getParams()[0];
  }

  @Override
  public void renderClient(Renderer renderer, GameState gameState, int playerToDraw) {
    Packet packet = new Packet(CommandType.RENDER_CLIENT, new Object[]{gameState, playerToDraw});
    send(packet);
  }

  public void sendGameOver(){
    Packet packet = new Packet(CommandType.QUIT, null);
    send(packet);
  }
}
