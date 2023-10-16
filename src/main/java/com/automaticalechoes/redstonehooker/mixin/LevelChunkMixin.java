package com.automaticalechoes.redstonehooker.mixin;

import com.automaticalechoes.redstonehooker.api.hooker.AddressListenerManager;
import com.automaticalechoes.redstonehooker.api.hooker.ILevelChunk;
import com.automaticalechoes.redstonehooker.common.blockentity.ProxyBlockEntity;
import com.automaticalechoes.redstonehooker.event.server.OnListenPosChange;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin extends ChunkAccess implements ILevelChunk, LevelChunkInvoker {
    public LevelChunkMixin(ChunkPos p_187621_, UpgradeData p_187622_, LevelHeightAccessor p_187623_, Registry<Biome> p_187624_, long p_187625_, @Nullable LevelChunkSection[] p_187626_, @Nullable BlendingData p_187627_) {
        super(p_187621_, p_187622_, p_187623_, p_187624_, p_187625_, p_187626_, p_187627_);
    }

//   SRG name: m_5685_(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/chunk/LevelChunk$EntityCreationType;)Lnet/minecraft/world/level/block/entity/BlockEntity;
    @Inject(method = "getBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/chunk/LevelChunk$EntityCreationType;)Lnet/minecraft/world/level/block/entity/BlockEntity;",at = {@At("RETURN")}, cancellable = true)
    public void getBlockEntity(BlockPos p_62868_, LevelChunk.EntityCreationType p_62869_, CallbackInfoReturnable<BlockEntity> infoReturnable){
        if(infoReturnable.getReturnValue() instanceof ProxyBlockEntity proxyBlockEntity){
            infoReturnable.setReturnValue(proxyBlockEntity.getProxyTarget());
        }
    }

    @Inject(method = "setBlockState",at = {@At("RETURN")})
    public void setBlockState(BlockPos p_62865_, BlockState p_62866_, boolean p_62867_,CallbackInfoReturnable<BlockState> infoReturnable){
        if(infoReturnable.getReturnValue() != null && AddressListenerManager.posContainersListener(p_62865_)){
            MinecraftForge.EVENT_BUS.post(new OnListenPosChange(p_62865_));
        }
    }

    @Redirect(
            method = "getBlockEntityNbtForSaving",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunk;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;")
    )
    private BlockEntity redirectGetBlockEntity(LevelChunk level, BlockPos pos){
        return getVanillaBlockEntity(pos);
    }

    @Nullable
    public BlockEntity getVanillaBlockEntity(BlockPos pos){
        return getVanillaBlockEntity(pos, LevelChunk.EntityCreationType.CHECK);
    }

    @Nullable
    public BlockEntity getVanillaBlockEntity(BlockPos pos, LevelChunk.EntityCreationType type){
        BlockEntity blockentity = this.blockEntities.get(pos);
        if (blockentity != null && blockentity.isRemoved()) {
            blockEntities.remove(pos);
            blockentity = null;
        }
        if (blockentity == null) {
            CompoundTag compoundtag = this.pendingBlockEntities.remove(pos);
            if (compoundtag != null) {
                 blockentity = this.invokePromotePendingBlockEntity(pos, compoundtag);
            }
        }

        if (blockentity == null) {
            if (type == LevelChunk.EntityCreationType.IMMEDIATE) {
                blockentity = this.invokeCreateBlockEntity(pos);
                if(blockentity != null){
                    ((LevelChunk)(Object)this).addAndRegisterBlockEntity(blockentity);
                }
            }
        }

        return blockentity;
    }
}
