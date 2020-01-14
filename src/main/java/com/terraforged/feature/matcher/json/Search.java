package com.terraforged.feature.matcher.json;

import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

public class Search {

    private final List<Matcher> matchers;

    Search(List<Rule> list) {
        matchers = new ArrayList<>(list.size());
        for (Rule rule : list) {
            matchers.add(rule.createMatcher());
        }
    }

    public boolean test(JsonPrimitive value) {
        for (Matcher matcher : matchers) {
            matcher.test(value);
            if (matcher.complete()) {
                return true;
            }
        }
        return false;
    }
}
