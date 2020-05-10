#!/bin/bash

python ./server.py North_Terminus 4001 4002 4004 &
python ./server.py East_Station 4003 4004 4002 4008 4006 &
python ./server.py West_Station 4005 4006 4004 4008 &
python ./server.py South_Busport 4007 4008 4006 4004 &


#python ./server.py North_Terminus 4001 4002 4004 & python ./server.py East_Station 4003 4004 4002 4008 4006 & python ./server.py West_Station 4005 4006 4004 4008 & python ./server.py South_Busport 4007 4008 4006 4004 &