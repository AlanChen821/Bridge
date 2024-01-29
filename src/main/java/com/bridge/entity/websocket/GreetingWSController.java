package com.bridge.entity.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingWSController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public GreetingWS greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new GreetingWS("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}
