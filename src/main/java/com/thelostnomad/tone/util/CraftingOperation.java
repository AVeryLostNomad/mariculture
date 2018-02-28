package com.thelostnomad.tone.util;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingOperation {

    private int complexity = 0;
    private Map<RecipeUtil.ComparableItem, Integer> existingIngredients = new HashMap<>();
    private Map<Integer, RecipeUtil.ComparableItem> steps = new HashMap<Integer, RecipeUtil.ComparableItem>();

    public Map<Integer, RecipeUtil.ComparableItem> getSteps() {
        return steps;
    }

    public void setSteps(Map<Integer, RecipeUtil.ComparableItem> steps) {
        this.steps = steps;
    }

    public void addStep(RecipeUtil.ComparableItem stepGoal){
        this.steps.put(steps.keySet().size(), stepGoal);
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public Map<RecipeUtil.ComparableItem, Integer> getExistingIngredients() {
        return existingIngredients;
    }

    public void addExistingIngredient(RecipeUtil.ComparableItem item){
        if(existingIngredients.containsKey(item)){
            existingIngredients.put(item, existingIngredients.get(item) + 1);
        }else{
            existingIngredients.put(item, 1);
        }
    }

    public void setExistingIngredients(Map<RecipeUtil.ComparableItem, Integer> existingIngredients) {
        this.existingIngredients = existingIngredients;
    }

    public void merge(CraftingOperation other){
        for(Map.Entry<RecipeUtil.ComparableItem, Integer> e : other.existingIngredients.entrySet()){
            if(this.existingIngredients.containsKey(e.getKey())){
                this.existingIngredients.put(e.getKey(), this.existingIngredients.get(e.getKey()) + 1);
            }else{
                this.existingIngredients.put(e.getKey(), 1);
            }
        }
        int index = 0;
        for(int i = 0, j = steps.keySet().size(); i < other.steps.keySet().size(); i++, j++){
            RecipeUtil.ComparableItem otherStep = other.steps.get(other.steps.keySet().toArray()[i]);
            this.steps.put(j, otherStep);
            index++;
        }
    }

    public void reverse(){
        Map<Integer, RecipeUtil.ComparableItem> newSteps = new HashMap<Integer, RecipeUtil.ComparableItem>();
        for(int i = 0, j = steps.keySet().size() - 1; j >= 0; i++, j--){
            newSteps.put(i, steps.get(steps.keySet().toArray()[j]));
        }
        this.steps = newSteps;
    }

    public void calculateComplexity() {
        int totalComp = 0;
        for(Map.Entry<Integer, RecipeUtil.ComparableItem> step : steps.entrySet()){
            totalComp += 2; // Add one for every step of crafting necessary to do this thing.
        }
        for(Map.Entry<RecipeUtil.ComparableItem, Integer> item : existingIngredients.entrySet()){
            totalComp += (item.getValue());
        }
        this.complexity = totalComp;
    }
}
