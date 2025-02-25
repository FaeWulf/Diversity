package xyz.faewulf.diversity.util;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import xyz.faewulf.diversity.mixin.item.spyglassWhatIsThat.AbstractFurnaceBlockEntityMixin;
import xyz.faewulf.diversity.mixin.item.spyglassWhatIsThat.BeaconBlockEntityMixin;
import xyz.faewulf.diversity.mixin.item.spyglassWhatIsThat.BrewingStandBlockEntityMixin;
import xyz.faewulf.diversity.mixin.item.spyglassWhatIsThat.TrialSpawnerDataMixin;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class hitResult2Infomations {
    public static Component parseBlockState(Level world, Player player, BlockPos blockPos) {

        // Get configs based on zooming or not
        boolean showDistance = true;
        boolean showDirection = true;
        boolean showInfo = true;
        boolean showName = true;

        if (player.isScoping()) {
            showInfo = ModConfigs.spyglass_what_is_that_zoom_show_infos;
            showDirection = ModConfigs.spyglass_what_is_that_zoom_show_direction;
            showDistance = ModConfigs.spyglass_what_is_that_zoom_show_distance;
            showName = ModConfigs.spyglass_what_is_that_zoom_show_block_name;
        } else {
            showInfo = ModConfigs.spyglass_what_is_that_normal_show_infos;
            showDirection = ModConfigs.spyglass_what_is_that_normal_show_direction;
            showDistance = ModConfigs.spyglass_what_is_that_normal_show_distance;
            showName = ModConfigs.spyglass_what_is_that_normal_show_block_name;
        }

        Direction playerDirection = player.getDirection();
        BlockState blockState = world.getBlockState(blockPos);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        Block block = blockState.getBlock();

        MutableComponent result = Component.empty();

        //name
        if (showName)
            result.append(Component.literal(block.getName().getString()));

        //direction
        if (blockState.hasProperty(BlockStateProperties.FACING) && showDirection) {
            Direction blockDirection = blockState.getValue(BlockStateProperties.FACING);
            result.append(Component.literal(" " + getRelativeFacing(playerDirection, blockDirection, false, false)).withStyle(ChatFormatting.AQUA));
        }

        //other horizontal facing
        if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && showDirection) {
            Direction blockDirection = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

            if (blockEntity != null)
                result.append(Component.literal(" " + getRelativeFacing(playerDirection, blockDirection, false, false)).withStyle(ChatFormatting.AQUA));
            else
                result.append(Component.literal(" " + getRelativeFacing(playerDirection, blockDirection, true, true)).withStyle(ChatFormatting.AQUA));
        }

        //Hopper case
        if (blockState.hasProperty(HopperBlock.FACING) && showDirection) {
            Direction blockDirection = blockState.getValue(HopperBlock.FACING);
            result.append(Component.literal(" " + getRelativeFacing(playerDirection, blockDirection, false, false)).withStyle(ChatFormatting.AQUA));
        }


        //redstone enabled
        if (blockState.hasProperty(BlockStateProperties.ENABLED) && showInfo) {
            if (!blockState.getValue(BlockStateProperties.ENABLED))
                result.append(Component.literal(" disabled").withStyle(ChatFormatting.DARK_RED));
        }

        if (blockState.hasProperty(BlockStateProperties.WATERLOGGED) && showInfo) {
            if (blockState.getValue(BlockStateProperties.WATERLOGGED))
                result.append(Component.literal(" waterlogged").withStyle(ChatFormatting.DARK_AQUA));
        }

        //noteBlock
        if (blockState.hasProperty(BlockStateProperties.NOTE) && showInfo) {
            int note = blockState.getValue(BlockStateProperties.NOTE);
            NoteBlockInstrument instrument = blockState.getValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT);

            result.append(Component.literal(" " + converter.getNoteCharacter(note)).withStyle(ChatFormatting.GOLD));
            result.append(Component.literal(" " + instrument.name()).withStyle(ChatFormatting.GOLD));
        }

        //trial spawner
        if (blockEntity instanceof TrialSpawnerBlockEntity trialSpawnerBlockEntity && showInfo) {
            TrialSpawnerData trialSpawnerData = trialSpawnerBlockEntity.getTrialSpawner().getData();
            if (!trialSpawnerData.isCooldownFinished((ServerLevel) world)) {
                long cooldown = ((TrialSpawnerDataMixin) trialSpawnerData).getCooldownEndsAt();
                result.append(Component.literal(" ⌛" + converter.tick2Time(cooldown - world.getGameTime())).withStyle(ChatFormatting.RED));
            }
        }

        //furnace
        if (blockEntity instanceof AbstractFurnaceBlockEntity abstractFurnaceBlockEntity && showInfo) {

            AtomicInteger totalExp = new AtomicInteger();
            for (Reference2IntMap.Entry<ResourceKey<Recipe<?>>> entry : ((AbstractFurnaceBlockEntityMixin) abstractFurnaceBlockEntity).getRecipesUsed().reference2IntEntrySet()) {
                ((ServerLevel) world).recipeAccess().byKey(entry.getKey()).ifPresent(recipe -> {
                    int multiplier = entry.getIntValue();
                    float experience = ((AbstractCookingRecipe) recipe.value()).experience();

                    int i = Mth.floor((float) multiplier * experience);
                    float f = Mth.frac((float) multiplier * experience);
                    if (f != 0.0F && Math.random() < (double) f) {
                        ++i;
                    }
                    totalExp.addAndGet(i);
                });
            }

            if (totalExp.get() > 0) {
                result.append(Component.literal(" Exp: " + totalExp.get()).withStyle(ChatFormatting.GREEN));
            }
        }


        //redstonne dust
        if (blockState.hasProperty(BlockStateProperties.POWER) && showInfo) {
            int power = blockState.getValue(BlockStateProperties.POWER);
            result.append(Component.literal(" ⚡" + power).withStyle(ChatFormatting.RED));
        }

        //beehive
        if (blockState.hasProperty(BeehiveBlock.HONEY_LEVEL) && showInfo) {
            int level = blockState.getValue(BlockStateProperties.LEVEL_HONEY);
            result.append(Component.literal(" \uD83C\uDF6F" + level).withStyle(ChatFormatting.GOLD));

            if (blockEntity instanceof BeehiveBlockEntity beehiveBlockEntity)
                result.append(Component.literal(" \uD83D\uDC1D" + beehiveBlockEntity.getOccupantCount()).withStyle(ChatFormatting.GOLD));
        }

        //brewing stand
        if (blockEntity instanceof BrewingStandBlockEntity brewingStandBlockEntity && showInfo) {
            int fuel = ((BrewingStandBlockEntityMixin) brewingStandBlockEntity).getFuel();
            result.append(Component.literal(" Fuel: " + fuel).withStyle(ChatFormatting.GOLD));
        }

        //beacon
        if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity && showInfo) {
            int level = ((BeaconBlockEntityMixin) beaconBlockEntity).getLevels();
            result.append(Component.literal(" Level: " + level).withStyle(ChatFormatting.GREEN));
            result.append(Component.literal(" Radius: " + (level * 10 + 10)).withStyle(ChatFormatting.DARK_AQUA));

        }

        //distance
        if (showDistance) {
            BlockPos playerPos = player.blockPosition();

            BlockPos distance = blockPos.subtract(playerPos);

            result.append(Component.literal(" \uD83D\uDCD0("));
            result.append(Component.literal(distance.getX() + ", ").withStyle(ChatFormatting.RED));
            result.append(Component.literal(distance.getY() + ", ").withStyle(ChatFormatting.GREEN));
            result.append(Component.literal(String.valueOf(distance.getZ())).withStyle(ChatFormatting.BLUE));
            result.append(Component.literal(")"));

            double distanceGeometry = Math.sqrt(distance.getX() * distance.getX() + distance.getY() * distance.getY() + distance.getZ() * distance.getZ());
            distanceGeometry = Math.round(distanceGeometry * 10.0) / 10.0;
            result.append(Component.literal(" \uD83D\uDCCF" + distanceGeometry));
        }

        return result;
    }

    private static String getRelativeFacing(Direction player, Direction block, boolean inverseLeftRight, boolean inverseBackFront) {
        //facing up
        if (block == Direction.UP) {
            return "↥";
        }
        //facing down
        else if (block == Direction.DOWN) {
            return "↧";
        }
        //other facings
        else {
            int turnValue = 0;
            while (player != block) {
                block = block.getClockWise();
                turnValue++;
            }

            String arrow = switch (turnValue) {
                case 1 -> inverseLeftRight ? "➡" : "⬅";
                case 2 -> inverseBackFront ? "⬆" : "⬇";
                case 3 -> inverseLeftRight ? "⬅" : "➡";
                default -> inverseBackFront ? "⬇" : "⬆";
            };

            return arrow;
        }
    }

    public static Component parseLivingEntity(Level world, Player player, Entity entity) {

        // Get configs based on zooming or not
        boolean showDistance = true;
        boolean showInfo = true;
        boolean showName = true;

        if (player.isScoping()) {
            showInfo = ModConfigs.spyglass_what_is_that_zoom_show_infos;
            showDistance = ModConfigs.spyglass_what_is_that_zoom_show_distance;
            showName = ModConfigs.spyglass_what_is_that_zoom_show_block_name;
        } else {
            showInfo = ModConfigs.spyglass_what_is_that_normal_show_infos;
            showDistance = ModConfigs.spyglass_what_is_that_normal_show_distance;
            showName = ModConfigs.spyglass_what_is_that_normal_show_block_name;
        }

        MutableComponent result = Component.empty();
        DecimalFormat df = new DecimalFormat("#.#");

        //entity variation
        if (entity instanceof Wolf wolfEntity && showName) {
            result.append(converter.UppercaseFirstLetter(wolfEntity.getVariant().getRegisteredName().replace("minecraft:", "").replace("_", " ")));
        }

        //cat
        if (entity instanceof Cat catEntity && showName) {
            result.append(converter.UppercaseFirstLetter(catEntity.getVariant().getRegisteredName().replace("minecraft:", "").replace("_", " ")));
        }

        //villager
        if (entity instanceof ZombieVillager zombieVillagerEntity && showName) {
            result.append(converter.UppercaseFirstLetter((zombieVillagerEntity.getVillagerData().getProfession().toString())));
        }

        if (showName) {
            result.append(" ");
            result.append(entity.getName());
        }

        //living entity
        if (entity instanceof LivingEntity livingEntity && showInfo) {
            //hp

            float hp = Math.round(livingEntity.getHealth() * 10.0f) / 10.0f;
            float maxHP = livingEntity.getMaxHealth();

            result.append(" |");
            result.append(Component.literal(" ♥").withStyle(ChatFormatting.RED));
            result.append(Component.literal(df.format(hp) + "/" + df.format(maxHP)));
        }

        //horse
        if (entity instanceof AbstractHorse abstractHorseEntity && showInfo) {
            double speed = abstractHorseEntity.getAttributes().getValue(Attributes.MOVEMENT_SPEED);
            double jump = abstractHorseEntity.getAttributes().getValue(Attributes.JUMP_STRENGTH);

            result.append(" |");
            result.append(Component.literal(" ➠").withStyle(ChatFormatting.BLUE));
            result.append(Component.literal(df.format(converter.genericSpeed2BlockPerSecond(speed))));
            result.append(Component.literal(" ⇡").withStyle(ChatFormatting.GREEN));
            result.append(Component.literal(df.format(converter.horseJumpStrength2JumpHeight(jump))));
        }

        //llama, donkey, every entity has chest
        if (entity instanceof AbstractChestedHorse abstractDonkeyEntity && showInfo) {
            String slot = df.format(3L * abstractDonkeyEntity.getInventoryColumns());
            result.append(" |");
            result.append(Component.literal(" Slots:").withStyle(ChatFormatting.GOLD));
            result.append(slot);
        }


        //tame check
        if (entity instanceof OwnableEntity tameable && showInfo) {
            if (tameable.getOwnerUUID() != null) {
                result.append(" |");
                result.append(Component.literal(" Tamed").withStyle(ChatFormatting.GREEN));
            }
        }


        //Animal entity
        if (entity instanceof Animal animalEntity && showInfo) {
            int breedAge = animalEntity.getAge();
            if (breedAge > 0) {
                result.append(" |");
                result.append(Component.literal(" ⌛Breed:" + converter.tick2Time(breedAge)).withStyle(ChatFormatting.GOLD));
            }
        }

        if (showDistance) {
            BlockPos playerPos = player.blockPosition();

            BlockPos distance = entity.blockPosition().subtract(playerPos);
            double distanceGeometry = Math.sqrt(distance.getX() * distance.getX() + distance.getY() * distance.getY() + distance.getZ() * distance.getZ());
            result.append(" |");
            result.append(Component.literal(" \uD83D\uDCCF" + df.format(distanceGeometry)));
        }


        return result;

    }
}
