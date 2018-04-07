package com.thelostnomad.mariculture;

import com.thelostnomad.mariculture.core.helpers.ConfigHelper;
import com.thelostnomad.mariculture.modules.ModuleManager;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Mariculture.MODID, name = Mariculture.MODNAME, version = Mariculture.VERSION, useMetadata = true, dependencies="before:guideapi")
public class Mariculture {

    public static final String MODID = "mariculture";
    public static final String MODNAME = "Mariculture - Greater Depths";
    public static final String VERSION = "0.0.1";

    public static final boolean IS_DEV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

    @SidedProxy(clientSide = "com.thelostnomad.mariculture.MClientProxy", serverSide = "com.thelostnomad.mariculture.MCommonProxy")
    public static MCommonProxy proxy;

    @Mod.Instance
    public static Mariculture instance;

    public static Logger logger = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHelper.setConfig(event.getSuggestedConfigurationFile());
        logger = event.getModLog();
        ModuleManager.loadModules(event.getAsmData(), proxy.isClient());
        ModuleManager.loadConfigs();
        proxy.load("preInit");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.load("init");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.load("postInit");
    }

}
