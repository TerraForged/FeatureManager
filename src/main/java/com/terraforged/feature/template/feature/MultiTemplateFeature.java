package com.terraforged.feature.template.feature;

import com.terraforged.feature.template.TemplateConfig;
import com.terraforged.feature.template.type.FeatureType;
import com.terraforged.feature.template.type.FeatureTypes;
import com.terraforged.feature.template.type.TypedFeature;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.List;
import java.util.Random;

public class MultiTemplateFeature extends Feature<NoFeatureConfig> implements TypedFeature {

    private final int baseDepth;

    private final FeatureType type;
    private final ResourceLocation name;
    private final List<TemplateFeature> templates;
    private final TemplateFeatureConfig config = new TemplateFeatureConfig(false, false);

    public MultiTemplateFeature(TemplateConfig config, List<TemplateFeature> templates) {
        super(NoFeatureConfig::deserialize);
        this.type = config.getType();
        this.name = config.getRegistryName();
        this.baseDepth = config.getBaseDepth();
        this.templates = templates;
        this.setRegistryName(name);
        FeatureTypes.register(type, this);
    }

    @Override
    public FeatureType getType() {
        return type;
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<?> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (getType().getPlacement().canPlaceAt(world, pos)) {
            if (templates.size() > 0) {
                Feature<TemplateFeatureConfig> feature = next(rand);
                this.config.baseDepth = baseDepth;
                return feature.place(world, generator, rand, pos, this.config);
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiTemplateFeature that = (MultiTemplateFeature) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    private TemplateFeature next(Random random) {
        int index = random.nextInt(templates.size());
        return templates.get(index);
    }
}
