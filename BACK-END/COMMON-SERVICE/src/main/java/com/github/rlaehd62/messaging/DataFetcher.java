package com.github.rlaehd62.messaging;

import com.github.rlaehd62.entity.auth.Account;
import com.github.rlaehd62.entity.auth.Block;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class DataFetcher
{

    private RestTemplate restTemplate;

    @Autowired
    public DataFetcher(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public <T> Optional<T> fetch(String url, HttpMethod method, HttpEntity<?> entity, Class<T> cls)
    {
        T response = restTemplate.exchange(url.startsWith("http://") ? url : "http://"+url, method, entity, cls).getBody();
        return Optional.ofNullable(response);
    }

    @Deprecated
    public <T> Optional<T> fetch(String url, HttpMethod method, Supplier<HttpEntity<?>> supplier, Class<T> cls)
    {
        T response = restTemplate.exchange(url.startsWith("http://") ? url : "http://"+url, method, supplier.get(), cls).getBody();
        return Optional.ofNullable(response);
    }

    public HttpEntity<?> createHttpEntity(String token)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set(TokenType.ACCESS.getName(), token);
        return new HttpEntity<>("", headers);
    }

    public boolean verifyAccount(String token, Account account)
    {
        Optional<AccountInfo> optional = fetch("AUTH-SERVICE/tokens/verify?token="+token, HttpMethod.GET, createHttpEntity(token), AccountInfo.class);
        return optional
                .map(AccountInfo::getId)
                .map(id -> id.equals(account.getId()))
                .orElse(false);
    }

    public boolean verifyBlocking(String token, Account account)
    {
        List<Block> blackList = account.getBlocks();
        List<String> list = blackList.stream()
                .map(Block::getBlocked)
                .map(Account::getId)
                .collect(Collectors.toList());

        Optional<AccountInfo> optional = fetch("AUTH-SERVICE/tokens/verify?token="+token, HttpMethod.GET, createHttpEntity(token), AccountInfo.class);
        return optional
                .map(AccountInfo::getId)
                .map(list::contains)
                .orElse(false);
    }
}
