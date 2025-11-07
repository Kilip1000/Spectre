package dev.spiritstudios.spectre.api.client.model;

import dev.spiritstudios.spectre.api.client.model.serial.GeoJson;
import dev.spiritstudios.spectre.api.client.model.serial.ModelBone;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpectreModelManager extends SimpleJsonResourceReloadListener<GeoJson> {
	public static final FileToIdConverter LISTER = new FileToIdConverter("spectre/models", ".geo.json");

	public SpectreModelManager() {
		super(GeoJson.CODEC, LISTER);
	}

	public final Map<String, SpectreModel> models = new Object2ObjectOpenHashMap<>();

	@Override
	protected void apply(Map<ResourceLocation, GeoJson> prepared, ResourceManager manager, ProfilerFiller profiler) {
		models.clear();

		prepared.values()
			.stream()
			.flatMap(geoJson -> geoJson.geometry().stream())
			.forEach(geometry -> {
				var nameToBone = geometry.bones()
					.stream()
					.collect(Collectors.toMap(
						ModelBone::name,
						Function.identity()
					));

				var bones = new Object2ObjectOpenHashMap<String, Bone>();
				var root = new ArrayList<Bone>();
				for (ModelBone bone : nameToBone.values()) {
					var cuboids = bone.cubes()
						.stream()
						.map(cube -> cube.bake(bone, geometry.description().textureWidth(), geometry.description().textureHeight()))
						.toList();

					bones.put(bone.name(), new Bone(
						bone.name(),
						cuboids,
						bone.pivot().mul(-1F, 1F, 1F, new Vector3f()),
						bone.rotation()
							.mul(Mth.DEG_TO_RAD)
					));
				}

				for (ModelBone bone : nameToBone.values()) {
					if (bone.parent().isPresent()) {
						var parent = bones.get(bone.parent().get());
						var child = bones.get(bone.name());

						parent.children.add(child);
					} else {
						root.add(bones.get(bone.name()));
					}
				}

				models.put(
					geometry.description().id(),
					new SpectreModel(
						bones,
						root
					)
				);
			});

	}
}
