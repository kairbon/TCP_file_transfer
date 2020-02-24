package me.tft.common;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * protocol
 *
 * -------------------------------------------------------------
 * |contentLength | fileName |              content            |
 * -------------------------------------------------------------
 * |    16bit     |   64*8bit  |  contentLength - fileNameLength |
 * -------------------------------------------------------------
 * **/

public class Decoder {

  public static TransportProtocolEntity streamDecode(DataInputStream stream) throws IOException {
    byte[] contentByte;
    int length = stream.readUnsignedByte();
    contentByte = new byte[length];
    stream.read(contentByte);
    byte[] fileNameByte = new byte[64];
    System.arraycopy(contentByte, 0, fileNameByte, 0, 64);
    String fileName = new String(fileNameByte, "utf-8");
    byte[] fileByte = new byte[length - 64];
    System.arraycopy(contentByte, 64, fileByte, 0, length - 64);
    TransportProtocolEntity entity = new TransportProtocolEntity();
    entity.setContent(fileByte);
    entity.setContentLength(length);
    entity.setFileName(fileName);
    return entity;
  }
}

