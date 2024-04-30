package com.bridge.demo;

import com.bridge.entity.websocket.HelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@Slf4j
public class LoginController {

    public LoginController() {
        log.info("intialize");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody HelloMessage message) {
        Random random = new Random();
        int randomResult = random.nextInt(1000);
        return new ResponseEntity<>(randomResult, HttpStatus.OK);
    }
}
