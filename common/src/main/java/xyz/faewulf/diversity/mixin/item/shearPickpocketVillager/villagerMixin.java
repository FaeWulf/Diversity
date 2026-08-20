package xyz.faewulf.diversity.mixin.item.shearPickpocketVillager;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.villager.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.CustomLootTables;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.Compare;
import xyz.faewulf.lib.util.entity.EntityUtils;

@Mixin(Villager.class)
public abstract class villagerMixin extends AbstractVillager implements ReputationEventHandler, VillagerDataHolder {

    //Todo: Tooltip overlay diaply chance when you about to pickpocket a villager
    //Todo: announce when player pickpocketed villager
    //Todo: named villager got ignore

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
    public abstract @NotNull VillagerData getVillagerData();

    @Shadow
    public abstract void setVillagerData(@NotNull VillagerData data);

    @Shadow
    public abstract void handleEntityEvent(byte id);

    @Shadow
    public abstract @NotNull Brain<Villager> getBrain();

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickInject(CallbackInfo ci) {
        if (!this.isClientSide() && ModConfigs.shear_can_pickpocket_villager && this.Diversity$pickpocket_cooldown > 0)
            this.Diversity$pickpocket_cooldown--;
    }

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void mobInteractInject(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!ModConfigs.shear_can_pickpocket_villager || this.level().isClientSide())
            return;

        ItemStack itemStack = player.getItemInHand(hand);

        //sneak
        //correct tool
        //no cooldown
        if (
                player.isShiftKeyDown()
                        && Compare.isHasTag(itemStack.getItem(), "diversity:pickpocket_tool")
                        && Diversity$pickpocket_cooldown <= 0
        ) {
            String job = this.getVillagerData().profession().value().name().getString();
            int jobLevel = this.getVillagerData().level();
            Vec3 blockPos = this.blockPosition().getCenter();
            //System.out.println(job + " " + jobLevel);

            //generate loot
            this.dropFromGiftLootTable(
                    (ServerLevel) level(),
                    diversity_Multiloader$getPickPocketLootTable(job),
                    (level, itemStack1) -> {
                        ItemEntity itementity = new ItemEntity(this.level(), blockPos.x, blockPos.y, blockPos.z, itemStack1);
                        itementity.setDefaultPickUpDelay();
                        level.addFreshEntity(itementity);
                    });

            //only stealing behind has only a small % to make villager noticed
            boolean sneaky = EntityUtils.isEntity2BehindEntity1(this, player);

            //villager can't see player (hiding between block, or using invisibility potion, no % to notice
            boolean stealth = !this.hasLineOfSight(player) || player.hasEffect(MobEffects.INVISIBILITY);

            //default success chance
            float successChance = 0.3f;

            if (sneaky) successChance = 0.7f;
            if (stealth) successChance = 1.0f;

            //caculate fail chance
            if (this.random.nextFloat() > successChance)
                this.hurtServer((ServerLevel) level(), this.damageSources().playerAttack(player), 0);

            //Below codes will try to lower villager's job level

            //if already level 1, (default level) then set cooldown
            if (jobLevel == 1)
                Diversity$pickpocket_cooldown = 20 * 60 * 5; //5 min

            //if level > 1
            //higher the level, higher chance to keep level (to skip this if case)
            if (jobLevel > 1 && this.random.nextFloat() > jobLevel * 0.15f) {

                //reset
                VillagerData old = this.getVillagerData();

                this.setVillagerData(new VillagerData(old.type(), BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(VillagerProfession.NONE), 1));

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
            player.getItemInHand(hand).hurtAndBreak(1, player, hand.asEquipmentSlot());

            //sound effect
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 1.0f, 1.0f);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0f, 1.0f);

            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
        }

        //still on cooldown
        if (
                player.isShiftKeyDown()
                        && Compare.isHasTag(itemStack.getItem(), "diversity:pickpocket_tool")
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

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveDataInject(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putInt("diversity:pickpocket_cooldown", Diversity$pickpocket_cooldown);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveDataInject(ValueInput valueInput, CallbackInfo ci) {
        this.Diversity$pickpocket_cooldown = valueInput.getInt("diversity:pickpocket_cooldown").orElse(0);
    }
}
