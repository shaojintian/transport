package com.my.server;

import com.alibaba.fastjson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springfox.documentation.spring.web.json.Json;

import java.io.*;
import java.net.*;
public class ServerThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ServerThread.class);
    private Socket socket;
    //private T obj;
    private BufferedReader is;
    private OutputStream os;
    //why need buffer ? because we need to package the req/res
    public ServerThread(Socket socket){
        this.socket =socket;
        //this.obj=obj;
    }

    @Override
    public void run(){
        InputStream input = null;
        OutputStream output = null;
        try {
            //等待连接，连接成功后，返回一个Socket对象
            input = socket.getInputStream();
            output = socket.getOutputStream();

            // 创建Request对象并解析
            Request request = new Request(input);
            request.parse();


            // 创建 Response 对象
            Response response = new Response(output);
            response.packageRequest(request);
            response.sendResponse();

            // 关闭 socket 对象
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void close(Closeable closeable) {
        if (closeable!=null){
            try {
                closeable.close();
            }catch (IOException e){
                logger.error(e+"");
            }
        }
    }
}
