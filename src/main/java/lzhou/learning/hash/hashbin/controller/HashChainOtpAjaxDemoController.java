package lzhou.learning.hash.hashbin.controller;

import com.google.common.hash.Hashing;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 散列链一次性口令
 * - 散列链是一种由单个密钥或密码生成多个一次性密钥或密码的一种方法. 例如`h^4(x) = h(h(h(h(x))))`
 * - 作为一种在非安全环境中的密码保护方案
 *   - 服务器储存用户提供的口令`h^1000(password)`
 *   - 验证时, 用户提供`h^999(password)`, 服务器验证`h(h^999(password))`
 *   - 如果验证成功, 服务器更新口令为`h^999(password)`
 *   - [哈希链](https://baike.baidu.com/item/%E5%93%88%E5%B8%8C%E9%93%BE/10230309)
 * @author: lingy
 * @Date: 2019-06-17 08:52:28
 * @param: null
 * @return:
 */
@RestController
@RequestMapping("ajax/hash-chain-otp")
public class HashChainOtpAjaxDemoController {
    private byte[] hash = new byte[0];

    @PostMapping(value="set-hash", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void setHash(@RequestBody byte[] hash) {
        this.hash = hash;
    }

    @PostMapping(value="validate", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public boolean validate(@RequestBody byte[] hash) {
        if (Hashing.md5().hashBytes(hash).equals(this.hash)) {
            this.hash = hash;
            return true;
        }
        return false;
    }
}
