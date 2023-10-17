package com.automaticalechoes.redstonehooker.common.blockentity.BlockPosProxy;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.DataBlockEntity;
import com.automaticalechoes.redstonehooker.api.dataBlockEntity.SynchedBlockEntityData;
import com.automaticalechoes.redstonehooker.api.messageEntityBlock.MessagesPreviewable;
import com.automaticalechoes.redstonehooker.common.blockentity.BlockPosProxyBlockEntity;
import com.automaticalechoes.redstonehooker.common.item.AddressItem;
import com.automaticalechoes.redstonehooker.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndGatewayBlock;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class GatewayProxyBlockEntity extends BlockPosProxyBlockEntity<BlockState> {
    private static final EntityDataAccessor<Integer> GATEWAY_TYPE =  SynchedBlockEntityData.defineId(GatewayProxyBlockEntity.class,EntityDataSerializers.INT);
    public GatewayProxyBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(BlockEntityRegister.GATEWAY_PROXY_BLOCK_ENTITY.get(), p_155229_, p_155230_);
    }

    @Override
    public void define() {
        super.define();
        this.blockEntityData.define(GATEWAY_TYPE,0);
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        super.saveAdditional(p_187471_);
        p_187471_.putInt("gateway_type",getGatewayType());
    }

    @Override
    public void load(CompoundTag p_155245_) {
        super.load(p_155245_);
        this.setGatewayType(p_155245_.getInt("gateway_type"));
    }

    @Nullable
    public BlockState getProxyTarget() {
        return isProxyNull() ? this.getBlockState() : proxyTarget;
    }

    public boolean isProxyNull(){
        return this.proxyTarget == null;
    }

    public void initGatewayType(){
        if(this.proxyTarget.getBlock() instanceof EndPortalBlock || this.proxyTarget.getBlock() instanceof EndGatewayBlock){
            setGatewayType(3);
        }else if(this.proxyTarget.getBlock() instanceof NetherPortalBlock){
            setGatewayType(2);
        }else {
            setGatewayType(1);
        }
    }

    public void entityInside(Entity entity){
        if(this.level instanceof ServerLevel serverLevel && this.proxyTargetPos != null && this.proxyTarget !=null) {
            if(this.level.getBlockEntity(proxyTargetPos) instanceof TheEndGatewayBlockEntity endGatewayBlockEntity && !endGatewayBlockEntity.isCoolingDown()){
                TheEndGatewayBlockEntity.teleportEntity(serverLevel,endGatewayBlockEntity.getBlockPos(),endGatewayBlockEntity.getBlockState(),entity,endGatewayBlockEntity);
            }else {
                this.proxyTarget.entityInside(this.level,this.proxyTarget.is(Blocks.NETHER_PORTAL) ? this.proxyTargetPos : this.getBlockPos(),entity);
            }
        }
    }

    public void blockChange(BlockPos pos){
        this.proxyTargetPos = getAddress(0);
        if(proxyTargetPos == null) {
            setError(1);
            return;
        }

        BlockState blockState = level.getBlockState(proxyTargetPos);
        if(!blockState.isAir()){
            this.proxyTarget = blockState;
            initGatewayType();
            setError(0);
        }else {
            setError(5);
        }

        if(level instanceof ServerLevel serverLevel){
            serverLevel.getPoiManager().getType(proxyTargetPos)
                    .ifPresent(poiTypeHolder -> serverLevel.getPoiManager().add(pos, poiTypeHolder));
        }
    }

    public int getGatewayType(){
        return this.blockEntityData.get(GATEWAY_TYPE);
    }

    public void setGatewayType(int num){
        this.blockEntityData.set(GATEWAY_TYPE,num);
    }

    @Override
    public void reset() {
        setGatewayType(0);
        super.reset();
    }
}
