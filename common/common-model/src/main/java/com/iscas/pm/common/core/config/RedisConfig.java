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
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        // jackson???????????????
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // String???????????????
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key??????String??????????????????
        template.setKeySerializer(stringRedisSerializer);
        // hash???key?????????String??????????????????
        template.setHashKeySerializer(stringRedisSerializer);
        // value?????????????????????jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash???value?????????????????????jackson
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
     * ???RedisTemplate??????Redis??????????????????
     * LettuceConnectionFactory?????????RedisConnectionFactory??????
     * ?????????????????????????????????LettuceConnectionFactory ??????????????????????????????destroyMethod??????????????????Redis??????????????????Bean?????????
     *
     * @return ??????LettuceConnectionFactory
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
     * ??????LettuceClientConfiguration ??????????????????????????? ???????????????????????????????????????
     *
     * @param genericObjectPoolConfig common-pool2?????????
     * @return lettuceClientConfiguration
     */
    private LettuceClientConfiguration getLettuceClientConfiguration(GenericObjectPoolConfig genericObjectPoolConfig) {
        /*
        ClusterTopologyRefreshOptions?????????????????????????????????????????????????????????????????????????????????Redis??????????????????????????????????????????
         */
        ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                //??????????????????????????????MOVED???ASK???PERSISTENT????????????
                .enableAllAdaptiveRefreshTriggers()
                // ???????????????????????????(??????30???)
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(30))
                // ???????????????
                //.enablePeriodicRefresh(Duration.ofSeconds(20))
                .build();
        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder().topologyRefreshOptions(topologyRefreshOptions).build();
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .clientOptions(clusterClientOptions)
                .build();
    }

}