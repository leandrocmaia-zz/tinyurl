package com.leomaya.tinyurl.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.graphite.GraphiteMeterRegistry;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MetricsConfig {

    @Getter
    private Counter error;
    @Getter
    private Counter internalServerError;
    @Getter
    private Counter notFound;
    @Getter
    private Counter badRequest;

    @Value("${spring.application.name}")
    private String serviceName;

    @PostConstruct
    void registerCounters(){
        error = Metrics.counter("app.exception.count");
        internalServerError = Metrics.counter("app.internalerror.count");
        notFound = Metrics.counter("app.notfound.count");
        badRequest = Metrics.counter("app.badrequest.count");
    }


    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }


}
