package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.dtos.userDtos.UserInfoDto;
import com.salespage.salespageservice.domains.entities.types.CurrencyType;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document("user")
@Data
public class User {

  @Id
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId id;

  @Indexed(unique = true)
  private String username;

  @Field("email")
  private String email;

  @Field("phone_number")
  private String phoneNumber;

  @Field("display_name")
  private String displayName;

  @Field("date_of_born")
  private Date dateOfBirth;
  @Field("first_name")
  private String firstName;

  @Field("last_name")
  private String lastName;

  @Field("image_url")
  private String imageUrl;

  @Field("rate")
  private Rate rate;

  @Field("balance")
  private UserBalance balance;

  public void createUser(SignUpDto dto) {
    username = dto.getUsername();
    firstName = dto.getFirstName();
    lastName = dto.getLastName();
    email = dto.getEmail();
    imageUrl = "";
    phoneNumber = dto.getPhoneNumber();
    dateOfBirth = dto.getDateOfBirth();
    rate = new Rate();
    balance = new UserBalance();
  }

  public void updateUser(UserInfoDto dto) {
    displayName = dto.getDisplayName();
    imageUrl = dto.getImageUrl();
    phoneNumber = dto.getPhoneNumber();
  }

  public void processRatePoint(Long point) {
    rate.totalRate += 1;
    rate.totalPoint += point;
    rate.avgPoint = getRate().totalPoint / rate.totalRate;
  }

  @Data
  public static class Rate {

    @Field("total_point")
    private float totalPoint = 0;

    @Field("total_rate")
    private float totalRate = 0;

    @Field("avg_point")
    private float avgPoint = 0;
  }

  @Data
  public static class UserBalance {
    @Field("currency_unit")
    private CurrencyType type = CurrencyType.USD;

    @Field("money")
    private long money;
  }

}
