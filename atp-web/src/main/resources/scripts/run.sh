#!/bin/bash

APP="atp-web"
PHOME=$(dirname `readlink -f "$0"`)
PHOME=$(dirname $PHOME)

APP=${APP}$1
echo "app name is ${APP} "


#JMX_PORT=`expr 8500`
JAVA_OPTS="-server -Xms1024m -Xmx1024m -Xmn384m -XX:MaxPermSize=128m \
-Xss256k -XX:+UseConcMarkSweepGC \
-XX:+UseParNewGC -XX:CMSFullGCsBeforeCompaction=5 \
-XX:+UseCMSCompactAtFullCollection \
-XX:+PrintGC -Xloggc:/data/logs/${APP}/gc_$1.log"
#-Djava.rmi.server.hostname=114.215.143.54 \
#-Dcom.sun.management.jmxremote \
#-Dcom.sun.management.jmxremote.port=$JMX_PORT \
#-Dcom.sun.management.jmxremote.authenticate=false \
#-Dcom.sun.management.jmxremote.ssl=false"

pid=`ps -eo pid,args | grep ${APP} | grep java | grep -v grep | awk '{print $1}'`
echo "-----------------${APP} pid is ${pid}-----------------"

if [ -n "$pid" ]
then
    kill -3 ${pid}
    kill ${pid} && sleep 3
    if [  -n "`ps -eo pid | grep $pid`" ]
    then
        kill -9 ${pid}
    fi
    echo "-----------------kill pid: ${pid}-----------------"
fi

if [  -n "`ps -eo pid | grep $pid`" ]
then
    echo "-----------------kill failure!-----------------"
fi

echo "start ...."
java $JAVA_OPTS -cp ${PHOME}/conf:${PHOME}/lib/* com.lsh.atp.web.Application ${APP} > /dev/null 2>&1 &


#java -Ddubbo.spring.config=classpath*:META-INF/spring/*.xml $JAVA_OPTS -cp ${PHOME}/conf:${PHOME}/lib/* com.lsh.risk.bootstrap.RiskProviderMain ${APP} > /dev/null 2>&1 &
