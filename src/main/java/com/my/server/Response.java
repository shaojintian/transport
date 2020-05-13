package com.my.server;

import java.io.*;
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

    public void sendResponse() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        try {
            //route
            String response ="";
            int flag = -1;
            String uri = request.getUri().split("=")[1];//  station name
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
            if(flag != 1){
                //没有找到
                response = "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Type: text/html\r\n"+
                        "Content-Length: 100\r\n" + "\r\n" +
                        "<h1>can't arrive this station/port.</h1>";
            }
            output.write(response.getBytes());
            System.out.println(Server.getServerName()+" send response down");

        } catch (Exception e) {
            String errorMessage = "HTTP/1.1 500 INTERNAL SERVER ERR\r\n" +
                "Content-Type: text/html\r\n"+
                "Content-Length: 23\r\n" + "\r\n" +
                "<h1>Internal Server Error</h1>";
            output.write(errorMessage.getBytes());
            // thrown if cannot instantiate a File object
            e.printStackTrace();
        } finally {
            output.close();
        }
    }
}