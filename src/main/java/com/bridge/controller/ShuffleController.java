package com.bridge.controller;

import com.bridge.service.IShuffleService;
import com.bridge.entity.user.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShuffleController {

    @Autowired
    private IShuffleService shuffleService;

    public ShuffleController() {
//        System.out.println("initialize.");
    }

    @GetMapping("/shuffle")
    public List<Player> shuffle(@RequestParam(value = "name", defaultValue = "World") String name) {
        return shuffleService.shuffle("", "", "", "");
//        return new Greeting(0, name);
    }
}
