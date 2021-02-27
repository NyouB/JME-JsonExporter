package fr.exratio.jme.exporter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.jme3.export.Savable;
import java.io.IOException;

public class SavableJsonDeserializer extends JsonDeserializer<Savable> {

  @Override
  public Savable deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    return (Savable) deserializeWithType(jsonParser, deserializationContext, null);
  }

  @Override
  public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt,
      TypeDeserializer typeDeserializer) throws IOException {
    JsonNode jsonNode = ctxt.readTree(jp);
    JsonImporter jsonImporter = new JsonImporter(jsonNode);
    return jsonImporter.load();
  }


}
