package xyz.faewulf.diversity.mixin.TridentSummonLightning;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.ModConfigs;

import java.util.Map;

@Mixin(ThrownTrident.class)
public abstract class TridentEntityMixin extends AbstractArrow {

    @Shadow
    protected abstract @NotNull ItemStack getPickupItem();

    @Shadow
    private ItemStack tridentItem;

    protected TridentEntityMixin(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tickInject(CallbackInfo ci) {

        if (!ModConfigs.trident_call_thunder)
            return;

        if (this.level().isClientSide)
            return;

        // if not overworld
        if (this.level().dimension() != Level.OVERWORLD)
            return;

        if (this.getOwner() instanceof ServerPlayer serverPlayerEntity && this.level() instanceof ServerLevel serverWorld) {

            //check game rule
            if (!serverWorld.getLevelData().getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE))
                return;

            ItemStack itemStack = this.getPickupItem();

            //must 1/2 of max durability
            if (itemStack.getDamageValue() > itemStack.getMaxDamage() * 1.0f / 2)
                return;

            //peak height
            if (this.getY() > 340 && !serverWorld.isThundering()) {

                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);

                //check enchantment
                if (!enchantments.isEmpty()) {

                    //check channeling
                    int value = enchantments.get(Enchantments.CHANNELING);
                    if (value > 0) {
                        //set durability
                        itemStack.setDamageValue(itemStack.getMaxDamage() - 1);

                        //remove channeling by suing builder to remove channeling enchantments from current itemStack component
                        enchantments.remove(Enchantments.CHANNELING);
                        EnchantmentHelper.setEnchantments(enchantments, itemStack);

                        //this.setPickupItemStack(itemStack);
                        this.tridentItem = itemStack;

                        //summon lightning
                        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(serverWorld);
                        if (lightningBolt != null) {
                            lightningBolt.setPosRaw(this.getX(), this.getY(), this.getZ());
                            serverWorld.addFreshEntity(lightningBolt);
                        }

                        //then set weather to storm

                        //weather duration random
                        int duration = ServerLevel.THUNDER_DURATION.sample(serverWorld.random);
                        serverWorld.setWeatherParameters(15, duration, true, true);
                    }

                }
            }
        }
    }
}
