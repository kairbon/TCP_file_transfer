package me.tft.server;

import me.tft.common.Decoder;
import me.tft.common.TransportProtocolEntity;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOTFTServer extends AbstractServer {

  private static final int TIMEOUT = 3000;
  private static final int BUF_SIZE = 1024;
  private static final int POOL_SIZE = 10;

  private Selector selector = null;

  private ServerSocketChannel ssc = null;

  @Override
  public void run() {
    try {
      selector = Selector.open();
      ssc = ServerSocketChannel.open();
      ssc.socket().bind(new InetSocketAddress(getPort()));
      ssc.configureBlocking(false);
      ssc.register(selector, SelectionKey.OP_ACCEPT);
      ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);
      while (true) {

        if (selector.select(TIMEOUT) == 0) {
          System.out.println("waiting");
          continue;
        }
        Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
        while (iter.hasNext()) {
          SelectionKey key = iter.next();
          iter.remove();
          if (key.isAcceptable()) {
            handleAccept(key);
          }
          if (key.isReadable()) {
            TransportProtocolEntity t = handleRead(key);
            if (t != null) {
              executorService.submit(new NIOServerHandler(t, getStorePath()));
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (selector != null) {
          selector.close();
        }
        if (ssc != null) {
          ssc.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private TransportProtocolEntity handleRead(SelectionKey key) throws IOException {
    SocketChannel sc = (SocketChannel) key.channel();
    ByteBuffer buf = (ByteBuffer) key.attachment();

    int readCnt = 0;
    long bytesRead = 0;
    int contentLength = 0;
    byte[] content = new byte[0];
    bytesRead = sc.read(buf);
    int nowSize = 0;
    while (bytesRead > 0) {
      readCnt++;
      buf.flip();
      if (readCnt == 1 && bytesRead >= 2) {
        byte[] lengthBytes = new byte[2];
        buf.get(lengthBytes);
        contentLength = readUShort(lengthBytes);
        content = new byte[contentLength];
        if (bytesRead - 2 < contentLength) {
          int size = (int) bytesRead - 2;
          byte[] tmpBytes = new byte[size];
          buf.get(tmpBytes);
          System.arraycopy(tmpBytes, 0, content, nowSize, size);
          nowSize += size;
          buf.flip();
          bytesRead = sc.read(buf);
          continue;
        } else {
          buf.get(content);
          break;
        }
      } else if (readCnt == 1 && bytesRead < 2) {
        readCnt = 0;
        buf.flip();
        bytesRead = sc.read(buf);
      }
      if (readCnt > 1) {
        if (bytesRead < contentLength) {
          int size = (int) bytesRead;
          byte[] tmpBytes = new byte[size];
          buf.get(tmpBytes);
          System.arraycopy(tmpBytes, 0, content, nowSize, size);
          nowSize += size;
          buf.flip();
          bytesRead = sc.read(buf);
          continue;
        } else {
          content = new byte[contentLength];
          buf.get(content);
          break;
        }
      }
    }
    if (contentLength == 0) {
      sc.close();
    } else {
      sc.write(ByteBuffer.wrap(new byte[]{0, 1}));
    }
    return Decoder.bytesDecode(contentLength, content);
  }


  private void handleAccept(SelectionKey key) throws IOException {
    ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
    SocketChannel sc = ssChannel.accept();
    sc.configureBlocking(false);
    sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(BUF_SIZE));
  }

  @Override
  public void close() {
    try {
      if (selector != null) {
        selector.close();
      }
      if (ssc != null) {
        ssc.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static int readUShort(byte[] bytes) throws EOFException {
    if (bytes.length != 2) {
      return -1;
    }
    int ch1 = bytes[1];
    int ch2 = bytes[0];
    if ((ch1 | ch2) < 0)
      throw new EOFException();
    return (ch1 << 8) + (ch2 << 0);
  }
}