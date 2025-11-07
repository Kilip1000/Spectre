package dev.spiritstudios.spectre.mixin.client.world.entity;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.spectre.api.world.entity.EntityPart;
import dev.spiritstudios.spectre.api.world.entity.PartHolder;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	@Inject(method = "extractAdditionalHitboxes(Lnet/minecraft/world/entity/LivingEntity;Lcom/google/common/collect/ImmutableList$Builder;F)V", at = @At("HEAD"))
	private void appendHitboxes(LivingEntity entity, ImmutableList.Builder<HitboxRenderState> builder, float tickProgress, CallbackInfo ci) {
		if (entity instanceof PartHolder<?> partHolder) {
			for (EntityPart<?> part : partHolder.getParts()) {
				AABB box = part.getBoundingBox();
				builder.add(new HitboxRenderState(
					box.minX - entity.getX(),
					box.minY - entity.getY(),
					box.minZ - entity.getZ(),
					box.maxX - entity.getX(),
					box.maxY - entity.getY(),
					box.maxZ - entity.getZ(),
					1.0F,
					1.0F,
					1.0F
				));
			}
		}
	}
}
