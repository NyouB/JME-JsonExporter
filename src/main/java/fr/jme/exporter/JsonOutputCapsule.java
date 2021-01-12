package fr.jme.exporter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.jme3.export.JmeExporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.util.IntMap;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.IdentityHashMap;
import java.util.Map;
import org.w3c.dom.Element;

public class JsonOutputCapsule implements OutputCapsule {

  private static final String dataAttributeName = "data";
  private JsonGenerator jsonGenerator;
  private Element currentElement;
  private JmeExporter exporter;

  public JsonOutputCapsule(JsonGenerator jsonGenerator, JmeExporter exporter) {
    this.jsonGenerator = jsonGenerator;
    this.exporter = exporter;
    currentElement = null;
  }

  @Override
  public void write(byte value, String name, byte defVal) throws IOException {
    if (value == defVal) {
      return;
    }
    jsonGenerator.writeStringField(name, String.valueOf(value));
  }

  @Override
  public void write(byte[] value, String name, byte[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }

    jsonGenerator.writeStringField(name, Base64.encode(value));
  }

  @Override
  public void write(byte[][] value, String name, byte[][] defVal) throws IOException {
    if (value == null) {
      value = defVal;
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
    if (value == defVal) {
      return;
    }
    jsonGenerator.writeNumberField(name, value);
  }

  @Override
  public void write(int[] value, String name, int[] defVal) throws IOException {
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
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {

      jsonGenerator.writeArray(value[i], 0, value[i].length);

    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(float value, String name, float defVal) throws IOException {
    if (value == defVal) {
      return;
    }
    jsonGenerator.writeNumberField(name, value);
  }

  @Override
  public void write(float[] value, String name, float[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeNumber(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(float[][] value, String name, float[][] defVal) throws IOException {
    if (value == null) return;

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
    if (value == defVal) {
      return;
    }
    jsonGenerator.writeNumberField(name, value);
  }

  @Override
  public void write(double[] value, String name, double[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeNumber(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(double[][] value, String name, double[][] defVal) throws IOException {
    if (value == null) return;
    if (Arrays.deepEquals(value, defVal)) return;

    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeArray(value[i], 0, value[i].length);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(long value, String name, long defVal) throws IOException {
    if (value == defVal) {
      return;
    }
    jsonGenerator.writeNumberField(name, value);
  }

  @Override
  public void write(long[] value, String name, long[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeNumber(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(long[][] value, String name, long[][] defVal) throws IOException {
    if (value == null) return;
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeArray(value[i], 0, value[i].length);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(short value, String name, short defVal) throws IOException {
    if (value == defVal) {
      return;
    }
    jsonGenerator.writeNumberField(name, value);
  }

  @Override
  public void write(short[] value, String name, short[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeNumber(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(short[][] value, String name, short[][] defVal) throws IOException {
    if (value == null) return;

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
    if (value == defVal) {
      return;
    }
    jsonGenerator.writeBooleanField(name, value);
  }

  @Override
  public void write(boolean[] value, String name, boolean[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeBoolean(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(boolean[][] value, String name, boolean[][] defVal) throws IOException {
    if (value == null) return;
    if (Arrays.deepEquals(value, defVal)) return;

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
    if (value == null || value.equals(defVal)) {
      return;
    }
    jsonGenerator.writeStringField(name, value);
  }

  @Override
  public void write(String[] value, String name, String[] defVal) throws IOException {
    if (value == null) {
      value = defVal;
    }
    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeString(value[i]);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(String[][] value, String name, String[][] defVal) throws IOException {
    if (value == null) return;
    if (Arrays.deepEquals(value, defVal)) return;

    jsonGenerator.writeArrayFieldStart(name);
    for (int i = 0; i < value.length; i++) {
      jsonGenerator.writeArray(value[i], 0, value[i].length);
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(BitSet value, String name, BitSet defVal) throws IOException {
    if (value == null || value.equals(defVal)) {
      return;
    }
    StringBuilder buf = new StringBuilder();
    for (int i = value.nextSetBit(0); i >= 0; i = value.nextSetBit(i + 1)) {
      buf.append(i);
      buf.append(" ");
    }

    if (buf.length() > 0) {
      // remove last space
      buf.setLength(buf.length() - 1);
    }

    jsonGenerator.writeStringField(name, buf.toString());
  }

  @Override
  public void write(Savable object, String name, Savable defVal) throws IOException {
    if (object == null) {
      return;
    }
    if (object.equals(defVal)) {
      return;
    }

    String className = null;
    if (!object.getClass().getName().equals(name)) {
      className = object.getClass().getName();
    }
    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeStartArray();
    jsonGenerator.writeString(className);
    jsonGenerator.writeStartObject();
    object.write(exporter);
    jsonGenerator.writeEndObject();
    jsonGenerator.writeEndArray();
  }

  @Override
  public void write(Savable[] objects, String name, Savable[] defVal) throws IOException {
    if (objects == null) {
      return;
    }
    if (Arrays.equals(objects, defVal)) {
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
    if (value == null) return;
    if (Arrays.deepEquals(value, defVal)) return;

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
      return;
    }
    if (array.equals(defVal)) {
      return;
    }
    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeStartArray();
    for (Object o : array) {
      if (o == null) {
        continue;
      } else if (o instanceof Savable) {
        Savable s = (Savable) o;
        write(s, s.getClass().getName(), null);
      } else {
        throw new ClassCastException("Not a Savable instance: " + o);
      }
    }
    jsonGenerator.writeEndArray();
  }

  @Override
  public void writeSavableArrayListArray(ArrayList[] objects, String name, ArrayList[] defVal)
      throws IOException {
    if (objects == null) {
      return;
    }
    if (Arrays.equals(objects, defVal)) {
      return;
    }

    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeStartArray();
    for (int i = 0; i < objects.length; i++) {
      jsonGenerator.writeStartArray();
      for (Object o : objects[i]) {
        if (o == null) {
          continue;
        } else if (o instanceof Savable) {
          Savable s = (Savable) o;
          write(s, s.getClass().getName(), null);
        } else {
          throw new ClassCastException("Not a Savable instance: " + o);
        }
      }
      jsonGenerator.writeEndArray();
    }

    jsonGenerator.writeEndArray();
  }

  @Override
  public void writeSavableArrayListArray2D(ArrayList[][] value, String name, ArrayList[][] defVal)
      throws IOException {
    if (value == null) return;
    if (Arrays.deepEquals(value, defVal)) return;
/*
    Element el = appendElement(name);
    int size = value.length;
    el.setAttribute(XMLExporter.ATTRIBUTE_SIZE, String.valueOf(size));

    for (int i = 0; i < size; i++) {
      ArrayList[] vi = value[i];
      writeSavableArrayListArray(vi, "SavableArrayListArray_" + i, null);
    }
    currentElement = (Element) el.getParentNode();

 */
  }

  @Override
  public void writeFloatBufferArrayList(
      ArrayList<FloatBuffer> array, String name, ArrayList<FloatBuffer> defVal) throws IOException {
    if (array == null) {
      return;
    }
    if (array.equals(defVal)) {
      return;
    }
    /*
    Element el = appendElement(name);
    el.setAttribute(XMLExporter.ATTRIBUTE_SIZE, String.valueOf(array.size()));
    for (FloatBuffer o : array) {
      write(o, XMLExporter.ELEMENT_FLOATBUFFER, null);
    }
    currentElement = (Element) el.getParentNode();

     */
  }

  @Override
  public void writeSavableMap(
      Map<? extends Savable, ? extends Savable> map,
      String name,
      Map<? extends Savable, ? extends Savable> defVal)
      throws IOException {
    if (map == null) {
      return;
    }
    if (map.equals(defVal)) {
      return;
    }
    /*
    Element stringMap = appendElement(name);

    Iterator<? extends Savable> keyIterator = map.keySet().iterator();
    while (keyIterator.hasNext()) {
      Savable key = keyIterator.next();
      Element mapEntry = appendElement(XMLExporter.ELEMENT_MAPENTRY);
      write(key, XMLExporter.ELEMENT_KEY, null);
      Savable value = map.get(key);
      write(value, XMLExporter.ELEMENT_VALUE, null);
      currentElement = stringMap;
    }

    currentElement = (Element) stringMap.getParentNode();

     */
  }

  @Override
  public void writeStringSavableMap(
      Map<String, ? extends Savable> map, String name, Map<String, ? extends Savable> defVal)
      throws IOException {
    if (map == null) {
      return;
    }
    if (map.equals(defVal)) {
      return;
    }
    /*
    Element stringMap = appendElement(name);

    Iterator<String> keyIterator = map.keySet().iterator();
    while (keyIterator.hasNext()) {
      String key = keyIterator.next();
      Element mapEntry = appendElement("MapEntry");
      mapEntry.setAttribute("key", key);
      Savable s = map.get(key);
      write(s, "Savable", null);
      currentElement = stringMap;
    }

    currentElement = (Element) stringMap.getParentNode();

     */
  }

  @Override
  public void writeIntSavableMap(
      IntMap<? extends Savable> map, String name, IntMap<? extends Savable> defVal)
      throws IOException {
    if (map == null) {
      return;
    }
    if (map.equals(defVal)) {
      return;
    }
    jsonGenerator.writeFieldName(name);
/*
    for (Entry<? extends Savable> entry : map) {
      int key = entry.getKey();

      Element mapEntry = appendElement("MapEntry");
      mapEntry.setAttribute("key", Integer.toString(key));
      Savable s = entry.getValue();
      write(s, "Savable", null);
    }

 */

  }

  @Override
  public void write(FloatBuffer value, String name, FloatBuffer defVal) throws IOException {
    if (value == null) {
      return;
    }

    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeNumberField("size", value.limit());
    StringBuilder buf = new StringBuilder();
    int pos = value.position();
    value.rewind();
    int ctr = 0;
    while (value.hasRemaining()) {
      ctr++;
      buf.append(value.get());
      buf.append(" ");
    }
    if (ctr != value.limit()) {
      throw new IOException(
          "'"
              + name
              + "' buffer contention resulted in write data consistency.  "
              + ctr
              + " values written when should have written "
              + value.limit());
    }

    if (buf.length() > 0) {
      // remove last space
      buf.setLength(buf.length() - 1);
    }

    value.position(pos);
    jsonGenerator.writeStringField(dataAttributeName, buf.toString());
  }

  @Override
  public void write(IntBuffer value, String name, IntBuffer defVal) throws IOException {
    if (value == null) {
      return;
    }
    if (value.equals(defVal)) {
      return;
    }

    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeNumberField("size", value.limit());
    StringBuilder buf = new StringBuilder();
    int pos = value.position();
    value.rewind();
    int ctr = 0;
    while (value.hasRemaining()) {
      ctr++;
      buf.append(value.get());
      buf.append(" ");
    }
    if (ctr != value.limit()) {
      throw new IOException(
          "'"
              + name
              + "' buffer contention resulted in write data consistency.  "
              + ctr
              + " values written when should have written "
              + value.limit());
    }

    if (buf.length() > 0) {
      // remove last space
      buf.setLength(buf.length() - 1);
    }

    value.position(pos);
    jsonGenerator.writeStringField(dataAttributeName, buf.toString());
  }

  @Override
  public void write(ByteBuffer value, String name, ByteBuffer defVal) throws IOException {
    if (value == null) return;
    if (value.equals(defVal)) return;

    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeNumberField("size", value.limit());
    StringBuilder buf = new StringBuilder();
    int pos = value.position();
    value.rewind();
    int ctr = 0;
    while (value.hasRemaining()) {
      ctr++;
      buf.append(value.get());
      buf.append(" ");
    }
    if (ctr != value.limit()) {
      throw new IOException(
          "'"
              + name
              + "' buffer contention resulted in write data consistency.  "
              + ctr
              + " values written when should have written "
              + value.limit());
    }

    if (buf.length() > 0) {
      // remove last space
      buf.setLength(buf.length() - 1);
    }

    value.position(pos);
    jsonGenerator.writeStringField(dataAttributeName, buf.toString());
  }

  @Override
  public void write(ShortBuffer value, String name, ShortBuffer defVal) throws IOException {
    if (value == null) {
      return;
    }
    if (value.equals(defVal)) {
      return;
    }

    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeNumberField("size", value.limit());
    StringBuilder buf = new StringBuilder();
    int pos = value.position();
    value.rewind();
    int ctr = 0;
    while (value.hasRemaining()) {
      ctr++;
      buf.append(value.get());
      buf.append(" ");
    }
    if (ctr != value.limit()) {
      throw new IOException(
          "'"
              + name
              + "' buffer contention resulted in write data consistency.  "
              + ctr
              + " values written when should have written "
              + value.limit());
    }

    if (buf.length() > 0) {
      // remove last space
      buf.setLength(buf.length() - 1);
    }

    value.position(pos);
    jsonGenerator.writeStringField(dataAttributeName, buf.toString());
  }


  @Override
  public void write(Enum value, String name, Enum defVal) throws IOException {
    if (value == defVal) {
      return;
    }
    jsonGenerator.writeStringField(name, String.valueOf(value));
  }

  @Override
  public void writeByteBufferArrayList(
      ArrayList<ByteBuffer> array, String name, ArrayList<ByteBuffer> defVal) throws IOException {
    if (array == null) {
      return;
    }
    if (array.equals(defVal)) {
      return;
    }
    jsonGenerator.writeFieldName(name);
    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("size", array.size());
    for (ByteBuffer o : array) {
      write(o, "ByteBuffer", null);
    }
  }
}
