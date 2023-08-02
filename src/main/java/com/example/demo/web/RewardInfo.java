package com.example.demo.web;

import lombok.Data;

import java.util.Map;

@Data
public class RewardInfo {
    private String name;
    private Long total;
    private Map<Integer, Long> rewards;
}
