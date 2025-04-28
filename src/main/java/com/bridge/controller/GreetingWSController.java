package com.bridge.controller;

import com.bridge.entity.Call;
import com.bridge.entity.Game;
import com.bridge.entity.card.Card;
import com.bridge.entity.websocket.*;
import com.bridge.enumeration.WebsocketNotifyType;
import com.bridge.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static com.bridge.constant.WebsocketDestination.TOPIC_ENTRY;

@RestController
@RequestMapping("ws")
@Slf4j
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
    public void testWebSocket(@PathVariable Long playerId) {
        WebsocketNotifyEntry websocketNotifyEntry = WebsocketNotifyEntry.builder()
                .type(WebsocketNotifyType.ENTRY)
                .message("new player " + playerId + " has entered the room ")
                .createTime(new Timestamp(System.currentTimeMillis()))
                .build();
        simpMessagingTemplate.convertAndSend(TOPIC_ENTRY + "/" + playerId, JsonUtils.serialize(websocketNotifyEntry));
    }

    @PostMapping("test")
    public void webWebSocket2(@RequestBody WebsocketTest testBody) {
        WebsocketNotifyBase base = null;
        switch (testBody.getWebsocketNotifyType()) {
            case ENTRY :
                base = WebsocketNotifyEntry.builder()
                        .type(WebsocketNotifyType.ENTRY)
                        .message("new player " + testBody.getUserId() + " has entered the room test room")
                        .createTime(new Timestamp(System.currentTimeMillis()))
                        .build();
                break;
            case READY:
                base = WebsocketNotifyEntry.builder()
                    .type(WebsocketNotifyType.READY)
                    .message("new player " + testBody.getUserId() + " has entered the room test room")
                    .createTime(new Timestamp(System.currentTimeMillis()))
                    .build();
                break;
            case BEGIN:
                base = WebsocketNotifyBegin.builder()
                    .type(WebsocketNotifyType.BEGIN)
                    .message("The room is full, let the game begin!")
                    .build();
                break;
            case SHUFFLE:
                base = WebsocketNotifyShuffle.builder()
                        .type(WebsocketNotifyType.SHUFFLE)
                        .player1Cards(null)
                        .player2Cards(null)
                        .player3Cards(null)
                        .player4Cards(null)
                        .createTime(new Timestamp(System.currentTimeMillis()))
                        .build();
                break;
            case CALL:
                Game game = new Game();
                Call call = new Call();
                base = WebsocketNotifyCall.builder()
                        .type(WebsocketNotifyType.CALL)
                        .call(call)
                        .message(String.format("Game %s has started, trump : %s, level : %d.", game.getRoomName(), game.getTrump(), game.getLevel()))
                        .createTime(new Timestamp(System.currentTimeMillis()))
                        .build();
                break;
            case PLAY:
                Card card = new Card();
                base = WebsocketNotifyPlay.builder()
                        .type(WebsocketNotifyType.PLAY)
                        .account(String.valueOf(testBody.getUserId()))
                        .card(card)
                        .createTime(new Timestamp(System.currentTimeMillis()))
                        .build();
                break;
        }
        String string = JsonUtils.serialize(base);
        String destination = testBody.getWebsocketNotifyType().destination + "/" + testBody.getUserId();
        simpMessagingTemplate.convertAndSend(destination, string);
        log.info("Try to send websocket {} to {}", string, destination);
    }
}
