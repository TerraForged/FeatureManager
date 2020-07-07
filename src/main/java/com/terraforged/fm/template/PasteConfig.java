package com.terraforged.fm.template;

import com.google.gson.JsonObject;
import com.terraforged.fm.util.Json;

public class PasteConfig {

    public static final PasteConfig DEFAULT = new PasteConfig(0, false, false, false, false);

    public final int baseDepth;
    public final boolean pasteAir;
    public final boolean checkBounds;
    public final boolean replaceSolid;
    public final boolean updatePostPaste;

    public PasteConfig(int baseDepth, boolean pasteAir, boolean checkBounds, boolean replaceSolid, boolean updatePostPaste) {
        this.checkBounds = checkBounds;
        this.replaceSolid = replaceSolid;
        this.baseDepth = baseDepth;
        this.pasteAir = pasteAir;
        this.updatePostPaste = updatePostPaste;
    }

    @Override
    public String toString() {
        return "PasteConfig{" +
                "baseDepth=" + baseDepth +
                ", pasteAir=" + pasteAir +
                ", checkBounds=" + checkBounds +
                ", replaceSolid=" + replaceSolid +
                ", updatePostPaste=" + updatePostPaste +
                '}';
    }

    public static PasteConfig parse(JsonObject config) {
        if (config != null) {
            int baseDepth = Json.getInt("extend_base", config, 0);
            boolean pasteAir = Json.getBool("paste_air", config, false);
            boolean checkBounds = Json.getBool("check_bounds", config, false);
            boolean replaceSolid = Json.getBool("replace_solid", config, false);
            boolean updatePostPaste = Json.getBool("update_post_paste", config, false);
            return new PasteConfig(baseDepth, pasteAir, checkBounds, replaceSolid, updatePostPaste);
        }
        return PasteConfig.DEFAULT;
    }
}
