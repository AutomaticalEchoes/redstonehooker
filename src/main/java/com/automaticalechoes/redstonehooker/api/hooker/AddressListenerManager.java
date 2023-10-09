package com.automaticalechoes.redstonehooker.api.hooker;

import com.automaticalechoes.redstonehooker.common.blockentity.AnisotropicSignalBlockEntity;
import net.minecraft.core.BlockPos;
import org.antlr.v4.runtime.misc.MultiMap;

import java.util.List;

public class AddressListenerManager {
    private static final MultiMap<BlockPos, AnisotropicSignalBlockEntity> SIGNAL_LISTENER = new MultiMap<>();

    public static List<AnisotropicSignalBlockEntity> getPosListener(BlockPos pos){
        return SIGNAL_LISTENER.get(pos);
    }

    public static boolean posContainersListener(BlockPos pos){
        return SIGNAL_LISTENER.containsKey(pos);
    }

    public static void removePosListener(BlockPos pos ,AnisotropicSignalBlockEntity anisotropicSignalBlockEntity){
        if(SIGNAL_LISTENER.containsKey(pos) && SIGNAL_LISTENER.get(pos).contains(anisotropicSignalBlockEntity))
            SIGNAL_LISTENER.remove(pos,anisotropicSignalBlockEntity);
    }

    public static void addPosListener(BlockPos pos,AnisotropicSignalBlockEntity anisotropicSignalBlockEntity){
        if(SIGNAL_LISTENER.containsKey(pos) && SIGNAL_LISTENER.get(pos).contains(anisotropicSignalBlockEntity)) return;
        SIGNAL_LISTENER.map(pos,anisotropicSignalBlockEntity);
    }
}
