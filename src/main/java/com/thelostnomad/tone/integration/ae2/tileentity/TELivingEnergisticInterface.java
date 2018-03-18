package com.thelostnomad.tone.integration.ae2.tileentity;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.networking.*;
import appeng.api.networking.energy.IEnergySource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.IMEInventoryHandler;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.util.*;
import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.network.TonePacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class TELivingEnergisticInterface extends TileEntity implements IGridHost, IGridBlock, ITickable, IEnergySource, IActionSource {

    public static final String NAME = "tone_ae2_livingenergisticinterface";

    IGridNode node = null;
    boolean initialized = false;

    private EntityPlayer placingPlayer = null;

    private void unRegisterNode() {
        if(node != null){
            node.destroy();
            node = null;
            initialized = false;
        }
    }

    public ItemStack drawItem(ItemStack i){
        if(node == null){
            return null;
        }

        IItemStorageChannel channel = AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class);
        IAEItemStack iaeItemStack = channel.createStack(i);
        // Now we've got the item in a stack that we can handle
        List<IMEInventoryHandler> drives = getDrivesInNetwork();
        for(IMEInventoryHandler d : drives){
            IAEStack resultStack = d.extractItems(iaeItemStack, Actionable.MODULATE, this);
            if(resultStack == null){
                continue;
            }
            ItemStack stack = resultStack.asItemStackRepresentation();
            return stack;
        }

        // If we got here, we couldn't find it.
        return null;
    }

    public List<IMEInventoryHandler> getDrivesInNetwork() {
        if(node == null){
            return new ArrayList<>();
        }

        IItemStorageChannel channel = AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class);
        IReadOnlyCollection<Class<? extends IGridHost>> hosts = node.getGrid().getMachinesClasses();
        IMachineSet driveBays = null;
        for(Class<? extends IGridHost> c : hosts){
            String name = c.getName();
            if(name.toLowerCase().contains("tiledrive")){
                // We've got the right one.
                driveBays = node.getGrid().getMachines(c);
                break;
            }
        }

        if(driveBays == null){
            // We have no connected drives.
            return new ArrayList<>();
        }

        List<IMEInventoryHandler> connectedDriveInventories = new ArrayList<IMEInventoryHandler>();
        for(IGridNode host : driveBays){
            IGridHost hst = host.getMachine();
            try {
                Method m = hst.getClass().getMethod("getCellArray", IStorageChannel.class);
                connectedDriveInventories.addAll((List<IMEInventoryHandler>) m.invoke(hst, channel));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                // No method. Meaning this is either bad code or not an inventory handler that responds to this type
                // of thing.
                continue;
            }
        }

        return connectedDriveInventories;
    }

    public IGridNode refreshNode() {
        node = AEApi.instance().grid().createGridNode(this);
        return node;
    }

    public IGridNode getNode() {
        return node;
    }

    public void setPlacingPlayer(EntityPlayer ep){
        this.placingPlayer = ep;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound) {
        super.writeToNBT(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location

        if(node != null) node.saveToNBT("ae_node", parentNBTTagCompound);

        return parentNBTTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound parentNBTTagCompound) {
        super.readFromNBT(parentNBTTagCompound); // The super call is required to save and load the tiles location

        if(FMLCommonHandler.instance().getEffectiveSide().isServer()){
            // Server
            unRegisterNode();
            node = AEApi.instance().grid().createGridNode(this);
            if(parentNBTTagCompound.hasKey("ae_node")){
                node.loadFromNBT("ae_node", parentNBTTagCompound);
            }
        }
        initialized = false;
    }

    @Override
    public void update() {
        if(!initialized){
            if(placingPlayer != null){
                if(node != null) node.setPlayerID(AEApi.instance().registries().players().getID(placingPlayer));
            }
            if(node != null){
                node.updateState();
            }
            initialized = true;
        }
    }

    @Override
    public void invalidate() {
        unRegisterNode();
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        unRegisterNode();
        super.onChunkUnload();
    }

    @Override
    public IGridNode getGridNode(AEPartLocation aePartLocation) {
        if(getWorld() == null || getWorld().isRemote) return null;
        ThingsOfNaturalEnergies.logger.error("Creating grid node");
        if(node == null){
            node = AEApi.instance().grid().createGridNode(this);
        }
        return node;
    }

    @Override
    public AECableType getCableConnectionType(AEPartLocation aePartLocation) {
        return AECableType.COVERED;
    }

    @Override
    public void securityBreak() {
        world.setBlockToAir(pos);
    }

    @Override
    public double getIdlePowerUsage() {
        return 0;
    }

    @Override
    public EnumSet<GridFlags> getFlags() {
        return EnumSet.noneOf(GridFlags.class);
    }

    @Override
    public boolean isWorldAccessible() {
        return true;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public AEColor getGridColor() {
        return AEColor.TRANSPARENT;
    }

    @Override
    public void onGridNotification(GridNotification gridNotification) { }

    @Override
    public void setNetworkStatus(IGrid iGrid, int i) { }

    @Override
    public EnumSet<EnumFacing> getConnectableSides() {
        return EnumSet.allOf(EnumFacing.class);
    }

    @Override
    public IGridHost getMachine() {
        return this;
    }

    @Override
    public void gridChanged() { }

    @Override
    public ItemStack getMachineRepresentation() {
        return null;
    }

    @Override
    public double extractAEPower(double v, Actionable actionable, PowerMultiplier powerMultiplier) {
        return v; // Simply invent power, for now. It costs "nothing" to draw power for item transfer. Maybe we make
        // this tie into the number of "ae berries" you have?
    }

    @Nonnull
    @Override
    public Optional<EntityPlayer> player() {
        return null;
    }

    @Nonnull
    @Override
    public Optional<IActionHost> machine() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public <T> Optional<T> context(@Nonnull Class<T> aClass) {
        return Optional.empty();
    }
}
