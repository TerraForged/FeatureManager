package com.terraforged.feature.matcher.feature;

import com.google.gson.JsonPrimitive;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Matcher {

    private final Set<JsonPrimitive> set;

    Matcher(Collection<JsonPrimitive> set) {
        this.set = new HashSet<>(set);
    }

    public boolean complete() {
        return set.isEmpty();
    }

    public void test(JsonPrimitive value) {
        set.remove(value);
    }
}
