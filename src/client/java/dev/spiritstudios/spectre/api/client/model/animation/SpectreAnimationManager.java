package dev.spiritstudios.spectre.api.client.model.animation;

import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import dev.spiritstudios.spectre.api.client.model.serial.ActorAnimation;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class SpectreAnimationManager extends SimplePreparableReloadListener<Map<String, ActorAnimation>> {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final FileToIdConverter FINDER = new FileToIdConverter("spectre/animations", ".animation.json");

	public final Map<String, ActorAnimation> animations = new Object2ObjectOpenHashMap<>();

	@Override
	protected Map<String, ActorAnimation> prepare(ResourceManager manager, ProfilerFiller profiler) {
		var resources = FINDER.listMatchingResources(manager);

		var results = new HashMap<String, ActorAnimation>();

		for (Map.Entry<ResourceLocation, Resource> resource : resources.entrySet()) {
			try (Reader reader = resource.getValue().openAsReader()) {
				var element = StrictJsonParser.parse(reader);
				var map = JsonOps.INSTANCE.getMap(element).getOrThrow();
				var animations = JsonOps.INSTANCE.getMap(map.get("animations")).getOrThrow();
				animations.entries()
					.forEach(entry -> {
						var name = JsonOps.INSTANCE.getStringValue(entry.getFirst()).getOrThrow();
						ActorAnimation.CODEC.parse(
								JsonOps.INSTANCE,
								entry.getSecond()
							)
							.ifSuccess(anim -> results.put(name, anim))
							.ifError(error -> LOGGER.error("Couldn't parse data file '{}': {}", resource.getKey(), error));
					});


			} catch (IllegalArgumentException | IOException | JsonParseException error) {
				LOGGER.error("Couldn't parse data file '{}': {}", resource.getKey(), error);
			}
		}

		return results;
	}

	@Override
	protected void apply(Map<String, ActorAnimation> prepared, ResourceManager manager, ProfilerFiller profiler) {
		animations.clear();
		animations.putAll(prepared);
	}

}
