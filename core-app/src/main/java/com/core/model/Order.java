package com.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Order {

    private List<OrderItem> items;
    @JsonProperty("customer")
    private Customer customer;
    private String orderId;

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
