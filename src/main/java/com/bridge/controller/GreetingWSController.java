package com.bridge.controller;

import com.bridge.entity.websocket.GreetingWS;
import com.bridge.entity.websocket.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GreetingWSController {

    private static Map<String, Integer> map = new HashMap<>();

    private SimpMessagingTemplate messageTemplate;

    public GreetingWSController(SimpMessagingTemplate template) {
        this.messageTemplate = template;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public GreetingWS greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
//        messageTemplate.convertAndSend("/topic/greetings", "Let's play!");
        return new GreetingWS("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/login")
    @SendTo("/topic/login")
    public GreetingWS login(HelloMessage message) throws Exception {
//        Thread.sleep(100);
        Integer order = map.getOrDefault(message.getId(), null);
        if (null == order) {
            order = map.size();
            map.put(message.getId(), order);
        }
        messageTemplate.convertAndSend("/topic/greetings", "{\"message\":\"start!\"}");
//        if (order != 3) {
            System.out.println(order + "th player " + message.getId() + " has logged in.");
            return new GreetingWS("Welcome! You are the " + HtmlUtils.htmlEscape(order.toString()) + "th user.");
//        } else {
//            start();
//            return null;
//        }
    }

    @SendTo("/topic/greetings")
//    @SendTo("/topic/1/cards")
    public void start() {
        System.out.println("Let's play!");
//        messageTemplate.convertAndSend("/topic/1/cards", "Let's play!");
        messageTemplate.convertAndSend("/topic/greetings", "Let's play!");
        System.out.println("Has sent.");
    }
//    @SendTo("/topic/1/cards")
//    public GreetingWS start() {
//        return new GreetingWS("Let's play!");
//    }
}
