package dev.spiritstudios.spectre.mixin.world.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.spiritstudios.spectre.api.world.entity.EntityPart;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin {
	@WrapMethod(method = "addEntity")
	private void loadEntity(Entity entity, Operation<Void> original) {
		if (entity instanceof EntityPart<?>) return;

		original.call(entity);
	}
}
