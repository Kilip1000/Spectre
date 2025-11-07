package dev.spiritstudios.spectre.mixin.dfu;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.spiritstudios.spectre.impl.dfu.SpectreDataFixerUpperImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NbtUtils.class)
public abstract class NbtUtilsMixin {
	@ModifyReturnValue(method = "addCurrentDataVersion(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;", at = @At("RETURN"))
	private static CompoundTag putDataVersion(CompoundTag original) {
		return SpectreDataFixerUpperImpl.get().addCurrentDataVersion(original);
	}
}
