package it.sevenbits.sample.springboot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Configures items database.
 */
@Configuration
public class ItemsDatabaseConfig {

    @Bean
    @Qualifier("itemsDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.items")
    public DataSource itemsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Qualifier("itemsJdbcTemplate")
    public JdbcTemplate itemsJdbcTemplate(
            @Qualifier("itemsDataSource") DataSource itemsDataSource
    ) {
        return new JdbcTemplate(itemsDataSource);
    }

}
