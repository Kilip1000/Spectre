package dev.spiritstudios.spectre.api.client.model;

import java.util.List;
import java.util.Map;

public record SpectreModel(Map<String, Bone> namedBones, List<Bone> rootBones) {
}
