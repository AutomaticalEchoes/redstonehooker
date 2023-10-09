package com.automaticalechoes.redstonehooker.api.hooker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

public interface ILevelChunk {
    @Nullable
    BlockEntity getVanillaBlockEntity(BlockPos pos);
    @Nullable
    BlockEntity getVanillaBlockEntity(BlockPos pos,LevelChunk.EntityCreationType type);

}
