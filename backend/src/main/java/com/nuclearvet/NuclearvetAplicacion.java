package com.nuclearvet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Clase principal de la aplicación NuclearVET
 * Sistema de Gestión Veterinaria - Colombia
 * 
 * @author NuclearVET Team
 * @version 1.0.0
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.nuclearvet")
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class NuclearvetAplicacion {

    public static void main(String[] args) {
        SpringApplication.run(NuclearvetAplicacion.class, args);
        System.out.println("\n" +
                "███╗   ██╗██╗   ██╗ ██████╗██╗     ███████╗ █████╗ ██████╗ ██╗   ██╗███████╗████████╗\n" +
                "████╗  ██║██║   ██║██╔════╝██║     ██╔════╝██╔══██╗██╔══██╗██║   ██║██╔════╝╚══██╔══╝\n" +
                "██╔██╗ ██║██║   ██║██║     ██║     █████╗  ███████║██████╔╝██║   ██║█████╗     ██║   \n" +
                "██║╚██╗██║██║   ██║██║     ██║     ██╔══╝  ██╔══██║██╔══██╗╚██╗ ██╔╝██╔══╝     ██║   \n" +
                "██║ ╚████║╚██████╔╝╚██████╗███████╗███████╗██║  ██║██║  ██║ ╚████╔╝ ███████╗   ██║   \n" +
                "╚═╝  ╚═══╝ ╚═════╝  ╚═════╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝  ╚═══╝  ╚══════╝   ╚═╝   \n" +
                "\n🏥 Sistema de Gestión Veterinaria - Colombia 🇨🇴\n" +
                "✅ Aplicación iniciada correctamente\n" +
                "📍 Swagger UI: http://localhost:8080/api/swagger-ui.html\n" +
                "📍 API Docs: http://localhost:8080/api/api-docs\n");
    }
}
