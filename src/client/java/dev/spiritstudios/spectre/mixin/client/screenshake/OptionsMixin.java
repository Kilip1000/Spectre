package dev.spiritstudios.spectre.mixin.client.screenshake;


import dev.spiritstudios.spectre.impl.client.SpectreAccessibilityOptions;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public abstract class OptionsMixin {
	@Inject(method = "processOptions", at = @At("TAIL"))
	private void addScreenshakeIntensity(Options.FieldAccess accessor, CallbackInfo ci) {
		accessor.process("spectre_screenshakeIntensity", SpectreAccessibilityOptions.SCREENSHAKE_INTENSITY);
	}
}
