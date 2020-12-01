package com.sgfm2.gameengine;

import com.sgfm2.gameobjects.GameState;
import com.sgfm2.interfaces.Renderer;

public class Game {
    public static final byte HOST = 0;
    public static final byte CLIENT = 1;
    public static final int TIE = 2;

    protected GameLobby gameLobby;
    protected Renderer gameBoard;
    protected GameState gameState;

    public Game(GameLobby gameLobby, Renderer gameBoard, GameState gameState) {
        this.gameLobby = gameLobby;
        this.gameBoard = gameBoard;
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
