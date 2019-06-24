FROM debian
RUN mkdir /output && \
    apt-get update && \
    apt-get install -y openssl && \
    apt-get purge -y --auto-remove
VOLUME /output
WORKDIR /output
CMD ["openssl", "req", "-x509", "-sha256", "-newkey", "rsa:2048", "-keyout", "certificate.key", "-out", "certificate.crt", "-days", "1024", "-nodes", "-subj", "/CN=nginxhttps"]
