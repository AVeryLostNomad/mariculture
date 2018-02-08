package com.thelostnomad.tone;

import com.thelostnomad.tone.network.TonePacketHandler;
import com.thelostnomad.tone.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ThingsOfNaturalEnergies.MODID, name = ThingsOfNaturalEnergies.MODNAME, version = ThingsOfNaturalEnergies.VERSION, useMetadata = true)
public class ThingsOfNaturalEnergies {

    public static final String MODID = "thingsofnaturalenergies";
    public static final String MODNAME = "Things of Natural Energies";
    public static final String VERSION = "0.0.1";

    @SidedProxy(clientSide = "com.thelostnomad.tone.proxy.ClientProxy", serverSide = "com.thelostnomad.tone.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static ThingsOfNaturalEnergies instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
        TonePacketHandler.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
