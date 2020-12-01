package com.sgfm2.network;

import java.io.Serializable;
import java.time.LocalTime;

public class Packet implements Serializable {

    private static final long SerialVersionUID = 10L;
    public int cardsInPlayedCards = 0;
    private LocalTime localTime = LocalTime.now();
    CommandType commandType;
    Object[] params;

    public Packet(CommandType commandType, Object[] params) {
        this.commandType = commandType;
        this.params = params;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public Object[] getParams() {
        return params;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }
}
