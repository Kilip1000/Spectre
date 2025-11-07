package dev.spiritstudios.spectre.api.client.model;

import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

public class Bone {
	public final String name;

	public final List<Bone> children = new ArrayList<>(0);
	public final List<SpectreCuboid> cuboids;

	public final Vector3fc rotation;
	public final Vector3fc pivot;

	public Bone(String name, List<SpectreCuboid> cuboids, Vector3fc pivot, Vector3fc rotation) {
		this.name = name;

		this.cuboids = cuboids;
		this.pivot = pivot;
		this.rotation = rotation;
	}

}
