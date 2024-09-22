package xyz.faewulf.diversity.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class compare {
    public static boolean isHasTag(Block block, String tagName) {
        // Create a TagKey for the block using the tagName.


        ResourceLocation path = ResourceLocation.tryParse(tagName);

        if (path == null)
            return false;

        TagKey<Block> blockTag = TagKey.create(BuiltInRegistries.BLOCK.key(), path);

        try {
            // Check if the block is in the specified tag.
            return BuiltInRegistries.BLOCK.getHolderOrThrow(BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow())
                    .is(blockTag);
        } catch (IllegalStateException e) {
            return false;
        }

    }

    public static boolean isHasTag(Item item, String tagName) {
        // Create a TagKey for the block using the tagName.


        ResourceLocation path = ResourceLocation.tryParse(tagName);

        if (path == null)
            return false;

        TagKey<Item> itemTag = TagKey.create(BuiltInRegistries.ITEM.key(), path);

        try {
            // Check if the block is in the specified tag.
            return BuiltInRegistries.ITEM.getHolderOrThrow(BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow())
                    .is(itemTag);
        } catch (IllegalStateException e) {
            return false;
        }

    }
}
