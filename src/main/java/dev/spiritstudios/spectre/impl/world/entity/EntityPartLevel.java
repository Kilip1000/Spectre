package dev.spiritstudios.spectre.impl.world.entity;

import dev.spiritstudios.spectre.api.world.entity.EntityPart;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public interface EntityPartLevel {
	default Int2ObjectMap<EntityPart<?>> specter$getParts() {
		throw new IllegalStateException("Spectre ServerLevelMixin injection failed!");
	}
}
