package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.exceptions.UnauthorizedException;
import com.salespage.salespageservice.domains.security.services.UserDetailsImpl;
import com.salespage.salespageservice.domains.utils.JwtUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
public class BaseController {
  @Autowired
  JwtUtils jwtUtils;

  protected String getUsername(Authentication authentication) {
    if (Objects.isNull(authentication)) return null;
    UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
    return userDetails.getUsername();
  }

  protected String getUserInfoFromToken(String token) {
    if (jwtUtils.validateToken(token)) {
      return jwtUtils.getUsernameFromJWT(token);
    } else {
      throw new UnauthorizedException();
    }
  }

  protected List<UserRole> getUserRoles(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return new ArrayList<>();
    }
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .map(UserRole::valueOf)
        .collect(Collectors.toList());
  }

  protected ResponseEntity<BaseResponse> successApi(String message, Object data) {
    return ResponseEntity.ok(new BaseResponse(0, false, message, data));
  }

  protected ResponseEntity<BaseResponse> successApi(String message) {
    return ResponseEntity.ok(new BaseResponse(0, false, message, null));
  }

  protected ResponseEntity<BaseResponse> successApi(Object data) {
    return ResponseEntity.ok(new BaseResponse(0, false, null, data));
  }

  protected ResponseEntity<BaseResponse> errorApi(String message) {
    return ResponseEntity.ok(new BaseResponse(0, true, message, null));
  }

  protected ResponseEntity<BaseResponse> errorApi(Exception ex) {
    log.error(ex);
    return ResponseEntity.ok(new BaseResponse(0, true, ex.getMessage(), null));
  }

  protected ResponseEntity<BaseResponse> errorApi(String message, Object data) {
    return ResponseEntity.ok(new BaseResponse(0, true, message, data));
  }

  protected ResponseEntity<BaseResponse> errorApi(Integer code, String message) {
    return ResponseEntity.ok(new BaseResponse(code, true, message, null));
  }

  protected ResponseEntity<BaseResponse> errorApi(Integer code) {
    return ResponseEntity.ok(new BaseResponse(code, true, null, null));
  }

  protected ResponseEntity<BaseResponse> errorApiStatus500(String message) {
    return ResponseEntity.status(500).body(new BaseResponse(0, true, message, null));
  }
}
