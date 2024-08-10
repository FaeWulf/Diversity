package faewulf.diversity.mixin.buildingBundle;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import faewulf.diversity.Diversity;
import faewulf.diversity.inter.ICustomBundleContentBuilder;
import faewulf.diversity.inter.ICustomBundleItem;
import faewulf.diversity.util.ModConfigs;
import faewulf.diversity.util.converter;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(value = BundleItem.class, priority = 1)
public abstract class BundleItemMixin extends Item implements ICustomBundleItem {
    public BundleItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public int getEnchantability() {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    //@ModifyConstant(method = "appendTooltip", constant = @Constant(intValue = 64, ordinal = 1))
    @ModifyExpressionValue(method = "appendTooltip", at = @At(value = "CONSTANT", args = "intValue=64", ordinal = 1))
    private int appendTooltipInject(int value, @Local(argsOnly = true) ItemStack stack) {
        ItemEnchantmentsComponent t = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);

        AtomicInteger level = new AtomicInteger();
        t.getEnchantments().forEach(enchantmentRegistryEntry -> {
            ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(stack);
            if (enchantmentRegistryEntry.matchesId(Identifier.of(Diversity.MODID, "capacity"))) {
                level.set(itemEnchantmentsComponent.getLevel(enchantmentRegistryEntry));
            }
        });

        return Math.max(value, 64 + 64 * level.get());
    }

    @Inject(method = "onClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/BundleContentsComponent$Builder;add(Lnet/minecraft/item/ItemStack;)I"))
    private void onClickedInject(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir, @Local BundleContentsComponent.Builder builder) {
        ((ICustomBundleContentBuilder) builder).setMaxSize(this.getMaxSize(player.getWorld(), stack));
    }

    @Inject(method = "onStackClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/BundleContentsComponent$Builder;add(Lnet/minecraft/item/ItemStack;)I"))
    private void onStackClickedInject(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir, @Local BundleContentsComponent.Builder builder) {
        ((ICustomBundleContentBuilder) builder).setMaxSize(this.getMaxSize(player.getWorld(), stack));
    }

    @Inject(method = "onStackClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/BundleContentsComponent$Builder;add(Lnet/minecraft/screen/slot/Slot;Lnet/minecraft/entity/player/PlayerEntity;)I"))
    private void onStackClickedInject2(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir, @Local BundleContentsComponent.Builder builder) {
        ((ICustomBundleContentBuilder) builder).setMaxSize(this.getMaxSize(player.getWorld(), stack));
    }


    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {

        if (!ModConfigs.bundle_place_mode)
            return;

        if (!world.isClient && user instanceof ServerPlayerEntity) {
            if (getMode(user.getStackInHand(hand)) != 0) {
                syncBundleContents((ServerPlayerEntity) user);
                cir.setReturnValue(TypedActionResult.fail(user.getStackInHand(hand)));
            }
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if (!ModConfigs.bundle_place_mode)
            return ActionResult.PASS;

        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        if (world.isClient)
            return ActionResult.PASS;

        ItemStack bundle = context.getStack();

        if (getMode(bundle) == 0)
            return ActionResult.PASS;

        if (world instanceof ServerWorld serverWorld && player instanceof ServerPlayerEntity serverPlayerEntity) {
            if (bundle.get(DataComponentTypes.BUNDLE_CONTENTS) instanceof BundleContentsComponent bundleContentsComponent
                    && !bundleContentsComponent.isEmpty()) {

                //get all index that is blockItem
                List<Integer> blockItemList = new ArrayList<>();
                for (int i = 0; i < bundleContentsComponent.size(); i++) {
                    if (bundleContentsComponent.get(i).getItem() instanceof BlockItem) {
                        blockItemList.add(i);
                    }
                }

                if (blockItemList.isEmpty())
                    return ActionResult.PASS;

                //get indexOfItemInInventory based on mode value, if 2 then choose random, if 1 then choose first indexOfItemInInventory
                int chosenIndex = getMode(bundle) == 2 ? blockItemList.get(serverPlayerEntity.getRandom().nextInt(blockItemList.size())) : blockItemList.getFirst();

                //if blockItem
                if (bundleContentsComponent.get(chosenIndex).getItem() instanceof BlockItem blockItem) {
                    //try to place it
                    ActionResult actionResult = blockItem.useOnBlock(context);
                    //check result Consume or SUCCESS then block is placed then -1 that block from bundle
                    if (actionResult == ActionResult.CONSUME || actionResult == ActionResult.SUCCESS) {
                        removeItem(player, bundle, bundleContentsComponent, chosenIndex);

                        BlockState blockState = blockItem.getBlock().getDefaultState();
                        SoundEvent soundEvent = ((BlockItemInvoker) blockItem).invokeGetPlaceSound(blockState);

                        serverWorld.playSound(
                                null,
                                context.getBlockPos(),
                                soundEvent,
                                SoundCategory.BLOCKS,
                                1f,
                                0.8F
                        );
                    }
                }
            }
        }

        return ActionResult.PASS;
    }

    @Unique
    private void removeItem(PlayerEntity player, ItemStack bundleItem, BundleContentsComponent bundleContentsComponent, int index) {
        if (bundleContentsComponent.isEmpty() || bundleContentsComponent.size() < index + 1)
            return;

        ItemStack itemStack = bundleContentsComponent.get(index);
        List<ItemStack> itemStacks = new ArrayList<>(bundleContentsComponent.stream().toList());

        if (itemStack.getCount() < 1)
            return;

        if (player.isCreative())
            return;

        if (itemStack.getCount() > 1) {
            itemStacks.get(index).decrement(1);

            bundleContentsComponent = new BundleContentsComponent(itemStacks);
            bundleItem.set(DataComponentTypes.BUNDLE_CONTENTS, bundleContentsComponent);
            return;
        }

        boolean isRefilled = false;

        if (isRefillable(player.getWorld(), bundleItem))
            isRefilled = refill(player, bundleItem, bundleContentsComponent, itemStacks, index);

        if (!isRefilled) {
            itemStacks.remove(index);
            //System.out.println(itemStacks);
            player.sendMessage(Text.literal("Run out of " + itemStack.getItem().getName().getString()), true);
        }

        bundleContentsComponent = new BundleContentsComponent(itemStacks);
        bundleItem.set(DataComponentTypes.BUNDLE_CONTENTS, bundleContentsComponent);
    }

    @Unique
    private void syncBundleContents(ServerPlayerEntity player) {
        // Sync the entire inventory to the client
        player.networkHandler.sendPacket(new InventoryS2CPacket(player.playerScreenHandler.syncId, 0, player.playerScreenHandler.getStacks(), player.playerScreenHandler.getCursorStack()));
    }

    @Unique
    private boolean refill(PlayerEntity player, ItemStack bundle, BundleContentsComponent bundleContentsComponent, List<ItemStack> itemStacks, int index) {

        PlayerInventory playerInventory = player.getInventory();
        int indexOfItemInInventory = playerInventory.getSlotWithStack(itemStacks.get(index));

        if (indexOfItemInInventory == -1)
            return false;

        ItemStack itemStackWillPutInto = playerInventory.getStack(indexOfItemInInventory);
        //Bundle treats a slot = 64, sign max size 16 fit a slot = 64,
        //so have to getCount()*4 "(64 / 16 = 4)" to match the bundle size
        int stackMultiplier = 64 / itemStackWillPutInto.getMaxCount();
        int realStackSizeOfTheItemWillPutInto = stackMultiplier * itemStackWillPutInto.getCount();
        //int usedSlotInBundle = totalItemInBundle(itemStacks) - 1;

        //subtract 1 because there is 1 amount of the target item in the bundle we did not remove yet, if we remove
        //then target item will lose its position in the bundle -> no consistency
        //because of that, for the later we have to subtract 1 when increase amount of target item in the bundle
        int usedSlotInBundle = MathHelper.multiplyFraction(bundleContentsComponent.getOccupancy(), 64) - stackMultiplier;

        final int maxBundleSize = getMaxSize(player.getWorld(), bundle);

        if (usedSlotInBundle >= maxBundleSize)
            return false;

        final int freeSlotInBundle = maxBundleSize - usedSlotInBundle;

        int numberOfItemWillPut = (int) Math.floor(Math.min(freeSlotInBundle, realStackSizeOfTheItemWillPutInto) * 1.0f / stackMultiplier);

        //decrestack
        itemStackWillPutInto.decrementUnlessCreative(numberOfItemWillPut, player);

        //because there is already 1 item in the bundle
        itemStacks.get(index).increment(numberOfItemWillPut - 1);
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_DECORATED_POT_INSERT, SoundCategory.PLAYERS, 0.5f, 1.0f);

        return true;
    }

    @Unique
    private boolean isRefillable(World world, ItemStack itemStack) {
        if (world.isClient)
            return false;

        ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack);
        int value = itemEnchantmentsComponent.getLevel(converter.getEnchant(world, Diversity.MODID, "refill"));
        return value > 0;
    }

    @Unique
    private int getMaxSize(World world, ItemStack itemStack) {
        ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack);
        int value = itemEnchantmentsComponent.getLevel(converter.getEnchant(world, Diversity.MODID, "capacity"));

        return 64 + value * 64;
    }

    @Override
    public int getMode(ItemStack itemStack) {

        NbtComponent customData = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);

        if (customData.contains("diversity:mode"))
            return customData.copyNbt().getInt("diversity:mode");
        return 0;
    }

    @Override
    public void setMode(ItemStack itemStack, int mode) {

        String modeText = switch (mode) {
            case 1 -> "place first slot";
            case 2 -> "place random slot";
            default -> "normal";
        };

        itemStack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.putInt("diversity:mode", mode);
        }));

        itemStack.set(DataComponentTypes.LORE, new LoreComponent(new ArrayList<>() {{
            add(Text.literal("Mode: " + modeText).formatted(Formatting.GRAY));
        }}));


    }


}
