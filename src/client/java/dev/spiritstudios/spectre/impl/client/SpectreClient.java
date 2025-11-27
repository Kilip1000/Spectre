package dev.spiritstudios.spectre.impl.client;

import dev.spiritstudios.spectre.api.client.SpectreScreenshake;
import dev.spiritstudios.spectre.impl.client.world.entity.render.SpectreModelLoader;
import dev.spiritstudios.spectre.impl.client.world.entity.render.animation.AnimationLoader;
import dev.spiritstudios.spectre.api.network.ScreenshakeS2CPayload;
import dev.spiritstudios.spectre.impl.registry.MetatagContents;
import dev.spiritstudios.spectre.impl.registry.MetatagSyncS2CPayload;
import dev.spiritstudios.spectre.impl.world.item.CreativeModeTabReloader;
import dev.spiritstudios.spectre.impl.world.item.CreativeModeTabsS2CPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.SharedConstants;

public final class SpectreClient implements ClientModInitializer {
	public static final SpectreModelLoader MODEL_MANAGER = new SpectreModelLoader();
	public static final AnimationLoader ANIMATION_MANAGER = new AnimationLoader();

    @Override
    public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(
			ScreenshakeS2CPayload.TYPE,
			(payload, context) -> {
				SpectreScreenshake.addTrauma(payload.trauma());
			}
		);

		ClientPlayNetworking.registerGlobalReceiver(
			MetatagSyncS2CPayload.TYPE,
			(payload, context) -> {
				MetatagContents.apply(context.player().registryAccess(), payload.contents());
			}
		);

		ClientPlayNetworking.registerGlobalReceiver(
			CreativeModeTabsS2CPayload.TYPE,
			(payload, context) -> {
				CreativeModeTabReloader.apply(payload.tabs());
			}
		);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			SpectreScreenshake.addTrauma(-(1F / SharedConstants.TICKS_PER_SECOND) / 2F);
		});
    }
}
