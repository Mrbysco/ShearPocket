package com.mrbysco.shearpocket.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ShearConfig {
	public static class Common {
		public final ModConfigSpec.DoubleValue villagerReputationChance;
		public final ModConfigSpec.IntValue villagerReputationReduction;

		Common(ModConfigSpec.Builder builder) {
			//General settings
			builder.comment("General settings")
					.push("general");

			villagerReputationChance = builder
					.comment("The chance the villager will reduce your reputation for pick-pocketing")
					.defineInRange("villagerReputationChance", 0.5, 0.0, 1.0);

			villagerReputationReduction = builder
					.comment("The amount the villager will reduce your reputation for pick-pocketing")
					.defineInRange("villagerReputationReduction", 20, 0, 100);

			builder.pop();
		}
	}

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}
}
