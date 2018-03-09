package com.thelostnomad.tone.integration;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.block.container.ContainerLivingCraftingStation;
import mezz.jei.api.*;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;

@JEIPlugin
public class ToneJEIIntegration implements IModPlugin {

    static IIngredientListOverlay jeiOverlay;
    static IJeiHelpers jeiHelpers;

    @Override
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();
        IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();

        recipeTransferRegistry.addRecipeTransferHandler(new LivingCraftingRecipeTransferHandler(), VanillaRecipeCategoryUid.CRAFTING);
    }

}
