package com.salespage.salespageservice.domains.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Log4j2
public class RequestUtil {

  @Value("${url.exchange}")
  private String ExchangeUrl;

  /**
   * CURL
   *
   * @param method
   * @param requestUrl
   * @param entity
   * @param headerParam
   * @return
   */
  public static <T> T sportRequest(
          HttpMethod method, String requestUrl, Class<T> tClass, Object entity, Map<String, String> headerParam) {
    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(List.of(MediaType.APPLICATION_JSON));
      if (headerParam != null) {
        for (Map.Entry<String, String> entry : headerParam.entrySet()) {
          headers.add(entry.getKey(), entry.getValue());
        }
      }

      HttpEntity<Object> data = new HttpEntity<>(entity, headers);

      return restTemplate.exchange(requestUrl, method, data, tClass).getBody();
    } catch (Exception e) {
      return null;
    }
  }

  public static Object request(
          HttpMethod method, String requestUrl, Object entity, Map<String, String> headerParam) {
    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(List.of(MediaType.APPLICATION_JSON));
      if (headerParam != null) {
        for (Map.Entry<String, String> entry : headerParam.entrySet()) {
          headers.add(entry.getKey(), entry.getValue());
        }
      }

      HttpEntity<Object> data = new HttpEntity<>(entity, headers);

      return restTemplate.exchange(requestUrl, method, data, Object.class).getBody();
    } catch (HttpClientErrorException e) {
      return e.getResponseBodyAsString();
    } catch (Exception e) {
      return "Error: " + e.getMessage();
    }
  }

  public static String request(String url) {
    RestTemplate restTemplate = new RestTemplate();
    return restTemplate.getForObject(url, String.class);
  }
}
