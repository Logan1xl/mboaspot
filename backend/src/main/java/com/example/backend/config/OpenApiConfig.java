package com.example.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;



@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productApiOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8003");
        server.setDescription("Serveur de développement");

        Contact contact = new Contact();
        contact.setEmail("johann.jgouaffo@gmail.com");
        contact.setName("Johann DevOps");
        contact.setUrl("https://www.johanngouaffo.com");

        License license = new License()
                .name("IUC License")
                .url("https://tech.com/licenses/iuc/");

        Info info = new Info()
                .title("API de Gestion de Produits")
                .version("1.0.0")
                .contact(contact)
                .description("API DE GESTION LYCEE .")
                .termsOfService("https://www.iuc.com/EADL")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}