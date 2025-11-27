package dev.spiritstudios.spectre.api.client.model.animation;

import dev.spiritstudios.spectre.api.core.math.Query;
import dev.spiritstudios.spectre.mixin.client.animation.AnimationStateAccessor;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.AnimationState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public class NamedAnimationState implements AnimationApplicator {
	public final String name;
	public long startTick;

	public NamedAnimationState(String name) {
		this.name = name;
	}

	public void copyFrom(AnimationState state) {
		this.startTick = ((AnimationStateAccessor) state).getStartTick();
	}

	@Override
	public void apply(Function<String, @Nullable ModelPart> lookup, Map<String, ActorAnimation> animations, Query query, float time) {
		var animation = animations.get(name);

		animation.bones().forEach((name, anim) -> {
			var part = lookup.apply(name);
			if (part != null) {
				anim.apply(
					part,
					animation,
					query,
					time - startTick,
					1F
				);
			}
		});
	}
}
