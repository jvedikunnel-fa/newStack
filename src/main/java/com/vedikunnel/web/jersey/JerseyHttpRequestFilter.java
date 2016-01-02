package com.vedikunnel.web.jersey;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.services.RequestImpl;
import org.apache.tapestry5.internal.services.ResponseImpl;
import org.apache.tapestry5.internal.services.TapestrySessionFactory;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JerseyHttpRequestFilter implements HttpServletRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JerseyModule.class);

    @Inject
    private RequestGlobals requestGlobals;

    @Inject
    private TapestrySessionFactory sessionFactory;

    @Symbol(SymbolConstants.CHARSET)
    private String applicationCharset;

    @Inject
    private JerseyApplications applications;

    @Override
    public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler)
            throws IOException {

        for (JerseyEndPoint endPoint : applications.getEndPoints()) {
            if (endPoint.accept(request.getServletPath())){
                // make the request/response available in jersey managed services.
                storeInToGlobals(request, response);
                return endPoint.service(request.getServletPath());
            }
        }

        return handler.service(request, response);

    }

    private void storeInToGlobals(HttpServletRequest request, HttpServletResponse response) {
        Request t5request = new RequestImpl(request, applicationCharset, sessionFactory);
        Response t5response = new ResponseImpl(request, response);
        requestGlobals.storeRequestResponse(t5request, t5response);
    }


}
