package me.tft.server;

import me.tft.common.TransportProtocolEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NIOServerHandler implements Runnable {
  private TransportProtocolEntity entity;

  private String storePath;

  public NIOServerHandler(TransportProtocolEntity entity, String path) {
    this.entity = entity;
    this.storePath = path;
  }

  @Override
  public void run() {
    try {
      File outputFile =
          new File(storePath + File.separator + entity.getFileName());
      outputFile.createNewFile();
      FileOutputStream fos = new FileOutputStream(outputFile);
      fos.write(entity.getContent());
      fos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
