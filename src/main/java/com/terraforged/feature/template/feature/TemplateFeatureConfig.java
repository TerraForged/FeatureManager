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

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class TemplateFeatureConfig extends NoFeatureConfig {

    public static final TemplateFeatureConfig DEFAULT = new TemplateFeatureConfig(false, false, 0);

    public final boolean pasteAir;
    public final boolean replaceSolid;
    public final int baseDepth;

    public TemplateFeatureConfig(boolean pasteAir, boolean replaceSolid, int baseDepth) {
        this.pasteAir = pasteAir;
        this.replaceSolid = replaceSolid;
        this.baseDepth = baseDepth;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(
                ops,
                ops.createMap(
                        ImmutableMap.of(
                                ops.createString("paste_air"),
                                ops.createBoolean(pasteAir),
                                ops.createString("replace_solid"),
                                ops.createBoolean(replaceSolid)
                        )
                )
        );
    }

    public static <T> TemplateFeatureConfig deserialize(Dynamic<T> dynamic) {
        try {
            boolean pasteAir = dynamic.get("paste_air").asBoolean(false);
            boolean replaceSolid = dynamic.get("replace_solid").asBoolean(false);
            int baseDepth = dynamic.get("base_depth").asInt(0);
            return new TemplateFeatureConfig(pasteAir, replaceSolid, baseDepth);
        } catch (Throwable t) {
            t.printStackTrace();
            return DEFAULT;
        }
    }
}
