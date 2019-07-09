package com.github.iunius118.type18grenadelauncher;

import net.minecraftforge.common.config.Config;

@Config(modid = Type18GrenadeLauncher.MOD_ID, category = "")
public class Type18GrenadeLauncherConfig {
    @Config.LangKey("type18grenadelauncher.configgui.category.commonConfig")
    public static CommonConfig common = new CommonConfig();

    @Config.LangKey("type18grenadelauncher.configgui.category.clientConfig")
    public static ClientConfig client = new ClientConfig();

    public static class CommonConfig {
        @Config.Comment("Detonate grenade when it cannot be updated by unloaded chunks.")
        @Config.LangKey("type18grenadelauncher.configgui.detonateWhenCannotUpdate")
        public boolean detonateWhenCannotUpdate = true;

        @Config.Comment("Enable to log grenade's launching and detonating.")
        @Config.LangKey("type18grenadelauncher.configgui.enableLog")
        public boolean enableLog = false;

        @Config.Comment("Set grenade's damage (0: none, 1: entities, 2: terrain and entities).")
        @Config.LangKey("type18grenadelauncher.configgui.grenadeDamageLevel")
        @Config.RangeInt(min = 0, max = 2)
        public int grenadeDamageLevel = 1;
    }

    public static class ClientConfig {
        @Config.Comment("Disable recoil of Grenade Launcher.")
        @Config.LangKey("type18grenadelauncher.configgui.disableRecoil")
        public boolean disableRecoil = false;
    }
}
