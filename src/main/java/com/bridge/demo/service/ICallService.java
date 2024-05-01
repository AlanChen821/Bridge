package com.bridge.demo.service;

import com.bridge.entity.Call;

import java.util.List;

public interface ICallService {
    List<Call> call(Call call) throws Exception;
}
