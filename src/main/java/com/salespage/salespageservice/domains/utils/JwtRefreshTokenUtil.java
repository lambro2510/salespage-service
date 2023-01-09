package com.salespage.salespageservice.domains.utils;


import com.salespage.salespageservice.domains.info.TokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtRefreshTokenUtil {

  @Autowired
  @Lazy
  protected RemoteCacheManager remoteCacheManager;
  @Value("${jwt.token-refresh-expire-time}")
  private int tokenRefreshExpireTime;

  public String generateToken(TokenInfo tokenInfo) {
    UUID uuid = UUID.randomUUID();
    String hashToken = Helper.md5Token(tokenInfo.getUsername() + uuid);
    String sessionKey = CacheKey.genSessionKey(hashToken);

//    cacheManager.set(sessionKey, JsonParser.toJson(tokenInfo), tokenRefreshExpireTime);

    return hashToken;
  }

  public TokenInfo validate(String refreshToken) {
    String sessionKey = CacheKey.genSessionKey(refreshToken);
    try {
//      return cacheManager.get(sessionKey, TokenInfo.class);
    } catch (Exception e) {
      return null;
    }
    return null;
  }

  public void delete(String refreshToken) {
    String sessionKey = CacheKey.genSessionKey(refreshToken);
    try {
//      cacheManager.del(sessionKey);
    } catch (Exception e) {

    }
  }
}
