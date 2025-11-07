package dev.spiritstudios.spectre.api.client.model.animation;

import com.mojang.serialization.Codec;
import dev.spiritstudios.spectre.api.math.MolangContext;
import dev.spiritstudios.spectre.api.math.MolangExpression;
import dev.spiritstudios.spectre.api.serialization.SpectreCodecs;
import org.joml.Vector3f;

import java.util.List;

@FunctionalInterface
public interface Vector3fExpression {
	Vector3f evaluate(MolangContext context);

	Codec<Vector3fExpression> CODEC = Codec.withAlternative(
		SpectreCodecs.MOLANG.listOf(3, 3).xmap(
			Vector3fExpression::of,
			exp -> {
				throw new UnsupportedOperationException("Cannot encode Vector3fExpression");
			}
		),
		SpectreCodecs.MOLANG.xmap(
			Vector3fExpression::of,
			exp -> {
				throw new UnsupportedOperationException("Cannot encode Vector3fExpression");
			}
		)
	);

	static Vector3fExpression of(MolangExpression expression) {
		return context -> new Vector3f(expression.evaluate(context));
	}

	static Vector3fExpression of(List<MolangExpression> list) {
		var x = list.get(0);
		var y = list.get(1);
		var z = list.get(2);

		return context -> new Vector3f(
			x.evaluate(context),
			y.evaluate(context),
			z.evaluate(context)
		);
	}
}
