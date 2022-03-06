package pl.vemu.sociely;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.vemu.sociely.controllers.UserController;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ChatApplicationTests {

    @Autowired
    private UserController controller;

    @Test
    void contextLoads() {
        assertNotNull(controller);
    }

}
