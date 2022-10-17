package com.iscas.pm.common.core.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iscas.pm.common.core.util.RedisUtil;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@ConditionalOnClass(RedisOperations.class)
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig {

    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(factory);

        // jackson序列化方式
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // String序列化方式
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();

        return template;
    }

    @Bean
    public RedisUtil redisUtil(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil redisUtil = new RedisUtil(redisTemplate);
        return redisUtil;
    }

    /**
     * 为RedisTemplate配置Redis连接工厂实现
     * LettuceConnectionFactory实现了RedisConnectionFactory接口
     * 这里要注意的是，在构建LettuceConnectionFactory 时，如果不使用内置的destroyMethod，可能会导致Redis连接早于其它Bean被销毁
     *
     * @return 返回LettuceConnectionFactory
     */
//    @Bean(destroyMethod = "destroy")
//    public LettuceConnectionFactory lettuceConnectionFactory() {
//
//        List<String> clusterNodes = redisProperties.getCluster().getNodes();
//        Set<RedisNode> nodes = new HashSet<>();
//        clusterNodes.forEach(address -> nodes.add(new RedisNode(address.split(":")[0].trim(), Integer.parseInt(address.split(":")[1]))));
//        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
//        clusterConfiguration.setClusterNodes(nodes);
//        clusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
//        clusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
//
//        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
//        poolConfig.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
//        poolConfig.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
//        poolConfig.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
//
//        return new LettuceConnectionFactory(clusterConfiguration, getLettuceClientConfiguration(poolConfig));
//    }
    /**
     * 配置LettuceClientConfiguration 开启自适应刷新拓扑 包括线程池配置和安全项配置
     *
     * @param genericObjectPoolConfig common-pool2线程池
     * @return lettuceClientConfiguration
     */
    private LettuceClientConfiguration getLettuceClientConfiguration(GenericObjectPoolConfig genericObjectPoolConfig) {
        /*
        ClusterTopologyRefreshOptions配置用于开启自适应刷新和定时刷新。如自适应刷新不开启，Redis集群变更时将会导致连接异常！
         */
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                //开启所有自适应刷新，MOVED，ASK，PERSISTENT都会触发
                .enableAllAdaptiveRefreshTriggers()
                // 自适应刷新超时时间(默认30秒)
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(30))
                // 开周期刷新
                //.enablePeriodicRefresh(Duration.ofSeconds(20))
                .build();
        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder().topologyRefreshOptions(topologyRefreshOptions).build();
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .clientOptions(clusterClientOptions)
                .build();
    }

}