/*
 * MIT License
 *
 * Copyright (c) 2020 TerraForged
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.terraforged.fm.matcher.feature;

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
