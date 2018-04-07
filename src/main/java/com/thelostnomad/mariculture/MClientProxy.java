package com.thelostnomad.mariculture;

import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static net.minecraft.block.BlockLiquid.LEVEL;

@Mod.EventBusSubscriber(Side.CLIENT)
public class MClientProxy extends MCommonProxy {

    public static final StateMap NO_WATER = new StateMap.Builder().ignore(LEVEL).build();

    @Override
    public boolean isClient() {
        return true;
    }

    @SubscribeEvent
    public static void registerAllModels(final ModelRegistryEvent event) {
        Mariculture.proxy.passEvent("registerAllModels", event);
    }

}
