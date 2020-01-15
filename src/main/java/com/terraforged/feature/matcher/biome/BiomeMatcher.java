package com.terraforged.feature.matcher.biome;

import net.minecraft.world.biome.Biome;

import java.util.Set;
import java.util.function.Predicate;

public interface BiomeMatcher extends Predicate<Biome> {

    BiomeMatcher ANY = b -> true;

    default BiomeMatcher or(BiomeMatcher other) {
        return b -> test(b) || other.test(b);
    }

    static BiomeMatcher of(Biome biome) {
        return b -> b == biome;
    }

    static BiomeMatcher of(Set<Biome> biomes) {
        return biomes::contains;
    }
}
