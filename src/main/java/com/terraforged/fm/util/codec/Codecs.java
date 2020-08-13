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

package com.terraforged.fm.util.codec;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Codecs {

    public static <V> Codec<V> create(EncoderFunc<V> encoder, DecoderFunc<V> decoder) {
        return Codec.of(encoder, decoder);
    }

    public static <V, T> Optional<V> decode(Codec<V> codec, T input, DynamicOps<T> ops) {
        return codec.decode(ops, input).result().map(Pair::getFirst);
    }

    public static <V, T> DataResult<T> encode(Codec<V> codec, V value, DynamicOps<T> ops) {
        return codec.encodeStart(ops, value);
    }

    public static <V, T> T encodeAndGet(Codec<V> codec, V value, DynamicOps<T> ops) {
        return codec.encodeStart(ops, value).result().get();
    }

    public static <V> JsonElement encode(Codec<V> codec, V value) {
        return encode(codec, value, JsonOps.INSTANCE).result().orElse(JsonNull.INSTANCE);
    }

    public static <V> Optional<V> decode(Codec<V> codec, JsonElement element) {
        return decode(codec, element, JsonOps.INSTANCE);
    }

    public static <V, T> T createList(Codec<V> codec, DynamicOps<T> ops, List<V> list) {
        return codec.listOf().encodeStart(ops, list).result().orElse(ops.createList(Stream.empty()));
    }
}
