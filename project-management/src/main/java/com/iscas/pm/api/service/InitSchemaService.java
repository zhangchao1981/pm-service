package com.iscas.pm.api.service;

import javax.sql.DataSource;

public interface InitSchemaService {
    boolean initSchema(String databaseName);

    boolean initSchema(String databaseName, DataSource dataSource);
}
