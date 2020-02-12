package com.terraforged.feature.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DataHelper {

    public static final Predicate<String> ANY = s -> true;
    public static final Predicate<String> NBT = s -> s.endsWith(".nbt");
    public static final Predicate<String> JSON = s -> s.endsWith(".json");

    private static final Supplier<IResourceManager> resourceManager = () -> {
        MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        return server.getResourceManager();
    };

    public static void iterateData(String path, ResourceVisitor<InputStream> consumer) {
        IResourceManager manager = getResourceManager();
        for (ResourceLocation location : manager.getAllResourceLocations(path, JSON)) {
            try (IResource resource = manager.getResource(location)) {
                consumer.accept(location, resource.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void iterateJson(String path, ResourceVisitor<JsonElement> consumer) {
        IResourceManager manager = getResourceManager();
        JsonParser parser = new JsonParser();
        for (ResourceLocation location : manager.getAllResourceLocations(path, JSON)) {
            try (IResource resource = manager.getResource(location)) {
                Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                JsonElement element = parser.parse(reader);
                consumer.accept(location, element);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static IResourceManager getResourceManager() {
        return resourceManager.get();
    }
}
