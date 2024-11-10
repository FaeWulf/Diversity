package xyz.faewulf.diversity.inter;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public interface ICustomBonemealable {
    default boolean Diversity$isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    boolean Diversity$isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state);

    void Diversity$performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state);
}
