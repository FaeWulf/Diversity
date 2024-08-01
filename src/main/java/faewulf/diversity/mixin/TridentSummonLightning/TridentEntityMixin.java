package faewulf.diversity.mixin.TridentSummonLightning;

import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.converter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void tickInject(CallbackInfo ci) {

        if (!ModConfigs.trident_call_thunder)
            return;

        if (this.getWorld().isClient)
            return;

        // if not overworld
        if (this.getWorld().getRegistryKey() != World.OVERWORLD)
            return;

        if (this.getOwner() instanceof ServerPlayerEntity serverPlayerEntity && this.getWorld() instanceof ServerWorld serverWorld) {

            //check game rule
            if (!serverWorld.getLevelProperties().getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE))
                return;

            ItemStack itemStack = this.getItemStack();

            //must 1/2 of max durability
            if (itemStack.getDamage() > itemStack.getMaxDamage() * 1.0f / 2)
                return;

            //peak height
            if (this.getY() > 340 && !serverWorld.isThundering()) {
                ItemEnchantmentsComponent itemEnchantmentsComponent = itemStack.get(DataComponentTypes.ENCHANTMENTS);


                //check enchantment
                if (itemEnchantmentsComponent != null && !itemEnchantmentsComponent.isEmpty()) {

                    //check channeling
                    RegistryEntry<Enchantment> channeling = converter.getEnchant(this.getWorld(), Identifier.DEFAULT_NAMESPACE, "channeling");
                    int value = itemEnchantmentsComponent.getLevel(channeling);

                    if (value > 0) {
                        //set durability
                        itemStack.setDamage(itemStack.getMaxDamage() - 1);

                        //remove channeling by suing builder to remove channeling enchantments from current itemStack component
                        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(itemEnchantmentsComponent);
                        builder.set(channeling, 0);
                        itemStack.set(DataComponentTypes.ENCHANTMENTS, builder.build());
                        this.setStack(itemStack);

                        //summon lightning
                        LightningEntity lightningBolt = EntityType.LIGHTNING_BOLT.create(serverWorld);
                        if (lightningBolt != null) {
                            lightningBolt.setPos(this.getX(), this.getY(), this.getZ());
                            serverWorld.spawnEntity(lightningBolt);
                        }

                        //then set weather to storm

                        //weather duration random
                        int duration = ServerWorld.THUNDER_WEATHER_DURATION_PROVIDER.get(serverWorld.random);
                        serverWorld.setWeather(15, duration, true, true);
                    }

                }
            }
        }
    }
}
