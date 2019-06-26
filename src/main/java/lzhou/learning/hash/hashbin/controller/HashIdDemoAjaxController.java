package lzhou.learning.hash.hashbin.controller;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 混淆Id Demo
 * - 不表现出递增的特征, 防止爬取
 * - 例:
 *   - 对id加盐后, 求散列值
 *   - 对内容求散列值
 * @author: lingy
 * @Date: 2019-06-18 09:44:50
 * @param: null
 * @return: 
 */
@RequestMapping("ajax/hashid")
@RestController
public class HashIdDemoAjaxController {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class HashIdDemoItem {
        Integer id;
        String content;
        String hash;
    }
    @Value("${hashid.salt}")
    private String salt;
    private List<HashIdDemoItem> list = new ArrayList<>();
    public HashIdDemoAjaxController() {
        for (int i=0; i<10; ++i) {
            String content = "Item " + i;
            list.add(HashIdDemoItem.builder()
                    .id(i)
                    .content(content)
                    .hash(BaseEncoding.base16().encode(Hashing.md5().hashString(content, Charsets.UTF_8).asBytes()))
                    .build()
            );
        }
    }

    @GetMapping()
    public List<HashIdDemoItem> list() {
        return list;
    }

    @GetMapping("{id}")
    public HashIdDemoItem getById(@PathVariable Integer id, @RequestParam String hash) {
        HashIdDemoItem item = list.get(id);
        if (item.getHash().equals(hash)) {
            return item;
        }
        throw new RuntimeException("Not Found");
    }
}
