package com.geek.flowable.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.db.DbIdGenerator;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {
    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        engineConfiguration.setActivityFontName("宋体");
        engineConfiguration.setLabelFontName("宋体");
        engineConfiguration.setAnnotationFontName("宋体");
        engineConfiguration.setIdGenerator(new DbIdGenerator());
    }

    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(
            SqlSessionFactory sqlSessionFactory,
            PlatformTransactionManager annotationDrivenTransactionManager) {

        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();

        // 指定 MyBatis-Flex 数据源
        processEngineConfiguration.setDataSource(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource());

        // 配置 MyBatis-Flex 的事务管理器
        processEngineConfiguration.setTransactionManager(annotationDrivenTransactionManager);
        return processEngineConfiguration;
    }
}
