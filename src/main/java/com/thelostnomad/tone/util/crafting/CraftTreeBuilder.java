package com.thelostnomad.tone.util.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class CraftTreeBuilder {

    private static List<IRecipe> recipesLoaded;
    private static List<EquivalenceStack> equivalenceStacks;

    public static void loadRecipes() {
        Set<ResourceLocation> iterator = CraftingManager.REGISTRY.getKeys();
        recipesLoaded = new ArrayList<IRecipe>();
        for (ResourceLocation r : iterator) {
            IRecipe thisRecipe = CraftingManager.REGISTRY.getObject(r);
            recipesLoaded.add(thisRecipe);
            //System.out.println(thisRecipe.getRecipeOutput().getItem().getUnlocalizedName());
        }
    }

    public static List<IRecipe> getRecipe(ItemStack o){
        List<IRecipe> possibleRecipes = new ArrayList<>();
        for (IRecipe rec : recipesLoaded) {
            if(rec.getRecipeOutput().isItemEqual(o)){
                possibleRecipes.add(rec);
            }
        }
        return possibleRecipes;
    }

    private static int countIngredients(IRecipe recipe){
        Map<ItemStack[], Integer> ingredientMap = new HashMap<ItemStack[], Integer>();
        for(Ingredient i : recipe.getIngredients()){
            ItemStack[] match = i.getMatchingStacks();
            if(ingredientMap.containsKey(match)){
                ingredientMap.put(match, ingredientMap.get(match) + 1);
            }else{
                ingredientMap.put(match, 1);
            }
        }
        return ingredientMap.keySet().size();
    }

    public static void isolateEquivalenceStacks(){
        equivalenceStacks = new ArrayList<EquivalenceStack>();

        // Go through every single recipe
        for(IRecipe recipe : recipesLoaded){
            ItemStack thisItem = recipe.getRecipeOutput();
            // We have a recipe for this item.
            // How many ingredients does it have?

            if(countIngredients(recipe) != 1){
                // We have more than one/less than one ingredient. Disclude. Not an equivalence stack.
                continue;
            }

            boolean gotThisRecipe = false;
            // We might have an equivalence stack, but we must first ensure that you can craft the ingredients using thisItems
            for(ItemStack ig : recipe.getIngredients().get(0).getMatchingStacks()){
                // Go through the first stack of matching ingredients, since it's the only stack

                for(IRecipe testRecipe : getRecipe(ig)){
                    // See if this item only has one ingredient!
                    if(countIngredients(testRecipe) != 1){
                        continue;
                    }

                    // It does! Is that ingredient equal to this?
                    for(ItemStack isCompare : testRecipe.getIngredients().get(0).getMatchingStacks()){
                        if(isCompare.isItemEqual(thisItem)){
                            // By jove, we've got a match! This is an equivalence stack!
                            // TODO EQUISTACK
                            addEquivalenceStackIfNotExists(thisItem, ig);
                            gotThisRecipe = true;
                            break;
                        }
                    }
                    if(gotThisRecipe){
                        break;
                    }
                }
                if(gotThisRecipe){
                    break;
                }
            }
        }
    }

    private static void addEquivalenceStackIfNotExists(ItemStack ... listOfEquivalencies){
        // First ensure that we do not already have this equistack in place.
        for(EquivalenceStack es : equivalenceStacks){
            if(es.containsAll(listOfEquivalencies)){
                // This stack already exists entirely. Skip
                return;
            }
            if(es.containsAny(listOfEquivalencies)){
                // This stack contains one of our items, let's go ahead and add the other.
                es.addComponent();
            }
        }
    }


}
