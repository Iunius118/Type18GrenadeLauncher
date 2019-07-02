package com.github.iunius118.type18grenadelauncher;

import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Type18GrenadeLauncher.MOD_ID,
        name = Type18GrenadeLauncher.MOD_NAME,
        version = Type18GrenadeLauncher.MOD_VERSION,
        dependencies = Type18GrenadeLauncher.MOD_DEPENDENCIES)
@Mod.EventBusSubscriber
public class Type18GrenadeLauncher {
    public static final String MOD_ID = "type18grenadelauncher";
    public static final String MOD_NAME = "Type 18 Grenade Launcher";
    public static final String MOD_VERSION = "1.12-2-1.0.0.0";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.12.2-14.23.3.2768,)";

    public static final boolean DEBUG = true;

    public static Logger logger;
    public static final Type18GrenadeLauncherConfig config = new Type18GrenadeLauncherConfig();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {

    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(
                EntityEntryBuilder.create().entity(Type18GrenadeEntity.class).id(Type18GrenadeEntity.ID, 0).name(Type18GrenadeEntity.NAME).tracker(256, 40, true).build()
        );
    }

    @SubscribeEvent
    public static void onEnteringChunk(EntityEvent.EnteringChunk event) {
        Entity entity = event.getEntity();
        World world = entity.world;

        if (!world.isRemote && entity instanceof Type18GrenadeEntity) {
            if (Type18GrenadeLauncher.config.common.killGrenadeWhichEnteringUnloadedChunk) {
                ChunkProviderServer chunkProvider = (ChunkProviderServer) world.getChunkProvider();

                if (!chunkProvider.chunkExists(event.getNewChunkX(), event.getOldChunkZ())) {
                    // Grenade is entering unloaded chunk
                    Type18GrenadeEntity grenadeEntity = (Type18GrenadeEntity) entity;
                    grenadeEntity.setDead();
                    grenadeEntity.logOnDead("onEnteringUnloadedChunk", new Vec3d(grenadeEntity.posX, grenadeEntity.posY, grenadeEntity.posZ));
                }
            }
        }
    }
}