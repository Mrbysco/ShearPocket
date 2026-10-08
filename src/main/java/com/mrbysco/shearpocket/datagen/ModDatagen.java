package com.mrbysco.shearpocket.datagen;

import com.mrbysco.shearpocket.ShearPocketMod;
import com.mrbysco.shearpocket.registry.ModEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ModDatagen {
	private final static RegistrySetBuilder REGISTRY = new RegistrySetBuilder()
		.add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		event.createDatapackRegistryObjects(REGISTRY);
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		event.createProvider(ModLanguageProvider::new);

		generator.addProvider(event.includeServer(), new ModEnchantmentTagProvider(packOutput, lookupProvider, helper));
		generator.addProvider(event.includeServer(), new ModEntityTypeTagsProvider(packOutput, lookupProvider, helper));
	}

	private static class ModEnchantmentTagProvider extends EnchantmentTagsProvider {
		public ModEnchantmentTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
			super(output, lookupProvider, ShearPocketMod.MOD_ID, fileHelper);
		}

		@Override
		protected void addTags(HolderLookup.Provider pProvider) {
			tag(EnchantmentTags.TREASURE).add(ModEnchantments.PICK_POCKET);
		}
	}

	private static class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
		public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
			super(output, lookupProvider, ShearPocketMod.MOD_ID, fileHelper);
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
			ResourceLocation location = key.location();
			add("enchantment." + location.getNamespace() + "." + location.getPath(), name);
		}

		private void addEnchantmentDescription(ResourceKey<Enchantment> key, String description) {
			ResourceLocation location = key.location();
			add("enchantment." + location.getNamespace() + "." + location.getPath() + ".desc", description);
		}
	}

}
