package com.github.iunius118.type18grenadelauncher.data;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.item.*;
import net.minecraft.block.Blocks;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class Type18GrenadeLauncherDataGenerator {
    public static class Recipes extends RecipeProvider implements IConditionBuilder {
        public Recipes(net.minecraft.data.DataGenerator generatorIn) {
            super(generatorIn);
        }

        public void registerRecipes(Consumer<IFinishedRecipe> consumer) {
            ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "conditional");

            // HE 40 mm Grenade Cartridge
            ConditionalRecipe.builder()
                    .addCondition(TRUE())
                    .addRecipe(
                            ShapedRecipeBuilder.shapedRecipe(Type18GrenadeLauncher.Items.GRENADE_40, 8)
                                    .patternLine("P")
                                    .patternLine("T")
                                    .patternLine("g")
                                    .key('P', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
                                    .key('T', Blocks.TNT)
                                    .key('g', Tags.Items.GUNPOWDER)
                                    .setGroup(Type18Grenade40Item.ID.toString())
                                    .addCriterion("has_gunpowder", hasItem(Tags.Items.GUNPOWDER))
                                    ::build
                    )
                    .build(consumer, Type18Grenade40Item.ID);

            // HE 51 mm Grenade Cartridge
            ConditionalRecipe.builder()
                    .addCondition(TRUE())
                    .addRecipe(
                            ShapedRecipeBuilder.shapedRecipe(Type18GrenadeLauncher.Items.GRENADE_51, 1)
                                    .patternLine("4")
                                    .patternLine("g")
                                    .key('4', Type18GrenadeLauncher.Items.GRENADE_40)
                                    .key('g', Tags.Items.GUNPOWDER)
                                    .setGroup(Type18Grenade51Item.ID.toString())
                                    .addCriterion("has_" + Type18Grenade40Item.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_40))
                                    ::build
                    )
                    .build(consumer, Type18Grenade51Item.ID);

            // 40 mm Grenade Launcher
            ConditionalRecipe.builder()
                    .addCondition(TRUE())
                    .addRecipe(
                            ShapedRecipeBuilder.shapedRecipe(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER, 1)
                                    .patternLine("B i")
                                    .patternLine("Bi ")
                                    .patternLine("iL ")
                                    .key('i', Tags.Items.INGOTS_IRON)
                                    .key('B', Items.IRON_BARS)
                                    .key('L', Blocks.LEVER)
                                    .setGroup(Type18GrenadeLauncherItem.ID.toString())
                                    .addCriterion("has_iron_ingot", hasItem(Tags.Items.INGOTS_IRON))
                                    ::build
                    )
                    .build(consumer, Type18GrenadeLauncherItem.ID);

            // 40 mm Revolver Grenade Launcher
            ConditionalRecipe.builder()
                    .addCondition(TRUE())
                    .addRecipe(
                            ShapedRecipeBuilder.shapedRecipe(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER_REVOLVER, 1)
                                    .patternLine("iii")
                                    .patternLine("i#i")
                                    .patternLine("iii")
                                    .key('i', Tags.Items.INGOTS_IRON)
                                    .key('#', Type18GrenadeLauncher.Items.GRENADE_LAUNCHER)
                                    .setGroup(Type18RevolverGrenadeLauncherItem.ID.toString())
                                    .addCriterion("has_" + Type18GrenadeLauncherItem.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER))
                                    ::build
                    )
                    .build(consumer, Type18RevolverGrenadeLauncherItem.ID);

            // 51 mm Light Mortar
            ConditionalRecipe.builder()
                    .addCondition(TRUE())
                    .addRecipe(
                            ShapedRecipeBuilder.shapedRecipe(Type18GrenadeLauncher.Items.GRENADE_DISCHARGER, 1)
                                    .patternLine(" ii")
                                    .patternLine(" #i")
                                    .patternLine("i  ")
                                    .key('i', Tags.Items.INGOTS_IRON)
                                    .key('#', Type18GrenadeLauncher.Items.GRENADE_LAUNCHER)
                                    .setGroup(Type18GrenadeDischargerItem.ID.toString())
                                    .addCriterion("has_" + Type18GrenadeLauncherItem.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER))
                                    ::build
                    )
                    .build(consumer, Type18GrenadeDischargerItem.ID);

            // * 40 mm Revolver Grenade Launcher Tactical Reload
            ConditionalRecipe.builder()
                    .addCondition(TRUE())
                    .addRecipe(
                            ShapelessRecipeBuilder.shapelessRecipe(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER_REVOLVER, 1)
                                    .addIngredient(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER_REVOLVER)
                                    .setGroup(Type18RevolverGrenadeLauncherItem.ID.toString() + "_2")
                                    .addCriterion("has_" + Type18RevolverGrenadeLauncherItem.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER_REVOLVER))
                                    ::build
                    )
                    .build(consumer, Type18GrenadeLauncher.MOD_ID, Type18RevolverGrenadeLauncherItem.ID.getPath() + "_2");

            // * 2 x HE 40 mm Grenade Cartridge -> HE 51 mm Grenade Cartridge
            ConditionalRecipe.builder()
                    .addCondition(TRUE())
                    .addRecipe(
                            ShapelessRecipeBuilder.shapelessRecipe(Type18GrenadeLauncher.Items.GRENADE_51, 1)
                                    .addIngredient(Type18GrenadeLauncher.Items.GRENADE_40)
                                    .addIngredient(Type18GrenadeLauncher.Items.GRENADE_40)
                                    .setGroup(Type18Grenade51Item.ID.toString() + "_2")
                                    .addCriterion("has_" + Type18Grenade40Item.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_40))
                                    ::build
                    )
                    .build(consumer, Type18GrenadeLauncher.MOD_ID, Type18Grenade51Item.ID.getPath() + "_2");

            // * HE 51 mm Grenade Cartridge -> 2 x HE 40 mm Grenade Cartridge
            ConditionalRecipe.builder()
                    .addCondition(TRUE())
                    .addRecipe(
                            ShapelessRecipeBuilder.shapelessRecipe(Type18GrenadeLauncher.Items.GRENADE_40, 2)
                                    .addIngredient(Type18GrenadeLauncher.Items.GRENADE_51)
                                    .setGroup(Type18Grenade40Item.ID.toString() + "_2")
                                    .addCriterion("has_" + Type18Grenade51Item.ID.toString(), hasItem(Type18GrenadeLauncher.Items.GRENADE_51))
                                    ::build
                    )
                    .build(consumer, Type18GrenadeLauncher.MOD_ID, Type18Grenade40Item.ID.getPath() + "_2");

        }
    }
}
