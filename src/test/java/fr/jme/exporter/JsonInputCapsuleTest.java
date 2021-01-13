package fr.jme.exporter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonInputCapsuleTest {

  @Test
  void readByte() throws IOException {
    String json = "{\"myField\":\"1\"}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    Assertions.assertEquals((byte) 1, jsonInputCapsule.readByte("myField", (byte) 2));
    Assertions.assertEquals((byte) 2, jsonInputCapsule.readByte("doestnotexists", (byte) 2));
  }

  @Test
  void readByteArray() throws IOException {
    String json = "{\"myField\":\"AQIDBAUGBwg=\"}";
    byte[] expected = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    byte[] res = jsonInputCapsule.readByteArray("myField", null);
    Assertions.assertTrue(Arrays.equals(expected, res));
    Assertions.assertTrue(
        Arrays.equals(
            new byte[] {1, 3},
            jsonInputCapsule.readByteArray("doestnotexists", new byte[] {1, 3})));
  }

  @Test
  void readByteArray2D() throws IOException {
    String json = "{\"myField\":[\"AQI=\",\"AwQF\",\"BgcI\"]}";
    byte[][] expected =
        new byte[][] {new byte[] {1, 2}, new byte[] {3, 4, 5}, new byte[] {6, 7, 8}};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    byte[][] res = jsonInputCapsule.readByteArray2D("myField", null);
    for (int i = 0; i < expected.length; i++) {
      Assertions.assertTrue(Arrays.equals(expected[i], res[i]));
    }

    Assertions.assertTrue(
        Arrays.equals(
            new byte[] {1, 3},
            jsonInputCapsule.readByteArray("doestnotexists", new byte[] {1, 3})));
  }

  @Test
  void readInt() throws IOException {
    String json = "{\"myField\":1}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    Assertions.assertEquals(1, jsonInputCapsule.readInt("myField", 2));
    Assertions.assertEquals(2, jsonInputCapsule.readInt("doestnotexists", 2));
  }

  @Test
  void readIntArray() throws IOException {
    String json = "{\"myField\":[1,2,3,4,5,6,7,8]}";
    int[] expected = new int[] {1, 2, 3, 4, 5, 6, 7, 8};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    int[] res = jsonInputCapsule.readIntArray("myField", null);
    Assertions.assertTrue(Arrays.equals(expected, res));
    Assertions.assertTrue(
        Arrays.equals(
            new int[] {1, 3}, jsonInputCapsule.readIntArray("doestnotexists", new int[] {1, 3})));
  }

  @Test
  void readIntArray2D() throws IOException {
    String json = "{\"myField\":[[1,2,3,4],[1,2,3,4],[1,2,3,4]]}";
    int[][] expected =
        new int[][] {new int[] {1, 2, 3, 4}, new int[] {1, 2, 3, 4}, new int[] {1, 2, 3, 4}};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    int[][] res = jsonInputCapsule.readIntArray2D("myField", null);
    for (int i = 0; i < res.length; i++) {
      Assertions.assertTrue(Arrays.equals(expected[i], res[i]));
    }
  }

  @Test
  void readFloat() throws IOException {
    String json = "{\"myField\":1.5}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    Assertions.assertEquals(1.5f, jsonInputCapsule.readFloat("myField", 2));
    Assertions.assertEquals(2f, jsonInputCapsule.readFloat("doestnotexists", 2));
  }

  @Test
  void readFloatArray() throws IOException {
    String json = "{\"myField\":[1.1,2.2,3.3]}";
    float[] expected = new float[] {1.1f, 2.2f, 3.3f};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    float[] res = jsonInputCapsule.readFloatArray("myField", null);
    Assertions.assertTrue(Arrays.equals(expected, res));
    Assertions.assertTrue(
        Arrays.equals(
            new float[] {1, 3},
            jsonInputCapsule.readFloatArray("doestnotexists", new float[] {1, 3})));
  }

  @Test
  void readFloatArray2D() throws IOException {
    String json = "{\"myField\":[[1.1,2.2,3.3,4.4],[1.1,2.2,3.3,4.4],[1.1,2.2]]}";
    float[][] expected =
        new float[][] {
          new float[] {1.1f, 2.2f, 3.3f, 4.4f},
          new float[] {1.1f, 2.2f, 3.3f, 4.4f},
          new float[] {1.1f, 2.2f}
        };
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    float[][] res = jsonInputCapsule.readFloatArray2D("myField", null);
    for (int i = 0; i < res.length; i++) {
      Assertions.assertTrue(Arrays.equals(expected[i], res[i]));
    }
  }

  @Test
  void readDouble() throws IOException {
    String json = "{\"myField\":1234}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    Assertions.assertEquals(1234, jsonInputCapsule.readDouble("myField", 2));
    Assertions.assertEquals(2f, jsonInputCapsule.readDouble("doestnotexists", 2));
  }

  @Test
  void readDoubleArray() throws IOException {
    String json = "{\"myField\":[1.1,2.2,3.3]}";
    double[] expected = new double[] {1.1, 2.2, 3.3};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    double[] res = jsonInputCapsule.readDoubleArray("myField", null);
    Assertions.assertTrue(Arrays.equals(expected, res));
    Assertions.assertTrue(
        Arrays.equals(
            new double[] {1, 3},
            jsonInputCapsule.readDoubleArray("doestnotexists", new double[] {1, 3})));
  }

  @Test
  void readDoubleArray2D() throws IOException {
    String json = "{\"myField\":[[1.1,2.2,3.3,4.4],[1.1,2.2,3.3,4.4],[1.1,2.2]]}";
    double[][] expected =
        new double[][] {
          new double[] {1.1d, 2.2d, 3.3d, 4.4d},
          new double[] {1.1d, 2.2d, 3.3d, 4.4d},
          new double[] {1.1d, 2.2d}
        };
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    double[][] res = jsonInputCapsule.readDoubleArray2D("myField", null);
    for (int i = 0; i < res.length; i++) {
      Assertions.assertTrue(Arrays.equals(expected[i], res[i]));
    }
  }

  @Test
  void readLong() throws IOException {
    String json = "{\"myField\":1234}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    Assertions.assertEquals(1234, jsonInputCapsule.readLong("myField", 2));
    Assertions.assertEquals(2f, jsonInputCapsule.readLong("doestnotexists", 2));
  }

  @Test
  void readLongArray() throws IOException {
    String json = "{\"myField\":[1,2,3,4,5,6,7,8]}";
    long[] expected = new long[] {1, 2, 3, 4, 5, 6, 7, 8};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    long[] res = jsonInputCapsule.readLongArray("myField", null);
    Assertions.assertTrue(Arrays.equals(expected, res));
    Assertions.assertTrue(
        Arrays.equals(
            new long[] {1, 3},
            jsonInputCapsule.readLongArray("doestnotexists", new long[] {1, 3})));
  }

  @Test
  void readLongArray2D() throws IOException {
    String json = "{\"myField\":[[1,2,3,4],[1,2,3,4],[1,2,3,4]]}";
    long[][] expected =
        new long[][] {new long[] {1, 2, 3, 4}, new long[] {1, 2, 3, 4}, new long[] {1, 2, 3, 4}};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    long[][] res = jsonInputCapsule.readLongArray2D("myField", null);
    for (int i = 0; i < res.length; i++) {
      Assertions.assertTrue(Arrays.equals(expected[i], res[i]));
    }
  }

  @Test
  void readShort() throws IOException {
    String json = "{\"myField\":1234}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    Assertions.assertEquals(1234, jsonInputCapsule.readShort("myField", (short) 2));
    Assertions.assertEquals(2f, jsonInputCapsule.readShort("doestnotexists", (short) 2));
  }

  @Test
  void readShortArray() throws IOException {
    String json = "{\"myField\":[1,2,3,4,5,6,7,8]}";
    short[] expected = new short[] {1, 2, 3, 4, 5, 6, 7, 8};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    short[] res = jsonInputCapsule.readShortArray("myField", null);
    Assertions.assertTrue(Arrays.equals(expected, res));
    Assertions.assertTrue(
        Arrays.equals(
            new short[] {1, 3},
            jsonInputCapsule.readShortArray("doestnotexists", new short[] {1, 3})));
  }

  @Test
  void readShortArray2D() throws IOException {
    String json = "{\"myField\":[[1,2,3,4],[1,2,3,4],[1,2,3,4]]}";
    short[][] expected =
        new short[][] {new short[] {1, 2, 3, 4}, new short[] {1, 2, 3, 4}, new short[] {1, 2, 3, 4}};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    short[][] res = jsonInputCapsule.readShortArray2D("myField", null);
    for (int i = 0; i < res.length; i++) {
      Assertions.assertTrue(Arrays.equals(expected[i], res[i]));
    }
  }

  @Test
  void readBoolean() throws IOException {
    String json = "{\"myField\":true}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    Assertions.assertEquals(true, jsonInputCapsule.readBoolean("myField", false));
    Assertions.assertEquals(false, jsonInputCapsule.readBoolean("doestnotexists", false));
  }

  @Test
  void readBooleanArray() throws IOException {
    String json = "{\"myField\":[true,true,false]}";
    boolean[] expected = new boolean[] {true, true, false};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    boolean[] res = jsonInputCapsule.readBooleanArray("myField", null);
    Assertions.assertTrue(Arrays.equals(expected, res));
    Assertions.assertTrue(
        Arrays.equals(
            new boolean[] {true, false},
            jsonInputCapsule.readBooleanArray("doestnotexists", new boolean[] {true, false})));
  }

  @Test
  void readBooleanArray2D() throws IOException {
    String json = "{\"myField\":[[true,true,false],[true,true,false],[true,true,false]]}";
    boolean[][] expected =
        new boolean[][] {new boolean[] {true,true,false}, new boolean[] {true,true,false}, new boolean[] {true,true,false}};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    boolean[][] res = jsonInputCapsule.readBooleanArray2D("myField", null);
    for (int i = 0; i < res.length; i++) {
      Assertions.assertTrue(Arrays.equals(expected[i], res[i]));
    }
  }

  @Test
  void readString() throws IOException {
    String json = "{\"myField\":\"myText\"}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    Assertions.assertEquals("myText", jsonInputCapsule.readString("myField", "def"));
    Assertions.assertEquals("defValue", jsonInputCapsule.readString("doestnotexists", "defValue"));
  }

  @Test
  void readStringArray() throws IOException {
    String json = "{\"myField\":[\"value1\",\"value2\",\"value3\"]}";
    String[] expected = new String[] {"value1", "value2", "value3"};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    String[] res = jsonInputCapsule.readStringArray("myField", null);
    Assertions.assertTrue(Arrays.equals(expected, res));
    Assertions.assertTrue(
        Arrays.equals(
            new String[] {"value1", "value2"},
            jsonInputCapsule.readStringArray("doestnotexists", new String[] {"value1", "value2"})));
  }

  @Test
  void readStringArray2D() throws IOException {
    String json =
        "{\"myField\":[[\"value1\",\"value2\",\"value3\"],[\"value1\",\"value2\",\"value3\"]]}";
    String[][] expected =
        new String[][] {new String[] {"value1", "value2", "value3"}, new String[] {"value1", "value2", "value3"}};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    String[][] res = jsonInputCapsule.readStringArray2D("myField", null);
    for (int i = 0; i < res.length; i++) {
      Assertions.assertTrue(Arrays.equals(expected[i], res[i]));
    }
  }

  @Test
  void readBitSet() throws IOException {
    String json ="{\"myField\":[1,2,4]}";
    BitSet expected = new BitSet();
    expected.set(0, false);
    expected.set(1, true);
    expected.set(2, true);
    expected.set(3, false);
    expected.set(4, true);
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    BitSet res = jsonInputCapsule.readBitSet("myField", null);
    Assertions.assertEquals(expected, res);
    Assertions.assertEquals(new BitSet(), jsonInputCapsule.readBitSet("doestnotexists", new BitSet()));
  }

  @Test
  void readSavable() {}

  @Test
  void readSavableArray() {}

  @Test
  void readSavableArray2D() {}

  @Test
  void readSavableArrayList() {}

  @Test
  void readSavableArrayListArray() {}

  @Test
  void readSavableArrayListArray2D() {}

  @Test
  void readFloatBufferArrayList() {}

  @Test
  void readSavableMap() {}

  @Test
  void readStringSavableMap() {}

  @Test
  void readIntSavableMap() {}

  @Test
  void readFloatBuffer() {}

  @Test
  void readIntBuffer() {}

  @Test
  void readByteBuffer() {}

  @Test
  void readShortBuffer() {}

  @Test
  void readByteBufferArrayList() {}

  @Test
  void readEnum() {}
}
