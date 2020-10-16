package com.nickjn92.conference.organizer.configuration;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;

@Configuration
public class DatabaseConfiguration {

  @Bean
  ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);
    /*
     * Initializing this way instead of using proper way with Flyway because flyway
     * doesnt work with R2DBC and it feels unecessary to bring in a lot of dependencies just
     * for flyway.
     */
    initializer.setDatabasePopulator(
        new ResourceDatabasePopulator(
            new ClassPathResource("db/migration/V1_1__Create_table_conference.sql"),
            new ClassPathResource("db/migration/V1_2__Create_table_food.sql"),
            new ClassPathResource("db/migration/V1_3__Create_table_morning_topic.sql"),
            new ClassPathResource("db/migration/V1_4__Create_table_afternoon_topic.sql"),
            new ClassPathResource("db/migration/V1_5__Create_table_registration.sql")));

    return initializer;
  }
}
