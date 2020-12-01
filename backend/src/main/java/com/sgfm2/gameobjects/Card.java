package com.sgfm2.gameobjects;

import com.sgfm2.utils.TextUtil;

import java.io.Serializable;

public class Card implements Serializable {

    private int power;
    private String name;
    private int currentPower;

    public Card() {}

    public Card(int power, String name) {
        this.power = power;
        this.name = name;
        this.currentPower = power;
    }

    public void decreasePower(int decreaseBy) {
        setCurrentPower(currentPower - decreaseBy);
    }
    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(int currentPower) {
        this.currentPower = Math.max(currentPower, 0);
    }
    public boolean isDead() {
        return currentPower == 0;
    }

    @Override
    public String toString() {
        return String.format("%s, Power: %s, current power: %s", TextUtil.pimpString(name, TextUtil.LEVEL_BOLD), power, currentPower);
    }
}
