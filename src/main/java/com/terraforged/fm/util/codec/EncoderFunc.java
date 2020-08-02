package com.terraforged.fm.util.codec;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;

public interface EncoderFunc<V> extends Encoder<V> {

    <T> Dynamic<T> _encode(V v, DynamicOps<T> ops);

    @Override
    default <T> DataResult<T> encode(V input, DynamicOps<T> ops, T prefix) {
        try {
            Dynamic<T> dynamic = _encode(input, ops);
            return DataResult.success(dynamic.getValue());
        } catch (Throwable t) {
            return DataResult.error(t.getMessage());
        }
    }
}
