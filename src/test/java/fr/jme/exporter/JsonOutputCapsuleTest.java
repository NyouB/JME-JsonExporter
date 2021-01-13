package fr.jme.exporter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.jme3.export.JmeExporter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.BitSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonOutputCapsuleTest {

  JsonOutputCapsule jsonOutputCapsule;
  JmeExporter jmeExporter;
  StringWriter stringWriter;
  JsonGenerator jGenerator;

  @BeforeEach
  void init() throws IOException {
    jmeExporter = new JsonExporter();
    stringWriter = new StringWriter();
    jGenerator = new JsonFactory().createGenerator(stringWriter);
    jsonOutputCapsule = new JsonOutputCapsule(jGenerator, jmeExporter);
  }

  @Test
  void writeByte() throws IOException {
    byte mybyte = 1;
    byte defVal = 2;
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(mybyte, "myByte", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myByte\":\"1\"}", stringWriter.toString());
  }

  @Test
  void writeByteArray() throws IOException {
    byte[] myValues = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
    byte[] defVal = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myValues", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals("{\"myValues\":\"AQIDBAUGBwg=\"}", stringWriter.toString());
  }

  @Test
  void writeByteArray2D() throws IOException {
    byte[][] myValues = new byte[][] {new byte[] {1, 2}, new byte[] {3, 4, 5}, new byte[] {6, 7, 8}};
    byte[][] defVal = new byte[][] {new byte[] {1, 2}, new byte[] {3, 4, 5}, new byte[] {6, 7, 8}};
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myValues", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myValues\":[\"AQI=\",\"AwQF\",\"BgcI\"]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeInt() throws IOException {
    int myInt = 1;
    int defVal = 2;
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myInt, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":1}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeIntArray() throws IOException {
    int[] myValues = new int[] {1, 2, 3, 4, 5, 6, 7, 8};
    int[] defVal = new int[] {1, 2, 3, 4, 5, 6, 7, 8};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals("{\"myField\":[1,2,3,4,5,6,7,8]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeIntArray2D() throws IOException {
    int[][] myValues = new int[][] {new int[] {1, 2, 3}, new int[] { -5, 6, 7, 8}};
    int[][] defVal = new int[][] {new int[] {1, 2, 3}, new int[] { -5, 6, 7, 8}};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals("{\"myField\":[[1,2,3],[-5,6,7,8]]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeFloat() throws IOException {
    float value = 1.5f;
    float defVal = 2.5f;
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(value, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":1.5}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeFloatArray() throws IOException {
    float[] myValues = new float[] {1.1f, 2.2f, 3.3f};
    float[] defVal = new float[] {3.3f, 2.2f, 3.3f};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":[1.1,2.2,3.3]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeFloatArray2D() throws IOException {
    float[][] myValues = new float[][] {new float[] {1.1f, 2.2f, 3.3f}, new float[] { -5.5f, 6.6f}};
    float[][] defVal = new float[][] {new float[] {1, 2, 3}, new float[] { -5, 6, 7, 8}};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals("{\"myField\":[[1.1,2.2],[-5.5,6.6]]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeDouble() throws IOException {
    double value = 1.5f;
    double defVal = 2;
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(value, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":1.5}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeDoubleArray() throws IOException {
    double[] myValues = new double[] {1.1, 2.2, 3.3};
    double[] defVal = new double[] {3.3, 2.2, 3.3};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":[1.1,2.2,3.3]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeDoubleArray2D() throws IOException {
    double[][] myValues = new double[][] {new double[] {1.1, 2.2, 3.3}, new double[] { -5.5, 6.6}};
    double[][] defVal = new double[][] {new double[] {1, 2, 3}, new double[] { -5, 6, 7, 8}};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals(
        "{\"myField\":[[1.1,2.2,3.3],[-5.5,6.6]]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeLong() throws IOException {
    long value = 12345;
    long defVal = 2;
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(value, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":12345}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeLongArray() throws IOException {
    long[] myValues = new long[] {1, 2, -3};
    long[] defVal = new long[] {3, 2, 3};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":[1,2,-3]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeLongArray2D() throws IOException {
    long[][] myValues = new long[][] {new long[] {1, 2, 3}, new long[] { -5, 6, 7, 8}};
    long[][] defVal = new long[][] {new long[] {1, 2, 3}, new long[] { -5, 6, 7, 8}};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals("{\"myField\":[[1,2,3],[-5,6,7,8]]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeShort() throws IOException {
    short value = 123;
    short defVal = 2;
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(value, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":123}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeShortArray() throws IOException {
    short[] myValues = new short[] {1, 2, -3};
    short[] defVal = new short[] {3, 2, 3};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":[1,2,-3]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeShortArray2D() throws IOException {
    short[][] myValues = new short[][] {new short[] {1, 2, 3}, new short[] { -5, 6, 7, 8}};
    short[][] defVal = new short[][] {new short[] {1, 2, 3}, new short[] { -5, 6, 7, 8}};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals("{\"myField\":[[1,2,3],[-5,6,7,8]]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeBoolean() throws IOException {
    boolean value = true;
    boolean defVal = false;
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(value, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":true}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeBooleanArray() throws IOException {
    boolean[] myValues = new boolean[] {true, false, true};
    boolean[] defVal = new boolean[] {true, true, true};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals("{\"myField\":[true,false,true]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeBooleanArray2D() throws IOException {
    boolean[][] myValues = new boolean[][] {new boolean[] {true, false, true}, new boolean[] { true, true, true, false}};
    boolean[][] defVal = new boolean[][] {new boolean[] {true, true, true}, new boolean[] { true, true, true, true}};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals("{\"myField\":[[true,false,true],[true,true,true,false]]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeString() throws IOException {
    String value = "myText";
    String defVal = "defaultValue";
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(value, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":\"myText\"}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeStringArray() throws IOException {
    boolean[] myValues = new boolean[] {true, false, false};
    boolean[] defVal = new boolean[] {true, true, false};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":[true,false,false]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeStringArray2D() throws IOException {
    String[][] myValues = new String[][] {new String[] {"value1", "value2"}, new String[] { "value3", "value4"}};
    String[][] defVal = new String[][] {new String[] {"value1", "value2", "value3"}, new String[] { "value3", "value4", "value5"}};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals(
        "{\"myField\":[[\"value1\",\"value2\"],[\"value3\",\"value4\"]]}",
        stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeBitset() throws IOException {
    BitSet value = new BitSet();
    value.set(0, false);
    value.set(1, true);
    value.set(2, true);
    value.set(3, false);
    value.set(4, true);
    BitSet defVal = new BitSet();
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(value, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals("{\"myField\":[1,2,4]}", stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void testWrite23() {}

  @org.junit.jupiter.api.Test
  void testWrite24() {}

  @org.junit.jupiter.api.Test
  void testWrite25() {}

  @org.junit.jupiter.api.Test
  void testWrite26() {}

  @org.junit.jupiter.api.Test
  void writeSavableArrayList() {}

  @org.junit.jupiter.api.Test
  void writeSavableArrayListArray() {}

  @org.junit.jupiter.api.Test
  void writeSavableArrayListArray2D() {}

  @org.junit.jupiter.api.Test
  void writeFloatBufferArrayList() {}

  @org.junit.jupiter.api.Test
  void writeSavableMap() {}

  @org.junit.jupiter.api.Test
  void writeStringSavableMap() {}

  @org.junit.jupiter.api.Test
  void writeIntSavableMap() {}

  @org.junit.jupiter.api.Test
  void testWrite27() {}

  @org.junit.jupiter.api.Test
  void testWrite28() {}

  @org.junit.jupiter.api.Test
  void testWrite29() {}

  @org.junit.jupiter.api.Test
  void testWrite30() {}

  @org.junit.jupiter.api.Test
  void testWrite31() {}

  @org.junit.jupiter.api.Test
  void writeByteBufferArrayList() {}
}
