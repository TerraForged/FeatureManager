/*
 *
 * MIT License
 *
 * Copyright (c) 2020 TerraForged
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.terraforged.feature.template.feature;

import com.terraforged.feature.template.TemplateConfig;
import com.terraforged.feature.template.type.FeatureType;
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

    public MultiTemplateFeature(TemplateConfig config, List<TemplateFeature> templates) {
        super(NoFeatureConfig::deserialize);
        this.type = config.getType();
        this.name = config.getRegistryName();
        this.baseDepth = config.getBaseDepth();
        this.templates = templates;
        this.setRegistryName(name);
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
                TemplateFeatureConfig cfg = new TemplateFeatureConfig(false, false, baseDepth);
                return feature.place(world, generator, rand, pos, cfg);
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
