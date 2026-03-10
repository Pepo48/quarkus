package io.quarkus.test.junit;

import io.smallrye.config.SmallRyeConfigBuilder;
import io.smallrye.config.SmallRyeConfigBuilderCustomizer;

public final class FailingBootstrapConfigBuilderCustomizer implements SmallRyeConfigBuilderCustomizer {
    @Override
    public void configBuilder(SmallRyeConfigBuilder builder) {
        throw new IllegalStateException(
                "Discovered customizers must not be loaded during Quarkus test bootstrap config creation");
    }
}