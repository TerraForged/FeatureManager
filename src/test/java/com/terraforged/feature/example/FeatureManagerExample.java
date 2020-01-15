package com.terraforged.feature.example;

import com.terraforged.feature.event.FeatureInitEvent;
import com.terraforged.feature.matcher.feature.FeatureMatcher;
import com.terraforged.feature.transformer.FeatureTransformer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod("example_mod")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FeatureManagerExample {

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        System.out.println("REGISTERING WORLD TYPE");
        ExampleWorld.init();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class FeatureRegistrationListener {

        @SubscribeEvent
        public static void register(FeatureInitEvent.Transformer event) {
            if (event.getWorldType() == ExampleWorld.TYPE) {
                System.out.println("REGISTERING TRANSFORMER");
                event.register(
                        // match any feature that uses oak_leaves & oak_log blocks (ie oak trees)
                        FeatureMatcher.and("minecraft:oak_leaves", "minecraft:oak_log"),
                        // replace any occurrence of "minecraft:oak_leaves" with "minecraft:gold_block"
                        FeatureTransformer.replace("minecraft:oak_leaves", "minecraft:gold_block")
                );
            }
        }
    }
}
