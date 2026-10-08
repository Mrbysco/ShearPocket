package com.mrbysco.shearpocket.datagen;

import com.mrbysco.shearpocket.ShearPocketMod;
import com.mrbysco.shearpocket.registry.ModEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.locale.Language;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.event.GatherDataRegistryEntriesEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class ModDatagen {
	@SubscribeEvent
	static void onGatherRegistries(GatherDataRegistryEntriesEvent event) {
		event.gatherFor(ShearPocketMod.MOD_ID)
				.add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		event.createProvider(ModLanguageProvider::new);
		event.createProvider(ModEnchantmentTagProvider::new);
		event.createProvider(ModEntityTypeTagsProvider::new);
	}

	private static class ModEnchantmentTagProvider extends EnchantmentTagsProvider {
		public ModEnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(output, lookupProvider, ShearPocketMod.MOD_ID);
		}

		@Override
		protected void addTags(HolderLookup.Provider pProvider) {
			tag(EnchantmentTags.TREASURE).add(ModEnchantments.PICK_POCKET);
		}
	}

	private static class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
		public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(output, lookupProvider, ShearPocketMod.MOD_ID);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			tag(ShearPocketMod.BLACKLIST);
		}
	}

	private static class ModLanguageProvider extends LanguageProvider {
		public ModLanguageProvider(PackOutput gen) {
			super(gen, ShearPocketMod.MOD_ID, Language.DEFAULT);
		}

		@Override
		protected void addTranslations() {
			addEnchantment(ModEnchantments.PICK_POCKET, "Pick-Pocket");
			addEnchantmentDescription(ModEnchantments.PICK_POCKET, "Allows disarming targets");
		}

		private void addEnchantment(ResourceKey<Enchantment> key, String name) {
			Identifier location = key.identifier();
			add("enchantment." + location.getNamespace() + "." + location.getPath(), name);
		}

		private void addEnchantmentDescription(ResourceKey<Enchantment> key, String description) {
			Identifier location = key.identifier();
			add("enchantment." + location.getNamespace() + "." + location.getPath() + ".desc", description);
		}
	}

}
