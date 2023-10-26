package com.salespage.salespageservice.domains.security.jwt;


import com.salespage.salespageservice.app.dtos.accountDtos.CheckInDto;
import com.salespage.salespageservice.domains.security.services.UserDetailsServiceImpl;
import com.salespage.salespageservice.domains.utils.Helper;
import com.salespage.salespageservice.domains.utils.JwtUtils;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

@Component
@Log4j2
public class AuthTokenFilter extends OncePerRequestFilter {

  @Autowired
  protected JwtUtils jwtUtils;
  @Value("${casso.token}")
  String cassoToken;
  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    Instant start = Instant.now();
    try {

      String jwt = getJwtFromRequest(request);
      String bankToken = getCassoTokenFromRequest(request);
      String lat = getLatFromRequest(request);
      String lng = getLngFromRequest(request);
      if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {

        String username = jwtUtils.getUsernameFromJWT(jwt);
        log.debug("=====username: " + username);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails != null) {
          userDetailsService.checkIn(new CheckInDto(lat, lng, username));
          UsernamePasswordAuthenticationToken
              authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } else if (StringUtils.hasText(bankToken) && cassoToken.equals(bankToken)) {
        UserDetails userDetails = new User("casso", "lam12345", new ArrayList<>());
        UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception ex) {
      log.error("failed on set user authentication", ex);
    }

    filterChain.doFilter(request, response);
    long duration = Duration.between(start, Instant.now()).toMillis();
    MDC.put("duration", duration + "");
    if (duration > 200) {
      log.warn("end request ==> {}  {}", request.getMethod(), Helper.getPath(request));
    } else {
      log.debug("end request ==> {}  {}", request.getMethod(), Helper.getPath(request));
    }

    MDC.remove("duration");
  }

  private String getLngFromRequest(HttpServletRequest request) {
    String lat = request.getHeader("lat");
    if (StringUtils.hasText(lat)) return lat;
    return null;
  }

  private String getLatFromRequest(HttpServletRequest request) {
    String lng = request.getHeader("lng");
    if (StringUtils.hasText(lng)) return lng;
    return null;
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private String getCassoTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader("secure-token");
    if (StringUtils.hasText(token)) return token;
    return null;
  }
}
