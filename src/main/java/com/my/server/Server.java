package com.my.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    /**
     * based on socket
     * @param port
     * @throws IOException
     */
    public static  void startService( int port) throws IOException{
        ServerSocket serverSocket = new ServerSocket(port);
        //server never stop
        while (true){
            logger.info("Waiting a socket in ");
            final Socket socket =  serverSocket.accept();//block here
            logger.info("start"+socket);
            //并发处理所有调用逻辑
            ServerThread thread  = new ServerThread(socket);
            thread.run();
        }

    }

    public static void main(String[] args) {

    }

}
