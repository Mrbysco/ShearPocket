package com.mrbysco.shearpocket;

import com.mojang.logging.LogUtils;
import com.mrbysco.shearpocket.config.ShearConfig;
import com.mrbysco.shearpocket.registry.ModEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(ShearPocketMod.MOD_ID)
public class ShearPocketMod {
	public static final String MOD_ID = "shearpocket";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static TagKey<EntityType<?>> BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "blacklist"));

	public ShearPocketMod(IEventBus eventBus, Dist dist, ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, ShearConfig.commonSpec);

		ModEnchantments.ENCHANTMENT_EFFECT_REGISTER.register(eventBus);

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}
}
