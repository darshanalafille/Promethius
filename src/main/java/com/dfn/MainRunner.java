package com.dfn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class MainRunner {

    private static final Logger logger = LogManager.getLogger(MainRunner.class);
    private static Random r = new Random();

    public static void main(String[] args) {

        logger.debug("Starting Application");
        MetricsServer metricsServer = new MetricsServer();
        try {
            metricsServer.startMetricsServer();

                int orderBatchSize = 100; //getVal();
                logger.info("Starting orders with batch size {} ", orderBatchSize);
                System.out.println("Starting orders with batch size " + orderBatchSize);
                for (int i = 0; i < orderBatchSize; i++){
                    Runnable r = () -> new OrderProcessor().processOrder();
                    new Thread(r).start();
                }
                Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    static int getVal(){
        int low = 100;
        int high = 500; // to make busy use 42
        return r.nextInt(high-low) + low;
    }

}
