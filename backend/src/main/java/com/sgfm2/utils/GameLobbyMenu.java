package com.sgfm2.utils;

import com.sgfm2.gameengine.GameLobby;

import java.util.ArrayList;

public class GameLobbyMenu extends Menu {

    private ArrayList<MenuChoiceBaseClass> mainMenu = new ArrayList<>();
    private ArrayList<MenuChoiceBaseClass> networkGameMenu = new ArrayList<>();

    public GameLobbyMenu(GameLobby p){
        mainMenu.add(new MenuChoiceConsumer("Start a local game", '1', p::startLocalGame));
        mainMenu.add(new MenuChoiceConsumer("Start a network game", '2', this::showNetworkGameMenu));
        mainMenu.add(new MenuChoiceConsumer("Bye bye", '0', null));

        networkGameMenu.add(new MenuChoiceConsumer("Start a new game as host", '1', p::startNetworkGame));
        networkGameMenu.add(new MenuChoiceConsumer("Connect to a game", '2', p::connectToNetworkGame));
        networkGameMenu.add(new MenuChoiceConsumer("Back", '0', this::backToMain));
    } // MainMenu

    private void showNetworkGameMenu(Object o) {
        setMenu(networkGameMenu);
    } // showNetworkGameMenu

    @Override
    public ArrayList<MenuChoiceBaseClass> setInitialMenu() {
        return mainMenu;
    } // setInitialMenu

    private void backToMain(Object o) {
        setMenu(mainMenu);
    } // backToMain
} // class MainMenu
