package com.github.hronom.test.spring.rabbit.consumer.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

@Controller
public class ConsumerController {
    private static final Logger logger = LogManager.getLogger();

    @RabbitListener(containerFactory="myRabbitListenerContainerFactory", queues = "queue1")
    public String processQueue1(String message) {
        logger.info("Received from \"queue1\" message: \"" + message + "\"");
        try {
            // Emulate work.
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String processedMessage = message + " - processed.";
        logger.info("Send to \"queue1\" processed message: \"" + processedMessage + "\"");
        return processedMessage;
    }
}