package fr.jme.exporter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.jme3.export.JmeExporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.util.IntMap;
import com.jme3.util.IntMap.Entry;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;

public class JsonOutputCapsule implements OutputCapsule {

  private static final String dataAttributeName = "data";
  private final JsonGenerator jsonGenerator;
  private final JmeExporter exporter;

  public JsonOutputCapsule(JsonGenerator jsonGenerator, JmeExporter exporter) {
    this.jsonGenerator = jsonGenerator;
    this.exporter = exporter;
  }

  @Override
  public void write(byte value, String name, byte defVal) throws IOException {
    jsonGenerator.writeStringField(name, String.valueOf(value));
  }

  @Override
  public void write(byte[] value, String name, byte[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }

    jsonGenerator.writeStringField(name, Base64.encode(value));
  }

  @Override
  public void write(byte[][] value, String name, byte[][] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      byte[] array = value[i];
      jsonGenerator.writeString(Base64.encode(array));
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(int value, String name, int defVal) throws IOException {
    jsonGenerator.writeNumberField(name, value);
  }

  @Override
  public void write(int[] value, String name, int[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeArray(value, 0, value.length);
  }

  @Override
  public void write(int[][] value, String name, int[][] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {

      jsonGenerator.writeArray(value[i], 0, value[i].length);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(float value, String name, float defVal) throws IOException {
    jsonGenerator.writeNumberField(name, value);
  }

  @Override
  public void write(float[] value, String name, float[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeNumber(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(float[][] value, String name, float[][] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeStartArray();
      for (int y = 0; y < value.length; y++) {
        jsonGenerator.writeNumber(value[i][y]);
      }
      jsonGenerator.writeEndArray();
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(double value, String name, double defVal) throws IOException {
    jsonGenerator.writeNumberField(name, value);
  }

  @Override
  public void write(double[] value, String name, double[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeNumber(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(double[][] value, String name, double[][] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }

    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeArray(value[i], 0, value[i].length);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(long value, String name, long defVal) throws IOException {
    jsonGenerator.writeNumberField(name, value);
  }

  @Override
  public void write(long[] value, String name, long[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeNumber(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(long[][] value, String name, long[][] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeArray(value[i], 0, value[i].length);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(short value, String name, short defVal) throws IOException {
    jsonGenerator.writeNumberField(name, value);
  }

  @Override
  public void write(short[] value, String name, short[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeNumber(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(short[][] value, String name, short[][] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeStartArray();
      for (int y = 0; y < value[i].length; y++) {
        jsonGenerator.writeNumber(value[i][y]);
      }
      jsonGenerator.writeEndArray();
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(boolean value, String name, boolean defVal) throws IOException {
    jsonGenerator.writeBooleanField(name, value);
  }

  @Override
  public void write(boolean[] value, String name, boolean[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeBoolean(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(boolean[][] value, String name, boolean[][] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeStartArray();
      for (int y = 0; y < value[i].length; y++) {
        jsonGenerator.writeBoolean(value[i][y]);
      }
      jsonGenerator.writeEndArray();
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(String value, String name, String defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeStringField(name, value);
  }

  @Override
  public void write(String[] value, String name, String[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeString(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(String[][] value, String name, String[][] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }

    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeArray(value[i], 0, value[i].length);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(BitSet value, String name, BitSet defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = value.nextSetBit(0); i >= 0; i = value.nextSetBit(i + 1)) {
      jsonGenerator.writeNumber(i);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(Savable object, String name, Savable defVal) throws IOException {
    if (object == null) {
      object = defVal;
    }
    if (object == null) {
      return;
    }
    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeStartArray();
    jsonGenerator.writeString(object.getClass().getName());
    jsonGenerator.writeStartObject();
    object.write(exporter);
    jsonGenerator.writeEndObject();
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(Savable[] objects, String name, Savable[] defVal) throws IOException {
    if (objects == null) {
      objects = defVal;
    }
    if (objects == null) {
      return;
    }

    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeStartArray();
    for (int i = 0; i < objects.length; i++) {
      jsonGenerator.writeStartArray();
      jsonGenerator.writeString(objects[i].getClass().getName());
      jsonGenerator.writeStartObject();
      objects[i].write(exporter);
      jsonGenerator.writeEndObject();
      jsonGenerator.writeEndArray();
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(Savable[][] value, String name, Savable[][] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }

    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeStartArray();
    for (int y = 0; y < value.length; y++) {
      jsonGenerator.writeStartArray();
      for (int i = 0; i < value[y].length; i++) {
        jsonGenerator.writeStartArray();
        jsonGenerator.writeString(value[y][i].getClass().getName());
        jsonGenerator.writeStartObject();
        value[y][i].write(exporter);
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndArray();
      }
      jsonGenerator.writeEndArray();
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void writeSavableArrayList(ArrayList array, String name, ArrayList defVal)
      throws IOException {
    if (array == null) {
      array = defVal;
    }
    if (array == null) {
      return;
    }

    write(
        (Savable[]) array.toArray(new Savable[0]),
        name,
        defVal == null ? null : (Savable[]) defVal.toArray(new Savable[0]));
  }

  @Override
  public void writeSavableArrayListArray(ArrayList[] objects, String name, ArrayList[] defVal)
      throws IOException {
    if (objects == null) {
      objects = defVal;
    }
    if (objects == null) {
      return;
    }
    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeStartArray();
    for (int i = 0; i < objects.length; i++) {
      ArrayList arrayList = objects[i];
      jsonGenerator.writeStartArray();
      for (int y = 0; y < arrayList.size(); y++) {
        if (!(arrayList.get(i) instanceof Savable)) {
          continue;
        }
        jsonGenerator.writeStartArray();
        Savable savable = (Savable) arrayList.get(i);
        jsonGenerator.writeString(savable.getClass().getName());
        jsonGenerator.writeStartObject();
        savable.write(exporter);
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndArray();
      }
      jsonGenerator.writeEndArray();
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void writeSavableArrayListArray2D(ArrayList[][] objects, String name, ArrayList[][] defVal)
      throws IOException {
    if (objects == null) {
      objects = defVal;
    }
    if (objects == null) {
      return;
    }
    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeStartArray();
    for (int i = 0; i < objects.length; i++) {
      ArrayList[] arrayListArray = objects[i];
      jsonGenerator.writeStartArray();
      for (int y = 0; y < arrayListArray.length; y++) {
        ArrayList arrayList = arrayListArray[y];
        jsonGenerator.writeStartArray();
        for (int z = 0; z < arrayList.size(); z++) {

          if (!(arrayList.get(z) instanceof Savable)) {
            continue;
          }
          jsonGenerator.writeStartArray();
          Savable savable = (Savable) arrayList.get(z);
          jsonGenerator.writeString(savable.getClass().getName());
          jsonGenerator.writeStartObject();
          savable.write(exporter);
          jsonGenerator.writeEndObject();
          jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndArray();
      }
      jsonGenerator.writeEndArray();
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void writeFloatBufferArrayList(
      ArrayList<FloatBuffer> array, String name, ArrayList<FloatBuffer> defVal) throws IOException {
    if (array == null) {
      array = defVal;
    }
    if (array == null) {
      return;
    }

    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeStartArray();
    for (int i = 0; i < array.size(); i++) {
      jsonGenerator.writeStartArray();
      FloatBuffer floatBuffer = array.get(i);
      if (floatBuffer.hasArray()) {
        for (int y = 0; y < floatBuffer.array().length; y++) {
          jsonGenerator.writeNumber(floatBuffer.array()[y]);
        }
      }
      jsonGenerator.writeEndArray();
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void writeSavableMap(
      Map<? extends Savable, ? extends Savable> map,
      String name,
      Map<? extends Savable, ? extends Savable> defVal)
      throws IOException {
    if (map == null) {
      map = defVal;
    }
    if (map == null) {
      return;
    }

    jsonGenerator.writeFieldName(name);
    Iterator<? extends Map.Entry<? extends Savable, ? extends Savable>> entryIterator =
        map.entrySet().iterator();
    jsonGenerator.writeStartArray();
    while (entryIterator.hasNext()) {
      Map.Entry<? extends Savable, ? extends Savable> entry = entryIterator.next();
      Savable key = entry.getKey();
      jsonGenerator.writeString(key.getClass().getCanonicalName());
      jsonGenerator.writeStartObject();
      key.write(exporter);
      jsonGenerator.writeEndObject();
      Savable value = entry.getValue();
      jsonGenerator.writeString(value.getClass().getCanonicalName());
      jsonGenerator.writeStartObject();
      value.write(exporter);
      jsonGenerator.writeEndObject();
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void writeStringSavableMap(
      Map<String, ? extends Savable> map, String name, Map<String, ? extends Savable> defVal)
      throws IOException {
    if (map == null) {
      map = defVal;
    }
    if (map == null) {
      return;
    }
    jsonGenerator.writeObjectFieldStart(name);
    Iterator<String> keyIterator = map.keySet().iterator();
    while (keyIterator.hasNext()) {
      String key = keyIterator.next();
      write(map.get(key), key, null);
    }
    jsonGenerator.writeEndObject();
  }

  @Override
  public void writeIntSavableMap(
      IntMap<? extends Savable> map, String name, IntMap<? extends Savable> defVal)
      throws IOException {
    if (map == null) {
      map = defVal;
    }
    if (map == null) {
      return;
    }
    jsonGenerator.writeObjectFieldStart(name);
    Iterator<? extends Entry<? extends Savable>> entryIterator = map.iterator();
    while (entryIterator.hasNext()) {
      Entry<? extends Savable> entry = entryIterator.next();
      int key = entry.getKey();
      write(entry.getValue(), Integer.toString(key), null);
    }
    jsonGenerator.writeEndObject();
  }

  @Override
  public void write(FloatBuffer value, String name, FloatBuffer defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }

    jsonGenerator.writeArrayFieldStart(name);
    if (value.hasArray()) {
      for (int i = 0; i < value.array().length; i++) {
        jsonGenerator.writeNumber(value.array()[i]);
      }
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(IntBuffer value, String name, IntBuffer defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }

    jsonGenerator.writeArrayFieldStart(name);
    if (value.hasArray()) {
      for (int i = 0; i < value.array().length; i++) {
        jsonGenerator.writeNumber(value.array()[i]);
      }
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(ByteBuffer value, String name, ByteBuffer defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }

    jsonGenerator.writeArrayFieldStart(name);
    if (value.hasArray()) {
      for (int i = 0; i < value.array().length; i++) {
        jsonGenerator.writeNumber(value.array()[i]);
      }
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(ShortBuffer value, String name, ShortBuffer defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }

    jsonGenerator.writeArrayFieldStart(name);
    if (value.hasArray()) {
      for (int i = 0; i < value.array().length; i++) {
        jsonGenerator.writeNumber(value.array()[i]);
      }
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(Enum value, String name, Enum defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    if (value == null) {
      return;
    }
    jsonGenerator.writeStringField(name, String.valueOf(value));
  }

  @Override
  public void writeByteBufferArrayList(
      ArrayList<ByteBuffer> array, String name, ArrayList<ByteBuffer> defVal) throws IOException {
    if (array == null) {
      array = defVal;
    }
    if (array == null) {
      return;
    }

    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeStartArray();
    for (int i = 0; i < array.size(); i++) {
      jsonGenerator.writeStartArray();
      ByteBuffer buffer = array.get(i);
      if (buffer.hasArray()) {
        for (int y = 0; y < buffer.array().length; y++) {
          jsonGenerator.writeNumber(buffer.array()[y]);
        }
      }
      jsonGenerator.writeEndArray();
    }
    jsonGenerator.writeEndArray();
  }
}
