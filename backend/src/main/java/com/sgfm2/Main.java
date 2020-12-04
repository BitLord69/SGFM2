package com.sgfm2;

public class Main {

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
        server.sendMsg();
        //server.stop();
    }
}
