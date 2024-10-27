package com.cb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void applicationStartsSuccessfully() {
        Application.main(new String[] {});
        assertNotNull(Application.class);
    }
}