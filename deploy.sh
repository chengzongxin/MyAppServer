#!/bin/bash

# 变量定义
APP_NAME="MyAppServer"
JAR_FILE="target/${APP_NAME}-0.0.1-SNAPSHOT.jar"
DEPLOY_PATH="/opt/myapp"
LOG_PATH="/var/log/myapp"
JAVA_OPTS="-Xms512m -Xmx1024m -Dspring.profiles.active=prod"

# 创建必要的目录
mkdir -p ${DEPLOY_PATH}
mkdir -p ${LOG_PATH}

# 停止旧的进程
PID=$(ps -ef | grep ${APP_NAME} | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]; then
    echo "Stopping existing application..."
    kill ${PID}
    sleep 5
fi

# 备份旧版本
if [ -f "${DEPLOY_PATH}/${APP_NAME}.jar" ]; then
    mv ${DEPLOY_PATH}/${APP_NAME}.jar ${DEPLOY_PATH}/${APP_NAME}.jar.bak
fi

# 复制新的jar包
cp ${JAR_FILE} ${DEPLOY_PATH}/${APP_NAME}.jar

# 启动应用
echo "Starting application..."
nohup java ${JAVA_OPTS} -jar ${DEPLOY_PATH}/${APP_NAME}.jar > ${LOG_PATH}/startup.log 2>&1 &

# 检查启动状态
sleep 10
if ps -ef | grep ${APP_NAME} | grep -v grep > /dev/null; then
    echo "Application started successfully!"
else
    echo "Failed to start application!"
    exit 1
fi 