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
