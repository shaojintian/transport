#!/usr/bin/env python
import socket

BUFFER_SIZE = 1024

class Response:
    def __init__(self,req,desNameMap,serverName):
        self.ser = serverName
        self.map =desNameMap
        self.req = req
    def sendRes(self,so):
        try:
            flag=-1
            uri = self.req.getUri().split('=')[1].strip()
            print(uri+'-------------uri')
            response = ""
            for k in self.map.keys():
                name = self.map[k]
                print(name+'----------name')
                # buy in str1 == str2  ?encode type ?\n \t whitespace
                if name == uri:
                    response ='''HTTP/1.1 200 OK
                    Content-Type: text/html
                    Content-Length: 100\r\n
                    <html><h1>connect successfully! This is %s. Its udp port is %d </h1><html>
                    '''%(name,k)
                    print(response)
                    flag =1
                    break
            if flag!=1:
                response = '''HTTP/1.1 200 OK
                        Content-Type: text/html
                        Content-Length: 100\r\n
                        <h1>They don't connect with each other directly.</h1>
                        '''
            so.sendall(bytes(response,encoding="utf-8"))
        except Exception as  e:
            err = '''HTTP/1.1 500 INTERNAL ERROR
                Content-Type: text/html
                Content-Length: 100\r\n
                <h1>Server internal err</h1>'''
            so.sendall(bytes(err,encoding="utf-8"))
            print(e)
        finally:
            so.close()



