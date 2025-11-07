package dev.spiritstudios.spectre.test;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class SpectreTestmodClient implements ClientModInitializer {


	@Override
	public void onInitializeClient() {
		EntityRenderers.register(
			SpectreTestmod.BLOBABO,
			(ctx) -> new BlobaboRenderer(ctx, 1f)
		);


	}
}
