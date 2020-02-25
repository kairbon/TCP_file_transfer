package me.tft.common;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
    int length = stream.readUnsignedShort();
    contentByte = new byte[length];
    stream.read(contentByte);
    return bytesDecode(length, contentByte);
  }

  public static TransportProtocolEntity bytesDecode(int contentLength, byte[] content) throws UnsupportedEncodingException {
    if (contentLength == 0) {
      return null;
    }
    byte[] fileNameByte = new byte[64];
    System.arraycopy(content, 0, fileNameByte, 0, 64);
    String fileName = new String(fileNameByte, "utf-8");
    fileName = fileName.replaceAll("[\u0000]", "");
    byte[] fileByte = new byte[contentLength - 64];
    System.arraycopy(content, 64, fileByte, 0, contentLength - 64);
    TransportProtocolEntity entity = new TransportProtocolEntity();
    entity.setContent(fileByte);
    entity.setContentLength(contentLength);
    entity.setFileName(fileName);
    return entity;
  }

}

