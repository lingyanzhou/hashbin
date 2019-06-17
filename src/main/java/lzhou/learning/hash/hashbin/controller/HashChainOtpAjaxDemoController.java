package lzhou.learning.hash.hashbin.controller;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    private String hash = "";

    @PostMapping(value="init-hash", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void initHash(@RequestBody String hash) {
        this.hash = hash;
    }

    @PostMapping(value="validate", consumes = MediaType.TEXT_PLAIN_VALUE)
    public boolean validate(@RequestBody String hash) {
        if (BaseEncoding.base16().encode(Hashing.md5().hashBytes(BaseEncoding.base16().decode(hash)).asBytes())
                .equals(this.hash)) {
            this.hash = hash;
            return true;
        }
        return false;
    }

    @PostMapping(value="hashchain", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String hashchain(@RequestParam String password, @RequestParam Integer iteration) throws IOException {
        byte[] hashCode = password.getBytes();
        for (int i=0; i<iteration; ++i) {
            hashCode = Hashing.md5().hashBytes(hashCode).asBytes();
        }
        return BaseEncoding.base16().encode(hashCode);
    }
}
