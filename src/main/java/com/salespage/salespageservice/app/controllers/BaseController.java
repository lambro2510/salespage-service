package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.security.services.UserDetailsImpl;
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
}
