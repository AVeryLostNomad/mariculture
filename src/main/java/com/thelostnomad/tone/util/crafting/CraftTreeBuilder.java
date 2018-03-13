package com.thelostnomad.tone.util.crafting;

import com.thelostnomad.tone.util.RecipeUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.asm.transformers.ItemStackTransformer;
import scala.tools.nsc.transform.patmat.Logic;

import java.util.*;

public class CraftTreeBuilder {

    private static final int DEPTH_LIMIT = 10;
    private static List<IRecipe> recipesLoaded;
    public static List<EquivalenceStack> equivalenceStacks;

    public static void loadRecipes() {
        Set<ResourceLocation> iterator = CraftingManager.REGISTRY.getKeys();
        recipesLoaded = new ArrayList<IRecipe>();
        for (ResourceLocation r : iterator) {
            IRecipe thisRecipe = CraftingManager.REGISTRY.getObject(r);
            recipesLoaded.add(thisRecipe);
        }
        isolateEquivalenceStacks();
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

    private static ItemStack getFirstNonempty(ItemStack[] array){
        for(ItemStack stack : array){
            if(stack == null) continue;
            if(stack.isEmpty()) continue;
            return stack;
        }
        return null;
    }

    private static int countIngredients(IRecipe recipe){
        Map<ItemStack, Integer> ingredientMap = new HashMap<ItemStack, Integer>();
        for(Ingredient i : recipe.getIngredients()){
            if(i == null) continue;
            ItemStack stack = getFirstNonempty(i.getMatchingStacks());
            if(stack == null) continue;
            if(ingredientMap.containsKey(stack)){
                ingredientMap.put(stack, ingredientMap.get(stack) + 1);
            }else{
                ingredientMap.put(stack, 1);
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
                        if(StackUtil.stacksEqual(thisItem, isCompare)){
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
                es.addTemplateComponent(listOfEquivalencies);
                es.retabulateConversionSpecs();
                return;
            }
        }
        EquivalenceStack es = new EquivalenceStack();
        es.addTemplateComponent(listOfEquivalencies);
        es.retabulateConversionSpecs();
        equivalenceStacks.add(es);
    }

    public static boolean isContainedInEquivalenceStack(Ingredient i){
        for(EquivalenceStack es : equivalenceStacks){
            for(ItemStack stk : i.getMatchingStacks()){
                if(es.contains(stk)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isContainedInEquivalenceStack(ItemStack i){
        for(EquivalenceStack es : equivalenceStacks){
            if(es.contains(i)){
                return true;
            }
        }
        return false;
    }

    public static EquivalenceStack getEquivalenceStack(ItemStack i){
        for(EquivalenceStack es : equivalenceStacks){
            if(es.contains(i)){
                return es;
            }
        }
        return null;
    }

    public static class InventoryWrapper {

        private List<ItemStack> standardStacks;
        private List<EquivalenceStack> equivalenceStacks;

        // Send in an player inventory, this wrapper will convert it into an interactable inventory
        // object.
        public InventoryWrapper(List<ItemStack> playerInventory){
            standardStacks = new ArrayList<>();
            equivalenceStacks = new ArrayList<>();

            for(ItemStack s : playerInventory){
                // Is this item something that is in "any" equivalence stack?
                if(isContainedInEquivalenceStack(s)){
                    // This is an equivalence stack item.
                    // First go through and see if we have one in our list
                    EquivalenceStack found = null;
                    for(EquivalenceStack es : equivalenceStacks){
                        if(es.contains(s)){
                            // This stack does indeed have that item type!
                            found = es;
                            break;
                        }
                    }
                    if(found != null){
                        found.setAmount(s, s.getCount() + found.getAmountInInv(s));
                    }else{
                        // We don't yet have an equivalence stack of this type.
                        EquivalenceStack template = getEquivalenceStack(s);
                        EquivalenceStack toAdd = template.copy();
                        toAdd.setAmount(s, s.getCount());
                        equivalenceStacks.add(toAdd);
                    }
                }
            }
        }

    }

    private static class RecipeBranch {
        public List<RecipeBranch> subBranches = new ArrayList<>();
        public ItemStack target;
        public boolean terminal = false;
        public int depth;
        public int amtMade;
        public RecipeBranch parent = null;
        public EquivalenceStack equiStack = null;

        public RecipeBranch(RecipeBranch parent, ItemStack target, int depth, int amtMade){
            this.parent = parent;
            this.target = target;
            this.depth = depth;
            this.amtMade = amtMade;

            List<IRecipe> ingredients = getRecipe(target);
            IRecipe recipe = ingredients.get(0);
            // If there is no recipe for this and/or we're at our depth limit
            // mark this branch as a terminal
            if(ingredients.size() == 0 || depth > DEPTH_LIMIT){
                terminal = true;
                return;
            }

            if(isContainedInEquivalenceStack(target)){
                this.equiStack = getEquivalenceStack(target);
                terminal = true;
                return;
            }

            List<Ingredient> stuff = recipe.getIngredients();
            Map<ItemStack, Integer> amtPerThing = new HashMap<ItemStack, Integer>();

            int index = 0;
            for(Ingredient i : stuff){
                index++;
                for(ItemStack is : i.getMatchingStacks()){
                    if(amtPerThing.containsKey(is)){
                        amtPerThing.put(is, amtPerThing.get(is) + 1);
                    }else{
                        amtPerThing.put(is, 1);
                    }
                }
            }
            for(Map.Entry<ItemStack, Integer> e : amtPerThing.entrySet()){
                // Can we multi-make this?
                int amt_per_make = 0;
                for(IRecipe rcp : getRecipe(e.getKey())){
                    amt_per_make = rcp.getRecipeOutput().getCount();
                }
                if(amt_per_make >= e.getValue()){
                    // Savvy, we're fine here.
                    subBranches.add(new RecipeBranch(this, e.getKey(), depth + 1, e.getValue()));
                }else{
                    if(amt_per_make == 0){
                        for(int i = 0; i < e.getValue(); i++){
                            subBranches.add(new RecipeBranch(this, e.getKey(), depth + 1, 1));
                        }
                        continue;
                    }
                    int timesNeeded = (int) Math.ceil((double) e.getValue() / (double) amt_per_make);
                    for(int i = 0; i < timesNeeded; i++){
                        subBranches.add(new RecipeBranch(this, e.getKey(), depth + 1, 1));
                    }
                }
            }

        }

        public List<RecipeBranch> getSubBranches(){
            return subBranches;
        }
    }

}
