package com.forero.infrastructure;

import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
import de.flapdoodle.reverse.TransitionWalker;
import de.flapdoodle.reverse.transitions.Start;
import org.springframework.context.annotation.Configuration;

@Configuration
class EmbeddedMongoConfig {
    private static final TransitionWalker.ReachedState<RunningMongodProcess> running;

    static {
        final Mongod serverMongoDB = Mongod.builder()
                .net(Start.to(Net.class).initializedWith(Net.defaults()
                        .withPort(27017)))
                .build();
        running = serverMongoDB.start(Version.Main.V8_0_RC);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            running.current().stop();
        }));
    }
}
