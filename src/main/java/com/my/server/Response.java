package com.my.server;

import javax.jws.Oneway;
import java.io.OutputStream;
import java.io.IOException;


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
            Server.printMap();
            for (int udpPort:Server.getDesName().keySet()) {
                String stationName  =Server.getDesName().get(udpPort);
                if(uri.equals(stationName)){
                    response = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/html\r\n"+
                            "Content-Length: 100\r\n" + "\r\n" +
                            "<html><h1>connect successfully! This is "+stationName+". Its udp port is "+udpPort+"</h1><html>";
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
                        "<h1>They don't connect with each other directly.</h1>";
            }
            output.write(response.getBytes());
            System.out.println(Server.getServerName()+" send response down");

        } catch (Exception e) {
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                "Content-Type: text/html\r\n"+
                "Content-Length: 23\r\n" + "\r\n" +
                "<h1>File Not Found</h1>";
            output.write(errorMessage.getBytes());
            // thrown if cannot instantiate a File object
            e.printStackTrace();
        } finally {
            output.close();
        }
    }
}