package com.github.iunius118.type18grenadelauncher;

import net.minecraftforge.common.config.Config;

@Config(modid = Type18GrenadeLauncher.MOD_ID, category = "")
public class Type18GrenadeLauncherConfig {
    @Config.LangKey("type18grenadelauncher.configgui.category.clientConfig")
    public static ClientConfig client = new ClientConfig();

    @Config.LangKey("type18grenadelauncher.configgui.category.commonConfig")
    public static CommonConfig common = new CommonConfig();

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

        @Config.LangKey("type18grenadelauncher.configgui.category.grenade40")
        public Type18Grenade40ItemConfig grenade40mm = new Type18Grenade40ItemConfig();

        @Config.LangKey("type18grenadelauncher.configgui.category.grenade51")
        public Type18Grenade51ItemConfig grenade51mm = new Type18Grenade51ItemConfig();

        @Config.LangKey("type18grenadelauncher.configgui.category.launcher40")
        public Type18GrenadeLauncherItemConfig launcher40mm = new Type18GrenadeLauncherItemConfig();

        @Config.LangKey("type18grenadelauncher.configgui.category.launcher40Revolver")
        public Type18RevolverGrenadeLauncherItemConfig launcher40mmRevolver = new Type18RevolverGrenadeLauncherItemConfig();

        @Config.LangKey("type18grenadelauncher.configgui.category.mortar51")
        public Type18GrenadeDischargerItemConfig mortar51mm = new Type18GrenadeDischargerItemConfig();

        public static class Type18Grenade40ItemConfig {
            @Config.Comment("Set explosive power of HE 40 mm Grenade Cartridge.")
            @Config.LangKey("type18grenadelauncher.configgui.grenade40.power")
            @Config.RangeDouble(min = 0.0D, max = 10.0D)
            public double explosivePower = 3.4D;
        }

        public static class Type18Grenade51ItemConfig {
            @Config.Comment("Set explosive power of HE 51 mm Grenade Cartridge.")
            @Config.LangKey("type18grenadelauncher.configgui.grenade51.power")
            @Config.RangeDouble(min = 0.0D, max = 10.0D)
            public double explosivePower = 5.3D;
        }

        public static class Type18GrenadeLauncherItemConfig {
            @Config.Comment("Set cool-down time of reloading 40 mm Grenade Launcher in ticks.")
            @Config.LangKey("type18grenadelauncher.configgui.launcher40.coolDownReload")
            public int coolDownReload = 160;
        }

        public static class Type18RevolverGrenadeLauncherItemConfig {
            @Config.Comment("Set cool-down time of firing 40 mm Revolver Grenade Launcher in ticks.")
            @Config.LangKey("type18grenadelauncher.configgui.launcher40Revolver.coolDownFire")
            public int coolDownFire = 10;

            @Config.Comment("Set cool-down time of reloading 40 mm Revolver Grenade Launcher in ticks.")
            @Config.LangKey("type18grenadelauncher.configgui.launcher40Revolver.coolDownReload")
            public int coolDownReload = 250;
        }

        public static class Type18GrenadeDischargerItemConfig {
            @Config.Comment("Set cool-down time of reloading 51 mm Light Mortar in ticks.")
            @Config.LangKey("type18grenadelauncher.configgui.mortar51.coolDownReload")
            public int coolDownReload = 40;
        }
    }

    public static class ClientConfig {
        @Config.Comment("Disable to draw gun sight on HUD.")
        @Config.LangKey("type18grenadelauncher.configgui.disableHUD")
        public boolean disableHUD = false;

        @Config.Comment("Disable recoil of Grenade Launcher.")
        @Config.LangKey("type18grenadelauncher.configgui.disableRecoil")
        public boolean disableRecoil = false;

        @Config.LangKey("type18grenadelauncher.configgui.category.gunSight")
        public Type18SightHUDConfig gunSight = new Type18SightHUDConfig();

        public static class Type18SightHUDConfig {
            @Config.Comment("Set value of color (A8R8G8B8 format in 32-bit signed integer) for gun sight.")
            @Config.LangKey("type18grenadelauncher.configgui.sightHUD.color")
            public int color = -1;

            @Config.Comment("Set values of angles for gun sight.")
            @Config.LangKey("type18grenadelauncher.configgui.sightHUD.listAngles")
            public double[] listAngles = {0.0D, 5.3D, 12.8D, 28.0D, 43.2D, 63.8D, 77.6D, 90.0D};

            @Config.Comment("Set strings of range for gun sight.")
            @Config.LangKey("type18grenadelauncher.configgui.sightHUD.listRange")
            public String[] listRange = {"", "50", "100", "150", "150", "100", "50", "0"};
        }
    }
}

