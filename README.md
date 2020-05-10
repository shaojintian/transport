
run mixture servers in background
```bash
cd .../transport/src/main/java/com/my/server
 ./killservers.sh&&./start.sh  
```

only java servers
```
./killservers.sh&& ./sJava.sh 

```

only python servers
```
./kill.sh&& ./startPyServer.sh
```

net
```
socket in java and python
```

```
browser --- server
type : byte[]
python client : bytes('xxxx\n',encoding=...)
because java is readLine() ,need \n to stop.

java read based on line '\n',so python req need +'\n'
python read baed on bytes length ,so java req need limit req length
```