package dev.spiritstudios.spectre.impl.dfu;

import java.util.Map;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;
import dev.spiritstudios.spectre.impl.Spectre;
import dev.spiritstudios.spectre.mixin.dfu.DataFixTypesAccessor;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;

public final class SpectreDataFixerUpperImpl {
	private static SpectreDataFixerUpperImpl INSTANCE;

	private final Schema vanillaSchema;
	private final Map<String, VersionedDataFixer> dataFixers = new Object2ObjectOpenHashMap<>();

	public SpectreDataFixerUpperImpl(Schema vanillaSchema) {
		this.vanillaSchema = vanillaSchema;
	}

	public static SpectreDataFixerUpperImpl get() {
		if (INSTANCE != null) return INSTANCE;

		Schema vanillaSchema = DataFixers.getDataFixer()
			.getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().dataVersion().version()));

		INSTANCE = new SpectreDataFixerUpperImpl(vanillaSchema);
		return INSTANCE;
	}

	public Schema createRootSchema() {
		return new Schema(0, vanillaSchema);
	}

	public void register(
		String modId,
		int currentDataVersion,
		DataFixer dataFixer
	) {
		if (dataFixers.containsKey(modId)) {
			throw new IllegalArgumentException("Mod with id %s attempted to register multiple DataFixers".formatted(modId));
		}

		dataFixers.put(modId, new VersionedDataFixer(dataFixer, currentDataVersion));
	}

	public <T> T update(
		DataFixTypes types,
		Dynamic<T> dynamic
	) {
		OptionalDynamic<T> specterDataVersions = dynamic.get("SpecterDataVersions");

		for (Map.Entry<String, VersionedDataFixer> entry : dataFixers.entrySet()) {
			String modId = entry.getKey();
			VersionedDataFixer value = entry.getValue();

			int dataVersion = specterDataVersions.get(modId).asInt(0);

			// Things can very easily go wrong on datafix, so I'm leaving this here in the logs
			// so we can see if a datafix has happened if a user's world gets corrupted
			if (dataVersion != value.currentDataVersion()) {
				Spectre.LOGGER.info(
					"Running data fixer for {} from data version {} -> {}",
					modId,
					dataVersion,
					value.currentDataVersion()
				);
			}

			dynamic = value.dataFixer().update(
				((DataFixTypesAccessor) (Object) types).getType(),
				dynamic,
				dataVersion, value.currentDataVersion()
			);
		}

		return dynamic.getValue();
	}

	public CompoundTag addCurrentDataVersion(CompoundTag compound) {
		CompoundTag dataVersions = new CompoundTag();
		dataFixers.forEach((key, value) -> {
			dataVersions.putInt(key, value.currentDataVersion());
		});

		compound.put("SpecterDataVersions", dataVersions);
		return compound;
	}

	record VersionedDataFixer(DataFixer dataFixer, int currentDataVersion) {
	}
}
