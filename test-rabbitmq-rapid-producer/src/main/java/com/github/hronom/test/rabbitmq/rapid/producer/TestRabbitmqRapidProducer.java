package com.github.hronom.test.rabbitmq.rapid.producer;

import com.github.hronom.test.rabbitmq.common.pojos.TextPojo;

import net.moznion.random.string.RandomStringGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestRabbitmqRapidProducer {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws
        IOException,
        TimeoutException, InterruptedException {
        final String
            stringPattern
            = "Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!Ccn!CCccCn!cccccccCn!!Ccccc!cc!ccccc!cccc!!Cc!C!C!";
        final RandomStringGenerator generator = new RandomStringGenerator();

        try (RabbitmqRapidProducer rapidProducer = new RabbitmqRapidProducer()) {
            int totalCountOfSendedMessages = 0;
            long totalSendTime = 0;

            long timeOfLastUpdate = 0;
            int countOfMessagesInSec = 0;

            while (true) {
                //logger.info("Generate random string...");
                TextPojo textPojo = new TextPojo();
                textPojo.text = generator.generateFromPattern(stringPattern);
                try {
                    long sendingStartTime = System.currentTimeMillis();

                    //logger.info("Emit to \"" + rapidProducer.getRequestQueueName() + "\" message: \"" + textPojo.text + "\"");
                    rapidProducer.post(textPojo);
                    //Thread.sleep(TimeUnit.SECONDS.toMillis(1));

                    long currentTime = System.currentTimeMillis();

                    long sendTime = currentTime - sendingStartTime;

                    totalSendTime += sendTime;

                    totalCountOfSendedMessages++;
                    countOfMessagesInSec++;
                    if (currentTime - timeOfLastUpdate > TimeUnit.SECONDS.toMillis(1)) {
                        logger.info("Average send time: " +
                                    (double) (totalSendTime / totalCountOfSendedMessages) + " ms.");
                        logger.info("Count of messages in second: " + countOfMessagesInSec);

                        timeOfLastUpdate = currentTime;
                        countOfMessagesInSec = 0;
                    }
                } catch (Exception exception) {
                    logger.fatal("Fail!", exception);
                }
            }
        } catch (Exception exception) {
            logger.fatal("Fail!", exception);
        }
    }
}
