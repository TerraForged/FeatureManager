package com.terraforged.fm.fast;

import net.minecraft.world.gen.placement.FrequencyConfig;

public abstract class FastFrequencyDecorator extends FastDecorator<FrequencyConfig> {

    public FastFrequencyDecorator() {
        super(FrequencyConfig.field_236971_a_);
    }

    @Override
    protected int getCount(FrequencyConfig config) {
        return config.count;
    }
}
