package xyz.faewulf.diversity.feature.entity.pseudoBlockEntity;

import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;
import java.util.function.Function;

public class PseudoBlockEntityBuilder {

    private Block[] parent;
    private String[] parentTag;

    private Consumer<Display> blockTickFunction;
    private Function<Display, Boolean> discardWhenFunction;
    private String diversity_type = null;
    private int tickDelay = 5;

    public PseudoBlockEntityBuilder() {
    }

    public Display.TextDisplay build(Level level) {
        Display.TextDisplay display = new Display.TextDisplay(EntityType.TEXT_DISPLAY, level);

        if (display instanceof PseudoBlockEntity pseudoBlockEntity) {
            pseudoBlockEntity.diversity_Multiloader$setEntityType(this.diversity_type);
            pseudoBlockEntity.diversity_Multiloader$setParentBlockType(this.parent);
            pseudoBlockEntity.diversity_Multiloader$setBlockTickFunction(this.blockTickFunction);
            pseudoBlockEntity.diversity_Multiloader$setDiscardWhenFunction(this.discardWhenFunction);
            pseudoBlockEntity.diversity_Multiloader$setDelayTick(this.tickDelay);
            pseudoBlockEntity.diversity_Multiloader$setParentTag(this.parentTag);
        }

        return display;
    }

    public PseudoBlockEntityBuilder setBlockEntityType(String diversity_type) {
        this.diversity_type = diversity_type;
        return this;
    }

    public Block[] getParent() {
        return parent;
    }

    public PseudoBlockEntityBuilder setParent(Block... parent) {
        this.parent = parent;
        return this;
    }

    public Consumer<Display> getBlockTickFunction() {
        return blockTickFunction;
    }

    public PseudoBlockEntityBuilder setBlockTickFunction(Consumer<Display> blockTickFunction) {
        this.blockTickFunction = blockTickFunction;
        return this;
    }

    public Function<Display, Boolean> getDiscardWhenFunction() {
        return discardWhenFunction;
    }

    public PseudoBlockEntityBuilder setDiscardWhenFunction(Function<Display, Boolean> discardWhenFunction) {
        this.discardWhenFunction = discardWhenFunction;
        return this;
    }

    public String getDiversity_type() {
        return diversity_type;
    }

    public PseudoBlockEntityBuilder setTickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
        return this;
    }

    public String[] getParentTag() {
        return parentTag;
    }

    public PseudoBlockEntityBuilder setParentTag(String... parentTag) {
        this.parentTag = parentTag;
        return this;
    }
}
