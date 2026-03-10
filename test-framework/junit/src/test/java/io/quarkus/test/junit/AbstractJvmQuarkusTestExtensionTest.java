package io.quarkus.test.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

import org.junit.jupiter.api.Test;

import io.quarkus.deployment.dev.testing.TestConfig;
import io.smallrye.config.SmallRyeConfig;

class AbstractJvmQuarkusTestExtensionTest {

    @Test
    void bootstrapConfigIgnoresDiscoveredCustomizers() throws IOException {
        System.setProperty("quarkus.test.hang-detection-timeout", "13s");
        Path serviceRoot = Files.createTempDirectory("bootstrap-config-customizer");
        Path serviceFile = serviceRoot.resolve("META-INF/services/io.smallrye.config.SmallRyeConfigBuilderCustomizer");
        Files.createDirectories(serviceFile.getParent());
        Files.writeString(serviceFile, FailingBootstrapConfigBuilderCustomizer.class.getName());

        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { serviceRoot.toUri().toURL() },
                AbstractJvmQuarkusTestExtensionTest.class.getClassLoader())) {
            SmallRyeConfig config = AbstractJvmQuarkusTestExtension
                    .buildBootstrapTestConfig(classLoader);

            assertEquals(Duration.ofSeconds(13), config.getConfigMapping(TestConfig.class).hangDetectionTimeout());
        } finally {
            System.clearProperty("quarkus.test.hang-detection-timeout");
        }
    }
}