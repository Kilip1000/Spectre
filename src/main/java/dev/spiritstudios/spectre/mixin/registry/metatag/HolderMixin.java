package dev.spiritstudios.spectre.mixin.registry.metatag;

import dev.spiritstudios.spectre.api.registry.MetatagHolder;
import net.minecraft.core.Holder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Holder.class)
public interface HolderMixin<T> extends MetatagHolder<T> {

}
