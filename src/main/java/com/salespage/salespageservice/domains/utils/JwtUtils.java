package com.salespage.salespageservice.domains.utils;


import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.entities.types.UserState;
import com.salespage.salespageservice.domains.info.TokenInfo;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Log4j2
public class JwtUtils {
    @Autowired
    @Lazy
    protected RemoteCacheManager remoteCacheManager;
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.token-expire-time}")
    private long jwtExpirationMs;

    // Tạo ra jwt từ thông tin user
    public String generateToken(TokenInfo tokenInfo) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(tokenInfo.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("role", tokenInfo.getUserRole())
                .claim("state", tokenInfo.getUserState())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // Lấy thông tin user từ jwt
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Lấy thông tin user từ jwt
    public UserRole getRoleFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", UserRole.class);
    }

    // Lấy thông tin user từ jwt
    public UserState getStateFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("state", UserState.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
