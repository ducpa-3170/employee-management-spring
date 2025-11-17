package com.example.employee_management.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemHealthScheduler {

    private static final Logger logger = LoggerFactory.getLogger(SystemHealthScheduler.class);

    @Scheduled(fixedRate = 30000) // 30 seconds = 30000 milliseconds
    public void logSystemStatus() {
        logger.info("System running");
    }
}
