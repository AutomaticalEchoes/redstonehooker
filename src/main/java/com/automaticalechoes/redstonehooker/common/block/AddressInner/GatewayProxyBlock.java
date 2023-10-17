package com.automaticalechoes.redstonehooker.common.block.AddressInner;

import com.automaticalechoes.redstonehooker.api.hooker.Proxys;
import com.automaticalechoes.redstonehooker.common.block.AddressInnerBlock;
import com.automaticalechoes.redstonehooker.common.blockentity.BlockPosProxy.GatewayProxyBlockEntity;
import com.automaticalechoes.redstonehooker.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GatewayProxyBlock extends AddressInnerBlock {
    private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(),
            Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D),
                    box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D),
                    box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);
    public GatewayProxyBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState p_60495_, Level p_60496_, BlockPos p_60497_, Entity p_60498_) {
        if(Proxys.getVanillaBlockEntity(p_60496_,p_60497_) instanceof GatewayProxyBlockEntity proxyBlockEntity)
            proxyBlockEntity.entityInside(p_60498_);
        super.entityInside(p_60495_, p_60496_, p_60497_, p_60498_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return BlockEntityRegister.GATEWAY_PROXY_BLOCK_ENTITY.get().create(p_153215_,p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return (p_155253_, p_155254_, p_155255_, p_155256_) -> ((GatewayProxyBlockEntity)p_155256_).tick(p_155253_,p_155254_,p_155255_);
    }
}
