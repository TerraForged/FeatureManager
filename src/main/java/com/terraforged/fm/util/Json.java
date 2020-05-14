package com.terraforged.fm.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Function;

public class Json {

    public static String getString(String path, JsonObject owner, String def) {
        return get(path, owner, JsonElement::getAsString, def);
    }

    public static int getInt(String path, JsonObject owner, int def) {
        return get(path, owner, JsonElement::getAsInt, def);
    }

    public static boolean getBool(String path, JsonObject owner, boolean def) {
        return get(path, owner, JsonElement::getAsBoolean, def);
    }

    public static float getFloat(String path, JsonObject owner, float def) {
        return get(path, owner, JsonElement::getAsFloat, def);
    }

    public static <T> T get(String path, JsonObject owner, Function<JsonElement, T> func, T def) {
        if (owner == null) {
            return def;
        }
        JsonElement child = owner.get(path);
        if (child == null) {
            return def;
        }
        return func.apply(child);
    }
}
