package com.dfn;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

public class MetricsRegistry {

    static final Counter requests = Counter.build()
            .name("oms_order_total").help("Total requests.").register();
    static final Gauge inprogressRequests = Gauge.build()
            .name("oms_inprogress_orders").help("Inprogress requests.").register();

    public static synchronized void incrementOrders(){
        requests.inc();
        inprogressRequests.inc();
    }

    public static synchronized void decrementOrders(){
        inprogressRequests.dec();
    }

}
