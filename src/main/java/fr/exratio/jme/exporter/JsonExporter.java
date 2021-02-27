/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package fr.exratio.jme.exporter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.jme3.export.JmeExporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * A Json serialisation exporter which can take advantage of the existing serialization methods
 *
 * @author NyouB
 */
public class JsonExporter implements JmeExporter {

  private JsonOutputCapsule jsonOutputCapsule;
  private JsonGenerator jsonGenerator;

  public JsonExporter() {
    JsonFactory jfactory = new JsonFactory();
    try {
      jsonGenerator = jfactory.createGenerator(new StringWriter());
    } catch (IOException e) {
      e.printStackTrace();
    }
    jsonOutputCapsule = new JsonOutputCapsule(jsonGenerator, this);
  }

  public JsonExporter(JsonGenerator jsonGenerator, JsonOutputCapsule jsonOutputCapsule) {
    this.jsonGenerator = jsonGenerator;
    this.jsonOutputCapsule = jsonOutputCapsule;
  }

  public JsonExporter(JsonGenerator jsonGenerator) {
    this.jsonGenerator = jsonGenerator;
    jsonOutputCapsule = new JsonOutputCapsule(jsonGenerator, this);
  }

  public JsonExporter(OutputStream stream) throws IOException {
    jsonGenerator = new JsonFactory().createGenerator(stream);
    jsonOutputCapsule = new JsonOutputCapsule(jsonGenerator, this);
  }

  public JsonExporter(Writer writer) throws IOException {
    jsonGenerator = new JsonFactory().createGenerator(writer);
    jsonOutputCapsule = new JsonOutputCapsule(jsonGenerator, this);
  }

  @Override
  public void save(Savable object, OutputStream f) throws IOException {

    // Initialize Document when saving so we don't retain state of previous export
    JsonFactory jfactory = new JsonFactory();
    JsonGenerator jGenerator = jfactory.createGenerator(f, JsonEncoding.UTF8);
    jsonOutputCapsule = new JsonOutputCapsule(jGenerator, this);
    jsonOutputCapsule.write(object.getClass().getCanonicalName(), "type", null);
    object.write(this);
    f.flush();
  }

  @Override
  public void save(Savable object, File f) throws IOException {
    FileOutputStream fos = new FileOutputStream(f);
    try {
      save(object, fos);
    } finally {
      fos.close();
    }
  }

  @Override
  public OutputCapsule getCapsule(Savable object) {
    return jsonOutputCapsule;
  }

  public static JsonExporter getInstance() {
    return new JsonExporter();
  }

  public JsonOutputCapsule getJsonOutputCapsule() {
    return jsonOutputCapsule;
  }

  public void setJsonOutputCapsule(JsonOutputCapsule jsonOutputCapsule) {
    this.jsonOutputCapsule = jsonOutputCapsule;
  }

  public JsonGenerator getJsonGenerator() {
    return jsonGenerator;
  }

  public void setJsonGenerator(JsonGenerator jsonGenerator) {
    this.jsonGenerator = jsonGenerator;
  }
}
