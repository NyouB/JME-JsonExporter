package fr.jme.exporter;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import java.io.IOException;


public class TestSavable implements Savable {

  Vector3f vector3f = new Vector3f();
  ColorRGBA colorRGBA = new ColorRGBA();
  int myInt = 7890;
  String myString = "myString";

  public TestSavable() {
  }

  @Override
  public void write(JmeExporter ex) throws IOException {
    OutputCapsule caps = ex.getCapsule(null);
    caps.write(vector3f, "vector3f", null);
    caps.write(colorRGBA, "colorRGBA", null);
    caps.write(myInt, "myInt", 0);
    caps.write(myString, "myString", null);
  }

  @Override
  public void read(JmeImporter im) throws IOException {
    InputCapsule inputCapsule = im.getCapsule(null);
    vector3f = (Vector3f) inputCapsule.readSavable("vector3f", null);
    colorRGBA = (ColorRGBA) inputCapsule.readSavable("colorRGBA", null);
    myInt = inputCapsule.readInt("myInt", 0);
    myString = inputCapsule.readString("myString", null);

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TestSavable that = (TestSavable) o;

    if (myInt != that.myInt) {
      return false;
    }
    if (vector3f != null ? !vector3f.equals(that.vector3f) : that.vector3f != null) {
      return false;
    }
    if (colorRGBA != null ? !colorRGBA.equals(that.colorRGBA) : that.colorRGBA != null) {
      return false;
    }
    return myString != null ? myString.equals(that.myString) : that.myString == null;
  }

  @Override
  public int hashCode() {
    int result = vector3f != null ? vector3f.hashCode() : 0;
    result = 31 * result + (colorRGBA != null ? colorRGBA.hashCode() : 0);
    result = 31 * result + myInt;
    result = 31 * result + (myString != null ? myString.hashCode() : 0);
    return result;
  }
}
