package com.terraforged.fm.util.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

public interface DecoderFunc<V> extends Decoder<V> {

    <T> V _decode(Dynamic<T> dynamic);

    default <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
        try {
            V v = _decode(new Dynamic<>(ops, input));
            return DataResult.success(Pair.of(v, input));
        } catch (Throwable t) {
            return DataResult.error(t.getMessage());
        }
    }
}
