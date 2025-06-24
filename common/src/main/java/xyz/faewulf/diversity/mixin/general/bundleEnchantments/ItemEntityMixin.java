package xyz.faewulf.diversity.mixin.general.bundleEnchantments;

import com.llamalad7.mixinextras.sugar.Local;
import com.sun.tools.jconsole.JConsoleContext;
import com.sun.tools.jconsole.JConsolePlugin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.inter.ICustomBundleVacuum;
import xyz.faewulf.diversity.util.CustomEnchant;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements TraceableEntity {

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onItemPickup(Lnet/minecraft/world/entity/item/ItemEntity;)V"))
    private void playerTouchInject(Player entity, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemstack, @Local(ordinal = 0) int i) {

        if (itemstack.getMaxStackSize() <= 1 || itemstack.isEmpty())
            return;

        // Check if player holding any vacuum bundle
        // Then return the first one
        List<ItemStack> bundles = new ArrayList<>();
        ItemStack targetItemStack = null;
        for (ItemStack item : entity.getInventory().items) {

            if (item.isEmpty())
                continue;

            // Check if has vacuum bundle
            int value1 = EnchantmentHelper.getItemEnchantmentLevel(CustomEnchant.VACUUM, item);
            int value2 = EnchantmentHelper.getItemEnchantmentLevel(CustomEnchant.SELECTIVE_VACUUM, item);

            if (value1 + value2 > 0 && item.getItem() instanceof ICustomBundleVacuum) {

                if (value2 > 0)
                    bundles.add(0, item);
                else
                    bundles.add(item);

                continue;
            }

            // Get ItemStack inside inventory that match picked up item, size must be >=
            if (ItemStack.isSameItemSameTags(item, itemstack)) {
                targetItemStack = item;
            }
        }

        int insertAmount = itemstack.getCount();

        // For loop through all the bundles
        for (ItemStack bundle : bundles) {

            if (insertAmount <= 0)
                break;

            // lets handle vacuum
            if (bundle != null && targetItemStack != null) {

                // Check for selective vacuum
                boolean isSelective = false;

                int checkEnchant = EnchantmentHelper.getItemEnchantmentLevel(CustomEnchant.SELECTIVE_VACUUM, bundle);

                if (checkEnchant > 0)
                    isSelective = true;

                //Bundle treats a slot = 64, sign max size 16 fit a slot = 64,
                //so have to getCount()*4 "(64 / 16 = 4)" to match the bundle size
                int stackMultiplier = 64 / targetItemStack.getMaxStackSize();
                int realStackSizeOfTheItemWillPutInto = stackMultiplier * targetItemStack.getCount();

                int usedSlotInBundle = BundleItemInvoker.getContentWeightInvoked(bundle);

                final int maxBundleSize = 64 + 64 * EnchantmentHelper.getItemEnchantmentLevel(CustomEnchant.CAPACITY, bundle);

                if (usedSlotInBundle >= maxBundleSize)
                    continue;

                final int freeSlotInBundle = maxBundleSize - usedSlotInBundle;

                int numberOfItemWillPut = (int) Math.floor(Math.min(freeSlotInBundle, realStackSizeOfTheItemWillPutInto) * 1.0f / stackMultiplier);

                if (numberOfItemWillPut <= 0)
                    continue;

                // Put target itemStack into the bundle stacks

                //convert into list
                Stream<ItemStack> itemStackStream = BundleItemInvoker.getContentsInvoked(bundle);
                if (itemStackStream == null)
                    continue;

                List<ItemStack> itemStacksInBundle = new LinkedList<>(itemStackStream.toList());

                boolean hasInsert = false;
                for (ItemStack itemStackInBundle : itemStacksInBundle) {

                    if (ItemStack.isSameItemSameTags(itemStackInBundle, targetItemStack)) {

                        // Prevent 1.20.1 item disappear if stack > 64
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
                                ItemStack newItemStack = targetItemStack.copy();
                                newItemStack.setCount(maxSize);
                                itemStacksInBundle.add(newItemStack);
                            }

                            // last stack
                            if (numberOfItemWillPutIntoCurrentBundle > 0) {
                                ItemStack newItemStack = targetItemStack.copy();
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

                    int maxSize = targetItemStack.getMaxStackSize();
                    int numberOfItemWillPutIntoCurrentBundle = numberOfItemWillPut;

                    // Insert new stack if the target stack is full, until no count left
                    while (numberOfItemWillPutIntoCurrentBundle > maxSize) {
                        numberOfItemWillPutIntoCurrentBundle -= maxSize;
                        ItemStack newItemStack = targetItemStack.copy();
                        newItemStack.setCount(maxSize);
                        itemStacksInBundle.add(newItemStack);
                    }

                    // last stack
                    if (numberOfItemWillPutIntoCurrentBundle > 0) {
                        ItemStack newItemStack = targetItemStack.copy();
                        newItemStack.setCount(numberOfItemWillPutIntoCurrentBundle);
                        itemStacksInBundle.add(newItemStack);
                    }

                    hasInsert = true;
                }

                // If haven't insert anything then return
                if (!hasInsert)
                    continue;

                //decrease stack
                //System.out.println("has shrink " + numberOfItemWillPut);
                targetItemStack.shrink(numberOfItemWillPut);

                insertAmount -= numberOfItemWillPut;

                // update bundle data
                diversity$saveItemListToBundle(itemStacksInBundle, bundle);
            }
        }
    }

    @Unique
    private void diversity$saveItemListToBundle(List<ItemStack> itemStackList, ItemStack bundle) {

        CompoundTag bundleTag = bundle.getOrCreateTag();

        // Get the 'Items' ListTag, or create a new one if it doesn't exist
        ListTag itemsTagList = new ListTag();

        // Add each item from the list into the bundle's NBT data
        for (ItemStack item : itemStackList) {
            if (!item.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                item.save(itemTag); // Save the ItemStack data into the CompoundTag
                itemsTagList.add(itemTag); // Add the CompoundTag to the ListTag
            }
        }

        // Update the 'Items' tag in the bundle's NBT data
        bundleTag.put("Items", itemsTagList);

        // Apply the updated NBT data back to the bundle
        bundle.setTag(bundleTag);
    }
}
