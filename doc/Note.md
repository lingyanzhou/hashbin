# 散列,数字签名与密码

# 主要参考资料
[Security: Integrity, Authentication, Non-repudiation, Lecture 20](https://www.cs.rutgers.edu/~sn624/352-S19/lectures/20-sec.pdf)

## 编码

- 用途
  - 方便二进制数据在文本协议里传输 (例如MIME, URL)
- 常用编码
  - Base16 / Hex
    - 以每4比特为刻度编码, 2^4 == 16
    - 码表: "0123456789ABCDEF" (不区分大小写)
    - 编码效率: 编码后为源文件大小的2倍
  - Base32
    - 以每5比特为刻度编码, 2^5 == 32
    - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567" (不区分大小写)
    - 编码效率: 编码后为源文件大小的8/5
  - Base64
    - 以每6比特为刻度编码, 2^6 == 64
    - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    - 编码效率: 编码后为源文件大小的4/3
  - Base64Url 编码
    - 最常用的Base64变体. 把原Base64里的"+"和"/"分别替换为"-"和"_"
    - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
     
## 散列算法

[Hash(Food)](http://cache.baiducontent.com/c?m=9f65cb4a8c8507ed19fa950d100b803a0e54f726678e8b572882c81b84642c1c0733fefe7c7b4b19a9963d3d16a83d4beafa622f6a5537b790dccd179ded9d7878df7323706ddc125389&p=c2769a479f8002ff57ee927a505c82&newp=882a9643d28717dd0be296261641c4231601d13523808c0a3b8fd12590655d55113d8eff7062515f8e99736305a44a5ae1f0317930052ab19ac88d41dcbe866e42c970767f4bda1756856b&user=baidu&fm=sc&query=beef+hash&qid=c63c236b00049b62&p1=3)

 - 定义
  把任意长度内容映射为定长的散列值的函数
- 性质
  - 一般性质
    - 变长输入
    - 定长输出
    - 伪随机性(置乱性, 均衡性)
    - 快速性
  - 加密用散列函数性质
    - 单向性(抗原相性)
    - 抗碰撞性(抗第二原相性)
    - 强伪随机性(敏感性, 均衡性, 非线性性)
- 功能
  - 分桶 (Hash Table)
  - 消息摘要 (Message Digest)
  - 完整性 (Integrity)
- 算法结构
  - 初始化状态向量, 把消息分块后, 级联非线性方程, 根据最终的状态向量输出
  - [常见的Hash算法(General Purpose Hash Function Algorithms)](https://blog.csdn.net/wwchao2012/article/details/80316862)
- 代表算法
  - (CRC-m 循环冗余校验码)
    - 原始帧与预先确定的除数(m+1位)进行模2除法运算，余数(m位)作为CRC校验码
    - 多用于底层通信的校验
    - 随机性差
    - [通信原理中CRC校验原理与过程](https://baijiahao.baidu.com/s?id=1608965002019598869&wfr=spider&for=pc)
  - SDBM
    - 通用散列算法, 分桶性能好
    - hash(i) = hash(i - 1)65599 + str[i]
    - [哈希(Hash)算法 DJB/ELF/PJW/SDBM/FNV1(a)](https://blog.csdn.net/wwchao2012/article/details/80329766)
  - BKDR
    - 通用散列算法, 可通过改变seed生成一组散列函数
    - [BKDRhash ](https://www.cnblogs.com/ldy-miss/p/6099454.html)
  - MD5 (Message Digest 5)
    - 四个状态变量, 四个非线性操作函数, 对每块(512位)进行64步计算
    - 摘要长度: 128位
    - 王小云于2004年破解
    - [信息摘要算法之一：MD5算法解析及实现 ](https://www.cnblogs.com/foxclever/p/7668369.html)
  - SHA1 (Secure Hash Algorithm 1)
    - 五个状态变量, 四个非线性操作函数, 对每块(512位)进行80步计算
    - 摘要长度: 160位散列值
    - 王小云于2005年破解
    - [信息摘要算法之二：SHA1算法分析及实现](https://www.cnblogs.com/foxclever/p/8282366.html)
  - SHA256
    - 八个状态变量, 六个非线性操作函数, 对每块(512位)进行64步计算
    - 摘要长度: 256位散列值
    - [比特币算法——SHA256算法介绍](https://blog.csdn.net/wowotuo/article/details/78907380)
- 高级应用简介
  - 工作量证明 (Proof-of-Work)
    - 常用于区块链
    - 计算H(message, counter), 直到散列值达到一定要求
  - 布伦过滤器 (Bloom Filter)
    - 常用于大数据集合, 数据库
    - 计算一个元素的k个散列值, 查看BitSet里这k个位的值, 如果BitSet包含这k个值, 元素可能在集合中, 否则元素一定不在集合中
    - 牺牲正确性, 以获得常数的时间和空间复杂度
  - 一致性哈希 (Consistent Hashing)
    - 常用于分布式缓存, 规避缓存雪崩
    - 利用哈希环, 保证原有的请求可以被映射到原有的或者新的服务器中去, 而不会被映射到原来的其它服务器上去
    - [深入浅出一致性Hash原理](https://www.jianshu.com/p/e968c081f563)
- 常见攻击
  - 暴力破解
    - 对抗: 强制信息的长度, 加盐(salt)
  - 生日攻击
    - [抗碰撞性、生日攻击及安全散列函数结构解析](https://blog.csdn.net/jerry81333/article/details/52763070/)
  - 长度扩展攻击
    - 利用MD5, SHA等散列函数直接把状态向量作为输出的漏洞, 把已知的散列值作为初始状态向量, 通过操纵明文的后缀, 来获取特定的散列值.
    - 能有效规避加盐(salt)
    - 对抗: HMAC
    - [MD5的Hash长度扩展攻击](https://www.cnblogs.com/p00mj/p/6288337.html)

## HMAC

- 定义
  - 利用散列算法(Hash)，以一个密钥和一个消息为输入，生成一个消息摘要作为消息认证码 (MAC).
  - `HMAC（K，M）=H（K⊕opad∣H（K⊕ipad∣M）`
  - [rfc2104](https://tools.ietf.org/html/rfc2104)
- 算法结构
  - [HMAC的图解](https://blog.csdn.net/chengqiuming/article/details/82822933)
  - [知乎: HMAC与MAC算法在密码学的区别？](https://www.zhihu.com/question/26605600/answer/33382509)
- 功能
  - 完整性 (Integrity)
  - 可认证性 (authenticity)
- 安全性
  - 引入了密钥，其安全性已经不完全依赖于所使用的HASH算法
  - 能对抗暴力破解和长度扩展攻击
- 应用
  - “质疑/应答”(Challenge/Response)
    - 注册时, 分发给用户API Key (key)
    - 验证时, 发送一段随机字符串(challenge)给用户
    - 用户计算HMAC(key, challenge), 并发送回服务器
    - 服务器计算HMAC(key, challenge), 验证用户身份
  - JWT (JSON Web Token)
    - 在前后端分离的网页或Restful API中, 取代Session.
    - 格式: header.playload.signature, 各部分以base64Url编码后, 以'.'分隔, signature支持以HMAC"签名"
    - 好处
      - 支持跨域
      - 服务层无状态
      - 可自定义载荷
    - JWT 问题
      - 有效期问题(用户登出, JWT任然有效)
      - 默认不加密
      - 盗用
      
## 数字签名
    
      
## 对等加密
    

## 非对等加密 / 公钥加密






## 散列, HMAC与数字签名的对比

[What are the difference between a digital signature, a MAC and a hash](https://crypto.stackexchange.com/questions/5646/what-are-the-differences-between-a-digital-signature-a-mac-and-a-hash/5647#5647)

- 完整性 (Integrity): 接收方能否确认信息未被修改?

- 可认证性 (uthenticity): 接收方能否确认信息来自发送方?

- 不可否认性 (Non-repudiation): 如果接收方把信息转交给第三方,第三方能否确认信息源自发送方?

| 安全性能           | 加密用散列 |    MAC    |  数字签名             |
|:------------------|:---------:|:---------:|:--------------------:|
| 完整性             |  Yes      |    Yes    |   Yes                |
| 可认证性           |  No       |    Yes    |   Yes                |
| 不可否认性         |  No       |    No     |   Yes                |
|-------------------|-----------|-----------|----------------------|
| 密钥类型           | 无        | 对称       | 非对称                |