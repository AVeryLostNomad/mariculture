package com.thelostnomad.tone.util;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class RecipeUtil {

    private static List<IRecipe> recipesLoaded;

    public static void loadRecipes() {
        Set<ResourceLocation> iterator = CraftingManager.REGISTRY.getKeys();
        recipesLoaded = new ArrayList<IRecipe>();
        for (ResourceLocation r : iterator) {
            IRecipe thisRecipe = CraftingManager.REGISTRY.getObject(r);
            recipesLoaded.add(thisRecipe);
            //System.out.println(thisRecipe.getRecipeOutput().getItem().getUnlocalizedName());
        }
    }

    public static List<IRecipe> getRecipe(Object o) {
        List<IRecipe> possibleRecipes = new ArrayList<>();
        for (IRecipe rec : recipesLoaded) {
            if (o instanceof Block) {
                if (rec.getRecipeOutput().getItem().getUnlocalizedName().equals(((Block) o).getUnlocalizedName())) {
                    possibleRecipes.add(rec);
                }
            }
            if (o instanceof Item) {
                if (rec.getRecipeOutput().getItem().getUnlocalizedName().equals(((Item) o).getUnlocalizedName())) {
                    possibleRecipes.add(rec);
                }
            }
        }
        return possibleRecipes;
    }

    public static List<Block> getAllVanillaBlocks() throws IllegalAccessException {
        List<Block> toReturn = new ArrayList<Block>();
        Field[] fields = Blocks.class.getDeclaredFields();
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers()) && f.getType() == Block.class) {
                toReturn.add((Block) f.get(null));
            }
        }
        return toReturn;
    }

    public static List<Item> getAllVanillaItems() throws IllegalAccessException {
        List<Item> toReturn = new ArrayList<Item>();
        Field[] fields = Items.class.getDeclaredFields();
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers()) && f.getType() == Item.class) {
                toReturn.add((Item) f.get(null));
            }
        }
        return toReturn;
    }

    public static CraftingOperation getRequiredItemsToMakeIfPossible(Object target, List<ItemStack> alreadyHave){
        Map<String, Integer> ah = new HashMap<String, Integer>();
        for(ItemStack is : alreadyHave){
            ComparableItem ci = new ComparableItem(is.getItem());
            if(ah.containsKey(ci.toString())){
                ah.put(ci.toString(), ah.get(ci.toString()) + is.getCount());
            }else{
                ah.put(ci.toString(), is.getCount());
            }
        }
        return getRequiredItemsToMakeIfPossible(target, ah);
    }

    // Will try to return a list of items needed to create this target item.
    // Will fail and return null if it is not possible to make this item right now.
    private static CraftingOperation getRequiredItemsToMakeIfPossible(Object target, Map<String, Integer> alreadyHave){
        RecipeBranch baseBranch = new RecipeBranch(null, target, 0, 1); // Target is the final item. Do we have all of its base branches?

        if(!canCraft(new HashMap<String, Integer>(alreadyHave), baseBranch)){
            // No good! We can't make this item
            return null;
        }

        // We definitely can make the item, but can we formalize it into a set of individual crafting components?
        // What if we reverse the lists? Hmmm.

        CraftingOperation calculatedSteps = formalize(new HashMap<String, Integer>(alreadyHave), baseBranch);
        calculatedSteps.reverse();
        calculatedSteps.addStep(new ComparableItem(target));

        calculatedSteps.calculateComplexity();

        return calculatedSteps;
    }

    private static CraftingOperation formalize(Map<String, Integer> alreadyHave, RecipeBranch branch){
        CraftingOperation co = new CraftingOperation();
        for(RecipeBranch rb : branch.getSubBranches()){
            if(alreadyHaveContains(alreadyHave, rb.thisTarget, rb.amtMade)){
                for(int i = 0; i < rb.amtMade; i++){
                    co.addExistingIngredient(rb.thisTarget);
                }
            }else{
                // We do not have the item.
                // Is there any way we might be able to make it from its parts?
                if(rb.getSubBranches().size() != 0){
                    // We have a chance, there are subbranches here
                    co.addStep(rb.thisTarget);
                    co.merge(formalize(alreadyHave, rb));
                }
            }
        }
        return co;
    }

    private static boolean canCraft(Map<String, Integer> alreadyHave, RecipeBranch branch){
        boolean totalApprox = true;
        for(RecipeBranch rb : branch.getSubBranches()){
            // For each one, do we have that item? If not, return
            if(alreadyHaveContains(alreadyHave, rb.thisTarget, rb.amtMade)){;
                // We do have it, but we probably should remove one off the top
                Integer newAmt = alreadyHave.get(rb.thisTarget.toString()) - rb.amtMade;
                if(newAmt <= 0){
                    alreadyHave.remove(rb.thisTarget.toString());
                }else{
                    alreadyHave.put(rb.thisTarget.toString(), newAmt);
                }
            }else{
                // We do not have the item.
                // Is there any way we might be able to make it from its parts?
                if(rb.getSubBranches().size() != 0){
                    // We have a chance, there are subbranches here
                     if(!canCraft(alreadyHave, rb)){
                         totalApprox = false;
                     }
                }else{
                    // There is no way to get this item.
                    totalApprox = false;
                    break;
                }
            }
        }
        return totalApprox;
    }

    private static boolean alreadyHaveContains(Map<String, Integer> alreadyHave, ComparableItem target, int amount){
        for(String o : alreadyHave.keySet()){
            if(target.toString().equals(o) && alreadyHave.get(o) >= amount){
                return true;
            }
        }
        return false;
    }

    public static class ComparableItem {

        private Item thisItem;
        private Block thisBlock;

        public ComparableItem(Object o){
            if(o instanceof Item){
                thisItem = (Item) o;
            }
            if(o instanceof Block){
                thisBlock = (Block) o;
            }
            if(thisItem == null && thisBlock == null) ThingsOfNaturalEnergies.logger.error(o.getClass().getName());
        }

        public Object getObject(){
            return thisItem == null ? thisBlock : thisItem;
        }

        @Override
        public String toString() {
            return thisItem == null ? thisBlock.getUnlocalizedName() : thisItem.getUnlocalizedName();
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof ComparableItem){
                ComparableItem ci = (ComparableItem) o;
                if(ci.thisItem == null && this.thisItem == null){
                    return ci.thisBlock.getUnlocalizedName().equals(thisBlock.getUnlocalizedName());
                }else if(ci.thisBlock == null && this.thisBlock == null){
                    return ci.thisItem.getUnlocalizedName().equals(thisItem.getUnlocalizedName());
                }else{
                    return false;
                }
            }
            if(o instanceof Item){
                if(thisItem == null) return false;
                return ((Item) o).getUnlocalizedName().equals(thisItem.getUnlocalizedName());
            }
            if(o instanceof Block){
                if(thisBlock == null) return false;
                return ((Block) o).getUnlocalizedName().equals(thisBlock.getUnlocalizedName());
            }
            return false;
        }

    }

    private static class RecipeBranch {

        public List<RecipeBranch> subBranches = new ArrayList<RecipeBranch>();
        public ComparableItem thisTarget;
        public boolean isEmpty = false;
        public int depth;
        public int amtMade;
        public RecipeBranch myParent = null;

        public RecipeBranch(RecipeBranch parentBranch, Object target, int depth, int amtMade){
            myParent = parentBranch;
            this.depth = depth;
            thisTarget = new ComparableItem(target);
            this.amtMade = amtMade;
            List<IRecipe> ingredients = getRecipe(target);
            if(ingredients.size() == 0 || depth > 6){
                isEmpty = true;
                return;
            }
            IRecipe recipe = ingredients.get(0); // We will always go with the very first recipe allowed to us, which is
            // not necessarily desired functionality. Bummmer? Bummer. Maybe we'll make a berry for that. One that does
            // a specific crafting recipe, and can be used for that specific craft? That'd be kinda cool.
            // All it would have to do is, when getRecipe is called, automatically provide the substituted one first.
            // Hmm, not a shabby idea.
            if(parentBranch != null){
                if(parentBranch.myParent != null){
                    RecipeBranch parentsParent = parentBranch.myParent;
                    if(parentsParent.thisTarget.equals(new ComparableItem(recipe.getRecipeOutput().getItem()))){
                        isEmpty = true;
                        return;
                    }
                }
            }
            List<Ingredient> stuff = recipe.getIngredients();
            Map<Item, Integer> amtPerThing = new HashMap<Item, Integer>();

            int index = 0;
            for(Ingredient i : stuff){
                index++;
                for(ItemStack is : i.getMatchingStacks()){
                    Item itm = is.getItem();
                    if(amtPerThing.containsKey(itm)){
                        amtPerThing.put(itm, amtPerThing.get(itm) + 1);
                    }else{
                        amtPerThing.put(itm, 1);
                    }
                }
            }
            for(Map.Entry<Item, Integer> e : amtPerThing.entrySet()){
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
