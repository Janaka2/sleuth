package com.jk.sleuth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

@RestController
@Slf4j
public class SleuthController {

    @Autowired
    private SleuthService sleuthService;

    @Autowired
    private Executor executor;

    @GetMapping("/new-thread")
    public String helloSleuthNewThread() {
        log.info("New Thread");
        Runnable runnable = () -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("I'm inside the new thread - with a new span");
        };
        executor.execute(runnable);

        log.info("I'm done - with the original span");
        return "success";
    }

    @GetMapping("/same-span")
    public String helloSleuthSameSpan() throws InterruptedException {
        log.info("Same Span");
        sleuthService.doSomeWorkSameSpan();
        return "success";
    }

    @GetMapping("/new-span")
    public String helloSleuthNewSpan() {
        log.info("New Span");
        sleuthService.doSomeWorkNewSpan();
        return "success";
    }

    @GetMapping("/")
    public String helloSleuth() {
        log.info("Hello Sleuth");
        return "success";
    }

    @GetMapping("/async")
    public String helloSleuthAsync() {
        log.info("Before Async Method Call");
        sleuthService.asyncMethod();
        log.info("After Async Method Call");

        return "success";
    }
}
