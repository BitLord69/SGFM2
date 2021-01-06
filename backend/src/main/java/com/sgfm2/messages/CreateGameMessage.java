package com.sgfm2.messages;

public class CreateGameMessage {
    private String name;
    private String league;
    private int pointsToWin;
    private int cardsOnHand;

    public CreateGameMessage() {
    }

    public CreateGameMessage(String name, int pointsToWin, int cardsOnHand, String league) {
        this.name = name;
        this.league = league;
        this.pointsToWin = pointsToWin;
        this.cardsOnHand = cardsOnHand;
    }

    public String getName() {
        return name;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPointsToWin() {
        return pointsToWin;
    }

    public void setPointsToWin(int pointsToWin) {
        this.pointsToWin = pointsToWin;
    }

    public int getCardsOnHand() {
        return cardsOnHand;
    }

    public void setCardsOnHand(int cardsOnHand) {
        this.cardsOnHand = cardsOnHand;
    }

    @Override
    public String toString() {
        return "Message{" + "name=" + name + '}';
    }
    
}
