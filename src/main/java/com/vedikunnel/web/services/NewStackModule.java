package com.vedikunnel.web.services;

import com.vedikunnel.web.rest.NewStackRestModule;
import com.vedikunnel.web.services.filters.TimingFilter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.ServiceOverride;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.URLEncoder;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.services.meta.MetaWorker;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;

import static java.lang.Boolean.FALSE;
import static org.apache.tapestry5.SymbolConstants.APPLICATION_VERSION;
import static org.apache.tapestry5.SymbolConstants.ENCODE_LOCALE_INTO_PATH;
import static org.apache.tapestry5.SymbolConstants.SUPPORTED_LOCALES;

@SubModule(NewStackRestModule.class)
public class NewStackModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(RequestFilter.class, TimingFilter.class).withId("NewStackTimingFilter");
        binder.bind(NewStackProperties.class);
    }

    //Override URLEncoder to have control on safe characters
    @Contribute(ServiceOverride.class)
    public static void setupApplicationServiceOverrides(MappedConfiguration<Class, Object> configuration) {
        configuration.addInstance(URLEncoder.class, CustomURLEncoder.class);
    }

    //region tapestry 5.3 patch for numeric fields

    // Currently there is an issue with null values fixed in 5.4 waiting for migration...
    // @see 'https://issues.apache.org/jira/browse/TAP5-2030'

    @Contribute(ValueEncoderSource.class)
    public void contribteValueEncoderSource(MappedConfiguration<Class, Object> configuration) {
        configuration.overrideInstance(Object.class, TypeCoercedValueEncoderFactoryOverride.class);

    }

    @Contribute(ComponentClassTransformWorker2.class)
    public static void provideTransformWorkers(OrderedConfiguration<ComponentClassTransformWorker2> configuration,
                                               MetaWorker metaWorker,
                                               ComponentClassResolver resolver) {
        configuration.overrideInstance("OnEvent", OnEventWorkerOverride.class);
    }

    //endregion

    @Contribute(SymbolProvider.class)
    @ApplicationDefaults
    public void contributeApplicationDefaults(MappedConfiguration<String, String> configuration,
                                              @InjectService("NewStackProperties") NewStackProperties newStackProperties) {
        configuration.add(SymbolConstants.HMAC_PASSPHRASE, "AS87YHT543FVHSzf hifgre642 gr_è---zyugbéé b 098GTRE");
        configuration.add(SUPPORTED_LOCALES, "fr,en");
        configuration.add(ENCODE_LOCALE_INTO_PATH, FALSE.toString());
        configuration.add(APPLICATION_VERSION, newStackProperties.getVersion());
    }

    public static void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration, @InjectService("NewStackTimingFilter") RequestFilter timingFilter) {
        configuration.add("Timing", timingFilter, "after:CheckForUpdates");
    }

}
