package faewulf.diversity.util;

import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.ChestType;
import net.minecraft.block.enums.RailShape;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

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
            if (state.get(ChestBlock.CHEST_TYPE) != ChestType.SINGLE)
                return null;
        }

        //proof check for powered piston
        if (state.getBlock() == Blocks.PISTON || state.getBlock() == Blocks.STICKY_PISTON) {
            if (state.get(PistonBlock.EXTENDED))
                return null;
        }


        //blacklist block returns null
        if (blackListBlock.contains(state.getBlock()))
            return null;


        //if slab type
        if (state.contains(Properties.SLAB_TYPE)) {
            SlabType slabType = switch (state.get(Properties.SLAB_TYPE)) {
                case TOP -> SlabType.BOTTOM;
                case BOTTOM -> SlabType.TOP;
                default -> state.get(Properties.SLAB_TYPE);
            };

            newState = state.with(Properties.SLAB_TYPE, slabType);

        }
        //if ground sign/banner
        else if (state.contains(Properties.ROTATION)) {
            int rot = state.get(Properties.ROTATION) + 1;

            if (rot == 16) {
                rot = 0;
            }

            newState = state.with(Properties.ROTATION, rot);
        } else if (state.contains(Properties.AXIS)) {
            Direction.Axis axis = switch (state.get(Properties.AXIS)) {
                case X -> Direction.Axis.Y;
                case Y -> Direction.Axis.Z;
                case Z -> Direction.Axis.X;
            };


            newState = state.with(Properties.AXIS, axis);
        }
        //
        else if (state.contains(Properties.FACING)) {
            Direction direction = switch (state.get(Properties.FACING)) {
                case UP -> Direction.NORTH;
                case NORTH -> Direction.EAST;
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.DOWN;
                case DOWN -> Direction.UP;
            };

            newState = state.with(Properties.FACING, direction);
        }
        //hopper ofcourse
        else if (state.contains(Properties.HOPPER_FACING)) {
            Direction direction = switch (state.get(Properties.HOPPER_FACING)) {
                case DOWN -> Direction.NORTH;
                case NORTH -> Direction.EAST;
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.DOWN;
                default -> state.get(Properties.HOPPER_FACING);
            };

            newState = state.with(Properties.HOPPER_FACING, direction);
        } else if (state.contains(Properties.HORIZONTAL_FACING)) {
            //rotate clockwise
            Direction direction = switch (state.get(Properties.HORIZONTAL_FACING)) {
                case NORTH -> Direction.EAST;
                case EAST -> Direction.SOUTH;
                case SOUTH -> Direction.WEST;
                case WEST -> Direction.NORTH;
                default -> state.get(Properties.HORIZONTAL_FACING);
            };

            newState = state.with(Properties.HORIZONTAL_FACING, direction);

            //to turn stair block up down
            if (state.contains(Properties.BLOCK_HALF) && state.get(Properties.HORIZONTAL_FACING) == Direction.NORTH) {
                BlockHalf blockHalf = state.get(Properties.BLOCK_HALF);
                newState = newState.with(Properties.BLOCK_HALF, blockHalf == BlockHalf.TOP ? BlockHalf.BOTTOM : BlockHalf.TOP);
            }
        }
        //for rail
        //first is straight rail
        else if (state.contains(Properties.STRAIGHT_RAIL_SHAPE)) {
            RailShape railShape = switch (state.get(Properties.STRAIGHT_RAIL_SHAPE)) {
                case NORTH_SOUTH -> RailShape.EAST_WEST;
                case EAST_WEST -> RailShape.NORTH_SOUTH;
                default -> state.get(Properties.STRAIGHT_RAIL_SHAPE);
            };

            newState = state.with(Properties.STRAIGHT_RAIL_SHAPE, railShape);
        }
        //1nd is curved rail
        else if (state.contains(Properties.RAIL_SHAPE)) {
            RailShape railShape = switch (state.get(Properties.RAIL_SHAPE)) {
                case NORTH_SOUTH -> RailShape.EAST_WEST;
                case EAST_WEST -> RailShape.NORTH_EAST;
                case NORTH_EAST -> RailShape.SOUTH_EAST;
                case SOUTH_EAST -> RailShape.SOUTH_WEST;
                case SOUTH_WEST -> RailShape.NORTH_WEST;
                case NORTH_WEST -> RailShape.NORTH_SOUTH;
                default -> state.get(Properties.RAIL_SHAPE);
            };

            newState = state.with(Properties.RAIL_SHAPE, railShape);
        }
        //lantern
        else if (state.contains(Properties.HANGING)) {
            newState = state.with(Properties.HANGING, !state.get(Properties.HANGING));
        }

        return newState;
    }
}
