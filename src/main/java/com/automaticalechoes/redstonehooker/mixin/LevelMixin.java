package com.automaticalechoes.redstonehooker.mixin;

import com.automaticalechoes.redstonehooker.api.hooker.ILevel;
import com.automaticalechoes.redstonehooker.api.hooker.ILevelChunk;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(Level.class)
public class LevelMixin implements ILevel {
    @Shadow
    @Final
    Thread thread;

    @Nullable
    public BlockEntity getVanillaBlockEntity(BlockPos p_46716_) {
        Level level = ((Level)(Object)this);
        if (level.isOutsideBuildHeight(p_46716_)) {
            return null;
        } else {
            return !level.isClientSide && Thread.currentThread() != thread ? null :( (ILevelChunk)(Object)level.getChunkAt(p_46716_)).getVanillaBlockEntity(p_46716_, LevelChunk.EntityCreationType.IMMEDIATE);
        }
    }

}
