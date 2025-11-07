package dev.spiritstudios.spectre.mixin.registry.unfreeze;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.core.MappedRegistry;

@Mixin(MappedRegistry.class)
public interface MappedRegistryAccessor {
	@Accessor
	void setFrozen(boolean value);
}
