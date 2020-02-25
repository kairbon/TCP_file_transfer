package me.tft.server;

public abstract class AbstractServer {
  private int port = -1;

  private String storePath;

  public String getStorePath() {
    return storePath;
  }

  public void setStorePath(String path) {
    this.storePath = path;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public abstract void run();

  public abstract void close();

  public int getPort() {
    return port;
  }

}
