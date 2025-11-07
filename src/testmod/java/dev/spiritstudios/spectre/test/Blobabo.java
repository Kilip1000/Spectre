package dev.spiritstudios.spectre.test;

import dev.spiritstudios.spectre.api.world.entity.EntityPart;
import dev.spiritstudios.spectre.api.world.entity.PartHolder;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Blobabo extends PathfinderMob implements PartHolder<Blobabo> {
	private final List<BlobaboPart> parts = List.of(new BlobaboPart(
		this,
		EntityDimensions.scalable(2f, 1f),
		new Vec3(0, 1, 0)
	));

	public final AnimationState animationState = new AnimationState();

	public Blobabo(EntityType<? extends PathfinderMob> entityType, Level world) {
		super(entityType, world);

		animationState.start(this.tickCount);
	}

	@Override
	public List<? extends EntityPart<Blobabo>> getParts() {
		return parts;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		parts.getFirst().setPos(parts.getFirst().relativePos.add(this.position()));
	}
}
