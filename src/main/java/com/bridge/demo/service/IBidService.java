package com.bridge.demo.service;

import com.bridge.entity.Bid;

import java.util.List;

public interface IBidService {
    List<Bid> bid(Bid bid) throws Exception;
}
