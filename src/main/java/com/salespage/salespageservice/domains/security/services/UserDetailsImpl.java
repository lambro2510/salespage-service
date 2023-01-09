package com.salespage.salespageservice.domains.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salespage.salespageservice.domains.entities.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

  private static final long serialVersionUID = 1L;

  private final String username;
  @JsonIgnore
  private final String password;

  private final GrantedAuthority authority;

  public UserDetailsImpl(
          String username,
          String password,
          GrantedAuthority authority) {
    this.username = username;
    this.password = password;
    this.authority = authority;
  }

  public static UserDetailsImpl build(Account account) {
    return new UserDetailsImpl(account.getUsername(), account.getPassword(), new SimpleGrantedAuthority(account.getRole().name()));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(authority);
    return grantedAuthorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public boolean userHasAuthority(String userRole) {
    return authority.getAuthority().equals(userRole);
  }

  public GrantedAuthority getAuthority() {
    return authority;
  }

  public String getRole() {
    return authority.getAuthority();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
}


