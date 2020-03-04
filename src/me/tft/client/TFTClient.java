package me.tft.client;

import me.tft.common.Encoder;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;

public class TFTClient {

  public static void main(String[] args) {
    try {
      Socket socket = new Socket("127.0.0.1", 9999);
      OutputStream ops = socket.getOutputStream();
      File file = Paths.get("F:\\a62f2e4dd07b75ee2ca1ffe576ece585aaf6439c6c83-4DS67H_fw658.jpg").toFile();
      byte[] content = Encoder.fileEncode(file);
      ops.write(content);
      DataInputStream dis = new DataInputStream(socket.getInputStream());
      int result = dis.readUnsignedShort();
      if (result == 1) {
        System.out.println("OK");
      }
      socket.shutdownOutput();
      socket.shutdownInput();
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
