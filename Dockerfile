# 选择openjdk:17 作为运行环境，该环境中有java
FROM openjdk:17

#将 JAR文件复制到容器里
COPY target/*.jar /app.jar
#指示暴露端口8080
EXPOSE 8080
#容器启动时运行命令
CMD ["java","-jar","app.jar"]