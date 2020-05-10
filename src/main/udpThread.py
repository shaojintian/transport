#!/usr/bin/env python
import socket


IP = "127.0.0.1"
def udpThread(desNameMap, serverName,udpSocket,port):
    try:
        #print("send bytes....")
        udpSocket.sendto(bytes(serverName,encoding="utf-8"),(IP,port))
        #recv
        recvStr = str(udpSocket.recv(1024)).strip()
        #print("recvStr :",recvStr)
        desNameMap[port] = recvStr
        #print(len(desNameMap))

    except Exception as e:
        print(e)







