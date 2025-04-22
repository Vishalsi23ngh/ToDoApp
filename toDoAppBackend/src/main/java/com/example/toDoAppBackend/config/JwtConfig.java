package com.example.toDoAppBackend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Configuration
public class JwtConfig {

    private static final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAevcljcDWWTyHHwjRvZRL\n" +
            "Se4tL+DcN3Tjz/cwjRS3Y3353PBBqQtWfFQeDdOniuxXqYVvQ4ZiSMd/wo5yDVwR\n" +
            "LFw+XNlSePXeYLHSh87r7UELaZoGMJNxXiwVyY6LyVxkPo99o79AOxXQXebCzPV/\n" +
            "S6cBF2DE5904+XSQqhLEHAP25UhwwJDwaDFcWs5o+BQTT6sDCLJVWk/EbgqOR+\n" +
            "nYXVXBShR1xZzZA9mDWztL9iRaoVENKSP5L0tx6oe98MRp8bMH+SVQPBHx4kicn/\n" +
            "qn1dBjpVnNLaHo6t9gDAsHrm0+ThfDFYCQ8UIvGKFoRPo8+FHz96XEzbgSWAz+Vc\n" +
            "MQIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    @Bean
    public JwtParser jwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getRSAPublicKey())
                .build();
    }


    private RSAPublicKey getRSAPublicKey() {
        try {
            String publicKeyPEM = PUBLIC_KEY
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");


            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load RSA public key", e);
        }
    }






    public Claims parseToken(String token) {
        try {
            return jwtParser().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }


    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }

    public String getUserEmail(String token) {
        return  null;
    }

    public String getUserPassword(String token) {
        return  null;
    }
}
