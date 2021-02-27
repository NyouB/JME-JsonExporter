package fr.exratio.jme.exporter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.jme3.export.Savable;
import java.io.IOException;

public class SavableJsonSerializer extends JsonSerializer<Savable> {

  @Override
  public void serialize(Savable value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {

    JsonExporter jsonExporter = new JsonExporter(gen);
    value.write(jsonExporter);
  }

  @Override
  public void serializeWithType(
      Savable value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
      throws IOException {

    WritableTypeId typeIdDef = typeSer.typeId(value, JsonToken.START_OBJECT);
    typeSer.writeTypePrefix(gen, typeIdDef);
    serialize(value, gen, null);
    typeSer.writeTypeSuffix(gen, typeIdDef);

  }
}
