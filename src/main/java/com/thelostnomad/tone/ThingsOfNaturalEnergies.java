package com.thelostnomad.tone;

import appeng.api.AEApi;
import com.thelostnomad.tone.integration.IToneIntegration;
import com.thelostnomad.tone.network.TonePacketHandler;
import com.thelostnomad.tone.proxy.CommonProxy;
import com.thelostnomad.tone.util.RecipeUtil;
import com.thelostnomad.tone.util.crafting.CraftTreeBuilder;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = ThingsOfNaturalEnergies.MODID, name = ThingsOfNaturalEnergies.MODNAME, version = ThingsOfNaturalEnergies.VERSION, useMetadata = true, dependencies="before:guideapi")
public class ThingsOfNaturalEnergies {

    public static final String MODID = "thingsofnaturalenergies";
    public static final String MODNAME = "Things of Natural Energies";
    public static final String VERSION = "0.0.1";

    public static final boolean IS_DEV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    @SidedProxy(clientSide = "com.thelostnomad.tone.proxy.ClientProxy", serverSide = "com.thelostnomad.tone.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static final CreativeTabTone creativeTab = new CreativeTabTone();

    @Mod.Instance
    public static ThingsOfNaturalEnergies instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);

        for(IToneIntegration iti : CommonProxy.toneIntegrations){
            iti.preInit(event);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
        TonePacketHandler.init();

        for(IToneIntegration iti : CommonProxy.toneIntegrations){
            iti.init(e);
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        for(IToneIntegration iti : CommonProxy.toneIntegrations){
            iti.postInit(event);
        }

        CraftTreeBuilder.loadRecipes();

//        List<ItemStack> inv = new ArrayList<ItemStack>();
//        inv.add(new ItemStack(Items.REEDS, 9));
//        inv.add(new ItemStack(Blocks.REDSTONE_BLOCK, 2));
//        inv.add(new ItemStack(Blocks.IRON_BLOCK, 2));
//        inv.add(new ItemStack(Blocks.OBSIDIAN, 5));
//        inv.add(new ItemStack(Blocks.DIAMOND_BLOCK, 2));
//        inv.add(new ItemStack(Items.LEATHER, 4));
//
//        List<CraftTreeBuilder.DirectionalItemStack> dir = CraftTreeBuilder.findProcessToMake(new ItemStack(Items.IRON_INGOT, 1), inv);
//        if(dir != null){
//            for(CraftTreeBuilder.DirectionalItemStack d : dir){
//                ThingsOfNaturalEnergies.logger.error(d.getStack().getDisplayName() + (d.isAdd() ? " +":" -") + d.getStack().getCount());
//            }
//        }
//        FMLCommonHandler.instance().exitJava(0, true);
//
//        List<ItemStack> alreadyHave = new ArrayList<ItemStack>(Arrays.asList(new ItemStack[]{
//            new ItemStack(Blocks.IRON_BLOCK, 2),
//            new ItemStack(Blocks.REDSTONE_BLOCK, 2),
//            new ItemStack(Items.REEDS, 9)
//        }));
//        CraftingOperation co = RecipeUtil.getRequiredItemsToMakeIfPossible(Items.MAP, alreadyHave);
//
//        if(co == null){
//            ThingsOfNaturalEnergies.logger.error("Cannot craft");
//            return;
//        }
//
//        for(Map.Entry<Integer, RecipeUtil.ComparableItem> e : co.getSteps().entrySet()){
//            ThingsOfNaturalEnergies.logger.error(e.getKey() + " : " + e.getValue());
//        }
//        ThingsOfNaturalEnergies.logger.error("You will need to use:");
//        for(Map.Entry<RecipeUtil.ComparableItem, Integer> e : co.getExistingIngredients().entrySet()){
//            ThingsOfNaturalEnergies.logger.error(e.getKey() + " x" + e.getValue());
//        }
//        ThingsOfNaturalEnergies.logger.error("Complexity: " + co.getComplexity());
//        System.exit(0);
    }
}
