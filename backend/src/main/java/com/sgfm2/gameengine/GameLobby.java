package com.sgfm2.gameengine;

import com.sgfm2.gameobjects.Card;
import com.sgfm2.gameobjects.GameState;
import com.sgfm2.gameobjects.Player;
import com.sgfm2.local.LocalGameHandler;
import com.sgfm2.renderer.ConsoleRenderer;
import com.sgfm2.interfaces.ComHandler;
import com.sgfm2.network.ClientHandler;
import com.sgfm2.network.NetworkComHandler;
import com.sgfm2.network.ServerHandler;
import com.sgfm2.utils.GameLobbyMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameLobby {

    private final GameLobbyMenu menu;
    private Dispatcher dispatcher;

    public GameLobby(Boolean runGame) {
        menu = new GameLobbyMenu(this);
        if (runGame) runMenu();
    }

    public void runMenu() {
        menu.handleConsumerMenu();
    }

    public void startLocalGame(Object o) {
        Player player1 = new Player(inputPlayerName("player 1"));
        Player player2 = new Player(inputPlayerName("player 2"));
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        GameState gs = new GameState(
                inputGameSettings("Enter points to win", 10, 20, 15), players, true);
        ComHandler comHandler = new LocalGameHandler();
        ConsoleRenderer renderer = new ConsoleRenderer();
        dispatcher = new Dispatcher(comHandler, renderer);
        GameHost host = new GameHost(this, renderer, gs,
                inputGameSettings("Enter amount of cards on hand", 1, 8, 5));
        host.runGame();
    }

    public String inputPlayerName(String player) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter name for " + player + ": ");
        return scanner.nextLine();
    }

    public int inputGameSettings(String prompt, int min, int max, int def) {
        boolean isValueOk;
        int value;
        String input;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.printf("\n%s (%d - %d) [%d]: ", prompt, min, max, def);
            input = scanner.nextLine();
            if (input.isEmpty()) {
                input = String.format("%d", def);
            }
            try {
                value = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                value = -1;
            }
            isValueOk = value >= min && value <= max;
        } while (!isValueOk);
        return value;
    }

    public void setDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void startNetworkGame(Object o) {
        Player player1 = new Player(inputPlayerName("player 1"));
        Player player2 = new Player("Player 2");
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        GameState gameState = new GameState(
            inputGameSettings("Enter points to win", 10, 20, 15), players, false);
        ConsoleRenderer renderer = new ConsoleRenderer();
        ServerHandler serverHandler = null;
        try {
          serverHandler = new ServerHandler();
        } catch (IOException e) {
          e.printStackTrace();
        }
        dispatcher = new Dispatcher(serverHandler, renderer);

        GameHost gameHost = new GameHost(this, renderer, gameState,
            inputGameSettings("Enter amount of cards on hand", 1, 8, 5));
      try {
        assert serverHandler != null;
        serverHandler.startServer();
      } catch (IOException e) {
        e.printStackTrace();
      }
      gameHost.runGame();
      serverHandler.close();
    }

    public void connectToNetworkGame(Object o) {
        NetworkComHandler comHandler;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ip to connect to [localhost]: ");
        String ipaddress = scanner.nextLine();
        System.out.println("Connecting to game...");
        if(ipaddress.isEmpty()) ipaddress = "localhost";
        try {
            comHandler = new ClientHandler(ipaddress);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ConsoleRenderer renderer = new ConsoleRenderer();
        dispatcher = new Dispatcher(comHandler, renderer);
        GameClient gameClient = new GameClient(this);
        gameClient.runGame();

        comHandler.close();
    }

    public int requestCardFromClient(GameState gameState){
       return dispatcher.getCardFromClient(gameState);
    }

    public GameState addToClientVictoryPile(Card cardToClient, GameState gameState){
       return dispatcher.addToClientVictoryPile(cardToClient, gameState);
    }

    public GameState sendCardToClient(ArrayList<Card> cardsToClient, GameState gameState){
      return dispatcher.sendCardToClient(cardsToClient, gameState);
    }

    public void renderClient(GameState gs, int playerToDraw){
        dispatcher.renderClient(gs, playerToDraw);
    }

    public String getPlayerNameFromClient() {
        return dispatcher.getPlayerNameFromClient();
    }

    public boolean getCommandFromHost() {
        return dispatcher.getCommandFromHost();
    }

    public void sendGameOver(){
      dispatcher.sendGameOver();
    }
}
