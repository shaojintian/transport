package com.my.server;


import java.io.*;
import java.net.*;
import java.util.HashMap;

public class UdpThread implements Runnable{
    private DatagramSocket socket;
    //private T obj;
    private BufferedReader is;
    private OutputStream os;
    private int[]des;
    private int number;
    //why need buffer ? because we need to package the req/res
    public UdpThread(DatagramSocket socket,int[]des,int i){
        this.socket =socket;
        this.des =des;
        this.number=i;
        //this.obj=obj;
    }

    @Override
    public void run(){
        try {
            //send req to a specific ip:port
            System.out.printf(Server.getServerName()+"send req to a specific ip:%d\n",des[number]);
            byte[] sendBytes = Integer.toString(des[number]).getBytes();
            socket.send(new DatagramPacket(sendBytes,sendBytes.length));
            //recv client name
            byte[] recvBytes = new byte[1024];
            DatagramPacket recvPacket = new DatagramPacket(recvBytes,1024);
            socket.receive(recvPacket);
            System.out.println(Server.getServerName()+"recv remote name is :"+new String(recvBytes));
            Server.getDesName().put(des[number], new String(recvBytes));


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            close(socket);
        }

    }

    private void close(Closeable closeable) {
        if (closeable!=null){
            try {
                closeable.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
