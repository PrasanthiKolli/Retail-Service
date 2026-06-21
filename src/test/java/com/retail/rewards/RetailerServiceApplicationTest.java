package com.retail.rewards;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = RetailerServiceApplication.class)
public class RetailerServiceApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void mainMethodLoadsApplication() {
        RetailerServiceApplication.main(new String[] {"--spring.main.web-application-type=none"});
    }
}
