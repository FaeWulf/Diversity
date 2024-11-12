package xyz.faewulf.diversity.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

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

    public static boolean isEntity2BehindEntity1(LivingEntity entity1, LivingEntity entity2) {
        // Villager's facing direction vector
        Vec3 entity1ViewVector = entity1.getViewVector(1.0F);

        // Vector from villager to player
        Vec3 toEntity2 = entity2.position().subtract(entity1.position()).normalize();

        // Calculate the angle between the two vectors
        double dotProduct = entity1ViewVector.dot(toEntity2);
        double angle = Math.acos(dotProduct);

        // If angle is close to π (180 degrees), the player is behind the villager
        return angle >= Math.PI / 2 && angle <= Math.PI;
    }
}
