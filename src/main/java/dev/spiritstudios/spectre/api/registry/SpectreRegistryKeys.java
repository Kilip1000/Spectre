package dev.spiritstudios.spectre.api.registry;

import dev.spiritstudios.spectre.impl.Spectre;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public final class SpectreRegistryKeys {
	public static final ResourceKey<Registry<MetatagKey<?, ?>>> METATAG = of("metatag");

	private static <T> ResourceKey<Registry<T>> of(String id) {
		return ResourceKey.createRegistryKey(Spectre.id(id));
	}
}
