package lzhou.learning.hash.hashbin.controller;

import com.google.common.io.BaseEncoding;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Description: 数字签名
 * @author: lingy
 * @Date: 2019-06-11 14:40:17
 * @param: null
 * @return:
 */

@RequestMapping("ajax/dsa")
@RestController
public class DigitalSignatureAjaxController {
    private static final String KEY_TYPE = "RSA";

    private static final String SIGN_ALGO = "SHA1withRSA";

    /***
     * @Description: DSA密钥
     *
     * ```sh
     * openssl genrsa -out dsa_private_key.pem 1024
     * openssl pkcs8 -topk8 -inform PEM -in dsa_private_key.pem -nocrypt -out dsa_private_key_pkcs8.der -outform DER
     * openssl rsa -in dsa_private_key.pem -pubout -out dsa_public_key.der -outform DER
     * ```
     *
     * @author: lingy
     * @Date: 2019-06-26 11:45:29
     * @param: null
     * @return:
     */
    @Value("${dsa.privKeyRes}")
    private Resource privKeyRes;

    @Value("${dsa.pubKeyRes}")
    @Setter
    private Resource pubKeyRes;

    private PrivateKey privKey;
    private PublicKey pubKey;

    @PostConstruct
    public void postConstruct() throws Exception {
        privKey = getPrivateKeyFromPKCS8(IOUtils.toByteArray(privKeyRes.getInputStream()));
        pubKey = getPublicKeyFromX509(IOUtils.toByteArray(pubKeyRes.getInputStream()));
    }

    /**
     * 获取私钥PKCS8格式（需DER格式)
     * @param priKey
     * @return PrivateKey
     * @throws Exception
     */
    private static PrivateKey getPrivateKeyFromPKCS8(byte[] priKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_TYPE);

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(priKey));
    }

    /**
     * 通过证书获取公钥（需DER）
     * @param pubKey
     * @return PublicKey
     * @throws Exception
     */
    private static PublicKey getPublicKeyFromX509(byte[] pubKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_TYPE);

        return keyFactory.generatePublic(new X509EncodedKeySpec(pubKey));
    }

    /**
     * 使用私钥对字符进行签名
     * @param data 数据
     * @return String
     * @throws Exception
     */
    @PostMapping(value="sign")
    public String sign(@RequestBody byte[] data) throws Exception {
        Signature signature = Signature.getInstance(SIGN_ALGO);
        signature.initSign(privKey);
        signature.update(data);
        byte[] signed = signature.sign();

        return BaseEncoding.base64Url().encode(signed);
    }

    /**
     * 将内容体、签名信息、及对方公钥进行验签
     * @param data 内容体
     * @param signBase64Url 签名信息
     * @return boolean
     * @throws Exception
     */
    @PostMapping(value="verify/{signBase64Url}")
    public boolean verify(@RequestBody byte[] data, @PathVariable String signBase64Url) throws Exception {
        Signature signature = Signature.getInstance(SIGN_ALGO);
        signature.initVerify(pubKey);
        signature.update(data);

        return signature.verify(BaseEncoding.base64Url().decode(signBase64Url));
    }
}
