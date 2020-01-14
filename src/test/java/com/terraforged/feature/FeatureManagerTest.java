package com.terraforged.feature;

import com.terraforged.feature.event.FeatureInitEvent;
import com.terraforged.feature.matcher.FeatureMatcher;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod("feature_manager")
public class FeatureManagerTest {

    @SubscribeEvent
    public static void init(FMLLoadCompleteEvent event) {
        FeatureTestWorld.get();
        FeatureManager.BUS.register(new FeatureListener());
    }

    public static class FeatureListener {

        @SubscribeEvent
        public void register(FeatureInitEvent.Transformer event) {
            if (event.getWorldType() == FeatureTestWorld.get()) {
                event.register(
                        FeatureMatcher.of("minecraft:oak_leaves"),
                        FeatureTransformer.of(Feature.FOSSIL, NoFeatureConfig.NO_FEATURE_CONFIG)
                );
            }
        }
    }
}
