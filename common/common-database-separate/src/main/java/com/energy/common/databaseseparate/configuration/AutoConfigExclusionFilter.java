package com.energy.common.databaseseparate.configuration;

import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.HashSet;
import java.util.Set;

public class AutoConfigExclusionFilter
        implements AutoConfigurationImportFilter, EnvironmentAware {
    //配置不拦截url
    private String url;

    private boolean enabled;

    private Set<String> getShouldSkipSet(){
        Set<String> set = new HashSet<>();
        if(Strings.isEmpty(url)){
            set.add("org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration");
            set.add("org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration");
            set.add("org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration");
        }
        if(!enabled){
            set.add("org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration");
            set.add("org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration");
        }
        return set;
    }

    @Override
    public boolean[] match(String[] classNames, AutoConfigurationMetadata metadata) {
        boolean[] matches = new boolean[classNames.length];
        Set<String> shouldSkipSet = getShouldSkipSet();
        for(int i = 0; i< classNames.length; i++) {
            matches[i] = !shouldSkipSet.contains(classNames[i]);
        }
        return matches;
    }

    @Override
    public void setEnvironment(Environment environment) {
        url = environment.getProperty("spring.datasource.url");
        enabled  = environment.getProperty("datasource.separator.enable", Boolean.class, true);
    }
}
