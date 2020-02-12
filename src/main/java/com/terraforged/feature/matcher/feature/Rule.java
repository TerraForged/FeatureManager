package com.terraforged.feature.matcher.feature;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Rule {

    private final Collection<JsonPrimitive> values;

    public Rule(Collection<JsonPrimitive> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "values=" + values +
                '}';
    }

    public Matcher createMatcher() {
        return new Matcher(values);
    }

    public static List<Rule> parseRules(JsonElement element) {
        List<Rule> rules = new LinkedList<>();
        if (element.isJsonPrimitive()) {
            rules.add(new Rule(Collections.singletonList(element.getAsJsonPrimitive())));
        } else if (element.isJsonArray()) {
            boolean arrays = true;
            boolean primitive = true;
            for (JsonElement e : element.getAsJsonArray()) {
                if (e.isJsonPrimitive()) {
                    arrays = false;
                    continue;
                }
                if (e.isJsonArray()) {
                    primitive = false;
                    continue;
                }
                return Collections.emptyList();
            }
            if (primitive) {
                List<JsonPrimitive> primitives = getPrimitives(element.getAsJsonArray());
                rules.add(new Rule(new HashSet<>(primitives)));
            } else if (arrays) {
                for (JsonElement e : element.getAsJsonArray()) {
                    List<JsonPrimitive> primitives = getPrimitives(e.getAsJsonArray());
                    rules.add(new Rule(primitives));
                }
            }
        }
        return rules;
    }

    private static List<JsonPrimitive> getPrimitives(JsonArray array) {
        List<JsonPrimitive> list = new LinkedList<>();
        for (JsonElement e : array) {
            if (!e.isJsonPrimitive()) {
                return Collections.emptyList();
            }
            list.add(e.getAsJsonPrimitive());
        }
        return list;
    }
}
