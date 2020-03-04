package me.tft.server;

import me.tft.common.Decoder;
import me.tft.common.TransportProtocolEntity;

import java.io.*;
import java.net.Socket;

public class IOServerHandler implements Runnable {
  private Socket socket;

  private String storePath;

  public IOServerHandler(Socket socket, String path) {
    this.socket = socket;
    this.storePath = path;
  }

  private void createFile(TransportProtocolEntity entity) throws IOException {
    File outputFile =
        new File(storePath + File.separator + entity.getFileName());
    outputFile.createNewFile();
    FileOutputStream fos = new FileOutputStream(outputFile);
    fos.write(entity.getContent());
    fos.close();
  }

  @Override
  public void run() {
    try {
      TransportProtocolEntity entity =
          Decoder.streamDecode(new DataInputStream(socket.getInputStream()));
      socket.shutdownInput();
      System.out.println(entity.getFileName());
      createFile(entity);
      OutputStream dos = socket.getOutputStream();
      dos.write(new byte[]{0, 1});
      dos.close();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
