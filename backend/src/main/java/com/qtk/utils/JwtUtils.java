/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;


/**
 *
 * @author lossrunner
 */

public class JwtUtils {
    private static final String SECRET;
    private static final long EXPIRATION_MS;
    
    static {
        try {
            Properties props = new Properties();
            InputStream input = JwtUtils.class
                    .getClassLoader()
                    .getResourceAsStream("databases.properties");
            props.load(input);
            SECRET = props.getProperty("jwt.secret");
            EXPIRATION_MS = Long.parseLong(
                    props.getProperty("jwt.expiration")
            );
        } catch (Exception e) {
            throw new RuntimeException("Cannot load properties file", e);
        }
    }
    
    public static String generateToken(String username, String role) throws KeyLengthException, JOSEException{
        JWSSigner signer = new MACSigner(SECRET);
        
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .issueTime(new Date())
                .claim("role", role)
                .build();
        
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );
        
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
    
    public static JWTClaimsSet validateTokenAndGetClaims(String token) throws Exception{
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET);
        
        if(signedJWT.verify(verifier)){
            Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
            if(expiration.after(new Date())){
                return signedJWT.getJWTClaimsSet();
            }
        }
        throw new Exception("Token đã hết hạn hoặc chữ ký không hợp lệ");
    }
    
}
