package com.java.meeting_room;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeetingRoomApplication  {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("spring.application.name", dotenv.get("SPRING_APPLICATION_NAME"));
        System.setProperty("spring.datasource.url", dotenv.get("SPRING_DATASOURCE_URL"));
        System.setProperty("spring.datasource.username", dotenv.get("SPRING_DATASOURCE_USERNAME"));
        System.setProperty("spring.datasource.password", dotenv.get("SPRING_DATASOURCE_PASSWORD"));

        System.setProperty("spring.jpa.database-platform", dotenv.get("SPRING_JPA_DATABASE_PLATFORM"));
        System.setProperty("spring.jpa.hibernate.ddl-auto", dotenv.get("SPRING_JPA_HIBERNATE_DDL_AUTO"));
        System.setProperty("spring.jpa.show-sql", dotenv.get("SPRING_JPA_SHOW_SQL"));

        System.setProperty("spring.flyway.enabled", dotenv.get("SPRING_FLYWAY_ENABLED"));
        System.setProperty("spring.flyway.locations", dotenv.get("SPRING_FLYWAY_LOCATIONS"));
        System.setProperty("spring.flyway.baseline-on-migrate", dotenv.get("SPRING_FLYWAY_BASELINE_ON_MIGRATE"));

        System.setProperty("sk.jwt.key", dotenv.get("SK_JWT_KEY"));

        SpringApplication.run(MeetingRoomApplication.class, args);
    }
}
