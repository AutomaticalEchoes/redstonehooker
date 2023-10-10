package com.automaticalechoes.redstonehooker.api.hooker;

import com.automaticalechoes.redstonehooker.api.addressItem.AddressItemInner;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class Proxys {

    public static BlockEntity getVanillaBlockEntity(Level level , BlockPos pos){
       return ((ILevel)(Object)level).getVanillaBlockEntity(pos);
    }

    public static int getRGBFromUUID(UUID uuid) {
//        String uuidString = uuid.toString().replace("-", "");
//        int red = variant & 0xFF;
//        int green = Integer.parseInt(uuidString.substring(8, 16), 16);
//        int blue = Integer.parseInt(uuidString.substring(16, 24), 16);
//        return (red << 16) + (green << 8) + blue;
        return uuid.hashCode();
    }

    public static int getRGBFromBlockPos(BlockPos pos){
        int red = Math.round(pos.getX() * 255);
        int green = Math.round(pos.getY() * 255);
        int blue = Math.round(pos.getZ() * 255);
        return (red << 16) + (green << 8) + blue;
    }



}
