package com.terraforged.fm.template;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StructureUtils {

    public static final List<Structure<?>> SURFACE_STRUCTURES = Structure.field_236365_a_.values().stream()
            .filter(structure -> structure.func_236396_f_() == GenerationStage.Decoration.SURFACE_STRUCTURES)
            .collect(Collectors.toList());

    public static boolean hasOvergroundStructure(IChunk chunk) {
        Map<Structure<?>, LongSet> references = chunk.getStructureReferences();
        for (Structure<?> structure : SURFACE_STRUCTURES) {
            LongSet refs = references.get(structure);
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
