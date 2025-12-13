package dev.spiritstudios.spectre.impl.client.pond;

import dev.spiritstudios.spectre.api.client.model.animation.EntityAnimationSet;

import java.util.function.Supplier;

public interface SpectreInternalEntityRenderDispatcher {
	void spectre$setEntityAnimations(Supplier<EntityAnimationSet> entityAnimations);
}
