package ru.denisovmaksim.voting.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import static ru.denisovmaksim.voting.config.SpringConfigForIT.TEST_PROFILE;

@Configuration
@Profile(TEST_PROFILE)
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "ru.denisovmaksim")
@PropertySource(value = "classpath:/config/application.yml")
public class SpringConfigForIT {
    public static final String TEST_PROFILE = "test";
}
