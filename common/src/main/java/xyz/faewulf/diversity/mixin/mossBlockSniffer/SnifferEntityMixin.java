package xyz.faewulf.diversity.mixin.mossBlockSniffer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.faewulf.diversity.util.CustomLootTables;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(Sniffer.class)
public abstract class SnifferEntityMixin extends Animal {

    protected SnifferEntityMixin(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract BlockPos getHeadBlock();

    @ModifyVariable(method = "dropSeed", at = @At(value = "STORE"))
    private LootTable dropSeedInject(LootTable lootTable) {

        if (!ModConfigs.sniffer_get_spore)
            return lootTable;

        ServerLevel serverWorld = (ServerLevel) this.level();
        if (!serverWorld.isClientSide) {
            BlockPos target = this.getHeadBlock().below();
            if (serverWorld.getBlockState(target).getBlock() == Blocks.MOSS_BLOCK) {
                return serverWorld.getServer().reloadableRegistries().getLootTable(CustomLootTables.SNIFFER_MOSS_BLOCK);
            }
        }

        return lootTable;
    }

}
