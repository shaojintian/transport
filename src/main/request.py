#!/usr/bin/env python
import socket

class Requset:
    def __init__(self,inp):
        self.inp =inp
        self.uri =""
        self.request=""

    def parse(self):
        ii =-1;
        try:
            self.request = str(self.inp.recv(1024)).strip()
            self.parseUri()
        except Exception as e:
            print(e)

    def parseUri(self):
        index1 = self.request.find(' ')
        index2 = -1
        if index1 != -1:
            index2 = self.request.find(' ',index1+1)
        if index2 > index1:
            self.uri =self.request[index1+1:index2]

    def getUri(self):
        return self.uri


