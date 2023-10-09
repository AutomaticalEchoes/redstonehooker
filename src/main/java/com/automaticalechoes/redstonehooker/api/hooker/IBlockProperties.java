package com.automaticalechoes.redstonehooker.api.hooker;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class IBlockProperties {
    public static final IntegerProperty POWER_DOWN = IntegerProperty.create("power_down",0,15);
    public static final IntegerProperty POWER_UP = IntegerProperty.create("power_up",0,15);
    public static final IntegerProperty POWER_NORTH = IntegerProperty.create("power_north",0,15);
    public static final IntegerProperty POWER_SOUTH = IntegerProperty.create("power_south",0,15);
    public static final IntegerProperty POWER_WEST = IntegerProperty.create("power_west",0,15);
    public static final IntegerProperty POWER_EAST = IntegerProperty.create("power_east",0,15);
    public static final IntegerProperty[] ANISOTROPIC_SIGNALS = {POWER_DOWN,POWER_UP,POWER_NORTH,POWER_SOUTH,POWER_WEST,POWER_EAST};
}
