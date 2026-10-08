package com.mrbysco.shearpocket.handler;

import com.mrbysco.shearpocket.ShearPocketMod;
import com.mrbysco.shearpocket.config.ShearConfig;
import com.mrbysco.shearpocket.registry.ModEnchantments;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class PickPocketHandler {

	@SubscribeEvent
	public static void onInteract(PlayerInteractEvent.EntityInteract event) {
		Entity target = event.getTarget();
		Level level = target.level();
		ItemStack stack = event.getItemStack();
		Player player = event.getEntity();
		ItemStack otherStack = player.getOffhandItem();
		if (!level.isClientSide() && target instanceof LivingEntity livingEntity && !livingEntity.is(ShearPocketMod.BLACKLIST)) {
			if (EnchantmentHelper.has(stack, ModEnchantments.PICK_POCKETING.get())) {
				handlePickpocket(stack, EquipmentSlot.MAINHAND, livingEntity, player);
			}
			if (EnchantmentHelper.has(otherStack, ModEnchantments.PICK_POCKETING.get())) {
				handlePickpocket(otherStack, EquipmentSlot.OFFHAND, livingEntity, player);
			}
		}
	}

	private static void handlePickpocket(ItemStack shearStack, EquipmentSlot usedHand, LivingEntity target, Player player) {
		if (!target.getMainHandItem().isEmpty()) {
			dropItem(player, target, InteractionHand.MAIN_HAND);
		} else if (!target.getOffhandItem().isEmpty()) {
			dropItem(player, target, InteractionHand.OFF_HAND);
		} else {
			return;
		}

		shearStack.hurtAndBreak(1, player, usedHand);
	}

	private static void dropItem(Player player, LivingEntity target, InteractionHand hand) {
		ItemStack stack = target.getItemInHand(hand);
		ItemEntity droppedItem = new ItemEntity(target.level(), target.getX(), target.getY(), target.getZ(), stack);
		droppedItem.setPickUpDelay(40);

		Vec3 targetEye = target.getEyePosition();
		Vec3 playerEye = player.getEyePosition();
		Vec3 direction = playerEye.subtract(targetEye).normalize();
		Vec3 deltaMovement = direction.scale(0.35).add(0, 0.15, 0);
		droppedItem.setDeltaMovement(deltaMovement);
		droppedItem.syncVelocity = true;

		target.level().addFreshEntity(droppedItem);
		target.setItemInHand(hand, ItemStack.EMPTY);

		target.level().playSound(null, target.blockPosition(), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);

		if (target instanceof Villager villager && player.getRandom().nextDouble() <= ShearConfig.COMMON.villagerReputationChance.get()) {
			villager.getGossips().add(player.getUUID(), GossipType.MINOR_NEGATIVE, ShearConfig.COMMON.villagerReputationReduction.getAsInt());
		}
	}
}
