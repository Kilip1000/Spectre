package dev.spiritstudios.spectre.test;

import dev.spiritstudios.spectre.api.world.entity.EntityPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BlobaboPart extends EntityPart<Blobabo> {
	public BlobaboPart(Blobabo owner, EntityDimensions dimensions, Vec3 offset) {
		super(owner, dimensions);

		this.relativePos = offset;
		this.makeBoundingBox();
	}

	@Override
	public boolean canBeCollidedWith(@Nullable Entity entity) {
		return false;
	}
}
