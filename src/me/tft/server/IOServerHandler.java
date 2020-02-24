package me.tft.server;

import me.tft.common.Decoder;
import me.tft.common.TransportProtocolEntity;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class IOServerHandler implements Runnable {
  private Socket socket;

  public IOServerHandler(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try {
      TransportProtocolEntity entity =
          Decoder.streamDecode(new DataInputStream(socket.getInputStream()));
      System.out.println(entity.getFileName());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
