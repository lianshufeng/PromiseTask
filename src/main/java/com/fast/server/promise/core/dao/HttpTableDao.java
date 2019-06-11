package com.fast.server.promise.core.dao;

import com.fast.server.promise.core.domain.HttpTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HttpTableDao extends JpaRepository<HttpTable, Long> {
}
