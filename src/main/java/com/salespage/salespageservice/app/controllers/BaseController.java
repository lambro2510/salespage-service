package com.salespage.salespageservice.app.controllers;

import com.salespage.salespageservice.domains.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class BaseController {
    protected String getUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
