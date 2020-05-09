package com.github.rubenqba.inegi;

import com.github.rubenqba.inegi.service.InegiService;
import com.github.rubenqba.inegi.service.impl.InegiServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name="inegi.enabled", havingValue="true")
public class InegiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    InegiService createService() {
        return new InegiServiceImpl();
    }
}
