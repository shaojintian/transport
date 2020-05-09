package com.my.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    public static final String WEB_ROOT = "/Users/my/IntelliJProjects/transport/src/main/java/com/my/server/script";
    private static final HashMap<Integer,String> desName = new HashMap<>();
    private static String name;
    /**
     *
     * @param args
     * @throws IOException
     */
    public static  void startService( String[] args) throws IOException{
        //gracefully exit
        //...
        name =args[0];
        int tcpPort = Integer.parseInt(args[1]);
        int udpPort = Integer.parseInt(args[2]);
        int[] des = new int[args.length-3];
        //[3,...] des udpPort
        //udp socket
        DatagramSocket udpSocket = new DatagramSocket(udpPort);
        for (int i = 0; i <des.length ; i++) {
            des[i]=Integer.parseInt(args[3+i]);
            UdpThread udpThread = new UdpThread(udpSocket,des,i);
            udpThread.run();
        }


        //tcp socket
        ServerSocket serverSocket = new ServerSocket(tcpPort);
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

    public static HashMap<Integer, String> getDesName() {
        return desName;
    }

    public static String getServerName() {
        return name;
    }

    public static void main(String[] args) {

        try {
            Server.startService(args);
        }catch (Exception e){
            logger.error(e+"");
        }
    }

}
