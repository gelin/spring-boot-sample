package it.sevenbits.sample.springboot.config;

import it.sevenbits.sample.springboot.core.repository.ItemsRepository;
import it.sevenbits.sample.springboot.core.repository.JdbcTemplateItemsRepository;
import it.sevenbits.sample.springboot.core.repository.SampleUsersRepository;
import it.sevenbits.sample.springboot.core.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Configures repositories
 */
@Configuration
public class RepositoryConfig {

    @Bean
    public ItemsRepository itemsRepository(
            @Qualifier("itemsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateItemsRepository(jdbcTemplate);
    }

//    @Bean
//    public UsersRepository usersRepository(
//            @Qualifier("itemsJdbcTemplate") JdbcTemplate jdbcTemplate) {
//        return new DatabaseUsersRepository(jdbcTemplate);
//    }

    @Bean
    public UsersRepository usersRepository() {
        return new SampleUsersRepository();
    }

}
