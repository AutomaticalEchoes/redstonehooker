package com.automaticalechoes.redstonehooker.api.hooker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public interface ILevel {
    @Nullable
    BlockEntity getVanillaBlockEntity(BlockPos p_46716_);
}
