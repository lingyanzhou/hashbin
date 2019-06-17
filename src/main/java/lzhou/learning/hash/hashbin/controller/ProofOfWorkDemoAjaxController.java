package lzhou.learning.hash.hashbin.controller;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 工作量证明 Demo
 *   - 比特币区块链通过竞争记账方式解决去中心化的账本一致性问题
 *   - 通过工作量证明算法使得区块链历史数据实际上不可更改
 *   - 工作量证明方程: 满足难度目标(前置零的个数)的散列值
 * [区块链共识技术一:pow共识机制](https://www.jianshu.com/p/1026fb3c566f)
 * @author: lingy
 * @Date: 2019-06-17 10:59:31
 * @param: null
 * @return:
 */
@RestController
@RequestMapping("ajax/pow")
public class ProofOfWorkDemoAjaxController {
    @Value("${pow.leadingzeros}")
    private int leadingZeros;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PowResult {
        private String content;
        private String pow;
        private Integer nounce;
        private Long timeInMillis;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public PowResult pow(@RequestBody byte[] content) {
        ByteArrayBuilder bab = new ByteArrayBuilder();
        long start = System.currentTimeMillis();
        for (int nounce = 0; nounce <= Integer.MAX_VALUE; ++nounce) {
            bab.reset();
            bab.write(content);
            bab.appendFourBytes(nounce);
            byte[] hashCode = Hashing.sha256().hashBytes(bab.toByteArray()).asBytes();
            String hashCodeHex = BaseEncoding.base16().encode(hashCode);
            boolean allZeros = true;
            for (int j=0; j<leadingZeros && j<hashCodeHex.length(); ++j) {
                if (hashCodeHex.charAt(j) != '0') {
                    allZeros = false;
                    break;
                }
            }
            if (allZeros) {
                long end = System.currentTimeMillis();
                return PowResult.builder()
                        .content(BaseEncoding.base16().encode(content))
                        .pow(hashCodeHex)
                        .nounce(nounce)
                        .timeInMillis(end-start)
                        .build();
            }
        }
        throw new RuntimeException("Cannot find a valid POW.");
    }
}

