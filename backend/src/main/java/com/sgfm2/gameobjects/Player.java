package com.sgfm2.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {

  private String name;
  private final ArrayList<Card> victoryPile = new ArrayList<>();
  private final ArrayList<Card> cardsOnHand = new ArrayList<>();

  public Player(String name){
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getScore(){
    int score = 0;
    for(Card card : victoryPile){
      score += card.getCurrentPower();
    }
    return score;
  }

  public Card getCard(int selectedCard){
    return selectedCard != 0 && selectedCard <= cardsOnHand.size() ? cardsOnHand.get(selectedCard-1) : null;
  }

  public boolean addToVictoryPile(Card wonCard){
    return victoryPile.add(wonCard);
  }

  public ArrayList<Card> getCardOnHandAsList(){
    return cardsOnHand;
  }

  public boolean addCardsToHand(ArrayList<Card> startHand) {
    return this.cardsOnHand.addAll(startHand);
  }

  public boolean addCardToHand(Card card) {
    return this.cardsOnHand.add(card);
  }

  public void setName(String name) {
    this.name = name;
  }
}
