package faewulf.diversity.util;

import faewulf.diversity.mixin.spyglassWhatIsThat.AbstractFurnaceBlockEntityMixin;
import faewulf.diversity.mixin.spyglassWhatIsThat.BeaconBlockEntityMixin;
import faewulf.diversity.mixin.spyglassWhatIsThat.BrewingStandBlockEntityMixin;
import faewulf.diversity.mixin.spyglassWhatIsThat.TrialSpawnerDataMixin;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.spawner.TrialSpawnerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

public class hitResult2Infomations {
    public static Text parseBlockState(World world, PlayerEntity player, BlockPos blockPos) {

        Direction playerDirection = player.getHorizontalFacing();
        BlockState blockState = world.getBlockState(blockPos);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        Block block = blockState.getBlock();

        MutableText result = Text.empty();

        //System.out.println(blockState.getProperties());

        //name
        result.append(Text.literal(block.getName().getString()));

        //direction
        if (blockState.contains(Properties.FACING)) {
            Direction blockDirection = blockState.get(Properties.FACING);
            result.append(Text.literal(" " + getRelativeFacing(playerDirection, blockDirection, false, false)).formatted(Formatting.AQUA));
        }

        //other horizontal facing
        if (blockState.contains(Properties.HORIZONTAL_FACING)) {
            Direction blockDirection = blockState.get(Properties.HORIZONTAL_FACING);

            if (blockEntity != null)
                result.append(Text.literal(" " + getRelativeFacing(playerDirection, blockDirection, false, false)).formatted(Formatting.AQUA));
            else
                result.append(Text.literal(" " + getRelativeFacing(playerDirection, blockDirection, true, true)).formatted(Formatting.AQUA));
        }

        //Hopper case
        if (blockState.contains(HopperBlock.FACING)) {
            Direction blockDirection = blockState.get(HopperBlock.FACING);
            result.append(Text.literal(" " + getRelativeFacing(playerDirection, blockDirection, false, false)).formatted(Formatting.AQUA));
        }


        //redstone enabled
        if (blockState.contains(Properties.ENABLED)) {
            if (!blockState.get(Properties.ENABLED))
                result.append(Text.literal(" disabled").formatted(Formatting.DARK_RED));
        }

        if (blockState.contains(Properties.WATERLOGGED)) {
            if (blockState.get(Properties.WATERLOGGED))
                result.append(Text.literal(" waterlogged").formatted(Formatting.DARK_AQUA));
        }

        //noteBlock
        if (blockState.contains(Properties.NOTE)) {
            int note = blockState.get(Properties.NOTE);
            NoteBlockInstrument instrument = blockState.get(Properties.INSTRUMENT);

            result.append(Text.literal(" " + converter.getNoteCharacter(note)).formatted(Formatting.GOLD));
            result.append(Text.literal(" " + instrument.name()).formatted(Formatting.GOLD));
        }

        //trial spawner
        if (blockEntity instanceof TrialSpawnerBlockEntity trialSpawnerBlockEntity) {
            TrialSpawnerData trialSpawnerData = trialSpawnerBlockEntity.getSpawner().getData();
            if (!trialSpawnerData.isCooldownOver((ServerWorld) world)) {
                long cooldown = ((TrialSpawnerDataMixin) trialSpawnerData).getCooldownEnd();
                result.append(Text.literal(" ⏳" + converter.tiok2Time(cooldown - world.getTime())).formatted(Formatting.RED));
            }
        }

        //furnace
        if (blockEntity instanceof AbstractFurnaceBlockEntity abstractFurnaceBlockEntity) {

            AtomicInteger totalExp = new AtomicInteger();
            for (Object2IntMap.Entry<Identifier> entry : ((AbstractFurnaceBlockEntityMixin) abstractFurnaceBlockEntity).getRecipesUsed().object2IntEntrySet()) {
                world.getRecipeManager().get((Identifier) entry.getKey()).ifPresent(recipe -> {
                    int multiplier = entry.getIntValue();
                    float experience = ((AbstractCookingRecipe) recipe.value()).getExperience();

                    int i = MathHelper.floor((float) multiplier * experience);
                    float f = MathHelper.fractionalPart((float) multiplier * experience);
                    if (f != 0.0F && Math.random() < (double) f) {
                        ++i;
                    }
                    totalExp.addAndGet(i);
                });
            }

            if (totalExp.get() > 0) {
                result.append(Text.literal(" Exp: " + totalExp.get()).formatted(Formatting.GREEN));
            }
        }


        //redstonne dust
        if (blockState.contains(Properties.POWER)) {
            int power = blockState.get(Properties.POWER);
            result.append(Text.literal(" ⚡" + power).formatted(Formatting.RED));
        }

        //beehive
        if (blockState.contains(BeehiveBlock.HONEY_LEVEL)) {
            int level = blockState.get(Properties.HONEY_LEVEL);
            result.append(Text.literal(" \uD83C\uDF6F" + level).formatted(Formatting.GOLD));

            if (blockEntity instanceof BeehiveBlockEntity beehiveBlockEntity)
                result.append(Text.literal(" \uD83D\uDC1D" + beehiveBlockEntity.getBeeCount()).formatted(Formatting.GOLD));
        }

        //brewing stand
        if (blockEntity instanceof BrewingStandBlockEntity brewingStandBlockEntity) {
            int fuel = ((BrewingStandBlockEntityMixin) brewingStandBlockEntity).getFuel();
            result.append(Text.literal(" Fuel: " + fuel).formatted(Formatting.GOLD));
        }

        //beacon
        if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity) {
            int level = ((BeaconBlockEntityMixin) beaconBlockEntity).getLevel();
            result.append(Text.literal(" Level: " + level).formatted(Formatting.GREEN));
            result.append(Text.literal(" Radius: " + (level * 10 + 10)).formatted(Formatting.DARK_AQUA));

        }

        //distance
        if (player.isUsingSpyglass()) {
            BlockPos playerPos = player.getBlockPos();

            BlockPos distance = blockPos.subtract(playerPos);

            result.append(Text.literal(" \uD83D\uDCD0("));
            result.append(Text.literal(distance.getX() + ", ").formatted(Formatting.RED));
            result.append(Text.literal(distance.getY() + ", ").formatted(Formatting.GREEN));
            result.append(Text.literal(String.valueOf(distance.getZ())).formatted(Formatting.BLUE));
            result.append(Text.literal(")"));

            double distanceGeometry = Math.sqrt(distance.getX() * distance.getX() + distance.getY() * distance.getY() + distance.getZ() * distance.getZ());
            distanceGeometry = Math.round(distanceGeometry * 10.0) / 10.0;
            result.append(Text.literal(" \uD83D\uDCCF" + distanceGeometry));
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
                block = block.rotateYClockwise();
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

    public static Text parseLivingEntity(World world, PlayerEntity player, Entity entity) {

        MutableText result = Text.empty();
        DecimalFormat df = new DecimalFormat("#.#");

        //entity variation
        if (entity instanceof WolfEntity wolfEntity) {
            result.append(converter.UppercaseFirstLetter(wolfEntity.getVariant().getIdAsString().replace("minecraft:", "").replace("_", " ")));
        }

        //cat
        if (entity instanceof CatEntity catEntity) {
            result.append(converter.UppercaseFirstLetter(catEntity.getVariant().getIdAsString().replace("minecraft:", "").replace("_", " ")));
        }

        //villager
        if (entity instanceof ZombieVillagerEntity zombieVillagerEntity) {
            result.append(converter.UppercaseFirstLetter((zombieVillagerEntity.getVillagerData().getProfession().toString())));
        }

        result.append(" ");
        result.append(entity.getName());

        //living entity
        if (entity instanceof LivingEntity livingEntity) {
            //hp

            float hp = Math.round(livingEntity.getHealth() * 10.0f) / 10.0f;
            float maxHP = livingEntity.getMaxHealth();

            result.append(" |");
            result.append(Text.literal(" ❤").formatted(Formatting.RED));
            result.append(Text.literal(df.format(hp) + "/" + df.format(maxHP)));
        }

        //horse
        if (entity instanceof AbstractHorseEntity abstractHorseEntity) {
            double speed = abstractHorseEntity.getAttributes().getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            double jump = abstractHorseEntity.getAttributes().getValue(EntityAttributes.GENERIC_JUMP_STRENGTH);

            result.append(" |");
            result.append(Text.literal(" ➠").formatted(Formatting.BLUE));
            result.append(Text.literal(df.format(converter.genericSpeed2BlockPerSecond(speed))));
            result.append(Text.literal(" ⇡").formatted(Formatting.GREEN));
            result.append(Text.literal(df.format(converter.horseJumpStrength2JumpHeight(jump))));
        }

        //llama, donkey, every entity has chest
        if (entity instanceof AbstractDonkeyEntity abstractDonkeyEntity) {
            String slot = df.format(3L * abstractDonkeyEntity.getInventoryColumns());
            result.append(" |");
            result.append(Text.literal(" Slots:").formatted(Formatting.GOLD));
            result.append(slot);
        }


        //tame check
        if (entity instanceof Tameable tameable) {
            if (tameable.getOwnerUuid() != null) {
                result.append(" |");
                result.append(Text.literal(" Tamed").formatted(Formatting.GREEN));
            }
        }


        //Animal entity
        if (entity instanceof AnimalEntity animalEntity) {
            int breedAge = animalEntity.getBreedingAge();
            if (breedAge > 0) {
                result.append(" |");
                result.append(Text.literal(" ⏳Breed:" + converter.tiok2Time(breedAge)).formatted(Formatting.GOLD));
            }
        }

        if (player.isUsingSpyglass()) {
            BlockPos playerPos = player.getBlockPos();

            BlockPos distance = entity.getBlockPos().subtract(playerPos);
            double distanceGeometry = Math.sqrt(distance.getX() * distance.getX() + distance.getY() * distance.getY() + distance.getZ() * distance.getZ());
            result.append(" |");
            result.append(Text.literal(" \uD83D\uDCCF" + df.format(distanceGeometry)));
        }


        return result;

    }
}
