package com.bank.gugu.global.config;

import com.bank.gugu.user.vo.MasterKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public MasterKey masterKey(@Value("${gugu.master-key}") String masterKey) {
        return new MasterKey(masterKey);
    }
}
