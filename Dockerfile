FROM java:8
ADD target/cron-data-backend-0.0.1-SNAPSHOT.war crondata.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","crondata.war"]
