package com.github.rlaehd62.messaging;

import com.github.rlaehd62.entity.auth.Account;
import com.github.rlaehd62.vo.TokenType;
import com.github.rlaehd62.vo.account.AccountInfo;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class DataFetcher
{

    private RestTemplate restTemplate;

    @Autowired
    public DataFetcher(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public <T> Optional<T> fetch(String url, HttpMethod method, Supplier<HttpEntity<?>> supplier, Class<T> cls)
    {
        T response = restTemplate.exchange(url.startsWith("http://") ? url : "http://"+url, method, supplier.get(), cls).getBody();
        return Optional.ofNullable(response);
    }
}
