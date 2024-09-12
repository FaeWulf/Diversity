package xyz.faewulf.diversity.mixin.buildingBundle;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.inter.ICustomBundleItem;
import xyz.faewulf.diversity.util.CustomEnchant;
import xyz.faewulf.diversity.util.MissingMethod.ItemStackMethod;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@Mixin(value = BundleItem.class, priority = 1)
public abstract class BundleItemMixin extends Item implements ICustomBundleItem {

    @Shadow
    private static Stream<ItemStack> getContents(ItemStack $$0) {
        return null;
    }

    @Shadow
    private static int getWeight(ItemStack $$0) {
        return 0;
    }

    @Shadow
    private static int getContentWeight(ItemStack $$0) {
        return 0;
    }

    public BundleItemMixin(Properties settings) {
        super(settings);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    //bundle tooltip
    //@ModifyConstant(method = "appendTooltip", constant = @Constant(intValue = 64, ordinal = 1))
    @ModifyExpressionValue(method = "appendHoverText", at = @At(value = "CONSTANT", args = "intValue=64", ordinal = 0))
    private int appendTooltipInject(int value, @Local(argsOnly = true) ItemStack stack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(CustomEnchant.CAPACITY, stack);
        return Math.max(value, 64 + 64 * level);
    }

    //modify max allowed items add to bundle
    @ModifyExpressionValue(method = "add", at = @At(value = "CONSTANT", args = "intValue=64"))
    private static int modifyMaxBundleValue(int original, @Local(ordinal = 0, argsOnly = true) ItemStack bundle) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(CustomEnchant.CAPACITY, bundle);
        return 64 + level * 64;
    }

    //modify max allowed items add to bundle
    @ModifyExpressionValue(method = "overrideStackedOnOther", at = @At(value = "CONSTANT", args = "intValue=64"))
    private int modifyMaxBundleValue2(int original, @Local(ordinal = 0, argsOnly = true) ItemStack bundle) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(CustomEnchant.CAPACITY, bundle);
        return 64 + 64 * level;
    }

    @ModifyExpressionValue(method = "add", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z"))
    private static boolean modifyIfCase(boolean original, @Local(ordinal = 2) int k) {

        if (ModConfigs.bundle_enchantment)
            return false;
        else
            return original;

    }

    @Inject(method = "add", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/ListTag;add(ILnet/minecraft/nbt/Tag;)V", shift = At.Shift.AFTER))
    private static void modifyListTag_extra_sort(ItemStack $$0, ItemStack $$1, CallbackInfoReturnable<Integer> cir, @Local ListTag listTag) {
        Map<CompoundTag, Integer> map = new HashMap<>();

        //map all item (ItemStack Data, total count)
        for (Tag tag : listTag) {
            if (tag instanceof CompoundTag compoundTag) {

                AtomicBoolean isExist = new AtomicBoolean(false);

                map.forEach((compoundTag1, integer) -> {
                    if (ItemStack.isSameItemSameTags(ItemStack.of(compoundTag1), ItemStack.of(compoundTag))) {
                        integer += compoundTag.getByte("Count");

                        map.put(compoundTag1, integer);
                        isExist.set(true);
                    }
                });

                if (!isExist.get())
                    map.put(compoundTag, (int) compoundTag.getByte("Count"));

            }
        }

        listTag.clear();
        map.forEach((compoundTag, integer) -> {
            //System.out.println(compoundTag + ", Count: " + integer);
            ItemStack itemStack = ItemStack.of(compoundTag);

            //insert item into TagList, but max stack only
            while (integer > itemStack.getMaxStackSize()) {
                CompoundTag insertOne = compoundTag.copy();
                insertOne.putByte("Count", (byte) itemStack.getMaxStackSize());
                listTag.add(insertOne);

                integer -= itemStack.getMaxStackSize();
            }

            CompoundTag insertOne = compoundTag.copy();

            insertOne.putByte("Count", integer.byteValue());
            listTag.add(insertOne);

        });
    }




    /*
    //no needed
    @Inject(method = "overrideOtherStackedOnMe", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BundleItem;add(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)I"))
    private void onClickedInject(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
        //((ICustomBundleContentBuilder) this).setMaxSize(this.getMaxSize(player.level(), stack));
        ((ICustomBundleContentBuilder) this).setMaxSize(3);
    }

    @Inject(method = "overrideStackedOnOther", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BundleItem;add(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)I", ordinal = 0))
    private void onStackClickedInject(ItemStack stack, Slot slot, ClickAction clickType, Player player, CallbackInfoReturnable<Boolean> cir) {
        //((ICustomBundleContentBuilder) this).setMaxSize(this.getMaxSize(player.level(), stack));
        ((ICustomBundleContentBuilder) this).setMaxSize(3);
    }

    @Inject(method = "overrideStackedOnOther", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BundleItem;add(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)I"))
    private void onStackClickedInject2(ItemStack stack, Slot slot, ClickAction clickType, Player player, CallbackInfoReturnable<Boolean> cir) {
        //((ICustomBundleContentBuilder) this).setMaxSize(this.getMaxSize(player.level(), stack));
        ((ICustomBundleContentBuilder) this).setMaxSize(3);
    }
     */

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(Level world, Player user, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {

        if (!ModConfigs.bundle_place_mode)
            return;

        if (!world.isClientSide && user instanceof ServerPlayer) {
            if (getMode(user.getItemInHand(hand)) != 0) {
                syncBundleContents((ServerPlayer) user);
                cir.setReturnValue(InteractionResultHolder.fail(user.getItemInHand(hand)));
            }
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {

        if (!ModConfigs.bundle_place_mode)
            return InteractionResult.PASS;

        Level world = context.getLevel();
        Player player = context.getPlayer();

        if (world.isClientSide)
            return InteractionResult.PASS;

        ItemStack bundle = context.getItemInHand();

        if (getMode(bundle) == 0)
            return InteractionResult.PASS;

        if (world instanceof ServerLevel serverWorld && player instanceof ServerPlayer serverPlayerEntity) {
            if (bundle.getItem() instanceof BundleItem bundleItem && getContentWeight(bundle) > 0) {


                //convert into list
                Stream<ItemStack> itemStackStream = getContents(bundle);
                if (itemStackStream == null)
                    return InteractionResult.PASS;

                List<ItemStack> itemStackListFromBundle = new LinkedList<>(itemStackStream.toList());

                //get all index from bundle's content that is blockItem
                List<Integer> blockItemList = new ArrayList<>();

                //list of BlockItem in bundle
                for (int i = 0; i < itemStackListFromBundle.size(); i++) {
                    if (itemStackListFromBundle.get(i).getItem() instanceof BlockItem) {
                        blockItemList.add(i);
                    }
                }

                if (blockItemList.isEmpty())
                    return InteractionResult.PASS;

                //get indexOfItemInInventory based on mode value, if 2 then choose random, if 1 then choose first indexOfItemInInventory
                int chosenIndex = getMode(bundle) == 2 ? blockItemList.get(serverPlayerEntity.getRandom().nextInt(blockItemList.size())) : blockItemList.get(0);

                //if blockItem
                if (itemStackListFromBundle.get(chosenIndex).getItem() instanceof BlockItem blockItem) {
                    //try to place it
                    InteractionResult actionResult = blockItem.useOn(context);
                    //check result Consume or SUCCESS then block is placed then -1 that block from bundle
                    if (actionResult == InteractionResult.CONSUME || actionResult == InteractionResult.SUCCESS) {

                        removeItem(player, bundle, itemStackListFromBundle, chosenIndex);

                        BlockState blockState = blockItem.getBlock().defaultBlockState();
                        SoundEvent soundEvent = ((BlockItemInvoker) blockItem).invokeGetPlaceSound(blockState);

                        serverWorld.playSound(
                                null,
                                context.getClickedPos(),
                                soundEvent,
                                SoundSource.BLOCKS,
                                1f,
                                0.8F
                        );
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Unique
    private void removeItem(Player player, ItemStack bundleItem, List<ItemStack> bundleContentsComponent, int index) {
        if (bundleContentsComponent.isEmpty() || bundleContentsComponent.size() < index + 1)
            return;

        ItemStack itemStack = bundleContentsComponent.get(index);
        //List<ItemStack> itemStacks = new ArrayList<>(bundleContentsComponent.itemCopyStream().toList());

        if (itemStack.getCount() < 1)
            return;

        if (player.isCreative())
            return;

        if (itemStack.getCount() > 1) {
            bundleContentsComponent.get(index).shrink(1);
            //bundleItem.set(DataComponents.BUNDLE_CONTENTS, bundleContentsComponent);
            //save data to bundle
            saveItemListToBundle(bundleContentsComponent, bundleItem);
            return;
        }

        boolean isRefilled = false;

        if (isRefillable(player.level(), bundleItem))
            isRefilled = refill(player, bundleItem, bundleContentsComponent, index);

        if (!isRefilled) {
            bundleContentsComponent.remove(index);
            //System.out.println(itemStacks);
            player.displayClientMessage(Component.literal("Ran out of " + itemStack.getItem().getDescription().getString()), true);
        }

        //save data to bundle
        saveItemListToBundle(bundleContentsComponent, bundleItem);
    }

    @Unique
    private void saveItemListToBundle(List<ItemStack> itemStackList, ItemStack bundle) {

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

    @Unique
    private void syncBundleContents(ServerPlayer player) {
        // Sync the entire inventory to the client
        player.connection.send(new ClientboundContainerSetContentPacket(player.inventoryMenu.containerId, 0, player.inventoryMenu.getItems(), player.inventoryMenu.getCarried()));
    }

    @Unique
    private boolean refill(Player player, ItemStack bundle, List<ItemStack> itemStacks, int index) {

        Inventory playerInventory = player.getInventory();
        int indexOfItemInInventory = playerInventory.findSlotMatchingItem(itemStacks.get(index));

        if (indexOfItemInInventory == -1)
            return false;

        ItemStack itemStackWillPutInto = playerInventory.getItem(indexOfItemInInventory);
        //Bundle treats a slot = 64, sign max size 16 fit a slot = 64,
        //so have to getCount()*4 "(64 / 16 = 4)" to match the bundle size
        int stackMultiplier = 64 / itemStackWillPutInto.getMaxStackSize();
        int realStackSizeOfTheItemWillPutInto = stackMultiplier * itemStackWillPutInto.getCount();
        //int usedSlotInBundle = totalItemInBundle(itemStacks) - 1;

        //subtract 1 because there is 1 amount of the target item in the bundle we did not remove yet, if we remove
        //then target item will lose its position in the bundle -> no consistency
        //because of that, for the later we have to subtract 1 when increase amount of target item in the bundle
        //int usedSlotInBundle = Mth.mulAndTruncate(bundleContentsComponent.weight(), 64) - stackMultiplier;
        int usedSlotInBundle = getContentWeight(bundle);

        final int maxBundleSize = 64 + 64 * EnchantmentHelper.getItemEnchantmentLevel(CustomEnchant.CAPACITY, bundle);

        if (usedSlotInBundle >= maxBundleSize)
            return false;

        final int freeSlotInBundle = maxBundleSize - usedSlotInBundle;

        int numberOfItemWillPut = (int) Math.floor(Math.min(freeSlotInBundle, realStackSizeOfTheItemWillPutInto) * 1.0f / stackMultiplier);

        //decrestack
        //itemStackWillPutInto.consume(numberOfItemWillPut, player);
        ItemStackMethod.consume(itemStackWillPutInto, numberOfItemWillPut, player);

        //because there is already 1 item in the bundle
        itemStacks.get(index).grow(numberOfItemWillPut - 1);
        player.level().playSound(null, player.blockPosition(), SoundEvents.BEEHIVE_EXIT, SoundSource.PLAYERS, 0.5f, 0.7f);

        return true;
    }

    @Unique
    private boolean isRefillable(Level world, ItemStack itemStack) {
        if (world.isClientSide)
            return false;

        int value = EnchantmentHelper.getItemEnchantmentLevel(CustomEnchant.REFILL, itemStack);

        return value > 0;
    }

    @Override
    public int getMode(ItemStack itemStack) {

        //CustomData customData = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag nbt = itemStack.getOrCreateTag();

        if (nbt.contains("diversity:mode", 3))
            return nbt.getInt("diversity:mode");

        return 0;
    }

    @Override
    public void setMode(ItemStack itemStack, int mode) {

        String modeText = switch (mode) {
            case 1 -> "place first slot";
            case 2 -> "place random slot";
            default -> "normal";
        };

        CompoundTag nbt = itemStack.getOrCreateTag();

        // Set a custom string value in the NBT data with the specified key
        nbt.putInt("diversity:mode", mode);

        CompoundTag displayTag = itemStack.getOrCreateTagElement("display");

        // Create a new ListTag to hold the lore lines
        ListTag loreTag = new ListTag();

        // Add each line of lore to the ListTag as a StringTag
        loreTag.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal("Mode: " + modeText).withStyle(ChatFormatting.GRAY))));

        // Set the lore tag in the display tag
        displayTag.put("Lore", loreTag);
    }

}
