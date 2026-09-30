package net.mehvahdjukaar.polytone.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.mehvahdjukaar.polytone.Polytone;
import net.minecraft.client.renderer.fog.environment.PowderedSnowFogEnvironment;
import net.minecraft.util.ARGB;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PowderedSnowFogEnvironment.class)
public class PowderedSnowFogEnvironmentMixin {

    @ModifyReturnValue(method = "getBaseColor", at = @At("RETURN"))
    private Vector3fc polytone$customPowderSnowFog(Vector3fc original) {
        Integer custom = Polytone.COLORS.getPowderSnowFogColor();
        return custom != null
                ? new Vector3f(ARGB.redFloat(custom), ARGB.greenFloat(custom), ARGB.blueFloat(custom))
                : original;
    }
}
