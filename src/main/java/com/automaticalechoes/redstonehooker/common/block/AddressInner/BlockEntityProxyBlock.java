package com.automaticalechoes.redstonehooker.common.block.AddressInner;

import com.automaticalechoes.redstonehooker.api.hooker.Proxys;
import com.automaticalechoes.redstonehooker.common.block.AddressInnerBlock;
import com.automaticalechoes.redstonehooker.common.blockentity.BlockPosProxy.ProxyBlockEntity;
import com.automaticalechoes.redstonehooker.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BlockEntityProxyBlock extends AddressInnerBlock {
    private static final VoxelShape SHAPE = Block.box(1.0F,0.0F,1.0F,15.0F,14.0F,15.0F);
    public BlockEntityProxyBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return BlockEntityRegister.BLOCK_ENTITY_PROXY_BLOCK_ENTITY.get().create(p_153215_,p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return (p_155253_, p_155254_, p_155255_, p_155256_) -> {
            ((ProxyBlockEntity)p_155256_).tick(p_153212_,p_155254_,p_155255_);
        };
    }


}
