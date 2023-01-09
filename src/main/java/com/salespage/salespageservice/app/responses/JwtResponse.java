package com.salespage.salespageservice.app.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String username;
    private String token;
}
