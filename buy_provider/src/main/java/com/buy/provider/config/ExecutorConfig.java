package com.buy.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorConfig {
    @Bean
    public Executor asyncExecutor(){
        //创建线程池
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数(即提前创建好10个线程)
        executor.setCorePoolSize(10);
        //配置最大线程数(即可以根据需要最大创建100个线程)
        executor.setMaxPoolSize(100);
        //配置最大队列的数量(池队列中消息的最多数量)
        executor.setQueueCapacity(200);
        //配置线程最大空闲时间(即线程空闲时间超过3000s就会被销毁)
        executor.setKeepAliveSeconds(3000);
        //配置线程池中的线程名称的前缀
        executor.setThreadNamePrefix("async-limitTimeBuy-");//线程的默认名称Thread0
        /*
            拒绝策略:当pool中的线程被占用完了达到max size的时候,如何处理新的任务,采取的策略
            CallerRunsPolicy: 即不在新线程中执行任务,而是在调用者所在的线程中执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
