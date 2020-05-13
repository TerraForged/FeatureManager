package com.terraforged.mod.feature.manager.data;

import com.terraforged.mod.feature.manager.FeatureManager;
import net.minecraft.resources.FolderPackFinder;
import net.minecraft.resources.ResourcePackInfo;
import org.jline.utils.Log;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FolderDataPackFinder extends FolderPackFinder {

    public FolderDataPackFinder(File folderIn) {
        super(folderIn);
    }

    @Override
    public <T extends ResourcePackInfo> void addPackInfosToMap(Map<String, T> map, ResourcePackInfo.IFactory<T> infoFactory) {
        Log.debug("Searching for DataPacks...");
        Set<String> initial = new HashSet<>(map.keySet());
        super.addPackInfosToMap(map, infoFactory);
        for (Map.Entry<String, ?> e : map.entrySet()) {
            if (!initial.contains(e.getKey())) {
                FeatureManager.LOG.debug("Added DataPack: {}", e.getKey());
            }
        }
    }
}
