package io.github.alexvish.testlogging;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.Closeable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import static net.logstash.logback.argument.StructuredArguments.*;

@SpringBootApplication
@EnableScheduling
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Autowired
    private Checker checker;

    @Autowired
    private TaskScheduler scheduler;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }




    private void checkRandomIntValue(){}

    @Scheduled(fixedDelay = 2000)
    public void logWithStructuredArguments() {
        Random random = new Random();
        int randomInt = random.nextInt(500);
        try (AutoCloseable c = MDC.putCloseable("randomInt", "" + randomInt)) {

            final String randomString = RandomStringUtils.randomAlphabetic(5);

            log.info("Argument is visible from message only: {}", randomString);

            checker.check(randomInt);

            log.info("Argument is available as \"randomString\" field \n" +
                     "Argument value: {}", value("randomString", randomString));

            log.info("Argument is available as \"randomString\" field \n" +
                    "Argument key and value: {}", keyValue("randomString", randomString));


            final Map<String, String> mdcContext = MDC.getCopyOfContextMap();
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    MDC.setContextMap(mdcContext);
                    log.info("MDC context restored: {}", keyValue("randomString", randomString));
                }
            }, ZonedDateTime.now().plusSeconds(50).toInstant());
        } catch (Exception x) {
            log.error("Exception thrown: " + x,x);
        }
    }
}
