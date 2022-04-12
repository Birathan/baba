FROM openjdk:11
ADD baba.jar baba.jar
ADD .env .env
ENTRYPOINT ["java","-cp", "baba.jar", "CommandHandler"]