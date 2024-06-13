package com.bridge.controller;

import com.bridge.service.ICallService;
import com.bridge.entity.Call;
import com.bridge.entity.Game;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CallController {

    @Autowired
    private ICallService callService;

    @PostMapping("/call")
    public ResponseEntity<Object> call(@RequestHeader HttpHeaders headers,
            @Valid @RequestBody Call call) {
        try {
            List<String> tokens = headers.get("token");
            if (null == tokens || tokens.isEmpty()) {
                return new ResponseEntity<>("No token.", HttpStatus.FORBIDDEN);
            }
            List<String> gameId = headers.get("gameId");
            if (null == gameId || gameId.isEmpty()) {
                return new ResponseEntity<>("No gameId.", HttpStatus.FORBIDDEN);
            }
            call.setPlayerId(tokens.get(0));
            call.setGameId(gameId.get(0));
            Game success = callService.call(call);
            return new ResponseEntity<>(success, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("exception : " + e);
            return new ResponseEntity<>("Call failed.", HttpStatus.BAD_REQUEST);
        }
    }
}
