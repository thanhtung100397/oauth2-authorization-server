package com.spring.oauth2_authorization_server.configs.datasource;

import com.spring.oauth2_authorization_server.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = ApplicationConstants.BASE_PACKAGE_NAME + ".modules.oauth2_admin.repositories",
        entityManagerFactoryRef = "oauth2AdminEntityManagerFactory",
        transactionManagerRef = "oauth2AdminTransactionManager"
)
public class OAuth2AdminDataSourceConfig {
    @Value("${spring.oauth2-admin-datasource.hibernate.ddl-auto}")
    private String hibernateDDLAuto;

    @Bean(name = "oauth2AdminDataSource")
    @ConfigurationProperties(prefix = "spring.oauth2-admin-datasource")
    public DataSource oauth2AdminDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "oauth2AdminEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean oauth2AdminEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                             @Qualifier("oauth2AdminDataSource") DataSource dataSource) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", hibernateDDLAuto);
        return builder.dataSource(dataSource)
                .packages(ApplicationConstants.BASE_PACKAGE_NAME  + ".modules.oauth2_admin.models.entities")
                .persistenceUnit("oauth2-admin")
                .properties(properties)
                .build();
    }

    @Bean(name = "oauth2AdminTransactionManager")
    public PlatformTransactionManager oauth2AdminTransactionManager(@Qualifier("oauth2AdminEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
