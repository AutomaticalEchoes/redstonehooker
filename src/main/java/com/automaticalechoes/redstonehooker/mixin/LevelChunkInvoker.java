package com.automaticalechoes.redstonehooker.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelChunk.class)
public interface LevelChunkInvoker {
    @Invoker
    BlockEntity invokePromotePendingBlockEntity(BlockPos p_62871_, CompoundTag p_62872_);
    @Invoker
    BlockEntity invokeCreateBlockEntity(BlockPos p_62935_);

}
