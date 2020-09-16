package com.dfn;

import java.util.UUID;

public class Order {

    private String orderId;
    private int side;
    private int type;
    private double price;
    private int qty;

    public Order(int orderId,int side, int type, double price, int qty) {
        this.side = side;
        this.type = type;
        this.price = price;
        this.qty = qty;
        this.orderId = Integer.toString(orderId);
    }

    public String getOrderId() {
        return orderId;
    }

    public int getSide() {
        return side;
    }

    public int getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }
}
