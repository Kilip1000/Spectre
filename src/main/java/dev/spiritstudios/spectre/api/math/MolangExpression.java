package dev.spiritstudios.spectre.api.math;

@FunctionalInterface
public interface MolangExpression {
	float evaluate(MolangContext context);
}
