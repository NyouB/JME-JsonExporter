package fr.jme.exporter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jme3.export.InputCapsule;
import com.jme3.export.Savable;
import com.jme3.export.SavableClassUtil;
import com.jme3.util.IntMap;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JsonInputCapsule implements InputCapsule {

  private static final Logger logger = LoggerFactory.getLogger(JsonInputCapsule.class);
  private final JsonImporter importer;
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private JsonNode rootNode = null;
  private JsonNode currentNode = null;

  private Element currentElem;
  private final boolean isAtRoot = true;
  private final Map<String, Savable> referencedSavables = new HashMap<String, Savable>();

  private int[] classHierarchyVersions;
  private Savable savable;

  public JsonInputCapsule(InputStream stream, JsonImporter importer) throws IOException {
    rootNode = OBJECT_MAPPER.readTree(stream);
    currentNode = rootNode;
    this.importer = importer;
    if (rootNode.has("format_version")) {
      String version = rootNode.get("format_version").textValue();
      importer.formatVersion = version.equals("") ? 0 : Integer.parseInt(version);
    }
  }

  public JsonInputCapsule(String json, JsonImporter importer) throws IOException {
    rootNode = OBJECT_MAPPER.readTree(json);
    currentNode = rootNode;
    this.importer = importer;
    if (rootNode.has("format_version")) {
      String version = rootNode.get("format_version").textValue();
      importer.formatVersion = version.equals("") ? 0 : Integer.parseInt(version);
    }
  }

  public int getSavableVersion(Class<? extends Savable> desiredClass) {
    if (classHierarchyVersions != null) {
      return SavableClassUtil.getSavedSavableVersion(
          savable, desiredClass, classHierarchyVersions, importer.getFormatVersion());
    } else {
      return 0;
    }
  }

  private static String decodeString(String s) {
    if (s == null) {
      return null;
    }
    s = s.replaceAll("\\&quot;", "\"").replaceAll("\\&lt;", "<").replaceAll("\\&amp;", "&");
    return s;
  }

  private Element findFirstChildElement(Element parent) {
    Node ret = parent.getFirstChild();
    while (ret != null && (!(ret instanceof Element))) {
      ret = ret.getNextSibling();
    }
    return (Element) ret;
  }

  private Element findChildElement(Element parent, String name) {
    if (parent == null) {
      return null;
    }
    Node ret = parent.getFirstChild();
    while (ret != null && (!(ret instanceof Element) || !ret.getNodeName().equals(name))) {
      ret = ret.getNextSibling();
    }
    return (Element) ret;
  }

  private Element findNextSiblingElement(Element current) {
    Node ret = current.getNextSibling();
    while (ret != null) {
      if (ret instanceof Element) {
        return (Element) ret;
      }
      ret = ret.getNextSibling();
    }
    return null;
  }

  public byte readByte(String name, byte defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    String tmpString = currentNode.get(name).asText();
    if (tmpString == null || tmpString.length() < 1) return defVal;
    try {
      return Byte.parseByte(tmpString);
    } catch (NumberFormatException nfe) {
      IOException io = new IOException(nfe.toString(), nfe);
      throw io;
    }
  }

  public byte[] readByteArray(String name, byte[] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    String tmpString = currentNode.get(name).asText();
    if (tmpString == null || tmpString.length() < 1) {
      return defVal;
    }
    return Base64.decode(tmpString);
  }

  public byte[][] readByteArray2D(String name, byte[][] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode nodeArray = currentNode.get(name);
    if (nodeArray == null || nodeArray.size() < 1) {
      return defVal;
    }
    byte[][] res = new byte[nodeArray.size()][];
    for (int i = 0; i < nodeArray.size(); i++) {
      res[i] = Base64.decode(nodeArray.get(i).asText());
    }
    return res;
  }

  public int readInt(String name, int defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    return currentNode.get(name).asInt(defVal);
  }

  public int[] readIntArray(String name, int[] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    int[] res = new int[arrayNode.size()];
    for (int i = 0; i < arrayNode.size(); i++) {
      res[i] = arrayNode.get(i).asInt();
    }

    return res;
  }

  public int[][] readIntArray2D(String name, int[][] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    int[][] res = new int[arrayNode.size()][];
    for (int i = 0; i < arrayNode.size(); i++) {
      JsonNode node = arrayNode.get(i);
      int[] intArray = new int[node.size()];
      for (int y = 0; y < node.size(); y++) {
        intArray[y] = node.get(y).asInt();
        res[i] = intArray;
      }
    }

    return res;
  }

  public float readFloat(String name, float defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    return Float.parseFloat(currentNode.get(name).asText());
  }

  public float[] readFloatArray(String name, float[] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    float[] res = new float[arrayNode.size()];
    for (int i = 0; i < arrayNode.size(); i++) {
      res[i] = Float.parseFloat(arrayNode.get(i).asText());
    }

    return res;
  }

  public float[][] readFloatArray2D(String name, float[][] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    float[][] res = new float[arrayNode.size()][];
    for (int i = 0; i < arrayNode.size(); i++) {
      JsonNode node = arrayNode.get(i);
      float[] array = new float[node.size()];
      for (int y = 0; y < node.size(); y++) {
        array[y] = Float.parseFloat(node.get(y).asText());
        res[i] = array;
      }
    }

    return res;
  }

  public double readDouble(String name, double defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    return currentNode.get(name).asDouble(defVal);
  }

  public double[] readDoubleArray(String name, double[] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    double[] res = new double[arrayNode.size()];
    for (int i = 0; i < arrayNode.size(); i++) {
      res[i] = Double.parseDouble(arrayNode.get(i).asText());
    }

    return res;
  }

  public double[][] readDoubleArray2D(String name, double[][] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    double[][] res = new double[arrayNode.size()][];
    for (int i = 0; i < arrayNode.size(); i++) {
      JsonNode node = arrayNode.get(i);
      double[] array = new double[node.size()];
      for (int y = 0; y < node.size(); y++) {
        array[y] = Double.parseDouble(node.get(y).asText());
        res[i] = array;
      }
    }

    return res;
  }

  public long readLong(String name, long defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    return currentNode.get(name).asLong(defVal);
  }

  public long[] readLongArray(String name, long[] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    long[] res = new long[arrayNode.size()];
    for (int i = 0; i < arrayNode.size(); i++) {
      res[i] = arrayNode.get(i).asLong();
    }
    return res;
  }

  public long[][] readLongArray2D(String name, long[][] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    long[][] res = new long[arrayNode.size()][];
    for (int i = 0; i < arrayNode.size(); i++) {
      JsonNode node = arrayNode.get(i);
      long[] array = new long[node.size()];
      for (int y = 0; y < node.size(); y++) {
        array[y] = node.get(y).asInt();
        res[i] = array;
      }
    }

    return res;
  }

  public short readShort(String name, short defVal) throws IOException {
    return (short) readInt(name, defVal);
  }

  public short[] readShortArray(String name, short[] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    short[] res = new short[arrayNode.size()];
    for (int i = 0; i < arrayNode.size(); i++) {
      res[i] = (short) arrayNode.get(i).asInt();
    }
    return res;
  }

  public short[][] readShortArray2D(String name, short[][] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    short[][] res = new short[arrayNode.size()][];
    for (int i = 0; i < arrayNode.size(); i++) {
      JsonNode node = arrayNode.get(i);
      short[] array = new short[node.size()];
      for (int y = 0; y < node.size(); y++) {
        array[y] = (short) node.get(y).asInt();
        res[i] = array;
      }
    }

    return res;
  }

  public boolean readBoolean(String name, boolean defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    return currentNode.get(name).asBoolean(defVal);
  }

  public boolean[] readBooleanArray(String name, boolean[] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    boolean[] res = new boolean[arrayNode.size()];
    for (int i = 0; i < arrayNode.size(); i++) {
      res[i] = arrayNode.get(i).asBoolean();
    }
    return res;
  }

  public boolean[][] readBooleanArray2D(String name, boolean[][] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    boolean[][] res = new boolean[arrayNode.size()][];
    for (int i = 0; i < arrayNode.size(); i++) {
      JsonNode node = arrayNode.get(i);
      boolean[] array = new boolean[node.size()];
      for (int y = 0; y < node.size(); y++) {
        array[y] = node.get(y).asBoolean();
        res[i] = array;
      }
    }

    return res;
  }

  public String readString(String name, String defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    return currentNode.get(name).asText(defVal);
  }

  public String[] readStringArray(String name, String[] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    String[] res = new String[arrayNode.size()];
    for (int i = 0; i < arrayNode.size(); i++) {
      res[i] = arrayNode.get(i).asText();
    }
    return res;
  }

  public String[][] readStringArray2D(String name, String[][] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    String[][] res = new String[arrayNode.size()][];
    for (int i = 0; i < arrayNode.size(); i++) {
      JsonNode node = arrayNode.get(i);
      String[] array = new String[node.size()];
      for (int y = 0; y < node.size(); y++) {
        array[y] = node.get(y).asText();
        res[i] = array;
      }
    }

    return res;
  }

  public BitSet readBitSet(String name, BitSet defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    BitSet res = new BitSet();
    for (int i = 0; i < arrayNode.size(); i++) {
      res.set(arrayNode.get(i).asInt());
    }
    return res;
  }

  public Savable readSavable(String name, Savable defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode previousNode;
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    String className = arrayNode.get(0).asText();
    previousNode = currentNode;
    currentNode = arrayNode.get(1);
    Savable res = null;
    try {
      res = SavableClassUtil.fromName(className, null);
      res.read(importer);
    } catch (Exception e) {
      e.printStackTrace();
      res = defVal;
    } finally {
      currentNode = previousNode;
      return res;
    }
  }

  private Savable readSavableFromCurrentElem(Savable defVal) throws IOException {
    JsonNode previousNode;
    JsonNode fieldsNode = currentNode.get(1);
    String className = currentNode.get(0).asText();
    previousNode = currentNode;
    currentNode = fieldsNode;
    Savable res = null;
    try {
      res = SavableClassUtil.fromName(className, null);
      res.read(importer);
    } catch (Exception e) {
      e.printStackTrace();
      res = defVal;
    } finally {
      currentNode = previousNode;
      return res;
    }
  }

  public Savable[] readSavableArray(String name, Savable[] defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    JsonNode previousNode = currentNode;
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    Savable[] res = new Savable[arrayNode.size()];
    for (int i = 0; i < arrayNode.size(); i++) {
      currentNode = arrayNode.get(i);
      res[i] = readSavableFromCurrentElem(null);
    }
    currentNode = previousNode;
    return res;
  }

  public Savable[][] readSavableArray2D(String name, Savable[][] defVal) throws IOException {
    Savable[][] ret = defVal;
    try {
      Element tmpEl = findChildElement(currentElem, name);
      if (tmpEl == null) {
        return defVal;
      }

      int size_outer = Integer.parseInt(tmpEl.getAttribute("size_outer"));
      int size_inner = Integer.parseInt(tmpEl.getAttribute("size_outer"));

      Savable[][] tmp = new Savable[size_outer][size_inner];
      currentElem = findFirstChildElement(tmpEl);
      for (int i = 0; i < size_outer; i++) {
        for (int j = 0; j < size_inner; j++) {
          tmp[i][j] = (readSavableFromCurrentElem(null));
          if (i == size_outer - 1 && j == size_inner - 1) {
            break;
          }
          currentElem = findNextSiblingElement(currentElem);
        }
      }
      ret = tmp;
      currentElem = (Element) tmpEl.getParentNode();
      return ret;
    } catch (IOException ioe) {
      throw ioe;
    } catch (Exception e) {
      IOException io = new IOException(e.toString(), e);
      throw io;
    }
  }

  public ArrayList<Savable> readSavableArrayList(String name, ArrayList defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    JsonNode previousNode = currentNode;
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    ArrayList<Savable> res = new ArrayList<>();
    for (int i = 0; i < arrayNode.size(); i++) {
      currentNode = arrayNode.get(i);
      res.add(readSavableFromCurrentElem(null));
    }
    currentNode = previousNode;
    return res;
  }

  public ArrayList<Savable>[] readSavableArrayListArray(String name, ArrayList[] defVal)
      throws IOException {
    try {
      Element tmpEl = findChildElement(currentElem, name);
      if (tmpEl == null) {
        return defVal;
      }
      currentElem = tmpEl;

      String sizeString = tmpEl.getAttribute("size");
      int requiredSize = (sizeString.length() > 0) ? Integer.parseInt(sizeString) : -1;

      ArrayList<Savable> sal;
      List<ArrayList<Savable>> savableArrayLists = new ArrayList<ArrayList<Savable>>();
      int i = -1;
      while (true) {
        sal = readSavableArrayList("SavableArrayList_" + ++i, null);
        if (sal == null && savableArrayLists.size() >= requiredSize) break;
        savableArrayLists.add(sal);
      }

      if (requiredSize > -1 && savableArrayLists.size() != requiredSize)
        throw new IOException(
            "String array contains wrong element count.  "
                + "Specified size "
                + requiredSize
                + ", data contains "
                + savableArrayLists.size());
      currentElem = (Element) tmpEl.getParentNode();
      return savableArrayLists.toArray(new ArrayList[0]);
    } catch (IOException ioe) {
      throw ioe;
    } catch (NumberFormatException nfe) {
      IOException io = new IOException(nfe.toString(), nfe);
      throw io;
    } catch (DOMException de) {
      IOException io = new IOException(de.toString(), de);
      throw io;
    }
  }

  public ArrayList<Savable>[][] readSavableArrayListArray2D(String name, ArrayList[][] defVal)
      throws IOException {
    try {
      Element tmpEl = findChildElement(currentElem, name);
      if (tmpEl == null) {
        return defVal;
      }
      currentElem = tmpEl;
      String sizeString = tmpEl.getAttribute("size");

      ArrayList<Savable>[] arr;
      List<ArrayList<Savable>[]> sall = new ArrayList<ArrayList<Savable>[]>();
      int i = -1;
      while ((arr = readSavableArrayListArray("SavableArrayListArray_" + ++i, null)) != null)
        sall.add(arr);
      if (sizeString.length() > 0) {
        int requiredSize = Integer.parseInt(sizeString);
        if (sall.size() != requiredSize)
          throw new IOException(
              "String array contains wrong element count.  "
                  + "Specified size "
                  + requiredSize
                  + ", data contains "
                  + sall.size());
      }
      currentElem = (Element) tmpEl.getParentNode();
      return sall.toArray(new ArrayList[0][]);
    } catch (IOException ioe) {
      throw ioe;
    } catch (Exception e) {
      IOException io = new IOException(e.toString(), e);
      throw io;
    }
  }

  public ArrayList<FloatBuffer> readFloatBufferArrayList(String name, ArrayList<FloatBuffer> defVal)
      throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    ArrayList<FloatBuffer> res = new ArrayList<>();
    for (int i = 0; i < arrayNode.size(); i++) {
      JsonNode floatArrayNode = arrayNode.get(i);
      FloatBuffer floatBuffer = FloatBuffer.allocate(floatArrayNode.size());
      for (int y = 0; y < floatArrayNode.size(); y++) {
        floatBuffer.put(Float.parseFloat(floatArrayNode.get(y).asText()));
      }
      res.add(floatBuffer);
    }
    return res;
  }

  public Map<? extends Savable, ? extends Savable> readSavableMap(
      String name, Map<? extends Savable, ? extends Savable> defVal) throws IOException {
    Map<Savable, Savable> ret;
    Element tempEl;

    if (name != null) {
      tempEl = findChildElement(currentElem, name);
    } else {
      tempEl = currentElem;
    }
    ret = new HashMap<Savable, Savable>();

    NodeList nodes = tempEl.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node n = nodes.item(i);
      if (n instanceof Element && n.getNodeName().equals("MapEntry")) {
        Element elem = (Element) n;
        currentElem = elem;
      }
    }
    currentElem = (Element) tempEl.getParentNode();
    return ret;
  }

  public Map<String, ? extends Savable> readStringSavableMap(
      String name, Map<String, ? extends Savable> defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }

    JsonNode previousNode = currentNode;

    // the whole map object
    currentNode = currentNode.get(name);
    if (currentNode == null || currentNode.size() < 1) {
      currentNode = previousNode;
      return new HashMap<>();
    }
    Map<String, Savable> res = new HashMap<>();

    // each entry in a json map is a field object
    Iterator<Entry<String, JsonNode>> fields = currentNode.fields();
    while (fields.hasNext()) {
      Entry<String, JsonNode> entry = fields.next();
      // each object is serialize whith an array. The first item is the classname and the second,
      // the value
      Savable savable = readSavable(entry.getKey(), null);
      res.put(entry.getKey(), savable);
    }
    currentNode = previousNode;
    return res;
  }

  public IntMap<? extends Savable> readIntSavableMap(String name, IntMap<? extends Savable> defVal)
      throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }

    JsonNode previousNode = currentNode;

    // the whole map object
    currentNode = currentNode.get(name);
    if (currentNode == null || currentNode.size() < 1) {
      currentNode = previousNode;
      return new IntMap<>();
    }
    IntMap<Savable> res = new IntMap<>();

    // each entry in a json map is a field object
    Iterator<Entry<String, JsonNode>> fields = currentNode.fields();
    while (fields.hasNext()) {
      Entry<String, JsonNode> entry = fields.next();
      // each object is serialize whith an array. The first item is the classname and the second,
      // the value
      Savable savable = readSavable(entry.getKey(), null);
      res.put(Integer.parseInt(entry.getKey()), savable);
    }
    currentNode = previousNode;
    return res;
  }

  /** reads from currentElem if name is null */
  public FloatBuffer readFloatBuffer(String name, FloatBuffer defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    FloatBuffer res = FloatBuffer.allocate(arrayNode.size());
    for (int i = 0; i < arrayNode.size(); i++) {
      res.put(Float.parseFloat(arrayNode.get(i).asText()));
    }

    return res;
  }

  public IntBuffer readIntBuffer(String name, IntBuffer defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    IntBuffer res = IntBuffer.allocate(arrayNode.size());
    for (int i = 0; i < arrayNode.size(); i++) {
      res.put(arrayNode.get(i).asInt());
    }

    return res;
  }

  public ByteBuffer readByteBuffer(String name, ByteBuffer defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    ByteBuffer res = ByteBuffer.allocate(arrayNode.size());
    for (int i = 0; i < arrayNode.size(); i++) {
      res.put((byte) arrayNode.get(i).asInt());
    }

    return res;
  }

  public ShortBuffer readShortBuffer(String name, ShortBuffer defVal) throws IOException {
    if (!currentNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = currentNode.get(name);
    if (arrayNode == null || arrayNode.size() < 1) {
      return defVal;
    }
    ShortBuffer res = ShortBuffer.allocate(arrayNode.size());
    for (int i = 0; i < arrayNode.size(); i++) {
      res.put((short) arrayNode.get(i).asInt());
    }

    return res;
  }

  public ArrayList<ByteBuffer> readByteBufferArrayList(String name, ArrayList<ByteBuffer> defVal)
      throws IOException {
    try {
      Element tmpEl = findChildElement(currentElem, name);
      if (tmpEl == null) {
        return defVal;
      }

      String sizeString = tmpEl.getAttribute("size");
      ArrayList<ByteBuffer> tmp = new ArrayList<ByteBuffer>();
      for (currentElem = findFirstChildElement(tmpEl);
          currentElem != null;
          currentElem = findNextSiblingElement(currentElem)) {
        tmp.add(readByteBuffer(null, null));
      }
      if (sizeString.length() > 0) {
        int requiredSize = Integer.parseInt(sizeString);
        if (tmp.size() != requiredSize)
          throw new IOException(
              "Wrong number of short buffers for '"
                  + name
                  + "'.  size says "
                  + requiredSize
                  + ", data contains "
                  + tmp.size());
      }
      currentElem = (Element) tmpEl.getParentNode();
      return tmp;
    } catch (IOException ioe) {
      throw ioe;
    } catch (NumberFormatException nfe) {
      IOException io = new IOException(nfe.toString(), nfe);
      throw io;
    } catch (DOMException de) {
      IOException io = new IOException(de.toString(), de);
      throw io;
    }
  }

  public <T extends Enum<T>> T readEnum(String name, Class<T> enumType, T defVal)
      throws IOException {
    T ret = defVal;
    try {
      JsonNode node = currentNode.get(name);
      if (node != null) {
        String eVal = node.asText();
        if (eVal != null && eVal.length() > 0) {
          ret = Enum.valueOf(enumType, eVal);
        }
      }
    } catch (Exception e) {
      IOException io = new IOException(e.toString(), e);
      throw io;
    }
    return ret;
  }

  private static final String[] zeroStrings = new String[0];

  protected String[] parseTokens(String inString) {
    String[] outStrings = inString.split("\\s+");
    return (outStrings.length == 1 && outStrings[0].length() == 0) ? zeroStrings : outStrings;
  }
}
