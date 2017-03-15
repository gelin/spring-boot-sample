package it.sevenbits.sample.springboot.config;

import it.sevenbits.sample.springboot.core.repository.ItemsRepository;
import it.sevenbits.sample.springboot.core.repository.SampleItemsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures repositories
 */
@Configuration
public class RepositoryConfig {

    @Bean
    public ItemsRepository itemsRepository() {
        return new SampleItemsRepository();
    }

}
