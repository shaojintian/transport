package com.my.server;


import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    //public static final String WEB_ROOT = "/Users/my/IntelliJProjects/transport/src/main/java/com/my/server/script";
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
        //print map
        //for (int port:desName.keySet()) {
        //    System.out.println("print map:"+Server.getServerName()+"--------name:"+desName.get(port)+" port:"+port);
        //}

        //tcp socket
        ServerSocket serverSocket = new ServerSocket(tcpPort);
        //server never stop
        while (true){
            System.out.println(Server.getServerName()+" Waiting a socket in ");
            final Socket socket =  serverSocket.accept();//block here
            System.out.println("start"+socket);
            //并发处理所有调用逻辑
            ServerThread thread  = new ServerThread(socket);
            thread.run();
        }

    }

    public static HashMap<Integer, String> getDesName() {
        return desName;
    }

    public static void printMap(){
        for (int port:desName.keySet()) {
                System.out.println("print map:\n"+Server.getServerName()+"--------name:"+desName.get(port)+" port:"+port);
        }
    }

    public static String getServerName() {
        return name;
    }

    public static void main(String[] args) {

        try {
            Server.startService(args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
