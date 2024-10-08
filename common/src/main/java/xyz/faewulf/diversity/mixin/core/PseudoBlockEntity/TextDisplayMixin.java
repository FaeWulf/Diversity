package xyz.faewulf.diversity.mixin.core.PseudoBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.feature.entity.pseudoBlockEntity.PseudoBlockEntities;
import xyz.faewulf.diversity.feature.entity.pseudoBlockEntity.PseudoBlockEntity;
import xyz.faewulf.diversity.feature.entity.pseudoBlockEntity.PseudoBlockEntityBuilder;
import xyz.faewulf.diversity.util.compare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(Display.class)
public abstract class TextDisplayMixin extends Entity implements PseudoBlockEntity {

    @Unique
    private List<String> diversity_Multiloader$parent = new ArrayList<>();

    @Unique
    private int diversity_Multiloader$tickDelay = 0;

    @Unique
    private String diversity_type;

    @Unique
    private Consumer<Display> diversity_Multiloader$blockTickFunction;

    @Unique
    private Function<Display, Boolean> diversity_Multiloader$discardWhenFunction;

    @Unique
    private List<String> diversity_Multiloader$parentTag = new ArrayList<>();


    public TextDisplayMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean diversity_Multiloader$isBlockEntityAlreadyExist() {

        BlockPos blockPos = this.blockPosition();

        AABB box = new AABB(
                blockPos.getX() + 0.3f, blockPos.getY() + 0.3f, blockPos.getZ() + 0.3f,
                blockPos.getX() + 0.7f, blockPos.getY() + 0.7f, blockPos.getZ() + 0.7f
        );

        List<Entity> entitiesWithinRadius = this.level().getEntitiesOfClass(Entity.class, box, entity -> entity.getType() == EntityType.TEXT_DISPLAY);

        return !(entitiesWithinRadius.isEmpty() || entitiesWithinRadius.size() == 1);
    }


    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (this.diversity_type != null)
            nbt.putString("diversity:pseudoType", this.diversity_type);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:pseudoType", Tag.TAG_STRING)) {
            this.diversity_type = nbt.getString("diversity:pseudoType");

            //based on the type, set all the relative variable using PseudoBlockEntityBuilder
            PseudoBlockEntityBuilder builder = PseudoBlockEntities.PseudoBlockEntityList.get(this.diversity_type);

            if (builder != null) {

                this.diversity_Multiloader$parent = new ArrayList<>();

                for (Block block : builder.getParent()) {
                    this.diversity_Multiloader$parent.add(block.toString());
                }

                if (builder.getParentTag() != null)
                    this.diversity_Multiloader$parentTag = List.of(builder.getParentTag());

                this.diversity_Multiloader$blockTickFunction = builder.getBlockTickFunction();
                this.diversity_Multiloader$discardWhenFunction = builder.getDiscardWhenFunction();
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickInject(CallbackInfo ci) {

        if (this.diversity_type == null || this.level().isClientSide)
            return;

        //for tick every 1sec
        if (diversity_Multiloader$tickDelay < 20) {
            diversity_Multiloader$tickDelay++;
            return;
        }
        diversity_Multiloader$tickDelay = 0;

        BlockPos pos = this.blockPosition();
        BlockState currentBlock = this.level().getBlockState(pos);

        //for checking discard criteria
        //if true then discard and return

        //checking tag
        boolean hasTagInList = false;
        for (String s : diversity_Multiloader$parentTag) {
            if (compare.isHasTag(currentBlock.getBlock(), s))
                hasTagInList = true;
        }

        if (!(this.diversity_Multiloader$parent.contains(currentBlock.getBlock().toString()) || hasTagInList)
                || diversity_Multiloader$isBlockEntityAlreadyExist()
                || (this.diversity_Multiloader$discardWhenFunction != null && this.diversity_Multiloader$discardWhenFunction.apply((Display) (Object) this))
        ) {
            this.discard();
            return;
        }

        //tick function for the BE
        if (this.diversity_Multiloader$blockTickFunction != null)
            this.diversity_Multiloader$blockTickFunction.accept((Display) (Object) this);
    }

    @Override
    public void diversity_Multiloader$setBlockTickFunction(Consumer<Display> callback) {
        this.diversity_Multiloader$blockTickFunction = callback;
    }


    @Override
    public void diversity_Multiloader$setDiscardWhenFunction(Function<Display, Boolean> callback) {
        this.diversity_Multiloader$discardWhenFunction = callback;
    }

    @Override
    public void diversity_Multiloader$setParentBlockType(Block... block) {
        this.diversity_Multiloader$parent = new ArrayList<>();
        for (Block block1 : block) {
            this.diversity_Multiloader$parent.add(block1.toString());
        }
    }

    @Override
    public String diversity_Multiloader$getEntityType() {
        return this.diversity_type;
    }

    @Override
    public void diversity_Multiloader$setEntityType(String value) {
        this.diversity_type = value;
    }

    @Override
    public void diversity_Multiloader$setDelayTick(int value) {
        this.diversity_Multiloader$tickDelay = value;
    }

    @Override
    public List<String> diversity_Multiloader$getParentTag() {
        return diversity_Multiloader$parentTag;
    }

    @Override
    public void diversity_Multiloader$setParentTag(@Nullable String[] parentTag) {

        if (parentTag == null)
            return;

        this.diversity_Multiloader$parentTag = Arrays.asList(parentTag);
    }
}
