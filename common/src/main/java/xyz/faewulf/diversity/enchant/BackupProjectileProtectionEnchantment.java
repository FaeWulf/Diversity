package xyz.faewulf.diversity.enchant;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import org.jetbrains.annotations.NotNull;
import xyz.faewulf.diversity.util.CustomEnchant;

import static xyz.faewulf.diversity.util.CustomEnchant.ARMOR_SLOTS;

public class BackupProjectileProtectionEnchantment extends Enchantment {

    public final BackupProjectileProtectionEnchantment.Type type;

    public BackupProjectileProtectionEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.ARMOR, ARMOR_SLOTS);
        this.type = Type.PROJECTILE;
    }

    @Override
    public int getMinCost(int $$0) {
        return this.type.getMinCost() + ($$0 - 1) * this.type.getLevelCost();
    }

    @Override
    public int getMaxCost(int $$0) {
        return this.getMinCost($$0) + this.type.getLevelCost();
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getDamageProtection(int $$0, DamageSource $$1) {

        if ($$1.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return 0;
        } else if (this.type == Type.ALL) {
            return $$0;
        } else if (this.type == Type.FIRE && $$1.is(DamageTypeTags.IS_FIRE)) {
            return $$0 * 2;
        } else if (this.type == Type.FALL && $$1.is(DamageTypeTags.IS_FALL)) {
            return $$0 * 3;
        } else if (this.type == Type.EXPLOSION && $$1.is(DamageTypeTags.IS_EXPLOSION)) {
            return $$0 * 2;
        } else {
            return this.type == Type.PROJECTILE && $$1.is(DamageTypeTags.IS_PROJECTILE) ? $$0 * 2 : 0;
        }
    }

    @Override
    public boolean checkCompatibility(@NotNull Enchantment $$0) {

        if ($$0 instanceof ProtectionEnchantment protectionEnchantment) {
            if (
                    (this.type == Type.ALL && protectionEnchantment.type == ProtectionEnchantment.Type.ALL) ||
                            (this.type == Type.FIRE && protectionEnchantment.type == ProtectionEnchantment.Type.FIRE) ||
                            (this.type == Type.PROJECTILE && protectionEnchantment.type == ProtectionEnchantment.Type.PROJECTILE) ||
                            (this.type == Type.EXPLOSION && protectionEnchantment.type == ProtectionEnchantment.Type.EXPLOSION)

            )
                return false;
        }

        if ($$0 instanceof BackupProjectileProtectionEnchantment $$1) {
            return this.type == $$1.type ? false : this.type == Type.FALL || $$1.type == Type.FALL;
        } else {
            return super.checkCompatibility($$0);
        }
    }

    public static int getFireAfterDampener(LivingEntity $$0, int $$1) {
        float $$2 = EnchantmentHelper.getEnchantmentLevel(CustomEnchant.BACKUP_FIRE_PROTECTION, $$0) * 0.25f;
        if ($$2 > 0) {
            $$1 -= Mth.floor((float) $$1 * $$2 * 0.15F);
        }

        return $$1;
    }

    public static double getExplosionKnockbackAfterDampener(LivingEntity $$0, double $$1) {
        double $$2 = EnchantmentHelper.getEnchantmentLevel(CustomEnchant.BACKUP_BLAST_PROTECTION, $$0) * 0.25f;
        if ($$2 > 0) {
            $$1 *= Mth.clamp(1.0 - $$2 * 0.15, 0.0, 1.0);
        }

        return $$1;
    }

    public static enum Type {
        ALL(1, 11),
        FIRE(10, 8),
        FALL(5, 6),
        EXPLOSION(5, 8),
        PROJECTILE(3, 6);

        private final int minCost;
        private final int levelCost;

        private Type(int $$0, int $$1) {
            this.minCost = $$0;
            this.levelCost = $$1;
        }

        public int getMinCost() {
            return this.minCost;
        }

        public int getLevelCost() {
            return this.levelCost;
        }
    }
}
