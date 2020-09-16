package com.dfn;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderIDGenerator {

    private static AtomicInteger orderId = new AtomicInteger(0);

    public static int getOrderId(){
        return orderId.incrementAndGet();
    }

}
