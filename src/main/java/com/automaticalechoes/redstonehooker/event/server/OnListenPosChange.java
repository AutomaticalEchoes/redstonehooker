package com.automaticalechoes.redstonehooker.event.server;

import com.automaticalechoes.redstonehooker.api.hooker.AddressListenerManager;
import com.automaticalechoes.redstonehooker.common.blockentity.AnisotropicSignalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class OnListenPosChange extends Event {
    private final BlockPos pos;
    private final List<AnisotropicSignalBlockEntity> posListeners;

    public OnListenPosChange(BlockPos pos, List<AnisotropicSignalBlockEntity> posListeners) {
        this.pos = pos;
        this.posListeners = posListeners;
    }

    public OnListenPosChange(BlockPos pos) {
        this.pos = pos;
        this.posListeners = AddressListenerManager.getPosListener(pos);
    }

    public BlockPos getPos() {
        return pos;
    }

    public List<AnisotropicSignalBlockEntity> getPosListeners() {
        return posListeners;
    }
}
