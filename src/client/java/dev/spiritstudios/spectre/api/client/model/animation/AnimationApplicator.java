package dev.spiritstudios.spectre.api.client.model.animation;

import dev.spiritstudios.spectre.api.core.math.Query;
import net.minecraft.client.model.geom.ModelPart;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public interface AnimationApplicator {
	void apply(Function<String, @Nullable ModelPart> lookup, Map<String, SpectreAnimationDefinition> animations, Query query, float time);
}
