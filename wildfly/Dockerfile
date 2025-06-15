FROM openjdk:21-jdk
COPY wildfly-custom /opt/wildfly
ENV JBOSS_HOME=/opt/wildfly
EXPOSE 8080 8443 9990
CMD ["/opt/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
