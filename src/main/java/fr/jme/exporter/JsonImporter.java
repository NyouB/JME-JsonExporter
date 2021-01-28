package fr.jme.exporter;

import com.fasterxml.jackson.databind.JsonNode;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JsonImporter implements JmeImporter {

  private AssetManager assetManager;
  private JsonInputCapsule jsonInputCapsule;
  int formatVersion = 0;

  public JsonImporter() {
  }

  public JsonImporter(JsonInputCapsule jsonInputCapsule) {
    this.jsonInputCapsule = jsonInputCapsule;
  }

  public JsonImporter(JsonNode jsonNode) {
    this.jsonInputCapsule = new JsonInputCapsule(jsonNode, this);
  }

  public JsonImporter(InputStream inputStream) throws IOException {
    this.jsonInputCapsule = new JsonInputCapsule(inputStream, this);
  }

  public JsonImporter(InputStream inputStream, DesktopAssetManager desktopAssetManager)
      throws IOException {
    this.jsonInputCapsule = new JsonInputCapsule(inputStream, this);
    this.assetManager = desktopAssetManager;
  }

  public int getFormatVersion() {
    return formatVersion;
  }

  public AssetManager getAssetManager() {
    return assetManager;
  }

  public void setAssetManager(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  public Object load(AssetInfo info) throws IOException {
    assetManager = info.getManager();
    InputStream in = info.openStream();
    try {
      return load(in);
    } finally {
      if (in != null) in.close();
    }
  }

  public Savable load(File f) throws IOException {
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(f);
      Savable sav = load(fis);
      return sav;
    } finally {
      if (fis != null) fis.close();
    }
  }

  public Savable load(InputStream f) throws IOException {
    //
    jsonInputCapsule = new JsonInputCapsule(f, this);
    return jsonInputCapsule.readSavable(null, null);
  }

  public InputCapsule getCapsule(Savable id) {
    return jsonInputCapsule;
  }

  public static JsonImporter getInstance() {
    return new JsonImporter();
  }
}
