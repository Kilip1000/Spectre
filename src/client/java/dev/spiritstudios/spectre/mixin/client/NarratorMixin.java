package dev.spiritstudios.spectre.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.text2speech.Narrator;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Narrator.class)
public interface NarratorMixin {
	@WrapOperation(method = "getNarrator", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V"), remap = false)
	private static void shortenNarratorError(Logger instance, String s, Throwable throwable, Operation<Void> original) {
		original.call(instance, s + " (Full error suppressed by Spectre)", null);
	}
}
