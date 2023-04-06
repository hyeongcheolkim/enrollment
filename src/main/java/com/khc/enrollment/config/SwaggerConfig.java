package com.khc.enrollment.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI api() {
        Info info = new Info()
                .title("MrDaeBak Dinner Service")
                .description("Software Engineering Project by Team.NaNSsoGong")
                .version("V1.0")
                .contact(new Contact()
                        .name("Front WEB")
                        .url("https://mr-daebak.netlify.app/"))
                .license(new License()
                        .name("Apache License Version 2.0")
                        .url("http://www.apache.org/license/LICENSE-2.0"));

        SecurityScheme auth = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("JSESSIONID");

        Server local = new Server();
        local.setDescription("local");
        local.setUrl("http://localhost:9090");

        Server prod = new Server();
        prod.setDescription("prod");
        prod.setUrl("https://mrdaebakservice.kro.kr");

        return new OpenAPI()
                .servers(List.of(local, prod))
                .components(new Components().addSecuritySchemes("basicAuth", auth))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .info(info);
    }
}
