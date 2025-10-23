package net.manaita_plus_neo.block.data;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Data {
    public static final IntegerProperty TYPES = IntegerProperty.create("types", 0, 8);
    public static final IntegerProperty HOOK = IntegerProperty.create("hook", 0, 8);
    public static final DirectionProperty WALL = DirectionProperty.create("wall", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final VoxelShape shapeS = Block.box(7, 12, 0, 9, 14, 2);
    public static final VoxelShape shapeN = Block.box(7, 12, 14, 9, 14, 16);
    public static final VoxelShape shapeE = Block.box(0, 12, 7, 2, 14, 9);
    public static final VoxelShape shapeW = Block.box(14, 12, 7, 16, 14, 9);

    public static final VoxelShape shapeNORTH = Block.box(3, 1, 0, 13, 15, 0.5F);
    public static final VoxelShape shapeSOUTH = Block.box(3, 1, 15.5F, 13, 15, 16F);
    public static final VoxelShape shapeEAST = Block.box(15.5F, 1, 3, 16F, 15, 13);
    public static final VoxelShape shapeWEST = Block.box(0F, 1, 3, 0.5F, 15, 13);
    public static final VoxelShape shapeUWE = Block.box(3, 15.5F, 1, 13, 16F, 15);
    public static final VoxelShape shapeUNS = Block.box(1, 15.5F, 3, 15, 16F, 13);
    public static final VoxelShape shapeDWE = Block.box(3, 0, 1, 13, 0.5F, 15);
    public static final VoxelShape shapeDNS = Block.box(1, 0, 3, 15, 0.5F, 13);
}