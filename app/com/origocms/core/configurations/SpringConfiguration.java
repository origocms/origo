package com.origocms.core.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"controllers", "services"})
public class SpringConfiguration {

    /**
     * You can use bean methods to create your beans
     * as here for the hello world service.
     */
    /*
    @Bean
    public HelloWorldService helloWorldService() {
        return new HelloWorldService();
    }
    */
}