package com.salespage.salespageservice.domains.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@OpenAPIDefinition(
//    info =@Info(
//        title = "Salepage api",
//        version = "v1",
//        contact = @Contact(
//            name = "lambro2510", email = "lambro2510@gmail.com", url = "http://lam-banhang.click"
//        ),
//        license = @License(
//            name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
//        ),
//        description = "api mô tả"
//    )
//)
public class OpenAPISecurityConfiguration {
  @Bean
  public OpenAPI customizeOpenAPI() {
    final String securitySchemeName = "bearerAuth";
    return new OpenAPI()
        .addSecurityItem(new SecurityRequirement()
            .addList(securitySchemeName))
        .components(new Components()
            .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")));
  }
}