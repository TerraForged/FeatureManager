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
