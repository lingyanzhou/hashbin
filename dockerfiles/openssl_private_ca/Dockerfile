FROM debian

RUN apt-get update && \
    apt-get install -y openssl && \
    apt-get purge -y --auto-remove

RUN mkdir -p /etc/pki/CA/private/ && \
    mkdir -p /etc/pki/CA/certs/ && \
    mkdir -p /etc/pki/CA/newcerts/ && \
    mkdir -p /etc/pki/tls/private/ && \
    mkdir -p /etc/pki/tls/certs/ && \
    touch /etc/pki/CA/index.txt && \
    touch /etc/pki/CA/index.txt.attr && \
    echo 01 > /etc/pki/CA/serial

COPY openssl.cnf /usr/lib/ssl/openssl.cnf

WORKDIR /
COPY gen_private_ca_signed_cert.sh /
CMD ["sh", "/gen_private_ca_signed_cert.sh"]
