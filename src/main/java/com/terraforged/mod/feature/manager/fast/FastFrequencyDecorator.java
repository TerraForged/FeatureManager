package com.terraforged.mod.feature.manager.fast;

import net.minecraft.world.gen.placement.FrequencyConfig;

public abstract class FastFrequencyDecorator extends FastDecorator<FrequencyConfig> {

    public FastFrequencyDecorator() {
        super(FrequencyConfig::deserialize);
    }

    @Override
    protected int getCount(FrequencyConfig config) {
        return config.count;
    }
}
