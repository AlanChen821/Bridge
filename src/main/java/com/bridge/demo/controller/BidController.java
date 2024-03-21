package com.bridge.demo.controller;

import com.bridge.demo.service.IBidService;
import com.bridge.entity.Bid;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BidController {

    @Autowired
    private IBidService bidService;

    public BidController() {
//        System.out.println("initialize BidController.");
    }

    @PostMapping("/bid")
    public ResponseEntity<Object> bid(@Valid @RequestBody Bid bid) {
        try {
            List<Bid> success = bidService.bid(bid);
            return new ResponseEntity<>(success, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("exception : " + e);
            return new ResponseEntity<>("bid failed.", HttpStatus.BAD_REQUEST);
        }
    }
}
