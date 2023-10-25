package com.salespage.salespageservice.domains.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;

import java.io.IOException;

@JsonSerialize(using = ObjectIdSerializer.class)
@JsonDeserialize(using = ObjectIdDeserializer.class)
public class ObjectIdSerializer extends JsonSerializer<ObjectId> {
  @Override
  public void serialize(ObjectId objectId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    if (objectId != null) {
      jsonGenerator.writeString(objectId.toHexString());
    } else {
      jsonGenerator.writeNull();
    }
  }
}