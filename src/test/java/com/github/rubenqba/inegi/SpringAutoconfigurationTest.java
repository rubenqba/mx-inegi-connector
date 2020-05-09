package com.github.rubenqba.inegi;

import com.github.rubenqba.inegi.service.InegiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class SpringAutoconfigurationTest {

//    @Rule
//    public ExpectedException exceptions = ExpectedException.none();

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(InegiAutoConfiguration.class));

    @Test
    public void contextLoads() {
        contextRunner
                .withPropertyValues(
                        "inegi.enabled=true"
                )
                .run(ctx -> {
                    assertThat(ctx).hasSingleBean(InegiService.class);
                });
        contextRunner
                .withPropertyValues(
                        "inegi.enabled=false"
                )
                .run(ctx -> assertThat(ctx).doesNotHaveBean(InegiService.class));
        contextRunner
                .run(ctx -> assertThat(ctx).doesNotHaveBean(InegiService.class));
    }

    @Test
    public void contextMissingConfiguration() {
        contextRunner.run(ctx -> assertThat(ctx).doesNotHaveBean(InegiAutoConfiguration.class).doesNotHaveBean(InegiService.class));
    }
}
