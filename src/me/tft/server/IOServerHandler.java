package me.tft.server;

import me.tft.common.Decoder;
import me.tft.common.TransportProtocolEntity;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class IOServerHandler implements Runnable {
  private Socket socket;

  private String storePath;

  public IOServerHandler(Socket socket, String path) {
    this.socket = socket;
    this.storePath = path;
  }

  @Override
  public void run() {
    try {
      TransportProtocolEntity entity =
          Decoder.streamDecode(new DataInputStream(socket.getInputStream()));
      System.out.println(entity.getFileName());
      File outputFile =
          new File(storePath + File.separator + entity.getFileName());
      outputFile.createNewFile();
      FileOutputStream fos = new FileOutputStream(outputFile);
      fos.write(entity.getContent());
      fos.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
