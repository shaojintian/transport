package com.my.server;

import java.net.DatagramPacket;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.*;
import java.util.ArrayList;
import java.util.Date;


/*
  HTTP Response =
    response line
    response headers

    response body(html)

*/

public class Response {

    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;

    public Response(OutputStream output) {
        this.output = output;
    }

    public void packageRequest(Request request) {
        this.request = request;
    }

    public static  int  getDesPort(String desName){
        for (int udpPort:Server.getDesName().keySet()) {
            if(Server.getDesName().get(udpPort).equals(desName)){
                return udpPort;
            }
        }
        return -1;
    }

    public void sendResponse() throws IOException {
        //byte[] bytes = new byte[BUFFER_SIZE];
        try {
            //route
            String response ="HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n"+
                    "Content-Length: 100\r\n" + "\r\n" +
                    "<html><h1>Can't find ant information!</h1><html>";
            int flag = -1;
            String uri = request.getUri().split("=")[1];//  B  des station name
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
                    break;
                }
            }
            //indirect arrive
            if(flag != 1){
                //没有找到
                if(recentlyTimeLine!=null&&recentlyTimeLine.length()>=5) {
                    String indirectDes = recentlyTimeLine.split(",")[4];//len>=5 Fstation
                    //
                    FileReader ttIndirect = new FileReader("/Users/shaojintian/IntelliJProjects/transport/src/main/script/tt-"+indirectDes);
                    bufferedtt = new BufferedReader(ttIndirect);
                    //leave time /arrive time date format
                    Date fromDateI = simpleFormat.parse("2020-01-01 "+recentlyArriveT);
                    recentlyArriveT = "23:59";
                    //target arrive time
                    timeLine =null;
                    while ((timeLine=bufferedtt.readLine())!=null){
                        //check leave time
                        //10:36
                        if(timeLine.split(",").length<4)continue;
                        if(!timeLine.split(",")[4].equals(uri))continue;
                        //System.out.println("timeline:"+timeLine);
                        String leaveT =timeLine.split(",")[0].trim();
                        String arriveT =timeLine.split(",")[3].trim();//len>=4
                        Date temLeaveDateI = simpleFormat.parse("2020-01-01 "+leaveT);
                        if(temLeaveDateI.compareTo(fromDateI)>=0){
                            if(arriveT.compareTo(recentlyArriveT)<0){//cur < recently
                                recentlyArriveT = arriveT;
                                recentlyTimeLine = timeLine;
                            }
                        }
                    }
                    System.out.println("indirect:"+recentlyTimeLine);
                    if(recentlyTimeLine.split(",")[4].trim().equals(uri)){
                        response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: text/html\r\n"+
                                "Content-Length: 100\r\n" + "\r\n" +
                                "<html><h1>"+recentlyTimeLine+"</h1><html>";
                    }
                    //
//                    //get indirect port
//                    int indirectDesPort = getDesPort(indirectDes);
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
//                    DatagramPacket  indirectDesName = new DatagramPacket(uri.getBytes(),uri.getBytes().length, InetAddress.getByName("127.0.0.1"),indirectDesPort);
//                    System.out.println("send indirect req...");
//                    udpSocket.send(indirectDesName);
//                    byte[] recvBytes = new byte[1024];
//                    DatagramPacket recvPacket = new DatagramPacket(recvBytes, 1024);
//                    udpSocket.receive(recvPacket);
//                    response = new String(recvBytes).trim();
//                    System.out.println("indirect res:"+response);

                }
            }

            output.write(response.getBytes());
            System.out.println(Server.getServerName()+" send response down");

        } catch (Exception e) {
            String errorMessage = "HTTP/1.1 500 INTERNAL SERVER ERR\r\n" +
                "Content-Type: text/html\r\n"+
                "Content-Length: 100\r\n" + "\r\n" +
                "<h1>Internal Server Error"+e+"</h1>";
            output.write(errorMessage.getBytes());
            // thrown if cannot instantiate a File object
            e.printStackTrace();
        } finally {
            output.close();
        }
    }
}