package lzhou.learning.hash.hashbin.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.common.io.BaseEncoding;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.function.Function;

/**
 * @Description: 编码
 * - 用途
 *   - 方便二进制数据在文本协议里传输 (例如MIME, URL, HTML)
 * - 常用编码
 *   - Base16 / Hex
 *   - Base32
 *   - Base64
 * - Base16 / Hex 编码
 *   - 以每4比特为刻度编码, 16 == 2^4, 即每1字节编码为2字节
 *   - 码表: "0123456789ABCDEF", 不区分大小写
 *   - 编码效率: 编码后为源文件大小的2倍
 * - Base32 编码
 *   - 以每5比特为刻度编码, 32 == 2^5, 即每5字节编码为8字节
 *   - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", 不区分大小写, 填充字符(可选): "="
 *   - 编码效率: 编码后为源文件大小的8/5
 * - Base64 编码
 *  - 以每6比特为刻度编码, 64 == 2^6, 即每3字节编码为4字节
 *  - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", 填充字符(可选): "="
 *  - 编码效率: 编码后为源文件大小的4/3
 * - Base64Url 编码
 *   - 最常用的Base64变体. 把原Base64里的"+"和"/"分别替换为"-"和"_".
 *   - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", 无填充字符
 * @author: lingy
 * @Date: 2019-06-10 09:44:06
 * @param: null
 * @return:
 */
@RestController
@RequestMapping("ajax/codec")
public class CodecAjaxController {
    /**
     * @Description: Base16 / Hex 编码
     *  - 以每4比特为刻度编码, 2^4 == 16
     *  - 码表: "0123456789ABCDEF" (不区分大小写)
     *  - 编码效率: 编码后为源文件大小的2倍
     * @author: lingy
     * @Date: 2019-06-10 09:43:30
     * @param: data
     * @return: org.springframework.http.ResponseEntity<org.springframework.core.io.Resource>
     */
    @PostMapping(value={"toBase16", "toHex"}, consumes= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String toBase16(@RequestBody byte[] data) throws IOException {
        return BaseEncoding.base16().encode(data);
    }

    @PostMapping(value={"fromBase16", "fromHex"}, consumes= MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Resource> fromBase16(@RequestBody String data) throws IOException {
        return toResponse(BaseEncoding.base16().decode(data));
    }
    /**
     * @Description: Base32 编码
     *  - 以每5比特为刻度编码, 2^5 == 32
     *  - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
     *  - 编码效率: 编码后为源文件大小的8/5
     * @author: lingy
     * @Date: 2019-06-10 09:43:30
     * @param: data
     * @return: org.springframework.http.ResponseEntity<org.springframework.core.io.Resource>
     */
    @PostMapping(value="toBase32", consumes= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String toBase32(@RequestBody byte[] data) throws IOException {
        return BaseEncoding.base32().encode(data);
    }

    @PostMapping(value="fromBase32", consumes= MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Resource> fromBase32(@RequestBody String data) throws IOException {
        return toResponse(BaseEncoding.base32().decode(data));
    }
    /**
     * @Description: Base64 编码
     *  - 以每6比特为刻度编码, 2^6 == 64
     *  - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
     *  - 编码效率: 编码后为源文件大小的4/3
     * @author: lingy
     * @Date: 2019-06-10 09:43:30
     * @param: data
     * @return: org.springframework.http.ResponseEntity<org.springframework.core.io.Resource>
     */
    @PostMapping(value="toBase64", consumes= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String toBase64(@RequestBody byte[] data) throws IOException {
        return BaseEncoding.base64().encode(data);
    }

    @PostMapping(value="fromBase64", consumes= MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Resource> fromBase64(@RequestBody String data) throws IOException {
        return toResponse(BaseEncoding.base64().decode(data));
    }
    /**
     * @Description: Base64Url 编码
     * - 最常用的Base64变体. 把原Base64里的"+"和"/"分别替换为"-"和"_"
     * - 码表: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
     * @author: lingy
     * @Date: 2019-06-10 09:43:30
     * @param: data
     * @return: org.springframework.http.ResponseEntity<org.springframework.core.io.Resource>
     */
    @PostMapping(value="toBase64Url", consumes= MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public String toBase64Url(@RequestBody byte[] data) throws IOException {
        return BaseEncoding.base64Url().encode(data);
    }
    @PostMapping(value="fromBase64Url", consumes= MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Resource> fromBase64Url(@RequestBody String data) throws IOException {
        return toResponse(BaseEncoding.base64Url().decode(data));
    }

    @PostMapping(value="convert", consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Resource> convert(@RequestParam String data, @RequestParam String from, @RequestParam String to) throws IOException {
        byte[] raw = null;
        switch (from.toLowerCase()) {
            case "hex" :
            case "base16" :
                raw = BaseEncoding.base16().decode(data);
                break;
            case "base32" :
                raw = BaseEncoding.base32().decode(data);
                break;
            case "base64" :
                raw = BaseEncoding.base64().decode(data);
                break;
        }
        String converted = null;
        switch (to.toLowerCase()) {
            case "hex" :
            case "base16" :
                converted = BaseEncoding.base16().encode(raw);
                break;
            case "base32" :
                converted = BaseEncoding.base32().encode(raw);
                break;
            case "base64" :
                converted = BaseEncoding.base64().encode(raw);
                break;
        }
        ByteArrayResource resource = new ByteArrayResource(converted.getBytes());
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    private static ResponseEntity<Resource> toResponse(byte[] data) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment;filename=decoded.bin")
                .body(resource);
    }
}
