package com.automaticalechoes.redstonehooker.api.hooker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class Proxys {

    public static BlockEntity getVanillaBlockEntity(Level level , BlockPos pos){
       return ((ILevel)(Object)level).getVanillaBlockEntity(pos);
    }

}
