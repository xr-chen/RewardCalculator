package com.example.demo.business;

import com.example.demo.Customer;
import com.example.demo.Purchase;
import com.example.demo.data.CustomerRepository;
import com.example.demo.web.RewardInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class RewardPoint {

    private CustomerRepository customerRepo;

    public RewardPoint(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    private Function<Purchase, Long> rewardPerPurchase = purchase -> {
        long amount = purchase.getAmount();
        if (amount > 100) {
            return (amount - 100) * 2 + 50;
        } else if (amount > 50) {
            return amount - 50;
        } else {
            return 0L;
        }
    };

    public void rewardForAll(List<RewardInfo> infoList) {
        for (Customer customer : customerRepo.findAll()) {
            RewardInfo info = new RewardInfo();
            info.setName(customer.getAccountName());
            rewardForCustomer(customer, info);
            infoList.add(info);
        }
    }

    private void rewardForCustomer(Customer customer, RewardInfo info) {
        Map<Integer, Long> rewardsPerMonth = new HashMap<>();
        long total = 0L;
        for (int i = 1; i <= 12; i++) {
            List<Purchase> purchases = customer.getPurchases();
            long perMonth = rewardPerMonth(purchases.stream(), i);
            rewardsPerMonth.put(i, perMonth);
            total += perMonth;
        }
        info.setRewards(rewardsPerMonth);
        info.setTotal(total);
    }

    public void rewardByCustomerName(RewardInfo info) {
        Customer customer = customerRepo.findById(info.getName()).orElse(null);
        if (customer == null) {
            return;
        }
        rewardForCustomer(customer, info);
    }

    public void rewardByCustomerPerMonth(RewardInfo info, int month) {
        Customer customer = customerRepo.findById(info.getName()).orElse(null);
        if (customer == null) {
            return;
        }
        Map<Integer, Long> rewardsPerMonth = new HashMap<>();
        long perMonth = rewardPerMonth(customer.getPurchases().stream(), month);
        rewardsPerMonth.put(month, perMonth);
        info.setRewards(rewardsPerMonth);
        info.setTotal(perMonth);
    }

    private long rewardPerMonth(Stream<Purchase> purchases, int month) {
        Stream<Purchase> filteredStream = purchases.filter(purchase -> {
            LocalDate date = purchase.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return date.getMonthValue() == month;
        });
        return rewardForPurchases(filteredStream);
    }

    public long rewardForPurchases(Stream<Purchase> purchases) {
        return purchases
                .map(rewardPerPurchase)
                .reduce(0L, (a, b) -> a + b);
    }
}
