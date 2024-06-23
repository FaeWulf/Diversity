package faewulf.diversity.mixin.client.noLevelLimitAnvil;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.player.PlayerAbilities;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AnvilScreen.class)
public abstract class AnvilMixin {
    @Redirect(method = "drawForeground", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z", opcode = Opcodes.GETFIELD))
    private boolean drawForeGroundRedirect(PlayerAbilities playerAbilities) {
        return playerAbilities.creativeMode || ModConfigs.no_level_limit_anvil;
    }
}
