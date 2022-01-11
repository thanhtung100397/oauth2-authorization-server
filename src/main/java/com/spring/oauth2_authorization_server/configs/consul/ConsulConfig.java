package com.spring.oauth2_authorization_server.configs.consul;

import com.ecwid.consul.v1.ConsulClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.cloud.consul.enabled", havingValue = "true", matchIfMissing = true)
public class ConsulConfig {
    @Bean
    public ConsulServiceRegistry consulServiceRegistry(ConsulClient consulClient,
                                                       ConsulDiscoveryProperties consulDiscoveryProperties,
                                                       HeartbeatProperties heartbeatProperties) {
        return new ConsulServiceRegistry(consulClient, consulDiscoveryProperties, null, heartbeatProperties);
    }
}
