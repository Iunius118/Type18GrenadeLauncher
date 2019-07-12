package com.github.iunius118.type18grenadelauncher;

import com.github.iunius118.type18grenadelauncher.client.ClientEventHandler;
import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import com.github.iunius118.type18grenadelauncher.item.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(
        modid = Type18GrenadeLauncher.MOD_ID,
        name = Type18GrenadeLauncher.MOD_NAME,
        version = Type18GrenadeLauncher.MOD_VERSION,
        dependencies = Type18GrenadeLauncher.MOD_DEPENDENCIES)
@Mod.EventBusSubscriber
public class Type18GrenadeLauncher {
    public static final String MOD_ID = "type18grenadelauncher";
    public static final String MOD_NAME = "Type 18 Grenade Launcher";
    public static final String MOD_VERSION = "1.12-2-1.1.1.1";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.12.2-14.23.3.2768,)";

    public static final boolean DEBUG = false;

    public static Logger logger;
    public static final Type18GrenadeLauncherConfig config = new Type18GrenadeLauncherConfig();

    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Items {
        public static final Item GRENADE_LAUNCHER = null;
        public static final Item GRENADE_LAUNCHER_REVOLVER = null;
        public static final Item GRENADE_DISCHARGER = null;
        public static final Item GRENADE_40 = null;
        public static final Item GRENADE_51 = null;
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        if (event.getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        }
    }

    @SubscribeEvent
    public static void remapItems(RegistryEvent.MissingMappings<Item> mappings) {
        final Map<ResourceLocation, Item> remappingItemMap = new HashMap<>();
        remappingItemMap.put(new ResourceLocation(MOD_ID, "type_18_grenade_launcher"), Items.GRENADE_LAUNCHER);
        remappingItemMap.put(new ResourceLocation(MOD_ID, "type_18_grenade_discharger"), Items.GRENADE_DISCHARGER);
        remappingItemMap.put(new ResourceLocation(MOD_ID, "type_18_grenade_40"), Items.GRENADE_40);
        remappingItemMap.put(new ResourceLocation(MOD_ID, "type_18_grenade_51"), Items.GRENADE_51);

        // Replace item ID v1.12-2-1.0.0.0 with v1.12-2-1.0.1.0
        mappings.getAllMappings().stream()
                .filter(mapping -> mapping.key.getNamespace().equals(MOD_ID) && remappingItemMap.containsKey(mapping.key))
                .forEach(mapping -> mapping.remap(remappingItemMap.get(mapping.key)));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new Type18GrenadeLauncherItem().setRegistryName(Type18GrenadeLauncherItem.ID).setTranslationKey(Type18GrenadeLauncher.MOD_ID + "." + Type18GrenadeLauncherItem.ID.getPath()),
                new Type18RevolverGrenadeLauncherItem().setRegistryName(Type18RevolverGrenadeLauncherItem.ID).setTranslationKey(Type18GrenadeLauncher.MOD_ID + "." + Type18RevolverGrenadeLauncherItem.ID.getPath()),
                new Type18GrenadeDischargerItem().setRegistryName(Type18GrenadeDischargerItem.ID).setTranslationKey(Type18GrenadeLauncher.MOD_ID + "." + Type18GrenadeDischargerItem.ID.getPath()),
                new Type18Grenade40Item().setRegistryName(Type18Grenade40Item.ID).setTranslationKey(Type18GrenadeLauncher.MOD_ID + "." + Type18Grenade40Item.ID.getPath()),
                new Type18Grenade51Item().setRegistryName(Type18Grenade51Item.ID).setTranslationKey(Type18GrenadeLauncher.MOD_ID + "." + Type18Grenade51Item.ID.getPath())
        );

    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(
                EntityEntryBuilder.create().entity(Type18GrenadeEntity.class).id(Type18GrenadeEntity.ID, 0).name(Type18GrenadeEntity.NAME).tracker(256, 40, true).build()
        );
    }

    @SubscribeEvent
    public static void canEntityUpdate(EntityEvent.CanUpdate event) {
        Entity entity = event.getEntity();

        if (entity instanceof Type18GrenadeEntity
                && !entity.world.isRemote
                && Type18GrenadeLauncher.config.common.detonateWhenCannotUpdate
                && !event.getCanUpdate()) {
            // Grenade is entering unloaded chunk
            Type18GrenadeEntity grenadeEntity = (Type18GrenadeEntity) entity;
            // Detonate grenade
            grenadeEntity.onImpact(new RayTraceResult(grenadeEntity));
        }
    }
}
