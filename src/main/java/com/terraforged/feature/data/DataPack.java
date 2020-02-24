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
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
        return pack.getAllResourceLocations(type, namespace, path, 8, filenameMatcher);
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
