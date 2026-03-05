package com.bank.gugu.global.config.dataBase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DataSourceConfig {

    @Bean("masterHikariConfig")
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public HikariConfig masterHikariConfig() {
        return new HikariConfig();
    }

    @Bean("slaveHikariConfig")
    @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
    public HikariConfig slaveHikariConfig() {
        return new HikariConfig();
    }

    @Primary
    @Bean("masterDataSource")
    public DataSource masterDataSource(@Qualifier("masterHikariConfig") HikariConfig config) {
        return new HikariDataSource(config);
    }

    @Bean("slaveDataSource")
    public DataSource slaveDataSource(@Qualifier("slaveHikariConfig") HikariConfig config) {
        return new HikariDataSource(config);
    }


}
