# JME-JsonExporter

A Json exporter to serializer deserialize jmonkey engine object using the existing interfaces

## Default Configuration
A default working ObjectMapper is provided via the ObjectMapperHelper class. It works out of the box for all Savable implementation declared
in com.jme3 package.

There is just one static method which accept 3 parameters:

> public static ObjectMapper configure(
> ObjectMapper objectMapper, List<Class> polymorphClasses, String... acceptedPackages)

If you don't provide an ObjectMapper, we create one for you. 
The second parameter is an optional list of class. If you want a the subtype of some class to be handled, you should add them to the list.
In the third parameter you can list all the package where exists some Savable implementation you want to serialize/deserialize.(The packages are scan recursively)

Some example: 
> static {
> List polymorphClass = new ArrayList();
> polymorphClass.add(EntityComponent.class);
> ObjectMapperHelper.configure(MAPPER, polymorphClass, "com.simsilica");
> MAPPER.addMixIn(Vec3d.class, Vec3dMixin.class);
> }


## Example

### Serialization

First declare a jackson Serializer or use the default provided Serializer (SavableJsonSerializer.class)

> public class SavableJsonSerializer extends JsonSerializer< Savable > {
>
> @Override
> public void serialize(Savable value, JsonGenerator gen, SerializerProvider serializers)
> throws IOException {
>
>    JsonExporter jsonExporter = new JsonExporter(gen);
> value.write(jsonExporter);
> }
>
> @Override
> public void serializeWithType(
> Savable value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
> throws IOException {
>
>    WritableTypeId typeIdDef =typeSer.typeId(value, JsonToken.START_OBJECT);
> typeSer.writeTypePrefix(gen, typeIdDef);
> serialize(value, gen, null); // call your customized serialize method
> typeSer.writeTypeSuffix(gen, typeIdDef);
>
>    }
> }

Them declare it to your ObjectMapper:

> SimpleModule module = new SimpleModule();
> module.addDeserializer(Savable.class, new SavableDeserializer());
> module.addSerializer(Savable.class, new SavableJsonSerializer());
> MAPPER.registerModule(module);
