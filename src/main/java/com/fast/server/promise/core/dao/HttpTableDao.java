package com.fast.server.promise.core.dao;

import com.fast.server.promise.core.domain.HttpTable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HttpTableDao extends MongoRepository<HttpTable, Long> {
}
