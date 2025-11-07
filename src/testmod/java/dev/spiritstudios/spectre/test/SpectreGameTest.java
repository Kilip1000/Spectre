package dev.spiritstudios.spectre.test;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;
import java.util.Optional;

@SuppressWarnings("unused")
public final class SpectreGameTest {
	@GameTest
	public void testMetatagGet(GameTestHelper context) {
		context.assertValueEqual(
			Optional.of(6969),
			Blocks.DIORITE.getData(SpectreTestmod.TEST_METATAG),
			Component.nullToEmpty("Metatag value")
		);

		context.succeed();
	}
}
