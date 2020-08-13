package com.terraforged.fm.structure;

import com.terraforged.fm.predicate.FeaturePredicate;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.HashMap;
import java.util.Map;

public class FMStructureManager {

    private final Map<String, FeaturePredicate> predicates = new HashMap<>();

    public void register(Structure<?> structure, FeaturePredicate predicate) {
        register(structure.getStructureName(), predicate);
    }

    public void register(String structureName, FeaturePredicate predicate) {
        predicates.put(structureName, predicate);
    }

    public FeaturePredicate getPredicate(Structure<?> structure) {
        return getPredicate(structure.getStructureName());
    }

    public FeaturePredicate getPredicate(String structureName) {
        return predicates.getOrDefault(structureName, FeaturePredicate.ALLOW);
    }
}
