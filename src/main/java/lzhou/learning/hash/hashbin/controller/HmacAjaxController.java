package lzhou.learning.hash.hashbin.controller;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.crypto.spec.DESKeySpec;
import java.security.*;

/**
 * @Description HMAC (Keyed-Hashing for Message Authentication Code)
 * - 定义
 *   - 利用散列算法(Hash)，以一个密钥和一个消息为输入，生成一个消息摘要作为消息认证码 (MAC).
 *   - `HMAC（K，M）=H（K⊕opad∣H（K⊕ipad∣M）`
 *   - [rfc2104](https://tools.ietf.org/html/rfc2104)
 * - 算法结构
 *   - [HMAC的图解](https://blog.csdn.net/chengqiuming/article/details/82822933)
 *   - [知乎: HMAC与MAC算法在密码学的区别？](https://www.zhihu.com/question/26605600/answer/33382509)
 * - 功能
 *   - 完整性 (Integrity)
 *   - 可认证性 (Authentication)
 * - 安全性
 *   - 引入了密钥，其安全性已经不完全依赖于所使用的HASH算法
 *   - 能对抗暴力破解和长度扩展攻击
 * - 应用
 *   - “质疑/应答”(Challenge/Response)
 *     - 注册时, 分发给用户API Key (key)
 *     - 验证时, 发送一段随机字符串(challenge)给用户
 *     - 用户计算HMAC(key, challenge), 并发送回服务器
 *     - 服务器计算HMAC(key, challenge), 验证用户身份
 *   - JWT (JSON Web Token)
 *     - 在前后端分离的网页或Restful API中, 取代Session.
 *     - 格式: header.playload.signature, 各部分以base64Url编码后, 以'.'分隔, signature支持以HMAC"签名"
 *     - 好处
 *       - 支持跨域
 *       - 服务层无状态
 *       - 可自定义载荷
 *     - JWT 问题
 *       - 有效期问题(用户登出, JWT任然有效)
 *       - 默认不加密
 *       - 盗用
 * @author: lingy
 * @Date: 2019-06-10 15:53:25
 * @param: null
 * @return:
 */
@RestController
@RequestMapping("ajax/hmac")
public class HmacAjaxController {
    @Value("${hmac.key}")
    private String keyStr;

    byte[] keyBytes = null;

    @PostMapping("hmac-md5")
    public String hmacMd5(@RequestBody byte[] data) throws NoSuchAlgorithmException {
        return BaseEncoding.base16().encode(
                Hashing.hmacMd5(keyBytes).hashBytes(data).asBytes()
        );
    }

    @PostMapping("hmac-sha256")
    public String hmacSha256(@RequestBody byte[] data) throws NoSuchAlgorithmException {
        return BaseEncoding.base16().encode(
                Hashing.hmacSha256(keyBytes).hashBytes(data).asBytes()
        );
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        keyBytes = new DESKeySpec(keyStr.getBytes()).getKey();
    }
}
