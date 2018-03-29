package com.thelostnomad.tone.block.tileentity;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.util.gui.SyncableTileEntity;
import com.thelostnomad.tone.util.world.IInteractable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

public class TEKeeper extends TileEntity implements IInventory, IInteractable, ITickable, SyncableTileEntity {

    public static final String NAME = "tone_keeper_tileentity";
    private BlockPos coreLocation = null;

    private final int NUMBER_OF_SLOTS = 2;
    private ItemStack[] itemStacks;

    private Boolean includeInInventory;
    private Boolean redstoneRequired;
    private Boolean exactItem;

    public TEKeeper() {
        itemStacks = new ItemStack[NUMBER_OF_SLOTS];
        clear();
    }

    public ItemStack[] getStacks(){
        return itemStacks;
    }

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    public boolean isIncludeInInventory() {
        return includeInInventory;
    }

    public void setIncludeInInventory(boolean value){
        includeInInventory = value;
    }

    public boolean isRedstoneRequired() {
        return redstoneRequired;
    }

    public boolean isExactItem() {
        return exactItem;
    }

    public void setExactItem(boolean value){
        exactItem = value;
    }

    public void setRedstoneRequired(boolean value){
        redstoneRequired = value;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : itemStacks) {
            if (!itemstack.isEmpty()) {  // terminal()
                return false;
            }
        }

        return true;
    }

    // Gets the stack in the given slot
    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        int actualSlot = slotIndex - 36;
        return itemStacks[actualSlot];
    }

    /**
     * Removes some of the units from itemstack in the given slot, and returns as a separate itemstack
     * @param slotIndex the slot number to remove the items from
     * @param count the number of units to remove
     * @return a new itemstack containing the units removed from the slot
     */
    @Override
    public ItemStack decrStackSize(int slotIndex, int count) {
        ItemStack itemStackInSlot = getStackInSlot(slotIndex);
        if (itemStackInSlot.isEmpty()) return ItemStack.EMPTY;  // isEmpt();   EMPTY_ITEM

        ItemStack itemStackRemoved;
        if (itemStackInSlot.getCount() <= count) {  // getStackSize()
            itemStackRemoved = itemStackInSlot;
            setInventorySlotContents(slotIndex, ItemStack.EMPTY);   // EMPTY_ITEM
        } else {
            itemStackRemoved = itemStackInSlot.splitStack(count);
            if (itemStackInSlot.getCount() == 0) { // getStackSize
                setInventorySlotContents(slotIndex, ItemStack.EMPTY);   // EMPTY_ITEM
            }
        }
        markDirty();
        return itemStackRemoved;
    }

    // overwrites the stack in the given slotIndex with the given stack
    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemstack) {
        int actualSlot = slotIndex - 36;
        itemStacks[actualSlot] = itemstack;
        if (itemstack.isEmpty() && itemstack.getCount() > getInventoryStackLimit()) { //  terminal(); getStackSize()
            itemstack.setCount(getInventoryStackLimit());  //setStackSize
        }
        markDirty();
    }

    // This is the maximum number if items allowed in each slot
    // This only affects things such as hoppers trying to insert items you need to use the container to enforce this for players
    // inserting items via the gui
    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemstack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    // Return true if the given player is able to use this block. In this case it checks that
    // 1) the world tileentity hasn't been replaced in the meantime, and
    // 2) the player isn't too far away from the centre of the block
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) return false;
        final double X_CENTRE_OFFSET = 0.5;
        final double Y_CENTRE_OFFSET = 0.5;
        final double Z_CENTRE_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
        return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    public void setCoreLocation(BlockPos core){
        this.coreLocation = core;
    }

    public BlockPos getCoreLocation() {
        return this.coreLocation;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound)
    {
        super.writeToNBT(parentNBTTagCompound);

        if(coreLocation == null){
            ThingsOfNaturalEnergies.logger.error("Writing to null");
            return parentNBTTagCompound;
        }

        NBTTagCompound blockPosNBT = new NBTTagCompound();        // NBTTagCompound is similar to a Java HashMap
        blockPosNBT.setInteger("x", coreLocation.getX());
        blockPosNBT.setInteger("y", coreLocation.getY());
        blockPosNBT.setInteger("z", coreLocation.getZ());
        parentNBTTagCompound.setTag("coreLocation", blockPosNBT);

        NBTTagList dataForAllSlots = new NBTTagList();
        for (int i = 0; i < this.itemStacks.length; ++i) {
            if (!this.itemStacks[i].isEmpty())	{ //terminal()
                NBTTagCompound dataForThisSlot = new NBTTagCompound();
                dataForThisSlot.setByte("Slot", (byte) i);
                this.itemStacks[i].writeToNBT(dataForThisSlot);
                dataForAllSlots.appendTag(dataForThisSlot);
            }
        }
        // the array of hashmaps is then inserted into the parent hashmap for the container
        parentNBTTagCompound.setTag("Items", dataForAllSlots);

        parentNBTTagCompound.setBoolean("IncludeInventory", includeInInventory);
        parentNBTTagCompound.setBoolean("ExactItem", exactItem);
        parentNBTTagCompound.setBoolean("RedstoneOn", redstoneRequired);
        ThingsOfNaturalEnergies.logger.error("Setting redstoneOn to " + redstoneRequired);

        // return the NBT Tag Compound
        return parentNBTTagCompound;
    }

    @Override
    public ItemStack removeStackFromSlot(int slotIndex) {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (!itemStack.isEmpty()) setInventorySlotContents(slotIndex, ItemStack.EMPTY);  //terminal(), EMPTY_ITEM
        return itemStack;
    }

    @Override
    public void clear() {
        Arrays.fill(itemStacks, ItemStack.EMPTY);  //empty item
    }

    @Override
    public void readFromNBT(NBTTagCompound parentNBTTagCompound)
    {
        super.readFromNBT(parentNBTTagCompound);

        NBTTagCompound coreLoc = parentNBTTagCompound.getCompoundTag("coreLocation");
        if(coreLoc != null){
            coreLocation = new BlockPos(coreLoc.getInteger("x"),
                    coreLoc.getInteger("y"), coreLoc.getInteger("z"));
        }

        final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing
        NBTTagList dataForAllSlots = parentNBTTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

        Arrays.fill(itemStacks, ItemStack.EMPTY);           // set all slots to empty EMPTY_ITEM
        for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
            NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
            int slotIndex = dataForOneSlot.getByte("Slot") & 255;

            if (slotIndex >= 0 && slotIndex < this.itemStacks.length) {
                this.itemStacks[slotIndex] = new ItemStack(dataForOneSlot);
            }
        }

        includeInInventory = parentNBTTagCompound.getBoolean("IncludeInventory");
        exactItem = parentNBTTagCompound.getBoolean("ExactItem");
        redstoneRequired = parentNBTTagCompound.getBoolean("RedstoneOn");
        ThingsOfNaturalEnergies.logger.error("Reading in redstone " + redstoneRequired);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public InteractableType getType() {
        return InteractableType.KEEPER;
    }

    @Override
    public void update() {
        if(world.isRemote) return;

        if(redstoneRequired){
            // Only function if this block is powered
            if(world.getStrongPower(pos) < 13){
                return;
            }
            ThingsOfNaturalEnergies.logger.error("Redstone powered");
        }
    }

    @Override
    public NBTTagCompound getSyncable() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("include", includeInInventory);
        tag.setBoolean("exact", exactItem);
        tag.setBoolean("redstone", redstoneRequired);
        return tag;
    }

    @Override
    public void doSync(NBTTagCompound fromClient) {
        includeInInventory = fromClient.getBoolean("include");
        exactItem = fromClient.getBoolean("exact");
        redstoneRequired = fromClient.getBoolean("redstone");
        ThingsOfNaturalEnergies.logger.error("Updating info to have redstone: " + redstoneRequired);
    }
}
