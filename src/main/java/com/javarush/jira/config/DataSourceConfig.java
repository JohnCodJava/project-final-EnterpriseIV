package com.javarush.jira.config;


import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {


    @Bean
    @Profile("!test")
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/jira")
                .username("jira")
                .password("JiraRush")
                .driverClassName("org.postgresql.Driver")
                .build();
    }


    @Bean
    @Profile("test")
    public DataSource h2DataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:h2:mem:testdb;MODE=PostgreSQL")
                .username("jira")
                .password("JiraRush")
                .driverClassName("org.h2.Driver")
                .build();
    }
}