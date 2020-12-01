package com.sgfm2.gameobjects;

public class CardSettings {

    private final int power;
    private final String name;
    public int amount;

    public CardSettings(int power, String name, int amount) {
        this.power = power;
        this.name = name;
        this.amount = amount;
    }

    public Card createCard(){
       return new Card(power, name);
    }
}
