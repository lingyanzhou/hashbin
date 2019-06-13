package lzhou.learning.hash.hashbin.controller;

import com.sun.crypto.provider.DESKeyFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.function.Function;

/**
 * @Description:
 *
 * [第二篇：对称加密及AES加密算法](https://www.jianshu.com/p/3840b344b27c?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation)
 * @author: lingy
 * @Date: 2019-06-11 17:48:44
 * @param: null
 * @return:
 */
@RestController
@RequestMapping("ajax/symmetric-encryption")
public class SymmetricEncryptionAjaxController {
    private static final String DES_KEY = "my-des-key";

    @PostMapping("DES/ECB/encrypt")
    public ResponseEntity<Resource> desEcbEncrypt(@RequestBody byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, IOException {
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(DES_KEY.getBytes()));
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS7Padding");

        desCipher.init(Cipher.ENCRYPT_MODE, key);
        return toReponse(desCipher.doFinal(data));
    }

    @PostMapping("DES/ECB/decrypt")
    public ResponseEntity<Resource> desEcbDecrypt(@RequestBody byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, IOException {
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(DES_KEY.getBytes()));
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS7Padding");

        desCipher.init(Cipher.DECRYPT_MODE, key);
        return toReponse(desCipher.doFinal(data));
    }

    @PostMapping("DES/CBC/encrypt")
    public ResponseEntity<Resource> desCbcEncrypt(@RequestBody byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, IOException {
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(DES_KEY.getBytes()));
        Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS7Padding");

        desCipher.init(Cipher.ENCRYPT_MODE, key);
        return toReponse(desCipher.doFinal(data));
    }

    @PostMapping("DES/CBC/decrypt")
    public ResponseEntity<Resource> desCbcDecrypt(@RequestBody byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, IOException {
        SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(DES_KEY.getBytes()));
        Cipher desCipher = Cipher.getInstance("DES/CBC/PKCS7Padding");

        desCipher.init(Cipher.DECRYPT_MODE, key);
        return toReponse(desCipher.doFinal(data));
    }

    private static ResponseEntity<Resource> toReponse(byte[] data) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment;filename=decoded.bin")
                .body(resource);
    }
}
