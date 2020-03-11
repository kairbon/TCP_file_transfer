package me.tft.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IOTFTServer extends AbstractServer {

  private ServerSocket serverSocket;

  @Override
  public void run() {
    try {
      serverSocket = new ServerSocket(getPort());
      System.out.println("BIO-Server启动成功，预备开始监听请求");
      ExecutorService threadPool = Executors.newFixedThreadPool(10);

      while (true) {
        Socket socket = serverSocket.accept();
        Runnable r = new IOServerHandler(socket, getStorePath());
        threadPool.submit(r);
        System.out.println("接收成功！！");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() {
    try {
      serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
