package com.github.rlaehd62.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.EventBus;

@Configuration
public class EventBusConfiguration
{
        @Bean
        public EventBus eventBus() 
        {
            return new EventBus();
        }
}
