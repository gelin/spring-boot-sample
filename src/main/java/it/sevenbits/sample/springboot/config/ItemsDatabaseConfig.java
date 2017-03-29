package it.sevenbits.sample.springboot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configures items database.
 */
@Configuration
public class ItemsDatabaseConfig {

    @Bean
    @Qualifier("itemsDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.items")
    @FlywayDataSource
    public DataSource itemsDataSource() {
        return DataSourceBuilder.create().build();
    }

}
