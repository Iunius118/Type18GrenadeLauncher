package com.github.iunius118.type18grenadelauncher;

import net.minecraftforge.common.config.Config;

@Config(modid = Type18GrenadeLauncher.MOD_ID, category = "")
public class Type18GrenadeLauncherConfig {
    @Config.Comment("Common settings. If you are playing a multiplayer game, the server-side settings will be used.")
    @Config.LangKey("type18grenadelauncher.configgui.category.commonConfig")
    public static CommonConfig common = new CommonConfig();

    public static class CommonConfig {
        public boolean killGrenadeWhichEnteringUnloadedChunk = true;

        public int grenadeDamageLevel = 1;

        public boolean enableLog = true;
    }
}
