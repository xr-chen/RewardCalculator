package com.example.demo.service;

import com.example.demo.Customer;
import com.example.demo.Purchase;
import com.example.demo.business.RewardPoint;
import com.example.demo.data.CustomerRepository;
import com.example.demo.web.RewardInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RewardPointTest {

    private RewardPoint rewardPoint;

    @Mock
    private CustomerRepository customerRepo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        rewardPoint = new RewardPoint(customerRepo);
    }

    @Test
    public void testRewardForAll() {
        List<Customer> customers = new ArrayList<>();
        customers.add(createCustomerWithPurchases("customer1", 120L, 80L, 200L));
        customers.add(createCustomerWithPurchases("customer2", 60L, 70L));

        when(customerRepo.findAll()).thenReturn(customers);

        List<RewardInfo> infoList = new ArrayList<>();
        rewardPoint.rewardForAll(infoList);

        assertEquals(2, infoList.size());
        assertEquals("customer1", infoList.get(0).getName());
        assertEquals(370L, infoList.get(0).getTotal());

        assertEquals("customer2", infoList.get(1).getName());
        assertEquals(30L, infoList.get(1).getTotal());

    }

    @Test
    public void testRewardByCustomerName() {
        Customer customer = createCustomerWithPurchases("customer1", 120L, 80L, 200L);
        when(customerRepo.findById("customer1")).thenReturn(java.util.Optional.of(customer));

        RewardInfo info = new RewardInfo();
        info.setName("customer1");

        rewardPoint.rewardByCustomerName(info);

        assertEquals("customer1", info.getName());
        assertEquals(370L, info.getTotal());
        assertEquals(0L, info.getRewards().get(1));
    }

    @Test
    public void testRewardByCustomerPerMonth() {
        Customer customer = createCustomerWithPurchases("customer1", 120L, 80L, 200L);
        when(customerRepo.findById("customer1")).thenReturn(java.util.Optional.of(customer));

        RewardInfo info = new RewardInfo();
        info.setName("customer1");

        rewardPoint.rewardByCustomerPerMonth(info, 1);

        assertEquals("customer1", info.getName());
        assertEquals(0L, info.getTotal());
        assertEquals(0L, info.getRewards().get(1));
    }

    // Helper method to create a customer with purchases
    private Customer createCustomerWithPurchases(String accountName, Long... amounts) {
        Customer customer = new Customer();
        customer.setAccountName(accountName);

        List<Purchase> purchases = new ArrayList<>();
        for (Long amount : amounts) {
            Purchase purchase = new Purchase();
            purchase.setAmount(amount);
            purchase.setCreatedAt(new Date());
            purchases.add(purchase);
        }
        customer.setPurchases(purchases);

        return customer;
    }
}
