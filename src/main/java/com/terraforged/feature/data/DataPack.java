package com.terraforged.feature.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;

import java.io.*;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public class DataPack {

    private final String namespace;
    private final ResourcePackType type;
    private final ModFileResourcePack pack;

    public DataPack(String namespace, ModFileResourcePack pack) {
        this.pack = pack;
        this.namespace = namespace;
        this.type = ResourcePackType.SERVER_DATA;
    }

    public Collection<ResourceLocation> getAllResources(String path, Predicate<String> filenameMatcher) {
        return pack.func_225637_a_(type, namespace, path, 8, filenameMatcher);
    }

    public InputStream getResource(ResourceLocation location) throws IOException {
        return pack.getResourceStream(type, location);
    }

    public void iterateData(String path, Predicate<String> matcher, ResourceVisitor<InputStream> consumer) {
        try {
            for (ResourceLocation location : getAllResources(path, matcher)) {
                try (InputStream data = getResource(location)) {
                    consumer.accept(location, data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void iterateJson(String path, ResourceVisitor<JsonElement> consumer) {
        JsonParser parser = new JsonParser();
        iterateData(path, DataHelper.JSON, (location, data) -> {
            Reader reader = new BufferedReader(new InputStreamReader(data));
            JsonElement element = parser.parse(reader);
            consumer.accept(location, element);
        });
    }

    public static Optional<DataPack> getModDataPack(String modid) {
        return ModList.get().getModContainerById(modid).map(container -> {
            ModFileInfo fileInfo = ModList.get().getModFileById(modid);
            return new DataPack(container.getNamespace(), new ModFileResourcePack(fileInfo.getFile()));
        });
    }
}
