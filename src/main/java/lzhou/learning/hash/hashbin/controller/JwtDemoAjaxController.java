package lzhou.learning.hash.hashbin.controller;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * @Description: JWT (JSON Web Token)
 *  - 在前后端分离的网页或Restful API中, 取代Session.
 *  - 格式: header.playload.signature, 各部分以base64Url编码后, 以'.'分隔, signature支持以HMAC"签名"
 *  - 好处
 *    - 支持跨域
 *    - 服务层无状态
 *    - 可自定义载荷
 *  - JWT 问题
 *    - 有效期问题(用户登出, JWT任然有效)
 *    - 默认不加密
 *    - 盗用
 * @author: lingy
 * @Date: 2019-06-11 16:04:03
 * @param: null
 * @return: 
 */
@RestController
@RequestMapping("ajax/jwt")
public class JwtDemoAjaxController {
    Key key = null;

    public JwtDemoAjaxController() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("DES");
        keyGen.init(SecureRandom.getInstance("SHA1PRNG"));
        key = keyGen.generateKey();
    }

    @PostMapping(value="jwt-hs256", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String toJwtHs256(@RequestBody Map<String, Object> data) throws NoSuchAlgorithmException {
        return Jwts.builder()
                .setSubject("Login")
                .setClaims(data)
                .setExpiration(Date.from(Instant.now().plus(10, ChronoUnit.SECONDS)))
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    @PostMapping(value="jwt-hs256-validate", consumes = MediaType.TEXT_PLAIN_VALUE)
    public String jwtHs256Validate(@RequestBody String token) throws NoSuchAlgorithmException {
        return Jwts.parser()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(5)
                .parseClaimsJws(token)
                .toString();
    }
}
