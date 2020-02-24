/*
 *
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

package com.terraforged.feature.template;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.terraforged.feature.template.type.FeatureType;
import com.terraforged.feature.template.type.FeatureTypes;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TemplateConfig {

    private final String type;
    private final int extendBase;
    private final JsonElement decorators;
    private final ResourceLocation name;
    private final List<ResourceLocation> paths;

    private TemplateConfig(Builder builder) {
        this.type = builder.type;
        this.name = builder.name;
        this.paths = builder.paths;
        this.extendBase = builder.base;
        this.decorators = builder.decorators;
    }

    public FeatureType getType() {
        return FeatureTypes.getType(type);
    }

    public int getBaseDepth() {
        return extendBase;
    }

    public JsonElement getDecorators() {
        return decorators;
    }

    public ResourceLocation getRegistryName() {
        return name;
    }

    public Iterable<ResourceLocation> getPaths() {
        return paths;
    }

    public static Optional<TemplateConfig> parse(ResourceLocation location, JsonElement element) {
        if (!element.isJsonObject()) {
            return Optional.empty();
        }

        JsonObject root = element.getAsJsonObject();
        if (!root.has("name") || !root.has("paths")) {
            return Optional.empty();
        }


        TemplateConfig.Builder builder = TemplateConfig.builder();
        builder.name(location.getNamespace(), root.get("name").getAsString());

        if (root.has("type")) {
            builder.type(root.get("type").getAsString());
        }

        if (root.has("base")) {
            builder.base(root.get("base").getAsInt());
        }

        if (root.has("decorators")) {
            builder.decorators(root.get("decorators"));
        }

        for (JsonElement path : root.getAsJsonArray("paths")) {
            builder.path(location.getNamespace(), path.getAsString());
        }

        return Optional.of(builder.build());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int base;
        private String type;
        private ResourceLocation name;
        private JsonElement decorators = JsonNull.INSTANCE;
        private List<ResourceLocation> paths = new ArrayList<>();

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder base(int depth) {
            this.base = depth;
            return this;
        }

        public Builder decorators(JsonElement element) {
            this.decorators = element;
            return this;
        }

        public Builder name(String namespace, String name) {
            int split = name.indexOf(':');
            if (split > 0) {
                namespace = name.substring(0, split);
                name = name.substring(split + 1);
            }
            this.name = new ResourceLocation(namespace, name);
            return this;
        }

        public Builder path(String namespace, String location) {
            int split = location.indexOf(':');
            if (split > 0) {
                namespace = location.substring(0, split);
                location = location.substring(split + 1);
                if (!location.startsWith("structures/")) {
                    location = "structures/" + location;
                }
            }
            paths.add(new ResourceLocation(namespace, location));
            return this;
        }

        public TemplateConfig build() {
            return new TemplateConfig(this);
        }
    }
}
