package dev.spiritstudios.spectre.api.client.model.animation;

import com.google.common.collect.Streams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.spiritstudios.spectre.api.client.model.Bone;
import dev.spiritstudios.spectre.api.client.model.BoneState;
import dev.spiritstudios.spectre.api.client.model.serial.ActorAnimation;
import dev.spiritstudios.spectre.api.math.MolangContext;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;

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
			SpectreTransformation.CODEC.optionalFieldOf("position", SpectreTransformation.EMPTY).forGetter(SpectreBoneAnimation::positionKeyframes),
			SpectreTransformation.CODEC.optionalFieldOf("rotation", SpectreTransformation.EMPTY).forGetter(SpectreBoneAnimation::rotationKeyframes),
			SpectreTransformation.CODEC.optionalFieldOf("scale", SpectreTransformation.EMPTY).forGetter(SpectreBoneAnimation::scaleKeyframes)
		).apply(instance, (pos, rot, sca) -> new SpectreBoneAnimation(key, pos, rot, sca)));
	}

	public void update(BoneState state, Bone bone, ActorAnimation animation, MolangContext context, AnimationState animationState, float age) {
		this.update(state, bone, animation, context, animationState, age, 1.0F);
	}

	public void update(
		BoneState boneState,
		Bone bone,
		ActorAnimation animation,
		MolangContext context,
		AnimationState animationState,
		float age,
		float speedMultiplier
	) {
		animationState.ifStarted(state -> this.update(boneState, bone, animation, context, (long) ((float) state.getTimeInMillis(age) * speedMultiplier), 1.0F));
	}

	private float getRunningSeconds(long timeInMilliseconds, LoopType loopType, float length) {
		float f = (float) timeInMilliseconds / 1000.0F;
		return loopType == LoopType.TRUE ? f % length : f;
	}

	public void update(
		BoneState state,
		Bone bone,
		ActorAnimation animation,
		MolangContext context,
		long timeInMilliseconds,
		float scale
	) {
		float seconds = this.getRunningSeconds(timeInMilliseconds, animation.loop(), animation.length());

		state.offset().set(0F);
		state.pivot().set(bone.pivot);
		state.rotation().set(bone.rotation);
		state.scale().set(1F);

		positionKeyframes.apply(
			context,
			animation.loop(),
			seconds,
			scale,
			state.offset()
		);


		rotationKeyframes.apply(
			context,
			animation.loop(),
			seconds,
			Mth.DEG_TO_RAD * scale,
			state.rotation()
		);


		scaleKeyframes.apply(
			context,
			animation.loop(),
			seconds,
			scale,
			state.scale()
		);
	}
}
