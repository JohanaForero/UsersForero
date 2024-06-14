package com.forero;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {

    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0.10");

    @BeforeAll
    public static void startMongoDBContainer() {
        mongoDBContainer.start();
    }

    @AfterAll
    public static void stopMongoDBContainer() {
        if (mongoDBContainer.isRunning()) {
            mongoDBContainer.close();
        }
    }
}
