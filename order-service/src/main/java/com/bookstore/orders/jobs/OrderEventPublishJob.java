package com.bookstore.orders.jobs;

import com.bookstore.orders.domain.OrderEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OrderEventPublishJob {


    private final static Logger logger = LoggerFactory.getLogger(OrderEventPublishJob.class);

    private final OrderEventService orderEventService;

    public OrderEventPublishJob(OrderEventService orderEventService) {
        this.orderEventService = orderEventService;
    }

    @Scheduled(cron = "*/25 * * * * *")
    public void publishOrderEvents(){
        logger.info("Publishing order events at" + Instant.now());
        orderEventService.publishOrderEvents();
    }

}
