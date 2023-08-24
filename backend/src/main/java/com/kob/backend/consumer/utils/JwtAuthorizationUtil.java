package com.kob.backend.consumer.utils;

import com.kob.backend.utils.jwt.JwtUtil;
import io.jsonwebtoken.Claims;

public class JwtAuthorizationUtil {
    public static Integer getUserId(String token) {
        int userid = -1;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userid;
    }
}
