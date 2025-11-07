package dev.spiritstudios.spectre.api.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;

public final class SpectreRegistries {
	public static final Registry<MetatagKey<?, ?>> METATAG = FabricRegistryBuilder
		.createSimple(SpectreRegistryKeys.METATAG)
		.buildAndRegister();
}
