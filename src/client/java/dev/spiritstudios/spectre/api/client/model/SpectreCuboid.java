package dev.spiritstudios.spectre.api.client.model;

import net.minecraft.core.Direction;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record SpectreCuboid(
	Quad[] quads,
	Vector3fc pivot,
	Vector3fc scale,
	float inflate,
	boolean mirror
) {
	public record Quad(
		Vertex[] vertices,
		Vector3fc normal,
		Direction direction
	) {

	}

	public record Vertex(
		Vector3f pos,
		float u, float v
	) {

	}
}
