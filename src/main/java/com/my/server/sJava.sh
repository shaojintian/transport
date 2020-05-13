#!/usr/bin/env bash

javac -d ./bin/ ./*.java
java -cp ./bin com.my.server.Server JunctionA 4001 4002 4012 &
java -cp ./bin com.my.server.Server BusportB 4003 4004 4006 4008 4010 4012 &
java -cp ./bin com.my.server.Server StationC 4005 4006 4004 4012 &
java -cp ./bin com.my.server.Server TerminalD 4007 4008 4004 &
java -cp ./bin com.my.server.Server JunctionE 4009 4010 4004 &
java -cp ./bin com.my.server.Server BusportF 4011 4012 4002 4004 4006 &
