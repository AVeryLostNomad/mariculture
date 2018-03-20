package com.thelostnomad.tone.network;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.integration.IToneIntegration;
import com.thelostnomad.tone.proxy.CommonProxy;
import com.thelostnomad.tone.util.ChatUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class TonePacketHandler {
    public static final SimpleNetworkWrapper MSG_INSTANCE = new SimpleNetworkWrapper(ThingsOfNaturalEnergies.MODID);

    public static void init() {
        int id = 0;
        MSG_INSTANCE.registerMessage(ChatUtil.PacketNoSpamChat.Handler.class, ChatUtil.PacketNoSpamChat.class, id++, Side.CLIENT);
        MSG_INSTANCE.registerMessage(LastRecipeMessage.Handler.class, LastRecipeMessage.class, id++, Side.CLIENT);
        MSG_INSTANCE.registerMessage(MessageRecipeSync.Handler.class, MessageRecipeSync.class, id++, Side.SERVER);
        MSG_INSTANCE.registerMessage(MessageCraftingSync.Handler.class, MessageCraftingSync.class, id++, Side.CLIENT);

        for(IToneIntegration iti : CommonProxy.toneIntegrations){
            id+=iti.registerNetworkMessages(MSG_INSTANCE, id);
        }
    }

    public static void sendToAllAround(IMessage message, TileEntity te, int range) {
        MSG_INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimension(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), range));
    }

    public static void sendToAllAround(IMessage message, TileEntity te) {
        sendToAllAround(message, te, 64);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player) {
        MSG_INSTANCE.sendTo(message, player);
    }

    public static void sendToServer(IMessage message) {MSG_INSTANCE.sendToServer(message);}

}
