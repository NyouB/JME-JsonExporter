package fr.jme.exporter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jme3.export.InputCapsule;
import com.jme3.export.Savable;
import com.jme3.export.SavableClassUtil;
import com.jme3.util.BufferUtils;
import com.jme3.util.IntMap;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JsonInputCapsule implements InputCapsule {
  private static final Logger logger = LoggerFactory.getLogger(JsonInputCapsule.class);
  private final JsonImporter importer;
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private JsonNode rootNode = null;

  private Document doc;
  private Element currentElem;
  private boolean isAtRoot = true;
  private Map<String, Savable> referencedSavables = new HashMap<String, Savable>();

  private int[] classHierarchyVersions;
  private Savable savable;

  public JsonInputCapsule(InputStream stream, JsonImporter importer) throws IOException {
    rootNode = OBJECT_MAPPER.readTree(stream);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    String tmpString = rootNode.get(name).asText();
    if (tmpString == null || tmpString.length() < 1) return defVal;
    try {
      return Byte.parseByte(tmpString);
    } catch (NumberFormatException nfe) {
      IOException io = new IOException(nfe.toString());
      io.initCause(nfe);
      throw io;
    }
  }

  public byte[] readByteArray(String name, byte[] defVal) throws IOException {
    if (!rootNode.has(name)) {
      return defVal;
    }
    String tmpString = rootNode.get(name).asText();
    if (tmpString == null || tmpString.length() < 1) {
      return defVal;
    }
    return Base64.decode(tmpString);
  }

  public byte[][] readByteArray2D(String name, byte[][] defVal) throws IOException {
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode nodeArray = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    return rootNode.get(name).asInt(defVal);
  }

  public int[] readIntArray(String name, int[] defVal) throws IOException {
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    return Float.parseFloat(rootNode.get(name).asText());
  }

  public float[] readFloatArray(String name, float[] defVal) throws IOException {
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    return rootNode.get(name).asDouble(defVal);
  }

  public double[] readDoubleArray(String name, double[] defVal) throws IOException {
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    return rootNode.get(name).asLong(defVal);
  }

  public long[] readLongArray(String name, long[] defVal) throws IOException {
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    return rootNode.get(name).asBoolean(defVal);
  }

  public boolean[] readBooleanArray(String name, boolean[] defVal) throws IOException {
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    return rootNode.get(name).asText(defVal);
  }

  public String[] readStringArray(String name, String[] defVal) throws IOException {
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    if (!rootNode.has(name)) {
      return defVal;
    }
    JsonNode arrayNode = rootNode.get(name);
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
    Savable ret = defVal;
    if (name != null && name.equals("")) logger.warn("Reading Savable String with name \"\"?");
    try {
      Element tmpEl = null;
      if (name != null) {
        tmpEl = findChildElement(currentElem, name);
        if (tmpEl == null) {
          return defVal;
        }
      } else if (isAtRoot) {
        tmpEl = doc.getDocumentElement();
        isAtRoot = false;
      } else {
        tmpEl = findFirstChildElement(currentElem);
      }
      currentElem = tmpEl;
      ret = readSavableFromCurrentElem(defVal);
      if (currentElem.getParentNode() instanceof Element) {
        currentElem = (Element) currentElem.getParentNode();
      } else {
        currentElem = null;
      }
    } catch (IOException ioe) {
      throw ioe;
    } catch (Exception e) {
      IOException io = new IOException(e.toString());
      io.initCause(e);
      throw io;
    }
    return ret;
  }

  private Savable readSavableFromCurrentElem(Savable defVal)
      throws InstantiationException, ClassNotFoundException, NoSuchMethodException,
          InvocationTargetException, IOException, IllegalAccessException {
    Savable ret = defVal;
    Savable tmp = null;

    if (currentElem == null || currentElem.getNodeName().equals("null")) {
      return null;
    }
    String reference = currentElem.getAttribute("ref");
    if (reference.length() > 0) {
      ret = referencedSavables.get(reference);
    } else {
      String className = currentElem.getNodeName();
      if (currentElem.hasAttribute("class")) {
        className = currentElem.getAttribute("class");
      } else if (defVal != null) {
        className = defVal.getClass().getName();
      }
      tmp = SavableClassUtil.fromName(className, null);

      String versionsStr = currentElem.getAttribute("savable_versions");
      if (versionsStr != null && !versionsStr.equals("")) {
        String[] versionStr = versionsStr.split(",");
        classHierarchyVersions = new int[versionStr.length];
        for (int i = 0; i < classHierarchyVersions.length; i++) {
          classHierarchyVersions[i] = Integer.parseInt(versionStr[i].trim());
        }
      } else {
        classHierarchyVersions = null;
      }

      String refID = currentElem.getAttribute("reference_ID");
      if (refID.length() < 1) refID = currentElem.getAttribute("id");
      if (refID.length() > 0) referencedSavables.put(refID, tmp);
      if (tmp != null) {
        // Allows reading versions from this savable
        savable = tmp;
        tmp.read(importer);
        ret = tmp;
      }
    }
    return ret;
  }

  public Savable[] readSavableArray(String name, Savable[] defVal) throws IOException {
    Savable[] ret = defVal;
    try {
      Element tmpEl = findChildElement(currentElem, name);
      if (tmpEl == null) {
        return defVal;
      }

      String sizeString = tmpEl.getAttribute("size");
      List<Savable> savables = new ArrayList<Savable>();
      for (currentElem = findFirstChildElement(tmpEl);
          currentElem != null;
          currentElem = findNextSiblingElement(currentElem)) {
        savables.add(readSavableFromCurrentElem(null));
      }
      if (sizeString.length() > 0) {
        int requiredSize = Integer.parseInt(sizeString);
        if (savables.size() != requiredSize)
          throw new IOException(
              "Wrong number of Savables for '"
                  + name
                  + "'.  size says "
                  + requiredSize
                  + ", data contains "
                  + savables.size());
      }
      ret = savables.toArray(new Savable[0]);
      currentElem = (Element) tmpEl.getParentNode();
      return ret;
    } catch (IOException ioe) {
      throw ioe;
    } catch (Exception e) {
      IOException io = new IOException(e.toString());
      io.initCause(e);
      throw io;
    }
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
      IOException io = new IOException(e.toString());
      io.initCause(e);
      throw io;
    }
  }

  public ArrayList<Savable> readSavableArrayList(String name, ArrayList defVal) throws IOException {
    try {
      Element tmpEl = findChildElement(currentElem, name);
      if (tmpEl == null) {
        return defVal;
      }

      String sizeString = tmpEl.getAttribute("size");
      ArrayList<Savable> savables = new ArrayList<Savable>();
      for (currentElem = findFirstChildElement(tmpEl);
          currentElem != null;
          currentElem = findNextSiblingElement(currentElem)) {
        savables.add(readSavableFromCurrentElem(null));
      }
      if (sizeString.length() > 0) {
        int requiredSize = Integer.parseInt(sizeString);
        if (savables.size() != requiredSize)
          throw new IOException(
              "Wrong number of Savable arrays for '"
                  + name
                  + "'.  size says "
                  + requiredSize
                  + ", data contains "
                  + savables.size());
      }
      currentElem = (Element) tmpEl.getParentNode();
      return savables;
    } catch (IOException ioe) {
      throw ioe;
    } catch (Exception e) {
      IOException io = new IOException(e.toString());
      io.initCause(e);
      throw io;
    }
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
      IOException io = new IOException(nfe.toString());
      io.initCause(nfe);
      throw io;
    } catch (DOMException de) {
      IOException io = new IOException(de.toString());
      io.initCause(de);
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
      IOException io = new IOException(e.toString());
      io.initCause(e);
      throw io;
    }
  }

  public ArrayList<FloatBuffer> readFloatBufferArrayList(String name, ArrayList<FloatBuffer> defVal)
      throws IOException {
    try {
      Element tmpEl = findChildElement(currentElem, name);
      if (tmpEl == null) {
        return defVal;
      }

      String sizeString = tmpEl.getAttribute("size");
      ArrayList<FloatBuffer> tmp = new ArrayList<FloatBuffer>();
      for (currentElem = findFirstChildElement(tmpEl);
          currentElem != null;
          currentElem = findNextSiblingElement(currentElem)) {
        tmp.add(readFloatBuffer(null, null));
      }
      if (sizeString.length() > 0) {
        int requiredSize = Integer.parseInt(sizeString);
        if (tmp.size() != requiredSize)
          throw new IOException(
              "String array contains wrong element count.  "
                  + "Specified size "
                  + requiredSize
                  + ", data contains "
                  + tmp.size());
      }
      currentElem = (Element) tmpEl.getParentNode();
      return tmp;
    } catch (IOException ioe) {
      throw ioe;
    } catch (NumberFormatException nfe) {
      IOException io = new IOException(nfe.toString());
      io.initCause(nfe);
      throw io;
    } catch (DOMException de) {
      IOException io = new IOException(de.toString());
      io.initCause(de);
      throw io;
    }
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
    Map<String, Savable> ret = null;
    Element tempEl;

    if (name != null) {
      tempEl = findChildElement(currentElem, name);
    } else {
      tempEl = currentElem;
    }
    if (tempEl != null) {
      ret = new HashMap<String, Savable>();

      NodeList nodes = tempEl.getChildNodes();
      for (int i = 0; i < nodes.getLength(); i++) {
        Node n = nodes.item(i);
        if (n instanceof Element && n.getNodeName().equals("MapEntry")) {
          Element elem = (Element) n;
          currentElem = elem;
          String key = currentElem.getAttribute("key");
          Savable val = readSavable("Savable", null);
          ret.put(key, val);
        }
      }
    } else {
      return defVal;
    }
    currentElem = (Element) tempEl.getParentNode();
    return ret;
  }

  public IntMap<? extends Savable> readIntSavableMap(String name, IntMap<? extends Savable> defVal)
      throws IOException {
    IntMap<Savable> ret = null;
    Element tempEl;

    if (name != null) {
      tempEl = findChildElement(currentElem, name);
    } else {
      tempEl = currentElem;
    }
    if (tempEl != null) {
      ret = new IntMap<Savable>();

      NodeList nodes = tempEl.getChildNodes();
      for (int i = 0; i < nodes.getLength(); i++) {
        Node n = nodes.item(i);
        if (n instanceof Element && n.getNodeName().equals("MapEntry")) {
          Element elem = (Element) n;
          currentElem = elem;
          int key = Integer.parseInt(currentElem.getAttribute("key"));
          Savable val = readSavable("Savable", null);
          ret.put(key, val);
        }
      }
    } else {
      return defVal;
    }
    currentElem = (Element) tempEl.getParentNode();
    return ret;
  }

  /** reads from currentElem if name is null */
  public FloatBuffer readFloatBuffer(String name, FloatBuffer defVal) throws IOException {
    try {
      Element tmpEl;
      if (name != null) {
        tmpEl = findChildElement(currentElem, name);
      } else {
        tmpEl = currentElem;
      }
      if (tmpEl == null) {
        return defVal;
      }
      String sizeString = tmpEl.getAttribute("size");
      String[] strings = parseTokens(tmpEl.getAttribute("data"));
      if (sizeString.length() > 0) {
        int requiredSize = Integer.parseInt(sizeString);
        if (strings.length != requiredSize)
          throw new IOException(
              "Wrong number of float buffers for '"
                  + name
                  + "'.  size says "
                  + requiredSize
                  + ", data contains "
                  + strings.length);
      }
      FloatBuffer tmp = BufferUtils.createFloatBuffer(strings.length);
      for (String s : strings) tmp.put(Float.parseFloat(s));
      tmp.flip();
      return tmp;
    } catch (IOException ioe) {
      throw ioe;
    } catch (NumberFormatException nfe) {
      IOException io = new IOException(nfe.toString());
      io.initCause(nfe);
      throw io;
    } catch (DOMException de) {
      IOException io = new IOException(de.toString());
      io.initCause(de);
      throw io;
    }
  }

  public IntBuffer readIntBuffer(String name, IntBuffer defVal) throws IOException {
    try {
      Element tmpEl = findChildElement(currentElem, name);
      if (tmpEl == null) {
        return defVal;
      }

      String sizeString = tmpEl.getAttribute("size");
      String[] strings = parseTokens(tmpEl.getAttribute("data"));
      if (sizeString.length() > 0) {
        int requiredSize = Integer.parseInt(sizeString);
        if (strings.length != requiredSize)
          throw new IOException(
              "Wrong number of int buffers for '"
                  + name
                  + "'.  size says "
                  + requiredSize
                  + ", data contains "
                  + strings.length);
      }
      IntBuffer tmp = BufferUtils.createIntBuffer(strings.length);
      for (String s : strings) tmp.put(Integer.parseInt(s));
      tmp.flip();
      return tmp;
    } catch (IOException ioe) {
      throw ioe;
    } catch (NumberFormatException nfe) {
      IOException io = new IOException(nfe.toString());
      io.initCause(nfe);
      throw io;
    } catch (DOMException de) {
      IOException io = new IOException(de.toString());
      io.initCause(de);
      throw io;
    }
  }

  public ByteBuffer readByteBuffer(String name, ByteBuffer defVal) throws IOException {
    try {
      Element tmpEl = findChildElement(currentElem, name);
      if (tmpEl == null) {
        return defVal;
      }

      String sizeString = tmpEl.getAttribute("size");
      String[] strings = parseTokens(tmpEl.getAttribute("data"));
      if (sizeString.length() > 0) {
        int requiredSize = Integer.parseInt(sizeString);
        if (strings.length != requiredSize)
          throw new IOException(
              "Wrong number of byte buffers for '"
                  + name
                  + "'.  size says "
                  + requiredSize
                  + ", data contains "
                  + strings.length);
      }
      ByteBuffer tmp = BufferUtils.createByteBuffer(strings.length);
      for (String s : strings) tmp.put(Byte.valueOf(s));
      tmp.flip();
      return tmp;
    } catch (IOException ioe) {
      throw ioe;
    } catch (NumberFormatException nfe) {
      IOException io = new IOException(nfe.toString());
      io.initCause(nfe);
      throw io;
    } catch (DOMException de) {
      IOException io = new IOException(de.toString());
      io.initCause(de);
      throw io;
    }
  }

  public ShortBuffer readShortBuffer(String name, ShortBuffer defVal) throws IOException {
    try {
      Element tmpEl = findChildElement(currentElem, name);
      if (tmpEl == null) {
        return defVal;
      }

      String sizeString = tmpEl.getAttribute("size");
      String[] strings = parseTokens(tmpEl.getAttribute("data"));
      if (sizeString.length() > 0) {
        int requiredSize = Integer.parseInt(sizeString);
        if (strings.length != requiredSize)
          throw new IOException(
              "Wrong number of short buffers for '"
                  + name
                  + "'.  size says "
                  + requiredSize
                  + ", data contains "
                  + strings.length);
      }
      ShortBuffer tmp = BufferUtils.createShortBuffer(strings.length);
      for (String s : strings) tmp.put(Short.valueOf(s));
      tmp.flip();
      return tmp;
    } catch (IOException ioe) {
      throw ioe;
    } catch (NumberFormatException nfe) {
      IOException io = new IOException(nfe.toString());
      io.initCause(nfe);
      throw io;
    } catch (DOMException de) {
      IOException io = new IOException(de.toString());
      io.initCause(de);
      throw io;
    }
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
      IOException io = new IOException(nfe.toString());
      io.initCause(nfe);
      throw io;
    } catch (DOMException de) {
      IOException io = new IOException(de.toString());
      io.initCause(de);
      throw io;
    }
  }

  public <T extends Enum<T>> T readEnum(String name, Class<T> enumType, T defVal)
      throws IOException {
    T ret = defVal;
    try {
      String eVal = currentElem.getAttribute(name);
      if (eVal != null && eVal.length() > 0) {
        ret = Enum.valueOf(enumType, eVal);
      }
    } catch (Exception e) {
      IOException io = new IOException(e.toString());
      io.initCause(e);
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
