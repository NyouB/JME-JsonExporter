package fr.jme.exporter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.export.Savable;
import com.jme3.light.LightProbe;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.material.RenderState.BlendEquation;
import com.jme3.material.RenderState.BlendEquationAlpha;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.TestFunction;
import com.jme3.math.Vector3f;
import com.jme3.scene.debug.Arrow;
import com.jme3.util.IntMap;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonOutputCapsuleTest {

  JsonOutputCapsule jsonOutputCapsule;
  JsonExporter jmeExporter;
  StringWriter stringWriter;
  JsonGenerator jGenerator;

  @BeforeEach
  void init() throws IOException {
    stringWriter = new StringWriter();
    jmeExporter = new JsonExporter(stringWriter);
    jGenerator = jmeExporter.getJsonGenerator();
    jsonOutputCapsule = jmeExporter.getJsonOutputCapsule();
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
    String[] myValues = new String[]{"str1", "str2", "str3"};
    String[] defVal = new String[]{"str1", "str2", "str3"};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    Assertions.assertEquals("{\"myField\":[\"str1\",\"str2\",\"str3\"]}", stringWriter.toString());
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
  void writeSavable() throws IOException {
    MaterialDef materialDef = new MaterialDef(new DesktopAssetManager(), "matDefName");
    materialDef.setAssetName("assetName");
    Material value = new Material(materialDef);
    value.getAdditionalRenderState().setBlendEquation(BlendEquation.Add);
    value.getAdditionalRenderState().setColorWrite(true);
    value.getAdditionalRenderState().setBlendEquationAlpha(BlendEquationAlpha.InheritColor);
    value.getAdditionalRenderState().setDepthTest(true);
    value.getAdditionalRenderState().setBlendMode(BlendMode.Additive);

    Material defVal = new Material();
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(value, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals(
        "{\"myField\":[\"com.jme3.material.Material\",{\"material_def\":\"assetName\",\"render_state\":[\"com.jme3.material.RenderState\",{\"pointSprite\":true,\"wireframe\":false,\"cullMode\":\"Back\",\"depthWrite\":true,\"depthTest\":true,\"colorWrite\":true,\"blendMode\":\"Additive\",\"offsetEnabled\":false,\"offsetFactor\":0.0,\"offsetUnits\":0.0,\"stencilTest\":false,\"frontStencilStencilFailOperation\":\"Keep\",\"frontStencilDepthFailOperation\":\"Keep\",\"frontStencilDepthPassOperation\":\"Keep\",\"frontStencilStencilFailOperation\":\"Keep\",\"backStencilDepthFailOperation\":\"Keep\",\"backStencilDepthPassOperation\":\"Keep\",\"frontStencilFunction\":\"Always\",\"backStencilFunction\":\"Always\",\"blendEquation\":\"Add\",\"blendEquationAlpha\":\"InheritColor\",\"depthFunc\":\"LessOrEqual\",\"lineWidth\":1.0,\"sfactorRGB\":\"One\",\"dfactorRGB\":\"One\",\"sfactorAlpha\":\"One\",\"dfactorAlpha\":\"One\",\"applyWireFrame\":false,\"applyCullMode\":false,\"applyDepthWrite\":false,\"applyDepthTest\":true,\"applyColorWrite\":true,\"applyBlendMode\":true,\"applyPolyOffset\":false,\"applyDepthFunc\":false,\"applyLineWidth\":false}],\"is_transparent\":false,\"parameters\":{}}]}",
        stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeSavableArray() throws IOException {
    Savable[] myValues = new Savable[]{new TestSavable(), new TestSavable()};
    Savable[] defVal = new Savable[]{};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals(
        "{\"myField\":[[\"fr.jme.exporter.TestSavable\",{\"vector3f\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"colorRGBA\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"myInt\":7890,\"myString\":\"myString\"}],[\"fr.jme.exporter.TestSavable\",{\"vector3f\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"colorRGBA\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"myInt\":7890,\"myString\":\"myString\"}]]}",
        stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeSavableArray2D() throws IOException {
    Arrow arrow = new Arrow(new Vector3f(1, 2, 3));
    Arrow arrow2 = new Arrow(new Vector3f(1, 2, 3));
    Arrow arrow3 = new Arrow(new Vector3f(1, 2, 3));
    Arrow arrow4 = new Arrow(new Vector3f(1, 2, 3));
    Arrow arrow5 = new Arrow(new Vector3f(1, 2, 3));
    Savable[][] myValues = new Savable[][]{new Savable[]{arrow, arrow2},
        new Savable[]{arrow3, arrow4}};
    Savable[][] defVal = new Savable[][]{new Savable[]{arrow5}, new Savable[]{}};

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(myValues, "myField", defVal);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals(
        "{\"myField\":[[[\"com.jme3.scene.debug.Arrow\",{\"modelBound\":[\"com.jme3.bounding.BoundingBox\",{\"center\":[\"com.jme3.math.Vector3f\",{\"x\":0.53874123,\"y\":1.0,\"z\":1.5000001}],\"xExtent\":0.53874123,\"yExtent\":1.0,\"zExtent\":1.5000001}],\"vertCount\":6,\"elementCount\":5,\"instanceCount\":1,\"max_num_weights\":-1,\"mode\":\"Lines\",\"pointSize\":1.0,\"buffers\":{\"0\":[\"com.jme3.scene.VertexBuffer\",{\"components\":3,\"usage\":\"Dynamic\",\"buffer_type\":\"Position\",\"format\":\"Float\",\"normalized\":false,\"offset\":0,\"stride\":0,\"instanceSpan\":0,\"dataFloat\":[]}],\"9\":[\"com.jme3.scene.VertexBuffer\",{\"components\":2,\"usage\":\"Dynamic\",\"buffer_type\":\"Index\",\"format\":\"UnsignedShort\",\"normalized\":false,\"offset\":0,\"stride\":0,\"instanceSpan\":0,\"dataUnsignedShort\":[]}]}}],[\"com.jme3.scene.debug.Arrow\",{\"modelBound\":[\"com.jme3.bounding.BoundingBox\",{\"center\":[\"com.jme3.math.Vector3f\",{\"x\":0.53874123,\"y\":1.0,\"z\":1.5000001}],\"xExtent\":0.53874123,\"yExtent\":1.0,\"zExtent\":1.5000001}],\"vertCount\":6,\"elementCount\":5,\"instanceCount\":1,\"max_num_weights\":-1,\"mode\":\"Lines\",\"pointSize\":1.0,\"buffers\":{\"0\":[\"com.jme3.scene.VertexBuffer\",{\"components\":3,\"usage\":\"Dynamic\",\"buffer_type\":\"Position\",\"format\":\"Float\",\"normalized\":false,\"offset\":0,\"stride\":0,\"instanceSpan\":0,\"dataFloat\":[]}],\"9\":[\"com.jme3.scene.VertexBuffer\",{\"components\":2,\"usage\":\"Dynamic\",\"buffer_type\":\"Index\",\"format\":\"UnsignedShort\",\"normalized\":false,\"offset\":0,\"stride\":0,\"instanceSpan\":0,\"dataUnsignedShort\":[]}]}}]],[[\"com.jme3.scene.debug.Arrow\",{\"modelBound\":[\"com.jme3.bounding.BoundingBox\",{\"center\":[\"com.jme3.math.Vector3f\",{\"x\":0.53874123,\"y\":1.0,\"z\":1.5000001}],\"xExtent\":0.53874123,\"yExtent\":1.0,\"zExtent\":1.5000001}],\"vertCount\":6,\"elementCount\":5,\"instanceCount\":1,\"max_num_weights\":-1,\"mode\":\"Lines\",\"pointSize\":1.0,\"buffers\":{\"0\":[\"com.jme3.scene.VertexBuffer\",{\"components\":3,\"usage\":\"Dynamic\",\"buffer_type\":\"Position\",\"format\":\"Float\",\"normalized\":false,\"offset\":0,\"stride\":0,\"instanceSpan\":0,\"dataFloat\":[]}],\"9\":[\"com.jme3.scene.VertexBuffer\",{\"components\":2,\"usage\":\"Dynamic\",\"buffer_type\":\"Index\",\"format\":\"UnsignedShort\",\"normalized\":false,\"offset\":0,\"stride\":0,\"instanceSpan\":0,\"dataUnsignedShort\":[]}]}}],[\"com.jme3.scene.debug.Arrow\",{\"modelBound\":[\"com.jme3.bounding.BoundingBox\",{\"center\":[\"com.jme3.math.Vector3f\",{\"x\":0.53874123,\"y\":1.0,\"z\":1.5000001}],\"xExtent\":0.53874123,\"yExtent\":1.0,\"zExtent\":1.5000001}],\"vertCount\":6,\"elementCount\":5,\"instanceCount\":1,\"max_num_weights\":-1,\"mode\":\"Lines\",\"pointSize\":1.0,\"buffers\":{\"0\":[\"com.jme3.scene.VertexBuffer\",{\"components\":3,\"usage\":\"Dynamic\",\"buffer_type\":\"Position\",\"format\":\"Float\",\"normalized\":false,\"offset\":0,\"stride\":0,\"instanceSpan\":0,\"dataFloat\":[]}],\"9\":[\"com.jme3.scene.VertexBuffer\",{\"components\":2,\"usage\":\"Dynamic\",\"buffer_type\":\"Index\",\"format\":\"UnsignedShort\",\"normalized\":false,\"offset\":0,\"stride\":0,\"instanceSpan\":0,\"dataUnsignedShort\":[]}]}}]]]}",
        stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeSavableArrayList() throws IOException {
    ArrayList<Savable> myValues = new ArrayList<>(Arrays.asList(new TestSavable(),
        new TestSavable()));

    jGenerator.writeStartObject();
    jsonOutputCapsule.writeSavableArrayList(myValues, "myField", null);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals(
        "{\"myField\":[[\"fr.jme.exporter.TestSavable\",{\"vector3f\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"colorRGBA\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"myInt\":7890,\"myString\":\"myString\"}],[\"fr.jme.exporter.TestSavable\",{\"vector3f\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"colorRGBA\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"myInt\":7890,\"myString\":\"myString\"}]]}",
        stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeSavableArrayListArray() {}

  @org.junit.jupiter.api.Test
  void writeSavableArrayListArray2D() {}

  @org.junit.jupiter.api.Test
  void writeFloatBufferArrayList() {}

  @org.junit.jupiter.api.Test
  void writeSavableMap() throws IOException {

    MaterialDef materialDef = new MaterialDef(new DesktopAssetManager(), "matDefName");
    materialDef.setAssetName("assetName");
    Material value = new Material(materialDef);
    value.getAdditionalRenderState().setBlendEquation(BlendEquation.Add);
    value.getAdditionalRenderState().setColorWrite(true);
    value.getAdditionalRenderState().setBlendEquationAlpha(BlendEquationAlpha.InheritColor);
    value.getAdditionalRenderState().setDepthTest(true);
    value.getAdditionalRenderState().setBlendMode(BlendMode.Additive);

    Map<String, Savable> savableMap = new HashMap<>();
    savableMap.put("key1", value);
    savableMap.put("key2", new LightProbe());
    jGenerator.writeStartObject();
    jsonOutputCapsule.writeStringSavableMap(savableMap, "myField", new HashMap<>());
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals(
        "{\"myField\":{\"key1\":[\"com.jme3.material.Material\",{\"material_def\":\"assetName\",\"render_state\":[\"com.jme3.material.RenderState\",{\"pointSprite\":true,\"wireframe\":false,\"cullMode\":\"Back\",\"depthWrite\":true,\"depthTest\":true,\"colorWrite\":true,\"blendMode\":\"Additive\",\"offsetEnabled\":false,\"offsetFactor\":0.0,\"offsetUnits\":0.0,\"stencilTest\":false,\"frontStencilStencilFailOperation\":\"Keep\",\"frontStencilDepthFailOperation\":\"Keep\",\"frontStencilDepthPassOperation\":\"Keep\",\"frontStencilStencilFailOperation\":\"Keep\",\"backStencilDepthFailOperation\":\"Keep\",\"backStencilDepthPassOperation\":\"Keep\",\"frontStencilFunction\":\"Always\",\"backStencilFunction\":\"Always\",\"blendEquation\":\"Add\",\"blendEquationAlpha\":\"InheritColor\",\"depthFunc\":\"LessOrEqual\",\"lineWidth\":1.0,\"sfactorRGB\":\"One\",\"dfactorRGB\":\"One\",\"sfactorAlpha\":\"One\",\"dfactorAlpha\":\"One\",\"applyWireFrame\":false,\"applyCullMode\":false,\"applyDepthWrite\":false,\"applyDepthTest\":true,\"applyColorWrite\":true,\"applyBlendMode\":true,\"applyPolyOffset\":false,\"applyDepthFunc\":false,\"applyLineWidth\":false}],\"is_transparent\":false,\"parameters\":{}}],\"key2\":[\"com.jme3.light.LightProbe\",{\"color\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"enabled\":true,\"position\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"area\":[\"com.jme3.light.SphereProbeArea\",{\"center\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"radius\":1.0}],\"ready\":false,\"nbMipMaps\":0}]}}",
        stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeStringSavableMap() throws IOException {

    MaterialDef materialDef = new MaterialDef(new DesktopAssetManager(), "matDefName");
    materialDef.setAssetName("assetName");
    Material value = new Material(materialDef);
    value.getAdditionalRenderState().setBlendEquation(BlendEquation.Add);
    value.getAdditionalRenderState().setColorWrite(true);
    value.getAdditionalRenderState().setBlendEquationAlpha(BlendEquationAlpha.InheritColor);
    value.getAdditionalRenderState().setDepthTest(true);
    value.getAdditionalRenderState().setBlendMode(BlendMode.Additive);

    Map<String, Savable> savableMap = new HashMap<>();
    savableMap.put("key1", value);
    savableMap.put("key2", new LightProbe());
    jGenerator.writeStartObject();
    jsonOutputCapsule.writeStringSavableMap(savableMap, "myField", new HashMap<>());
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals(
        "{\"myField\":{\"key1\":[\"com.jme3.material.Material\",{\"material_def\":\"assetName\",\"render_state\":[\"com.jme3.material.RenderState\",{\"pointSprite\":true,\"wireframe\":false,\"cullMode\":\"Back\",\"depthWrite\":true,\"depthTest\":true,\"colorWrite\":true,\"blendMode\":\"Additive\",\"offsetEnabled\":false,\"offsetFactor\":0.0,\"offsetUnits\":0.0,\"stencilTest\":false,\"frontStencilStencilFailOperation\":\"Keep\",\"frontStencilDepthFailOperation\":\"Keep\",\"frontStencilDepthPassOperation\":\"Keep\",\"frontStencilStencilFailOperation\":\"Keep\",\"backStencilDepthFailOperation\":\"Keep\",\"backStencilDepthPassOperation\":\"Keep\",\"frontStencilFunction\":\"Always\",\"backStencilFunction\":\"Always\",\"blendEquation\":\"Add\",\"blendEquationAlpha\":\"InheritColor\",\"depthFunc\":\"LessOrEqual\",\"lineWidth\":1.0,\"sfactorRGB\":\"One\",\"dfactorRGB\":\"One\",\"sfactorAlpha\":\"One\",\"dfactorAlpha\":\"One\",\"applyWireFrame\":false,\"applyCullMode\":false,\"applyDepthWrite\":false,\"applyDepthTest\":true,\"applyColorWrite\":true,\"applyBlendMode\":true,\"applyPolyOffset\":false,\"applyDepthFunc\":false,\"applyLineWidth\":false}],\"is_transparent\":false,\"parameters\":{}}],\"key2\":[\"com.jme3.light.LightProbe\",{\"color\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"enabled\":true,\"position\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"area\":[\"com.jme3.light.SphereProbeArea\",{\"center\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"radius\":1.0}],\"ready\":false,\"nbMipMaps\":0}]}}",
        stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeIntSavableMap() throws IOException {
    MaterialDef materialDef = new MaterialDef(new DesktopAssetManager(), "matDefName");
    materialDef.setAssetName("assetName");
    Material value = new Material(materialDef);
    value.getAdditionalRenderState().setBlendEquation(BlendEquation.Add);
    value.getAdditionalRenderState().setColorWrite(true);
    value.getAdditionalRenderState().setBlendEquationAlpha(BlendEquationAlpha.InheritColor);
    value.getAdditionalRenderState().setDepthTest(true);
    value.getAdditionalRenderState().setBlendMode(BlendMode.Additive);

    IntMap<Savable> savableMap = new IntMap<>();
    savableMap.put(1, value);
    savableMap.put(12, new LightProbe());
    jGenerator.writeStartObject();
    jsonOutputCapsule.writeIntSavableMap(savableMap, "myField", new IntMap<>());
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    Assertions.assertEquals(
        "{\"myField\":{\"1\":[\"com.jme3.material.Material\",{\"material_def\":\"assetName\",\"render_state\":[\"com.jme3.material.RenderState\",{\"pointSprite\":true,\"wireframe\":false,\"cullMode\":\"Back\",\"depthWrite\":true,\"depthTest\":true,\"colorWrite\":true,\"blendMode\":\"Additive\",\"offsetEnabled\":false,\"offsetFactor\":0.0,\"offsetUnits\":0.0,\"stencilTest\":false,\"frontStencilStencilFailOperation\":\"Keep\",\"frontStencilDepthFailOperation\":\"Keep\",\"frontStencilDepthPassOperation\":\"Keep\",\"frontStencilStencilFailOperation\":\"Keep\",\"backStencilDepthFailOperation\":\"Keep\",\"backStencilDepthPassOperation\":\"Keep\",\"frontStencilFunction\":\"Always\",\"backStencilFunction\":\"Always\",\"blendEquation\":\"Add\",\"blendEquationAlpha\":\"InheritColor\",\"depthFunc\":\"LessOrEqual\",\"lineWidth\":1.0,\"sfactorRGB\":\"One\",\"dfactorRGB\":\"One\",\"sfactorAlpha\":\"One\",\"dfactorAlpha\":\"One\",\"applyWireFrame\":false,\"applyCullMode\":false,\"applyDepthWrite\":false,\"applyDepthTest\":true,\"applyColorWrite\":true,\"applyBlendMode\":true,\"applyPolyOffset\":false,\"applyDepthFunc\":false,\"applyLineWidth\":false}],\"is_transparent\":false,\"parameters\":{}}],\"12\":[\"com.jme3.light.LightProbe\",{\"color\":[\"com.jme3.math.ColorRGBA\",{\"r\":1.0,\"g\":1.0,\"b\":1.0,\"a\":1.0}],\"enabled\":true,\"position\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"area\":[\"com.jme3.light.SphereProbeArea\",{\"center\":[\"com.jme3.math.Vector3f\",{\"x\":0.0,\"y\":0.0,\"z\":0.0}],\"radius\":1.0}],\"ready\":false,\"nbMipMaps\":0}]}}",
        stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeFloatBuffer() throws IOException {
    FloatBuffer buffer = FloatBuffer.allocate(5);
    buffer.put(1.5f);
    buffer.put(2f);
    buffer.put(3.3f);
    buffer.put(4.3f);
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(buffer, "myField", null);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    String json = "{\"myField\":[1.5,2.0,3.3,4.3,0.0]}";
    Assertions.assertEquals(json, stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeIntBuffer() throws IOException {
    IntBuffer buffer = IntBuffer.allocate(5);
    buffer.put(1);
    buffer.put(2);
    buffer.put(3);
    buffer.put(4);
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(buffer, "myField", null);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    String json = "{\"myField\":[1,2,3,4,0]}";
    Assertions.assertEquals(json, stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeByteBuffer() throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(5);
    buffer.put((byte) 1);
    buffer.put((byte) 2);
    buffer.put((byte) 3);
    buffer.put((byte) 4);
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(buffer, "myField", null);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    String json = "{\"myField\":[1,2,3,4,0]}";
    Assertions.assertEquals(json, stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeShortBuffer() throws IOException {
    ShortBuffer buffer = ShortBuffer.allocate(5);
    buffer.put((short) 1);
    buffer.put((short) 2);
    buffer.put((short) 3);
    buffer.put((short) 4);
    jGenerator.writeStartObject();
    jsonOutputCapsule.write(buffer, "myField", null);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    String json = "{\"myField\":[1,2,3,4,0]}";
    Assertions.assertEquals(json, stringWriter.toString());
  }

  @org.junit.jupiter.api.Test
  void writeByteBufferArrayList() {
  }

  @org.junit.jupiter.api.Test
  void writeEnum() throws IOException {

    jGenerator.writeStartObject();
    jsonOutputCapsule.write(TestFunction.Always, "myField", TestFunction.Always);
    jGenerator.writeEndObject();
    jGenerator.close();
    System.out.println(stringWriter.toString());
    String json = "{\"myField\":\"" + TestFunction.Always.name() + "\"}";
    Assertions.assertEquals(json, stringWriter.toString());
  }

  @Test
  void scratch() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    StringWriter stringWriter = new StringWriter();
    Map<Object, Object> map = new HashMap<>();
    map.put(new JLabel(), new BitSet());
    byte[] mybyte = new byte[]{3, 2, 1};
    objectMapper.writeValue(stringWriter, map);
    System.out.println(stringWriter.toString());
  }
}
