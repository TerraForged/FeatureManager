package com.terraforged.fm.template;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.List;
import java.util.Map;

public class StructureUtils {

    public static final List<Structure<?>> SURFACE_STRUCTURES = ImmutableList.of(
            Structure.VILLAGE,
            Structure.JUNGLE_TEMPLE,
            Structure.PILLAGER_OUTPOST,
            Structure.SWAMP_HUT,
            Structure.IGLOO,
            Structure.DESERT_PYRAMID,
            Structure.WOODLAND_MANSION
    );

    public static boolean hasOvergroundStructure(IChunk chunk) {
        Map<String, LongSet> references = chunk.getStructureReferences();
        for (Structure<?> structure : SURFACE_STRUCTURES) {
            LongSet refs = references.get(structure.getStructureName());
            if (refs != null && refs.size() > 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasStructure(IChunk chunk, Structure<?> structure) {
        LongSet refs = chunk.getStructureReferences().get(structure.getStructureName());
        return refs != null && refs.size() > 0;
    }
}
