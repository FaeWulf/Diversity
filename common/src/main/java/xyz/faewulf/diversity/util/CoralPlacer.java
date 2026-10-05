package xyz.faewulf.diversity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.stream.Stream;

public final class CoralPlacer {

    private CoralPlacer() {
    }

    public enum Shape { TREE, CLAW, MUSHROOM }

    public static boolean place(LevelAccessor level, RandomSource random, BlockPos origin, BlockState state, Shape shape) {
        return switch (shape) {
            case TREE -> placeTree(level, random, origin, state);
            case CLAW -> placeClaw(level, random, origin, state);
            case MUSHROOM -> placeMushroom(level, random, origin, state);
        };
    }

    private static boolean placeTree(LevelAccessor level, RandomSource random, BlockPos origin, BlockState state) {
        BlockPos.MutableBlockPos mutPos = origin.mutable();
        int trunckHeight = random.nextInt(3) + 1;

        for (int i = 0; i < trunckHeight; i++) {
            if (!placeCoralBlock(level, random, mutPos, state)) {
                return true;
            }

            mutPos.move(Direction.UP);
        }

        BlockPos trunckTopPos = mutPos.immutable();
        int nBranches = random.nextInt(3) + 2;
        List<Direction> directions = Direction.Plane.HORIZONTAL.shuffledCopy(random);

        for (Direction branchDirection : directions.subList(0, nBranches)) {
            mutPos.set(trunckTopPos);
            mutPos.move(branchDirection);
            int branchHeight = random.nextInt(5) + 2;
            int segmentLength = 0;

            for (int j = 0; j < branchHeight && placeCoralBlock(level, random, mutPos, state); j++) {
                segmentLength++;
                mutPos.move(Direction.UP);
                if (j == 0 || segmentLength >= 2 && random.nextFloat() < 0.25F) {
                    mutPos.move(branchDirection);
                    segmentLength = 0;
                }
            }
        }

        return true;
    }

    private static boolean placeClaw(LevelAccessor level, RandomSource random, BlockPos origin, BlockState state) {
        if (!placeCoralBlock(level, random, origin, state)) {
            return false;
        }

        Direction clawDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        int nBranches = random.nextInt(2) + 2;
        List<Direction> possibleDirections = Util.toShuffledList(
            Stream.of(clawDirection, clawDirection.getClockWise(), clawDirection.getCounterClockWise()), random
        );

        for (Direction branchDirection : possibleDirections.subList(0, nBranches)) {
            BlockPos.MutableBlockPos mutPos = origin.mutable();
            int sidewayLength = random.nextInt(2) + 1;
            mutPos.move(branchDirection);
            int inwayLenth;
            Direction segmentDirection;
            if (branchDirection == clawDirection) {
                segmentDirection = clawDirection;
                inwayLenth = random.nextInt(3) + 2;
            } else {
                mutPos.move(Direction.UP);
                Direction[] segmentPossibleDirections = new Direction[]{branchDirection, Direction.UP};
                segmentDirection = Util.getRandom(segmentPossibleDirections, random);
                inwayLenth = random.nextInt(3) + 3;
            }

            for (int i = 0; i < sidewayLength && placeCoralBlock(level, random, mutPos, state); i++) {
                mutPos.move(segmentDirection);
            }

            mutPos.move(segmentDirection.getOpposite());
            mutPos.move(Direction.UP);

            for (int i = 0; i < inwayLenth; i++) {
                mutPos.move(clawDirection);
                if (!placeCoralBlock(level, random, mutPos, state)) {
                    break;
                }

                if (random.nextFloat() < 0.25F) {
                    mutPos.move(Direction.UP);
                }
            }
        }

        return true;
    }

    private static boolean placeMushroom(LevelAccessor level, RandomSource random, BlockPos origin, BlockState state) {
        int height = random.nextInt(3) + 3;
        int width = random.nextInt(3) + 3;
        int length = random.nextInt(3) + 3;
        int sinkValue = random.nextInt(3) + 1;
        BlockPos.MutableBlockPos mutPos = origin.mutable();

        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                for (int z = 0; z <= length; z++) {
                    mutPos.set(x + origin.getX(), y + origin.getY(), z + origin.getZ());
                    mutPos.move(Direction.DOWN, sinkValue);
                    if ((x != 0 && x != width || y != 0 && y != height)
                        && (z != 0 && z != length || y != 0 && y != height)
                        && (x != 0 && x != width || z != 0 && z != length)
                        && (x == 0 || x == width || y == 0 || y == height || z == 0 || z == length)
                        && !(random.nextFloat() < 0.1F)
                        && !placeCoralBlock(level, random, mutPos, state)) {
                    }
                }
            }
        }

        return true;
    }

    public static boolean placeCoralBlock(LevelAccessor level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();
        BlockState targetBlockState = level.getBlockState(pos);
        if ((targetBlockState.is(Blocks.WATER) || targetBlockState.is(BlockTags.CORALS)) && level.getBlockState(above).is(Blocks.WATER)) {
            level.setBlock(pos, state, 3);
            if (random.nextFloat() < 0.25F) {
                BuiltInRegistries.BLOCK
                    .getRandomElementOf(BlockTags.CORALS, random)
                    .map(Holder::value)
                    .ifPresent(block -> level.setBlock(above, block.defaultBlockState(), 2));
            } else if (random.nextFloat() < 0.05F) {
                level.setBlock(above, Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, random.nextInt(4) + 1), 2);
            }

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (random.nextFloat() < 0.2F) {
                    BlockPos relativePos = pos.relative(direction);
                    if (level.getBlockState(relativePos).is(Blocks.WATER)) {
                        BuiltInRegistries.BLOCK.getRandomElementOf(BlockTags.WALL_CORALS, random).map(Holder::value).ifPresent(coral -> {
                            BlockState coralFanState = coral.defaultBlockState();
                            if (coralFanState.hasProperty(BaseCoralWallFanBlock.FACING)) {
                                coralFanState = coralFanState.setValue(BaseCoralWallFanBlock.FACING, direction);
                            }

                            level.setBlock(relativePos, coralFanState, 2);
                        });
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }
}