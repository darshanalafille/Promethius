package com.dfn;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class OrderProcessor {

    private static final Gson gson = new Gson();
    private static final Logger logger = LogManager.getLogger(OrderProcessor.class);
    private static Random r = new Random();

    public void processOrder() {
        long a = System.currentTimeMillis();
        for(int i = 0; i < 1000; i++){
            sendOrder();
        }

        long b = System.currentTimeMillis();
        logger.info("Thread {} Processing time {} mills.", Thread.currentThread().getName(), (b - a));
    }

    private void sendOrder(){
        logger.info("New Order has been created");
        MetricsRegistry.incrementOrders();
        long a = System.currentTimeMillis();
        int randId = getVal(10,20);
        fib(randId);
        long b = System.currentTimeMillis();
        Order order = new Order(OrderIDGenerator.getOrderId(),1,1,56.3,50);
        byte [] ar = eatMemory();
        logger.info("Order Generated {} ", gson.toJson(order));
        logger.info("**************** Fibonacy Time For {} is {} **************** ", randId, (b-a));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MetricsRegistry.decrementOrders();
    }

    static int fib(int n)
    {
        if (n <= 1)
            return n;
        return fib(n-1) + fib(n-2);
    }

    static int getVal(int l, int h){
        int low = l;
        int high = h; // to make busy use 42
        return r.nextInt(high-low) + low;
    }

    static byte[]  eatMemory(){
        byte[] arr = new byte[100000];
        return arr;
    }



}
