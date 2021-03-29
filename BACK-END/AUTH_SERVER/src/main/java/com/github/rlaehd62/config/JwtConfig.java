package com.github.rlaehd62.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class JwtConfig
{
    @Value("${security.jwt.header:ACCESS_TOKEN}")
    private String access_header;
    
    @Value("${security.jwt.header:REFRESH_TOKEN}")
    private String refresh_header;
    
    @Value("${security.jwt.ACCESS_TOKEN_EXPIRATION:86400}")
    private int access_token_expiration;
    
    @Value("${security.jwt.REFRESH_TOKEN_EXPIRATION:604800}")
    private int refresh_token_expiration;

    @Value("${security.jwt.secret:MTM6vSAapEMJ3IWtx9BCEgzfq7ULldFU}")
    private String secret;
}
