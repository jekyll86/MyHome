package com.jeckyll86.xenont.myhome.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XenonT on 16/04/2015.
 */
public class JSONToMapDeserializer implements JsonDeserializer<Map<String,String>> {

    @Override
    public Map<String,String> deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        Map<String, String> result = new HashMap<String, String>();

        JsonArray array = json.getAsJsonArray();
        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            // This does not check if the objects only have one property, so JSON
            // like [{a:b,c:d}{e:f}] will become a Map like {a:b,c:d,e:f} as well.
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().getAsString();
                result.put(key, value);
            }
        }
        return result;
    }
}