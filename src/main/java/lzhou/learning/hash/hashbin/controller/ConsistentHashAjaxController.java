package lzhou.learning.hash.hashbin.controller;

import com.google.common.hash.Hashing;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 一致性哈希 (Consistent Hashing)
 *  - 常用于分布式缓存, 规避缓存雪崩
 *  - 保证原有的请求可以被映射到原有的或者新的服务器中去，而不会被映射到原来的其它服务器上去
 *  - 利用哈希环, 保证原有的请求可以被映射到原有的或者新的服务器中去, 而不会被映射到原来的其它服务器上去
 *  - [Link](https://www.jianshu.com/p/e968c081f563)
 * @author: lingy
 * @Date: 2019-06-11 11:35:35
 * @param: null
 * @return:
 */
@RestController
@RequestMapping("ajax/consistenthash")
public class ConsistentHashAjaxController {
    @PostMapping("/bucket/{buckets}")
    public Integer getBucket(@RequestBody byte[] data, @PathVariable Integer buckets) {
        return Hashing.consistentHash(Hashing.md5().hashBytes(data), buckets);
    }
}
