package dev.spiritstudios.spectre.mixin.debug;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// This is done as a mixin since there are many references to it before mod initialization
@Mixin(SharedConstants.class)
public abstract class SharedConstantsMixin {
	@Shadow
	public static boolean IS_RUNNING_IN_IDE;

	@Inject(method = "<clinit>", at = @At("HEAD"))
	private static void init(CallbackInfo ci) {
		// |= in case another mod has enabled it
		IS_RUNNING_IN_IDE |= FabricLoader.getInstance().isDevelopmentEnvironment();
	}
}
