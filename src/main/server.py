#!/usr/bin/env python
import socket,signal
import sys,time
import os,_thread
from request import  Requset
from response import Response
from udpThread import udpThread

UFT8 = 'utf-8'
IP = '127.0.0.1'
# request

# GET /index.html HTTP/1.1
# Host: localhost:xxxx
# Connection: keep-alive
# Cache-Control: max-age=0

desNameMap = {}
serverName = ""
def startService(argv):
    # exit
    #_thread.start_new_thread(do_exit(),None)
    global serverName
    serverName = argv[1]
    tcpPort = int(argv[2])
    udpPort = int(argv[3])
    des = []
    #udpsocket
    udpSocket = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
    udpSocket.bind((IP,udpPort))

    for port in argv[4:]:
        des.append(int(port))
        _thread.start_new_thread(udpThread,(desNameMap,serverName,udpSocket,int(port)))
    #wait udp fin
    time.sleep(1)

    #print map
    # print('map size:',len(desNameMap))
    # for k,v in desNameMap.items():
    #     print("----------")
    #     print(k,v)

    #tcp
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.bind((IP, tcpPort))
    sock.listen(5)
    # infinite loop
    while True:
         # maximum number of requests waiting
         print(serverName+" wait a conn in....")
         conn, addr = sock.accept()
         doTcpThread(conn)


def do_exit():
    signal.signal(signal.SIGINT,os._exit(0))
    signal.pause()
    while True: # busy waiting
        pass

def doTcpThread(so):
    try:
        #req
        #print("req-----------")
        req = Requset(so)
        req.parse()
        #res
        #print("res-----------")
        res = Response(req,desNameMap,serverName)
        res.sendRes(so)
    except Exception as e:
        print(e)
    finally:
        so.close()


if __name__ == '__main__':
    #B/S model
    startService(sys.argv)





