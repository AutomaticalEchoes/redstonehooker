package com.automaticalechoes.redstonehooker.common.entity;

import com.automaticalechoes.redstonehooker.common.item.AddressItem;
import com.automaticalechoes.redstonehooker.common.item.AddressNameTagItem;
import com.automaticalechoes.redstonehooker.register.EntityRegister;
import com.automaticalechoes.redstonehooker.register.ItemRegister;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class AddressTagProjectile extends AbstractArrow implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(AddressTagProjectile.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> TURN = SynchedEntityData.defineId(AddressTagProjectile.class,EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> VERTICAL = SynchedEntityData.defineId(AddressTagProjectile.class,EntityDataSerializers.BOOLEAN);
    public  int tickCount = 0;
    public int turnAngle = 0;

    public static AddressTagProjectile Create(EntityType<? extends AddressTagProjectile> p_37442_, Level p_37443_){
        return new AddressTagProjectile(p_37443_);
    }

    public AddressTagProjectile(Level p_37443_) {
        super(EntityRegister.ADDRESS_TAG_PROJECTILE.get(), p_37443_);
    }

    public AddressTagProjectile(double p_37433_, double p_37434_, double p_37435_, Level p_37436_) {
        super(EntityRegister.ADDRESS_TAG_PROJECTILE.get(), p_37433_, p_37434_, p_37435_, p_37436_);
    }

    public AddressTagProjectile(LivingEntity p_37439_, Level p_37440_) {
        super(EntityRegister.ADDRESS_TAG_PROJECTILE.get(), p_37439_, p_37440_);
    }

    public AddressTagProjectile(LivingEntity p_37439_, Level p_37440_, ItemStack itemStack) {
        super(EntityRegister.ADDRESS_TAG_PROJECTILE.get(), p_37439_, p_37440_);
        this.setItem(itemStack);
    }


    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
        this.getEntityData().define(TURN,false);
        this.getEntityData().define(VERTICAL,true);
    }

    public void addAdditionalSaveData(CompoundTag p_37449_) {
        super.addAdditionalSaveData(p_37449_);
        ItemStack itemstack = this.getItemRaw();
        if (!itemstack.isEmpty()) {
            p_37449_.put("Item", itemstack.save(new CompoundTag()));
        }

        p_37449_.putBoolean("turn",this.isTurn());
        p_37449_.putBoolean("vertical",this.isVertical());
    }

    public void readAdditionalSaveData(CompoundTag p_37445_) {
        super.readAdditionalSaveData(p_37445_);
        ItemStack itemstack = ItemStack.of(p_37445_.getCompound("Item"));
        this.setTurn(p_37445_.getBoolean("turn"));
        this.setVertical(p_37445_.getBoolean("vertical"));
        this.setItem(itemstack);
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        BlockState blockstate = this.level().getBlockState(p_37258_.getBlockPos());
        blockstate.onProjectileHit(this.level(), blockstate, p_37258_, this);
        Vec3 vec3 = p_37258_.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale((double)0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.inGround = true;
        this.shakeTime = 7;
        this.setSoundEvent(SoundEvents.GLASS_HIT);
        this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.setShotFromCrossbow(false);
        if(!(this.level() instanceof ServerLevel serverLevel) ||!(this.getItem().getItem() instanceof AddressItem && this.getOwner() instanceof Player player)) return;
        ItemStack defaultInstance = ItemRegister.ADDRESS_ITEM.get().getDefaultInstance();
        if(AddressItem.putAddress(defaultInstance,p_37258_.getBlockPos(),p_37258_.getDirection().get3DDataValue())) {
            putAndDiscord(serverLevel,player,defaultInstance,this.getX() ,this.getY() ,this.getZ());
        }

    }

    @Override
    public boolean canChangeDimensions() {
        if(this.level() instanceof ServerLevel serverLevel
                && this.isAlive() && this.getItem().getItem() instanceof AddressItem
                && this.getOwner() instanceof Player player){
            ItemStack defaultInstance = ItemRegister.ADDRESS_ITEM.get().getDefaultInstance();
            if(AddressItem.putAddress(defaultInstance,this.blockPosition(),0)) {
                putAndDiscord(serverLevel,player,defaultInstance,this.getX() ,this.getY() ,this.getZ());
                return false;
            }
        }
        return super.canChangeDimensions();
    }

    @Override
    protected ItemStack getPickupItem() {
        return getItem();
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        if(!(this.level() instanceof ServerLevel serverLevel)
                || !p_37259_.getEntity().isAlive()
                || !(this.getItem().getItem() instanceof AddressNameTagItem && this.getOwner() instanceof Player player)
                || !this.getItem().hasCustomHoverName()) return;

        ItemStack defaultInstance = ItemRegister.ADDRESS_ITEM.get().getDefaultInstance();
        if(AddressItem.putEntityAddress(defaultInstance,p_37259_.getEntity().getUUID(),this.getItem().getHoverName(),p_37259_.getEntity() instanceof Player)){
            Entity entity = p_37259_.getEntity();
            entity.setCustomName(this.getItem().getHoverName());
            if (entity instanceof Mob) {
                ((Mob)entity).setPersistenceRequired();
            }

            putAndDiscord(serverLevel,player,defaultInstance,entity.getX(),entity.getY(),entity.getZ());
        }
    }

    public void putAndDiscord(ServerLevel serverLevel, Player player, ItemStack itemStack, double x, double y, double z){
        serverLevel.sendParticles(ParticleTypes.GLOW, x, y, z,20,0,1,0,0.05D);
        player.playSound(SoundEvents.PLAYER_LEVELUP);
        player.addItem(itemStack);
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        if(isTurn()) this.turnAngle++;
        if(!this.isAlive()) return;
        tickCount ++ ;

    }

    //_____________________________________________________________________________________________________________________________

    public void setVertical(boolean isVertical){
        this.entityData.set(VERTICAL ,isVertical);
    }

    public Boolean isVertical() {
        return this.getEntityData().get(VERTICAL);
    }

    public void setTurn(boolean turn) {
        this.entityData.set(TURN,turn);
    }

    public boolean isTurn() {
        return entityData.get(TURN);
    }

    public int TickCount() {
        return tickCount;
    }

    public void setItem(ItemStack p_37447_) {
        if (!p_37447_.is(this.getDefaultItem()) || p_37447_.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, Util.make(p_37447_.copy(), (p_37451_) -> {
                p_37451_.setCount(1);
            }));
        }
    }

    protected Item getDefaultItem(){
       return ItemRegister.ADDRESS_ITEM.get();
    }

    protected ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
    }



}
