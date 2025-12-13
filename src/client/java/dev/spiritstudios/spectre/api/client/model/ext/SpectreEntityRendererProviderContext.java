package dev.spiritstudios.spectre.api.client.model.ext;

import dev.spiritstudios.spectre.api.client.model.animation.AnimationLocation;
import dev.spiritstudios.spectre.api.client.model.animation.EntityAnimationSet;
import dev.spiritstudios.spectre.api.client.model.animation.SpectreKeyframeAnimation;
import net.minecraft.client.model.geom.ModelPart;

/**
 * This is an injected interface, don't use it directly
 */
public interface SpectreEntityRendererProviderContext {
	default EntityAnimationSet getAnimationSet() {
		throw new IllegalStateException("Implemented via mixin.");
	}

	default SpectreKeyframeAnimation bakeAnimation(AnimationLocation location, ModelPart part) {
		return getAnimationSet().bake(location, part);
	}
}
