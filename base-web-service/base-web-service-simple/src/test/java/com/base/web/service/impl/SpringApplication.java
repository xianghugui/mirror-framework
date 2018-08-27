package com.base.web.service.impl;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by   on 16-4-20.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.base.web"})
@EnableTransactionManagement(proxyTargetClass = true)
//@MapperScan("com.base.web.dao")
public class SpringApplication {

}
