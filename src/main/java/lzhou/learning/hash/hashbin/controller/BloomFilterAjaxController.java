package lzhou.learning.hash.hashbin.controller;


import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @Description: 布伦过滤器 (Bloom Filter)
 * - 常用于大数据集合, 数据库
 * - 计算一个元素的k个散列值, 查看BitSet里这k个位的值, 如果BitSet包含这k个值, 元素可能在集合中, 否则元素一定不在集合中
 * - 牺牲正确性, 以获得常数的时间和空间复杂度
 * [Link](https://www.jianshu.com/p/bed57bfad826)
 * @author: lingy
 * @Date: 2019-06-11 09:46:10
 * @param: null
 * @return:
 */
@RestController
@RequestMapping("ajax/bloomfilter")
public class BloomFilterAjaxController {
    private static int insertions = 1024*1024;
    private BloomFilter<String> filterHighFpp = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), insertions, 0.5);
    private BloomFilter<String> filterLowFpp = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), insertions, 0.01);
    private char[] zeros = new char[1024];
    public BloomFilterAjaxController() {
        Arrays.fill(zeros, '0');
        StringBuilder sb = new StringBuilder(1040);
        sb.append(zeros);
        for (int i=0; i<insertions; ++i) {
            filterHighFpp.put(sb.append(i).toString());
            sb.setLength(1024);
        }
        for (int i=0; i<insertions; ++i) {
            filterLowFpp.put(sb.append(i).toString());
            sb.setLength(1024);
        }
    }

    @GetMapping("high-fpp/{val}")
    public Boolean mightContainHighFpp(@PathVariable Integer val) {
        StringBuilder sb = new StringBuilder(1040);
        sb.append(zeros).append(val);
        return filterHighFpp.mightContain(sb.toString());
    }

    @GetMapping("low-fpp/{val}")
    public Boolean mightContainLowFpp(@PathVariable Integer val) {
        StringBuilder sb = new StringBuilder(1040);
        sb.append(zeros).append(val);
        return filterLowFpp.mightContain(sb.toString());
    }
}
