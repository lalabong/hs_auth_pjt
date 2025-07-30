package com.hs.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "emailExecutor")
    public Executor emailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 기본 실행 대기 중인 Thread 수
        executor.setMaxPoolSize(5); // 동시 동작하는 최대 Thread 수
        executor.setQueueCapacity(10); // Thread Pool Queue 크기
        executor.setThreadNamePrefix("EmailAsync-");
        executor.initialize();
        return executor;
    }
}