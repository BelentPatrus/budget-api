package com.budgetapp.budgetapi.testutil;

import org.testcontainers.containers.PostgreSQLContainer;

public final class SharedPostgresContainer {

    private static final PostgreSQLContainer<?> INSTANCE =
            new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        INSTANCE.start();
    }

    private SharedPostgresContainer() {}

    public static PostgreSQLContainer<?> getInstance() {
        return INSTANCE;
    }
}
