package dev.spiritstudios.spectre.mixin.world.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends LevelMixin {
	@ModifyReturnValue(method = "getEntityOrPart", at = @At("RETURN"))
	private Entity getEntityOrPart(Entity original, @Local(argsOnly = true) int id) {
		return original != null ? original : this.specter$parts.get(id);
	}
}
