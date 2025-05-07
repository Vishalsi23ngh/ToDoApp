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
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwcoZdndhOfRVByVlDJIT\n" +
            "oEsMF+GhK+ca8wKc5+lovrBaD5a2bNM77sHpmdW/K1lv3nO8zKyXEDI1l5DrdqsX\n" +
            "D1rS4vtiQzlcr/XrDTOD8WLLkIkIil8WHHHar6BO0E34aWNiH0nGNg2leq8kDhhI\n" +
            "b8MuaYCDs1OjeXMU/vSyBN/15VTE1silFh5DKjFXmdDtTY3oSJavrJsJ6+rh0rgy\n" +
            "Gj28XdRCOv6irXymKeE6ikQhtRa6+66Y8RyrcjQiPaJmDlLVU9jEHmeFcq34WnzO\n" +
            "FJ/CD+mybko05eXw/dmGm57cf8VFmJQXtG/0xmC86+DRdVlTmm/gAYmTy4CcIASJ\n" +
            "/wIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";

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
                    .replaceAll("\\s+", "");

            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
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
        return null;
    }

    public String getUserPassword(String token) {
        return null;
    }
}
