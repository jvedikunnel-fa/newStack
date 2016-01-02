package com.vedikunnel.web.rest;

import com.google.common.base.Joiner;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.vedikunnel.web.jersey.JerseyApplications;
import com.vedikunnel.web.jersey.JerseyModule;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.SubModule;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@SubModule(JerseyModule.class)
public class NewStackRestModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(NewStackRestApplication.class);
    }

    @Contribute(JerseyApplications.class)
    public static void contributeJerseyApplications(Configuration<Application> configuration, NewStackRestApplication restApplication) {
        configuration.add(restApplication);
    }

    @ApplicationPath("/rest/")
    public static class NewStackRestApplication extends PackagesResourceConfig {

        static Map<String, Object> props() {
            Map<String, Object> props = new HashMap<String, Object>();
            props.put(PackagesResourceConfig.PROPERTY_PACKAGES, "com.vedikunnel.web.rest");
            props.put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
            return props;
        }

        public NewStackRestApplication() {
            super(props());
        }

        @Override
        public Map<String, MediaType> getMediaTypeMappings() {
            Map<String, MediaType> mediaTypeMappings = super.getMediaTypeMappings();
            mediaTypeMappings.put("json", MediaType.APPLICATION_JSON_TYPE);
            mediaTypeMappings.put("html", MediaType.TEXT_HTML_TYPE);
            mediaTypeMappings.put("xml", MediaType.APPLICATION_XML_TYPE);
            mediaTypeMappings.put("js", MediaType.valueOf("text/javascript"));
            mediaTypeMappings.put("csv", MediaType.valueOf("text/csv"));
            mediaTypeMappings.put("rtf", MediaType.valueOf("application/rtf"));
            mediaTypeMappings.put("protobuf", MediaType.valueOf("application/x-protobuf"));
            return mediaTypeMappings;
        }
    }
}
