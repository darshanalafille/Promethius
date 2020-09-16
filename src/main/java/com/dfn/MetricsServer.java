package com.dfn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;



public class MetricsServer {

    private static final Logger logger = LogManager.getLogger(MetricsServer.class);

    public void startMetricsServer() throws Exception {
        Server server = new Server(3336);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new MetricsServlet()),"/metrics");
        server.start();
        logger.info("Metrics Server has been started");
    }
}
