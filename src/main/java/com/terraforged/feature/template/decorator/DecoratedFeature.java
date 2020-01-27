package com.terraforged.feature.template.decorator;

import com.terraforged.feature.template.type.FeatureType;
import com.terraforged.feature.template.type.TypedFeature;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class DecoratedFeature<T extends Feature<NoFeatureConfig> & TypedFeature, W extends IWorld> extends Feature<NoFeatureConfig> implements TypedFeature {

    private final T feature;
    private final List<Decorator<W>> decorators;
    private final Function<IWorld, W> worldFactory;

    public DecoratedFeature(T feature, List<Decorator<W>> decorators, Function<IWorld, W> factory) {
        super(NoFeatureConfig::deserialize);
        this.worldFactory = factory;
        this.feature = feature;
        this.decorators = decorators;
        setRegistryName(feature.getRegistryName());
    }

    @Override
    public FeatureType getType() {
        return feature.getType();
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        W featureWorld = worldFactory.apply(world);
        boolean result = feature.place(featureWorld, generator, rand, pos, config);
        if (result) {
            for (Decorator<W> decorator : decorators) {
                decorator.apply(featureWorld, rand);
            }
        }
        return result;
    }
}
