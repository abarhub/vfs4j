package io.github.abarhub.vfs.logback;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test1 {

    @Test
    public void test1(){
        Logger logger = LoggerFactory.getLogger(Test1.class);
        logger.info("Hello world.");
        logger.info("end");

    }
}
