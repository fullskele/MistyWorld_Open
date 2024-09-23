package ru.liahim.mist.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.vecmath.Vector2f;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import ru.liahim.mist.api.item.MistItems;
import ru.liahim.mist.api.registry.MistRegistry;
import ru.liahim.mist.capability.handler.ISkillCapaHandler.Skill;
import ru.liahim.mist.common.Mist;
import ru.liahim.mist.common.MistTime;
import ru.liahim.mist.tileentity.TileEntityCampfire;
import ru.liahim.mist.world.generators.TombGen;

@Config(modid = Mist.MODID, name = "mist", category = "")
public class ModConfig {

	@Ignore private static final Map<ItemStack,Vector2f> stoneColors = new HashMap<ItemStack,Vector2f>();

	@LangKey("config.mist.dimension")
	public static Dimension dimension = new Dimension();
	@LangKey("config.mist.player")
	public static Player player = new Player();
	@LangKey("config.mist.graphic")
	public static Graphic graphic = new Graphic();
	@LangKey("config.mist.campfire")
	public static Campfire campfire = new Campfire();
	@LangKey("config.mist.time")
	public static Time time = new Time();
	@LangKey("config.mist.generation")
	public static Generation generation = new Generation();

	public static void init() {}

	public static class Dimension {

		@RequiresMcRestart
		@LangKey("config.mist.dimension.id")
		@Comment("What ID number to assign to the Misty World dimension. Change if you are having conflicts with another mod")
		public int dimensionID = 69;

		@LangKey("config.mist.dimension.stones")
		@Comment("Disable portal stone drop")
		public boolean disableStoneDrop = false;

		@LangKey("config.mist.dimension.trees")
		@Comment("Disable vanilla tree growth")
		public boolean disableVanillaTreeGrowth = true;

		@LangKey("config.mist.dimension.mycelium")
		@Comment("Can mycelium be harvested without a silk touch")
		public boolean myceliumHarvesting = false;

		@LangKey("config.mist.dimension.saplings")
		@Comment("Saplings drop chance. The final chance will be multiplied by the number of leaves attached to the cut branch")
		@RangeDouble(min = 0, max = 1)
		public double saplingsDropChance = 0.1;

		@LangKey("config.mist.dimension.bonemeal")
		@Comment("Enable the use Bone Meal to accelerate tree growth")
		public boolean enableUseBoneMeal = false;

		@LangKey("config.mist.dimension.blacklist")
		@Comment("Black list of dimensions in which it is impossible to build a portal into a Misty World")
		public int[] dimBlackList = { 1 };

		@LangKey("config.mist.dimension.breakers")
		@Comment("Assigns the mining speed multiplier of the foggy stone to the item (ModID:Item:Porous:Upper:Basic). Please do not change this parameter! This may affect the game balance")
		public String[] stoneBreakers = { "mist:niobium_pickaxe:1:8:8" };

		@LangKey("config.mist.dimension.filter_coal_breakers")
		@Comment("List of tools that can mine filter coal ore. Please do not change this parameter! This may affect the game balance")
		public String[] filterCoalBreakers = { "mist:niobium_pickaxe" };

		@LangKey("config.mist.dimension.mod_blacklist")
		@Comment("Blacklist of mobs that can't spawn in a Misty World (modId:mobName or modId:* for all mobs in the mod). For example: minecraft:pig")
		public String[] mobsBlackList = {};

		@LangKey("config.mist.dimension.cascad_lag")
		@Comment("Disable the message about cascading worldgen lag. Temporary measure until I find a solution to the problem")
		public boolean disableCascadingLog = true;

		@Ignore public static final ArrayList<Integer> loadedDimBlackList = new ArrayList<Integer>();

		@LangKey("config.mist.dimension.allow_acid_rain_damage")
		@Comment("Disable acid rain damage. Note that this will not disable the poisoning caused by acid rain.")
		public boolean acidRainDamage = true;

		@LangKey("config.mist.dimension.portal_spawn_layer")
		@Comment("Which 'layer' of the dimension to allow portal spawns in. 0 = Up Biomes, 1 = Border-Up Biomes, 2 = Border-Down Biomes, 3 = Down Biomes, 4 = Center-Down Biomes")
		@RangeInt(min = 0, max = 4)
		public int portalSpawnLayer = 0;

		@LangKey("config.mist.dimension.mob_immunities")
		@Comment("Blacklist of mobs that are immune to the mist (modId:mobName or modId:* for all mobs in the mod). For example: minecraft:pig")
		public String[] mobImmunities = {};

		@LangKey("config.mist.dimension.portal_middle_block")
		@Comment("ModId:ItemId of the middle portal block")
		public String portalMiddleBlock = "minecraft:gold_block";
	}

	public static class Player {

		@LangKey("config.mist.player.search")
		@Comment("Enable search bar on creative tab")
		public boolean enableSearchBar = true;

		@LangKey("config.mist.player.cut")
		@Comment("To what percent will the values of intoxication and chemical pollution be cut after the player's death")
		@RangeInt(min = 0, max = 100)
		public int effectsReduction = 50;

		@LangKey("config.mist.player.bars")
		@Comment("Show effects progress bar on the main screen")
		public boolean showEffectsBar = true;

		@LangKey("config.mist.player.effects")
		@Comment("Show effects percentages on the main screen")
		public boolean showEffectsPercent = false;

		@LangKey("config.mist.player.soap")
		@Comment("Enable washing armor with soap")
		public boolean soapWashingArmor = true;

		@LangKey("config.mist.player.skill_factor")
		@Comment("Skill factor in order: Taming, Cutting, Mason. The lower the value, the faster the skills upgrade")
		@RangeDouble(min = 0.02, max = 10)
		public double[] skillFactor = new double[] { 1, 1, 1 };

		@LangKey("config.mist.player.mobs_for_skill")
		@Comment("A list of creatures (not monsters) whose killing will increase the cutting skill (modId:mobName:points or modId:*:points for all mobs in the mod). For example: mist:mossling:1, mist:monk:2, mist:brachiodon:3")
		public String[] mobsForSkill = {};

		@LangKey("config.mist.player.allow_potion_for_filter")
		@Comment("Whether to allow a specific potion effect to act as a stand-in for an air filter")
		public boolean potionFilterAllow = false;

		@LangKey("config.mist.player.filter_potion")
		@Comment("Which potion to act as a stand-in for an air filter: ModID:potionID")
		public String potionFilter = "minecraft:absorption";

		@LangKey("config.mist.player.urn_always_breaks")
		@Comment("Whether if mining an urn always results in the urn shattering")
		public boolean urnAlwaysBreaks = false;

		@LangKey("config.mist.player.allow_urn_opening_inventory")
		@Comment("Whether to allow the player to use the urn in their inventory (without placing it down)")
		public boolean urnInventoryOpening = true;

		@LangKey("config.mist.player.allow_mask_keybind")
		@Comment("Whether to enable the mask inventory keybind")
		public boolean keybindMask = true;

		@LangKey("config.mist.player.allow_skill_keybind")
		@Comment("Whether to enable the skill screen keybind")
		public boolean keybindSkill = true;

		@LangKey("config.mist.player.replace_pollution_with_potion")
		@Comment("Whether to replace all occurences of Pollution with a potion effect tick")
		public boolean pollutionReplaceWithPotion = false;

		@LangKey("config.mist.player.pollution_potion")
		@Comment("Which potion to apply instead of Pollution: ModID:potionID")
		public String pollutionPotion = "minecraft:slowness";

		@LangKey("config.mist.player.pollution_potion_amplifier")
		@Comment("Amplifier of the Pollution potion")
		public int pollutionPotionAmplifier = 0;

		@LangKey("config.mist.player.pollution_potion_duration_multiplier")
		@Comment("Multiplier for the Pollution potion duration")
		public double pollutionPotionDurationMultiplier = 0.25;

		@LangKey("config.mist.player.replace_intoxication_with_potion")
		@Comment("Whether to replace all occurences of Intoxication with a potion effect tick")
		public boolean intoxicationReplaceWithPotion = false;

		@LangKey("config.mist.player.intoxication_potion")
		@Comment("Which potion to apply instead of Intoxication: ModID:potionID")
		public String intoxicationPotion = "minecraft:poison";

		@LangKey("config.mist.player.intoxication_potion_amplifier")
		@Comment("Amplifier of the Intoxication potion")
		public int intoxicationPotionAmplifier = 0;

		@LangKey("config.mist.player.intoxication_potion_duration_multiplier")
		@Comment("Multiplier for the Intoxication potion duration")
		public double intoxicationPotionDurationMultiplier = 0.25;

		@LangKey("config.mist.player.filter_hotbar_x_offset")
		@Comment("X Offset for Air Filter Hotbar Slot")
		public int hotbarFilterXOffset = 0;

		@LangKey("config.mist.player.filter_hotbar_y_offset")
		@Comment("Y Offset for Air Filter Hotbar Slot")
		public int hotbarFilterYOffset = 0;

		@LangKey("config.mist.player.respawn_in_mist")
		@Comment("Toggle whether respawning back into the Misty World is permitted")
		public boolean respawnInMist = true;

		@LangKey("config.mist.player.helmet_filters")
		@Comment("List of helmets that allow for mist filtration")
		public String[] helmetFilters = {};

		@LangKey("config.mist.player.helmet_filters_take_damage")
		@Comment("Whether or not helmets that allow for mist filtration take damage each filtered tick")
		public boolean helmetFiltersTakeDamage = true;
	}

	public static class Graphic {

		//@Ignore public static boolean smoothFogTexture = Mist.proxy.hasOptifine();
		@Ignore public static boolean smoothFogTexture = false;
		@Ignore public static boolean mipMapOptimization = smoothFogTexture;

		@LangKey("config.mist.graphic.fog")
		@Comment("Advanced fog renderer. Adds falling shadows to the fog")
		public boolean advancedFogRenderer = true;

		@LangKey("config.mist.graphic.fog_quality")
		@Comment("Fog render quality")
		@RangeInt(min = 1, max = 5)
		public int fogQuality = 5;
	}

	public static class Campfire {

		@LangKey("config.mist.campfire.stones")
		@Comment("The stones are available for the creation of the campfire base (ModID:Item:Required quantity:Metadata:Color)")
		public String[] stoneAndColors = {	"mist:rocks:4:0:9bb6af",
				"minecraft:cobblestone:1:0:bcbcbc",
				"minecraft:brick:4:0:c46c58",
				"minecraft:netherbrick:4:0:522932",
				"minecraft:flint:4:0:262626",
				"minecraft:dye:4:4:3052c1",
				"minecraft:prismarine_shard:4:0:92f0de" };

		@LangKey("config.mist.campfire.effects")
		@Comment("Enable pottage effects display")
		public boolean showSoupEffects = false;
	}

	public static class Time {

		@LangKey("config.mist.time.day")
		@Comment("The number of days in a month")
		@RangeInt(min = 2, max = 16)
		public int dayInMonth = 4;

		@LangKey("config.mist.time.cotton")
		@Comment("How many times a year will bloom desert cotton")
		@RangeInt(min = 1, max = 4)
		public int desertCottonBloomCount = 2;
	}

	public static class Generation {

		@LangKey("config.mist.generation.basement")
		@Comment("Old basement generation chance (0 - never, 1 - each chunk)")
		@RangeDouble(min = 0, max = 1)
		public double basementGenerationChance = 0.2;

		@LangKey("config.mist.generation.well")
		@Comment("Wells generation chance (0 - never, 1 - each chunk)")
		@RangeDouble(min = 0, max = 1)
		public double wellsGenerationChance = 0.002;

		@LangKey("config.mist.generation.tomb_forest")
		@Comment("Mixed Forest Tomb generation chance (0 - never, 1 - each chunk)")
		@RangeDouble(min = 0, max = 1)
		public double forestTombGenerationChance = 0.02;

		@LangKey("config.mist.generation.tomb_swamp")
		@Comment("Swampy Tomb generation chance (0 - never, 1 - each chunk)")
		@RangeDouble(min = 0, max = 1)
		public double swampTombGenerationChance = 0.007;

		@LangKey("config.mist.generation.tomb_desert")
		@Comment("Desert and Savanna Tomb generation chance (0 - never, 1 - each chunk)")
		@RangeDouble(min = 0, max = 1)
		public double desertTombGenerationChance = 0.005;

		@LangKey("config.mist.generation.tomb_snow")
		@Comment("Taiga Tomb generation chance (0 - never, 1 - each chunk)")
		@RangeDouble(min = 0, max = 1)
		public double snowTombGenerationChance = 0.015;

		@LangKey("config.mist.generation.tomb_jungle")
		@Comment("Jungle Tomb generation chance (0 - never, 1 - each chunk)")
		@RangeDouble(min = 0, max = 1)
		public double jungleTombGenerationChance = 0.008;

		@LangKey("config.mist.generation.tomb_cliff")
		@Comment("Cliff Tomb generation chance (0 - never, 1 - each chunk)")
		@RangeDouble(min = 0, max = 1)
		public double cliffTombGenerationChance = 0.15;

		@LangKey("config.mist.generation.altar")
		@Comment("Altar generation chance (0 - never, 1 - each chunk)")
		@RangeDouble(min = 0, max = 1)
		public double altarGenerationChance = 0.005;

		@LangKey("config.mist.generation.rare_urn")
		@Comment("Rare Urn generation chance (0 - never, 1 - each urn)")
		@RangeDouble(min = 0, max = 1)
		public double rareUrnGenerationChance = 0.1;

		@LangKey("config.mist.generation.desert_fish")
		@Comment("Desert Fish generation chance (0 - never, 1 - each block)")
		@RangeDouble(min = 0, max = 1)
		public double desertFishGenRarity = 0.015;
	}

	public static void onConfigChange() {
		ConfigManager.sync(Mist.MODID, Type.INSTANCE);
		for (int i : ModConfig.dimension.dimBlackList) Dimension.loadedDimBlackList.add(i);
		MistTime.setMonthLength(ModConfig.time.dayInMonth);
		for (Skill skill : Skill.values()) skill.updateSizes();
		ModConfig.applyFirePitColors(false);
		ModConfig.applyStoneBreakers();
		ModConfig.applyFilterCoalBreakers();
		ModConfig.applyMobsForSkill();
		ModConfig.applyMobsBlackList();
		ModConfig.applyMobImmunities();
		ModConfig.applyHelmetFilters();
		ModConfig.verifyPortalBlock();
		TombGen.updateChance();
		Mist.proxy.onConfigChange();
	}

	private static void verifyPortalBlock() {
		Block newMiddleBlock = Block.getBlockFromName(ModConfig.dimension.portalMiddleBlock);
		if (newMiddleBlock == null) MistRegistry.middlePortalBlock = newMiddleBlock;
	}

	public static long getCustomSeed(long seed) {
        /*String s = config.get("dimension", "customSeed", "").getString();
        if (!StringUtils.isNullOrEmpty(s)) {
            try {
                long j = Long.parseLong(s);
                if (j != 0L) seed = j;
            }
            catch (NumberFormatException num) {
            	long j = s.hashCode();
            	if (j != 0L) seed = j;
            }
        }*/
		//MistWorld.setCustomSeed(seed);
		return seed;
	}

	public static void applyFirePitColors(boolean repeat) {
		if (!repeat) {
			stoneColors.clear();
			Pattern splitpattern = Pattern.compile(":");
			for (int i = 0; i < ModConfig.campfire.stoneAndColors.length; i++) {
				String s = ModConfig.campfire.stoneAndColors[i];
				String[] pettern = splitpattern.split(s);
				if (pettern.length != 5) {
					Mist.logger.warn("Invalid set of parameters at stoneAndColors line " + (i + 1));
					continue;
				}
				ResourceLocation res = new ResourceLocation(pettern[0], pettern[1]);
				Item item;
				int count;
				int meta;
				int color;
				if (ForgeRegistries.ITEMS.containsKey(res)) {
					item = ForgeRegistries.ITEMS.getValue(res);
				} else if (ForgeRegistries.BLOCKS.containsKey(res)) {
					item = Item.getItemFromBlock(ForgeRegistries.BLOCKS.getValue(res));
				} else {
					Mist.logger.warn("Cannot found item/block \"" + pettern[0] + ":" + pettern[1] + "\" from stoneAndColors line " + (i + 1));
					continue;
				}
				try {
					count = Integer.parseInt(pettern[2]);
					if (count < 1 || count > 4) {
						MathHelper.clamp(count, 1, 4);
						Mist.logger.warn("Count \"" + pettern[2] + "\" out of valid range point at stoneAndColors line " + (i + 1) + ". Count will be changed to " + count);
					}
				} catch (NumberFormatException e) {
					Mist.logger.warn("Cannot parse Count \"" + pettern[2] + "\" to integer point at stoneAndColors line " + (i + 1));
					continue;
				}
				try {
					meta = Integer.parseInt(pettern[3]);
					if (meta < 0 || (meta > 0 && item.getMaxDamage() != 0) || (meta > 15 && item instanceof ItemBlock)) {
						meta = 0;
						Mist.logger.warn("Metadata \"" + pettern[3] + "\" out of valid range point at stoneAndColors line " + (i + 1) + ". Metadata will be changed to " + meta);
					}
				} catch (NumberFormatException e) {
					Mist.logger.warn("Cannot parse Metadata \"" + pettern[3] + "\" to integer point at stoneAndColors line " + (i + 1));
					continue;
				}
				try {
					color = Integer.parseInt(pettern[4], 16);
				} catch (NumberFormatException e) {
					Mist.logger.warn("Cannot parse Color \"" + pettern[4] + "\" to integer point at stoneAndColors line " + (i + 1));
					continue;
				}
				boolean check = false;
				lab:
				{
					for (ItemStack stones : stoneColors.keySet()) {
						if (stones.getItem() == item && stones.getItemDamage() == meta) {
							check = true;
							Mist.logger.warn("Item \"" + pettern[0] + ":" + pettern[1] + "\" with Metadata \"" + pettern[3] + "\" is already exist");
							break lab;
						}
					}
					if (!check) stoneColors.put(new ItemStack(item, 1, meta), new Vector2f(count, color));
				}
			}
			if (stoneColors.isEmpty()) stoneColors.put(new ItemStack(MistItems.ROCKS), new Vector2f(4, 0x9bb6af));
		}
		TileEntityCampfire.putStoneAndColorList(stoneColors);
	}

	public static void applyStoneBreakers() {
		MistRegistry.mistStoneBreakers.clear();
		Pattern splitpattern = Pattern.compile(":");
		for (int i = 0; i < ModConfig.dimension.stoneBreakers.length; i++) {
			String s = ModConfig.dimension.stoneBreakers[i];
			String[] pettern = splitpattern.split(s);
			if (pettern.length != 5) {
				Mist.logger.warn("Invalid set of parameters at stoneBreakers line " + (i + 1));
				continue;
			}
			ResourceLocation res = new ResourceLocation(pettern[0], pettern[1]);
			Item item;
			int porous;
			int upper;
			int basic;
			if (ForgeRegistries.ITEMS.containsKey(res)) {
				item = ForgeRegistries.ITEMS.getValue(res);
			} else {
				Mist.logger.warn("Cannot found item \"" + pettern[0] + ":" + pettern[1] + "\" from stoneBreakers line " + (i + 1));
				continue;
			}
			try {
				porous = Integer.parseInt(pettern[2]);
				if (porous < 0) {
					porous = 1;
					Mist.logger.warn("Porous stone multiplier \"" + pettern[2] + "\" less than zero point at stoneBreakers line " + (i + 1) + ". Multiplier will be changed to " + porous);
				}
			} catch (NumberFormatException e) {
				Mist.logger.warn("Cannot parse porous stone multiplier \"" + pettern[2] + "\" to integer point at stoneBreakers line " + (i + 1));
				continue;
			}
			try {
				upper = Integer.parseInt(pettern[3]);
				if (upper < 0) {
					upper = 1;
					Mist.logger.warn("Upper stone multiplier \"" + pettern[3] + "\" less than zero point at stoneBreakers line " + (i + 1) + ". Multiplier will be changed to " + upper);
				}
			} catch (NumberFormatException e) {
				Mist.logger.warn("Cannot parse upper stone multiplier \"" + pettern[3] + "\" to integer point at stoneBreakers line " + (i + 1));
				continue;
			}
			try {
				basic = Integer.parseInt(pettern[4]);
				if (basic < 0) {
					basic = 1;
					Mist.logger.warn("Basic stone multiplier \"" + pettern[4] + "\" less than zero point at stoneBreakers line " + (i + 1) + ". Multiplier will be changed to " + basic);
				}
			} catch (NumberFormatException e) {
				Mist.logger.warn("Cannot parse basic stone multiplier \"" + pettern[4] + "\" to integer point at stoneBreakers line " + (i + 1));
				continue;
			}
			boolean check = false;
			lab:
			{
				for (Item tool : MistRegistry.mistStoneBreakers.keySet()) {
					if (tool == item) {
						check = true;
						Mist.logger.warn("Item \"" + pettern[0] + ":" + pettern[1] + "\" is already exist (stoneBreakers, line " + (i + 1) + ")");
						break lab;
					}
				}
				if (!check) MistRegistry.mistStoneBreakers.put(item, new int[] {porous, upper, basic});
			}
		}
		if (MistRegistry.mistStoneBreakers.isEmpty()) MistRegistry.mistStoneBreakers.put(MistItems.NIOBIUM_PICKAXE, new int[] {1, 8, 8});
	}

	private static void applyHelmetFilters() {
		MistRegistry.helmetFilters.clear();
		Pattern splitpattern = Pattern.compile(":");
		for (int i = 0; i < ModConfig.player.helmetFilters.length; i++) {
			String s = ModConfig.player.helmetFilters[i];
			String[] pettern = splitpattern.split(s);
			if (pettern.length != 2) {
				Mist.logger.warn("Invalid set of parameters at helmetFilters line " + (i + 1));
				continue;
			}
			ResourceLocation res = new ResourceLocation(pettern[0], pettern[1]);
			Item item;
			if (ForgeRegistries.ITEMS.containsKey(res)) {
				item = ForgeRegistries.ITEMS.getValue(res);
			} else {
				Mist.logger.warn("Cannot found item \"" + pettern[0] + ":" + pettern[1] + "\" from helmetFilters line " + (i + 1));
				continue;
			}
			if (MistRegistry.helmetFilters.contains(item)) Mist.logger.warn("Item \"" + pettern[0] + ":" + pettern[1] + "\" is already exist (helmetFilters, line " + (i + 1) + ")");
			else MistRegistry.helmetFilters.add(item);
		}
	}

	public static void applyFilterCoalBreakers() {
		MistRegistry.filterCoalBreakers.clear();
		Pattern splitpattern = Pattern.compile(":");
		for (int i = 0; i < ModConfig.dimension.filterCoalBreakers.length; i++) {
			String s = ModConfig.dimension.filterCoalBreakers[i];
			String[] pettern = splitpattern.split(s);
			if (pettern.length != 2) {
				Mist.logger.warn("Invalid set of parameters at filterCoalBreakers line " + (i + 1));
				continue;
			}
			ResourceLocation res = new ResourceLocation(pettern[0], pettern[1]);
			Item item;
			if (ForgeRegistries.ITEMS.containsKey(res)) {
				item = ForgeRegistries.ITEMS.getValue(res);
			} else {
				Mist.logger.warn("Cannot found item \"" + pettern[0] + ":" + pettern[1] + "\" from filterCoalBreakers line " + (i + 1));
				continue;
			}
			boolean check = false;
			if (MistRegistry.filterCoalBreakers.contains(item)) Mist.logger.warn("Item \"" + pettern[0] + ":" + pettern[1] + "\" is already exist (filterCoalBreakers, line " + (i + 1) + ")");
			else MistRegistry.filterCoalBreakers.add(item);
		}
		if (MistRegistry.filterCoalBreakers.isEmpty()) MistRegistry.filterCoalBreakers.add(MistItems.NIOBIUM_PICKAXE);
	}

	public static void applyMobsForSkill() {
		MistRegistry.dimsForSkill.clear();
		MistRegistry.mobsForSkill.clear();
		Pattern splitpattern = Pattern.compile(":");
		for (int i = 0; i < ModConfig.player.mobsForSkill.length; i++) {
			String[] pettern = splitpattern.split(ModConfig.player.mobsForSkill[i]);
			if (pettern.length != 3) {
				Mist.logger.warn("Invalid set of parameters at mobsForSkill line " + (i + 1));
				continue;
			}
			if (!Loader.isModLoaded(pettern[0])) {
				Mist.logger.warn("Cannot found the modId \"" + pettern[0] + "\" from mobsForSkill line " + (i + 1));
				continue;
			} else if (pettern[0].equals(Mist.MODID)) {
				Mist.logger.warn("Misty mobs are already participating in skill calculation (mobsForSkill, line " + (i + 1) + ")");
				continue;
			} else {
				int point = 0;
				try {
					point = Integer.parseInt(pettern[2]);
					if (point < 0) {
						point = 1;
						Mist.logger.warn("Mob skill points \"" + pettern[2] + "\" less than zero point at mobsForSkill line " + (i + 1) + ". Points will be changed to " + point);
					}
				} catch (NumberFormatException e) {
					Mist.logger.warn("Cannot parse the mob skill points \"" + pettern[2] + "\" to integer point at mobsForSkill line " + (i + 1));
					continue;
				}
				if (!pettern[1].equals("*")) {
					ResourceLocation res = new ResourceLocation(pettern[0], pettern[1]);
					if (!ForgeRegistries.ENTITIES.containsKey(res)) {
						Mist.logger.warn("Cannot found the mob \"" + pettern[0] + ":" + pettern[1] + "\" from mobsForSkill line " + (i + 1));
						continue;
					} else if (MistRegistry.mobsForSkill.containsKey(res)) {
						Mist.logger.warn("Mob \"" + pettern[0] + ":" + pettern[1] + "\" is already exist (mobsForSkill, line " + (i + 1) + ")");
						continue;
					} else {
						MistRegistry.mobsForSkill.put(res, Integer.valueOf(point));
					}
				} else MistRegistry.dimsForSkill.put(pettern[0], Integer.valueOf(point));
			}
		}
	}

	public static void applyMobsBlackList() {
		MistRegistry.mobsDimsBlackList.clear();
		MistRegistry.mobsBlackList.clear();
		Pattern splitpattern = Pattern.compile(":");
		for (int i = 0; i < ModConfig.dimension.mobsBlackList.length; i++) {
			String[] pettern = splitpattern.split(ModConfig.dimension.mobsBlackList[i]);
			if (pettern.length != 2) {
				Mist.logger.warn("Invalid set of parameters at mobsBlackList line " + (i + 1));
				continue;
			}
			if (!Loader.isModLoaded(pettern[0])) {
				Mist.logger.warn("Cannot found the modId \"" + pettern[0] + "\" from mobsBlackList line " + (i + 1));
				continue;
			} else if (pettern[0].equals(Mist.MODID)) {
				Mist.logger.warn("Misty mobs cannot add to the blacklist (mobsBlackList, line " + (i + 1) + ")");
				continue;
			} else {
				if (!pettern[1].equals("*")) {
					ResourceLocation res = new ResourceLocation(pettern[0], pettern[1]);
					if (!ForgeRegistries.ENTITIES.containsKey(res)) {
						Mist.logger.warn("Cannot found the mob \"" + pettern[0] + ":" + pettern[1] + "\" from mobsBlackList line " + (i + 1));
						continue;
					} else if (MistRegistry.mobsBlackList.contains(res)) {
						Mist.logger.warn("Mob \"" + pettern[0] + ":" + pettern[1] + "\" is already exist (mobsBlackList, line " + (i + 1) + ")");
						continue;
					} else {
						MistRegistry.mobsBlackList.add(res);
					}
				} else MistRegistry.mobsDimsBlackList.add(pettern[0]);
			}
		}
	}

	private static void applyMobImmunities() {
		MistRegistry.mobImmunities.clear();
		MistRegistry.mobImmunitiesMod.clear();
		Pattern splitpattern = Pattern.compile(":");
		for (int i = 0; i < ModConfig.dimension.mobImmunities.length; i++) {
			String[] pettern = splitpattern.split(ModConfig.dimension.mobImmunities[i]);
			if (pettern.length != 2) {
				Mist.logger.warn("Invalid set of parameters at mobImmunities line " + (i + 1));
				continue;
			}
			if (!Loader.isModLoaded(pettern[0])) {
				Mist.logger.warn("Cannot found the modId \"" + pettern[0] + "\" from mobImmunities line " + (i + 1));
				continue;
			} else {
				if (!pettern[1].equals("*")) {
					ResourceLocation res = new ResourceLocation(pettern[0], pettern[1]);
					if (!ForgeRegistries.ENTITIES.containsKey(res)) {
						Mist.logger.warn("Cannot found the mob \"" + pettern[0] + ":" + pettern[1] + "\" from mobImmunities line " + (i + 1));
						continue;
					} else if (MistRegistry.mobImmunities.contains(res)) {
						Mist.logger.warn("Mob \"" + pettern[0] + ":" + pettern[1] + "\" is already exist (mobImmunities, line " + (i + 1) + ")");
						continue;
					} else {
						MistRegistry.mobImmunities.add(res);
					}
				} else MistRegistry.mobImmunitiesMod.add(pettern[0]);
			}
		}
	}
}