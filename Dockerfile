FROM java:8
ADD target/cron-data-backend-0.0.1-SNAPSHOT.war crondata.war
HEALTHCHECK --start-period=30s --interval=10s --timeout=10s \
  CMD curl -f http://localhost:8080/api/ping
EXPOSE 8080
ENTRYPOINT ["java","-jar","crondata.war"]
