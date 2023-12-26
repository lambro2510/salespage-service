package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.domains.config.ObjectIdDeserializer;
import com.salespage.salespageservice.domains.config.ObjectIdSerializer;
import com.salespage.salespageservice.domains.entities.types.UserRole;
import com.salespage.salespageservice.domains.entities.types.UserState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
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
  @JsonSerialize(using = ObjectIdSerializer.class)
  @JsonDeserialize(using = ObjectIdDeserializer.class)
  private ObjectId id;

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

  @Field("last_login")
  private String lastLogin;

  public void createAccount(SignUpDto dto) {
    username = dto.getUsername();
    salt = BCrypt.gensalt();
    password = BCrypt.hashpw(dto.getPassword(), salt);
    role = dto.getUserRole();
    state = UserState.NOT_VERIFIED;
  }
}
