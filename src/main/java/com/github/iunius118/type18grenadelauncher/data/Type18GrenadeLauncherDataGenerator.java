package com.github.iunius118.type18grenadelauncher.data;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.item.*;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class Type18GrenadeLauncherDataGenerator {
    public static class Recipes extends RecipeProvider implements IConditionBuilder {
        public Recipes(DataGenerator generatorIn) {
            super(generatorIn);
        }

        public void registerRecipes(Consumer<IFinishedRecipe> consumer) {
            // HE 40 mm Grenade Cartridge
            ShapedRecipeBuilder.shapedRecipe(Type18GrenadeLauncher.Items.GRENADE_40, 8)
                    .patternLine("P")
                    .patternLine("T")
                    .patternLine("g")
                    .key('P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
                    .key('T', Blocks.TNT)
                    .key('g', Tags.Items.GUNPOWDER)
                    .setGroup(Type18Grenade40Item.ID.toString())
                    .addCriterion("has_gunpowder", hasItem(Tags.Items.GUNPOWDER))
                    .build(consumer, Type18Grenade40Item.ID);

            // HE 51 mm Grenade Cartridge
            ShapedRecipeBuilder.shapedRecipe(Type18GrenadeLauncher.Items.GRENADE_51, 1)
                    .patternLine("4")
                    .patternLine("g")
                    .key('4', Type18GrenadeLauncher.Items.GRENADE_40)
                    .key('g', Tags.Items.GUNPOWDER)
                    .setGroup(Type18Grenade51Item.ID.toString())
                    .addCriterion("has_" + Type18Grenade40Item.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_40))
                    .build(consumer, Type18Grenade51Item.ID);

            // 40 mm Grenade Launcher
            ShapedRecipeBuilder.shapedRecipe(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER, 1)
                    .patternLine("B i")
                    .patternLine("Bi ")
                    .patternLine("iL ")
                    .key('i', Tags.Items.INGOTS_IRON)
                    .key('B', Items.IRON_BARS)
                    .key('L', Blocks.LEVER)
                    .setGroup(Type18GrenadeLauncherItem.ID.toString())
                    .addCriterion("has_iron_ingot", hasItem(Tags.Items.INGOTS_IRON))
                    .build(consumer, Type18GrenadeLauncherItem.ID);

            // 40 mm Revolver Grenade Launcher
            ShapedRecipeBuilder.shapedRecipe(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER_REVOLVER, 1)
                    .patternLine("iii")
                    .patternLine("i#i")
                    .patternLine("iii")
                    .key('i', Tags.Items.INGOTS_IRON)
                    .key('#', Type18GrenadeLauncher.Items.GRENADE_LAUNCHER)
                    .setGroup(Type18RevolverGrenadeLauncherItem.ID.toString())
                    .addCriterion("has_" + Type18GrenadeLauncherItem.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER))
                    .build(consumer, Type18RevolverGrenadeLauncherItem.ID);

            // 51 mm Light Mortar
            ShapedRecipeBuilder.shapedRecipe(Type18GrenadeLauncher.Items.GRENADE_DISCHARGER, 1)
                    .patternLine(" ii")
                    .patternLine(" #i")
                    .patternLine("i  ")
                    .key('i', Tags.Items.INGOTS_IRON)
                    .key('#', Type18GrenadeLauncher.Items.GRENADE_LAUNCHER)
                    .setGroup(Type18GrenadeDischargerItem.ID.toString())
                    .addCriterion("has_" + Type18GrenadeLauncherItem.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER))
                    .build(consumer, Type18GrenadeDischargerItem.ID);

            // * 40 mm Revolver Grenade Launcher Tactical Reload
            ShapelessRecipeBuilder.shapelessRecipe(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER_REVOLVER, 1)
                    .addIngredient(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER_REVOLVER)
                    .setGroup(Type18RevolverGrenadeLauncherItem.ID.toString() + "_2")
                    .addCriterion("has_" + Type18RevolverGrenadeLauncherItem.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER_REVOLVER))
                    .build(consumer, new ResourceLocation(Type18GrenadeLauncher.MOD_ID, Type18RevolverGrenadeLauncherItem.ID.getPath() + "_2"));

            // * 2 x HE 40 mm Grenade Cartridge -> HE 51 mm Grenade Cartridge
            ShapelessRecipeBuilder.shapelessRecipe(Type18GrenadeLauncher.Items.GRENADE_51, 1)
                    .addIngredient(Type18GrenadeLauncher.Items.GRENADE_40)
                    .addIngredient(Type18GrenadeLauncher.Items.GRENADE_40)
                    .setGroup(Type18Grenade51Item.ID.toString() + "_2")
                    .addCriterion("has_" + Type18Grenade40Item.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_40))
                    .build(consumer, new ResourceLocation(Type18GrenadeLauncher.MOD_ID, Type18Grenade51Item.ID.getPath() + "_2"));

            // * HE 51 mm Grenade Cartridge -> 2 x HE 40 mm Grenade Cartridge
            ShapelessRecipeBuilder.shapelessRecipe(Type18GrenadeLauncher.Items.GRENADE_40, 2)
                    .addIngredient(Type18GrenadeLauncher.Items.GRENADE_51)
                    .setGroup(Type18Grenade40Item.ID.toString() + "_2")
                    .addCriterion("has_" + Type18Grenade51Item.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_51))
                    .build(consumer, new ResourceLocation(Type18GrenadeLauncher.MOD_ID, Type18Grenade40Item.ID.getPath() + "_2"));

        }
    }
}
