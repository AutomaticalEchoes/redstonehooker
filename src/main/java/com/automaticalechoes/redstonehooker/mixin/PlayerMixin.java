package com.automaticalechoes.redstonehooker.mixin;

import com.automaticalechoes.redstonehooker.api.hooker.MapSimpleContainer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(Player.class)
public class PlayerMixin implements InventoryCarrier {
    @Shadow
    @Final
    private Inventory inventory;
    @Nullable
    private MapSimpleContainer mapInventory = null;

    @Override
    public SimpleContainer getInventory() {
        if(mapInventory == null){
            this.mapInventory = new MapSimpleContainer(inventory.items.size(),inventory.items);
        }
        return mapInventory;
    }
}
