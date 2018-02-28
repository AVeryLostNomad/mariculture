package com.thelostnomad.tone.block.tileentity;

import com.google.common.base.Predicate;
import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.block.BlockPuller;
import com.thelostnomad.tone.block.BlockPusher;
import com.thelostnomad.tone.block.RootBlock;
import com.thelostnomad.tone.block.berries.BlockBerry;
import com.thelostnomad.tone.block.berries.FuncoBerry;
import com.thelostnomad.tone.block.berries.GlutoBerry;
import com.thelostnomad.tone.block.berries.HastoBerry;
import com.thelostnomad.tone.item.tokens.ItemToken;
import com.thelostnomad.tone.registry.ModBlocks;
import com.thelostnomad.tone.registry.ModItems;
import com.thelostnomad.tone.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TESentientTreeCore extends TileEntity implements ITickable {

    // Name of tile entity
    public static final String NAME = "tone_sentienttree_tileentity";

    List<BlockPos> storageHollows = new ArrayList<BlockPos>();
    List<BlockPos> pullers = new ArrayList<>();
    List<BlockPos> pushers = new ArrayList<>();

    List<BlockPos> berries = new ArrayList<>();

    List<BlockPos> roots = new ArrayList<>();

    private long tickcount = 0;
    private Long tickRate = 60L; // For a tree without hastoberries, this is the default. One action cycle every three
                                // seconds.

    private Integer glutoCount = 0;
    private Integer funcoCount = 0;
    private Integer craftoCount = 30; // TODO implement later.

    private Double life = 0D;
    private Double maxLife = 0D; // The maximum amount of life that can be stored in this thing.

    // When the world loads from disk, the server needs to send the TileEntity information to the client
    //  it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this:
    //  getUpdatePacket() and onDataPacket() are used for one-at-a-time TileEntity updates
    //  getUpdateTag() and handleUpdateTag() are used by vanilla to collate together into a single chunk update packet
    //  Not really required for this example since we only use the timer on the client, but included anyway for illustration
    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        int metadata = getBlockMetadata();
        return new SPacketUpdateTileEntity(this.pos, metadata, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    /* Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
   */
    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    public void reIndexRoots(){
        roots = TreeUtil.findAllConnectedRoots(world, pos.down());
    }

    public void addRoot(BlockPos pos){
        this.roots.add(pos);
    }

    public void removeRoot(BlockPos pos){
        this.roots.remove(pos);
    }

    /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
   */
    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }

    // This is where you save any data that you don't want to lose when the tile entity unloads
    // In this case, we only need to store the ticks left until explosion, but we store a bunch of other
    //  data as well to serve as an example.
    // NBTexplorer is a very useful tool to examine the structure of your NBT saved data and make sure it's correct:
    //   http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-tools/1262665-nbtexplorer-nbt-editor-for-windows-and-mac
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound parentNBTTagCompound)
    {
        super.writeToNBT(parentNBTTagCompound); // The super call is required to save the tiles location

        parentNBTTagCompound.setTag("tickRate", new NBTTagLong(tickRate));
        parentNBTTagCompound.setTag("life", new NBTTagDouble(life));
        parentNBTTagCompound.setTag("maxlife", new NBTTagDouble(maxLife));
        parentNBTTagCompound.setTag("glutocount", new NBTTagInt(glutoCount));
        parentNBTTagCompound.setTag("funcocount", new NBTTagInt(funcoCount));

        NBTTagList shs = new NBTTagList();
        for(BlockPos b : this.storageHollows){
            NBTTagCompound thisBlockPos = new NBTTagCompound();        // NBTTagCompound is similar to a Java HashMap
            thisBlockPos.setInteger("x", b.getX());
            thisBlockPos.setInteger("y", b.getY());
            thisBlockPos.setInteger("z", b.getZ());
            shs.appendTag(thisBlockPos);
        }
        parentNBTTagCompound.setTag("storageHollows", shs);
        NBTTagList pullers = new NBTTagList();
        for(BlockPos b : this.pullers){
            NBTTagCompound thisBlockPos = new NBTTagCompound();        // NBTTagCompound is similar to a Java HashMap
            thisBlockPos.setInteger("x", b.getX());
            thisBlockPos.setInteger("y", b.getY());
            thisBlockPos.setInteger("z", b.getZ());
            pullers.appendTag(thisBlockPos);
        }
        parentNBTTagCompound.setTag("pullers", pullers);
        NBTTagList pushers = new NBTTagList();
        for(BlockPos b : this.pushers){
            NBTTagCompound thisBlockPos = new NBTTagCompound();        // NBTTagCompound is similar to a Java HashMap
            thisBlockPos.setInteger("x", b.getX());
            thisBlockPos.setInteger("y", b.getY());
            thisBlockPos.setInteger("z", b.getZ());
            pushers.appendTag(thisBlockPos);
        }
        parentNBTTagCompound.setTag("pushers", pushers);

        NBTTagList berries = new NBTTagList();
        for(BlockPos b : this.berries){
            NBTTagCompound thisBlockPos = new NBTTagCompound();        // NBTTagCompound is similar to a Java HashMap
            thisBlockPos.setInteger("x", b.getX());
            thisBlockPos.setInteger("y", b.getY());
            thisBlockPos.setInteger("z", b.getZ());
            berries.appendTag(thisBlockPos);
        }
        parentNBTTagCompound.setTag("berries", berries);

        NBTTagList roots = new NBTTagList();
        for(BlockPos b : this.roots){
            NBTTagCompound thisBlockPos = new NBTTagCompound();        // NBTTagCompound is similar to a Java HashMap
            thisBlockPos.setInteger("x", b.getX());
            thisBlockPos.setInteger("y", b.getY());
            thisBlockPos.setInteger("z", b.getZ());
            roots.appendTag(thisBlockPos);
        }
        parentNBTTagCompound.setTag("roots", roots);

        return parentNBTTagCompound;
    }

    // This is where you load the data that you saved in writeToNBT
    @Override
    public void readFromNBT(NBTTagCompound parentNBTTagCompound)
    {
        super.readFromNBT(parentNBTTagCompound); // The super call is required to load the tiles location

        tickRate = parentNBTTagCompound.getLong("tickRate");
        if(tickRate == null) tickRate = 60L;
        life = parentNBTTagCompound.getDouble("life");
        if(life == null) life = 0D;
        maxLife = parentNBTTagCompound.getDouble("maxlife");
        if(maxLife == null) maxLife = 0D;
        glutoCount = parentNBTTagCompound.getInteger("glutocount");
        if(glutoCount == null) glutoCount = 0;
        funcoCount = parentNBTTagCompound.getInteger("funcocount");
        if(funcoCount == null) funcoCount = 0;

        NBTTagList hollows = parentNBTTagCompound.getTagList("storageHollows",10);
        for(int i = 0; i < hollows.tagCount(); i++){
            NBTTagCompound comp = hollows.getCompoundTagAt(i);
            BlockPos pos = new BlockPos(comp.getInteger("x"), comp.getInteger("y"), comp.getInteger("z"));
            this.storageHollows.add(pos);
        }
        NBTTagList pullers = parentNBTTagCompound.getTagList("pullers",10);
        for(int i = 0; i < pullers.tagCount(); i++){
            NBTTagCompound comp = pullers.getCompoundTagAt(i);
            BlockPos pos = new BlockPos(comp.getInteger("x"), comp.getInteger("y"), comp.getInteger("z"));
            this.pullers.add(pos);
        }
        NBTTagList pushers = parentNBTTagCompound.getTagList("pushers",10);
        for(int i = 0; i < pushers.tagCount(); i++){
            NBTTagCompound comp = pushers.getCompoundTagAt(i);
            BlockPos pos = new BlockPos(comp.getInteger("x"), comp.getInteger("y"), comp.getInteger("z"));
            this.pushers.add(pos);
        }
        NBTTagList berries = parentNBTTagCompound.getTagList("berries", 10);
        for(int i = 0; i < berries.tagCount(); i++){
            NBTTagCompound comp = berries.getCompoundTagAt(i);
            BlockPos pos = new BlockPos(comp.getInteger("x"), comp.getInteger("y"), comp.getInteger("z"));
            this.berries.add(pos);
        }
        NBTTagList roots = parentNBTTagCompound.getTagList("roots", 10);
        for(int i = 0; i < roots.tagCount(); i++){
            NBTTagCompound comp = roots.getCompoundTagAt(i);
            BlockPos pos = new BlockPos(comp.getInteger("x"), comp.getInteger("y"), comp.getInteger("z"));
            this.roots.add(pos);
        }

    }
    public void addBerry(BlockPos position) {
        if(world.getBlockState(position).getBlock() instanceof HastoBerry){
            this.tickRate --;
            if(this.tickRate < 1){
                this.tickRate = 1L;
            }
        }
        if(world.getBlockState(position).getBlock() instanceof GlutoBerry){
            this.glutoCount ++;
        }
        if(world.getBlockState(position).getBlock() instanceof FuncoBerry){
            this.funcoCount ++;
        }
        this.berries.add(position);
    }

    public void removeBerry(BlockPos position, String name){
        if(name.equals("hastoberry")){
            this.tickRate ++;
            if(this.tickRate > 60){
                this.tickRate = 60L;
            }
        }
        if(name.equals("glutoberry")){
            this.glutoCount--;
        }
        if(name.equals("funcoberry")){
            this.funcoCount--;
            ThingsOfNaturalEnergies.logger.error("New funco: " + this.funcoCount);
        }
        this.berries.remove(position);
    }

    public void addStorageHollow(BlockPos position){
        this.storageHollows.add(position);
    }

    public void removeStorageHollow(BlockPos position){
        this.storageHollows.remove(position);
    }

    public void addPuller(BlockPos position) { this.pullers.add(position); }

    public void removePuller(BlockPos position) { this.pullers.remove(position); }

    public void addPusher(BlockPos position) { this.pushers.add(position); }

    public void removePusher(BlockPos position) { this.pushers.remove(position); }

    public boolean hasItems() {
        for(BlockPos bp : this.storageHollows){
            TEStorageHollow teStorageHollow = (TEStorageHollow) world.getTileEntity(bp);
            if(!teStorageHollow.isEmpty()){
                return true;
            }
        }
        return false;
    }

    public boolean hasRoomLeft() {
        for(BlockPos bp : this.storageHollows){
            TEStorageHollow teStorageHollow = (TEStorageHollow) world.getTileEntity(bp);
            if(!teStorageHollow.isFull()){
                return true;
            }
        }
        return false;
    }

    public void storeItemInFirstOpenSlot(ItemStack stack){
        for(BlockPos bp : this.storageHollows){
            TEStorageHollow teStorageHollow = (TEStorageHollow) world.getTileEntity(bp);
            if(!teStorageHollow.isFull()){
                teStorageHollow.addItem(stack);
                return;
            }
        }
    }

    public void reIndexMaxLife(){
        maxLife = (double) (2000 * TreeUtil.findAllTreeBlocks(world, pos).size());
    }

    @Override
    public void update() {
        if (!this.hasWorld()) return;  // prevent crash
        World world = this.getWorld();
        if (world.isRemote) return;   // don't bother doing anything on the client side.

        // Tick slowing thing
        tickcount ++;
        if(tickcount == Long.MAX_VALUE){
            tickcount = 0L;
        }

        try {
            if ((tickcount % tickRate) != 0) {
                // Timer not up yet, continue
                return;
            }
        }catch(ArithmeticException e){
            tickRate = 60L;
            return;
        }

        if((tickcount % 1200) == 0){
            reIndexMaxLife();
        }

//		this.markDirty();            // if you update a tileentity variable on the server and this should be communicated to the client,
// 																		you need to markDirty() to force a resend.  In this case, the client doesn't need to know

        if(!pullers.isEmpty() && hasRoomLeft()){
            // We can pull things, maybe!
            for(BlockPos pos : pullers){
                if(world.getBlockState(pos).getBlock() instanceof BlockPuller){
                    TEPuller tePuller = (TEPuller) world.getTileEntity(pos);
                    if(tePuller == null) continue;
                    // We need to get the item filter, here.
                    ItemStack[] filter = tePuller.getStacks();
                    // We're in good shape. Get it's facing
                    EnumFacing direction = world.getBlockState(pos).getValue(BlockPuller.FACING);
                    EnumFacing blockFace = direction.getOpposite();
                    BlockPos offset = pos.offset(direction);
                    // Allows us to stack functional blocks
                    while(world.getBlockState(offset).getBlock() instanceof BlockPuller || world.getBlockState(offset).getBlock() instanceof BlockPusher){
                        offset = offset.offset(direction);
                    }
                    if(world.isAirBlock(offset)){
                        // There is no block here.
                        continue;
                    }

                    // We are ready to pull, but do we have enough life? Maybe.
                    // This call will pull one item. We'll make one item cost 200. A stack of items is now 12,800 life
                    if(this.getLife() < (200 * (funcoCount + 1))){
                        continue;
                    }
                    boolean success = pullItems(offset, blockFace, filter);

                    if(success){
                        this.setLife(getLife() - (200 * (funcoCount + 1)));
                    }
                }
            }
        }

        if(!pushers.isEmpty() && hasItems()){
            for(BlockPos pos : pushers){
                if(world.getBlockState(pos).getBlock() instanceof BlockPusher){
                    TEPusher tePusher = (TEPusher) world.getTileEntity(pos);
                    if(tePusher == null) continue;

                    ItemStack[] filter = tePusher.getStacks();

                    EnumFacing direction = world.getBlockState(pos).getValue(BlockPuller.FACING);
                    EnumFacing blockface = direction.getOpposite();
                    BlockPos offset = pos.offset(direction);

                    while(world.getBlockState(offset).getBlock() instanceof BlockPuller || world.getBlockState(offset).getBlock() instanceof BlockPusher){
                        offset = offset.offset(direction);
                    }
                    if(world.isAirBlock(offset)){
                        continue;
                    }

                    // What was true for pulling is also true for pushing
                    if(this.getLife() < (200 * (funcoCount + 1))){
                        continue;
                    }
                    boolean success = pushItemsIfPossible(offset, blockface, filter);
                    if(success) this.setLife(getLife() - (200 * (funcoCount + 1)));
                }
            }
        }

        pullLifeIntoSystem();
    }

    private void pullLifeIntoSystem(){
        // Scan through all still connected root blocks and see if we can pull something here.
        for(BlockPos bp : roots){
            List<EntityLiving> nearbyEntities = world.getEntities(EntityLiving.class, new Predicate<EntityLiving>() {
                @Override
                public boolean apply(@Nullable EntityLiving input) {
                    return (Math.sqrt(input.getPosition().distanceSq(bp.getX(), bp.getY(), bp.getZ())) < 5D) && ((input.getHealth() / input.getMaxHealth()) > 0.5F);
                }
            });
            for(EntityLiving near : nearbyEntities){
                double amt = LifeUtil.getLifeForEntity(near);
                this.setLife(getLife() + amt);
                if(this.life > this.maxLife){
                    this.life = this.maxLife;
                    return;
                }
                LifeUtil.deductLifeFromEntity(near);
            }

            if(glutoCount > 0){
                // We can feed on some items
                List<EntityItem> nearbyItems = world.getEntities(EntityItem.class, new Predicate<EntityItem>() {
                    @Override
                    public boolean apply(@Nullable EntityItem input) {
                        return (Math.sqrt(input.getPosition().distanceSq(bp.getX(), bp.getY(), bp.getZ())) < 5D) && (input.getItem().getItem() instanceof ItemFood);
                    }
                });
                // See if we have any food nearby
                for(EntityItem i : nearbyItems) {
                    ItemStack stack = i.getItem();
                    int amt = stack.getCount();
                    ItemFood food = (ItemFood) stack.getItem();
                    int healAmt = food.getHealAmount(stack);
                    float saturation = food.getSaturationModifier(stack);
                    int totalLife = (int) ((healAmt * (10 + glutoCount)) + (saturation * (40 + glutoCount))) * amt;
                    this.setLife(getLife() + (totalLife));
                    if (this.life > this.maxLife) {
                        this.life = this.maxLife;
                        return;
                    }
                    i.setDropItemsWhenDead(false);
                    i.setDead();
                    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                }
            }
        }
    }

    public double getLife(){
        return this.life;
    }

    public void setLife(double amt){
        this.life = amt;
    }

    public double getMaxLife() {
        return this.maxLife;
    }

    private boolean itemContainedInStorage(ItemStack stackToFind){
        for(BlockPos storage : storageHollows){
            TEStorageHollow storageHollow = (TEStorageHollow) world.getTileEntity(storage);
            for (int i = 0; i < storageHollow.getSizeInventory(); ++i)
            {
                ItemStack stack = storageHollow.getStackInSlot(i);
                if(stack.isItemEqual(stackToFind)){
                    return true;
                }
            }
        }
        return false;
    }

    private List<ItemStack> allItemsInStorage() {
        List<ItemStack> toReturn = new ArrayList<ItemStack>();
        for(BlockPos storage : storageHollows){
            TEStorageHollow storageHollow = (TEStorageHollow) world.getTileEntity(storage);
            for (int i = 0; i < storageHollow.getSizeInventory(); ++i)
            {
                ItemStack stack = storageHollow.getStackInSlot(i);
                if(!stack.isEmpty()){
                    toReturn.add(stack);
                }
            }
        }
        return toReturn;
    }

    private boolean pushItemsIfPossible(BlockPos place, EnumFacing face, ItemStack[] filter){
        IInventory target = TileEntityHopper.getInventoryAtPosition(world, place.getX(), place.getY(), place.getZ());
        boolean itemWasPushed = false;
        if (target == null)
        {
            return false;
        }
        else
        {
            // There is an inventory here
            if (this.isInventoryFull(target, face))
            {
                // It's full
                return false;
            }
            else
            {
                // See if we can find the items that go in here

                int amtToPull = 1 + this.funcoCount;
                int count = 0;
                for(BlockPos storage : storageHollows){
                    TEStorageHollow storageHollow = (TEStorageHollow) world.getTileEntity(storage);
                    for (int i = 0; i < storageHollow.getSizeInventory(); ++i)
                    {
                        ItemStack stack = storageHollow.getStackInSlot(i);
                        if(!matchesFilter(stack, new CompareOptions(filter)))continue;
                        if (!stack.isEmpty())
                        {
                            ItemStack itemstack = storageHollow.getStackInSlot(i).copy().splitStack(1); // Get one item off the top

                            TileEntityHopper.putStackInInventoryAllSlots(storageHollow, target, itemstack, face);
                            itemWasPushed = true;
                            ItemStack second = storageHollow.decrStackSize(i, 1);
                            if(second.isEmpty()){
                                storageHollow.removeStackFromSlot(i);
                            }
                            count++;
                            if(count == amtToPull){
                                break;
                            }
                        }
                    }
                }

                if(count == amtToPull)
                    return itemWasPushed;

                // See if we can output a crafted material? Sounds good
                tryAutocraft(filter, count, amtToPull, target, face);
            }
        }
        return itemWasPushed;
    }

    private void tryAutocraft(ItemStack[] filter, int count, int amtToPull, IInventory target, EnumFacing face){
        List<ItemStack> alreadyHave = allItemsInStorage();
        for(ItemStack is : filter){
            if(is.isEmpty()) continue;
            if(!(is.getItem() instanceof ItemToken) && !itemContainedInStorage(is)){
                // It's not a token, so we can try to craft it!
                CraftingOperation co = RecipeUtil.getRequiredItemsToMakeIfPossible(is.getItem(), alreadyHave);
                if(co == null){
                    continue;
                }
                if(craftoCount >= co.getComplexity()){
                    // If we get here, we *can* craft this item, theoretically.
                    // So, let's do it.
                    for(Map.Entry<Integer, RecipeUtil.ComparableItem> step : co.getSteps().entrySet()){
                        IRecipe recipe = RecipeUtil.getRecipe(step.getValue().getObject()).get(0);
                        for(Ingredient i : recipe.getIngredients()){
                            for(ItemStack stack : i.getMatchingStacks()){
                                if(tryRemoveItemFromInventory(stack) != null)
                                    break;
                            }
                        }
                        // Now that we've removed the necessary ingredients, let's pop out the output.
                        storeItemInFirstOpenSlot(recipe.getRecipeOutput());
                    }
                    // We've done all the steps. Theoretically the final product is somewhere in there.
                    //TEStorageHollow tesh = tryRemoveItemFromInventory(is);
//                    TileEntityHopper.putStackInInventoryAllSlots(null, target, is, face);
//                    count++;
//                    if(count == amtToPull){
//                        break;
//                    }
                    // This ought to have spit out the items, after decrementing the ingredients.
                }
            }
        }
    }

    private TEStorageHollow tryRemoveItemFromInventory(ItemStack stackToRemove){
        for(BlockPos storage : storageHollows){
            TEStorageHollow storageHollow = (TEStorageHollow) world.getTileEntity(storage);
            for (int i = 0; i < storageHollow.getSizeInventory(); ++i)
            {
                ItemStack stack = storageHollow.getStackInSlot(i);
                if (!stack.isEmpty())
                {
                    if(!stack.isItemEqual(stackToRemove)){
                        continue;
                    }
                    ItemStack second = storageHollow.decrStackSize(i, stackToRemove.getCount());
                    if(second.isEmpty()){
                        storageHollow.removeStackFromSlot(i);
                    }
                    return storageHollow;
                }
            }
        }
        return null;
    }

    private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side)
    {
        if (inventoryIn instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int k : aint)
            {
                ItemStack itemstack1 = isidedinventory.getStackInSlot(k);

                if (itemstack1.isEmpty() || itemstack1.getCount() != itemstack1.getMaxStackSize())
                {
                    return false;
                }
            }
        }
        else
        {
            int i = inventoryIn.getSizeInventory();

            for (int j = 0; j < i; ++j)
            {
                ItemStack itemstack = inventoryIn.getStackInSlot(j);

                if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
                {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side)
    {
        return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canExtractItem(index, stack, side);
    }

    private boolean compareItems(ItemStack toCheck, ItemStack filter, CompareOptions co){
        if(filter == null){
            // We're checking for broad category type filters.
            /*
            public boolean pullAll = false; // Pull all items?
        public boolean invert = false; // If true, all values will be inverted. "ex: pull NOT full stacks, pull NOT named this"
        public boolean compareItemType = true; // Should the item type be compared. On by default.
        public boolean compareItemAmount = false; // Compare the amount of items? Useful if you want to pull, say, a stack at a time.
        public boolean compareItemName = false; // Compare the literal name of the items for match?
        public boolean compareItemNBT = false; // Should NBT data match?
        public boolean pullAnyNBT = false; // Should pull any item with nbt data?
        public boolean compareEnchantment = false; // Should it have the same enchantment?
        public boolean pullAnyEnchantment = false; // Pull any random enchantment?
             */
            if(co.pullAll) return true;
            if(co.pullAnyEnchantment || co.pullAnyNBT){
                if(toCheck.hasTagCompound()){
                    return true;
                }
            }
        }else{
            // We have two items to compare
            // 'toCheck' is the item we're checking, filter is the item we're filtering
            boolean resultSoFar = true;
            if(co.compareItemType){
                if(toCheck.getItem() != filter.getItem()){
                    resultSoFar = false;
                }
            }
            if(co.compareItemName){
                if(!toCheck.getDisplayName().equals(filter.getDisplayName())){
                    resultSoFar = false;
                }
            }
            if(co.compareItemAmount){
                if(toCheck.getCount() != filter.getCount()){
                    resultSoFar = false;
                }
            }
            if(co.compareMeta){
                if(toCheck.getMetadata() != filter.getMetadata()){
                    resultSoFar = false;
                }
            }
            if(co.compareItemNBT || co.compareEnchantment){
                if(!toCheck.getEnchantmentTagList().equals(filter.getEnchantmentTagList())){
                    resultSoFar = false;
                }
            }
            return co.invert ? !resultSoFar : resultSoFar;
        }
        return false;
    }

    private boolean matchesFilter(ItemStack item, CompareOptions co){
        if(compareItems(item, null, co)) return true;

        for(ItemStack stack : co.filterItems){
            if(compareItems(item, stack, co)){
                return true;
            }
        }
        return false;
    }

    private boolean pullItems(BlockPos place, EnumFacing face, ItemStack[] filter) {
        IInventory target = TileEntityHopper.getInventoryAtPosition(world, place.getX(), place.getY(), place.getZ());
        boolean itemWasPulled = false;
        if (target != null){
            // We can see this inventory
            if(isInventoryEmpty(target, face)){
                // We could not pull an item from this block
                return false;
            }

            int amtToPull = 1 + this.funcoCount;
            int count = 0;
            if(target instanceof ISidedInventory) {
                ISidedInventory iSidedInventory = (ISidedInventory) target;
                int[] aint = iSidedInventory.getSlotsForFace(face);
                for(int i : aint){
                    ItemStack itemstack = target.getStackInSlot(i);

                    if(!itemstack.isEmpty() && canExtractItemFromSlot(target, itemstack, i, face)){
                        // We're good - theoretically, for this item, but does it match the filter?
                        if(!matchesFilter(itemstack, new CompareOptions(filter)))continue;
                        ItemStack one = itemstack.copy().splitStack(1);
                        storeItemInFirstOpenSlot(one);
                        itemWasPulled = true;
                        ItemStack second = target.decrStackSize(i, 1);
                        if(second.isEmpty()){
                            target.removeStackFromSlot(i);
                        }
                        count++;
                        if(count == amtToPull){
                            break;
                        }
                    }
                }
            }else{
                int j = target.getSizeInventory();

                for(int k = 0; k < j; ++k){
                    ItemStack itemstack = target.getStackInSlot(k);

                    if(!itemstack.isEmpty() && canExtractItemFromSlot(target, itemstack, k, face)){
                        // We're good - theoretically, for this item, but does it match the filter?
                        if(!matchesFilter(itemstack, new CompareOptions(filter)))continue;
                        // We're good. Let's store it.
                        ItemStack one = itemstack.copy().splitStack(1);

                        storeItemInFirstOpenSlot(one);
                        itemWasPulled = true;
                        ItemStack second = target.decrStackSize(k, 1);
                        if(second.isEmpty()){
                            target.removeStackFromSlot(k);
                        }
                        count++;
                        if(count == amtToPull){
                            break;
                        }
                    }
                }
            }
        }else{
            // There is no inventory here
            return false;
        }
        return itemWasPulled;
    }

    private static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side)
    {
        if (inventoryIn instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int i : aint)
            {
                if (!isidedinventory.getStackInSlot(i).isEmpty())
                {
                    return false;
                }
            }
        }
        else
        {
            int j = inventoryIn.getSizeInventory();

            for (int k = 0; k < j; ++k)
            {
                if (!inventoryIn.getStackInSlot(k).isEmpty())
                {
                    return false;
                }
            }
        }

        return true;
    }

    // These define the behavior of the comparison algorithm. The puller can ONLY pull items which match those things
    // put into its inventory, unless you add special tokens that change behavior.
    private class CompareOptions {

        public boolean pullAll = false; // Pull all items?
        public boolean invert = false; // If true, all values will be inverted. "ex: pull NOT full stacks, pull NOT named this"
        public boolean compareMeta = true;
        public boolean compareItemType = true; // Should the item type be compared. On by default.
        public boolean compareItemAmount = false; // Compare the amount of items? Useful if you want to pull, say, a stack at a time.
        public boolean compareItemName = false; // Compare the literal name of the items for match?
        public boolean compareItemNBT = false; // Should NBT data match?
        public boolean pullAnyNBT = false; // Should pull any item with nbt data?
        public boolean compareEnchantment = false; // Should it have the same enchantment?
        public boolean pullAnyEnchantment = false; // Pull any random enchantment?

        public ItemStack[] filterItems;

        public CompareOptions(ItemStack[] filterItems){
            this.filterItems = filterItems;

            ArrayList<ItemStack> withSpecialsRemoved = new ArrayList<>();
            // TODO go through items in the filter list, find any special thigns and mark those flags.
            for(ItemStack is : filterItems){
                if(is.getItem() == ModItems.tokenPullAll){
                    // We don't need this one
                    pullAll = true;
                    continue;
                }
                withSpecialsRemoved.add(is);
            }

            ItemStack[] newFilter = new ItemStack[withSpecialsRemoved.size()];
            for(int i = 0; i < newFilter.length; i++){
                newFilter[i] = withSpecialsRemoved.get(i);
            }
            this.filterItems = newFilter;
        }

    }

}
