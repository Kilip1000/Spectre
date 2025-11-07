package dev.spiritstudios.spectre.mixin.dfu;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import dev.spiritstudios.spectre.impl.dfu.SpectreDataFixerUpperImpl;
import net.minecraft.util.datafix.DataFixTypes;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DataFixTypes.class)
public abstract class DataFixTypesMixin {
	@WrapMethod(
		method = "update(Lcom/mojang/datafixers/DataFixer;Lcom/mojang/serialization/Dynamic;II)Lcom/mojang/serialization/Dynamic;"
	)
	private <T> Dynamic<T> update(DataFixer dataFixer, Dynamic<T> dynamic, int oldVersion, int newVersion, Operation<Dynamic<T>> original) {
		return new Dynamic<>(
			dynamic.getOps(),
			SpectreDataFixerUpperImpl.get().update(
				(DataFixTypes) (Object) this,
				original.call(dataFixer, dynamic, oldVersion, newVersion)
			)
		);
	}
}
