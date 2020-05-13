package com.terraforged.mod.feature.manager.data;

import com.terraforged.mod.feature.manager.FeatureManager;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;

import java.util.Map;
import java.util.function.Supplier;

public class ModDataPackFinder implements IPackFinder {

    @Override
    public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> map, ResourcePackInfo.IFactory<T> infoFactory) {
        for (ModFileInfo info : ModList.get().getModFiles()) {
            T packInfo = ResourcePackInfo.createResourcePack(
                    info.getFile().getFileName(),
                    true,
                    new ModSupplier(info.getFile()),
                    infoFactory,
                    ResourcePackInfo.Priority.TOP
            );

            if (packInfo != null) {
                FeatureManager.LOG.debug(" Adding Mod RP: {}", packInfo.getName());
                map.put(packInfo.getName(), packInfo);
            }
        }
    }

    private static class ModSupplier implements Supplier<IResourcePack> {

        private final ModFile modFile;

        private ModSupplier(ModFile modFile) {
            this.modFile = modFile;
        }

        @Override
        public IResourcePack get() {
            return new ModFileResourcePack(modFile);
        }
    }
}
