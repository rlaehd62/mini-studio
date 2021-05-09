package com.github.rlaehd62.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@Component
public class EventSubscribeBeanPostProcessor implements BeanPostProcessor 
{
	@Autowired private EventBus eventBus;
	
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException 
    {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException 
    {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) 
        {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) 
            {
                if (annotation.annotationType().equals(Subscribe.class)) 
                {
                    eventBus.register(bean);
                    return bean;
                }
            }
        }

        return bean;
    }
}