# READ ME

###### About Prometheus

prometheus is an open source system to monitor metrics about Applications, Environments, build pipe lines, data bases and many more. Prometheus by
combining with **Graphana** will provide very attractive and informative visual information about whatever we monitored. Unlike ELK stack which
was initially build as a full text searching mechanism and later extended to metrics gathering and visualization, the prometheus environment is
specifically build for continues monitoring in dev opps pipe line. Prometheus will not support centralized log management or full text searching
capabilities. Prometheus will facilitate real time monitoring with very lightweight manner without requiring much powerful hardware.         

###### About Simulation

The program will simulate simple order sending mechanism with some considerable load. We have integrated this with prometheus API to
produce metrics about the operations. Two such matrices has been gathered.

    1. oms_order_total - ever increasing counter to producerd how many orders has been recieved.
    2. oms_inprogress_orders - it will be increment at method start and decrement at method end hence simulate how many orders being processed at the moment.
    
To run this just start MainRunner from intelijIdea.

##### Maven Changes

```xml
        <dependency>
            <groupId>io.prometheus</groupId>
            <artifactId>simpleclient</artifactId>
            <version>0.9.0</version>
        </dependency>
        
        <dependency>
            <groupId>io.prometheus</groupId>
            <artifactId>simpleclient_httpserver</artifactId>
            <version>0.9.0</version>
        </dependency>
```
    
##### MetricsRegistry

This class will be acting as a central repository for metrices it will provide api methods to other class to send their matrices.
Following code line shows how we register variables with prometheus environment. Please refer to https://github.com/prometheus/client_java#http to
get more idea about Java API of prometheus.
```java
static final Counter requests = Counter.build()
            .name("oms_order_total").help("Total requests.").register();
    static final Gauge inprogressRequests = Gauge.build()
            .name("oms_inprogress_orders").help("Inprogress requests.").register();
```
Follwing code line shows a simple method to exposed to other classes.

```java
public static synchronized void incrementOrders(){
        requests.inc();
        inprogressRequests.inc();
    }

    public static synchronized void decrementOrders(){
        inprogressRequests.dec();
    }
```

##### MetricsServlet

As Prometheus is a pull based solution we need a some machanism to expose http end point to pull gathered matrices over time, Metrics Servlet is a simple such
servlet to write gathered matrices to Prometheus.

```java
@Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(TextFormat.CONTENT_TYPE_004);

        Writer writer = new BufferedWriter(resp.getWriter());
        try {
            TextFormat.write004(writer, registry.filteredMetricFamilySamples(parse(req)));
            writer.flush();
        } finally {
            writer.close();
        }
    }
```

##### MetricsServer

As this is standalone java application we need some way to expose http server, this class will start embedded jetty and then register 
MetricsServlet with jetty server and then finally exposed http end point http://localhost:3336/metrics which will called by Prometheus server 
to pull matrices.

```java
public void startMetricsServer() throws Exception {
        Server server = new Server(3336);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new MetricsServlet()),"/metrics");
        server.start();
        logger.info("Metrics Server has been started");
    }
```

##### OrderProcessor

A simple class to simulate an order, this class will call api exposed through MetricsRegistry. 

```java
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
```

    1. When calling method it will call MetricsRegistry.incrementOrders(); to increment total order count and inprogress order count by one.
    2. Then method will simulate some CPU intensive processing by calculating febonacy with in random range and some memory intensive processing by allocating some byte buffer.
    3. Finally it will call MetricsRegistry.decrementOrders(); in the end of the method to decrement inprogrss orders by one.
 
 
_**Sample Graphana Dashboard**_

![Sample graphana dashbord](http://codeblog.dotsandbrackets.com/wp-content/uploads/2017/01/grafana-dashboard.jpg)



