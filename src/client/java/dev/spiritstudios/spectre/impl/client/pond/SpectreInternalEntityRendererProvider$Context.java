package dev.spiritstudios.spectre.impl.client.pond;

import dev.spiritstudios.spectre.api.client.model.animation.EntityAnimationSet;

public interface SpectreInternalEntityRendererProvider$Context {
	default void spectre$setAnimationSet(EntityAnimationSet animationSet) {
		throw new IllegalStateException("Implemented via mixin.");
	}
}
