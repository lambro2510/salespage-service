package com.salespage.salespageservice.domains.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.salespage.salespageservice.app.dtos.accountDtos.SignUpDto;
import com.salespage.salespageservice.app.dtos.userDtos.UserInfoDto;
import com.salespage.salespageservice.domains.entities.infor.Rate;
import com.salespage.salespageservice.domains.entities.types.CurrencyType;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Calendar;
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
  private Rate rate = new Rate();

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

  public void createUserAdmin(Account account) {
    username = account.getUsername();
    firstName = "ADMIN";
    lastName = "Hệ thống";
    email = "admin@gmail.com";
    imageUrl = "";
    phoneNumber = "+84979163206";
    dateOfBirth = new Date(2001, Calendar.NOVEMBER, 25);
    rate = new Rate();
    balance = new UserBalance();
  }

  public void updateUser(UserInfoDto dto) {
    displayName = dto.getDisplayName();
    imageUrl = dto.getImageUrl();
    phoneNumber = dto.getPhoneNumber();
  }

  public boolean updateBalance(boolean add, long balance) {
    if (add)
      getBalance().money = getBalance().money + balance;
    else
      getBalance().money = getBalance().money - balance;
    return getBalance().money >= 0;
  }

  @Data
  public static class UserBalance {
    @Field("currency_unit")
    private CurrencyType type = CurrencyType.VND;

    @Field("money")
    private long money;

    public void addMoney(long amount){
      this.money = this.money + amount;
    }
    public void minusMoney(long amount){
      money = money - amount;
      if(money < 0)
        money = 0;
    }

  }

}
