package com.yx.house.service.user.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang.time.DateUtils;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: yx
 * @date: 2020/02/22/14:53
 */
public class JwtHelper {

    private static final String ISSUER = "mooc_user";
    private static final String SECRET = "session_secret";

    public static String genToken(Map<String,String> claims){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTCreator.Builder builder = JWT.create().withIssuer(ISSUER).withExpiresAt(
                    DateUtils.addDays(new Date(), 1)
            );
            claims.forEach(
                    (k,v) -> builder.withClaim(k,v)
            );
            return builder.sign(algorithm).toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String,String> verifyToken(String token){
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(SECRET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> map = jwt.getClaims();
        Map<String,String> resMap = new HashMap<>();
        map.forEach( (k,v) -> resMap.put(k,v.asString()));
        return resMap;
    }



}
