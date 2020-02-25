package me.tft.client;

import me.tft.common.Encoder;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Paths;

public class TFTClient {

  public static void main(String[] args) {
    try {
      Socket socket = new Socket("127.0.0.1", 9999);
      OutputStream ops = socket.getOutputStream();
      File file = Paths.get("F:\\a.txt").toFile();
      byte[] content = Encoder.fileEncode(file);
      ops.write(content);
      ops.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
