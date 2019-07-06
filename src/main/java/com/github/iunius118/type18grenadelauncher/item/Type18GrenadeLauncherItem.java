package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncherConfig;
import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Type18GrenadeLauncherItem extends Item {
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "type_18_grenade_launcher");
    public static final int COOL_DOWN = 160;

    public Type18GrenadeLauncherItem() {
        this.setCreativeTab(CreativeTabs.COMBAT);
    }

    public ItemStack findAmmo(EntityPlayer player) {
        ItemStack stackAmmo = ItemStack.EMPTY;

        if (this.isAmmo(player.getHeldItem(EnumHand.OFF_HAND))) {
            stackAmmo = player.getHeldItem(EnumHand.OFF_HAND);

        } else if (this.isAmmo(player.getHeldItem(EnumHand.MAIN_HAND))) {
            stackAmmo = player.getHeldItem(EnumHand.MAIN_HAND);

        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isAmmo(itemstack)) {
                    stackAmmo = itemstack;
                    break;
                }
            }
        }

        if (player.capabilities.isCreativeMode || !Type18GrenadeEntity.isPermittedDamage(Type18GrenadeEntity.DamageLevel.ENTITY)) {
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
        return Type18GrenadeLauncher.Items.TYPE_18_GRENADE_40;
    }

    public Type18GrenadeEntity getAmmoEntityAndConsumeAmmo(ItemStack stack, World world, EntityLivingBase thrower) {
        Item item = stack.getItem();

        if (item instanceof Type18GrenadeItem) {
            stack.shrink(1);
            return ((Type18GrenadeItem) item).getEntity(world, thrower);
        }

        return null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (!worldIn.isRemote) {
            // Server side
            if (!canLaunch(worldIn, playerIn, handIn)) {
                return new ActionResult<>(EnumActionResult.PASS, stack);
            }

            // Get ammo from player's inventory
            ItemStack stackAmmo = this.findAmmo(playerIn);

            if (stackAmmo.isEmpty()) {
                return new ActionResult<>(EnumActionResult.PASS, stack);
            }

            Type18GrenadeEntity entity = getAmmoEntityAndConsumeAmmo(stackAmmo, worldIn, playerIn);

            if (entity != null) {
                if (!Type18GrenadeEntity.isPermittedDamage(Type18GrenadeEntity.DamageLevel.ENTITY)) {
                    // If config of grenadeDamageLevel is 0, grenade won't cause any damage
                    entity.power = 0.0F;
                }

                Vec3d posEntity = entity.getPositionVector();

                // Shoot grenade
                entity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYawHead, 0.0F, entity.getInitialVelocity(), entity.getInaccuracy());
                worldIn.spawnEntity(entity);
                entity.logInfo("+Launched", posEntity, entity.getForward());

                // Generate sound
                worldIn.playSound(null, posEntity.x, posEntity.y, posEntity.z, SoundEvents.ENTITY_FIREWORK_BLAST, SoundCategory.AMBIENT, 0.5F, (1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.2F) * 0.7F);

                // Set cool down
                playerIn.getCooldownTracker().setCooldown(stack.getItem(), this.getCoolDownTime());
            }
        } else {
            playerIn.swingArm(handIn);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    public int getCoolDownTime() {
        return COOL_DOWN;
    }

    public boolean canLaunch(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        return playerIn.getCooldownTracker().getCooldown(stack.getItem(), 0.0F) == 0.0F;
    }
}
