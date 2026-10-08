package com.mrbysco.shearpocket.registry;

import com.mrbysco.shearpocket.ShearPocketMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModEnchantments {
	public static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_EFFECT_REGISTER = DeferredRegister.create(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, ShearPocketMod.MOD_ID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Unit>> PICK_POCKETING = register("pick_pocketing", builder -> builder.persistent(Unit.CODEC));

	private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String path, UnaryOperator<DataComponentType.Builder<T>> pOperator) {
		return ENCHANTMENT_EFFECT_REGISTER.register(path, () -> pOperator.apply(DataComponentType.builder()).build());
	}

	public static final ResourceKey<Enchantment> PICK_POCKET = key("pick_pocket");

	public static void bootstrap(BootstrapContext<Enchantment> context) {
		HolderGetter<Item> itemHolderGetter = context.lookup(Registries.ITEM);

		register(
				context,
				PICK_POCKET,
				Enchantment.enchantment(
								Enchantment.definition(
										itemHolderGetter.getOrThrow(Tags.Items.TOOLS_SHEAR),
										2, 1,
										Enchantment.dynamicCost(20, 25),
										Enchantment.dynamicCost(65, 25),
										4,
										EquipmentSlotGroup.HAND)
						)
						.withEffect(PICK_POCKETING.get())
		);
	}

	private static ResourceKey<Enchantment> key(String path) {
		return ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(ShearPocketMod.MOD_ID, path));
	}

	private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> resourceKey,
	                             Enchantment.Builder builder) {
		context.register(resourceKey, builder.build(resourceKey.identifier()));
	}
}
