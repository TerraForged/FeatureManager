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
