package com.salespage.salespageservice.domains.entities;

import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.entities.types.UserState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Document("account")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {

    @Id
    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("salt")
    private String salt;

    @Field("role")
    private UserRole role;

    @Field("user_state")
    private UserState state;

    public void createAccount(SignUpDto dto) {
        username = dto.getUsername();
        salt = BCrypt.gensalt();
        password = BCrypt.hashpw(dto.getPassword(), salt);
        role = UserRole.USER;
        state = UserState.NOT_VERIFIED;
    }
}
