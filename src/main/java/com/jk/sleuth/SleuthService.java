package com.jk.sleuth;

import brave.Span;
import brave.Tracer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SleuthService {

    @Autowired
    private brave.Tracer tracer;

    // ...
    @SneakyThrows
    public void doSomeWorkNewSpan() {
        log.info("I'm in the original span");

        Span newSpan = tracer.nextSpan().name("newSpan").start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(newSpan.start())) {
            Thread.sleep(1000L);
            log.info("I'm in the new span doing some cool work that needs its own span");
        } finally {
            newSpan.finish();
        }

        log.info("I'm in the original span");
    }

    @SneakyThrows
    public void doSomeWorkSameSpan() {
        Thread.sleep(1000L);
        log.info("Doing some work");
    }

    @Async
    @SneakyThrows
    public void asyncMethod() {
        log.info("Start Async Method");
        Thread.sleep(1000L);
        log.info("End Async Method");
        throw new RuntimeException("tester");
    }

//    @Scheduled(fixedDelay = 30000)
    public void scheduledWork() throws InterruptedException {
        log.info("Start some work from the scheduled task");
        this.asyncMethod();
        log.info("End work from scheduled task");
    }
}