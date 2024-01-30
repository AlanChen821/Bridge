package com.bridge.demo;

import com.bridge.entity.websocket.GreetingWS;
import com.bridge.entity.websocket.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GreetingWSController {

    private static Map<String, Integer> map = new HashMap<>();

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public GreetingWS greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new GreetingWS("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/login")
    @SendTo("/topic/login")
    public GreetingWS login(HelloMessage message) throws Exception {
        Thread.sleep(100);
        Integer order = map.getOrDefault(message.getId(), null);
        if (null == order) {
            order = map.size();
            map.put(message.getId(), order);
        }
        return new GreetingWS("Hello! You are the " + order + "th user.");
    }
}
