package faewulf.diversity.mixin.mossBlockSniffer;

import faewulf.diversity.util.CustomLootTables;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SnifferEntity.class)
public abstract class SnifferEntityMixin extends AnimalEntity {

    protected SnifferEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract BlockPos getDigPos();

    @ModifyVariable(method = "dropSeeds", at = @At(value = "STORE"))
    private LootTable dropSeedInject(LootTable lootTable) {

        if (!ModConfigs.sniffer_get_spore)
            return lootTable;

        ServerWorld serverWorld = (ServerWorld) this.getWorld();
        if (!serverWorld.isClient) {
            BlockPos target = this.getDigPos().down();
            if (serverWorld.getBlockState(target).getBlock() == Blocks.MOSS_BLOCK) {
                return serverWorld.getServer().getReloadableRegistries().getLootTable(CustomLootTables.SNIFFER_MOSS_BLOCK);
            }
        }

        return lootTable;
    }

}
