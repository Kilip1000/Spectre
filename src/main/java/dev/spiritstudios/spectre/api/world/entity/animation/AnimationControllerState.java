package dev.spiritstudios.spectre.api.world.entity.animation;

import java.util.List;

public record AnimationControllerState(
	List<String> animations,
	List<Transition> transitions,
	float transitionLength
) {
	public record Transition(AnimationControllerState state, BooleanExpression condition) {
	}
}
