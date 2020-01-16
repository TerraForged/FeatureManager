package com.terraforged.feature.template.feature;

import com.terraforged.feature.template.type.FeatureType;
import com.terraforged.feature.template.type.TypedFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.List;
import java.util.Random;

public class MultiTemplateFeature extends Feature<TemplateFeatureConfig> implements TypedFeature {

    private final FeatureType type;
    private final List<TemplateFeature> templates;

    public MultiTemplateFeature(FeatureType type, ResourceLocation name, List<TemplateFeature> templates) {
        super(TemplateFeatureConfig::deserialize);
        this.type = type;
        this.templates = templates;
        this.setRegistryName(name);
    }

    @Override
    public FeatureType getType() {
        return type;
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<?> generator, Random rand, BlockPos pos, TemplateFeatureConfig config) {
        if (getType().getPlacement().canPlaceAt(world, pos)) {
            if (templates.size() > 0) {
                return next(rand).place(world, generator, rand, pos, config);
            }
        }
        return false;
    }

    private TemplateFeature next(Random random) {
        int index = random.nextInt(templates.size());
        return templates.get(index);
    }
}
