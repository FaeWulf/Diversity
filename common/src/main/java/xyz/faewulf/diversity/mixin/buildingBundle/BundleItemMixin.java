package xyz.faewulf.diversity.mixin.buildingBundle;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.inter.ICustomBundleContentBuilder;
import xyz.faewulf.diversity.inter.ICustomBundleItem;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.diversity.util.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(value = BundleItem.class, priority = 1)
public abstract class BundleItemMixin extends Item implements ICustomBundleItem {
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

    //@ModifyConstant(method = "appendTooltip", constant = @Constant(intValue = 64, ordinal = 1))
    @ModifyExpressionValue(method = "appendHoverText", at = @At(value = "CONSTANT", args = "intValue=64", ordinal = 1))
    private int appendTooltipInject(int value, @Local(argsOnly = true) ItemStack stack) {
        ItemEnchantments t = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        AtomicInteger level = new AtomicInteger();
        t.keySet().forEach(enchantmentRegistryEntry -> {
            ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(stack);
            if (enchantmentRegistryEntry.is(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "capacity"))) {
                level.set(itemEnchantmentsComponent.getLevel(enchantmentRegistryEntry));
            }
        });

        return Math.max(value, 64 + 64 * level.get());
    }

    @Inject(method = "overrideOtherStackedOnMe", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/BundleContents$Mutable;tryInsert(Lnet/minecraft/world/item/ItemStack;)I"))
    private void onClickedInject(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference, CallbackInfoReturnable<Boolean> cir, @Local BundleContents.Mutable builder) {
        ((ICustomBundleContentBuilder) builder).setMaxSize(this.getMaxSize(player.level(), stack));
    }

    @Inject(method = "overrideStackedOnOther", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/BundleContents$Mutable;tryInsert(Lnet/minecraft/world/item/ItemStack;)I"))
    private void onStackClickedInject(ItemStack stack, Slot slot, ClickAction clickType, Player player, CallbackInfoReturnable<Boolean> cir, @Local BundleContents.Mutable builder) {
        ((ICustomBundleContentBuilder) builder).setMaxSize(this.getMaxSize(player.level(), stack));
    }

    @Inject(method = "overrideStackedOnOther", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/BundleContents$Mutable;tryTransfer(Lnet/minecraft/world/inventory/Slot;Lnet/minecraft/world/entity/player/Player;)I"))
    private void onStackClickedInject2(ItemStack stack, Slot slot, ClickAction clickType, Player player, CallbackInfoReturnable<Boolean> cir, @Local BundleContents.Mutable builder) {
        ((ICustomBundleContentBuilder) builder).setMaxSize(this.getMaxSize(player.level(), stack));
    }


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
    public InteractionResult useOn(UseOnContext context) {

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
            if (bundle.get(DataComponents.BUNDLE_CONTENTS) instanceof BundleContents bundleContentsComponent
                    && !bundleContentsComponent.isEmpty()) {

                //get all index that is blockItem
                List<Integer> blockItemList = new ArrayList<>();
                for (int i = 0; i < bundleContentsComponent.size(); i++) {
                    if (bundleContentsComponent.getItemUnsafe(i).getItem() instanceof BlockItem) {
                        blockItemList.add(i);
                    }
                }

                if (blockItemList.isEmpty())
                    return InteractionResult.PASS;

                //get indexOfItemInInventory based on mode value, if 2 then choose random, if 1 then choose first indexOfItemInInventory
                int chosenIndex = getMode(bundle) == 2 ? blockItemList.get(serverPlayerEntity.getRandom().nextInt(blockItemList.size())) : blockItemList.getFirst();

                //if blockItem
                if (bundleContentsComponent.getItemUnsafe(chosenIndex).getItem() instanceof BlockItem blockItem) {
                    //try to place it
                    InteractionResult actionResult = blockItem.useOn(context);
                    //check result Consume or SUCCESS then block is placed then -1 that block from bundle
                    if (actionResult == InteractionResult.CONSUME || actionResult == InteractionResult.SUCCESS) {
                        removeItem(player, bundle, bundleContentsComponent, chosenIndex);

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
    private void removeItem(Player player, ItemStack bundleItem, BundleContents bundleContentsComponent, int index) {
        if (bundleContentsComponent.isEmpty() || bundleContentsComponent.size() < index + 1)
            return;

        ItemStack itemStack = bundleContentsComponent.getItemUnsafe(index);
        List<ItemStack> itemStacks = new ArrayList<>(bundleContentsComponent.itemCopyStream().toList());

        if (itemStack.getCount() < 1)
            return;

        if (player.isCreative())
            return;

        if (itemStack.getCount() > 1) {
            itemStacks.get(index).shrink(1);

            bundleContentsComponent = new BundleContents(itemStacks);
            bundleItem.set(DataComponents.BUNDLE_CONTENTS, bundleContentsComponent);
            return;
        }

        boolean isRefilled = false;

        if (isRefillable(player.level(), bundleItem))
            isRefilled = refill(player, bundleItem, bundleContentsComponent, itemStacks, index);

        if (!isRefilled) {
            itemStacks.remove(index);
            //System.out.println(itemStacks);
            player.displayClientMessage(Component.literal("Ran out of " + itemStack.getItem().getDescription().getString()), true);
        }

        bundleContentsComponent = new BundleContents(itemStacks);
        bundleItem.set(DataComponents.BUNDLE_CONTENTS, bundleContentsComponent);
    }

    @Unique
    private void syncBundleContents(ServerPlayer player) {
        // Sync the entire inventory to the client
        player.connection.send(new ClientboundContainerSetContentPacket(player.inventoryMenu.containerId, 0, player.inventoryMenu.getItems(), player.inventoryMenu.getCarried()));
    }

    @Unique
    private boolean refill(Player player, ItemStack bundle, BundleContents bundleContentsComponent, List<ItemStack> itemStacks, int index) {

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
        int usedSlotInBundle = Mth.mulAndTruncate(bundleContentsComponent.weight(), 64) - stackMultiplier;

        final int maxBundleSize = getMaxSize(player.level(), bundle);

        if (usedSlotInBundle >= maxBundleSize)
            return false;

        final int freeSlotInBundle = maxBundleSize - usedSlotInBundle;

        int numberOfItemWillPut = (int) Math.floor(Math.min(freeSlotInBundle, realStackSizeOfTheItemWillPutInto) * 1.0f / stackMultiplier);

        //decrestack
        itemStackWillPutInto.consume(numberOfItemWillPut, player);

        //because there is already 1 item in the bundle
        itemStacks.get(index).grow(numberOfItemWillPut - 1);
        player.level().playSound(null, player.blockPosition(), SoundEvents.DECORATED_POT_INSERT, SoundSource.PLAYERS, 0.5f, 1.0f);

        return true;
    }

    @Unique
    private boolean isRefillable(Level world, ItemStack itemStack) {
        if (world.isClientSide)
            return false;

        ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(itemStack);
        int value = itemEnchantmentsComponent.getLevel(converter.getEnchant(world, Constants.MOD_ID, "refill"));
        return value > 0;
    }

    @Unique
    private int getMaxSize(Level world, ItemStack itemStack) {
        ItemEnchantments itemEnchantmentsComponent = EnchantmentHelper.getEnchantmentsForCrafting(itemStack);
        int value = itemEnchantmentsComponent.getLevel(converter.getEnchant(world, Constants.MOD_ID, "capacity"));

        return 64 + value * 64;
    }

    @Override
    public int getMode(ItemStack itemStack) {

        CustomData customData = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        if (customData.contains("diversity:mode"))
            return customData.copyTag().getInt("diversity:mode");
        return 0;
    }

    @Override
    public void setMode(ItemStack itemStack, int mode) {

        String modeText = switch (mode) {
            case 1 -> "place first slot";
            case 2 -> "place random slot";
            default -> "normal";
        };

        itemStack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, comp -> comp.update(currentNbt -> {
            currentNbt.putInt("diversity:mode", mode);
        }));

        itemStack.set(DataComponents.LORE, new ItemLore(new ArrayList<>() {{
            add(Component.literal("Mode: " + modeText).withStyle(ChatFormatting.GRAY));
        }}));


    }


}
