FROM mcr.microsoft.com/java/jdk:8u192-zulu-alpine

RUN mkdir /output
VOLUME /output
WORKDIR output

CMD ["keytool", "-genkey", "-alias", "server", "-keyalg", "RSA", "-storepass", "testtest", "-keypass", "testtest", "-dname", "CN=localhost", "-keystore", "server.jks"]