package com.terraforged.feature.matcher.biome;

import net.minecraft.world.biome.Biome;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class BiomeMatcher implements Predicate<Biome>, Comparable<BiomeMatcher> {

    public static final BiomeMatcher ANY = new BiomeMatcher(Collections.emptySet());

    private final Set<Biome> biomes;

    private BiomeMatcher(Set<Biome> biomes) {
        this.biomes = biomes;
    }

    @Override
    public int compareTo(BiomeMatcher o) {
        // reverse order so more biomes == tested first
        return Integer.compare(o.biomes.size(), biomes.size());
    }

    @Override
    public boolean test(Biome biome) {
        return biomes.isEmpty() || biomes.contains(biome);
    }

    public BiomeMatcher or(BiomeMatcher other) {
        Set<Biome> combined = new HashSet<>();
        combined.addAll(biomes);
        combined.addAll(other.biomes);
        return new BiomeMatcher(combined);
    }

    ;

    public static BiomeMatcher of(Biome biome) {
        return new BiomeMatcher(Collections.singleton(biome));
    }

    public static BiomeMatcher of(Set<Biome> biomes) {
        return new BiomeMatcher(biomes);
    }
}
