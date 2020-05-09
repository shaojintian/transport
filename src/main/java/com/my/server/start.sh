#!/bin/bash

java Server.class North_Terminus 4001 4002 4004 &
java Server.class East_Station 4003 4004 4002 4008 4006 &
java Server.class West_Station 4005 4006 4004 4008 &
java Server.class South_Busport 4007 4008 4006 4004 &
