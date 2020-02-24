package me.tft.server;

import me.tft.common.Decoder;
import me.tft.common.TransportProtocolEntity;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IOTFTServer extends AbstractServer {

  private ServerSocket serverSocket;

  public static void main(String[] args) {

  }

  @Override
  public void run() {
    try {
      serverSocket = new ServerSocket(getPort());
      System.out.println("Server启动成功，预备开始监听请求");
      ExecutorService threadPool = Executors.newFixedThreadPool(10);

      while (true) {
        Socket socket = serverSocket.accept();
        Runnable r =  new IOServerHandler(socket);
        threadPool.submit(r);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() {

  }
}
