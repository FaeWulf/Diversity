package xyz.faewulf.diversity.mixin.general.bundleEnchantments;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.compat.MetalBundles.MetalBundleItemInvoker;
import xyz.faewulf.diversity.inter.ICustomBundleVacuum;
import xyz.faewulf.diversity.platform.Services;
import xyz.faewulf.diversity.util.Utils;
import xyz.faewulf.lib.util.EnchantHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements TraceableEntity {
    @Shadow
    private int pickupDelay;

    @Shadow
    @Nullable
    private UUID target;

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private static int diversity_Multiloader$getMaxSize(Level level, ItemStack itemStack) {

        // Check if this the item is from metal bundles
        // Then calculate max capacity from its weight fraction.
        int originalSize = 64;

        if (Services.PLATFORM.isModLoaded("metalbundles")) {
            Fraction a = MetalBundleItemInvoker.getActualWeightInvoker(itemStack);
            int usedSpace = Mth.mulAndTruncate(itemStack.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY).weight(), 64);
            originalSize = Utils.recoverCapacity(a, usedSpace);
        }

        // Vanilla bundle handle with capacity enchantment
        int value = EnchantHelper.getEnchantLevelFromItem(level, itemStack, Constants.MOD_ID, "capacity");
        return originalSize + value * 64;
    }

    //@Inject(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onItemPickup(Lnet/minecraft/world/entity/item/ItemEntity;)V"), cancellable = true)
    @Inject(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getCount()I"), cancellable = true)
    private void playerTouchInject(Player entity, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemstack) {

        // original if case to match vanilla pickup behavior
        if (this.pickupDelay != 0 || !(this.target == null || this.target.equals(entity.getUUID())))
            return;

        // if case for item not "real"
        if (itemstack.getMaxStackSize() <= 1 || itemstack.isEmpty())
            return;

        List<ItemStack> bundles = new ArrayList<>();
        int originalCount = itemstack.getCount();

        // Check if player holding any vacuum bundle
        // Then return the list of bundles (prior any selective vacuum to the beginning of the list)
        for (int index = 0; index < entity.getInventory().getContainerSize(); index++) {
            ItemStack item = entity.getInventory().getItem(index);

            if (item.isEmpty())
                continue;

            // Check if has a vacuum enchantment
            int value1 = EnchantHelper.getEnchantLevelFromItem(this.level(), item, Constants.MOD_ID, "vacuum");
            int value2 = EnchantHelper.getEnchantLevelFromItem(this.level(), item, Constants.MOD_ID, "selective_vacuum");

            if (value1 + value2 > 0 && item.getItem() instanceof ICustomBundleVacuum) {
                if (value2 > 0)
                    bundles.addFirst(item);
                else
                    bundles.add(item);
            }
        }

        // For loop through all the bundles
        for (ItemStack bundle : bundles) {

            if (ci.isCancelled())
                break;

            // lets handle vacuum
            // Bundle not null, and get that bundle content instance
            if (bundle != null && bundle.get(DataComponents.BUNDLE_CONTENTS) instanceof BundleContents bundleContentsComponent) {

                // Check for selective vacuum
                boolean isSelective = false;

                // Is selective enchantment
                if (EnchantHelper.hasEnchantment(this.level(), bundle, Constants.MOD_ID, "selective_vacuum"))
                    isSelective = true;

                // Bundle treats a slot = 64, but max size 16 fit a slot = 64,
                // so have to getCount()*4 "(64 / 16 = 4)" to match the bundle size
                int stackMultiplier = 64 / itemstack.getMaxStackSize();
                int realStackSizeOfTheItemWillPutInto = stackMultiplier * itemstack.getCount();

                int usedSlotInBundle = Mth.mulAndTruncate(bundleContentsComponent.weight(), 64);

                // Check if bundle already full
                final int maxBundleSize = diversity_Multiloader$getMaxSize(this.level(), bundle);

                if (usedSlotInBundle >= maxBundleSize)
                    continue;

                final int freeSlotInBundle = maxBundleSize - usedSlotInBundle;

                int numberOfItemWillPut = (int) Math.floor(Math.min(freeSlotInBundle, realStackSizeOfTheItemWillPutInto) * 1.0f / stackMultiplier);

                if (numberOfItemWillPut <= 0)
                    continue;

                // Put target itemStack into the bundle stacks
                List<ItemStack> itemStacksInBundle = new ArrayList<>(bundleContentsComponent.itemCopyStream().toList());

                // This loop is checking for existing same item already in bundle, then just increment it
                // Then flip the flag hasInsert to pass the next step
                boolean hasInsert = false;
                for (ItemStack itemStackInBundle : itemStacksInBundle) {

                    if (ItemStack.isSameItemSameComponents(itemStackInBundle, itemstack)) {

                        // Check if current stack already max then continue
                        if (itemStackInBundle.getCount() >= itemStackInBundle.getMaxStackSize())
                            continue;

                        // Prevent item stack grow higher than its maxStack
                        int countInBundle = itemStackInBundle.getCount();
                        int maxSize = itemStackInBundle.getMaxStackSize();

                        if (countInBundle + numberOfItemWillPut > maxSize) {
                            int slotLeft = maxSize - countInBundle;

                            int numberOfItemWillPutIntoCurrentBundle = numberOfItemWillPut;

                            numberOfItemWillPutIntoCurrentBundle -= slotLeft;

                            itemStackInBundle.grow(slotLeft);

                            // Insert new stack if the target stack is full, until no count left
                            while (numberOfItemWillPutIntoCurrentBundle > maxSize) {
                                numberOfItemWillPutIntoCurrentBundle -= maxSize;
                                ItemStack newItemStack = itemstack.copy();
                                newItemStack.setCount(maxSize);
                                itemStacksInBundle.add(newItemStack);
                            }

                            // last stack
                            if (numberOfItemWillPutIntoCurrentBundle > 0) {
                                ItemStack newItemStack = itemstack.copy();
                                newItemStack.setCount(numberOfItemWillPutIntoCurrentBundle);
                                itemStacksInBundle.add(newItemStack);
                            }

                        } else {
                            itemStackInBundle.grow(numberOfItemWillPut);
                        }
                        hasInsert = true;
                        break;
                    }
                }

                // if not exist then add instead
                // And not selective vacuum (selective vacuum only insert item that exists in the bundle
                if (!hasInsert && !isSelective) {

                    int maxSize = itemstack.getMaxStackSize();
                    int numberOfItemWillPutIntoCurrentBundle = numberOfItemWillPut;

                    // Insert new stack if the target stack is full, until no count left
                    while (numberOfItemWillPutIntoCurrentBundle > maxSize) {
                        numberOfItemWillPutIntoCurrentBundle -= maxSize;
                        ItemStack newItemStack = itemstack.copy();
                        newItemStack.setCount(maxSize);
                        itemStacksInBundle.add(newItemStack);
                    }

                    // last stack
                    if (numberOfItemWillPutIntoCurrentBundle > 0) {
                        ItemStack newItemStack = itemstack.copy();
                        newItemStack.setCount(numberOfItemWillPutIntoCurrentBundle);
                        itemStacksInBundle.add(newItemStack);
                    }

                    hasInsert = true;
                }

                // If haven't insert anything then return
                if (!hasInsert)
                    continue;

                //decrease stack
                itemstack.shrink(numberOfItemWillPut);

                // update bundle data
                bundleContentsComponent = new BundleContents(itemStacksInBundle);

                bundle.set(DataComponents.BUNDLE_CONTENTS, bundleContentsComponent);

                if (itemstack.isEmpty()) {
                    entity.take(this, originalCount);

                    entity.level().playSound(null, entity.blockPosition(), SoundEvents.DECORATED_POT_INSERT, SoundSource.PLAYERS, 0.3f, 0.8f + this.random.nextFloat() * 0.4f);
                    entity.awardStat(Stats.ITEM_PICKED_UP.get(itemstack.getItem()), originalCount);

                    this.discard();
                    ci.cancel();
                } else {
                    int pickedUpCount = originalCount - itemstack.getCount();
                    if (pickedUpCount > 0) {
                        entity.take(this, pickedUpCount);

                        entity.level().playSound(null, entity.blockPosition(), SoundEvents.DECORATED_POT_INSERT, SoundSource.PLAYERS, 0.3f, 0.8f + this.random.nextFloat() * 0.4f);
                        entity.awardStat(Stats.ITEM_PICKED_UP.get(itemstack.getItem()), pickedUpCount);
                    }
                }

            }
        }
    }
}
