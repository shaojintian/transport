package com.my.server;

//
import java.io.*;
import java.net.*;

public class UdpThread implements Runnable{
    private DatagramSocket socket;
    private int[]des;
    private int number;
    //why need buffer ? because we need to package the req/res
    public UdpThread(DatagramSocket socket,int[]des,int i){
        this.socket =socket;
        this.des =des;
        this.number=i;
        //this.obj=obj;
    }
    public UdpThread(DatagramSocket socket){
        this.socket =socket;
        this.des =null;
        //this.obj=obj;
    }


    @Override
    public void run(){
        try {
            //send req to a specific port ,req is client name
            System.out.printf(Server.getServerName()+" send req to a specific port:%d\n",des[number]);
            byte[] sendBytes = Server.getServerName().getBytes();
            socket.send(new DatagramPacket(sendBytes,sendBytes.length,InetAddress.getByName("127.0.0.1"),des[number]));
            //recv client name
            byte[] recvBytes = new byte[1024];
            DatagramPacket recvPacket = new DatagramPacket(recvBytes,1024);
            socket.receive(recvPacket);
            String desName = new String(recvBytes).trim();
            System.out.println(Server.getServerName()+" recv remote name is :"+desName);
            Server.getDesName().put(des[number], desName);


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //close(socket);
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
