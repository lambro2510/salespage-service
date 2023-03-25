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
//        title = "User API",
//        version = "${api.version}",
//        contact = @Contact(
//            name = "Baeldung", email = "user-apis@baeldung.com", url = "https://www.baeldung.com"
//        ),
//        license = @License(
//            name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
//        ),
//        termsOfService = "${tos.uri}",
//        description = "${api.description}"
//    ),
//    servers = @Server(
//        url = "${api.server.url}",
//        description = "Production"
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