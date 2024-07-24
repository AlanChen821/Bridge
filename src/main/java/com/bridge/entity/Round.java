package com.bridge.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Round implements Serializable {
    private List<Play> plays;
}
