package com.thelostnomad.tone.block.tileentity;


// Simple Tile Entity that accepts items from any side and shoves them into the storage hollow.
// Literally any time an item is put in, we will do the thing to it.

import com.thelostnomad.tone.util.world.IInteractable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

// Is an *outward facing inventory* (blocks an automation will be interacting with this), so we need to use capabilities
public class TEAcceptor extends TileEntity implements IInteractable, ICapabilityProvider, ITickable {

    private BlockPos coreLocation;

    private ItemStackHandler inventory = new ItemStackHandler(1){
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }
    };

    public BlockPos getCoreLocation(){
        return coreLocation;
    }

    public void setCoreLocation(BlockPos core){
        this.coreLocation = core;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        super.readFromNBT(compound);
    }

    @Override
    public InteractableType getType() {
        return InteractableType.INTEGRATION;
    }

    @Override
    public void update() {
        if(!inventory.getStackInSlot(0).isEmpty()){
            // We have an item in that slot. Get rid of it!
            ItemStack result = inventory.extractItem(0, inventory.getStackInSlot(0).getCount(), false);

        }
    }
}
