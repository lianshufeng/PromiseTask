package com.fast.server.promise.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
// 重要
@EnableMongoRepositories(basePackages = {"com.fast.server.promise.core.dao"})
@Import(MongoConfiguration.class)
public class MongoJpaConfig {
}
