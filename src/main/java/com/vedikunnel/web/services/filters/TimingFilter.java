package com.vedikunnel.web.services.filters;

import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

import java.io.IOException;

/**
 *
 */
public class TimingFilter implements RequestFilter {

    private Logger log;

    public TimingFilter(Logger log) {
        this.log = log;
    }

    @Override
    public boolean service(Request request, Response response, RequestHandler handler)
            throws IOException {
        long startTime = System.currentTimeMillis();

        try {
            // The responsibility of a filter is to invoke the corresponding method
            // in the handler. When you chain multiple filters together, each filter
            // received a handler that is a bridge to the next filter.

            return handler.service(request, response);
        } finally {
            long elapsed = System.currentTimeMillis() - startTime;
            String path = request.getPath();

            if (!path.endsWith("css") &&
                !path.endsWith("js") &&
                !path.endsWith(".jpg") &&
                !path.endsWith(".woff") &&
                !path.endsWith("fetch_data_event") &&
                !path.endsWith("fetch_data_event_gv") &&
                !path.endsWith("eventcallback") &&
                !path.endsWith("progressivedisplay") &&
                !path.endsWith("gif") &&
                !path.endsWith("png")) {
                log.info(String.format("Request time: %d ms (%s %s)", elapsed, request.getMethod(), request.getPath()));
            }
        }
    }
}
