package lzhou.learning.hash.hashbin.controller;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import org.apache.tomcat.util.buf.ByteBufferUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.google.common.hash.Hashing;

import java.nio.ByteBuffer;

/**
 * @Description: 散列算法
 * [Hash直译](http://image.baidu.com/search/index?tn=baiduimage&ps=1&ct=201326592&lm=-1&cl=2&nc=1&ie=utf-8&word=beef+hash)
 * - 定义
 *   把任意长度内容映射为定长的散列值的函数
 * - 应用
 *   - 分桶 (Hash Table)
 *   - 消息摘要 (Message Digest)
 *   - 完整性校验和(Checksum)
 *   - 数字签名 (Digital Signature)
 * - 性质
 *   - 一般性质
 *     - 变长输入
 *     - 定长输出
 *     - 伪随机性(置乱性, 均衡性)
 *     - 快速性
 *   - 密码学哈希
 *     - 单向性(抗原相性)
 *     - 抗碰撞性(抗第二原相性)
 *     - 强伪随机性(敏感性, 均衡性, 非线性性)
 * - 散列算法结构
 *   - 初始化状态向量, 把消息分块后, 级联非线性方程, 根据最终的状态向量输出
 *   - [Link](https://blog.csdn.net/wwchao2012/article/details/80316862)
 * - 代表算法
 *   - (CRC-m 循环冗余校验码)
 *     - 原始帧与预先确定的除数(m+1位)进行模2除法运算，余数(m位)作为CRC校验码
 *     - 多用于底层通信的校验
 *     - 随机性差
 *     - [Link](https://baijiahao.baidu.com/s?id=1608965002019598869&wfr=spider&for=pc)
 *   - SDBM
 *     - 通用哈希算法, 分桶性能好
 *     - hash(i) = hash(i - 1) * 65599 + str[i]
 *     - [Link](https://blog.csdn.net/wwchao2012/article/details/80329766)
 *     - [通用哈希对比](https://www.cnblogs.com/cavehubiao/p/3412599.html)
 *   - MD5 (Message Digest 5)
 *     - 四个状态变量, 四个非线性操作函数, 对每块(512位)进行64步计算
 *     - 摘要长度: 128位
 *     - 王小云于2004年破解
 *     - [Link](https://www.cnblogs.com/foxclever/p/7668369.html)
 *   - SHA1 (Secure Hash Algorithm 1)
 *     - 五个状态变量, 四个非线性操作函数, 对每块(512位)进行80步计算
 *     - 摘要长度: 160位散列值
 *     - 王小云于2004年破解
 *     - [Link](https://www.cnblogs.com/foxclever/p/8282366.html)
 *   - SHA256
 *     - 八个状态变量, 六个非线性操作函数, 对每块(512位)进行64步计算
 *     - 摘要长度: 256位散列值
 *     - 王小云于2005年破解
 *     - [Link](https://blog.csdn.net/wowotuo/article/details/78907380)
 * - 高级应用简介
 *   - Proof-of-Work
 *     - 计算H(message, counter), 直到散列值达到一定要求
 *   - 布伦过滤器
 *     - , 判断一个元素一定不在集合内
 *   - 一致性哈希
 *     -
 * - 常见攻击
 *   - 暴力破解
 *     - 强制信息的长度
 *     - 加盐(salt)
 *   - 生日攻击
 *     - [Link](https://blog.csdn.net/jerry81333/article/details/52763070/)
 *   - 长度扩展攻击
 *     - 利用MD5, SHA1等散列函数直接把状态向量作为输出的漏洞, 把已知的散列值作为初始状态项链, 通过操纵明文的后缀, 来获取特定的散列值.
 *     - 能有效规避加盐(salt)
 *     - [Link](https://www.cnblogs.com/p00mj/p/6288337.html)
 * @author: lingy
 * @Date: 2019-06-06 10:37:50
 * @param: null
 * @return:
 */
@RestController
@RequestMapping("ajax")
public class MessageDigestAjaxController {

    /**
     * @Description: CRC-m 循环冗余校验码
     * 原始帧与预先确定的除数进行模2除法运算，余数作为CRC校验码
     * 伪随机性差
     * @link https://baijiahao.baidu.com/s?id=1608965002019598869&wfr=spider&for=pc
     * @author: lingy
     * @Date: 2019-06-10 14:48:10
     * @param: null
     * @return:
     */
    @PostMapping(value = "crc32", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String crc32(@RequestBody byte[] input) {
        return BaseEncoding.base16().encode(Hashing.crc32().hashBytes(input).asBytes());
    }

    /**
     * @Description: SDBM 散列算法
     * 通用哈希算法, 分桶性能好
     * hash(i) = hash(i - 1) * 65599 + str[i]
     * @link https://blog.csdn.net/wwchao2012/article/details/80329766
     * @author: lingy
     * @Date: 2019-06-10 14:48:10
     * @param: null
     * @return:
     */
    @PostMapping(value = "sdbm", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String sdbm(@RequestBody byte[] input) {
        //TODO
        return BaseEncoding.base16().encode(input);
    }

    /**
     * @Description: MD5 散列算法
     * @link https://blog.csdn.net/xuejianbest/article/details/80391237
     * @author: lingy
     * @Date: 2019-06-06 10:37:48
     * @param: input
     * @return: java.lang.String
     */
    @PostMapping(value = "md5", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String md5(@RequestBody String input) {
        return BaseEncoding.base16().encode(Hashing.md5().hashString(input, Charsets.UTF_8).asBytes());
    }

    /**
     * @Description: SHA1 散列算法
     * @author: lingy
     * @Date: 2019-06-10 10:48:50
     * @param: input
     * @return: java.lang.String
     */
    @PostMapping(value = "sha1", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String sha1(@RequestBody String input) {
        return BaseEncoding.base16().encode(Hashing.sha1().hashString(input, Charsets.UTF_8).asBytes());
    }

    /**
     * @Description: SHA256 散列算法
     * @author: lingy
     * @Date: 2019-06-10 10:48:50
     * @param: input
     * @return: java.lang.String
     */
    @PostMapping(value = "sha256", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String sha256(@RequestBody byte[] input) {
        return BaseEncoding.base16().encode(Hashing.sha256().hashBytes(input).asBytes());
    }

    @PostMapping("java-hash-code")
    public String javaHashCode(@RequestBody String input) {
        return HexUtils.toHexString(
                ByteBuffer.allocate(4)
                        .putInt(input.hashCode())
                        .array()
        );
    }
}