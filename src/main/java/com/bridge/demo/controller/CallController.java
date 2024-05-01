package com.bridge.demo.controller;

import com.bridge.demo.service.ICallService;
import com.bridge.entity.Call;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CallController {

    @Autowired
    private ICallService callService;

    @PostMapping("/call")
    public ResponseEntity<Object> call(@Valid @RequestBody Call call) {
        try {
            List<Call> success = callService.call(call);
            return new ResponseEntity<>(success, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("exception : " + e);
            return new ResponseEntity<>("Call failed.", HttpStatus.BAD_REQUEST);
        }
    }
}
