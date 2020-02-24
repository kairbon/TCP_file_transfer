package me.tft.common;

import java.io.*;

public class Encoder {
  public static byte[] fileEncode(File file) {
    if (file == null) {
      throw new NullPointerException("file can't be null");
    }
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length() + 66);
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
      String fileName = file.getName();
      byte[] fileNameBytes = fileName.getBytes("utf-8");
      if (fileNameBytes.length > 64) {
        fileNameBytes = "NameTooLong".getBytes("utf-8");
      }
      byte[] fnb = new byte[64];
      System.arraycopy(fileNameBytes, 0, fnb, 0, 64);
      int uintLength = ((int) file.length() + 64) & 0x0000ffff;
      byte[] contentLengthByte = intToByte(uintLength);
      bos.write(contentLengthByte, 0, 2);
      bos.write(fnb, 0, 2);
      int buf_size = 1024;
      byte[] buffer = new byte[buf_size];
      int len = 0;
      while (-1 != (len = bis.read(buffer, 0, buf_size))) {
        bos.write(buffer, 0, len);
      }
      return bos.toByteArray();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }

  }

  public static byte[] intToByte(int val) {
    byte[] b = new byte[2];
    b[0] = (byte) (val & 0xff);
    b[1] = (byte) ((val >> 8) & 0xff);
//    b[2] = (byte)((val >> 16) & 0xff);
//    b[3] = (byte)((val >> 24) & 0xff);
    return b;
  }
}
