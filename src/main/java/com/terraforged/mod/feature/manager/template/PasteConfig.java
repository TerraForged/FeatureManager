package com.terraforged.mod.feature.manager.template;

import com.google.gson.JsonObject;
import com.terraforged.mod.feature.manager.util.Json;

public class PasteConfig {

    public static final PasteConfig DEFAULT = new PasteConfig(0, false, false, false);

    public final int baseDepth;
    public final boolean pasteAir;
    public final boolean checkBounds;
    public final boolean replaceSolid;

    public PasteConfig(int baseDepth, boolean pasteAir, boolean checkBounds, boolean replaceSolid) {
        this.checkBounds = checkBounds;
        this.replaceSolid = replaceSolid;
        this.baseDepth = baseDepth;
        this.pasteAir = pasteAir;
    }

    @Override
    public String toString() {
        return "PasteConfig{" +
                "baseDepth=" + baseDepth +
                ", pasteAir=" + pasteAir +
                ", checkBounds=" + checkBounds +
                ", replaceSolid=" + replaceSolid +
                '}';
    }

    public static PasteConfig parse(JsonObject config) {
        if (config != null) {
            int baseDepth = Json.getInt("extend_base", config, 0);
            boolean pasteAir = Json.getBool("paste_air", config, false);
            boolean checkBounds = Json.getBool("check_bounds", config, false);
            boolean replaceSolid = Json.getBool("replace_solid", config, false);
            return new PasteConfig(baseDepth, pasteAir, checkBounds, replaceSolid);
        }
        return PasteConfig.DEFAULT;
    }
}
