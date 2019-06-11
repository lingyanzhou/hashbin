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
