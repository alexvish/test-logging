package io.github.alexvish.testlogging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.*;

@Component
public class Checker {
    private static final Logger log = LoggerFactory.getLogger(Checker.class);

    public void check(int randomValue) {
        log.info("Checking {} > 50", value("checkValue", randomValue));
        if (randomValue <= 50) {
            throw new IllegalArgumentException("Random value is too small");
        }
    }
}
