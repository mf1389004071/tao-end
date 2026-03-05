package com.geek.framework.mybatis;

import java.util.Properties;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mybatisflex.spring.boot.SqlSessionFactoryBeanCustomizer;

/**
 * 为 MyBatis 提供 databaseId，使 Mapper XML 中 {@code _databaseId == 'postgresql'} 等分支生效。
 * 用于 PostgreSQL / MySQL 等多库时区分 sysdate() 与 CURRENT_TIMESTAMP 等方言。
 *
 * @see org.apache.ibatis.mapping.VendorDatabaseIdProvider
 */
@Configuration
public class DatabaseIdProviderConfig {

    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider provider = new VendorDatabaseIdProvider();
        Properties props = new Properties();
        // 数据库产品名 -> Mapper 中使用的 databaseId（小写）
        props.setProperty("PostgreSQL", "postgresql");
        props.setProperty("OpenGauss", "openGauss");
        props.setProperty("MySQL", "mysql");
        provider.setProperties(props);
        return provider;
    }

    /** 显式将 DatabaseIdProvider 设置到 SqlSessionFactory，确保 _databaseId 在 Mapper XML 中生效 */
    @Bean
    public SqlSessionFactoryBeanCustomizer databaseIdProviderCustomizer(DatabaseIdProvider databaseIdProvider) {
        return factoryBean -> factoryBean.setDatabaseIdProvider(databaseIdProvider);
    }
}
