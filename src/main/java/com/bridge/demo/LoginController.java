package com.bridge.demo;

import com.bridge.entity.websocket.HelloMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@Slf4j
public class LoginController {

    private final SimpMessagingTemplate messagingTemplate;

    public LoginController(SimpMessagingTemplate simpMessagingTemplate) {
//        log.info("intialize");
        this.messagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody HelloMessage message) {
        Random random = new Random();
//        int randomResult = random.nextInt(1000);
        int randomResult = 1;
        return new ResponseEntity<>(randomResult, HttpStatus.OK);
    }

    @PostMapping("/login2")
    public ResponseEntity<Object> send(@RequestBody HelloMessage data) {
        String destination = "ws://localhost:8080/gs-guide-websocket/topic/cards/" + data.getId();
        destination = "/app/cards/" + data.getId();
        log.info("send message to " + destination);
        messagingTemplate.convertAndSend(destination, "Let's play! Here's your cards:");
        return new ResponseEntity<>(destination, HttpStatus.OK);
    }
}
