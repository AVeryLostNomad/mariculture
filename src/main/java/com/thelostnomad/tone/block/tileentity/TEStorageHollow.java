package com.thelostnomad.tone.block.tileentity;

import com.thelostnomad.tone.util.world.IInteractable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Arrays;

public class TEStorageHollow extends TileEntity implements IInventory, IInteractable {

    public static final String NAME = "tone_storagehollow_tileentity";
    private BlockPos coreLocation = null;

    private HollowType storageLevel;
    private ItemStack[] itemStacks;

    public TEStorageHollow() {
    }

    public void setStorageLevel(HollowType type) {
        this.storageLevel = type;
        init();
    }

    public boolean isFull() {
        return getFilled() == getCapacity();
    }

    public void init() {
        this.storageLevel = storageLevel;
        itemStacks = new ItemStack[storageLevel.size];
        clear();
    }

    public ItemStack[] getItemStacks(){
        return this.itemStacks;
    }

    public void setCoreLocation(BlockPos core) {
        this.coreLocation = core;
    }

    public BlockPos getCoreLocation() {
        return this.coreLocation;
    }

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : itemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public int getCapacity() {
        return storageLevel.size;
    }

    public int getFilled() {
        int total = 0;
        for (ItemStack itemStack : itemStacks) {
            if (!itemStack.isEmpty()) {
                total++;
            }
        }
        return total;
    }

    public synchronized boolean canAddItem(ItemStack stack){
        boolean found = false;
        for (ItemStack i : itemStacks) {
            if (i.isEmpty()) continue;
            if (i.getItem() == stack.getItem()) {
                // We can go ahead and put this here.
                if ((i.getCount() + stack.getCount()) >= this.getInventoryStackLimit()) continue;
                found = true;
                return true;
            }
        }
        if(!found && (this.getFilled() != this.getCapacity())){
            return true;
        }
        return false;
    }

    public synchronized boolean addItem(ItemStack stack) {
        boolean found = false;
        for (ItemStack i : itemStacks) {
            if (i.isEmpty()) continue;
            if (i.getItem() == stack.getItem()) {
                // We can go ahead and put this here.
                if ((i.getCount() + stack.getCount()) >= this.getInventoryStackLimit()) continue;
                found = true;
                i.setCount(i.getCount() + stack.getCount());
                return true;
            }
        }
        if(!found && (this.getFilled() != this.getCapacity())){
            setInventorySlotContents(this.getFilled(), stack);
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        return itemStacks[slotIndex];
    }

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

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemStack = getStackInSlot(index);
        if (!itemStack.isEmpty()) setInventorySlotContents(index, ItemStack.EMPTY);  //terminal(), EMPTY_ITEM
        return itemStack;
    }

    // overwrites the stack in the given slotIndex with the given stack
    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemstack) {
        itemStacks[slotIndex] = itemstack;
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
        if (storageLevel == HollowType.INDIVIDUAL) {
            return 32000;
        }
        if (storageLevel.size >= 108) {
            return 128;
        }
        return 64;
    }

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

    // Return true if the given stack is allowed to go in the given slot.  In this case, we can insert anything.
    // This only affects things such as hoppers trying to insert items you need to use the container to enforce this for players
    // inserting items via the gui
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
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    // This is where you save any data that you don't want to lose when the tile entity unloads
    // In this case, it saves the itemstacks stored in the container
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound) {
        super.writeToNBT(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location

        NBTTagString storageType = new NBTTagString(storageLevel.name);

        NBTTagList dataForAllSlots = new NBTTagList();
        for (int i = 0; i < this.itemStacks.length; ++i) {
            if (!this.itemStacks[i].isEmpty()) { //terminal()
                NBTTagCompound dataForThisSlot = new NBTTagCompound();
                dataForThisSlot.setInteger("Slot", i);
                this.itemStacks[i].writeToNBT(dataForThisSlot);
                dataForAllSlots.appendTag(dataForThisSlot);
            }
        }
//        // the array of hashmaps is then inserted into the parent hashmap for the container
        parentNBTTagCompound.setTag("Level", storageType);
        parentNBTTagCompound.setTag("Items", dataForAllSlots);

        if (coreLocation == null) {
            return parentNBTTagCompound;
        }

        NBTTagCompound blockPosNBT = new NBTTagCompound();        // NBTTagCompound is similar to a Java HashMap
        blockPosNBT.setInteger("x", coreLocation.getX());
        blockPosNBT.setInteger("y", coreLocation.getY());
        blockPosNBT.setInteger("z", coreLocation.getZ());
        parentNBTTagCompound.setTag("coreLocation", blockPosNBT);

        // to use an analogy with Java, this code generates an array of hashmaps
        // The itemStack in each slot is converted to an NBTTagCompound, which is effectively a hashmap of key->value pairs such
        //   as slot=1, id=2353, count=1, etc
        // Each of these NBTTagCompound are then inserted into NBTTagList, which is similar to an array.
        // return the NBT Tag Compound
        return parentNBTTagCompound;
    }

    // This is where you load the data that you saved in writeToNBT
    @Override
    public void readFromNBT(NBTTagCompound parentNBTTagCompound) {
        super.readFromNBT(parentNBTTagCompound); // The super call is required to save and load the tiles location

        String level = parentNBTTagCompound.getString("Level");
        for (HollowType ht : HollowType.values()) {
            if (ht.name.equals(level)) {
                this.storageLevel = ht;
                this.itemStacks = new ItemStack[storageLevel.size];
                break;
            }
        }

        NBTTagCompound coreLoc = parentNBTTagCompound.getCompoundTag("coreLocation");
        if (coreLoc != null) {
            coreLocation = new BlockPos(coreLoc.getInteger("x"),
                    coreLoc.getInteger("y"), coreLoc.getInteger("z"));
        }

        final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing
        NBTTagList dataForAllSlots = parentNBTTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

        Arrays.fill(itemStacks, ItemStack.EMPTY);           // set all slots to empty EMPTY_ITEM
        for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
            NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
            if (dataForOneSlot == null) {

            }
            int slotIndex = dataForOneSlot.getInteger("Slot");

            if (slotIndex >= 0 && slotIndex < this.itemStacks.length) {
                this.itemStacks[slotIndex] = new ItemStack(dataForOneSlot);
            }
        }
    }

    // set all slots to empty
    @Override
    public void clear() {
        Arrays.fill(itemStacks, ItemStack.EMPTY);  //empty item
    }

    // will add a key for this container to the lang file so we can name it in the GUI
    @Override
    public String getName() {
        return "container.storage_hollow_ " + this.storageLevel.name.toLowerCase() + ".name";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    // standard code to look up what the human-readable name is
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    @Override
    public InteractableType getType() {
        return InteractableType.STORAGE;
    }

//    @Override
//    public void update() {
//        BlockPos under = pos.offset(EnumFacing.DOWN);
//        TileEntity chestEntity = world.getTileEntity(under);
//        if(chestEntity instanceof TileEntityChest){
//            TileEntityChest chest = (TileEntityChest) chestEntity;
//            for(int i = 0; i < chest.getSizeInventory(); i++){
//                ItemStack stack = chest.getStackInSlot(i);
//                this.addItem(stack);
//                chest.removeStackFromSlot(i);
//            }
//        }
//    }

    public enum HollowType {
        INDIVIDUAL(1, "Specialized"),
        BASIC(9, "Basic"),
        BIG(18, "Big"),
        LARGE(27, "Large"),
        MASSIVE(54, "Massive"),
        GARGANTUAN(108, "Gargantuan"),
        QUITE_BIG(216, "Quite Big"),
        BIGGER_THAN_THAT(423, "Even Bigger"),
        SINGULARITY(Integer.MAX_VALUE, "Singularity");

        private int size;
        private String name;

        HollowType(int size, String name) {
            this.size = size;
            this.name = name;
        }

    }

}
