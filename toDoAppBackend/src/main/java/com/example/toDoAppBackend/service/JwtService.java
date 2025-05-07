package com.example.toDoAppBackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class JwtService {

    private  JwtParser jwtParser;
    private static final String PUBLIC_KEY ="-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwcoZdndhOfRVByVlDJIT\n" +
            "oEsMF+GhK+ca8wKc5+lovrBaD5a2bNM77sHpmdW/K1lv3nO8zKyXEDI1l5DrdqsX\n" +
            "D1rS4vtiQzlcr/XrDTOD8WLLkIkIil8WHHHar6BO0E34aWNiH0nGNg2leq8kDhhI\n" +
            "b8MuaYCDs1OjeXMU/vSyBN/15VTE1silFh5DKjFXmdDtTY3oSJavrJsJ6+rh0rgy\n" +
            "Gj28XdRCOv6irXymKeE6ikQhtRa6+66Y8RyrcjQiPaJmDlLVU9jEHmeFcq34WnzO\n" +
            "FJ/CD+mybko05eXw/dmGm57cf8VFmJQXtG/0xmC86+DRdVlTmm/gAYmTy4CcIASJ\n" +
            "/wIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";


    public JwtService(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }
    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }


    public Claims parseToken(String token) {
        try {
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }




    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().getTime() < System.currentTimeMillis();
    }

    public String getEmailFromClaims(String token) {
        return  null;
    }

    public String getPassword(String token) {
        return null;
    }
}
