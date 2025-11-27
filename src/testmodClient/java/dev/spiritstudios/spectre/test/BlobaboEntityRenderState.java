package dev.spiritstudios.spectre.test;

import dev.spiritstudios.spectre.api.client.model.animation.AnimationControllerRenderState;
import dev.spiritstudios.spectre.api.client.model.animation.Queryable;
import dev.spiritstudios.spectre.api.core.math.Query;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class BlobaboEntityRenderState extends LivingEntityRenderState implements Queryable {
	public final Query query = new Query();

	@Override
	public Query query() {
		return query;
	}

	public final AnimationControllerRenderState movement = new AnimationControllerRenderState();
	public final AnimationControllerRenderState antenna = new AnimationControllerRenderState();
}
