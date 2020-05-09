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
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n"+
                    "Content-Length: 23\r\n" + "\r\n" +
                    "<h1>It works</h1>";
            output.write(response.getBytes());

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