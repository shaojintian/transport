package com.my.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class ServerThread implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ServerThread.class);
    private Socket socket;
    //private T obj;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ServerThread(Socket socket){
        this.socket =socket;
        //this.obj=obj;
    }

    @Override
    public void run(){
        //服务端接受invoked method params,types and method name && class which this method in
        try {
            ois =  new ObjectInputStream(socket.getInputStream());
            //read clazz of rpc method
            Object obj = ois.readObject();

            //write ans to oos
            String ans = "successful!!this is java server";
            oos = new ObjectOutputStream(socket.getOutputStream());
            try {
                oos.writeObject(ans);
            }catch (Exception e){
                System.out.println("error:"+this.getClass().getName()+e);
            }

            logger.info("Sending ans to client successful!");
        }catch (Exception e){
            logger.error("server err");
        }finally {
            close(ois);
            close(oos);
            close(socket);
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
