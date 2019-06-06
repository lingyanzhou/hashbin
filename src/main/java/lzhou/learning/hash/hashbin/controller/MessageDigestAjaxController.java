package lzhou.learning.hash.hashbin.controller;

import com.google.common.base.Charsets;
import org.apache.tomcat.util.buf.ByteBufferUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.hash.Hashing;

import java.nio.ByteBuffer;

/**
 * @Description: 常见哈希/散列算法
 * - 定义
 *   把任意长度内容映射为定长的散列值的函数
 * - 功能
 *   - 分桶
 *   - 讯息摘要
 *   - 数组签名
 * - 性质
 *   - 一般哈希
 *     - 变长输入
 *     - 定长输出
 *     - 伪随机性
 *     - 快速性
 *   - 密码学哈希
 *     - 单向性(抗原相性)
 *     - 抗碰撞性(抗第二原相性)
 *     - 强伪随机性(敏感性,均衡性,非线性性)
 * - 代表算法
 *   - MD5
 *   - SHA1
 *   - SHA256
 *   - HMAC
 *   - 一致性哈希
 * @author: lingy
 * @Date: 2019-06-06 10:37:50
 * @param: null
 * @return:
 */
@RestController
@RequestMapping("ajax")
public class MessageDigestAjaxController {

    /**
     * @Description: MD5 散列算法
     * @author: lingy
     * @Date: 2019-06-06 10:37:48
     * @param: input
     * @return: java.lang.String
     */
    @PostMapping("md5")
    public String md5(@RequestBody String input) {
        return Hashing.md5().hashString(input, Charsets.UTF_8).toString();
    }

    @PostMapping("sha1")
    public String sha1(@RequestBody String input) {
        return Hashing.sha1().hashString(input, Charsets.UTF_8).toString();
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
