package com.ssafy.myalbba.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .components(new Components().addSecuritySchemes("Bearer Token", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .name("JWT")
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("MyAlbba API")
                        .description("백엔드 API 문서")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("JWT"));
    }
}