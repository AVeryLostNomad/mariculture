package com.thelostnomad.tone.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;

@JEIPlugin
public class ToneJEIIntegration implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        IRecipeTransferRegistry recipeTranferRegistry = registry.getRecipeTransferRegistry();

        //recipeTransferRegistry.addRecipeTransferHandler(null, VanillaRecipeCategoryUid.CRAFTING);
    }

}
