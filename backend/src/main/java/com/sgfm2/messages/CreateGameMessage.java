package com.sgfm2.messages;

public class CreateGameMessage {
    private String name;
    private int pointsToWin;
    private int cardsOnHand;

    public CreateGameMessage() {
    }

    public CreateGameMessage(String name, int pointsToWin, int cardsOnHand) {
        this.name = name;
        this.pointsToWin = pointsToWin;
        this.cardsOnHand = cardsOnHand;
    }

    public String getName() {
        return name;
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
