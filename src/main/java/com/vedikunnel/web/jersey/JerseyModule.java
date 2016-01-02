package com.vedikunnel.web.jersey;

import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Handler;
import java.util.logging.LogManager;

public class JerseyModule {

    static {
        // Jersey uses java.util.logging - bridge to slf4
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        SLF4JBridgeHandler.install();
    }

    private static final Logger LOG = LoggerFactory.getLogger(JerseyModule.class);

    public static void bind(ServiceBinder binder) {
        binder.bind(TapestryRequestContext.class);
        binder.bind(JerseyApplications.class);
        binder.bind(HttpServletRequestFilter.class, JerseyHttpRequestFilter.class)
                .withId("JerseyHttpServletRequestFilter");
    }

    /**
     * Added {@link JerseyHttpRequestFilter} to the very beginning of servlet filter chain.
     *
     * @param configuration
     * @param jerseyFilter
     */
    public void contributeHttpServletRequestHandler(
            OrderedConfiguration<HttpServletRequestFilter> configuration,
            @Service("JerseyHttpServletRequestFilter") HttpServletRequestFilter jerseyFilter) {
        LOG.info("Integrating Jersey as HTTP request filter.");
        configuration.add("JerseyFilter", jerseyFilter,
                "after:StoreIntoGlobals",
                "after:SecurityConfiguration",
                "after:SecurityRequestFilter",
                "before:EndOfRequest", "before:GZIP");
    }

}
