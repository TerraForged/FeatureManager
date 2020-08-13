/*
 * Copyright © 2020 dags <dags@dags.me>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the LICENSE file for more details.
 */

package com.terraforged.fm.util.codec;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class GenericMapCodec<K, V> implements Codec<Map<K, V>> {

    private final Codec<K> keyType;
    private final Codec<V> valueType;

    private GenericMapCodec(Codec<K> keyType, Codec<V> valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    public final <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) {
        return ops.getMapEntries(input).flatMap(entries -> {
            Map<K, V> map = createDecodedMap();
            ErrorHandler handler = new ErrorHandler();

            entries.accept((k, v) -> {
                Optional<K> key = decodeElement(ops, k, keyType, handler);
                if (!key.isPresent()) {
                    return;
                }

                Optional<V> value = decodeElement(ops, v, valueType, handler);
                if (!value.isPresent()) {
                    return;
                }

                map.put(key.get(), value.get());
            });

            return handler.wrapResult(Pair.of(map, input));
        });
    }

    @Override
    public final <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T prefix) {
        Map<T, T> map = createEncodeMap(input.size());
        ErrorHandler handler = new ErrorHandler();

        for (Map.Entry<K, V> entry : input.entrySet()) {
            Optional<T> key = encodeElement(ops, entry.getKey(), keyType, handler);
            if (!key.isPresent()) {
                continue;
            }

            Optional<T> value = encodeElement(ops, entry.getValue(), valueType, handler);
            if (!value.isPresent()) {
                continue;
            }

            map.put(key.get(), value.get());
        }

        return handler.wrapResult(ops.createMap(map));
    }

    @Override
    public String toString() {
        return "GenericMapCodec{" +
                "keyType=" + keyType +
                ", valueType=" + valueType +
                '}';
    }

    Map<K, V> createDecodedMap() {
        return Maps.newHashMap();
    }

    <T> Map<T, T> createEncodeMap(int size) {
        return Maps.newHashMapWithExpectedSize(size);
    }

    public static <K, V> Codec<Map<K, V>> of(Codec<K> keyCodec, Codec<V> valueCodec) {
        return new GenericMapCodec<>(keyCodec, valueCodec);
    }

    public static <K, V> Codec<Map<K, V>> sorted(Codec<K> keyCodec, Codec<V> valueCodec) {
        return new GenericMapCodec.Sorted<>(keyCodec, valueCodec);
    }

    private static <V, T> Optional<V> decodeElement(DynamicOps<T> ops, T input, Codec<V> codec, Consumer<String> errorHandler) {
        DataResult<Pair<V, T>> result = codec.decode(ops, input);
        Optional<V> value = result.result().map(Pair::getFirst);
        if (!value.isPresent()) {
            result.error().map(DataResult.PartialResult::message).ifPresent(errorHandler);
        }
        return value;
    }

    private static <V, T> Optional<T> encodeElement(DynamicOps<T> ops, V input, Codec<V> codec, Consumer<String> errorHandler) {
        DataResult<T> result = codec.encodeStart(ops, input);
        Optional<T> value = result.result();
        if (!value.isPresent()) {
            result.error().map(DataResult.PartialResult::message).ifPresent(errorHandler);
        }
        return value;
    }

    private static class ErrorHandler implements Consumer<String> {

        private String message = "";

        public boolean caughtError() {
            return !message.isEmpty();
        }

        @Override
        public void accept(String message) {
            if (!caughtError()) {
                this.message = message;
            }
        }

        public <R> DataResult<R> wrapResult(R result) {
            if (caughtError()) {
                return DataResult.error(message, result);
            }
            return DataResult.success(result);
        }
    }

    private static class Sorted<K, V> extends GenericMapCodec<K, V> {

        private Sorted(Codec<K> keyType, Codec<V> valueType) {
            super(keyType, valueType);
        }

        @Override
        Map<K, V> createDecodedMap() {
            return Maps.newLinkedHashMap();
        }

        @Override
        <T> Map<T, T> createEncodeMap(int size) {
            return Maps.newLinkedHashMapWithExpectedSize(size);
        }
    }
}
