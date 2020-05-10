package com.my.server;

import java.io.InputStream;
import java.io.IOException;

public class Request {

    private InputStream input;
    // /xxx-station.html
    private String uri;
    private StringBuffer request;

    public Request(InputStream input) {
        this.input = input;
    }

    //从InputStream中读取request信息，并从request中获取uri值
    public void parse() {
        request = new StringBuffer(2048);
        int ii=-1;
        byte[] buffer = new byte[2048];
        try {
            ii = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        for (int j = 0; j < ii; j++) {
            request.append((char) buffer[j]);
        }
        //print des name map
        for (int port:Server.getDesName().keySet()) {
            System.out.println("------------------des name:"+Server.getDesName().get(port)+" port:"+port);
        }
        uri = parseUri(request.toString());
    }

    /**
     *
     * requestString形式如下：
     * GET /index.html HTTP/1.1
     * Host: localhost:xxxx
     * Connection: keep-alive
     * Cache-Control: max-age=0
     * ...
     * 该函数目的就是为了获取/index.html字符串
     */
    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    public String getUri() {
        return uri;
    }

    public StringBuffer getRequest(){return this.request;}
}