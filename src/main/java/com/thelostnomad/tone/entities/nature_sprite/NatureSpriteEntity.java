package com.thelostnomad.tone.entities.nature_sprite;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class NatureSpriteEntity extends EntityMob {

    // We reuse the zombie model which has arms that need to be raised when the zombie is attacking:
    // private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.createKey(NatureSpriteEntity.class, DataSerializers.BOOLEAN);

    public static final ResourceLocation LOOT = new ResourceLocation(ThingsOfNaturalEnergies.MODID, "entities/nature_sprite");
    private BlockPos spawnPosition;

    public NatureSpriteEntity(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.9F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        //this.getDataManager().register(ARMS_RAISED, Boolean.valueOf(false));
    }

    public void onUpdate() {
        super.onUpdate();
    }

//    protected void updateAITasks() {
//        super.updateAITasks();
//        BlockPos blockpos = new BlockPos(this);
//        BlockPos blockpos1 = blockpos.up();
//        if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
//            this.spawnPosition = null;
//        }
//
//        if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((double) ((int) this.posX), (double) ((int) this.posY), (double) ((int) this.posZ)) < 4.0D) {
//            this.spawnPosition = new BlockPos((int) this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int) this.posY + this.rand.nextInt(6) - 2, (int) this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
//        }
//
//        double d0 = (double) this.spawnPosition.getX() + 0.5D - this.posX;
//        double d1 = (double) this.spawnPosition.getY() + 0.1D - this.posY;
//        double d2 = (double) this.spawnPosition.getZ() + 0.5D - this.posZ;
//        this.motionX += (Math.signum(d0) * 0.5D - this.motionX) * 0.10000000149011612D;
//        this.motionY += (Math.signum(d1) * 0.699999988079071D - this.motionY) * 0.10000000149011612D;
//        this.motionZ += (Math.signum(d2) * 0.5D - this.motionZ) * 0.10000000149011612D;
//        float f = (float) (MathHelper.atan2(this.motionZ, this.motionX) * (180D / Math.PI)) - 90.0F;
//        float f1 = MathHelper.wrapDegrees(f - this.rotationYaw);
//        this.moveForward = 0.5F;
//        this.rotationYaw += f1;
//    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        // Here we set various attributes for our mob. Like maximum health, armor, speed, ...
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
//        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
//        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.13D);
//        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
//        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
//        this.tasks.addTask(2, new EntityAIWeirdZombieAttack(this, 1.0D, false));
//        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }

    private void applyEntityAI() {
//        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
//        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[]{EntityPigZombie.class}));
//        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
//        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
//        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
//        if (super.attackEntityAsMob(entityIn)) {
//            if (entityIn instanceof EntityLivingBase) {
//                // This zombie gives health boost and regeneration when it attacks
//                ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 200));
//                ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 200));
//            }
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    public static void registerFixesBat(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityBat.class);
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else {
            return super.attackEntityFrom(source, amount);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        //this.dataManager.set(HANGING, Byte.valueOf(compound.getByte("BatFlags")));
    }

    public boolean getCanSpawnHere() {
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);

        return true; // for now, it can spawn anywhere.
    }

    public float getEyeHeight() {
        return this.height / 2.0F;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        //compound.setByte("BatFlags", ((Byte)this.dataManager.get(HANGING)).byteValue());
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 5;
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

}
