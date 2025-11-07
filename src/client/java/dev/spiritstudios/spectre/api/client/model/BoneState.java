package dev.spiritstudios.spectre.api.client.model;

import org.joml.Vector3f;

public record BoneState(Vector3f pivot, Vector3f offset, Vector3f rotation, Vector3f scale) {
}
