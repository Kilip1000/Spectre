package dev.spiritstudios.spectre.mixin.client.animation;

import net.minecraft.world.entity.AnimationState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AnimationState.class)
public interface AnimationStateAccessor {
	@Accessor
	int getStartTick();
}
