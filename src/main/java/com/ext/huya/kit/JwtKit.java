package com.ext.huya.kit;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultHeader;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Map;

public class JwtKit {

    public static final String createH256Token(String payload , String secret){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        String apiKeySecretBytes = DatatypeConverter.parseString(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes.getBytes(), signatureAlgorithm.getJcaName());
        String token = Jwts.builder()
                .setHeader((Map<String, Object>) new DefaultHeader<>().setType(Header.JWT_TYPE))
                .setPayload(payload)
                //.signWith(s, key)
                .signWith(signatureAlgorithm, signingKey)
                .compact();

        return token;
    }
}
