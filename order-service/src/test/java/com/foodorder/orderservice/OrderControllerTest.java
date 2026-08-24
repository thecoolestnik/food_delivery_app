package com.foodorder.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodorder.orderservice.controller.OrderController;
import com.foodorder.orderservice.kafka.OrderProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderProducer orderProducer;

    @Test
    void placingValidOrderReturns202AndPublishesEvent() throws Exception {
        doNothing().when(orderProducer).publishOrderPlaced(any());

        Map<String, Object> body = Map.of(
                "customerId", "cust-1",
                "restaurantId", "rest-1",
                "items", List.of("Margherita Pizza", "Coke"),
                "totalAmount", 12.5,
                "deliveryAddress", "221B Baker Street"
        );

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("PLACED"))
                .andExpect(jsonPath("$.orderId").exists());
    }

    @Test
    void placingOrderWithNoItemsIsRejectedWithoutPublishing() throws Exception {
        Map<String, Object> body = Map.of(
                "customerId", "cust-1",
                "restaurantId", "rest-1",
                "items", List.of(),
                "totalAmount", 12.5,
                "deliveryAddress", "221B Baker Street"
        );

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }
}
