package com.thelostnomad.mariculture.core.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeHelper {
    public static void addSmelting(ItemStack output, ItemStack input, float xp) {
        FurnaceRecipes.instance().addSmeltingRecipe(input, output, xp);
    }

    private static Object[] changeValues(Object... original) {
        Object[] input = new Object[original.length];
        for (int i = 0; i < input.length; i++) {
            input[i] = changeValue(original[i]);
        }

        return input;
    }

    private static Object changeValue(Object object) {
        if (object instanceof String) {
            String string = (String) object;
            if (string.equals("ingotCopper") && !OreDictionary.doesOreNameExist("ingotCopper")) return "ingotIron";
            if (string.equals("ingotLead") && !OreDictionary.doesOreNameExist("ingotLead")) return "obsidian";
        }

        return object;
    }
}
