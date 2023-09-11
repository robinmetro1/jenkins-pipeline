FROM openjdk:17
EXPOSE 8080
ADD target/devops-demo.jar back/devops-demo.jar
ENTRYPOINT ["java","-jar","/back/devops-demo.jar"]
CMD ["-start"]