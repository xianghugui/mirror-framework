package com.base.web.mybatis;

import com.base.web.mybatis.utils.ResultMapsUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan(basePackages = {"com.base.web.mybatis"})
@MapperScan(basePackages = {"com.base.web.dao"})
@AutoConfigureAfter(MyBatisAutoConfiguration.class)
@EnableConfigurationProperties(MybatisProperties.class)
public class MybatisDaoAutoConfiguration {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @PostConstruct
    public void init() {
        ResultMapsUtils.setSqlSession(sqlSessionTemplate);
    }
}
