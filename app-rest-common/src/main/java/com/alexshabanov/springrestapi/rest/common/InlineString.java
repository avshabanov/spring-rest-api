package com.alexshabanov.springrestapi.rest.common;

import com.alexshabanov.springrestapi.domain.DomainObject;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.IOException;

/**
 * Represents string container that inlines it's representation into the one string.
 */
@JsonDeserialize(using = InlineString.Deserializer.class)
public final class InlineString extends DomainObject implements JsonSerializableWithType {
    private final String value;

    public String getValue() {
        return value;
    }

    /**
     * @param value string to wrap around
     */
    public InlineString(String value) {
        this.value = value;
    }

    @Override
    public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
            throws IOException {
        serialize(jgen, provider);
    }

    @Override
    public void serialize(JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value);
    }

    /**
     * Jackson deserializer of the hosting class.
     */
    public static final class Deserializer extends JsonDeserializer<InlineString> {
        @Override
        public InlineString deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new InlineString(jp.readValueAs(String.class));
        }
    }
}
