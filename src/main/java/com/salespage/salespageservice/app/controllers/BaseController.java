package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.app.responses.BaseResponse;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BaseController {
    protected String getUsername(Authentication authentication) {
        if (Objects.isNull(authentication)) return null;
        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUsername();
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
        return ResponseEntity.ok(new BaseResponse(false, message, data));
    }

    protected ResponseEntity<BaseResponse> successApi(String message) {
        return ResponseEntity.ok(new BaseResponse(false, message, null));
    }

    protected ResponseEntity<BaseResponse> successApi(Object data) {
        return ResponseEntity.ok(new BaseResponse(false, null, data));
    }

    protected ResponseEntity<BaseResponse> errorApi(String message) {
        return ResponseEntity.ok(new BaseResponse(true, message, null));
    }

    protected ResponseEntity<BaseResponse> errorApi(String message, Object data) {
        return ResponseEntity.ok(new BaseResponse(true, message, data));
    }

    protected  ResponseEntity<BaseResponse> errorApiStatus500(String message){
        return ResponseEntity.status(500).body(new BaseResponse(true, message, null));
    }
}
