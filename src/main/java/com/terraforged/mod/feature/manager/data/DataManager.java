package com.terraforged.mod.feature.manager.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import org.jline.utils.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Predicate;

public class DataManager implements AutoCloseable {

    public static final Predicate<String> ANY = s -> true;
    public static final Predicate<String> NBT = s -> s.endsWith(".nbt");
    public static final Predicate<String> JSON = s -> s.endsWith(".json");

    private final ResourcePackList<ResourcePackInfo> packList;
    private final IResourceManager resourceManager;

    public DataManager(IResourceManager resourceManager, ResourcePackList<ResourcePackInfo> packList) {
        this.resourceManager = resourceManager;
        this.packList = packList;
    }

    @Override
    public void close() {
        packList.close();
    }

    public IResource getResource(ResourceLocation location) throws IOException {
        return resourceManager.getResource(location);
    }

    public void forEach(String path, Predicate<String> matcher, ResourceVisitor<InputStream> consumer) {
        Log.debug("Input path: {}", path);
        for (ResourceLocation location : resourceManager.getAllResourceLocations(path, matcher)) {
            Log.debug(" Location: {}", location);
            try (IResource resource = getResource(location)) {
                if (resource == null) {
                    continue;
                }
                try (InputStream inputStream = resource.getInputStream()) {
                    consumer.accept(location, inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void forEachJson(String path, ResourceVisitor<JsonElement> consumer) {
        JsonParser parser = new JsonParser();
        forEach(path, DataManager.JSON, (location, data) -> {
            Reader reader = new BufferedReader(new InputStreamReader(data));
            JsonElement element = parser.parse(reader);
            consumer.accept(location, element);
        });
    }

    public static DataManager of(File dir) {
        SimpleReloadableResourceManager manager = new SimpleReloadableResourceManager(ResourcePackType.SERVER_DATA, Thread.currentThread());
        ResourcePackList<ResourcePackInfo> packList = new ResourcePackList<>(ResourcePackInfo::new);

        packList.addPackFinder(new ModDataPackFinder());
        // add global packs after mods so that they override
        packList.addPackFinder(new FolderDataPackFinder(dir));

        packList.reloadPacksFromFinders();
        packList.getEnabledPacks().stream().map(ResourcePackInfo::getResourcePack).forEach(manager::addResourcePack);

        return new DataManager(manager, packList);
    }
}
