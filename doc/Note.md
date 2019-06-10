# 散列,数字签名与密码

## 编码

- 用途
  - 方便二进制数据在文本协议里传输 (例如MIME, URL)
- 常用编码
  - Base16 / Hex
  - Base32
  - Base64

### Base16 / Hex 编码
  - 以每4比特为刻度编码, 2^4 == 16
  - 码表: "0123456789ABCDEF" (不区分大小写)
  - 编码效率: 编码后为源文件大小的2倍

### Base32 编码
  - 以每5比特为刻度编码, 2^5 == 32
  - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567" (不区分大小写)
  - 编码效率: 编码后为源文件大小的8/5
     
### Base64 编码
  - 以每6比特为刻度编码, 2^6 == 64
  - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
  - 编码效率: 编码后为源文件大小的4/3
     
### Base64Url 编码
 - 最常用的Base64变体. 把原Base64里的"+"和"/"分别替换为"-"和"_"
 - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
     
## 散列

## HMAC

### MAC

### HMAC-MD5

## 数字签名



## 散列, HMAC 与数字签名

[Source](https://crypto.stackexchange.com/questions/5646/what-are-the-differences-between-a-digital-signature-a-mac-and-a-hash/5647#5647)

- Integrity: 接收方能否确认信息未被修改?

- Authentication: 接收方能否确认信息来自发送方?

- Non-repudiation: 如果接收方把信息转交给第三方,第三方能否确认信息源自发送方?

| Security Goal           | Hash |    MAC    |  Digital Signiture   |
|-------------------------|------|-----------|----------------------|
| Integrity               |  Yes |    Yes    |   Yes                |
| Authentication          |  No  |    Yes    |   Yes                |
| Non-repudiation         |  No  |    No     |   Yes                |
|-------------------------|------|-----------|----------------------|
| Kind of keys            | none | symmetric | asymmetric           |