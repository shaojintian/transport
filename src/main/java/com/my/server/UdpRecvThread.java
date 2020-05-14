package com.my.server;

//
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UdpRecvThread implements Runnable{
    private DatagramSocket socket;


    public UdpRecvThread(DatagramSocket socket){
        this.socket =socket;

        //this.obj=obj;this.des =null;
    }


    private void sendResponse(String uri)throws Exception{
        try {
            //route
            String response ="";
            int flag = -1;
            System.out.println(Server.getServerName()+" go to "+uri);
            //
            //Server.printMap();
            //package ans
            FileReader tt = new FileReader("/Users/shaojintian/IntelliJProjects/transport/src/main/script/tt-"+Server.getServerName());
            BufferedReader bufferedtt = new BufferedReader(tt);
            //leave time /arrive time date format
            DateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");//如2016-08-10 20:40
            Date fromDate = simpleFormat.parse("2020-01-01 10:36");
            String recentlyArriveT = "23:59";
            String recentlyTimeLine = null;
            //target arrive time
            String timeLine =null;
            while ((timeLine=bufferedtt.readLine())!=null){
                //check leave time
                //10:36
                if(timeLine.split(",").length<4)continue;
                //System.out.println("timeline:"+timeLine);
                String leaveT =timeLine.split(",")[0].trim();
                String arriveT =timeLine.split(",")[3].trim();//len>=4
                Date temLeaveDate = simpleFormat.parse("2020-01-01 "+leaveT);
                if(temLeaveDate.compareTo(fromDate)>=0){
                    if(arriveT.compareTo(recentlyArriveT)<0){//cur < recently
                        recentlyArriveT = arriveT;
                        recentlyTimeLine = timeLine;
                    }
                }
            }
            //direct arrive
            for (int udpPort:Server.getDesName().keySet()) {
                String stationName  =Server.getDesName().get(udpPort);
                if(uri.equals(stationName)){
                    response = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/html\r\n"+
                            "Content-Length: 100\r\n" + "\r\n" +
                            "<html><h1>"+recentlyTimeLine+"</h1><html>";
                    System.out.println(response);
                    flag = 1;
                    socket.send(new DatagramPacket(response.getBytes(),response.getBytes().length,InetAddress.getByName("127.0.0.1"),Response.getDesPort(uri)));
                    break;
                }
            }
            //indirect arrive
            if(flag != 1){
                if(recentlyTimeLine!=null&&recentlyTimeLine.length()>=5) {
                    String indirectDes = recentlyTimeLine.split(",")[4];//len>=5 Fstation
                    FileReader ttIndirect = new FileReader("/Users/shaojintian/IntelliJProjects/transport/src/main/script/tt-"+indirectDes);

                }
                //间接的行有效
//                if(recentlyTimeLine!=null&&recentlyTimeLine.length()>=5) {
//                    String indirectDes = recentlyTimeLine.split(",")[4];//len>=5 xxxxstation
//                    //get indirect port
//                    int indirectDesPort = Response.getDesPort(indirectDes);
//                    if (indirectDesPort==-1){
//                        throw new Exception("can't find indirect des port,its name is:"+indirectDes);
//                    }
//                    //udp socket
//                    DatagramSocket  udpSocket =  Server.getUdpSocket();
//                    //package http request
////                    GET /index.html HTTP/1.1
////                            * Host: localhost:xxxx
////                            * Connection: keep-alive
////                            * Cache-Control: max-age=0
//                    DatagramPacket  finalDesName = new DatagramPacket(uri.getBytes(),uri.getBytes().length, InetAddress.getByName("127.0.0.1"),indirectDesPort);
//                    System.out.println("send indirect req...");
//                    udpSocket.send(finalDesName);
//                    byte[] recvBytes = new byte[1024];
//                    DatagramPacket recvPacket = new DatagramPacket(recvBytes, 1024);
//                    udpSocket.receive(recvPacket);
//                    response = new String(recvBytes).trim();
//                    System.out.println("indirect res:"+response);
//
//                }
            }

            System.out.println(Server.getServerName()+" send response down");

        } catch (Exception e) {
            String errorMessage = "HTTP/1.1 500 INTERNAL SERVER ERR\r\n" +
                    "Content-Type: text/html\r\n"+
                    "Content-Length: 100\r\n" + "\r\n" +
                    "<h1>Internal Server Error"+e+"</h1>";
            socket.send(new DatagramPacket(errorMessage.getBytes(),errorMessage.getBytes().length,InetAddress.getByName("127.0.0.1"),Response.getDesPort(uri)));
            // thrown if cannot instantiate a File object
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        try {
            //recv client name
            byte[] recvBytes = new byte[1024];
            DatagramPacket recvPacket = new DatagramPacket(recvBytes,1024);
            socket.receive(recvPacket);
            String desName = new String(recvBytes).trim();
            System.out.println(Server.getServerName()+" recv remote name is :"+desName);
            //send req to a specific port ,req is client name
            sendResponse(desName);


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
