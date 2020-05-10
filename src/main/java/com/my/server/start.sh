#!/bin/bash

#compile again
#javac -d ./bin/ ./*.java
#java -cp ./bin  com.my.server.Server North_Terminus 4001 4002 4004 &
#java -cp ./bin  com.my.server.Server East_Station 4003 4004 4002 4008 4006 &
#java -cp ./bin  com.my.server.Server West_Station 4005 4006 4004 4008 &
#java -cp ./bin  com.my.server.Server South_Busport 4007 4008 4006 4004 &


javac -d ./bin/ ./*.java
java -cp ./bin  com.my.server.Server North_Terminus 4001 4002 4004 &
java -cp ./bin  com.my.server.Server East_Station 4003 4004 4002 4008 4006 &
python ../../../../server.py West_Station 4005 4006 4004 4008 &
python ../../../../server.py South_Busport 4007 4008 4006 4004 &

