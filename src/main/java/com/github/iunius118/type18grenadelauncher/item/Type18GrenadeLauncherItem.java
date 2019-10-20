package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.config.Type18GrenadeLauncherConfig;
import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Type18GrenadeLauncherItem extends Item {
    public static Item.Properties properties = (new Item.Properties()).setNoRepair().group(ItemGroup.COMBAT).maxStackSize(1);
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "grenade_launcher");
    public static final float INACCURACY = 1.0F;

    public Type18GrenadeLauncherItem() {
        super(properties);
    }

    public ItemStack findAmmo(PlayerEntity player) {
        ItemStack stackAmmo = ItemStack.EMPTY;

        if (this.isAmmo(player.getHeldItem(Hand.OFF_HAND))) {
            stackAmmo = player.getHeldItem(Hand.OFF_HAND);

        } else if (this.isAmmo(player.getHeldItem(Hand.MAIN_HAND))) {
            stackAmmo = player.getHeldItem(Hand.MAIN_HAND);

        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isAmmo(itemstack)) {
                    stackAmmo = itemstack;
                    break;
                }
            }
        }

        if (player.abilities.isCreativeMode || !Type18GrenadeEntity.isPermittedDamage(Type18GrenadeEntity.DamageLevel.ENTITY)) {
            // If player is creative mode or grenadeDamageLevel is 0, grenade won't be consumed
            if (stackAmmo.isEmpty()) {
                // If player has no ammo and is creative mode, return default ammo
                stackAmmo = new ItemStack(getDefaultAmmoItem());
            } else {
                stackAmmo = new ItemStack(stackAmmo.getItem());
            }
        }

        return stackAmmo;
    }

    public boolean isAmmo(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof Type18Grenade40Item);
    }

    public Item getDefaultAmmoItem() {
        return Type18GrenadeLauncher.Items.GRENADE_40;
    }

    public Type18GrenadeEntity getAmmoEntityAndConsumeAmmo(ItemStack stack, World world, LivingEntity thrower) {
        Item item = stack.getItem();

        if (item instanceof Type18GrenadeItem) {
            stack.shrink(1);
            return ((Type18GrenadeItem) item).getEntity(world, thrower);
        }

        return null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (!worldIn.isRemote) {
            // Server side
            if (!canLaunch(worldIn, playerIn, handIn)) {
                return new ActionResult<>(ActionResultType.PASS, stack);
            }

            // Get ammo from player's inventory
            ItemStack stackAmmo = this.findAmmo(playerIn);

            if (stackAmmo.isEmpty()) {
                return new ActionResult<>(ActionResultType.PASS, stack);
            }

            Type18GrenadeEntity entity = getAmmoEntityAndConsumeAmmo(stackAmmo, worldIn, playerIn);

            if (entity != null) {
                if (!Type18GrenadeEntity.isPermittedDamage(Type18GrenadeEntity.DamageLevel.ENTITY)) {
                    // If config of grenadeDamageLevel is 0, grenade won't cause any damage
                    entity.power = 0.0F;
                }

                Vec3d posEntity = entity.getPositionVector();

                // Launch grenade
                entity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, entity.getInitialVelocity(), getInaccuracy(worldIn, playerIn, handIn));
                worldIn.addEntity(entity);
                entity.logInfo("+Launched", posEntity, entity.getMotion().normalize());

                // Generate sound
                playFiringSound(worldIn, playerIn, handIn);

                // Cool-down
                coolDown(worldIn, playerIn, handIn);
            }
        } else {
            // Client side
            playerIn.swingArm(handIn);

            if (Type18GrenadeLauncherConfig.CLIENT.enableRecoil.get()) {
                recoil(worldIn, playerIn, handIn);
            }
        }

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }

    public boolean canLaunch(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return !(playerIn.areEyesInFluid(FluidTags.WATER) || playerIn.areEyesInFluid(FluidTags.LAVA));
    }

    public float getInaccuracy(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return INACCURACY;
    }

    public void playFiringSound(World worldIn, PlayerEntity playerIn, Hand handIn) {
        Vec3d soundPos = playerIn.getLook(1.0F).scale(2.0D).add(playerIn.posX, playerIn.posY + playerIn.getEyeHeight(), playerIn.posZ);
        worldIn.playSound(null, soundPos.x, soundPos.y, soundPos.z, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.PLAYERS, 0.5F, (1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.2F) * 0.7F);
    }

    public void coolDown(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.getCooldownTracker().setCooldown(playerIn.getHeldItem(handIn).getItem(), Type18GrenadeLauncherConfig.COMMON.launcher40mm.reloadingCoolDown.get());
    }

    public void recoil(World worldIn, PlayerEntity playerIn, Hand handIn) {
        float recoilYaw = (playerIn.getRNG().nextFloat() * 2.5F - 0.5F) * (handIn == Hand.MAIN_HAND ? 1.0F : -1.0F) * (playerIn.getPrimaryHand() == HandSide.RIGHT ? 1.0F : -1.0F);
        playerIn.rotationYaw += recoilYaw;
        float recoilPitch = playerIn.getRNG().nextFloat() -0.5F;
        playerIn.rotationPitch += recoilPitch;
    }
}
