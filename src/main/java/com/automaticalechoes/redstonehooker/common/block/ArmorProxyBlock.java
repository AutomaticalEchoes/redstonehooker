package com.automaticalechoes.redstonehooker.common.block;

import com.automaticalechoes.redstonehooker.common.blockentity.ArmorProxyBlockEntity;
import com.automaticalechoes.redstonehooker.common.blockentity.InventoryEntityProxyBlockEntity;
import com.automaticalechoes.redstonehooker.register.BlockEntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class ArmorProxyBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final VoxelShape SHAPE = Block.box(1.0F,1.0F,1.0F,15.0F,15.0F,15.0F);
//    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public ArmorProxyBlock(BlockBehaviour.Properties p_273303_) {
        super(p_273303_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(POWERED, Boolean.FALSE));
//        .setValue(HALF, DoubleBlockHalf.LOWER)
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

//    public BlockState updateShape(BlockState p_52796_, Direction p_52797_, BlockState p_52798_, LevelAccessor p_52799_, BlockPos p_52800_, BlockPos p_52801_) {
//        DoubleBlockHalf doubleblockhalf = p_52796_.getValue(HALF);
//        if (p_52797_.getAxis() == Direction.Axis.Y && doubleblockhalf == DoubleBlockHalf.LOWER == (p_52797_ == Direction.UP)) {
//            return p_52798_.is(this) && p_52798_.getValue(HALF) != doubleblockhalf ?
//                    p_52796_.setValue(FACING, p_52798_.getValue(FACING)).setValue(POWERED, p_52798_.getValue(POWERED)) : Blocks.AIR.defaultBlockState();
//        } else {
//            return doubleblockhalf == DoubleBlockHalf.LOWER && p_52797_ == Direction.DOWN && !p_52796_.canSurvive(p_52799_, p_52800_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_52796_, p_52797_, p_52798_, p_52799_, p_52800_, p_52801_);
//        }
//    }

//    public void playerWillDestroy(Level p_52755_, BlockPos p_52756_, BlockState p_52757_, Player p_52758_) {
//        if (!p_52755_.isClientSide && p_52758_.isCreative()) {
//            preventCreativeDropFromBottomPart(p_52755_, p_52756_, p_52757_, p_52758_);
//        }
//        super.playerWillDestroy(p_52755_, p_52756_, p_52757_, p_52758_);
//    }
//
//    @Nullable
//    public BlockState getStateForPlacement(BlockPlaceContext p_52739_) {
//        BlockPos blockpos = p_52739_.getClickedPos();
//        Level level = p_52739_.getLevel();
//        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(p_52739_)) {
//            boolean flag = level.hasNeighborSignal(blockpos) || level.hasNeighborSignal(blockpos.above());
//            return this.defaultBlockState().setValue(FACING, p_52739_.getHorizontalDirection()).setValue(POWERED, Boolean.valueOf(flag)).setValue(HALF, DoubleBlockHalf.LOWER);
//        } else {
//            return null;
//        }
//    }
//
//    public void setPlacedBy(Level p_52749_, BlockPos p_52750_, BlockState p_52751_, LivingEntity p_52752_, ItemStack p_52753_) {
//        p_52749_.setBlock(p_52750_.above(), p_52751_.setValue(HALF, DoubleBlockHalf.UPPER), 3);
//    }
//
//    public void neighborChanged(BlockState p_52776_, Level p_52777_, BlockPos p_52778_, Block p_52779_, BlockPos p_52780_, boolean p_52781_) {
//        boolean flag = p_52777_.hasNeighborSignal(p_52778_) || p_52777_.hasNeighborSignal(p_52778_.relative(p_52776_.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
//        if (!this.defaultBlockState().is(p_52779_) && flag != p_52776_.getValue(POWERED)) {
//            p_52777_.setBlock(p_52778_, p_52776_.setValue(POWERED, Boolean.valueOf(flag)), 2);
//        }
//    }
//
//    public boolean canSurvive(BlockState p_52783_, LevelReader p_52784_, BlockPos p_52785_) {
//        BlockPos blockpos = p_52785_.below();
//        BlockState blockstate = p_52784_.getBlockState(blockpos);
//        return p_52783_.getValue(HALF) == DoubleBlockHalf.LOWER ? blockstate.isFaceSturdy(p_52784_, blockpos, Direction.UP) : blockstate.is(this);
//    }
//
//    public BlockState rotate(BlockState p_52790_, Rotation p_52791_) {
//        return p_52790_.setValue(FACING, p_52791_.rotate(p_52790_.getValue(FACING)));
//    }
//
//    public BlockState mirror(BlockState p_52787_, Mirror p_52788_) {
//        return p_52788_ == Mirror.NONE ? p_52787_ : p_52787_.rotate(p_52788_.getRotation(p_52787_.getValue(FACING)));
//    }
//
//    public long getSeed(BlockState p_52793_, BlockPos p_52794_) {
//        return Mth.getSeed(p_52794_.getX(), p_52794_.below(p_52793_.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), p_52794_.getZ());
//    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_52803_) {
        p_52803_.add(FACING, POWERED);
    }

//    protected static void preventCreativeDropFromBottomPart(Level p_52904_, BlockPos p_52905_, BlockState p_52906_, Player p_52907_) {
//        DoubleBlockHalf doubleblockhalf = p_52906_.getValue(HALF);
//        if (doubleblockhalf == DoubleBlockHalf.UPPER) {
//            BlockPos blockpos = p_52905_.below();
//            BlockState blockstate = p_52904_.getBlockState(blockpos);
//            if (blockstate.is(p_52906_.getBlock()) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER) {
//                BlockState blockstate1 = blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
//                p_52904_.setBlock(blockpos, blockstate1, 35);
//                p_52904_.levelEvent(p_52907_, 2001, blockpos, Block.getId(blockstate));
//            }
//        }
//
//    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return BlockEntityRegister.ARMOR_PROXY_BLOCK_ENTITY.get().create(p_153215_,p_153216_);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return (p_155253_, p_155254_, p_155255_, p_155256_) -> ((ArmorProxyBlockEntity)p_155256_).tick(p_155253_,p_155254_,p_155255_);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(p_60516_.getBlockEntity(p_60517_) instanceof ArmorProxyBlockEntity armorProxyBlockEntity){
            armorProxyBlockEntity.onRemove();
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }
}
