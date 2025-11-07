package dev.spiritstudios.spectre.impl.command;

import java.util.function.Function;
import java.util.stream.Collectors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;

import dev.spiritstudios.spectre.api.registry.MetatagKey;
import dev.spiritstudios.spectre.api.registry.SpectreRegistries;
import dev.spiritstudios.spectre.impl.registry.MetatagFile;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public final class MetatagCommand {
	public static final SuggestionProvider<CommandSourceStack> REGISTRY_SUGGESTIONS = (context, builder) ->
		SharedSuggestionProvider.suggestResource(SpectreRegistries.METATAG.stream()
			.map(key -> key.registry().location()), builder);

	public static final Function<String, SuggestionProvider<CommandSourceStack>> METATAG_SUGGESTIONS = (registryArg) -> (context, builder) -> {
		ResourceLocation registry = context.getArgument(registryArg, ResourceLocation.class);

		return SharedSuggestionProvider.suggestResource(
			SpectreRegistries.METATAG.listElements()
				.filter(key -> key.value().registry().location().equals(registry))
				.map(Holder.Reference::key)
				.map(ResourceKey::location),
			builder
		);
	};


	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("metatag")
			.requires(source -> source.hasPermission(2))
			.then(Commands.literal("dump")
				.then(Commands.argument("registry", ResourceLocationArgument.id())
					.suggests(REGISTRY_SUGGESTIONS)
					.then(Commands.argument("metatag", ResourceLocationArgument.id())
						.suggests(METATAG_SUGGESTIONS.apply("registry"))
						.executes(context -> dump(
							context,
							getMetatagFromContext(context)
						))
					)
				)
			));
	}

	private static MetatagKey<?, ?> getMetatagFromContext(CommandContext<CommandSourceStack> context) {
		ResourceLocation metatagId = context.getArgument("metatag", ResourceLocation.class);

		return SpectreRegistries.METATAG.getValue(metatagId);
	}

	private static <K, V> int dump(CommandContext<CommandSourceStack> context, MetatagKey<K, V> metatag) {
		Codec<MetatagFile<K, V>> codec = MetatagFile.resourceCodecOf(metatag);

		RegistryAccess registryManager = context.getSource().registryAccess();
		Registry<K> registry = registryManager.lookupOrThrow(metatag.registry());

		MetatagFile<K, V> resource = new MetatagFile<>(
			registry.listElements()
				.flatMap(entry -> entry.getData(metatag)
					.map(value -> new Pair<>(entry, value))
					.stream()
				)
				.collect(Collectors.toMap(
					Pair::getFirst,
					Pair::getSecond
				)),
			false
		);

		context.getSource().sendSuccess(() ->
			NbtUtils.toPrettyComponent(codec.encodeStart(registryManager.createSerializationContext(NbtOps.INSTANCE), resource)
				.getOrThrow()), true);
		return Command.SINGLE_SUCCESS;
	}
}
