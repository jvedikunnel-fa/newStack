package com.vedikunnel.web.jersey;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by June on 02/01/2016.
 */
public class JerseyApplications {

    private Map<String, JerseyEndPoint> applications = new HashMap<String, JerseyEndPoint>();

    private static final Logger LOG = LoggerFactory.getLogger(JerseyModule.class);

    public JerseyApplications(Collection<Application> configuration,
                              @Inject TapestryRequestContext tapestryRequestContext) {
        if (configuration != null) {
            for (Application application : configuration) {
                ApplicationPath path = application.getClass().getAnnotation(ApplicationPath.class);
                verify(path.value());
                if (path == null) {
                    throw new IllegalArgumentException("You must set the ApplicationPath on all registered applications: " + application.getClass().getName());
                }
                applications.put(path.value(), new JerseyEndPoint(path.value(), application, tapestryRequestContext));
            }
        }
    }

    private void verify(String path) {
        //TODO : Check that paths are mutually exclusive
    }


    public Collection<JerseyEndPoint> getEndPoints() {
        return applications.values();
    }
}
