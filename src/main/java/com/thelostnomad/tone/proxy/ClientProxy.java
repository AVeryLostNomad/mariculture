package com.thelostnomad.tone.proxy;

import com.thelostnomad.tone.registry.ModModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        ModModelManager mm = ModModelManager.INSTANCE;
    }

    public EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

}
