package dev.spiritstudios.spectre.api.client.model.animation;

import com.google.common.collect.Streams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;
import java.util.Optional;

import dev.spiritstudios.spectre.api.core.math.Query;
import net.minecraft.SharedConstants;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public record SpectreBoneAnimation(
	String bone,
	SpectreTransformation positionKeyframes,
	SpectreTransformation rotationKeyframes,
	SpectreTransformation scaleKeyframes
) {
	public float lastKeyframe() {
		return Streams.concat(
			Optional.ofNullable(positionKeyframes).stream().flatMap(trans -> Arrays.stream(trans.keyframes())),
			Optional.ofNullable(rotationKeyframes).stream().flatMap(trans -> Arrays.stream(trans.keyframes())),
			Optional.ofNullable(scaleKeyframes).stream().flatMap(trans -> Arrays.stream(trans.keyframes()))
		).reduce(
			(a, b) -> a.timestamp() > b.timestamp() ? a : b
		).map(SpectreKeyframe::timestamp).orElse(0F);
	}

	public static Codec<SpectreBoneAnimation> codec(String key) {
		return RecordCodecBuilder.create(instance -> instance.group(
			SpectreTransformation.CODEC_SIXTEENTH.optionalFieldOf("position", SpectreTransformation.EMPTY).forGetter(SpectreBoneAnimation::positionKeyframes),
			SpectreTransformation.CODEC.optionalFieldOf("rotation", SpectreTransformation.EMPTY).forGetter(SpectreBoneAnimation::rotationKeyframes),
			SpectreTransformation.CODEC_SIXTEENTH.optionalFieldOf("scale", SpectreTransformation.EMPTY).forGetter(SpectreBoneAnimation::scaleKeyframes)
		).apply(instance, (pos, rot, sca) -> new SpectreBoneAnimation(key, pos, rot, sca)));
	}

	private float getRunningSeconds(float startTick, LoopType loopType, float length) {
		float seconds =  (startTick / SharedConstants.TICKS_PER_SECOND);
		return loopType == LoopType.TRUE ? seconds % length : seconds;
	}

	public void apply(
		ModelPart part,
		ActorAnimation animation,
		Query query,
		float runningTicks,
		float scale
	) {
		float seconds = this.getRunningSeconds(runningTicks, animation.loop(), animation.length());

		part.resetPose();

		part.offsetPos(
			positionKeyframes.evaluate(
				query,
				animation.loop(),
				seconds,
				scale
			)
		);


		part.offsetRotation(
			rotationKeyframes.evaluate(
				query,
				animation.loop(),
				seconds,
				scale * Mth.DEG_TO_RAD
			)
		);

		part.offsetScale(
			scaleKeyframes.evaluate(
				query,
				animation.loop(),
				seconds,
				scale
			)
		);
	}
}
