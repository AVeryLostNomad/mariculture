package com.thelostnomad.tone.util.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// An set of items make up an equivalence stack if the individual items
// can be used to make all other items in a set *without* any other
// ingredients.

// Redstone and redstone blocks, for example, are computationally *the same*
// but things like alchemical blocks (Which require a philosopher's stone each
// craft) are not.

// This allows us to calculate craft trees with swappable ingredients.
// -> You don't need 4 gold ingots if you have a gold block to craft with
public class EquivalenceStack {

    // Hold all forms that this stack can take
    private Map<Item, Integer> amountsByType = new HashMap<Item, Integer>();
    private Map<Item, ArrayList<ConversionSpec>> conversions = new HashMap<Item, ArrayList<ConversionSpec>>();

    public boolean hasAmount(Item requested, int amount){
        // Does this stack equate to the requested item
        if(!amountsByType.containsKey(requested)) return false;

        // Do we already have the perfect amount of this item?
        if(amountsByType.get(requested) >= amount) return true;

        // Can we convert to it?
        for(Map.Entry<Item, ArrayList<ConversionSpec>> e : conversions.entrySet()){
            if(e.getKey().equals(requested)) continue; // We would have caught this if we could.

            for(ConversionSpec cs : e.getValue()){
                if(cs.convertingTo.equals(requested)){
                    // This is a conversion spec to the target value.
                    int neededConversions = (int) Math.ceil((double) amount / (double) cs.amtToMake);
                    int neededMaterials = cs.amtToTake * neededConversions;

                    if(amountsByType.get(e.getKey()) >= neededMaterials){
                        // We have enough to convert to make this item
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Add a thing to the amountsByType list
    public void addTemplateComponent(ItemStack[] stacks){
        for(ItemStack stack : stacks){
            for(Item s : amountsByType.keySet()){
                if(stack.getItem().equals(s)){
                    continue;
                }
            }

            amountsByType.put(stack.getItem(), 0);
            return;
        }
    }

    private ItemStack getIngredientsNeeded(Item i){
        int smallest = Integer.MAX_VALUE;
        Item igType = null;
        List<IRecipe> recipes = CraftTreeBuilder.getRecipe(new ItemStack(i));
        for(IRecipe recipe : recipes){
            int ingrdAmt = 0;
            if(ingrdAmt < smallest){
                smallest = ingrdAmt;
            }
            for(Ingredient ig : recipe.getIngredients()){
                if(ig.getMatchingStacks() == null) continue;
                if(ig.getMatchingStacks().length == 0) continue;
                if(ig.getMatchingStacks()[0] == null) continue;
                if(ig.getMatchingStacks()[0].isEmpty()) continue;
                igType = ig.getMatchingStacks()[0].getItem();
                ingrdAmt++;
            }
        }
        return new ItemStack(igType, smallest);
    }

    public void retabulateConversionSpecs(){
        // We have an item, here. It is unfortunately unordered.
        // We need to ascertain how this item converts to other items.
        for(Item s : amountsByType.keySet()){
            // We've gotta go through the other items and find the one that goes in front of this.
            List<ConversionSpec> thisConversionSpecs = new ArrayList<>();
            for(Item i : amountsByType.keySet()){
                if(i == s) continue;

                // We're checking things that are not the same
                ItemStack neededForS = getIngredientsNeeded(s);
                ItemStack neededForI = getIngredientsNeeded(i);
            }
        }
    }

    public boolean containsAny(ItemStack[] stacks){
        for(ItemStack stack : stacks){
            if(amountsByType.containsKey(stack.getItem())){
                return true;
            }
        }
        return false;
    }

    public boolean containsAll(ItemStack[] stacks){
        for(ItemStack stack : stacks){
            if(!amountsByType.containsKey(stack.getItem())){
                return false;
            }
        }
        return true;
    }

    // This should be the primary means of interacting with
    // equivalence stacks. We remove the thing we want, and conversion
    // logic is handled internally
    //
    // Remove a specific amount of an item, converting if necessary.
    // Returns the removed itemstack, if possible, otherwise
    // return null.
    public ItemStack removeAmount(Item requested, int amount){
        // Does this stack equate to the requested item
        if(!amountsByType.containsKey(requested)) return null;

        // Do we already have the perfect amount of this item?
        if(amountsByType.get(requested) >= amount){
            amountsByType.put(requested, amountsByType.get(requested) - amount);
            return new ItemStack(requested, amount);
        }

        // Can we convert to it?
        for(Map.Entry<Item, ArrayList<ConversionSpec>> e : conversions.entrySet()){
            if(e.getKey().equals(requested)) continue; // We would have caught this if we could.

            for(ConversionSpec cs : e.getValue()){
                if(cs.convertingTo.equals(requested)){
                    // This is a conversion spec to the target value.
                    int neededConversions = (int) Math.ceil((double) amount / (double) cs.amtToMake);

                    if(amountsByType.get(e.getKey()) < (neededConversions * cs.amtToTake)){
                        continue;
                    }

                    amountsByType.put(e.getKey(), amountsByType.get(e.getKey()) - (neededConversions * cs.amtToTake));
                    amountsByType.put(cs.convertingTo, amountsByType.get(cs.convertingTo) + (neededConversions * cs.amtToMake));
                }
            }
        }

        // Go back through and try to do the thing once more.
        if(amountsByType.get(requested) >= amount){
            amountsByType.put(requested, amountsByType.get(requested) - amount);
            return new ItemStack(requested, amount);
        }

        // We still couldn't do it. (Not enough to convert with, etc...)
        return null;
    }

    private class ConversionSpec {
        int amtToTake = 0;
        int amtToMake = 0;
        Item convertingTo;
        Item from;

        public ConversionSpec(Item from, Item other, int amtNeeded, int amtToMake){
            this.from = from;
            convertingTo = other;
            this.amtToTake = amtNeeded;
            this.amtToMake = amtToMake;
        }
    }

}
