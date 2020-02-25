package me.tft.server;

public class ExampleTFTServer {
  public final static int PORT = 9999;

  public final static String STORE_PATH = "F:\\output";

  public static void main (String[] args) {
    AbstractServer server = new IOTFTServer();
    server.setPort(PORT);
    server.setStorePath(STORE_PATH);
    server.run();
  }
}
