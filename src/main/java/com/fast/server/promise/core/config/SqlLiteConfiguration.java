package com.fast.server.promise.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//dao

@Configuration
@Import(JpaDataConfiguration.class)
@EnableJpaRepositories("com.fast.server.promise.core.dao")
@EntityScan("com.fast.server.promise.core.domain")
public class SqlLiteConfiguration {


}
