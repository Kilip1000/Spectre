package dev.spiritstudios.spectre.impl.client;

import dev.spiritstudios.spectre.api.client.SpectreScreenshake;
import dev.spiritstudios.spectre.api.client.model.SpectreModelManager;
import dev.spiritstudios.spectre.api.client.model.animation.SpectreAnimationManager;
import dev.spiritstudios.spectre.api.network.ScreenshakeS2CPayload;
import dev.spiritstudios.spectre.impl.Spectre;
import dev.spiritstudios.spectre.impl.world.item.CreativeModeTabReloader;
import dev.spiritstudios.spectre.impl.world.item.CreativeModeTabsS2CPayload;
import dev.spiritstudios.spectre.impl.registry.MetatagContents;
import dev.spiritstudios.spectre.impl.registry.MetatagSyncS2CPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.player.Player;

public final class SpectreClient implements ClientModInitializer {
	public static final SpectreModelManager MODEL_MANAGER = new SpectreModelManager();
	public static final SpectreAnimationManager ANIMATION_MANAGER = new SpectreAnimationManager();

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

		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(
			Spectre.id("geo_model"),
			MODEL_MANAGER
		);

		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(
			Spectre.id("animations"),
			ANIMATION_MANAGER
		);
    }
}
