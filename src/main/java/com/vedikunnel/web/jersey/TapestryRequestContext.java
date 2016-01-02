package com.vedikunnel.web.jersey;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.RequestGlobals;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class TapestryRequestContext {

    @Inject
    private ApplicationGlobals applicationGlobals;

    @Inject
    private RequestGlobals requestGlobals;

    public ApplicationGlobals getApplicationGlobals() {
        return applicationGlobals;
    }

    public ServletRequest getHTTPRequest() {
        return requestGlobals.getHTTPServletRequest();
    }

    public ServletResponse getHTTPResponse() {
        return requestGlobals.getHTTPServletResponse();
    }
}
