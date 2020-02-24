package me.tft.server;

import java.io.IOException;

public abstract class AbstractServer {
  private int port = -1;

  public void setPort(int port) {
    this.port = port;
  }

  public abstract void run();

  public abstract void close();

  public int getPort() {
    return port;
  }

  public void start() {
    if (port == -1) {
      System.out.println("Set port first plz");
      return;
    }
    this.run();
  }

}
