package com.example.demo.web;

import com.example.demo.business.RewardPoint;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/rewards")
public class RewardController {
    private RewardPoint rewardPoint;

    public RewardController(RewardPoint rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    @GetMapping("/{name}")
    public RewardInfo rewardsForCustomer(@PathVariable String name, @RequestParam(value = "month", required = false) Integer month) {
        RewardInfo rewardInfo = new RewardInfo();
        rewardInfo.setName(name);
        if (month == null) {
            rewardPoint.rewardByCustomerName(rewardInfo);
        } else {
            rewardPoint.rewardByCustomerPerMonth(rewardInfo, month);
        }
        return rewardInfo;
    }

    @GetMapping
    public List<RewardInfo> rewardsForAll() {
        List<RewardInfo> infoList = new ArrayList<>();
        rewardPoint.rewardForAll(infoList);
        return infoList;
    }

}
