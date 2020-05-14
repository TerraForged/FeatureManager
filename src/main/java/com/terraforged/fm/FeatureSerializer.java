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

package com.terraforged.fm;

import com.google.gson.JsonElement;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Optional;

public class FeatureSerializer {

    public static final Marker MARKER = MarkerManager.getMarker("Serializer");

    public static JsonElement serialize(ConfiguredFeature<?, ?> feature) {
        return feature.serialize(JsonOps.INSTANCE).getValue();
    }

    public static ConfiguredFeature<?, ?> deserializeUnchecked(JsonElement element) {
        Dynamic<JsonElement> dynamic = new Dynamic<>(JsonOps.INSTANCE, element);
        return ConfiguredFeature.deserialize(dynamic);
    }

    public static Optional<ConfiguredFeature<?, ?>> deserialize(JsonElement element) {
        try {
            return Optional.of(deserializeUnchecked(element));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }
}
