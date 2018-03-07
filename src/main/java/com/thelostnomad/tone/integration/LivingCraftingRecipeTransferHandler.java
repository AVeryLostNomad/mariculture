package com.thelostnomad.tone.integration;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerWorkbench;

import javax.annotation.Nullable;

public class LivingCraftingRecipeTransferHandler implements IRecipeTransferHandler<ContainerWorkbench> {

    @Override
    public Class<ContainerWorkbench> getContainerClass() {
        return ContainerWorkbench.class;
    }

    @Nullable
    @Override
    /**
     * In a Living Crafting Station, we follow this principle *almost* to a T. But there are settings here:
     * Based on the tile entity's settings, I may:
     *  - (Smart Recursive Craft) :
     *      Automatically craft all ingredients (if possible) needed to make this recipe
     *      If maxTransfer, do this literally as much as we can (up to a stack in each slot)
     *      If not, do it once
     *      If we can't do it at all, return an error of JEI *AND* log a message on the gui
     *          " We don't have enough .... ingredients .... "
     *      If done, fill the slots with the crafted sub-ingredients
     *  - (Standard Function) :
     *      Nothing is autocrafted. Doing the clicks will bring from inventory in the thing as normal.
     *
     * Because the living crafting table is connected to the tree, it will have a list of all contained items on the
     * gui.
     *
     * It can also automatically pull ingredients from the tree and put them in the resource grid.
     * It also has a dedicated slot on the gui that will send items placed into it back into the tree's storage hollows.
     * Shift clicking, by default, redirects items into that slot, *NOT* the crafting gui.
     *
     * The GUI also displays the number of craftoberries equipped to the tree. It can and will error if
     * you try to autocraft a recipe that uses too many craftoberries. Crafting through the tree "manually"
     * does give a 25% discount on the cost, fortunately.
     *
     * @param container    the container to act on
     * @param recipeLayout the layout of the recipe, with information about the ingredients
     * @param player       the player, to do the slot manipulation
     * @param maxTransfer  if true, transfer as many items as possible. if false, transfer one set
     * @param doTransfer   if true, do the transfer. if false, check for errors but do not actually transfer the items
     * @return a recipe transfer error if the recipe can't be transferred. Return null on success.
     * @since JEI 2.20.0
     */
    public IRecipeTransferError transferRecipe(ContainerWorkbench container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer, boolean doTransfer) {


        return null;
    }

}
