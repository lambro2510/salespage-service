package com.salespage.salespageservice.domains.info;

import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.entities.types.UserState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {
    private String username;
    private UserRole userRole;
    private UserState userState;
}
