package com.bookstore.orders.domain;

import com.bookstore.orders.domain.models.OrderCreatedEvent;
import com.bookstore.orders.domain.models.OrderEventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderEventService {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventService.class);

    private final OrderEventRepository orderEventRepository;

    private final OrderEventPublisher orderEventPublisher;

    private ObjectMapper objectMapper;


    public OrderEventService(OrderEventRepository orderEventRepository, OrderEventPublisher orderEventPublisher, ObjectMapper objectMapper) {
        this.orderEventRepository = orderEventRepository;
        this.orderEventPublisher = orderEventPublisher;
        this.objectMapper = objectMapper;
    }

    void saveOrder(OrderCreatedEvent event) {

        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setPayload(toJsonPayload(event));
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEventRepository.save(orderEvent);
        logger.info("Saved order event: {}", orderEvent);
    }

    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEventEntity> events = orderEventRepository.findAll(sort);
        logger.info("Found {} Order Events to be published", events.size());
        for (OrderEventEntity event : events) {
            this.publishEvent(event);
            orderEventRepository.delete(event);
            logger.warn("Deleted order event: {}", event.getEventId());
        }
    }

    private void publishEvent(OrderEventEntity event) {
        OrderEventType eventType = event.getEventType();
        switch (eventType) {
            case ORDER_CREATED:
                OrderCreatedEvent orderCreatedEvent = fromJsonPayload(event.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderCreatedEvent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + eventType);
        }
    }

    private <T> T fromJsonPayload(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
