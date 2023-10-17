package com.automaticalechoes.redstonehooker.common.block;

import com.automaticalechoes.redstonehooker.RedstoneHooker;
import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import com.automaticalechoes.redstonehooker.api.hooker.Proxys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

public abstract class AddressInnerBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    protected AddressInnerBlock(Properties p_49224_) {
        super(p_49224_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(POWERED, Boolean.FALSE));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_52803_) {
        p_52803_.add(FACING, POWERED);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_51493_) {
        Direction direction = p_51493_.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(RedstoneHooker.CanInteractWithAddressInner(p_60506_) && p_60507_ == InteractionHand.MAIN_HAND
                && Proxys.getVanillaBlockEntity(p_60504_,p_60505_) instanceof AddressItemInner addressItemInner
                && addressItemInner.getAddressItem(p_60508_.getDirection().get3DDataValue()) != ItemStack.EMPTY) {
            addressItemInner.popAddressItem(p_60508_.getDirection().get3DDataValue());
        }
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(Proxys.getVanillaBlockEntity(p_60516_,p_60517_) instanceof AddressItemInner addressItemInner){
            addressItemInner.reset();
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }
}
