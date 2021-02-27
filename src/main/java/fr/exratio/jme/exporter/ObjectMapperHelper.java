package fr.exratio.jme.exporter;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator.Builder;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jme3.export.Savable;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;

public class ObjectMapperHelper {

  public static final String JME_SAVABLE_PACKAGE = "com.jme3";

  private ObjectMapperHelper() {}

  /**
   * This method help to configure (or to create if the object mapper parameter provided is null) an
   * ObjectMapper that will serialize and deserialize savable and scenes out of the box.
   *
   * @param objectMapper
   * @param polymorphClasses list of parent classes allowed to be serialized/deserialized
   * @param acceptedPackages packages containing Savable implementations
   * @return
   */
  public static ObjectMapper configure(
      ObjectMapper objectMapper, List<Class> polymorphClasses, String... acceptedPackages) {
    ObjectMapper result = objectMapper != null ? objectMapper : new ObjectMapper();
    Builder builder =
        BasicPolymorphicTypeValidator.builder()
            .allowIfSubType(Map.class)
            .allowIfSubType(List.class)
            .allowIfSubType(Savable.class);

    if (polymorphClasses != null) {
      for (Class clazz : polymorphClasses) {
        builder.allowIfSubType(clazz);
      }
    }
    PolymorphicTypeValidator ptv = builder.build();

    result.activateDefaultTyping(ptv);
    result.setVisibility(PropertyAccessor.GETTER, Visibility.PUBLIC_ONLY);
    result.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY);

    String[] scannedPackages = ArrayUtils.add(acceptedPackages, JME_SAVABLE_PACKAGE);
    ;
    try (ScanResult scanResult =
        new ClassGraph().enableClassInfo().acceptPackages(scannedPackages).scan()) {
      ClassInfoList classes = scanResult.getClassesImplementing(Savable.class.getName());
      for (ClassInfo classInfo : classes) {
        result.addMixIn(classInfo.loadClass(), SavableMixIn.class);
      }
    }
    result
        .configOverride(Map.class)
        .setInclude(Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL));
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Savable.class, new SavableJsonDeserializer());
    module.addSerializer(Savable.class, new SavableJsonSerializer());
    result.registerModule(module);
    return result;
  }
}
