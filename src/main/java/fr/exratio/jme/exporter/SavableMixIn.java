package fr.exratio.jme.exporter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.CLASS, include = As.WRAPPER_ARRAY)
@JsonDeserialize(using = SavableJsonDeserializer.class)
public interface SavableMixIn {

}
