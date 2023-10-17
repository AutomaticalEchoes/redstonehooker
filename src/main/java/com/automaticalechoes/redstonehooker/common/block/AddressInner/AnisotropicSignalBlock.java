package com.automaticalechoes.redstonehooker.common.block.AddressInner;

import com.automaticalechoes.redstonehooker.common.block.AddressInnerBlock;
import com.automaticalechoes.redstonehooker.common.blockentity.BlockPosProxy.AnisotropicSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AnisotropicSignalBlock extends AddressInnerBlock {

    public AnisotropicSignalBlock(Properties p_49795_) {
        super(p_49795_);
    }
    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public int getDirectSignal(BlockState p_60559_, BlockGetter p_60560_, BlockPos p_60561_, Direction p_60562_) {
        return getSignal(p_60559_, p_60560_, p_60561_, p_60562_);
    }

    @Override
    public int getSignal(BlockState p_60483_, BlockGetter p_60484_, BlockPos p_60485_, Direction p_60486_) {
        if(p_60484_ instanceof ServerLevel serverLevel && serverLevel.getBlockEntity(p_60485_) instanceof AnisotropicSignalBlockEntity anisotropicSignalBlockEntity){
            return anisotropicSignalBlockEntity.getDirectionSignal(p_60486_.getOpposite());
        }
        return 0;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new AnisotropicSignalBlockEntity(p_153215_,p_153216_);
    }

    @Override
    public boolean isSignalSource(BlockState p_60571_) {
        return true;
    }

}
