package com.github.iunius118.type18grenadelauncher.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class Type18GrenadeLauncherConfig {
    public static final ForgeConfigSpec commonSpec;
    public static final CommonConfig COMMON ;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        commonSpec = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static final ForgeConfigSpec clientSpec;
    public static final ClientConfig CLIENT;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class CommonConfig {
        public BooleanValue detonateWhenCannotUpdate;
        public BooleanValue enableLog;
        public IntValue grenadeDamageLevel;

        public final Type18Grenade40ItemConfig grenade40mm;
        public final Type18Grenade51ItemConfig grenade51mm;
        public final Type18GrenadeLauncherItemConfig launcher40mm;
        public final Type18RevolverGrenadeLauncherItemConfig launcher40mmRevolver;
        public final Type18GrenadeDischargerItemConfig mortar51mm;

        CommonConfig(ForgeConfigSpec.Builder builder) {
            builder .comment("Common Settings")
                    .push("common");

            detonateWhenCannotUpdate = builder
                    .comment("Detonate grenade when it cannot be updated by unloaded chunks.")
                    .translation("type18grenadelauncher.configgui.detonateWhenCannotUpdate")
                    .define("detonateWhenCannotUpdate", true);
            enableLog = builder
                    .comment("Enable to log grenade's launching and detonating.")
                    .translation("type18grenadelauncher.configgui.enableLog")
                    .define("enableLog", false);
            grenadeDamageLevel = builder
                    .comment("Set grenade's damage (0: none, 1: entities, 2: terrain and entities).")
                    .translation("type18grenadelauncher.configgui.grenadeDamageLevel")
                    .defineInRange("grenadeDamageLevel", 1, 0, 2);

            grenade40mm = new Type18Grenade40ItemConfig(builder);
            grenade51mm = new Type18Grenade51ItemConfig(builder);
            launcher40mm = new Type18GrenadeLauncherItemConfig(builder);
            launcher40mmRevolver = new Type18RevolverGrenadeLauncherItemConfig(builder);
            mortar51mm = new Type18GrenadeDischargerItemConfig(builder);

            builder.pop();
        }

        public static class Type18Grenade40ItemConfig {
            public DoubleValue explosivePower;

            public Type18Grenade40ItemConfig(ForgeConfigSpec.Builder builder) {
                builder .comment("HE 40 mm Grenade Cartridge Setting")
                        .push("grenade40mm");

                explosivePower = builder
                        .comment("Set explosive power of HE 40 mm Grenade Cartridge.")
                        .translation("type18grenadelauncher.configgui.grenade40.power")
                        .defineInRange("explosivePower", 3.4, 0.0, 10.0);

                builder.pop();
            }
        }

        public static class Type18Grenade51ItemConfig {
            public DoubleValue explosivePower;

            public Type18Grenade51ItemConfig(ForgeConfigSpec.Builder builder) {
                builder .comment("HE 51 mm Grenade Cartridge Setting")
                        .push("grenade51mm");

                explosivePower = builder
                        .comment("Set explosive power of HE 51 mm Grenade Cartridge.")
                        .translation("type18grenadelauncher.configgui.grenade51.power")
                        .defineInRange("explosivePower", 5.3, 0.0, 10.0);

                builder.pop();
            }
        }

        public static class Type18GrenadeLauncherItemConfig {
            public IntValue reloadingCoolDown;

            public Type18GrenadeLauncherItemConfig(ForgeConfigSpec.Builder builder) {
                builder .comment("40 mm Grenade Launcher Setting")
                        .push("launcher40mm");

                reloadingCoolDown = builder
                        .comment("Set cool-down time of reloading 40 mm Grenade Launcher in ticks.")
                        .translation("type18grenadelauncher.configgui.launcher40.coolDownReload")
                        .defineInRange("reloadingCoolDownTimeTicks", 160, 0, 72000);

                builder.pop();
            }
        }

        public static class Type18RevolverGrenadeLauncherItemConfig {
            public IntValue firingCoolDown;
            public IntValue reloadingCoolDown;

            public Type18RevolverGrenadeLauncherItemConfig(ForgeConfigSpec.Builder builder) {
                builder .comment("40 mm Revolver Grenade Launcher Settings")
                        .push("launcher40mmRevolver");

                firingCoolDown = builder
                        .comment("Set cool-down time of firing 40 mm Revolver Grenade Launcher in ticks.")
                        .translation("type18grenadelauncher.configgui.launcher40Revolver.coolDownFire")
                        .defineInRange("firingCoolDownTimeTicks", 10, 0, 72000);

                reloadingCoolDown = builder
                        .comment("Set cool-down time of reloading 40 mm Revolver Grenade Launcher in ticks.")
                        .translation("type18grenadelauncher.configgui.launcher40Revolver.coolDownReload")
                        .defineInRange("reloadingCoolDownTimeTicks", 250, 0, 72000);

                builder.pop();
            }
        }

        public static class Type18GrenadeDischargerItemConfig {
            public IntValue reloadingCoolDown;

            public Type18GrenadeDischargerItemConfig(ForgeConfigSpec.Builder builder) {
                builder .comment("51 mm Light Mortar Setting")
                        .push("mortar51mm");

                reloadingCoolDown = builder
                        .comment("Set cool-down time of reloading 51 mm Light Mortar in ticks.")
                        .translation("type18grenadelauncher.configgui.mortar51.coolDownReload")
                        .defineInRange("reloadingCoolDownTimeTicks", 40, 0, 72000);

                builder.pop();
            }
        }
    }

    public static class ClientConfig {
        public BooleanValue enableHUD;
        public BooleanValue enableRecoil;

        public Type18SightHUDConfig sightHUD;

        ClientConfig(ForgeConfigSpec.Builder builder) {
            builder .comment("Client Settings")
                    .push("client");

            enableHUD = builder
                    .comment("Enable to draw gun sight on HUD.")
                    .translation("type18grenadelauncher.configgui.enableHUD")
                    .define("enableHUD", true);

            enableRecoil = builder
                    .comment("Enable recoil of Grenade Launchers.")
                    .translation("type18grenadelauncher.configgui.enableRecoil")
                    .define("enableRecoil", true);

            sightHUD = new Type18SightHUDConfig(builder);

            builder.pop();
        }

        public static class Type18SightHUDConfig {
            public IntValue color;
            public ConfigValue<List<Double>> angleList;
            public ConfigValue<List<String>> rangeList;

            Type18SightHUDConfig(ForgeConfigSpec.Builder builder) {
                builder .comment("Sight Settings")
                        .push("sightHUD");

                color = builder
                        .comment("Set value of color (A8R8G8B8 format in 32-bit signed integer) for gun sight.")
                        .translation("type18grenadelauncher.configgui.sightHUD.color")
                        .defineInRange("color", -1, Integer.MIN_VALUE, Integer.MAX_VALUE);

                angleList = builder
                        .comment("Set values of angles in degrees for gun sight.")
                        .translation("type18grenadelauncher.configgui.sightHUD.listAngles")
                        .define("angleListDegrees", Arrays.asList(0.0, 5.3, 12.8, 28.0, 43.2, 63.8, 77.6, 90.0));

                rangeList = builder
                        .comment("Set strings of range for gun sight.")
                        .translation("type18grenadelauncher.configgui.sightHUD.listRange")
                        .define("rangeList", Arrays.asList("", "50", "100", "150", "150", "100", "50", "0"));

                builder.pop();
            }
        }
    }
}

