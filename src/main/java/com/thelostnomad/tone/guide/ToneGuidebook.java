package com.thelostnomad.tone.guide;

import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Category;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageText;
import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.registry.ModBlocks;
import com.thelostnomad.tone.registry.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

@GuideBook
public class ToneGuidebook implements IGuideBook {

    public static Book book;

    @Nullable
    @Override
    public Book buildBook() {
        book = new Book();
        book.setAuthor("TheLostNomad");
        book.setColor(Color.GREEN);
        book.setDisplayName("Natural Energies Handbook");
        book.setTitle("Things of Natural Energies v" + ThingsOfNaturalEnergies.VERSION);
        book.setWelcomeMessage("Index");
        book.setCreativeTab(ThingsOfNaturalEnergies.creativeTab);

        CategoryAbstract testCategory = new CategoryItemStack("lore.expl", new ItemStack(Items.PAPER)).withKeyBase("guide_lore_category");
        testCategory.addEntry("entry", new EntryItemStack("test.entry.name", new ItemStack(Items.POTATO)));
        testCategory.getEntry("entry").addPage(new PageText("Hello, this is\nsome text"));

        CategoryAbstract treeCategory = new CategoryItemStack("category.trees", new ItemStack(ModBlocks.sentientSapling)).withKeyBase("guide_trees_category");
        treeCategory.addEntry("trees.logs", new EntryItemStack("category.trees.logs", new ItemStack(ModBlocks.sentientLog)));
        addAllPages("category.trees.logs.page", "trees.logs", treeCategory);
        treeCategory.addEntry("trees.samples", new EntryItemStack("category.trees.samples", new ItemStack(ModBlocks.sentientTreeCore)));
        addAllPages("category.trees.samples.page", "trees.samples", treeCategory);

        book.addCategory(testCategory);
        book.addCategory(treeCategory);

        book.setRegistryName(new ResourceLocation(ThingsOfNaturalEnergies.MODID, "guide_book"));
        return book;
    }

    private void addAllPages(String pageString, String categoryKey, CategoryAbstract categoryAbstract){
        int index = 0;
        while(I18n.canTranslate(pageString + index)){
            categoryAbstract.getEntry(categoryKey).addPage(new PageText(pageString + index));
            index++;
        }
    }

    @Nullable
    @Override
    public IRecipe getRecipe(@Nonnull ItemStack bookStack) {
        return new ShapelessOreRecipe(null, bookStack, Items.BOOK, ModBlocks.sentientLeaves).setRegistryName(book.getRegistryName());
    }
}