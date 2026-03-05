#!/usr/bin/env bash
# 使用项目指定的 JDK 21 执行命令（Maven 编译、运行等）
# 用法: ./run-with-jdk.sh <命令> [参数...]
# 示例: ./run-with-jdk.sh mvn clean compile
#       ./run-with-jdk.sh mvn -pl geek-admin spring-boot:run

export JAVA_HOME="/Users/msc/Library/Java/JavaVirtualMachines/ms-21.0.10/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

exec "$@"
