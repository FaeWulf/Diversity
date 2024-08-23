package xyz.faewulf.diversity.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;

import java.util.ArrayList;
import java.util.List;

public class getNextBlockState {

    static private final List<Block> blackListBlock = new ArrayList<>() {{
        add(Blocks.COCOA);
        add(Blocks.END_PORTAL_FRAME);
        add(Blocks.ATTACHED_MELON_STEM);
        add(Blocks.ATTACHED_PUMPKIN_STEM);
        add(Blocks.PISTON_HEAD);
        add(Blocks.MOVING_PISTON);
        add(Blocks.VINE);
        add(Blocks.BLUE_BED);
        add(Blocks.BLACK_BED);
        add(Blocks.CYAN_BED);
        add(Blocks.BROWN_BED);
        add(Blocks.GRAY_BED);
        add(Blocks.GREEN_BED);
        add(Blocks.LIGHT_BLUE_BED);
        add(Blocks.LIGHT_GRAY_BED);
        add(Blocks.LIME_BED);
        add(Blocks.MAGENTA_BED);
        add(Blocks.ORANGE_BED);
        add(Blocks.PINK_BED);
        add(Blocks.PURPLE_BED);
        add(Blocks.RED_BED);
        add(Blocks.WHITE_BED);
        add(Blocks.YELLOW_BED);
    }};

    public static BlockState getNextState(BlockState state) {
        BlockState newState = null;

        //proof check for double chest
        if (state.getBlock() == Blocks.CHEST || state.getBlock() == Blocks.TRAPPED_CHEST) {
            if (state.getValue(ChestBlock.TYPE) != ChestType.SINGLE)
                return null;
        }

        //proof check for powered piston
        if (state.getBlock() == Blocks.PISTON || state.getBlock() == Blocks.STICKY_PISTON) {
            if (state.getValue(PistonBaseBlock.EXTENDED))
                return null;
        }


        //blacklist block returns null
        if (blackListBlock.contains(state.getBlock()))
            return null;


        //if slab type
        if (state.hasProperty(BlockStateProperties.SLAB_TYPE)) {
            SlabType slabType = switch (state.getValue(BlockStateProperties.SLAB_TYPE)) {
                case TOP -> SlabType.BOTTOM;
                case BOTTOM -> SlabType.TOP;
                default -> state.getValue(BlockStateProperties.SLAB_TYPE);
            };

            newState = state.setValue(BlockStateProperties.SLAB_TYPE, slabType);

        }
        //if ground sign/banner
        else if (state.hasProperty(BlockStateProperties.ROTATION_16)) {
            int rot = state.getValue(BlockStateProperties.ROTATION_16) + 1;

            if (rot == 16) {
                rot = 0;
            }

            newState = state.setValue(BlockStateProperties.ROTATION_16, rot);
        } else if (state.hasProperty(BlockStateProperties.AXIS)) {
            Direction.Axis axis = switch (state.getValue(BlockStateProperties.AXIS)) {
                case X -> Direction.Axis.Y;
                case Y -> Direction.Axis.Z;
                case Z -> Direction.Axis.X;
            };


            newState = state.setValue(BlockStateProperties.AXIS, axis);
        }
        //
        else if (state.hasProperty(BlockStateProperties.FACING)) {
            Direction direction = switch (state.getValue(BlockStateProperties.FACING)) {
                case UP -> Direction.NORTH;
                case NORTH -> Direction.EAST;
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.DOWN;
                case DOWN -> Direction.UP;
            };

            newState = state.setValue(BlockStateProperties.FACING, direction);
        }
        //hopper ofcourse
        else if (state.hasProperty(BlockStateProperties.FACING_HOPPER)) {
            Direction direction = switch (state.getValue(BlockStateProperties.FACING_HOPPER)) {
                case DOWN -> Direction.NORTH;
                case NORTH -> Direction.EAST;
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.DOWN;
                default -> state.getValue(BlockStateProperties.FACING_HOPPER);
            };

            newState = state.setValue(BlockStateProperties.FACING_HOPPER, direction);
        } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            //rotate clockwise
            Direction direction = switch (state.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
                case NORTH -> Direction.EAST;
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.NORTH;
                default -> state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            };

            newState = state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);

            //to turn stair block up down
            if (state.hasProperty(BlockStateProperties.HALF) && state.getValue(BlockStateProperties.HORIZONTAL_FACING) == Direction.NORTH) {
                Half blockHalf = state.getValue(BlockStateProperties.HALF);
                newState = newState.setValue(BlockStateProperties.HALF, blockHalf == Half.TOP ? Half.BOTTOM : Half.TOP);
            }
        }
        //for rail
        //first is straight rail
        else if (state.hasProperty(BlockStateProperties.RAIL_SHAPE_STRAIGHT)) {
            RailShape railShape = switch (state.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT)) {
                case NORTH_SOUTH -> RailShape.EAST_WEST;
                case EAST_WEST -> RailShape.NORTH_SOUTH;
                default -> state.getValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT);
            };

            newState = state.setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, railShape);
        }
        //1nd is curved rail
        else if (state.hasProperty(BlockStateProperties.RAIL_SHAPE)) {
            RailShape railShape = switch (state.getValue(BlockStateProperties.RAIL_SHAPE)) {
                case NORTH_SOUTH -> RailShape.EAST_WEST;
                case EAST_WEST -> RailShape.NORTH_EAST;
                case NORTH_EAST -> RailShape.SOUTH_EAST;
                case SOUTH_EAST -> RailShape.SOUTH_WEST;
                case SOUTH_WEST -> RailShape.NORTH_WEST;
                case NORTH_WEST -> RailShape.NORTH_SOUTH;
                default -> state.getValue(BlockStateProperties.RAIL_SHAPE);
            };

            newState = state.setValue(BlockStateProperties.RAIL_SHAPE, railShape);
        }
        //lantern
        else if (state.hasProperty(BlockStateProperties.HANGING)) {
            newState = state.setValue(BlockStateProperties.HANGING, !state.getValue(BlockStateProperties.HANGING));
        }

        return newState;
    }
}
