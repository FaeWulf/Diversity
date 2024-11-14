package xyz.faewulf.diversity.mixin.shearPickpocketVillager;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.CustomLootTables;
import xyz.faewulf.diversity.util.compare;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.List;

@Mixin(Villager.class)
public abstract class villagerMixin extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {

    @Unique
    private int Diversity$pickpocket_cooldown = 0;

    public villagerMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private static ResourceKey<LootTable> diversity_Multiloader$getPickPocketLootTable(String name) {
        switch (name) {
            case "armorer" -> {
                return CustomLootTables.PICKPOCKET_ARMORER;
            }
            case "butcher" -> {
                return CustomLootTables.PICKPOCKET_BUTCHER;
            }
            case "cartographer" -> {
                return CustomLootTables.PICKPOCKET_CARTOGRAPHER;
            }
            case "cleric" -> {
                return CustomLootTables.PICKPOCKET_CLERIC;
            }
            case "farmer" -> {
                return CustomLootTables.PICKPOCKET_FARMER;
            }
            case "fisherman" -> {
                return CustomLootTables.PICKPOCKET_FISHERMAN;
            }
            case "fletcher" -> {
                return CustomLootTables.PICKPOCKET_FLETCHER;
            }
            case "leatherworker" -> {
                return CustomLootTables.PICKPOCKET_LEATHERWORKER;
            }
            case "librarian" -> {
                return CustomLootTables.PICKPOCKET_LIBRARIAN;
            }
            case "mason" -> {
                return CustomLootTables.PICKPOCKET_MASON;
            }
            case "nitwit" -> {
                return CustomLootTables.PICKPOCKET_NITWIT;
            }
            case "shepherd" -> {
                return CustomLootTables.PICKPOCKET_SHEPHERD;
            }
            case "toolsmith" -> {
                return CustomLootTables.PICKPOCKET_TOOLSMITH;
            }
            case "weaponsmith" -> {
                return CustomLootTables.PICKPOCKET_WEAPONSMITH;
            }
            default -> {
                return CustomLootTables.PICKPOCKET_NONE;
            }
        }
    }

    @Shadow
    public abstract VillagerData getVillagerData();

    @Shadow
    public abstract void setVillagerData(VillagerData data);

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickInject(CallbackInfo ci) {
        if (!this.isClientSide() && ModConfigs.shear_can_pickpocket_villager && this.Diversity$pickpocket_cooldown > 0)
            this.Diversity$pickpocket_cooldown--;
    }

    @Unique
    private static ResourceKey<LootTable> diversity_Multiloader$getPickPocketLootTable(String name) {
        switch (name) {
            case "armorer" -> {
                return CustomLootTables.PICKPOCKET_ARMORER;
            }
            case "butcher" -> {
                return CustomLootTables.PICKPOCKET_BUTCHER;
            }
            case "cartographer" -> {
                return CustomLootTables.PICKPOCKET_CARTOGRAPHER;
            }
            case "cleric" -> {
                return CustomLootTables.PICKPOCKET_CLERIC;
            }
            case "farmer" -> {
                return CustomLootTables.PICKPOCKET_FARMER;
            }
            case "fisherman" -> {
                return CustomLootTables.PICKPOCKET_FISHERMAN;
            }
            case "fletcher" -> {
                return CustomLootTables.PICKPOCKET_FLETCHER;
            }
            case "leatherworker" -> {
                return CustomLootTables.PICKPOCKET_LEATHERWORKER;
            }
            case "librarian" -> {
                return CustomLootTables.PICKPOCKET_LIBRARIAN;
            }
            case "mason" -> {
                return CustomLootTables.PICKPOCKET_MASON;
            }
            case "nitwit" -> {
                return CustomLootTables.PICKPOCKET_NITWIT;
            }
            case "shepherd" -> {
                return CustomLootTables.PICKPOCKET_SHEPHERD;
            }
            case "toolsmith" -> {
                return CustomLootTables.PICKPOCKET_TOOLSMITH;
            }
            case "weaponsmith" -> {
                return CustomLootTables.PICKPOCKET_WEAPONSMITH;
            }
            default -> {
                return CustomLootTables.PICKPOCKET_NONE;
            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveDataInject(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("diversity:pickpocket_cooldown", Diversity$pickpocket_cooldown);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveDataInject(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("diversity:pickpocket_cooldown", CompoundTag.TAG_INT)) {
            this.Diversity$pickpocket_cooldown = compound.getInt("diversity:pickpocket_cooldown");
        }
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void mobInteractInject(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!ModConfigs.shear_can_pickpocket_villager || this.level().isClientSide)
            return;

        ItemStack itemStack = player.getItemInHand(hand);

        //sneak
        //correct tool
        //no cooldown
        if (
                player.isShiftKeyDown()
                        && compare.isHasTag(itemStack.getItem(), "diversity:pickpocket_tool")
                        && Diversity$pickpocket_cooldown <= 0
                        && level() instanceof ServerLevel serverLevel
        ) {
            String job = this.getVillagerData().getProfession().name();
            int jobLevel = this.getVillagerData().getLevel();
            Vec3 blockPos = this.blockPosition().getCenter();
            //System.out.println(job + " " + jobLevel);

            //generate loot
            LootTable loottable = serverLevel.getServer().reloadableRegistries().getLootTable(diversity_Multiloader$getPickPocketLootTable(job));
            LootParams lootparams = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .create(LootContextParamSets.GIFT);

            List<ItemStack> itemStackList = loottable.getRandomItems(lootparams);

            for (ItemStack stack : itemStackList) {
                ItemEntity itementity = new ItemEntity(this.level(), blockPos.x, blockPos.y, blockPos.z, stack);
                itementity.setDefaultPickUpDelay();
                serverLevel.addFreshEntity(itementity);
            }

            //only stealing behind has only a small % to make villager noticed
            boolean sneaky = compare.isEntity2BehindEntity1(this, player);

            //villager can't see player (hiding between block, or using invisibility potion, no % to notice
            boolean stealth = !this.hasLineOfSight(player) || player.hasEffect(MobEffects.INVISIBILITY);

            //default success chance
            float successChance = 0.3f;

            if (sneaky) successChance = 0.7f;
            if (stealth) successChance = 1.0f;

            //caculate fail chance
            if (this.random.nextFloat() > successChance)
                this.hurt(this.damageSources().playerAttack(player), 0);

            //Below codes will try to lower villager's job level

            //if already level 1, (default level) then set cooldown
            if (jobLevel == 1)
                Diversity$pickpocket_cooldown = 20 * 60 * 5; //5 min

            //if level > 1
            //higher the level, higher chance to keep level (to skip this if case)
            if (jobLevel > 1 && this.random.nextFloat() > jobLevel * 0.15f) {

                //reset
                VillagerData old = this.getVillagerData();

                this.setVillagerData(new VillagerData(old.getType(), VillagerProfession.NONE, 1));

                //add particle effect
                ((ServerLevel) this.level()).sendParticles(
                        ParticleTypes.EXPLOSION_EMITTER,
                        this.getX(), this.getY(), this.getZ(),
                        1,
                        0f, 0f, 0f,
                        0.5f);

                Diversity$pickpocket_cooldown = 20 * 60 * 5; //5 min
            }

            //damage shear
            player.getItemInHand(hand).hurtAndBreak(1, player, getSlotForHand(hand));

            //sound effect
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 1.0f, 1.0f);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0f, 1.0f);

            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }

        //still on cooldown
        if (
                player.isShiftKeyDown()
                        && compare.isHasTag(itemStack.getItem(), "diversity:pickpocket_tool")
                        && Diversity$pickpocket_cooldown > 0
        ) {

            ((ServerLevel) this.level()).sendParticles(
                    ParticleTypes.SMOKE,
                    this.getX(), this.getY(), this.getZ(),
                    20,
                    0.2f, 0.2f, 0.2f,
                    0.02f);

            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }
    }
}
