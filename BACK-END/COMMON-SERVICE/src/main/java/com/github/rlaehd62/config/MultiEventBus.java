package com.github.rlaehd62.config;

import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MultiEventBus
{
    private EventBus eventBus;

    @Autowired
    public MultiEventBus(EventBus eventBus)
    {
        this.eventBus = eventBus;
    }
}
