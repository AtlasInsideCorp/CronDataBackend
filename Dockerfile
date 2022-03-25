FROM java:8
COPY ./applications /usr/share/crondata/applications
COPY ./target/crondata-backend-0.0.1-SNAPSHOT.war crondata.war
HEALTHCHECK --start-period=250s --interval=10s --timeout=15s \
  CMD curl -f http://localhost:8080/api/ping

EXPOSE 8080
ENTRYPOINT ["java","-jar","crondata.war"]
