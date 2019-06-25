# - 生成根CA私钥和自签名证书

openssl req \
    -x509 \
    -sha256 \
    -newkey rsa:2048 \
    -keyout /etc/pki/CA/private/cakey.pem \
    -out /etc/pki/CA/cacert.pem \
    -days 1024 \
    -nodes \
    -subj "/C=CN/ST=SC/L=CD/O=chaoxing/OU=dev/CN=privateCA"

# - 生成Web服务器证书申请

#   - 生成密钥

openssl genrsa \
    -aes256 \
    -passout pass:testtest \
    -out /etc/pki/tls/private/docker.local.pem 2048

#   - 生成证书申请

openssl req \
    -new \
    -passin pass:testtest \
    -key /etc/pki/tls/private/docker.local.pem \
    -out /etc/pki/tls/certs/docker.local.csr \
    -subj "/C=CN/ST=SC/L=CD/O=chaoxing/OU=dev/CN=docker.local"

#   - 上传证书申请给CA服务器

# - 根CA签发Web服务器证书

#   - 签发证书

openssl ca \
    -in /etc/pki/tls/certs/docker.local.csr \
    -out /etc/pki/CA/certs/docker.local.crt \
    -batch \
    -days 365

