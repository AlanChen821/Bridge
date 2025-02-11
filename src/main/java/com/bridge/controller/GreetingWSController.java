package com.bridge.controller;

import com.bridge.entity.websocket.GreetingWS;
import com.bridge.entity.websocket.HelloMessage;
import com.bridge.entity.websocket.WebsocketNotifyEntry;
import com.bridge.enumeration.WebsocketNotifyType;
import com.bridge.utils.JsonUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static com.bridge.constant.WebsocketDestination.TOPIC_ENTRY;

@RestController
@RequestMapping("ws")
public class GreetingWSController {

    private static Map<String, Integer> map = new HashMap<>();

    private SimpMessagingTemplate simpMessagingTemplate;

    public GreetingWSController(SimpMessagingTemplate template) {
        this.simpMessagingTemplate = template;
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public GreetingWS greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new GreetingWS("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/login")
    @SendTo("/topic/login")
    public GreetingWS login(HelloMessage message) throws Exception {
        Integer order = map.getOrDefault(message.getId(), null);
        if (null == order) {
            order = map.size();
            map.put(message.getId(), order);
        }
        simpMessagingTemplate.convertAndSend("/topic/greetings", "{\"message\":\"start!\"}");
//        if (order != 3) {
            System.out.println(order + "th player " + message.getId() + " has logged in.");
            return new GreetingWS("Welcome! You are the " + HtmlUtils.htmlEscape(order.toString()) + "th user.");
//        } else {
//            start();
//            return null;
//        }
    }

    @SendTo("/topic/greetings")
    public void start() {
        System.out.println("Let's play!");
        simpMessagingTemplate.convertAndSend("/topic/greetings", "Let's play!");
        System.out.println("Has sent.");
    }

    @GetMapping("/{playerId}")
    public void test(@PathVariable Long playerId) {
        WebsocketNotifyEntry websocketNotifyEntry = WebsocketNotifyEntry.builder()
                .type(WebsocketNotifyType.ENTRY)
                .message("new player " + playerId + " has entered the room ")
                .createTime(new Timestamp(System.currentTimeMillis()))
                .build();
        simpMessagingTemplate.convertAndSend(TOPIC_ENTRY + playerId, JsonUtils.serialize(websocketNotifyEntry));
    }
}
