package com.terraforged.feature.matcher.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.terraforged.feature.FeatureJsonCache;
import com.terraforged.feature.matcher.FeatureMatcher;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.*;

public class JsonMatcher implements FeatureMatcher {

    private final List<Rule> rules;

    private JsonMatcher(List<Rule> rules) {
        super();
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "JsonMatcher{" +
                "rules=" + rules +
                '}';
    }

    @Override
    public boolean test(ConfiguredFeature<?, ?> feature) {
        JsonElement element = FeatureJsonCache.getInstance().getJson(feature);
        if (element == JsonNull.INSTANCE) {
            return false;
        }
        return test(element, new Search(rules));
    }

    private boolean test(JsonElement element, Search search) {
        if (element.isJsonObject()) {
            for (Map.Entry<String, JsonElement> e : element.getAsJsonObject().entrySet()) {
                if (test(e.getValue(), search)) {
                    return true;
                }
            }
        } else if (element.isJsonArray()) {
            for (JsonElement e : element.getAsJsonArray()) {
                if (test(e, search)) {
                    return true;
                }
            }
        } else if (element.isJsonPrimitive()) {
            return search.test(element.getAsJsonPrimitive());
        }
        return false;
    }

    public static Optional<FeatureMatcher> of(JsonElement element) {
        List<Rule> rules = Rule.parseRules(element);
        if (rules.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new JsonMatcher(rules));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<Rule> rules = Collections.emptyList();
        private List<JsonPrimitive> values = Collections.emptyList();

        public Builder and(Object value) {
            if (value instanceof String) {
                return and((String) value);
            }
            if (value instanceof Number) {
                return and((Number) value);
            }
            if (value instanceof Boolean) {
                return and((Boolean) value);
            }
            return this;
        }

        public Builder and(Boolean value) {
            return and(new JsonPrimitive(value));
        }

        public Builder and(Number value) {
            return and(new JsonPrimitive(value));
        }

        public Builder and(String value) {
            return and(new JsonPrimitive(value));
        }

        public Builder and(JsonPrimitive value) {
            if (values.isEmpty()) {
                values = new ArrayList<>();
            }
            values.add(value);
            return this;
        }

        public Builder or(Object value) {
            if (value instanceof String) {
                return or((String) value);
            }
            if (value instanceof Number) {
                return or((Number) value);
            }
            if (value instanceof Boolean) {
                return or((Boolean) value);
            }
            return this;
        }

        public Builder or(Boolean value) {
            return or(new JsonPrimitive(value));
        }

        public Builder or(Number value) {
            return or(new JsonPrimitive(value));
        }

        public Builder or(String value) {
            return or(new JsonPrimitive(value));
        }

        public Builder or(JsonPrimitive value) {
            return drain().and(value);
        }

        private Builder drain() {
            if (!values.isEmpty()) {
                if (rules.isEmpty()) {
                    rules = new ArrayList<>();
                }
                rules.add(new Rule(values));
                values = Collections.emptyList();
            }
            return this;
        }

        public JsonMatcher build() {
            drain();
            return new JsonMatcher(rules);
        }
    }
}
