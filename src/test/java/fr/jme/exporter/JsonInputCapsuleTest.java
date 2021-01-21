package fr.jme.exporter;

import com.jme3.asset.AssetKey;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.export.Savable;
import com.jme3.light.LightProbe;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.material.RenderState.BlendEquation;
import com.jme3.material.RenderState.BlendEquationAlpha;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.TestFunction;
import com.jme3.util.IntMap;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

  public static final DesktopAssetManager TEST_ASSET_MANAGER =
      Mockito.spy(new DesktopAssetManager());
  public static final MaterialDef TEST_MAT_DEF = new MaterialDef(TEST_ASSET_MANAGER, "matDefName");

  @Test
  void readBoolean() throws IOException {
    String json = "{\"myField\":true}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    Assertions.assertTrue(jsonInputCapsule.readBoolean("myField", false));
    Assertions.assertFalse(jsonInputCapsule.readBoolean("doestnotexists", false));
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
  void readShortArray() throws IOException {
    String json = "{\"myField\":[1,2,3,4,5,6,7,8]}";
    short[] expected = new short[]{1, 2, 3, 4, 5, 6, 7, 8};
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    short[] res = jsonInputCapsule.readShortArray("myField", null);
    Assertions.assertTrue(Arrays.equals(expected, res));
    Assertions.assertTrue(
        Arrays.equals(
            new short[]{1, 3},
            jsonInputCapsule.readShortArray("doestnotexists", new short[]{1, 3})));
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
            new String[]{"value1", "value2"},
            jsonInputCapsule.readStringArray("doestnotexists", new String[]{"value1", "value2"})));
  }

  @Test
  void readShortArray2D() throws IOException {
    String json = "{\"myField\":[[1,2,3,4],[1,2,3,4],[1,2,3,4]]}";
    short[][] expected =
        new short[][]{
            new short[]{1, 2, 3, 4}, new short[]{1, 2, 3, 4}, new short[]{1, 2, 3, 4}
        };
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    short[][] res = jsonInputCapsule.readShortArray2D("myField", null);
    for (int i = 0; i < res.length; i++) {
      Assertions.assertTrue(Arrays.equals(expected[i], res[i]));
    }
  }

  @Test
  void readBooleanArray2D() throws IOException {
    String json = "{\"myField\":[[true,true,false],[true,true,false],[true,true,false]]}";
    boolean[][] expected =
        new boolean[][]{
            new boolean[]{true, true, false},
            new boolean[]{true, true, false},
            new boolean[]{true, true, false}
        };
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    boolean[][] res = jsonInputCapsule.readBooleanArray2D("myField", null);
    for (int i = 0; i < res.length; i++) {
      Assertions.assertTrue(Arrays.equals(expected[i], res[i]));
    }
  }

  @Test
  void readStringArray2D() throws IOException {
    String json =
        "{\"myField\":[[\"value1\",\"value2\",\"value3\"],[\"value1\",\"value2\",\"value3\"]]}";
    String[][] expected =
        new String[][]{
            new String[]{"value1", "value2", "value3"}, new String[]{"value1", "value2", "value3"}
        };
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());
    String[][] res = jsonInputCapsule.readStringArray2D("myField", null);
    for (int i = 0; i < res.length; i++) {
      Assertions.assertTrue(Arrays.equals(expected[i], res[i]));
    }
  }

  @Test
  void readSavableArray() throws IOException {
    String json =
        "{\"myField\":[[\"fr.jme.exporter.TestSavable\",{\"vector3f\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"colorRGBA\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"myInt\":7890,\"myString\":\"myString\"}],[\"fr.jme.exporter.TestSavable\",{\"vector3f\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"colorRGBA\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"myInt\":7890,\"myString\":\"myString\"}]]}";
    Savable[] expected = new Savable[]{new TestSavable(), new TestSavable()};
    JsonImporter jsonImporter = new JsonImporter(new ByteArrayInputStream(json.getBytes()));
    JsonInputCapsule jsonInputCapsule = (JsonInputCapsule) jsonImporter.getCapsule(null);
    Savable[] res = jsonInputCapsule.readSavableArray("myField", null);
    Assertions.assertArrayEquals(expected, res);
  }

  @Test
  void readSavableArray2D() throws IOException {
    String json =
        "{\"myField\":[[\"fr.jme.exporter.TestSavable\",{\"vector3f\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"colorRGBA\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"myInt\":7890,\"myString\":\"myString\"}],[\"fr.jme.exporter.TestSavable\",{\"vector3f\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"colorRGBA\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"myInt\":7890,\"myString\":\"myString\"}]]}";
    Savable[] expected = new Savable[]{new TestSavable(), new TestSavable()};
    JsonImporter jsonImporter = new JsonImporter(new ByteArrayInputStream(json.getBytes()));
    JsonInputCapsule jsonInputCapsule = (JsonInputCapsule) jsonImporter.getCapsule(null);
    Savable[] res = jsonInputCapsule.readSavableArray("myField", null);
    Assertions.assertArrayEquals(expected, res);
  }

  @Test
  void readSavableArrayList() throws IOException {
    String json =
        "{\"myField\":[[\"fr.jme.exporter.TestSavable\",{\"vector3f\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"colorRGBA\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"myInt\":7890,\"myString\":\"myString\"}],[\"fr.jme.exporter.TestSavable\",{\"vector3f\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"colorRGBA\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"myInt\":7890,\"myString\":\"myString\"}]]}";
    List<Savable> expected = Arrays.asList(new Savable[]{new TestSavable(), new TestSavable()});
    JsonImporter jsonImporter = new JsonImporter(new ByteArrayInputStream(json.getBytes()));
    JsonInputCapsule jsonInputCapsule = (JsonInputCapsule) jsonImporter.getCapsule(null);
    List<Savable> res = jsonInputCapsule.readSavableArrayList("myField", null);
    for (int i = 0; i < expected.size(); i++) {
      Assertions.assertEquals(expected.get(i), res.get(i));
    }
  }

  @Test
  void readSavableArrayListArray() {
  }

  @Test
  void readSavableArrayListArray2D() {
  }

  @Test
  void readFloatBufferArrayList() throws IOException {
    String json = "{\"myField\":[[1.1,2.2,3.3],[1.1,2.2]]}";
    FloatBuffer fb1 = FloatBuffer.allocate(3);
    fb1.put(1.1f);
    fb1.put(2.2f);
    fb1.put(3.3f);
    FloatBuffer fb2 = FloatBuffer.allocate(2);
    fb2.put(1.1f);
    fb2.put(2.2f);

    ArrayList<FloatBuffer> expected = new ArrayList<>(Arrays.asList(fb1,
        fb2));
    JsonImporter jsonImporter = new JsonImporter(new ByteArrayInputStream(json.getBytes()));
    JsonInputCapsule jsonInputCapsule = (JsonInputCapsule) jsonImporter.getCapsule(null);
    List<FloatBuffer> res = jsonInputCapsule.readFloatBufferArrayList("myField", null);
    for (int i = 0; i < expected.size(); i++) {
      Assertions.assertEquals(expected.get(i), res.get(i));
    }
  }

  @Test
  void readSavableMap() {
  }

  @Test
  void readBitSet() throws IOException {
    String json = "{\"myField\":[1,2,4]}";
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
    Assertions.assertEquals(
        new BitSet(), jsonInputCapsule.readBitSet("doestnotexists", new BitSet()));
  }

  @Test
  void readSavable() throws IOException {
    String json =
        "{\"myField\":[\"com.jme3.material.Material\",{\"material_def\":\"assetName\",\"render_state\":[\"com.jme3.material.RenderState\",{\"pointSprite\":true,\"wireframe\":false,\"cullMode\":\"Back\",\"depthWrite\":true,\"depthTest\":true,\"colorWrite\":true,\"blendMode\":\"Additive\",\"offsetEnabled\":false,\"offsetFactor\":0.0,\"offsetUnits\":0.0,\"stencilTest\":false,\"frontStencilStencilFailOperation\":\"Keep\",\"frontStencilDepthFailOperation\":\"Keep\",\"frontStencilDepthPassOperation\":\"Keep\",\"frontStencilStencilFailOperation\":\"Keep\",\"backStencilDepthFailOperation\":\"Keep\",\"backStencilDepthPassOperation\":\"Keep\",\"frontStencilFunction\":\"Always\",\"backStencilFunction\":\"Always\",\"blendEquation\":\"Add\",\"blendEquationAlpha\":\"InheritColor\",\"depthFunc\":\"LessOrEqual\",\"lineWidth\":1.0,\"sfactorRGB\":\"One\",\"dfactorRGB\":\"One\",\"sfactorAlpha\":\"One\",\"dfactorAlpha\":\"One\",\"applyWireFrame\":false,\"applyCullMode\":false,\"applyDepthWrite\":false,\"applyDepthTest\":true,\"applyColorWrite\":true,\"applyBlendMode\":true,\"applyPolyOffset\":false,\"applyDepthFunc\":false,\"applyLineWidth\":false}],\"is_transparent\":false,\"parameters\":{}}]}";
    TEST_MAT_DEF.setAssetName("assetName");
    Material value = new Material(TEST_MAT_DEF);
    value.getAdditionalRenderState().setBlendEquation(BlendEquation.Add);
    value.getAdditionalRenderState().setColorWrite(true);
    value.getAdditionalRenderState().setBlendEquationAlpha(BlendEquationAlpha.InheritColor);
    value.getAdditionalRenderState().setDepthTest(true);
    value.getAdditionalRenderState().setBlendMode(BlendMode.Additive);
    Mockito.doReturn(TEST_MAT_DEF).when(TEST_ASSET_MANAGER).loadAsset(Mockito.any(AssetKey.class));
    JsonImporter importer =
        new JsonImporter(new ByteArrayInputStream(json.getBytes()), TEST_ASSET_MANAGER);
    JsonInputCapsule jsonInputCapsule = (JsonInputCapsule) importer.getCapsule(null);
    Material res = (Material) jsonInputCapsule.readSavable("myField", null);
    Assertions.assertTrue(value.contentEquals(res));
  }

  @Test
  void readStringSavableMap() throws IOException {
    String json =
        " {\"myField\":{\"key1\":[\"com.jme3.material.Material\",{\"material_def\":\"assetName\",\"render_state\":[\"com.jme3.material.RenderState\",{\"pointSprite\":true,\"wireframe\":false,\"cullMode\":\"Back\",\"depthWrite\":true,\"depthTest\":true,\"colorWrite\":true,\"blendMode\":\"Additive\",\"offsetEnabled\":false,\"offsetFactor\":0.0,\"offsetUnits\":0.0,\"stencilTest\":false,\"frontStencilStencilFailOperation\":\"Keep\",\"frontStencilDepthFailOperation\":\"Keep\",\"frontStencilDepthPassOperation\":\"Keep\",\"frontStencilStencilFailOperation\":\"Keep\",\"backStencilDepthFailOperation\":\"Keep\",\"backStencilDepthPassOperation\":\"Keep\",\"frontStencilFunction\":\"Always\",\"backStencilFunction\":\"Always\",\"blendEquation\":\"Add\",\"blendEquationAlpha\":\"InheritColor\",\"depthFunc\":\"LessOrEqual\",\"lineWidth\":1.0,\"sfactorRGB\":\"One\",\"dfactorRGB\":\"One\",\"sfactorAlpha\":\"One\",\"dfactorAlpha\":\"One\",\"applyWireFrame\":false,\"applyCullMode\":false,\"applyDepthWrite\":false,\"applyDepthTest\":true,\"applyColorWrite\":true,\"applyBlendMode\":true,\"applyPolyOffset\":false,\"applyDepthFunc\":false,\"applyLineWidth\":false}],\"is_transparent\":false,\"parameters\":{}}],\"key2\":[\"com.jme3.light.LightProbe\",{\"color\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"enabled\":true,\"position\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"area\":[\"com.jme3.light.SphereProbeArea\",{\"center\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"radius\":1.0}],\"ready\":false,\"nbMipMaps\":0}]}}";
    TEST_MAT_DEF.setAssetName("assetName");
    Material value = new Material(TEST_MAT_DEF);
    value.getAdditionalRenderState().setBlendEquation(BlendEquation.Add);
    value.getAdditionalRenderState().setColorWrite(true);
    value.getAdditionalRenderState().setBlendEquationAlpha(BlendEquationAlpha.InheritColor);
    value.getAdditionalRenderState().setDepthTest(true);
    value.getAdditionalRenderState().setBlendMode(BlendMode.Additive);
    Mockito.doReturn(TEST_MAT_DEF).when(TEST_ASSET_MANAGER).loadAsset(Mockito.any(AssetKey.class));
    JsonImporter importer =
        new JsonImporter(new ByteArrayInputStream(json.getBytes()), TEST_ASSET_MANAGER);
    JsonInputCapsule jsonInputCapsule = (JsonInputCapsule) importer.getCapsule(null);
    Map<String, ? extends Savable> res = jsonInputCapsule.readStringSavableMap("myField", null);
    Assertions.assertEquals(2, res.size());
    Assertions.assertTrue(res.containsKey("key1"));
    Assertions.assertTrue(value.contentEquals(res.get("key1")));
    Assertions.assertTrue(res.containsKey("key2"));
    Assertions.assertTrue(res.get("key2") instanceof LightProbe);
  }

  @Test
  void readIntSavableMap() throws IOException {
    String json =
        " {\"myField\":{\"1\":[\"com.jme3.material.Material\",{\"material_def\":\"assetName\",\"render_state\":[\"com.jme3.material.RenderState\",{\"pointSprite\":true,\"wireframe\":false,\"cullMode\":\"Back\",\"depthWrite\":true,\"depthTest\":true,\"colorWrite\":true,\"blendMode\":\"Additive\",\"offsetEnabled\":false,\"offsetFactor\":0.0,\"offsetUnits\":0.0,\"stencilTest\":false,\"frontStencilStencilFailOperation\":\"Keep\",\"frontStencilDepthFailOperation\":\"Keep\",\"frontStencilDepthPassOperation\":\"Keep\",\"frontStencilStencilFailOperation\":\"Keep\",\"backStencilDepthFailOperation\":\"Keep\",\"backStencilDepthPassOperation\":\"Keep\",\"frontStencilFunction\":\"Always\",\"backStencilFunction\":\"Always\",\"blendEquation\":\"Add\",\"blendEquationAlpha\":\"InheritColor\",\"depthFunc\":\"LessOrEqual\",\"lineWidth\":1.0,\"sfactorRGB\":\"One\",\"dfactorRGB\":\"One\",\"sfactorAlpha\":\"One\",\"dfactorAlpha\":\"One\",\"applyWireFrame\":false,\"applyCullMode\":false,\"applyDepthWrite\":false,\"applyDepthTest\":true,\"applyColorWrite\":true,\"applyBlendMode\":true,\"applyPolyOffset\":false,\"applyDepthFunc\":false,\"applyLineWidth\":false}],\"is_transparent\":false,\"parameters\":{}}],\"12\":[\"com.jme3.light.LightProbe\",{\"color\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"enabled\":true,\"position\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"area\":[\"com.jme3.light.SphereProbeArea\",{\"center\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"radius\":1.0}],\"ready\":false,\"nbMipMaps\":0}]}}";
    TEST_MAT_DEF.setAssetName("assetName");
    Material value = new Material(TEST_MAT_DEF);
    value.getAdditionalRenderState().setBlendEquation(BlendEquation.Add);
    value.getAdditionalRenderState().setColorWrite(true);
    value.getAdditionalRenderState().setBlendEquationAlpha(BlendEquationAlpha.InheritColor);
    value.getAdditionalRenderState().setDepthTest(true);
    value.getAdditionalRenderState().setBlendMode(BlendMode.Additive);
    Mockito.doReturn(TEST_MAT_DEF).when(TEST_ASSET_MANAGER).loadAsset(Mockito.any(AssetKey.class));
    JsonImporter importer =
        new JsonImporter(new ByteArrayInputStream(json.getBytes()), TEST_ASSET_MANAGER);
    JsonInputCapsule jsonInputCapsule = (JsonInputCapsule) importer.getCapsule(null);
    IntMap<? extends Savable> res = jsonInputCapsule.readIntSavableMap("myField", null);
    Assertions.assertEquals(2, res.size());
    Assertions.assertTrue(res.containsKey(1));
    Assertions.assertTrue(value.contentEquals(res.get(1)));
    Assertions.assertTrue(res.containsKey(12));
    Assertions.assertTrue(res.get(12) instanceof LightProbe);
  }

  @Test
  void readFloatBuffer() throws IOException {
    FloatBuffer expected = FloatBuffer.allocate(5);
    expected.put(1.5f);
    expected.put(2.2f);
    expected.put(3.3f);
    expected.put(4.4f);
    expected.put(0f);
    String json = "{\"myField\":[1.5,2.2,3.3,4.4,0]}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());

    FloatBuffer res = jsonInputCapsule.readFloatBuffer("myField", null);
    Assertions.assertEquals(expected, res);
  }

  @Test
  void readIntBuffer() throws IOException {
    IntBuffer expected = IntBuffer.allocate(5);
    expected.put(1);
    expected.put(2);
    expected.put(3);
    expected.put(4);
    expected.put(0);
    String json = "{\"myField\":[1,2,3,4,0]}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());

    IntBuffer res = jsonInputCapsule.readIntBuffer("myField", null);
    Assertions.assertEquals(expected, res);
  }

  @Test
  void readByteBuffer() throws IOException {
    ByteBuffer expected = ByteBuffer.allocate(5);
    expected.put((byte) 1);
    expected.put((byte) 2);
    expected.put((byte) 3);
    expected.put((byte) 4);
    expected.put((byte) 0);
    String json = "{\"myField\":[1,2,3,4,0]}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());

    ByteBuffer res = jsonInputCapsule.readByteBuffer("myField", null);
    Assertions.assertEquals(expected, res);
  }

  @Test
  void readShortBuffer() throws IOException {
    ShortBuffer expected = ShortBuffer.allocate(5);
    expected.put((short) 1);
    expected.put((short) 2);
    expected.put((short) 3);
    expected.put((short) 4);
    expected.put((short) 0);
    String json = "{\"myField\":[1,2,3,4,0]}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());

    ShortBuffer res = jsonInputCapsule.readShortBuffer("myField", null);
    Assertions.assertEquals(expected, res);
  }

  @Test
  void readByteBufferArrayList() {}

  @Test
  void readEnum() throws IOException {

    String json = "{\"myField\":\"" + TestFunction.Always.name() + "\"}";
    JsonInputCapsule jsonInputCapsule =
        new JsonInputCapsule(new ByteArrayInputStream(json.getBytes()), new JsonImporter());

    TestFunction res = jsonInputCapsule.readEnum("myField", TestFunction.class, null);
    Assertions.assertEquals(TestFunction.Always, res);
    Assertions.assertEquals(
        TestFunction.Equal,
        jsonInputCapsule.readEnum("doestnotexists", TestFunction.class, TestFunction.Equal));
  }
}
