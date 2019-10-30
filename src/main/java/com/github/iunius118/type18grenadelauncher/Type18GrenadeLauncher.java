package com.github.iunius118.type18grenadelauncher;

import com.github.iunius118.type18grenadelauncher.client.ClientEventHandler;
import com.github.iunius118.type18grenadelauncher.client.renderer.entity.Type18GrenadeRenderer;
import com.github.iunius118.type18grenadelauncher.config.Type18GrenadeLauncherConfig;
import com.github.iunius118.type18grenadelauncher.data.Type18GrenadeLauncherDataGenerator;
import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import com.github.iunius118.type18grenadelauncher.item.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(Type18GrenadeLauncher.MOD_ID)
public class Type18GrenadeLauncher {
    public static final String MOD_ID = "type18grenadelauncher";
    public static final String MOD_NAME = "Type 18 Grenade Launcher";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public Type18GrenadeLauncher() {
        // Register lifecycle event listeners
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::preInit);
        modEventBus.addListener(this::initServer);
        modEventBus.addListener(this::initClient);
        modEventBus.addListener(this::postInit);
        modEventBus.register(Type18GrenadeLauncherConfig.class);

        // Register config handlers
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Type18GrenadeLauncherConfig.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Type18GrenadeLauncherConfig.clientSpec);

        // Register event handlers
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static class Items {
        public static final Item GRENADE_LAUNCHER = new Type18GrenadeLauncherItem().setRegistryName(Type18GrenadeLauncherItem.ID);
        public static final Item GRENADE_LAUNCHER_REVOLVER = new Type18RevolverGrenadeLauncherItem().setRegistryName(Type18RevolverGrenadeLauncherItem.ID);
        public static final Item GRENADE_DISCHARGER = new Type18GrenadeDischargerItem().setRegistryName(Type18GrenadeDischargerItem.ID);
        public static final Item GRENADE_40 = new Type18Grenade40Item().setRegistryName(Type18Grenade40Item.ID);
        public static final Item GRENADE_51 = new Type18Grenade51Item().setRegistryName(Type18Grenade51Item.ID);
    }

    public static class EntityTypes {
        public static EntityType<Type18GrenadeEntity> GRENADE;
    }

    private void preInit(final FMLCommonSetupEvent event) {

    }

    private void initServer(final FMLDedicatedServerSetupEvent event) {

    }

    private void initClient(final FMLClientSetupEvent event) {
        // Register client-side mod event handler
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        // Register Grenade entity renderer
        RenderingRegistry.registerEntityRenderingHandler(Type18GrenadeEntity.class, Type18GrenadeRenderer::new);
    }

    private void postInit(InterModProcessEvent event) {

    }

    @SubscribeEvent
    public void remapItems(RegistryEvent.MissingMappings<Item> mappings) {
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
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        ItemStack stackOut = event.getCrafting();


        if (stackOut.getItem() instanceof Type18RevolverGrenadeLauncherItem) {
            ((Type18RevolverGrenadeLauncherItem) stackOut.getItem()).onCrafting(event);
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    Items.GRENADE_LAUNCHER,
                    Items.GRENADE_LAUNCHER_REVOLVER,
                    Items.GRENADE_DISCHARGER,
                    Items.GRENADE_40,
                    Items.GRENADE_51
            );
        }

        @SubscribeEvent
        public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
            EntityTypes.GRENADE = EntityType.Builder.<Type18GrenadeEntity>create(Type18GrenadeEntity::new, EntityClassification.MISC).setCustomClientFactory(Type18GrenadeEntity::new).size(0.25F, 0.25F).setTrackingRange(256).setUpdateInterval(40).setShouldReceiveVelocityUpdates(true).build(Type18GrenadeEntity.ID.toString());
            EntityTypes.GRENADE.setRegistryName(Type18GrenadeEntity.ID);

            event.getRegistry().registerAll(
                    EntityTypes.GRENADE
            );
        }

        // Generate data
        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            if (event.includeServer()) {
                DataGenerator gen = event.getGenerator();
                gen.addProvider(new Type18GrenadeLauncherDataGenerator.Recipes(gen));
            }
        }
    }

    @SubscribeEvent
    public void onEnteringChunk(EntityEvent.EnteringChunk event) {
        Entity entity = event.getEntity();

        if (entity instanceof Type18GrenadeEntity
                && entity.world instanceof ServerWorld
                && Type18GrenadeLauncherConfig.COMMON.detonateWhenCannotUpdate.get()
                && !entity.world.getChunkProvider().isChunkLoaded(entity)) {
            // Grenade is entering unloaded chunk
            Type18GrenadeEntity grenadeEntity = (Type18GrenadeEntity) entity;
            // Detonate grenade
            grenadeEntity.onImpact(new EntityRayTraceResult(grenadeEntity));
        }
    }
}
