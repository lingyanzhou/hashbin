package lzhou.learning.hash.hashbin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Signature;

/**
 * @Description: 数字签名 (Digital Signature)
 * - 定义
 *   - 一个
 *   -
 *   -
 *
 * @author: lingy
 * @Date: 2019-06-11 14:40:17
 * @param: null
 * @return:
 */

@RequestMapping("ajax/digital-signature")
@RestController
public class DigitalSignatureAjaxController {
}

// AES/DEA   RSA  RSA2  DSA  .pem .pk8  jks  openssl
// 加解密(AES,RSA)和签名(SHA1WithRSA,SHA256WithRSA)
