package dev.spiritstudios.spectre.test;

import dev.spiritstudios.spectre.api.core.math.Query;
import net.minecraft.client.renderer.entity.state.DisplayEntityRenderState;

public class EntityDisplayRenderState extends DisplayEntityRenderState {
	public final Query query = new Query();
	public EntityDisplay.RenderState state;

	@Override
	public boolean hasSubState() {
		return this.state != null;
	}
}
