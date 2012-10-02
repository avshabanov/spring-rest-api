package com.alexshabanov.springrestapi.rest.common;

import com.alexshabanov.springrestapi.domain.DomainObject;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.*;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.io.IOException;

@JsonDeserialize(using = InlineInt.Deserializer.class)
public final class InlineInt extends DomainObject implements JsonSerializableWithType {
    private final int value;

    public int getValue() {
        return value;
    }

    public InlineInt(int value) {
        this.value = value;
    }

    public static InlineInt as(int value) {
        return new InlineInt(value);
    }

    @Override
    public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
            throws IOException {
        serialize(jgen, provider);
    }

    @Override
    public void serialize(JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeNumber(getValue());
    }

    /**
     * Jackson deserializer of the hosting class.
     */
    public static final class Deserializer extends JsonDeserializer<InlineInt> {
        @Override
        public InlineInt deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new InlineInt(jp.getIntValue());
        }
    }
}
