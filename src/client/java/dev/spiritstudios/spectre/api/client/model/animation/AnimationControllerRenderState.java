package dev.spiritstudios.spectre.api.client.model.animation;

import dev.spiritstudios.spectre.api.core.math.Query;
import dev.spiritstudios.spectre.api.world.entity.animation.AnimationController;
import dev.spiritstudios.spectre.api.world.entity.animation.AnimationControllerState;
import net.minecraft.SharedConstants;
import net.minecraft.client.model.geom.ModelPart;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public class AnimationControllerRenderState implements AnimationApplicator {
	public long prevStartTick;
	public @Nullable AnimationControllerState previousState;
	public AnimationControllerState state;
	public long transitionStartTick;

	public void copyFrom(AnimationController controller) {
		this.previousState = controller.getPreviousState();
		this.state = controller.getState();
		this.transitionStartTick = controller.getAnimStartTick();
		this.prevStartTick = controller.getPrevStartTick();
	}

	@Override
	public void apply(Function<String, @Nullable ModelPart> lookup, Map<String, ActorAnimation> animations, Query query, float time) {
		float stateTime = (time - transitionStartTick);
		float transitionProgress = previousState == null ? 1F :
			Math.min(stateTime / (previousState.transitionLength() * SharedConstants.TICKS_PER_SECOND), 1F);

		if (transitionProgress != 1f) {
			for (String animationName : previousState.animations()) {
				var animation = animations.get(animationName);

				if (animation == null) return;

				animation.bones().forEach((name, anim) -> {
					var part = lookup.apply(name);
					if (part != null) {
						anim.apply(
							part,
							animation,
							query,
							time - prevStartTick,
							1F - transitionProgress
						);
					}
				});
			}
		}

		for (String animationName : state.animations()) {
			var animation = animations.get(animationName);

			if (animation == null) return;

			animation.bones().forEach((name, anim) -> {
				var part = lookup.apply(name);
				if (part != null) {
					anim.apply(
						part,
						animation,
						query,
						time - transitionStartTick,
						transitionProgress
					);
				}
			});
		}
	}
}
