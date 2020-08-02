package com.terraforged.fm.data;

import net.minecraft.resources.FolderPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;
import org.jline.utils.Log;

import java.io.File;
import java.util.function.Consumer;

public class FolderDataPackFinder extends FolderPackFinder {

    public static final IPackNameDecorator TF_FOLDER = IPackNameDecorator.create("pack.source.folder");

    public FolderDataPackFinder(File folderIn) {
        this(folderIn, TF_FOLDER);
    }

    public FolderDataPackFinder(File folderIn, IPackNameDecorator decorator) {
        super(folderIn, decorator);
    }

    @Override
    public <T extends ResourcePackInfo> void func_230230_a_(Consumer<T> consumer, ResourcePackInfo.IFactory<T> factory) {
        Log.debug("Searching for DataPacks...");
        super.func_230230_a_(consumer, factory);
    }
}
