package dev.spiritstudios.spectre.api.world.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EntityPart<T extends Entity> extends Entity {
	protected final T owner;
	protected EntityDimensions dimensions;
	protected Vec3 prevRelativePos = new Vec3(0, 0, 0);
	public Vec3 relativePos = new Vec3(0, 0, 0);

	public EntityPart(T owner, EntityDimensions dimensions) {
		super(owner.getType(), owner.level());
		this.owner = owner;
		this.dimensions = dimensions;
		this.refreshDimensions();
	}

	public void setRelativePos(Vec3 relativePos) {
		this.prevRelativePos = this.relativePos;
		this.relativePos = relativePos;
		this.setPos(this.owner.position().add(relativePos));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	@Override
	protected void readAdditionalSaveData(ValueInput view) {

	}

	@Override
	protected void addAdditionalSaveData(ValueOutput view) {

	}


	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	protected @NotNull AABB makeBoundingBox(Vec3 pos) {
		return this.dimensions == null ? super.makeBoundingBox(pos) : this.dimensions.makeBoundingBox(pos);
	}

	@Override
	public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
		return this.owner.hurtServer(world, source, amount);
	}

	@Override
	public boolean is(Entity entity) {
		return this == entity || this.owner == entity;
	}

	@Override
	public @Nullable ItemStack getPickResult() {
		return this.owner.getPickResult();
	}

	@Override
	public boolean shouldBeSaved() {
		return false;
	}

	@Override
	public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entityTrackerEntry) {
		throw new UnsupportedOperationException();
	}

	public T getOwner() {
		return owner;
	}

	@Override
	public @NotNull EntityDimensions getDimensions(Pose pose) {
		return this.dimensions;
	}
}
