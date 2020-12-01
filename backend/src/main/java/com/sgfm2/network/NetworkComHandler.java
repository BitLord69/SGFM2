package com.sgfm2.network;

import com.sgfm2.interfaces.ComHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

abstract public class NetworkComHandler implements ComHandler {

  protected static final int PORT = 42069;

  protected Socket socket = null;
  protected ObjectOutputStream oos;
  protected ObjectInputStream ois;

  public NetworkComHandler(){
  }

  public ObjectOutputStream getOutputStream() {
    return oos;
  }

  public ObjectInputStream getInputStream() {
    return ois;
  }

  public void getStreams() {
    if (socket == null) return;
    try {
      oos = new ObjectOutputStream(socket.getOutputStream());
      ois = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public void send(Packet packet) {
    try {
      oos.reset();
      oos.writeUnshared(packet);
      oos.flush();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  public Packet receive() {
    Packet packet = null;
    try {
      packet = (Packet)ois.readUnshared();
    } catch (IOException | ClassNotFoundException e) {
      System.out.println(e.getMessage());
    }

    return packet;
  }

  public void close()  {
    if(socket == null) return;
    try {
      oos.close();
      ois.close();
      socket.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
