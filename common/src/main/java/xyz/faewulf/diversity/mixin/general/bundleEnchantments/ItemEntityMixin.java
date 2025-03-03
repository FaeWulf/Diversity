package xyz.faewulf.diversity.mixin.general.bundleEnchantments;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.inter.ICustomBundleVacuum;
import xyz.faewulf.lib.util.Converter;
import xyz.faewulf.lib.util.EnchantHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements TraceableEntity {
    @Shadow
    public abstract boolean ignoreExplosion(Explosion p_364217_);

    @Shadow
    @Nullable
    private UUID target;

    @Shadow
    private int pickupDelay;

    @Shadow
    public abstract boolean dampensVibrations();

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onItemPickup(Lnet/minecraft/world/entity/item/ItemEntity;)V"), cancellable = true)
    private void playerTouchInject(Player entity, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemstack, @Local(ordinal = 0) int i) {

        // Check if player holding any vacuum bundle
        // Then return the first one
        List<ItemStack> bundles = new ArrayList<>();
        ItemStack targetItemStack = null;
        for (ItemStack item : entity.getInventory().items) {

            if (item.isEmpty())
                continue;

            // Check if has vacuum bundle
            int value1 = EnchantHelper.getEnchantLevelFromItem(this.level(), item, Constants.MOD_ID, "vacuum");
            int value2 = EnchantHelper.getEnchantLevelFromItem(this.level(), item, Constants.MOD_ID, "selective_vacuum");

            if (value1 + value2 > 0 && item.getItem() instanceof ICustomBundleVacuum) {

                if (value2 > 0)
                    bundles.addFirst(item);
                else
                    bundles.add(item);

                continue;
            }

            // Get ItemStack inside inventory that match picked up item, size must be >=
            if (item.getItem() == itemstack.getItem() && item.getCount() >= i && targetItemStack == null) {
                targetItemStack = item;
            }
        }

        int insertAmount = i;

        // For loop through all the bundles
        for (ItemStack bundle : bundles) {

            if (insertAmount <= 0)
                break;

            // lets handle vacuum
            if (bundle != null && targetItemStack != null && bundle.get(DataComponents.BUNDLE_CONTENTS) instanceof BundleContents bundleContentsComponent) {

                // Check for selective vacuum
                boolean isSelective = false;

                ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(bundle);

                if (EnchantHelper.hasEnchantment(this.level(), bundle, Constants.MOD_ID, "selective_vacuum"))
                    isSelective = true;

                ItemStack itemStackWillPutInto = targetItemStack;
                //Bundle treats a slot = 64, sign max size 16 fit a slot = 64,
                //so have to getCount()*4 "(64 / 16 = 4)" to match the bundle size
                int stackMultiplier = 64 / itemStackWillPutInto.getMaxStackSize();
                int realStackSizeOfTheItemWillPutInto = stackMultiplier * itemStackWillPutInto.getCount();

                int usedSlotInBundle = Mth.mulAndTruncate(bundleContentsComponent.weight(), 64);

                final int maxBundleSize = diversity_Multiloader$getMaxSize(this.level(), bundle);

                if (usedSlotInBundle >= maxBundleSize)
                    continue;

                final int freeSlotInBundle = maxBundleSize - usedSlotInBundle;

                int numberOfItemWillPut = (int) Math.floor(Math.min(freeSlotInBundle, realStackSizeOfTheItemWillPutInto) * 1.0f / stackMultiplier);

                if (numberOfItemWillPut <= 0)
                    continue;

                // Put target itemStack into the bundle stacks
                List<ItemStack> itemStacksInBundle = new ArrayList<>(bundleContentsComponent.itemCopyStream().toList());

                boolean hasInsert = false;
                for (ItemStack itemStackInBundle : itemStacksInBundle) {
                    if (itemStackInBundle.getItem() == itemStackWillPutInto.getItem()) {
                        itemStackInBundle.grow(numberOfItemWillPut);
                        hasInsert = true;
                        break;
                    }
                }

                // if not exist then add instead
                // And not selective vacuum (selective vacuum only insert item that exists in the bundle
                if (!hasInsert && !isSelective) {
                    ItemStack newItemStack = itemStackWillPutInto.copy();
                    newItemStack.setCount(numberOfItemWillPut);
                    itemStacksInBundle.add(newItemStack);

                    hasInsert = true;
                }

                // If haven't insert anything then return
                if (!hasInsert)
                    continue;

                //decrease stack
                itemStackWillPutInto.shrink(numberOfItemWillPut);

                insertAmount -= numberOfItemWillPut;

                // update bundle data
                bundleContentsComponent = new BundleContents(itemStacksInBundle);

                bundle.set(DataComponents.BUNDLE_CONTENTS, bundleContentsComponent);

            }
        }
    }

    @Unique
    private static int diversity_Multiloader$getMaxSize(Level level, ItemStack itemStack) {
        int value = EnchantHelper.getEnchantLevelFromItem(level, itemStack, Constants.MOD_ID, "capacity");
        return 64 + value * 64;
    }
}
