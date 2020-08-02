package com.terraforged.fm.data;

import com.terraforged.fm.FeatureManager;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModDataPackFinder implements IPackFinder {

    @Override
    public <T extends ResourcePackInfo> void func_230230_a_(Consumer<T> consumer, ResourcePackInfo.IFactory<T> factory) {
        for (ModFileInfo info : ModList.get().getModFiles()) {
            T packInfo = ResourcePackInfo.createResourcePack(
                    info.getFile().getFileName(),
                    true,
                    new ModSupplier(info.getFile()),
                    factory,
                    ResourcePackInfo.Priority.TOP,
                    IPackNameDecorator.BUILTIN
            );

            if (packInfo != null) {
                FeatureManager.LOG.debug(" Adding Mod RP: {}", packInfo.getName());
                consumer.accept(packInfo);
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
