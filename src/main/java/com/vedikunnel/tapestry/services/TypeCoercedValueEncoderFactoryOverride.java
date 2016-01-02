package com.vedikunnel.tapestry.services;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.ValueEncoderFactory;

/**
 * Provides {@link ValueEncoder} instances that are backed by the {@link
 * TypeCoercer} service.
 */
public class TypeCoercedValueEncoderFactoryOverride implements ValueEncoderFactory<Object>
{
    private final TypeCoercer typeCoercer;

    public TypeCoercedValueEncoderFactoryOverride(TypeCoercer typeCoercer)
    {
        this.typeCoercer = typeCoercer;
    }

    public ValueEncoder<Object> create(final Class<Object> type)
    {
        final boolean blankToNull = Boolean.class.isAssignableFrom(type) || Number.class.isAssignableFrom(type);

        return new ValueEncoder<Object>()
        {
            public String toClient(Object value)
            {
                return typeCoercer.coerce(value, String.class);
            }

            public Object toValue(String clientValue)
            {
                if (blankToNull && InternalUtils.isBlank(clientValue))
                    return null;

                return typeCoercer.coerce(clientValue, type);
            }
        };
    }
}
