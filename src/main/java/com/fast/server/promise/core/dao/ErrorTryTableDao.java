package com.fast.server.promise.core.dao;

import com.fast.server.promise.core.dao.extend.ErrorTryTableDaoExtend;
import com.fast.server.promise.core.domain.ErrorTryTable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ErrorTryTableDao extends MongoRepository<ErrorTryTable, Long>, ErrorTryTableDaoExtend {


}
