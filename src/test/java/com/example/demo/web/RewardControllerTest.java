package com.example.demo.web;
import com.example.demo.business.RewardPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RewardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RewardPoint rewardPoint;

    @InjectMocks
    private RewardController rewardController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rewardController).build();
    }

    @Test
    public void testRewardsForCustomerWithoutMonth() throws Exception {
        RewardInfo rewardInfo = new RewardInfo();
        rewardInfo.setName("customer1");
        rewardInfo.setTotal(110L);
        rewardInfo.setRewards(Map.of(1, 170L));



        mockMvc.perform(get("/api/v1/rewards/customer1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("customer1"));


        verify(rewardPoint, times(1)).rewardByCustomerName(any());
        verifyNoMoreInteractions(rewardPoint);
    }

    @Test
    public void testRewardsForCustomerWithMonth() throws Exception {
        RewardInfo rewardInfo = new RewardInfo();
        rewardInfo.setName("customer1");
        rewardInfo.setTotal(170L);
        rewardInfo.setRewards(Map.of(1, 170L));


        mockMvc.perform(get("/api/v1/rewards/customer1?month=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("customer1"));


        verify(rewardPoint, times(1)).rewardByCustomerPerMonth(any(), anyInt());
        verifyNoMoreInteractions(rewardPoint);
    }

    @Test
    public void testRewardsForAll() throws Exception {
        List<RewardInfo> infoList = new ArrayList<>();
        RewardInfo rewardInfo1 = new RewardInfo();
        rewardInfo1.setName("customer1");
        rewardInfo1.setTotal(110L);
        rewardInfo1.setRewards(Map.of(1, 170L));
        infoList.add(rewardInfo1);

        RewardInfo rewardInfo2 = new RewardInfo();
        rewardInfo2.setName("customer2");
        rewardInfo2.setTotal(30L);
        rewardInfo2.setRewards(Map.of(1, 20L));
        infoList.add(rewardInfo2);


        MvcResult mvcResult = mockMvc.perform(get("/api/v1/rewards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        // Here you can validate the JSON response as per your needs

        verify(rewardPoint, times(1)).rewardForAll(anyList());
        verifyNoMoreInteractions(rewardPoint);
    }
}
