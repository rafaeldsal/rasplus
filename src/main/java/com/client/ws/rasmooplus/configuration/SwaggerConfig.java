package com.client.ws.rasmooplus.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Rasmoo Plus API")
            .description("API para atender o client Rasmoo Plus")
            .version("0.0.1")
            .license(new License().name("Rasmoo cursos de tecnologia"))
        );
  }
}
