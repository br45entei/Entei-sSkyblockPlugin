package com.gmail.br45entei.enteisSkyblock.main;

import com.gmail.br45entei.enteisSkyblock.challenge.Challenge;
import com.gmail.br45entei.enteisSkyblock.challenge.Challenge.ChallengeCommand;
import com.gmail.br45entei.enteisSkyblock.event.ChallengeCompleteEvent;
import com.gmail.br45entei.enteisSkyblock.event.ChallengeTierCompleteEvent;
import com.gmail.br45entei.enteisSkyblock.event.listeners.DamageEventListeners;
import com.gmail.br45entei.enteisSkyblock.main.Island.InvitationResult;
import com.gmail.br45entei.enteisSkyblock.main.Island.JoinRequestResult;
import com.gmail.br45entei.enteisSkyblock.vault.VaultHandler;
import com.gmail.br45entei.enteisSkyblockGenerator.main.GeneratorMain;
import com.gmail.br45entei.enteisSkyblockGenerator.main.SkyworldGenerator;
import com.gmail.br45entei.enteisSkyblockGenerator.main.SkyworldGenerator.SkyworldBlockPopulator;
import com.gmail.br45entei.util.CodeUtil;
import com.gmail.br45entei.util.Material_1_12_2;
import com.gmail.br45entei.util.PlayerAdapter;
import com.gmail.br45entei.util.ResourceUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_13_R2.block.CraftBlockState;
import org.bukkit.craftbukkit.v1_13_R2.block.impl.CraftPortal;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

/** @author Brian_Entei */
@SuppressWarnings("javadoc")
public strictfp class Main extends JavaPlugin implements Listener {
	
	//Dynamic; loaded upon startup
	protected static volatile Main plugin;
	public static volatile Server server;
	public static volatile ConsoleCommandSender console;
	public static volatile BukkitScheduler scheduler;
	public static volatile PluginManager pluginMgr;
	protected static volatile SimpleCommandMap commandMap;
	
	private static volatile boolean vaultEnabled = false;
	
	//Configuration settings; these need to be saved/loaded
	
	//materialLevels.yml
	protected static final ConcurrentHashMap<Material, Double> materialLevels = new ConcurrentHashMap<>();
	private volatile YamlConfiguration materialLevelConfig = null;
	protected static volatile double defaultLevel = 0.01;
	protected static volatile double pointsPerLevel = 1.0;
	protected static volatile boolean checkContainers = true;
	protected static volatile boolean checkItemStacks = true;
	protected static volatile boolean checkEntities = true;
	public static volatile boolean useDiminishingReturnsForMaterialLevels = true;
	protected static volatile double materialLevelDropOff = 5000.0;
	
	//Volatile states; no need to save
	private static final ConcurrentHashMap<String, Boolean> developerDebugStates = new ConcurrentHashMap<>();
	
	public static final boolean getDebugMode(Player player) {
		if(!player.hasPermission("skyblock.dev")) {//??? Are they hacking?
			developerDebugStates.remove(player.getUniqueId().toString());
			return false;
		}
		return developerDebugStates.get(player.getUniqueId().toString()) == Boolean.TRUE;
	}
	
	public static final void setDebugMode(Player player, boolean debugMode) {
		if(!player.hasPermission("skyblock.dev")) {//??? Are they hacking?
			developerDebugStates.remove(player.getUniqueId().toString());
			return;
		}
		developerDebugStates.put(player.getUniqueId().toString(), Boolean.valueOf(debugMode));
		player.sendMessage(ChatColor.GREEN.toString().concat("[Entei's Skyblock] Skyblock debug mode is now: ").concat(getDebugMode(player) ? ChatColor.DARK_GREEN.toString().concat("enabled") : ChatColor.DARK_GREEN.toString().concat("disabled")));
	}
	
	public static final void sendDebugMsg(CommandSender sender, String message) {
		if(sender.hasPermission("skyblock.dev") && (sender instanceof Player ? getDebugMode((Player) sender) : true)) {
			sender.sendMessage("[ESB DEBUG] ".concat(message));
		}
	}
	
	public static final void sendDebugMsg(CommandSender sender, String[] messages) {
		if(sender.hasPermission("skyblock.dev") && (sender instanceof Player ? getDebugMode((Player) sender) : true)) {
			for(String message : messages) {
				sender.sendMessage("[ESB DEBUG] ".concat(message));
			}
		}
	}
	
	public static final Logger getPluginLogger() {
		return Main.getPlugin().getLogger();
	}
	
	/** @param world The world
	 * @return Whether or not the given world is a world in which players may
	 *         open their island chests */
	public static boolean isWorldAllowedForIslandChests(World world) {
		if(Island.isSkyworld(world)) {
			return true;
		}
		for(String worldName : Main.getPlugin().getConfig().getStringList("island.allowedWorldsForIslandChests")) {
			if(worldName.equals(world.getName()) || worldName.equals(world.getUID().toString())) {
				return true;
			}
		}
		return false;
	}
	
	public static final double getIslandLevelDivisor() {
		return pointsPerLevel;
	}
	
	public static final double getDefaultMaterialBlockLevel() {
		return defaultLevel;
	}
	
	public static final boolean isIllegalInSurvivalSkyblock(Material material) {
		if(material == null) {
			throw new NullPointerException("Material cannot be null!");
		}
		switch(material) {
		case BEDROCK:
		case BARRIER:
		case CHAIN_COMMAND_BLOCK:
		case COMMAND_BLOCK:
		case COMMAND_BLOCK_MINECART:
			//case CONDUIT:
			//case END_CRYSTAL:
		case END_GATEWAY:
		case EXPERIENCE_BOTTLE:
		case KNOWLEDGE_BOOK://This could go either way, but it is only accessible via /give
			//case MOVING_PISTON:
		case REPEATING_COMMAND_BLOCK:
		case VOID_AIR:
			return true;
		//$CASES-OMITTED$
		default:
			return false;
		}
	}
	
	/** @param material The material whose level will be returned
	 * @return The level for the given material, or <code>0.01</code> if the
	 *         material was not found/configured. */
	public static final double getLevelFor(Material material) {
		if(material == null) {
			throw new NullPointerException("Material cannot be null!");
		}
		if(isIllegalInSurvivalSkyblock(material)) {
			return -9999999999999999.9999999999999999;//-999999999.999999999;
		}
		Double level = materialLevels.get(material);
		if(level == null) {
			Main.getPluginLogger().warning("A material was not set in the configuration file materialLevels.yml: " + material.name());
			level = Double.valueOf(defaultLevel);
			materialLevels.put(material, level);
		}
		return level.doubleValue();
	}
	
	public static final String getPrefix() {
		return plugin == null ? "" : "[" + plugin.getDescription().getPrefix() + "] ";
	}
	
	public static final void println(String str) {
		Logger.getLogger("Minecraft").log(Level.INFO, getPrefix() + str);
	}
	
	public static final void printErrln(String str) {
		Logger.getLogger("Minecraft").log(Level.WARNING, getPrefix() + str);
	}
	
	protected static final ConcurrentLinkedDeque<UUID> spawnVisitingPlayers = new ConcurrentLinkedDeque<>();
	
	public static final boolean isVaultEnabled() {
		return vaultEnabled;
	}
	
	private static final ConcurrentHashMap<UUID, Location> playerDeathLocations = new ConcurrentHashMap<>();
	
	public static final Location getLastDeathLocation(OfflinePlayer player) {
		return getLastDeathLocation(player.getUniqueId());
	}
	
	public static final Location getLastDeathLocation(UUID player) {
		return playerDeathLocations.get(player);
	}
	
	@SuppressWarnings("unused")
	private static final Method getDeclaredMethod(String name, Class<?> cls, Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException {
		name = name.trim();
		for(Method method : cls.getDeclaredMethods()) {
			if(method.getName().equals(name)) {
				Class<?>[] paramTypes = method.getParameterTypes();
				if(paramTypes.length != parameterTypes.length) {
					continue;
				}
				boolean sameTypes = true;
				for(int i = 0; i < paramTypes.length; i++) {
					if(!paramTypes[i].isAssignableFrom(parameterTypes[i])) {
						sameTypes = false;
						break;
					}
				}
				if(!sameTypes) {
					continue;
				}
				return method;
			}
		}
		throw new NoSuchMethodException(cls.getName() + "." + name + "()");
	}
	
	private static final Field getDeclaredField(String name, Class<?> cls) throws SecurityException, NoSuchFieldException {
		name = name.trim();
		for(Field field : cls.getDeclaredFields()) {
			if(field.getName().equals(name)) {
				return field;
			}
		}
		throw new NoSuchFieldException(cls.getName() + "." + name);
	}
	
	/** Gets the Display name as seen in the Client.<br>
	 * Currently the server only supports the English language. To override
	 * this,<br>
	 * you must replace the language file embedded in the server jar.<br>
	 * 
	 * @param item The ItemStack whose display name will be returned
	 * @return The ItemStack's display name */
	public static final String getI18NDisplayName(ItemStack item) {
		net.minecraft.server.v1_13_R2.ItemStack nms = null;
		if(item instanceof org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack) {
			//nms = ((org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack) item).handle;
			try {
				Field _handle = getDeclaredField("handle", org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.class);
				_handle.setAccessible(true);
				nms = (net.minecraft.server.v1_13_R2.ItemStack) _handle.get(item);
			} catch(SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				return item.hasItemMeta() ? item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name().toLowerCase().replace("_", " ") : item.getType().name().toLowerCase().replace("_", " ");
			}
		}
		if(nms == null) {
			nms = org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack.asNMSCopy(item);
		}
		return nms != null ? nms.getName().getString() : null;
	}
	
	@SuppressWarnings("deprecation")
	public static final String getItemName(Block block) {
		return getItemName(new ItemStack(block.getType(), 1, (short) 0, Byte.valueOf(block.getData())));
	}
	
	public static final String getItemName(ItemStack item) {
		if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
			return item.getItemMeta().getDisplayName();
		}
		try {
			return getI18NDisplayName(item);//item.getI18NDisplayName();
		} catch(NoSuchMethodError ignored) {
			return capitalizeFirstLetterOfEachWord(item.getType().name().toLowerCase(), '_', ' ');
		} catch(Throwable ignored) {
			return capitalizeFirstLetterOfEachWord(item.getType().name().toLowerCase(), '_', ' ');
		}
	}
	
	public static final String limitDecimalToNumberOfPlaces(double d, int places) {
		String s = Double.toString(d);
		if(s.contains("E")) {
			s = new BigDecimal(d).toPlainString();
		}
		if(!s.contains(".")) {
			return s;
		}
		String whole = s.substring(0, s.indexOf("."));
		String decimal = s.substring(s.indexOf(".") + 1);
		if(decimal.length() > places) {
			decimal = decimal.substring(0, places);
		}
		return whole + "." + decimal;
	}
	
	public static final String locationToString(Location location, int decimalPlaces, boolean showYawAndPitch, boolean showWorld) {
		return (showWorld ? "world=".concat(location.getWorld() == null ? "null" : location.getWorld().getName()) : "").concat("X=").concat(limitDecimalToNumberOfPlaces(location.getX(), decimalPlaces)).concat(", Y=").concat(limitDecimalToNumberOfPlaces(location.getY(), decimalPlaces)).concat(", Z=").concat(limitDecimalToNumberOfPlaces(location.getX(), decimalPlaces)).concat(!showYawAndPitch ? "" : ", Yaw=".concat(limitDecimalToNumberOfPlaces(location.getYaw(), decimalPlaces)).concat(", Pitch=").concat(limitDecimalToNumberOfPlaces(location.getPitch(), decimalPlaces)));
	}
	
	/** @return The instance of this plugin */
	public static final Main getPlugin() {
		return plugin;
	}
	
	public Main() {
		plugin = this;
	}
	
	public static final String vectorToString(Vector vector) {
		return "(" + limitDecimalToNumberOfPlaces(getDoubleSafe(Double.valueOf(vector.getX())), 4) + "," + limitDecimalToNumberOfPlaces(getDoubleSafe(Double.valueOf(vector.getY())), 4) + "," + limitDecimalToNumberOfPlaces(getDoubleSafe(Double.valueOf(vector.getZ())), 4) + ")";// You love me for making a function with one really long line, don't you? hehehe...
	}
	
	private static final double getDoubleSafe(Double d) {
		double value = d.doubleValue();
		if(value != value) {
			value = 0.0;
		}
		if(Double.isInfinite(value)) {
			value = 0.0;
		}
		return value;
	}
	
	public static final void convertMaterialValuesFromUSkyblock(File file) {
		YamlConfiguration converted = fromUSkyblock(file);
		File dest = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + "-converted." + FilenameUtils.getExtension(file.getName()));
		try {
			converted.save(dest);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final YamlConfiguration fromUSkyblock(File file) {
		YamlConfiguration config = new YamlConfiguration();
		config.set("checkContainers", Boolean.valueOf(checkContainers));
		config.set("checkItemStacks", Boolean.valueOf(checkItemStacks));
		config.set("checkEntities", Boolean.valueOf(checkEntities));
		YamlConfiguration fromFile = new YamlConfiguration();
		try {
			fromFile.load(file);
		} catch(IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			return config;
		}
		double pointsPerLevel = fromFile.getDouble("general.pointsPerLevel", 1.0);// Divide by
		double defaultValue = fromFile.getDouble("general.default", 1.0);
		//1/1-6: 20
		ConfigurationSection blockValues = fromFile.getConfigurationSection("blockValues");
		if(blockValues == null) {
			return config;
		}
		//MaterialName:DataValue, Level
		ConcurrentHashMap<Material, Integer> dataValues = new ConcurrentHashMap<>();
		
		for(String key : blockValues.getKeys(false)) {
			String materialID = key;
			if(key.contains("/")) {
				String[] split = key.split(Pattern.quote("/"));
				materialID = split[0];
				if(!Main.isInt(materialID)) {
					System.err.println("Skipping bad material id \"" + materialID + "\"...");
					continue;
				}
				//XXX This won't work for Minecraft 1.13+, so we'll need to add our own converter once we update to 1.13 using the Material.java class from 1.12.2
				Material material = getMaterial(Integer.parseInt(materialID));
				if(material == null) {
					System.err.println("Error: unknown materialID: ".concat(materialID));
					continue;
				}
				String dataValue = split.length > 1 ? (split.length == 2 ? split[1] : CodeUtil.stringArrayToString(split, '/', 1)) : "0";
				if(dataValue.contains("-")) {
					split = dataValue.split(Pattern.quote("-"));
					String start = split[0];
					String end = "15";
					if(split.length == 2) {
						end = split[1];
					}
					if(Main.isInt(start) && Main.isInt(end)) {
						int s = Integer.parseInt(start),
								e = Integer.parseInt(end);
						Integer value = Integer.valueOf(blockValues.getInt(key, 0));
						for(int i = Math.min(s, e); i <= Math.max(s, e); i++) {
							ItemStack item = new ItemStack(material, 1, (short) i);
							String enumName = Main.getI18NDisplayName(item).toUpperCase().replace(" ", "_");
							Material check = Material.getMaterial(enumName);
							material = check == null ? item.getType() : check;
							dataValues.put(material, value);//dataValues.put(material.name() + ":" + Integer.toString(i), value);
						}
					}
				} else {
					if(Main.isInt(dataValue)) {
						ItemStack item = new ItemStack(material, 1, (short) Integer.parseInt(dataValue));
						String enumName = Main.getI18NDisplayName(item).toUpperCase().replace(" ", "_");
						Material check = Material.getMaterial(enumName);
						material = check == null ? item.getType() : check;
						dataValues.put(material, Integer.valueOf(blockValues.getInt(key, 0)));////dataValues.put(material.name() + ":" + Integer.valueOf(dataValue), Integer.valueOf(blockValues.getInt(key, 0)));
					}
				}
			} else {
				if(!Main.isInt(materialID)) {
					System.err.println("Skipping bad material id \"" + materialID + "\"...");
					continue;
				}
				Material material = getMaterial(Integer.parseInt(materialID));
				if(material == null) {
					System.err.println("Error: unknown materialID: ".concat(materialID));
					continue;
				}
				Integer value = Integer.valueOf(blockValues.getInt(key, 0));
				ItemStack item = new ItemStack(material, 1);
				String enumName = Main.getI18NDisplayName(item).toUpperCase().replace(" ", "_");
				Material check = Material.getMaterial(enumName);
				material = check == null ? item.getType() : check;
				dataValues.put(material, value);////dataValues.put(material.name() + ":0", value);
			}
		}
		/*List<String> keySet = new ArrayList<>(dataValues.keySet());
		//System.out.println("keySet size: " + keySet.size());
		Collections.sort(keySet, NUMERICAL_CASE_INSENSITIVE_ORDER);
		for(Material material : Material.values()) {
			ConfigurationSection mem = config.createSection(material.name());
			boolean defined = false;
			for(String key : keySet) {
				Integer value = dataValues.get(key);
				if(key.startsWith(material.name() + ":")) {
					defined = true;
					String dataValue = key.substring((material.name() + ":").length());
					mem.set(dataValue, Double.valueOf(value.intValue() / pointsPerLevel));
				}
			}
			if(!defined) {
				mem.set("0", Double.valueOf(defaultValue / pointsPerLevel));
			}
		}*/
		for(Entry<Material, Integer> entry : dataValues.entrySet()) {
			config.set(entry.getKey().name(), entry.getValue());
		}
		return config;
	}
	
	protected static final Comparator<String> NUMERICAL_CASE_INSENSITIVE_ORDER = new Comparator<String>() {
		
		public final int indexOfNumeral(String s, boolean last) {
			int index = 0;
			int first = -1;
			int lastIndex = -1;
			for(char c : s.toCharArray()) {
				if(Character.isDigit(c)) {
					if(!last) {
						if(s.indexOf(':') > index) {//for input string "LEAVES_2:12" and "LEAVES_2:7" this would return incorrect index 7 for start and 7 for end, resulting in the comparator comparing 7 - 7 == 0, meaning the two are identical
							index++;
							first = -1;
							lastIndex = -1;
							continue;
						}
						return index;
					}
					lastIndex = index;
					if(first == -1) {
						first = index;
					}
				} else {
					if(first != -1) {
						break;//Break on first sign of non-digit after having found one
					}
				}
				index++;
			}
			return last ? lastIndex : first;
		}
		
		@Override
		public int compare(String s1, String s2) {
			if(s1 == s2 || s1.equalsIgnoreCase(s2)) {
				System.out.println("s1 equals s2: " + s1);
				return 0;
			}
			int digitIndex1 = this.indexOfNumeral(s1, false);
			int lastDigitIndex1 = this.indexOfNumeral(s1, true);
			int digitIndex2 = this.indexOfNumeral(s2, false);
			int lastDigitIndex2 = this.indexOfNumeral(s2, true);
			if(digitIndex1 != -1) {
				String prefix1 = s1.substring(0, digitIndex1);
				if(!s2.toLowerCase().startsWith(prefix1.toLowerCase())) {
					return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
				}
				if(digitIndex2 != -1) {
					String prefix2 = s2.substring(0, digitIndex2);
					//System.out.println("prefix1: \"" + prefix1 + "\"; prefix2: \"" + prefix2 + "\";");
					if(!prefix1.equalsIgnoreCase(prefix2)) {
						return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
					}
					String digit1 = s1.substring(digitIndex1, (lastDigitIndex1 == -1 || lastDigitIndex1 <= digitIndex1) ? digitIndex1 + 1 : lastDigitIndex1 + 1);
					String digit2 = s2.substring(digitIndex2, (lastDigitIndex2 == -1 || lastDigitIndex2 <= digitIndex2) ? digitIndex2 + 1 : lastDigitIndex2 + 1);
					//System.out.println("digit1: \"" + digit1 + "\"; digit2: \"" + digit2 + "\";");
					if(Main.isInt(digit1) && Main.isInt(digit2)) {
						int compare = Integer.parseInt(digit1) - Integer.parseInt(digit2);
						//System.out.println("return " + Integer.toString(compare));
						return compare;
					} else if(Main.isInt(digit1)) {
						//System.out.println("return 1");
						return 1;
					} else {
						//System.out.println("return s1 equals s2");
						return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
					}
				}
				//System.out.println("s2 does not contain any digits: " + s2);
				return 1;
			}
			//System.out.println("s1 does not contain any digits: " + s1);
			return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
		}
	};
	
	public final File getMaterialConfigFile() {
		return new File(this.getDataFolder(), "materialLevels.yml");
	}
	
	public final boolean loadMaterialConfig() {
		materialLevels.clear();
		File file = this.getMaterialConfigFile();
		if(!file.isFile() || ResourceUtil.size(file) <= 0) {
			this.loadMaterialConfigFrom(this.getDefaultMaterialConfig());
			this.saveMaterialConfig();
			return true;
		}
		YamlConfiguration config = this.getMaterialLevelConfig();
		if(config == null) {
			this.getLogger().warning("Unable to load or save configuration file materialLevels.yml!");
			this.getLogger().warning("Setting all materials to the default value of '" + new BigDecimal(defaultLevel).toPlainString() + "'.");
			for(Material material : Material.values()) {
				materialLevels.put(material, Double.valueOf(defaultLevel));
			}
			return false;
		}
		this.loadMaterialConfigFrom(config);
		return true;
	}
	
	protected final void loadMaterialConfigFrom(YamlConfiguration config) {
		this.materialLevelConfig = config;
		for(String materialName : config.getKeys(false)) {
			if(materialName.equals("defaultLevel")) {
				defaultLevel = config.getDouble(materialName, defaultLevel);
				continue;
			}
			if(materialName.equals("pointsPerLevel")) {
				pointsPerLevel = config.getDouble(materialName, pointsPerLevel);
				continue;
			}
			if(materialName.equals("checkContainers")) {
				checkContainers = config.getBoolean(materialName, checkContainers);
				continue;
			}
			if(materialName.equals("checkItemStacks")) {
				checkItemStacks = config.getBoolean(materialName, checkItemStacks);
				continue;
			}
			if(materialName.equals("checkEntities")) {
				checkEntities = config.getBoolean(materialName, checkEntities);
				continue;
			}
			if(materialName.equals("useDiminishingReturnsForMaterialLevels")) {
				useDiminishingReturnsForMaterialLevels = config.getBoolean(materialName, useDiminishingReturnsForMaterialLevels);
				continue;
			}
			if(materialName.equals("materialLevelDropOff")) {
				materialLevelDropOff = config.getDouble(materialName, materialLevelDropOff);
				continue;
			}
			Material material = Material.getMaterial(materialName);
			if(material == null) {
				this.getLogger().warning("Material \"" + materialName + "\" specified in file materialLevels.yml does not exist! Ignoring...");
				continue;
			}
			String check = config.getString(materialName, new BigDecimal(defaultLevel).toPlainString());
			double value = Main.isDouble(check) ? Double.parseDouble(check) : defaultLevel;
			materialLevels.put(material, Double.valueOf(value));
		}
	}
	
	public final boolean saveMaterialConfig() {
		File file = this.getMaterialConfigFile();
		YamlConfiguration config = new YamlConfiguration();
		config.set("defaultLevel", Double.valueOf(defaultLevel));
		config.set("pointsPerLevel", Double.valueOf(pointsPerLevel));
		config.set("checkContainers", Boolean.valueOf(checkContainers));
		config.set("checkItemStacks", Boolean.valueOf(checkItemStacks));
		config.set("checkEntities", Boolean.valueOf(checkEntities));
		config.set("useDiminishingReturnsForMaterialLevels", Boolean.valueOf(useDiminishingReturnsForMaterialLevels));
		config.set("materialLevelDropOff", Double.valueOf(materialLevelDropOff));
		ArrayList<Material> sortedMaterials = new ArrayList<>();
		sortedMaterials.addAll(materialLevels.keySet());
		sortedMaterials.sort(MATERIAL_CASE_INSENSITIVE_ORDER);
		for(Material material : sortedMaterials) {//for(Entry<Material, Double> entry : materialLevels.entrySet()) {
			Double value = materialLevels.get(material);
			if(value != null) {//just in case...
				config.set(material.name(), Double.toString(getDoubleSafe(value)));
			}
		}
		this.materialLevelConfig = config;
		try {
			config.save(file);
			return true;
		} catch(IOException ex) {
			this.getLogger().log(Level.SEVERE, "Failed to save to configuration file " + file.getName(), ex);
			return false;
		}
	}
	
	public final YamlConfiguration getDefaultMaterialConfig() {
		YamlConfiguration config = new YamlConfiguration();
		config.set("defaultLevel", Double.valueOf(0.01));
		config.set("pointsPerLevel", Double.valueOf(1.0));
		config.set("checkContainers", Boolean.valueOf(true));
		config.set("checkItemStacks", Boolean.valueOf(true));
		config.set("checkEntities", Boolean.valueOf(true));
		config.set("useDiminishingReturnsForMaterialLevels", Boolean.valueOf(true));
		config.set("materialLevelDropOff", Double.valueOf(5000.0));
		for(Material material : sortedMaterials) {
			if(material.name().startsWith("LEGACY_")) {
				continue;
			}
			config.set(material.name(), Double.valueOf(isIllegalInSurvivalSkyblock(material) ? getLevelFor(material) : (material == Material.AIR ? 0.0 : 0.01)));
		}
		return config;
	}
	
	public final YamlConfiguration getMaterialConfig() {
		if(this.materialLevelConfig == null) {
			this.materialLevelConfig = this.getMaterialLevelConfig();
		}
		return this.materialLevelConfig == null ? new YamlConfiguration() : this.materialLevelConfig;
	}
	
	private final YamlConfiguration getMaterialLevelConfig() {
		File file = ResourceUtil.loadResourceAsFile(this.getResource("materialLevels.yml"), this.getMaterialConfigFile());
		if(file == null) {
			this.getLogger().warning("Failed to create configuration file materialLevels.yml!");
			return null;
		}
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
		} catch(IOException | InvalidConfigurationException ex) {
			this.getLogger().log(Level.SEVERE, "Failed to load from configuration file materialLevels.yml", ex);
		}
		return config;
	}
	
	public static final boolean isSafeForTeleporting(Location location) {
		return location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid() && !location.getBlock().getType().isSolid() && !location.getBlock().getRelative(0, 1, 0).getType().isSolid() && !location.getBlock().getRelative(0, 2, 0).getType().isSolid();
	}
	
	public static final ConcurrentHashMap<String, Long> lastTeleportTimes = new ConcurrentHashMap<>(),
			playerLoginTimes = new ConcurrentHashMap<>();
	
	private static final boolean isBlockSafeForTeleportingInto(Block block) {
		if(block.getType() == Material.WATER) {
			return true;
		}
		return isSafeForTeleporting(block.getLocation());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onPlayerTeleportEvent(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		Long lastLoginTime = playerLoginTimes.get(player.getUniqueId().toString());
		if(lastLoginTime != null && System.currentTimeMillis() - lastLoginTime.longValue() <= 5000L) {
			if(Island.isInSkyworld(player.getLocation()) && Island.isWithinSpawnArea(event.getTo())) {
				event.setCancelled(true);
				return;
			}
		}
		if(!event.isCancelled()) {
			lastTeleportTimes.put(player.getUniqueId().toString(), Long.valueOf(System.currentTimeMillis() + 10000L));
		}
	}
	
	/** Teleports the player to the given location. If the player is riding a
	 * vehicle, it will be dismounted prior to teleportation.
	 *
	 * @param player The player to teleport
	 * @param location New location to teleport the player to
	 * @return <code>true</code> if the teleport was successful */
	public static final boolean safeTeleport(Player player, Location location) {
		if((player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) && (!location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || location.getBlock().getType().isSolid() || location.getBlock().getRelative(BlockFace.UP).getType().isSolid())) {
			final Location original = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
			boolean foundSafeSpot = false;
			if(isBlockSafeForTeleportingInto(location.getBlock()) || location.getBlock().getRelative(0, 1, 0).getType().isSolid()) {
				while(location.getBlockY() <= location.getWorld().getMaxHeight()) {
					location = location.add(0, 1, 0);
					if(isSafeForTeleporting(location)) {
						foundSafeSpot = true;
						break;
					}
				}
				if(!foundSafeSpot) {
					location = original;
					if(location.getBlockY() >= location.getWorld().getMaxHeight()) {
						location = new Location(original.getWorld(), original.getX(), original.getY(), original.getZ(), original.getYaw(), original.getPitch());
						while(location.getBlockY() >= 1) {
							location = location.subtract(0, 1, 0);
							if(isSafeForTeleporting(location)) {
								foundSafeSpot = true;
								break;
							}
						}
						if(!foundSafeSpot) {
							location = original;
						}
					}
				}
			}
			if(!foundSafeSpot && location == original) {
				player.sendMessage(ChatColor.YELLOW.toString().concat("Unable to teleport you to ").concat(ChatColor.WHITE.toString()).concat("(world=").concat(original.getWorld().getName()).concat(", ").concat(original.toVector().toString().replace(",", ", ")).concat(")").concat(ChatColor.YELLOW.toString()).concat(": Unable to find a safe block to stand on!"));
				return false;
			}
			player.setVelocity(new Vector(0.0, 0.1/*-player.getVelocity().getY()*/, 0.0));
		}
		//lastTeleportTimes.put(player.getUniqueId().toString(), Long.valueOf(System.currentTimeMillis() + 10000L));
		return player.teleport(location);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		if(CommandConfirmation.onCommand(sender, cmd, command, args)) {
			return true;
		}
		if(Challenge.ChallengeCommand.isChallengeCommand(command)) {
			return Challenge.ChallengeCommand.onCommand(sender, command, args);
		}
		/*if(UnsafeEnchantCommand.isUnsafeEnchantCommand(command)) {
			return UnsafeEnchantCommand.onCommand(sender, command, args);
		}*/
		command = command.startsWith("enteisskyblock:") ? command.substring("enteisskyblock:".length()) : command;
		Player user = sender instanceof Player ? (Player) sender : null;
		if(command.equalsIgnoreCase("enteisskyblock") || command.equalsIgnoreCase("esb")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.GREEN.toString().concat("[Entei's Skyblock] Version ".concat(this.getDescription().getVersion())));
				sender.sendMessage(ChatColor.GREEN.toString().concat("Type \"").concat(ChatColor.WHITE.toString()).concat("/enteisskyblock help").concat(ChatColor.GREEN.toString()).concat("\" to view sub-commands."));
				return true;
			}
			if(args[0].equalsIgnoreCase("damageTest")) {
				if(args.length == 2) {
					if(args[1].equalsIgnoreCase("list")) {
						sender.sendMessage(ChatColor.GREEN.toString().concat("Listing all DamageCause enum names:"));
						for(DamageCause cause : DamageCause.values()) {
							sender.sendMessage(ChatColor.AQUA.toString().concat("\"").concat(ChatColor.WHITE.toString()).concat(cause.name()).concat(ChatColor.AQUA.toString()).concat("\""));
						}
						return true;
					}
				}
				if(args.length == 1) {
					if(user != null) {
						sender.sendMessage(ChatColor.YELLOW.toString().concat("Usage: \"/esb damageTest {DamageCause.ENUMNAME}\""));
					} else {
						sender.sendMessage(ChatColor.YELLOW.toString().concat("Console usage: \"/esb damageTest {DamageCause.ENUMNAME} [playerName]\""));
					}
					sender.sendMessage(ChatColor.GREEN.toString().concat("To list all available DamageCause enum names, type \"/esb damageTest list\"."));
					return true;
				}
				DamageCause cause;
				try {
					cause = DamageCause.valueOf(args[1].toUpperCase());
				} catch(IllegalArgumentException ex) {
					cause = null;
				}
				if(cause == null && !args[1].equalsIgnoreCase("all")) {
					sender.sendMessage(ChatColor.YELLOW.toString().concat("Unknown/invalid DamageCause enum name: \"").concat(ChatColor.WHITE.toString()).concat(args[1]).concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat("\""));
					sender.sendMessage(ChatColor.GREEN.toString().concat("To list all available DamageCause enum names, type \"/esb damageTest list\"."));
					return true;
				}
				Player entity = null;
				if(args.length == 3) {
					entity = Main.server.getPlayer(args[2]);
					if(entity == null) {
						sender.sendMessage(ChatColor.YELLOW.toString().concat("Could not locate player \"").concat(ChatColor.WHITE.toString()).concat(args.length >= 3 ? args[2] : "null").concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat("\"."));
						if(user == null) {
							sender.sendMessage(ChatColor.YELLOW.toString().concat("Console usage: \"/esb damageTest {DamageCause.ENUMNAME} [playerName]\""));
						}
						return true;
					}
				}
				entity = entity == null ? (user == null ? test_player : user) : entity;
				final Entity[] e = new Entity[] {entity};
				/*EntityDamageEvent spoof = new EntityDamageEvent(entity, cause, 1.0) {
					@Override
					public void setCancelled(boolean cancel) {
						if(cancel && Island.isInSkyworld(this.getEntity().getLocation())) {
							IllegalStateException ex = new IllegalStateException("This event shouldn't have been cancelled!");
							sender.sendMessage(ChatColor.YELLOW.toString().concat("Found a culprit plugin:"));
							sender.sendMessage(ChatColor.RED.toString().concat(CodeUtil.throwableToStr(ex, "\n")));
							throw ex;
						}
						super.setCancelled(cancel);
					}
				};
				sender.sendMessage(ChatColor.GREEN.toString().concat("[Entei's Skyblock] Calling a spoofed EntityDamageEvent to catch the culprit plugin..."));
				Main.pluginMgr.callEvent(spoof);*/
				sender.sendMessage(ChatColor.GREEN.toString().concat("[Entei's Skyblock] Calling a spoofed EntityDamageEvent to catch the culprit plugin..."));
				final boolean[] foundOne = {false};
				final DamageCause[] dc = new DamageCause[] {cause};
				for(Plugin plugin : pluginMgr.getPlugins()) {
					//if(plugin == this) {
					//	continue;
					//}
					List<RegisteredListener> listeners = new ArrayList<>(HandlerList.getRegisteredListeners(plugin));
					Runnable code = () -> {
						for(RegisteredListener registeredListener : listeners) {
							Listener listener = registeredListener.getListener();
							if(e[0] == test_player) {
								test_player.teleport(GeneratorMain.getSkyworldSpawnLocation());
								test_player.setGameMode(GameMode.SURVIVAL);
								test_player.setFlying(false);
								test_player.setAllowFlight(false);
							}
							EntityDamageEvent event = new EntityDamageEvent(e[0], dc[0], 1.0);
							event.setCancelled(false);
							try {
								registeredListener.callEvent(event);
							} catch(Throwable ex) {
								Main.server.getLogger().log(Level.SEVERE, "Could not pass event ".concat(event.getEventName()).concat(" to ").concat(plugin.getDescription().getFullName()), ex);
							}
							if(event.isCancelled()) {
								foundOne[0] = true;
								String msg1 = ChatColor.RED.toString().concat(" /!\\  Plugin \"").concat(ChatColor.WHITE.toString()).concat(plugin.getName()).concat(ChatColor.RED.toString()).concat("\" is currently cancelling a damage event(damage cause: \"").concat(ChatColor.WHITE.toString()).concat(dc[0].name()).concat(ChatColor.RED.toString()).concat("\")").concat(Island.isInSkyworld(e[0].getLocation()) ? " in skyworld" : "").concat("!");
								String msg2 = null;
								if(!listener.getClass().getSimpleName().contains("Afk") && Island.isInSkyworld(e[0].getLocation())) {
									if(plugin != Main.getWorldEdit() && plugin != Main.getWorldGuard() && plugin != this) {
										msg2 = ChatColor.RED.toString().concat("/___\\ The event listener \"").concat(ChatColor.WHITE.toString()).concat(listener.getClass().getSimpleName()).concat(ChatColor.RED.toString()).concat("\" has been unregistered to prevent this.");
										HandlerList.unregisterAll(listener);
									} else {
										msg2 = ChatColor.RED.toString().concat("/___\\ The event listener \"").concat(ChatColor.WHITE.toString()).concat(listener.getClass().getSimpleName()).concat(ChatColor.RED.toString()).concat("\" was not unregistered, as it is either this plugin, WorldEdit or WorldGuard.");
									}
									this.getLogger().warning(msg1);
									if(msg2 != null) {
										this.getLogger().warning(msg2);
									}
								}
								sender.sendMessage(msg1);
								if(msg2 != null) {
									sender.sendMessage(msg2);
								}
							}
						}
					};
					if(args[1].equalsIgnoreCase("all")) {
						for(DamageCause c : DamageCause.values()) {
							dc[0] = c;
							code.run();
						}
					} else {
						code.run();
					}
				}
				if(!foundOne[0]) {
					sender.sendMessage(ChatColor.GREEN.toString().concat("[Entei's Skyblock] No plugins cancelled the EntityDamageEvent with cause \"").concat(ChatColor.WHITE.toString()).concat(cause == null ? "<ALL>" : cause.name()).concat(ChatColor.GREEN.toString()).concat("\"."));
				}
				return true;
			}
			if(args[0].equalsIgnoreCase("spawnTask")) {
				if(sender.hasPermission("skyblock.admin")) {
					sender.sendMessage(ChatColor.GREEN.toString().concat("The island spawn task is ").concat(Island.isSpawnTaskRunning() ? "" : "not").concat(" currently running."));
					return true;
				}
			}
			if(args[0].equalsIgnoreCase("reload")) {
				if(!sender.hasPermission("skyblock.dev") && !sender.hasPermission("skyblock.admin")) {
					sender.sendMessage(ChatColor.RED.toString().concat("You do not have permission to use this command."));
					return true;
				}
				Main.loadAll();
				sender.sendMessage(ChatColor.GREEN.toString().concat("[Entei's Skyblock] Re-loaded all configurations from file."));
				return true;
			}
			if(args[0].equalsIgnoreCase("reloadChest")) {
				if(!sender.hasPermission("skyblock.dev") && !sender.hasPermission("skyblock.admin")) {
					sender.sendMessage(ChatColor.RED.toString().concat("You do not have permission to use this command."));
					return true;
				}
				if(args.length == 1) {
					sender.sendMessage(ChatColor.YELLOW.toString().concat("Usage: \"").concat(ChatColor.WHITE.toString()).concat("/esb reloadChest {playerName}").concat(ChatColor.YELLOW.toString()).concat("\" - Reload the specified player's skyblock chest from file."));
					return true;
				}
				@SuppressWarnings("deprecation")
				OfflinePlayer target = Main.server.getOfflinePlayer(args[0]);
				String targetName = target.isOnline() ? target.getPlayer().getDisplayName() : target.getName();
				InventoryGUI.loadPlayersInventory(target);
				sender.sendMessage(ChatColor.GREEN.toString().concat("Re-loaded player ").concat(ChatColor.WHITE.toString()).concat(targetName).concat(ChatColor.GREEN.toString()).concat("'s island chest from file."));
				return true;
			}
			if(!(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))) {
				sender.sendMessage(ChatColor.YELLOW.toString().concat("[Entei's Skyblock] Unknown sub-command \"").concat(ChatColor.WHITE.toString()).concat(args[0]).concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat("\"."));
				args[0] = "help";
			}
			if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
				sender.sendMessage(ChatColor.GREEN.toString().concat("[Entei's Skyblock] Showing avaliable sub-commands:"));
				sender.sendMessage(ChatColor.WHITE.toString().concat("/esb [version]").concat(ChatColor.GREEN.toString()).concat(" - View this plugin's version."));
				sender.sendMessage(ChatColor.WHITE.toString().concat("/esb help").concat(ChatColor.GREEN.toString()).concat(" - View this help message."));
				sender.sendMessage(ChatColor.WHITE.toString().concat("/esb reload").concat(ChatColor.GREEN.toString()).concat(" - Reload all of this plugin's configuration files."));
				sender.sendMessage(ChatColor.WHITE.toString().concat("/esb reloadChest {playerName}").concat(ChatColor.GREEN.toString()).concat(" - Reload the specified player's skyblock chest from file."));
				return true;
			}
			return true;
		}
		if(command.equalsIgnoreCase("spot")) {
			if(!sender.hasPermission("skyblock.admin")) {
				return false;
			}
			if(user != null && args.length == 1 && (args[0].equalsIgnoreCase("animals") || args[0].equalsIgnoreCase("animal") || args[0].equalsIgnoreCase("mobs") || args[0].equalsIgnoreCase("monsters") || args[0].equalsIgnoreCase("monster") || args[0].equalsIgnoreCase("hostiles") || args[0].equalsIgnoreCase("hostile"))) {
				boolean animals = args[0].equalsIgnoreCase("animals") || args[0].equalsIgnoreCase("animal");
				Island island = Island.getIslandContaining(user.getLocation());
				if(island != null) {
					Collection<int[]> spots = island.getSpawnableBlocksFor(animals ? Animals.class : Monster.class);
					int[] closestSpot = Island.getSpotNearest(user.getLocation(), spots);
					if(closestSpot != null) {
						user.sendMessage(ChatColor.GRAY.toString().concat("The closest spot to you that ".concat(animals ? "animals" : "hostile mobs").concat(" may spawn on is at:")));
						user.sendMessage(ChatColor.GRAY.toString().concat("X: ".concat(Integer.toString(closestSpot[0]))).concat("; Y: ".concat(Integer.toString(closestSpot[1]))).concat("; Z: ".concat(Integer.toString(closestSpot[2]))).concat(";"));
					} else {
						user.sendMessage(ChatColor.GRAY.toString().concat("There are no spots on this island where ".concat(animals ? "animals" : "hostile mobs").concat(" may spawn")).concat(animals ? "." : "(excluding slimes)."));
					}
					return true;
				}
			}
			return false;
		}
		if(command.equalsIgnoreCase("iw") || command.equalsIgnoreCase("islandWarp")) {
			command = "island";
			String[] tmp = args;
			args = new String[tmp.length + 1];
			args[0] = "warp";
			for(int i = 1; i < args.length; i++) {
				args[i] = tmp[i - 1];
			}
		}
		if((command.equalsIgnoreCase("ih") || command.equalsIgnoreCase("hi") || command.equalsIgnoreCase("ig")) && args.length >= 0) {
			command = "island";
			String[] mkArgs = new String[1 + args.length];
			mkArgs[0] = "home";
			for(int i = 1; i < mkArgs.length && i - 1 < args.length; i++) {
				mkArgs[i] = args[i - 1];
			}
			args = mkArgs;
		}
		if((command.equalsIgnoreCase("island") || command.equalsIgnoreCase("is")) && args.length >= 1 && args[0].equalsIgnoreCase("unlock")) {
			command = "island";
			String[] mkArgs = new String[2 + (args.length - 1)];
			mkArgs[0] = "lock";
			mkArgs[1] = "off";
			for(int i = 2; i < mkArgs.length && i - 2 < args.length; i++) {
				mkArgs[i] = args[i - 2];
			}
			args = mkArgs;
		}
		if(command.equalsIgnoreCase("plotworld") || command.equalsIgnoreCase("creative")) {
			if(user != null) {
				World world = Main.server.getWorld("plotworld");
				if(world != null) {
					safeTeleport(user, new Location(world, 0.5, 64, 0.5));
					return true;
				}
			}
			return false;
		}
		if(command.equalsIgnoreCase("is") || command.equalsIgnoreCase("island")) {
			if(user != null) {
				if(args.length >= 1 && args[0].equalsIgnoreCase("spawnMobs") && user.hasPermission("skyblock.admin")) {
					Island island = Island.getIslandContaining(user.getLocation());
					if(island == null) {
						user.sendMessage(ChatColor.RED.toString().concat("You are not standing on an island."));
						return true;
					}
					boolean ignoreCramming = false, honorSpawnLimit = true;
					if(args.length >= 2) {
						ignoreCramming = Boolean.parseBoolean(args[1].toLowerCase());
					}
					if(args.length >= 3) {
						honorSpawnLimit = Boolean.parseBoolean(args[2].toLowerCase());
					}
					user.sendMessage(ChatColor.GRAY.toString().concat("Hostile mobs (to be)spawned on this island: ").concat(ChatColor.WHITE.toString()).concat(Integer.toString(Island.spawnHostileMobsOn(island, ignoreCramming, honorSpawnLimit))));
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("testIsland")) {
					if(!user.hasPermission("skyblock.admin")) {
						user.sendMessage(ChatColor.DARK_RED.toString().concat("You do not have permission to use this command."));
						return true;
					}
					for(Island island : Island.getAllIslands()) {
						if(island.isMember(user) && island.isTestIsland()) {
							if(island.getHomeFor(user.getUniqueId()) != null) {
								safeTeleport(user, island.getHomeFor(user.getUniqueId()));
							} else {
								safeTeleport(user, island.getSpawnLocation());
							}
							return true;
						}
					}
				}
				Island island = Island.getMainIslandFor(user);
				/*if(args.length >= 1 && (args[0].equalsIgnoreCase("mobs") || args[0].equalsIgnoreCase("animals"))) {
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(args[0].equalsIgnoreCase("mobs") && !user.hasPermission("skyblock.changeMobSpawning")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to enable, disable, or change the hostile mob spawning settings for this island.");
						return true;
					}
					if(args[0].equalsIgnoreCase("animals") && !user.hasPermission("skyblock.changeAnimalSpawning")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to enable, disable, or change the animal spawning settings for this island.");
						return true;
					}
					if(args.length == 1) {
						if(args[0].equalsIgnoreCase("mobs")) {
							sender.sendMessage(ChatColor.GREEN + "Hostile mob spawning is " + (island.isMobSpawningEnabled() ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled") + ChatColor.GREEN + " for this island.");
						} else {
							sender.sendMessage(ChatColor.GREEN + "Animal spawning is " + (island.isAnimalSpawningEnabled() ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled") + ChatColor.GREEN + " for this island.");
						}
						return true;
					}
					if(args.length == 2) {
						if(args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("enabled")) {
							if(args[0].equalsIgnoreCase("mobs")) {
								island.setMobSpawningEnabled(true);
								sender.sendMessage(ChatColor.GREEN + "Hostile mob spawning is now " + (island.isMobSpawningEnabled() ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled") + ChatColor.GREEN + " for this island.");
							} else {
								island.setAnimalSpawningEnabled(true);
								sender.sendMessage(ChatColor.GREEN + "Animal spawning is now " + (island.isAnimalSpawningEnabled() ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled") + ChatColor.GREEN + " for this island.");
							}
							return true;
						} else if(args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("disabled")) {
							if(args[0].equalsIgnoreCase("mobs")) {
								island.setMobSpawningEnabled(false);
								sender.sendMessage(ChatColor.GREEN + "Hostile mob spawning is now " + (island.isMobSpawningEnabled() ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled") + ChatColor.GREEN + " for this island.");
							} else {
								island.setAnimalSpawningEnabled(false);
								sender.sendMessage(ChatColor.GREEN + "Animal spawning is now " + (island.isAnimalSpawningEnabled() ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled") + ChatColor.GREEN + " for this island.");
							}
							return true;
						}
					}
					sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + (args[0].equalsIgnoreCase("mobs") ? "mobs" : "animals") + " [on|off]" + ChatColor.DARK_GREEN + ": Turn hostile mob or animal spawning on or off for this island");
					return true;
				}*/
				if(args.length >= 1 && args[0].equalsIgnoreCase("chest")) {
					if(user.hasPermission("skyblock.chest")) {
						if(Island.isInSkyworld(user) || Main.isWorldAllowedForIslandChests(user.getWorld())) {
							InventoryGUI.getStorageChestForPlayer(user).show(user);
						} else {
							sender.sendMessage(ChatColor.RED + "You can only access your storage chest in the skyworld and related areas.");
						}
						return true;
					}
					sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command.");
					return true;
				}
				if(args.length == 2 && (args[0].equalsIgnoreCase("trust") || args[0].equalsIgnoreCase("untrust"))) {
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(args[1].equalsIgnoreCase("list")) {
						String[] names = island.getTrustedPlayerNames();
						if(names.length == 0) {
							sender.sendMessage(ChatColor.YELLOW + "There are no trusted players on this island.");
							return true;
						}
						sender.sendMessage(ChatColor.GREEN + "Listing trusted players for this island:");
						for(String name : names) {
							sender.sendMessage(ChatColor.WHITE + name);
						}
						return true;
					}
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Main.server.getOfflinePlayer(args[1]);
					if(target == null) {
						sender.sendMessage(ChatColor.RED + "The player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\" does not exist.");
						return true;
					}
					if(island.isMember(target)) {
						sender.sendMessage(ChatColor.YELLOW + "That player is a member of the island, and cannot be trusted or untrusted.");
						return true;
					}
					if(args[0].equalsIgnoreCase("trust")) {
						island.addTrusted(target);
						sender.sendMessage(ChatColor.DARK_GREEN + "Player \"" + ChatColor.WHITE + (target.getName() == null ? args[1] : target.getName()) + ChatColor.RESET + ChatColor.DARK_GREEN + "\" is now trusted on this island. They may build and use blocks and items, but they cannot complete challenges or change any island settings.");
						if(target.isOnline()) {
							target.getPlayer().sendMessage(ChatColor.GREEN.toString().concat("You are now trusted on ").concat(ChatColor.WHITE.toString()).concat(island.getOwnerName()).concat(ChatColor.RESET.toString()).concat(ChatColor.GREEN.toString()).concat("'s island(ID: ").concat(island.getID()).concat(")!"));
						}
					} else {
						island.removeTrusted(target);
						sender.sendMessage(ChatColor.DARK_GREEN + "Player \"" + ChatColor.WHITE + (target.getName() == null ? args[1] : target.getName()) + ChatColor.RESET + ChatColor.DARK_GREEN + "\" is no longer trusted on this island.");
						if(target.isOnline()) {
							target.getPlayer().sendMessage(ChatColor.YELLOW.toString().concat("You are no longer trusted on ").concat(ChatColor.WHITE.toString()).concat(island.getOwnerName()).concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat("'s island(ID: ").concat(island.getID()).concat(")."));
						}
					}
					return true;
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("create")) {
					user.closeInventory();
					if(island == null) {
						String type = GeneratorMain.island_schematic.equalsIgnoreCase("none") ? "normal" : "schematic";
						if(args.length >= 2 && (type.equalsIgnoreCase("normal") ? false : (!args[1].equalsIgnoreCase("schematic") && (args.length >= 3 ? !args[2].equalsIgnoreCase("nearspawn") : true)))) {//XXX Disabled island type and position selection
							sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " create" + ChatColor.DARK_GREEN + ": Creates a new island for you to play on");
							return true;
						}
						if(args.length == 1) {
							island = Island.getNextAvailableIslandNearSpawn();
						} else if(args.length >= 2) {
							type = args[1];
							if(!type.equalsIgnoreCase("normal") && !type.equalsIgnoreCase("square") && !type.equalsIgnoreCase("schematic")) {
								sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " create [normal|square] [nearspawn|random|faraway]" + ChatColor.DARK_GREEN + ": Creates a new island for you to play on");
								return true;
							}
							String location = "nearspawn";
							if(args.length == 3) {
								location = args[2];
							} else if(args.length > 3) {
								sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " create [normal|square] [nearspawn|random|faraway]" + ChatColor.DARK_GREEN + ": Creates a new island for you to play on");
								return true;
							}
							if(location.equalsIgnoreCase("nearspawn")) {
								island = Island.getNextAvailableIslandNearSpawn();
							} else if(location.equalsIgnoreCase("random")) {
								island = Island.getNextRandomlyLocatedIsland();
							} else if(location.equalsIgnoreCase("faraway")) {
								island = Island.getNextFarAwayIsland();
							} else {
								sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " create [normal|square] [nearspawn|random|faraway]" + ChatColor.DARK_GREEN + ": Creates a new island for you to play on");
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " create [normal|square] [nearspawn|random|faraway]" + ChatColor.DARK_GREEN + ": Creates a new island for you to play on");
							return true;
						}
						island.setOwner(user).update();
						if(type.equalsIgnoreCase("normal")) {
							island.generateIsland();
						} else if(type.equalsIgnoreCase("square")) {
							island.generateSquareIsland();
						} else if(type.equalsIgnoreCase("schematic")) {
							island.generateSchematicIsland(true);
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "ERROR: Unknown/unimplemented island type provided: " + type);
							throw new IllegalStateException("Unknown/unimplemented island type provided: " + type);
						}
						if(!type.equalsIgnoreCase("schematic")) {
							safeTeleport(user, island.getSpawnLocation());
						}
						sender.sendMessage(ChatColor.GREEN + "Successfully created your island. Have fun!");
						sender.sendMessage(ChatColor.GREEN + "To invite other players, type \"" + ChatColor.WHITE + "/island invite {playername}" + ChatColor.GREEN + "\".");
						return true;
					}
					sender.sendMessage(ChatColor.RED + "You are already a member of an island!");
					return true;
				} else if(args.length == 2 && args[0].equalsIgnoreCase("join")) {
					if(island != null) {
						sender.sendMessage(ChatColor.RED + "You are already a member of an island!");
						return true;
					}
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Main.server.getOfflinePlayer(args[1]);
					if(target == null) {
						sender.sendMessage(ChatColor.RED + "The player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\" does not exist.");
						return true;
					}
					island = Island.getMainIslandFor(target);
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "Player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\" is not a member of an island.");
						return true;
					}
					JoinRequestResult result = island.addJoinRequest(user.getUniqueId());
					if(result == JoinRequestResult.FAILURE) {
						sender.sendMessage(ChatColor.RED + "Failed to send the join request. Please contact a server administrator for assistance.");
						return true;
					}
					if(result == JoinRequestResult.ISLAND_FULL) {
						sender.sendMessage(ChatColor.YELLOW + "That island has reached its' member capacity of " + ChatColor.WHITE + Integer.toString(island.getMemberLimit(), 10) + ChatColor.YELLOW + ".");
						return true;
					}
					if(result == JoinRequestResult.JOINED) {
						sender.sendMessage(ChatColor.GREEN + "You are now a member of " + ChatColor.WHITE + island.getOwnerName() + ChatColor.RESET + ChatColor.GREEN + "'s island!");
						island.sendMessage(ChatColor.GREEN + "Player \"" + ChatColor.WHITE + user.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" has joined the island!");
						safeTeleport(user, island.getSpawnLocation());
					}
					return true;
				} else if(args.length == 2 && args[0].equalsIgnoreCase("decline")) {
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Main.server.getOfflinePlayer(args[1]);
					if(target == null) {
						sender.sendMessage(ChatColor.RED + "The player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\" does not exist.");
						return true;
					}
					island = Island.getMainIslandFor(target);
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "Player \"" + ChatColor.WHITE + (target.isOnline() ? target.getPlayer().getDisplayName() : target.getName()) + ChatColor.RESET + ChatColor.RED + "\" is not a member of an island.");
						return true;
					}
					island.removeInvitation(user.getUniqueId());
					sender.sendMessage(ChatColor.GREEN + "Successfully declined " + ChatColor.WHITE + (target.isOnline() ? target.getPlayer().getDisplayName() : target.getName()) + ChatColor.RESET + ChatColor.GREEN + "'s invitation.");
					return true;
				} else if(args.length == 2 && args[0].equalsIgnoreCase("invite")) {
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Main.server.getOfflinePlayer(args[1]);
					if(target == null) {
						sender.sendMessage(ChatColor.RED + "The player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\" does not exist.");
						return true;
					}
					if(Island.getMainIslandFor(target) != null) {
						sender.sendMessage(ChatColor.RED + "Player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\" is already a member of an island.");
						return true;
					}
					InvitationResult result = island.sendInvitation(user, target.getUniqueId());
					if(result == InvitationResult.FAILURE) {
						sender.sendMessage(ChatColor.RED + "Failed to send the invitation. Please contact a server administrator for assistance.");
						return true;
					}
					if(result == InvitationResult.ISLAND_FULL) {
						sender.sendMessage(ChatColor.YELLOW + "Your island has reached its' member capacity of " + ChatColor.WHITE + Integer.toString(island.getMemberLimit(), 10) + ChatColor.YELLOW + ".");
						return true;
					}
					if(result == InvitationResult.JOINED) {
						if(target.isOnline()) {
							target.getPlayer().sendMessage(ChatColor.GREEN + "You are now a member of " + ChatColor.WHITE + island.getOwnerName() + ChatColor.RESET + ChatColor.GREEN + "'s island!");
							safeTeleport(target.getPlayer(), island.getSpawnLocation());
						}
						island.sendMessage(ChatColor.GREEN + "Player \"" + ChatColor.WHITE + (target.isOnline() ? target.getPlayer().getDisplayName() : target.getName()) + ChatColor.RESET + ChatColor.GREEN + "\" has joined the island!");
					}
					return true;
				} else if(args.length == 1 && args[0].equalsIgnoreCase("sapling")) {
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island" + ChatColor.GREEN + "\".");
						return true;
					}
					if(!Island.isInSkyworld(user)) {
						sender.sendMessage(ChatColor.RED + "You must be in the skyworld or one of its dimensions to request a replacement sapling.");
						return true;
					}
					final Island is = island;
					Thread thread = new Thread(() -> {
						boolean hasLeaves = false;
						boolean hasSaplings = false;
						for(ItemStack item : user.getInventory()) {
							if(item != null && item.getType() == Material.OAK_SAPLING) {
								hasSaplings = true;
								break;
							}
						}
						if(!hasSaplings) {
							int[] bounds = is.getBounds();
							for(int x = bounds[0]; x <= bounds[2]; x++) {
								for(int y = 0; y < GeneratorMain.getSkyworld().getMaxHeight(); y++) {
									for(int z = bounds[1]; z <= bounds[3]; z++) {
										int[] xz = Main.getWorldToChunkCoords(x, z);
										if(GeneratorMain.getSkyworld().isChunkLoaded(xz[0], xz[1])) {
											Block block = GeneratorMain.getSkyworld().getBlockAt(x, y, z);
											if(block.getType().name().contains("_LEAVES")) {
												hasLeaves = true;
											} else if(block.getType() == Material.OAK_SAPLING) {
												hasSaplings = true;
											}
										}
									}
								}
							}
						}
						if(!hasSaplings && !hasLeaves) {
							for(Entity entity : GeneratorMain.getSkyworld().getNearbyEntities(is.getLocation(), GeneratorMain.island_Range / 2, GeneratorMain.getSkyworld().getMaxHeight() / 2, GeneratorMain.island_Range / 2)) {
								if(entity instanceof Item) {
									Item item = (Item) entity;
									if(item.getItemStack() != null && item.getItemStack().getType() == Material.OAK_SAPLING) {
										hasSaplings = true;
										break;
									}
								} else if(entity instanceof InventoryHolder && !(entity instanceof Monster)) {
									InventoryHolder invHolder = (InventoryHolder) entity;
									Inventory inv = invHolder.getInventory();
									if(inv != null) {
										for(ItemStack item : inv.getContents()) {
											if(item == null) {
												continue;
											}
											if(item.getType() == Material.OAK_SAPLING) {
												hasSaplings = true;
												break;
											} else if(item.getType().name().contains("_LEAVES")) {
												hasLeaves = true;
												break;
											}
										}
										if(hasSaplings || hasLeaves) {
											break;
										}
									}
								}
							}
						}
						if(!hasSaplings && !hasLeaves) {
							Main.scheduler.runTask(getPlugin(), () -> {
								GeneratorMain.getSkyworld().dropItem(user.getLocation(), new ItemStack(Material.OAK_SAPLING, 1));
							});
							return;
						}
						if(!hasSaplings) {
							user.sendMessage(ChatColor.YELLOW + "There are some leaves on your island, try mining those first!");
						} else {
							user.sendMessage(ChatColor.YELLOW + "There are some saplings on your island. If you need more, grow some!");
						}
					}, "IslandSaplingSearchThread");
					thread.setDaemon(true);
					thread.start();
					return true;
				} else if(args.length == 1 && args[0].equalsIgnoreCase("home")) {
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					safeTeleport(user, island.getHomeFor(user.getUniqueId()));
					return true;
				} else if(args.length == 1 && args[0].equalsIgnoreCase("sethome")) {
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(island.setHomeFor(user.getUniqueId(), user.getLocation()).getHomeFor(user.getUniqueId()).equals(user.getLocation())) {
						sender.sendMessage(ChatColor.GREEN + "Your island home has been set to your current location.");
					} else {
						sender.sendMessage(ChatColor.RED + "Island homes can only be set within your island!");
					}
					return true;
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("warp")) {
					if(args.length == 1) {
						if(island == null) {
							sender.sendMessage(ChatColor.RED + "You are not a member of an island!");
							sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
							return true;
						}
						safeTeleport(user, island.getWarpLocation());
						sender.sendMessage(ChatColor.GREEN + "Taking you to " + (island.isOwner(user) ? "your" : "\"" + ChatColor.WHITE + island.getOwnerName() + ChatColor.RESET + ChatColor.GREEN + "\"'s") + " island.");
						return true;
					}
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Main.server.getOfflinePlayer(args[1]);
					if(target != null) {
						island = Island.getMainIslandFor(target);
						if(island != null) {
							if(island.isLocked() && !island.isMember(user)) {
								sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + island.getOwnerName() + ChatColor.RESET + ChatColor.YELLOW + "\"'s island is currently " + ChatColor.RED + "locked" + ChatColor.YELLOW + ".");
								return true;
							}
							safeTeleport(user, island.getWarpLocation());
							sender.sendMessage(ChatColor.GREEN + "Taking you to " + (island.isOwner(user) ? "your" : "\"" + ChatColor.WHITE + island.getOwnerName() + ChatColor.RESET + ChatColor.GREEN + "\"'s") + " island.");
							return true;
						}
						sender.sendMessage(ChatColor.RED + "Player \"" + ChatColor.WHITE + target.getName() + ChatColor.RESET + ChatColor.RED + "\" is not a member of an island.");
						return true;
					}
					sender.sendMessage(ChatColor.RED + "The player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\" does not exist.");
					return true;
				} else if(args.length == 1 && args[0].equalsIgnoreCase("setwarp")) {
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(!island.isOwner(user)) {
						sender.sendMessage(ChatColor.RED + "You are not the owner of the island, and so you cannot set its warp location.");
						return true;
					}
					if(!island.isOnIsland(user.getLocation())) {
						sender.sendMessage(ChatColor.RED + "Your island's warp can only be set on your island!");
						return true;
					}
					island.setWarpLocation(user.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Your island's warp has been set to your current location.");
					return true;
				} else if(args.length == 1 && args[0].equalsIgnoreCase("spawn")) {
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					safeTeleport(user, island.getSpawnLocation());
					return true;
				} else if(args.length == 1 && args[0].equalsIgnoreCase("restart")) {
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(!island.isOwner(user)) {
						sender.sendMessage(ChatColor.RED + "You are not the owner of the island, and so you cannot restart it.");
						return true;
					}
					if(!island.canRestart() && !(user.hasPermission("skyblock.dev") || user.hasPermission("skyblock.admin"))) {
						sender.sendMessage(ChatColor.YELLOW + "You'll need to wait " + ChatColor.GOLD + getLengthOfTime(island.getNextRestartTime()) + ChatColor.YELLOW + " until you can restart the island again.");
						return true;
					}
					island.sendMessage(ChatColor.YELLOW.toString().concat("Player \"").concat(ChatColor.WHITE.toString()).concat(user.getDisplayName()).concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat("\" just restarted the island."));
					for(Player p : island.getPlayersOnIsland()) {
						safeTeleport(p, GeneratorMain.getSkyworldSpawnLocation());
						if(!island.isMember(p)) {
							p.sendMessage(ChatColor.YELLOW + "The island you were just at was just restarted.");
						}
					}
					island.restart(true);
					//safeTeleport(user, island.getSpawnLocation());
					return true;
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("leave")) {
					if(island == null) {
						sender.sendMessage(ChatColor.RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(island.isOwner(user)) {
						if(args.length == 2) {
							@SuppressWarnings("deprecation")
							OfflinePlayer p = Main.server.getOfflinePlayer(args[1]);
							if(p == null) {
								sender.sendMessage(ChatColor.RED + "The player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\" does not exist.");
								return true;
							}
							if(p.getUniqueId().equals(user.getUniqueId())) {
								sender.sendMessage(ChatColor.GREEN + "Alright, leaving the island in your capable hands.");
								return true;
							}
							if(!island.isMember(p)) {
								sender.sendMessage(ChatColor.RED + "The player \"" + ChatColor.WHITE + p.getName() + ChatColor.RESET + ChatColor.RED + "\" is not a member of this island.");
								return true;
							}
							island.setOwner(p);
							sender.sendMessage(ChatColor.GREEN + "The player \"" + ChatColor.WHITE + p.getName() + ChatColor.RESET + ChatColor.GREEN + "\" is now the owner of this island.");
							island.removeMember(user);
							sender.sendMessage(ChatColor.YELLOW + "You are no longer a member of the island.");
							safeTeleport(user, GeneratorMain.getSkyworldSpawnLocation());
							island.save();
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " leave <playername>" + ChatColor.YELLOW + " To leave the island in the hands of a fellow member.");
							sender.sendMessage(ChatColor.GREEN + "If you just want to delete your island, use " + ChatColor.WHITE + "/" + command + ChatColor.DARK_RED + " delete" + ChatColor.GREEN + ", but know that this cannot be undone!");
						}
						return true;
					}
					if(args.length == 1) {
						sender.sendMessage(ChatColor.YELLOW + "Warning! Once you leave the island, your inventory will be wiped,");
						sender.sendMessage(ChatColor.YELLOW + "and you will have to request to join it again.");
						sender.sendMessage(ChatColor.YELLOW + "To confirm, type \"" + ChatColor.WHITE + "/island leave confirm" + ChatColor.YELLOW + "\".");
					} else if(args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
						island.removeMember(user);
						sender.sendMessage(ChatColor.YELLOW + "You are no longer a member of the island.");
						safeTeleport(user, GeneratorMain.getSkyworldSpawnLocation());
						island.save();
					} else {
						sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " leave");
					}
					return true;
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("delete")) {
					if(island == null) {
						sender.sendMessage(ChatColor.DARK_RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(!island.isOwner(user)) {
						sender.sendMessage(ChatColor.RED + "You are not the owner of the island, and so you cannot delete it.");
						return true;
					}
					if(args.length == 1) {
						sender.sendMessage(ChatColor.YELLOW + "Warning! Deleting the island is permanent, and cannot be undone.");
						sender.sendMessage(ChatColor.YELLOW + "All members will have their inventories wiped(including you),");
						sender.sendMessage(ChatColor.YELLOW + "and will have to join or create another island.");
						sender.sendMessage(ChatColor.YELLOW + "To confirm, type \"" + ChatColor.WHITE + "/island delete confirm" + ChatColor.YELLOW + "\".");
					} else if(args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
						if(island.isPlayerOnIsland(user)) {
							safeTeleport(user, GeneratorMain.getSkyworldSpawnLocation());
						}
						for(Player p : island.getPlayersOnIsland()) {
							safeTeleport(p, GeneratorMain.getSkyworldSpawnLocation());
							if(!island.isMember(p)) {
								p.sendMessage(ChatColor.YELLOW + "The island you were just at was deleted.");
							}
						}
						island.wipeMembersInventories(ChatColor.YELLOW + "The island you were a member of has been deleted.").deleteIsland();
						user.sendMessage(ChatColor.YELLOW + "The island has been deleted.");
					} else {
						sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " delete");
					}
					return true;
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("biome")) {
					if(island == null) {
						sender.sendMessage(ChatColor.DARK_RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(args.length == 2) {
						if(!island.isOwner(user)) {
							sender.sendMessage(ChatColor.RED + "You are not the owner of the island, and so you cannot change the biome.");
							return true;
						}
						Biome b = null;
						try {
							b = Biome.valueOf(args[1].toUpperCase());
						} catch(IllegalArgumentException ignored) {
						}
						if(b == null) {
							sender.sendMessage(ChatColor.RED + "\"" + ChatColor.WHITE + args[1] + ChatColor.RED + "\" is not a valid biome enum name.");
							return true;
						}
						island.setBiome(b);
						island.save();
					}
					sender.sendMessage(ChatColor.GREEN + "The island's biome is" + (args.length == 2 ? " now" : "") + ": " + ChatColor.WHITE + island.getBiome().name());
					return true;
				} else if(args.length == 1 && args[0].equalsIgnoreCase("info")) {
					String level = limitDecimalToNumberOfPlaces(island.getLevel(), 4);
					String owner = island.getOwnerName();
					String[] members = island.getMemberNames();
					sender.sendMessage(ChatColor.BLUE + "Island id: " + ChatColor.WHITE + island.getID());
					sender.sendMessage(ChatColor.BLUE + "Owner: " + ChatColor.WHITE + owner);
					sender.sendMessage(ChatColor.BLUE + "Level: " + ChatColor.WHITE + level);
					sender.sendMessage(ChatColor.BLUE + "Members:");
					for(String member : members) {
						sender.sendMessage(ChatColor.WHITE + member);
					}
					//TODO Finish this command - add island block totals in descending order from largest to smallest
					return true;
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("level")) {
					if(island == null) {
						sender.sendMessage(ChatColor.DARK_RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(args.length > 1) {
						@SuppressWarnings("deprecation")
						OfflinePlayer player = Main.server.getOfflinePlayer(args[1]);
						if(player == null) {
							sender.sendMessage(ChatColor.RED + "The player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\" does not exist.");
							return true;
						}
						island = Island.getMainIslandFor(player);
						if(island == null) {
							sender.sendMessage(ChatColor.RED + "That player is not a member of an island.");
							return true;
						}
						sender.sendMessage(ChatColor.WHITE + island.getOwnerName() + ChatColor.RESET + ChatColor.GREEN + "'s island's level is: " + ChatColor.BLUE + limitDecimalToNumberOfPlaces(island.getLevel(), 2));
						return true;
					}
					final Island is = island;
					sender.sendMessage(ChatColor.GREEN + "Calculating level, please wait...");
					Main.scheduler.runTaskAsynchronously(getPlugin(), () -> {
						sender.sendMessage(ChatColor.GREEN + "The island's level is: " + ChatColor.BLUE + limitDecimalToNumberOfPlaces(is.calculateLevel(), 4));
					});
					return true;
				} else if((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("top")) {
					if(island == null) {
						sender.sendMessage(ChatColor.DARK_RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					int page = 0;
					if(args.length == 2) {
						if(!isInt(args[1])) {
							sender.sendMessage(ChatColor.YELLOW + "\"" + args[1] + "\" is not a valid integer.");
							return true;
						}
						page = Integer.parseInt(args[1]);
						if(page < 0) {
							sender.sendMessage(ChatColor.YELLOW + "The page number must be greater than or equal to zero!");
							return true;
						}
					}
					ArrayList<Island> islands = new ArrayList<>(Island.getAllIslands());
					ArrayList<Island> sorted = new ArrayList<>(islands.size());
					int size = islands.size();
					int maxPages = size / 10;
					if(page > maxPages) {
						sender.sendMessage(ChatColor.YELLOW + "The page number that you specified(" + ChatColor.GOLD + Integer.toString(page) + ChatColor.YELLOW + ") is larger than the available pages(" + ChatColor.GOLD + Integer.toString(maxPages) + ChatColor.YELLOW + ").");
						return true;
					}
					for(int i = 0; i < size; i++) {
						Island highestIsland = null;
						for(Island is : islands) {
							if(is.isTestIsland() || is.getOwner() == null) {
								continue;
							}
							if(highestIsland == null) {
								highestIsland = is;
								continue;
							}
							if(is.getLevel() >= highestIsland.getLevel()) {
								highestIsland = is;
							}
						}
						if(highestIsland != null) {
							islands.remove(highestIsland);
							boolean canAdd = true;
							for(Island is : sorted) {
								if(is.getOwner().toString().equals(highestIsland.getOwner().toString())) {
									canAdd = false;
									break;
								}
							}
							if(canAdd) {
								sorted.add(highestIsland);
							}
						}
					}
					sender.sendMessage(ChatColor.DARK_AQUA + "Island top levels:");
					int i = 1;
					for(Island is : sorted) {
						if((page * 10) > i) {
							i++;
							continue;
						}
						sender.sendMessage(ChatColor.GOLD + "#" + Integer.toString(i) + ChatColor.WHITE + ": " + is.getOwnerName() + ChatColor.RESET + ChatColor.DARK_AQUA + " with " + limitDecimalToNumberOfPlaces(is.getLevel(), 4));
						i++;
					}
					return true;
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("kick")) {
					if(island == null) {
						sender.sendMessage(ChatColor.DARK_RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(!island.isOwner(user)) {
						sender.sendMessage(ChatColor.RED + "You are not the owner of the island, and so you cannot kick other island members.");
						return true;
					}
					if(args.length == 1) {
						sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " kick {memberName}" + ChatColor.DARK_GREEN + ": Kick the given member from the island if you are the owner");
						return true;
					}
					@SuppressWarnings("deprecation")
					OfflinePlayer player = Main.server.getOfflinePlayer(args[1]);
					if(player == null) {
						sender.sendMessage(ChatColor.RED + "The player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\" does not exist.");
						return true;
					}
					if(!island.isMember(player)) {
						sender.sendMessage(ChatColor.RED + "The player \"" + ChatColor.WHITE + player.getName() + ChatColor.RESET + ChatColor.RED + "\" is not a member of this island.");
						return true;
					}
					if(island.isOwner(player)) {
						sender.sendMessage(ChatColor.RED + "You are the owner of the island, you can't kick yourself!");
						sender.sendMessage(ChatColor.YELLOW + "To leave the island, type \"" + ChatColor.WHITE + "/island leave {memberName}" + ChatColor.YELLOW + "\", where " + ChatColor.WHITE + "{memberName}" + ChatColor.YELLOW + " is the name of the player that you wish to set as the new island owner.");
						return true;
					}
					if(island.removeMember(player)) {
						sender.sendMessage(ChatColor.GREEN + "Successfully removed player \"" + ChatColor.WHITE + (player.isOnline() ? player.getPlayer().getDisplayName() : player.getName()) + ChatColor.RESET + ChatColor.GREEN + "\" from the island.");
					} else {
						sender.sendMessage(ChatColor.YELLOW + "Unable to remove player \"" + ChatColor.WHITE + (player.isOnline() ? player.getPlayer().getDisplayName() : player.getName()) + ChatColor.RESET + ChatColor.YELLOW + "\" from the island.");
					}
					return true;
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("lock")) {
					if(island == null) {
						sender.sendMessage(ChatColor.DARK_RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(args.length == 2) {
						if(!island.isOwner(user)) {
							sender.sendMessage(ChatColor.RED + "You are not the owner of the island, and so you cannot change the lock state.");
							return true;
						}
						if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes") || args[1].equalsIgnoreCase("y") || args[1].equalsIgnoreCase("lock")) {
							island.setLocked(true);
						} else if(args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("no") || args[1].equalsIgnoreCase("n") || args[1].equalsIgnoreCase("unlock")) {
							island.setLocked(false);
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " lock [true/on|false/off]" + ChatColor.DARK_GREEN + ": Set or view whether or not your island is locked to non-members");
							return true;
						}
					}
					sender.sendMessage(ChatColor.GREEN + "The island is " + (args.length == 2 ? "now " : "") + (island.isLocked() ? ChatColor.DARK_RED + "locked" : "unlocked") + ChatColor.GREEN + ".");
					return true;
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("pvp")) {
					if(island == null) {
						sender.sendMessage(ChatColor.DARK_RED + "You are not a member of an island!");
						sender.sendMessage(ChatColor.GREEN + "To get started, type \"" + ChatColor.WHITE + "/island create" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/island join {memberName}" + ChatColor.GREEN + "\".");
						return true;
					}
					if(args.length == 2) {
						if(!island.isOwner(user)) {
							sender.sendMessage(ChatColor.RED + "You are not the owner of the island, and so you cannot change the lock state.");
							return true;
						}
						if(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("yes") || args[1].equalsIgnoreCase("y") || args[1].equalsIgnoreCase("allow") || args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("fight")) {
							island.setPVPAllowed(true);
						} else if(args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("no") || args[1].equalsIgnoreCase("n") || args[1].equalsIgnoreCase("deny") || args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("rest")) {
							island.setPVPAllowed(false);
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " lock [true/on|false/off]" + ChatColor.DARK_GREEN + ": Set or view whether or not your island is locked to non-members");
							return true;
						}
					}
					sender.sendMessage(ChatColor.GREEN + "PVP is " + (args.length == 2 ? "now " : "") + (island.allowPVP() ? ChatColor.DARK_RED + "allowed" : ChatColor.YELLOW + "denied") + ChatColor.GREEN + ".");
					return true;
				} else if(args.length >= 1 || (args.length >= 1 && args[0].equalsIgnoreCase("help"))) {
					sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.DARK_GREEN + ": Display the island main menu");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " create [normal|square] [nearspawn|random|faraway]" + ChatColor.DARK_GREEN + ": Creates a new island for you to play on");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " join {memberName}" + ChatColor.DARK_GREEN + ": Lets the island members know that you'd like to join");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " invite {playerName}" + ChatColor.DARK_GREEN + ": Invites the player to come join your island");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " home" + ChatColor.DARK_GREEN + ": Teleports you to your island home");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " sethome" + ChatColor.DARK_GREEN + ": Sets your personal island home");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " warp [playerName]" + ChatColor.DARK_GREEN + ": Teleports you to the given player's island's warp location, or to yours if none is specified");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " setwarp" + ChatColor.DARK_GREEN + ": Sets your island's warp location if you are the owner.");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " help" + ChatColor.DARK_GREEN + ": Display this help message");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " info" + ChatColor.DARK_GREEN + ": Display information about your island");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " level" + ChatColor.DARK_GREEN + ": Show your island's level by tallying up all the blocks on it");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " top [page]" + ChatColor.DARK_GREEN + ": Show everyone's island levels in order of highest to lowest");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " kick {memberName}" + ChatColor.DARK_GREEN + ": Kick the given member from the island if you are the owner");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " pvp {on|off}" + ChatColor.DARK_GREEN + ": Enable or disable Player-Vs-Player on your island if you are the owner");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " trust {playerName|list}" + ChatColor.DARK_GREEN + ": Trust the given player on your island, or list the trusted players on your island. If trusting, the player will be able to build and access items and chests, but will be unable to change any island settings.");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " untrust {playerName|list}" + ChatColor.DARK_GREEN + ": Untrust the given player on your island.");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " lock [true/on|false/off]" + ChatColor.DARK_GREEN + ": Set or view whether or not your island is locked to non-members");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " biome {biome}" + ChatColor.DARK_GREEN + ": Set or view your island's biome");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " {mobs|animals} [on|off]" + ChatColor.DARK_GREEN + ": Enable/disable hostile mob/animal spawning for the island");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " leave" + ChatColor.GREEN + ": Allows you to leave the island. If you are the owner, you'll need to pass in a fellow member's name here so that they can be the new owner");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " restart" + ChatColor.YELLOW + ": Allows you to restart the island if you are the owner. All of the island's blocks will be wiped and the island will be regenerated back to its' initial state.");
					sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " delete" + ChatColor.RED + ": Allows you to completely delete the island if you are the owner. All of the island's blocks will be wiped, and all of its' members(including you) will have their inventories wiped as well.");
					return true;
				}
				//XXX /island gui
				InventoryGUI gui = new InventoryGUI(ChatColor.DARK_GREEN + "Skyblock Menu", 18) {
					
					@Override
					public void onClick(int slot, Player player, Inventory inventory) {
						//ItemStack clicked = this.getSlotIcon(slot, false);
						//String name = clicked == null ? "nothing" : clicked.hasItemMeta() ? clicked.getItemMeta().hasDisplayName() ? clicked.getItemMeta().getDisplayName() : clicked.getType().name() : clicked.getType().name();
						//player.sendMessage(ChatColor.GREEN + "The item in slot " + slot + " is: " + name);
						Island island = Island.getMainIslandFor(player);
						if(island == null) {
							switch(slot) {
							case 0:
								if(!GeneratorMain.island_schematic.equalsIgnoreCase("none")) {
									Main.server.dispatchCommand(sender, "island create schematic nearspawn");
								} else {
									Main.server.dispatchCommand(sender, "island create normal nearspawn");
								}
								break;
							case 1:
								if(GeneratorMain.island_schematic.equalsIgnoreCase("none")) {
									Main.server.dispatchCommand(sender, "island create normal faraway");
								}
								break;
							case 2:
								if(GeneratorMain.island_schematic.equalsIgnoreCase("none")) {
									Main.server.dispatchCommand(sender, "island create normal random");
								}
								break;
							case 3:
								if(GeneratorMain.island_schematic.equalsIgnoreCase("none")) {
									Main.server.dispatchCommand(sender, "island create square nearspawn");
								}
								break;
							case 4:
								if(GeneratorMain.island_schematic.equalsIgnoreCase("none")) {
									Main.server.dispatchCommand(sender, "island create square faraway");
								}
								break;
							case 5:
								if(GeneratorMain.island_schematic.equalsIgnoreCase("none")) {
									Main.server.dispatchCommand(sender, "island create square random");
								}
								break;
							case 8:
								final List<Island> joinable = Island.getJoinableIslands();
								int guiSize = InventoryGUI.nextMultipleOf9(joinable.size());
								int bukkitSize = Math.max(9, Math.min(54, guiSize));
								final int pages;
								if(guiSize > bukkitSize) {
									int increments = 1;
									int total = guiSize;
									int current = bukkitSize;
									while(total > current) {
										bukkitSize += 52;//54, but minus two for the two next/prev-page papers
										increments++;
									}
									pages = increments;
								} else {
									pages = 1;
								}
								InventoryGUI join = new InventoryGUI(ChatColor.GOLD + "Island Join Menu", bukkitSize) {
									
									@Override
									public void onClick(int slot, Player player, Inventory inventory) {
										ItemStack clicked = inventory.getItem(slot);
										int page = this.currentPage;
										if(slot == 8 && page == 1) {
											//player.closeInventory();
											Main.server.dispatchCommand(player, "island");
											return;
										}
										if(slot == 45) {//Previous page
											if(clicked != null && clicked.getType() == Material.PAPER) {
												this.currentPage--;
											}
										}
										if(slot == 54) {
											if(clicked != null && clicked.getType() == Material.PAPER) {
												this.currentPage++;
											}
										}
										if(page != this.currentPage) {
											setInventoryWithIslandJoinPage(this, inventory);
											return;
										}
										if(clicked != null && clicked.getType() == Material.PLAYER_HEAD && clicked.hasItemMeta()) {
											SkullMeta meta = (SkullMeta) clicked.getItemMeta();
											OfflinePlayer chosenOwner = meta.getOwningPlayer();
											Island chosen = Island.getMainIslandFor(chosenOwner);
											if(chosen != null && chosen.getOwner() != null && !chosen.isTestIsland()) {
												player.closeInventory();
												Main.server.dispatchCommand(player, "island join " + chosen.getOwnerNamePlain());
												return;
											}
										}
									}
								}.setDefaultClickSound();
								//setInventoryWithIslandJoinPage(join, inventory);
								setMainMenuSign(join, 8);
								int i = 0;
								for(Island is : joinable) {
									int currentPage = (i / 54) + 1;
									if(i == 8 && currentPage == 1) {
										//Do nothing. Reserved for main menu sign.
										i++;
									}
									if(i + 1 % 53 == 0 || i + 1 % 45 == 0) {
										if(i + 1 != 45) {//There is no previous page past the first one!
											if(currentPage < pages || (currentPage == pages && i + 1 % 45 == 0)) {//There is no next page past the last one!
												i++;
												join.setSlot(i++, Material.PAPER, ChatColor.DARK_GRAY + (i % 53 == 0 ? "Next" : "Previous") + " page", ChatColor.GRAY + "Click to view the " + (i % 53 == 0 ? "next" : "previous"), ChatColor.GRAY + "page.");
												continue;
											}
										}
									}
									join.setSlotIcon(i++, is.getOwnerSkull(is.getOwnerName() + ChatColor.RESET + ChatColor.DARK_GREEN + "'s Island"));
								}
								join.show(player);
								break;
							default:
								break;
							}
						} else {
							switch(slot) {
							case 0:
								player.closeInventory();
								Main.server.dispatchCommand(sender, "island info");
								break;
							case 1:
								player.closeInventory();
								Main.server.dispatchCommand(sender, "island home");
								break;
							case 2:
								player.closeInventory();
								Main.server.dispatchCommand(sender, "island level");
								break;
							case 3:
								player.closeInventory();
								Main.server.dispatchCommand(sender, "island setwarp");
								break;
							case 9:
								//player.closeInventory();
								player.openInventory(Challenge.getChallengeScreen(player));
								break;
							case 10:
								//player.closeInventory();
								//player.openInventory(Perks.getPerksScreen(player));
								player.sendMessage(ChatColor.GREEN.toString().concat("Perks aren't available yet, but they'll be coming at some point in the near future!"));
								break;
							case 16:
								player.closeInventory();
								Main.server.dispatchCommand(sender, "island leave");
								break;
							case 17:
								if(island.isOwner(player)) {
									player.closeInventory();
									Main.server.dispatchCommand(sender, "island delete");
								}
								break;
							//TODO
							default:
								break;
							}
						}
						
					}
				}.setDefaultClickSound();
				//sender.sendMessage(ChatColor.YELLOW + "The Skyblock GUI system has not been implemented yet. Sorry!");
				if(island == null) {
					if(!GeneratorMain.island_schematic.equalsIgnoreCase("none")) {
						gui.setSlot(0, Material.GRASS_BLOCK, ChatColor.GREEN + "Create Island", this.getStringList("island.gui-lore.uncreated.normal-nearspawn.lore", ChatColor.GRAY + "Click to create an island."));
					} else {
						//TODO finish changing these to load from config!
						gui.setSlot(0, Material.GRASS_BLOCK, this.getStringColor("island.gui-lore.uncreated.normal-nearspawn.title", ChatColor.GREEN + "Create Normal Near Spawn"), this.getStringList("island.gui-lore.uncreated.normal-nearspawn.lore", ChatColor.GRAY + "Click to create a normal island", ChatColor.GRAY + "near the spawn area."));
						gui.setSlot(1, Material.GRASS_BLOCK, this.getStringColor("island.gui-lore.uncreated.normal-faraway.title", ChatColor.GREEN + "Create Normal Far Away"), this.getStringList("island.gui-lore.uncreated.normal-faraway.lore", ChatColor.GRAY + "Click to create a normal island", ChatColor.GRAY + "far away from other islands."));
						gui.setSlot(2, Material.GRASS_BLOCK, this.getStringColor("island.gui-lore.uncreated.normal-random.title", ChatColor.GREEN + "Create Normal Random"), this.getStringList("island.gui-lore.uncreated.normal-random.lore", ChatColor.GRAY + "Click to create a normal island", ChatColor.GRAY + "in a random location."));
						gui.setSlot(3, Material.GRASS_BLOCK, this.getStringColor("island.gui-lore.uncreated.square-nearspawn.title", ChatColor.GREEN + "Create Square Near Spawn"), this.getStringList("island.gui-lore.uncreated.square-nearspawn.lore", ChatColor.GRAY + "Click to create a square island", ChatColor.GRAY + "near the spawn area."));
						gui.setSlot(4, Material.GRASS_BLOCK, this.getStringColor("island.gui-lore.uncreated.square-faraway.title", ChatColor.GREEN + "Create Square Far Away"), this.getStringList("island.gui-lore.uncreated.square-faraway.lore", ChatColor.GRAY + "Click to create a square island", ChatColor.GRAY + "far away from other islands."));
						gui.setSlot(5, Material.GRASS_BLOCK, this.getStringColor("island.gui-lore.uncreated.square-random.title", ChatColor.GREEN + "Create Square Random"), this.getStringList("island.gui-lore.uncreated.square-random.lore", ChatColor.GRAY + "Click to create a square island", ChatColor.GRAY + "in a random location."));
					}
					gui.setSlot(8, Material.SIGN, this.getStringColor("island.gui-lore.uncreated.join-island.title", ChatColor.GREEN + "Join an Island"), this.getStringList("island.gui-lore.uncreated.join-island.lore", ChatColor.GRAY + "Click to view a list of islands", ChatColor.GRAY + "that you can request to join."));
				} else {
					gui.setSlot(0, Material.SIGN, ChatColor.GREEN + "Island information", ChatColor.GRAY + "Click to view information about", ChatColor.GRAY + "your island.");
					gui.setSlotIcon(1, new ItemStack(Material.RED_BED, 1)).setSlotTitle(1, ChatColor.GREEN + "Island home").setSlotLore(1, ChatColor.GRAY + "Click to teleport to your island's ", ChatColor.GRAY + "home.");
					gui.setSlot(2, Material.EXPERIENCE_BOTTLE, ChatColor.GREEN + "Island level", ChatColor.GRAY + "Click to calculate the island's", ChatColor.GRAY + "level.");
					gui.setSlot(9, Material.GOLD_INGOT, ChatColor.GOLD + "Island Challenges", ChatColor.GRAY + "Click to view the island challenges");
					gui.setSlot(10, Material.DIAMOND, ChatColor.GOLD + "Island Perks", ChatColor.GRAY + "Click to view/purchase island perks", ChatColor.DARK_PURPLE + "Not available yet - coming soon!");
					gui.setSlot(16, Material.SPRUCE_DOOR, ChatColor.RED + "Leave the Island", ChatColor.YELLOW + "Click to leave this island");
					if(island.isOwner(user)) {
						gui.setSlot(3, Material.NETHER_STAR, ChatColor.GREEN + "Island warp", ChatColor.GRAY + "Click to set the island's warp", ChatColor.GRAY + "location.");
						gui.setSlot(17, Material.LAVA_BUCKET, ChatColor.DARK_RED + "Delete the Island", ChatColor.YELLOW + "Click to delete this island");
					}
				}
				gui.show(user);
				sender.sendMessage(ChatColor.GREEN + "To go to your island, you can type \"" + ChatColor.WHITE + "/island home" + ChatColor.GREEN + "\" or \"" + ChatColor.WHITE + "/ih" + ChatColor.GREEN + "\" for short.");
				sender.sendMessage(ChatColor.GREEN + "To see what island commands are available, you can type \"" + ChatColor.WHITE + "/island help" + ChatColor.GREEN + "\".");
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "At the moment, this command can only be used by players.");
			}
			return true;
		}
		if(command.equalsIgnoreCase("spawn") && Main.server.getPluginCommand("spawn") == null) {
			if(user != null && (args.length >= 1 ? !sender.hasPermission("skyworld.spawn.others") : true)) {
				World world = server.getWorld("world");
				world = world == null ? GeneratorMain.getSkyworld() : world;//server.getWorlds().get(0) : world;
				Location location = world != null ? world.getSpawnLocation() : user.getWorld().getSpawnLocation();
				safeTeleport(user, location.add(0.5, 0x0.0p0, 0.5));
			} else {
				if(args.length == 1) {
					World world = server.getWorld("world");
					world = world == null ? GeneratorMain.getSkyworld() : world;//server.getWorlds().get(0) : world;
					if(args[0].equalsIgnoreCase("all")) {
						Location location = world != null ? world.getSpawnLocation() : null;
						if(location != null) {
							for(Player player : Main.server.getOnlinePlayers()) {
								safeTeleport(player, location.add(0.5, 0x0.0p0, 0.5));
							}
							sender.sendMessage(ChatColor.GREEN + "Teleported all online players to the spawn.");
						} else {
							sender.sendMessage(ChatColor.RED + "Unable to teleport all players; world not found.");
						}
					} else {
						@SuppressWarnings("deprecation")
						OfflinePlayer target = Main.server.getOfflinePlayer(args[0]);
						if(target != null && target.isOnline()) {
							Location location = world != null ? world.getSpawnLocation() : target.getPlayer().getWorld().getSpawnLocation();
							safeTeleport(target.getPlayer(), location.add(0.5, 0x0.0p0, 0.5));
							sender.sendMessage(ChatColor.GREEN + "Teleported player \"" + ChatColor.WHITE + target.getPlayer().getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" to the spawn.");
						}
					}
				} else {
					sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " {playerName}" + ChatColor.DARK_GREEN + ": Teleport the specified player to the server spawn");
				}
			}
			return true;
		}
		/*if((command.equalsIgnoreCase("dun") && Main.server.getPluginCommand("dun") == null) || (command.equalsIgnoreCase("dungeon") && Main.server.getPluginCommand("dungeon") == null) || (command.equalsIgnoreCase("dungeonworld") && Main.server.getPluginCommand("dungeonworld") == null)) {
			if(user != null && (args.length >= 1 ? !sender.hasPermission("skyworld.dun.others") : true)) {
				World world = server.getWorld("dungeonworld");
				Location location = world != null ? world.getSpawnLocation() : null;
				if(location != null) {
					safeTeleport(user, location.add(0.5, 0x0.0p0, 0.5));
					user.setGameMode(GameMode.ADVENTURE);
				} else {
					sender.sendMessage(ChatColor.RED + "Unable to teleport; dungeonworld not found.");
				}
			} else {
				if(args.length == 1) {
					World world = server.getWorld("dungeonworld");
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Main.server.getOfflinePlayer(args[0]);
					if(target != null && target.isOnline()) {
						Location location = world != null ? world.getSpawnLocation() : null;
						if(location != null) {
							safeTeleport(target.getPlayer(), location.add(0.5, 0x0.0p0, 0.5));
							target.getPlayer().setGameMode(GameMode.ADVENTURE);
							sender.sendMessage(ChatColor.GREEN + "Teleported player \"" + ChatColor.WHITE + target.getPlayer().getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" to the spawn.");
						} else {
							sender.sendMessage(ChatColor.RED + "Unable to teleport; dungeonworld not found.");
						}
					}
				} else {
					sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " {playerName}" + ChatColor.DARK_GREEN + ": Teleport the specified player to the server spawn");
				}
			}
			return true;
		}*/
		if(command.equalsIgnoreCase("skyworld") || command.equalsIgnoreCase("skyblock")) {
			if(user != null) {
				safeTeleport(user, GeneratorMain.getSkyworld().getSpawnLocation().add(0.5, 0x0.0p0, 0.5));
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "This command can only be used by players.");
			}
			return true;
		}
		if(command.equalsIgnoreCase("matid")) {
			if(args.length >= 1) {
				String searchString = "";
				int i = 0;
				for(String arg : args) {
					searchString = searchString.concat(arg).concat(i + 1 == args.length ? "" : "_");
					i++;
				}
				searchString = searchString.toUpperCase().trim();
				List<Material> matchingMaterials = new ArrayList<>();
				boolean idSearch = Main.isInt(searchString);
				int id = idSearch ? Integer.parseInt(searchString) : -1;
				for(Material material : Material.values()) {
					if(idSearch ? id == material.getId() : material.name().contains(searchString)) {
						matchingMaterials.add(material);
					}
				}
				if(matchingMaterials.isEmpty()) {
					sender.sendMessage(ChatColor.RED.toString().concat("There are no materials whose enum names contain \"").concat(ChatColor.WHITE.toString()).concat(searchString).concat(ChatColor.RED.toString()).concat("\"."));
					return true;
				}
				int matchesFound = matchingMaterials.size();
				sender.sendMessage(ChatColor.GREEN.toString().concat("There ").concat(matchesFound == 1 ? "is" : "are").concat(" ").concat(ChatColor.WHITE.toString()).concat(Integer.toString(matchesFound)).concat(ChatColor.GREEN.toString()).concat(" material").concat(matchesFound == 1 ? "" : "s").concat(" that match search query \"").concat(ChatColor.AQUA.toString()).concat(searchString).concat(ChatColor.GREEN.toString()).concat("\":"));
				for(Material material : matchingMaterials) {
					sender.sendMessage(ChatColor.AQUA.toString().concat("[").concat(Integer.toString(material.getId())).concat("] ").concat(ChatColor.WHITE.toString()).concat(material.name()));
				}
				return true;
			}
			sender.sendMessage(ChatColor.YELLOW.toString().concat("Usage: \"").concat(ChatColor.WHITE.toString()).concat("/matid {material name...}").concat(ChatColor.YELLOW.toString()).concat("\""));
			return true;
		}
		if(command.equalsIgnoreCase("testIsland")) {
			if(user != null) {
				if(!user.hasPermission("skyblock.admin")) {
					sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to create test islands.");
					return true;
				}
				Island island = null;
				String type = GeneratorMain.island_schematic.equals("none") ? "normal" : "schematic";
				if(args.length >= 1) {
					type = args[0];
					if(!type.equalsIgnoreCase("schematic") && !type.equalsIgnoreCase("normal") && !type.equalsIgnoreCase("square")) {
						sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " [normal|square] [nearspawn|random|faraway]" + ChatColor.DARK_GREEN + ": Creates a new island for you to play on");
						return true;
					}
					String location = "nearspawn";
					if(args.length == 2) {
						location = args[1];
						if(location.equalsIgnoreCase("nearspawn")) {
							island = Island.getNextAvailableIslandNearSpawn();
						} else if(location.equalsIgnoreCase("random")) {
							island = Island.getNextRandomlyLocatedIsland();
						} else if(location.equalsIgnoreCase("faraway")) {
							island = Island.getNextFarAwayIsland();
						} else {
							sender.sendMessage(ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " [normal|square] [nearspawn|random|faraway]" + ChatColor.DARK_GREEN + ": Creates a new island for you to play on");
							return true;
						}
					} else {
						Island check = Island.getIslandNearest(user.getLocation());
						if(check != null && check.getOwner() != null) {
							sender.sendMessage(ChatColor.DARK_RED + "The island location nearest you already contains an island!");
							return true;
						}
						island = Island.getOrCreateIslandNearest(user.getLocation());
					}
				} else {
					Island check = Island.getIslandNearest(user.getLocation());
					if(check != null && check.getOwner() != null) {
						sender.sendMessage(ChatColor.DARK_RED + "The island location nearest you already contains an island!");
						return true;
					}
					island = Island.getOrCreateIslandNearest(user.getLocation());
				}
				island.setTestOwner(user.getUniqueId());
				sender.sendMessage(ChatColor.GREEN + "Generating island at " + ChatColor.WHITE + island.getLocation().toVector().toString() + ChatColor.GREEN + "...");
				if(type.equalsIgnoreCase("normal")) {
					island.generateIsland();
				} else if(type.equalsIgnoreCase("square")) {
					island.generateSquareIsland();
				} else if(type.equalsIgnoreCase("schematic")) {
					island.generateSchematicIsland(true);
				} else {
					throw new IllegalStateException("Unknown island type provided: " + type);
				}
				if(!type.equalsIgnoreCase("schematic")) {
					safeTeleport(user, island.getSpawnLocation());
				}
				//The following code is not really necessary since the island was just created, but it's here anyway just in case if, in the future, freshly created islands already have members added
				for(UUID uuid : island.getMembers()) {
					if(uuid.toString().equals(user.getUniqueId().toString())) {
						continue;
					}
					OfflinePlayer member = Main.server.getOfflinePlayer(uuid);
					if(member.isOnline() && Island.isInSkyworld(member.getPlayer()) && member.getPlayer().getGameMode() == GameMode.SURVIVAL) {
						Main.safeTeleport(member.getPlayer(), island.getSpawnLocation());
					}
				}
				sender.sendMessage(ChatColor.GREEN + "Successfully created your island. Have fun testing! Be sure to delete when finished, unless the island is going to belong to someone.");
				//sender.sendMessage(ChatColor.GREEN + "To invite other players, type \"" + ChatColor.WHITE + "/island invite {playername}" + ChatColor.GREEN + "\".");
				return true;
			}
			sender.sendMessage(ChatColor.DARK_RED + "This test command can only be used by players.");
			return true;
		}
		if(command.equalsIgnoreCase("dev")) {
			if(user != null) {
				if(!user.hasPermission("skyblock.dev")) {
					sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to manage islands.");
					return true;
				}
				if(args.length == 0 || (args.length == 1 && (args[0].equalsIgnoreCase("help") || args[0].equals("?")))) {
					sender.sendMessage(ChatColor.WHITE + "/dev info - Run while visiting an island to view information about it.");
					sender.sendMessage(ChatColor.WHITE + "/dev update - Run while visiting an island to update its worldguard region and other misc. internal settings. This command is safe to use.");
					sender.sendMessage(ChatColor.WHITE + "/dev setType [normal|square|schematic] - Run while visiting an island to set its starting island type. This command is safe to use.");
					sender.sendMessage(ChatColor.YELLOW + "/dev deleteIsland [blocks] [restoreBiome:true|false] - Deletes the island that you are visiting. Use with care!");
					sender.sendMessage(ChatColor.YELLOW + "/dev deleteBlocks - Run while visiting an island to delete all of its' blocks. This does not remove the island. Use with care!");
					sender.sendMessage(ChatColor.YELLOW + "/dev delete {playername} [blocks] [restoreBiome:true|false] - Deletes the island that you specify. Use with care!");
					sender.sendMessage(ChatColor.YELLOW + "/dev restart - Run while visiting an island to restart it. This will wipe the island's member's inventories. Use with care!");
					sender.sendMessage(ChatColor.YELLOW + "/dev regenerate - Regenerates skyworld chunks. Use with care!");
					sender.sendMessage(ChatColor.YELLOW + "/dev setOwner {player} - Sets the owner of the island nearset you to the player that you specify.");
					sender.sendMessage(ChatColor.YELLOW + "/dev create {ownerName} [normal|square|schematic] - Creates an island of the specified type with the specified owner at the island location nearest you.");
					sender.sendMessage(ChatColor.YELLOW + "/dev regenChest - Regenerates skyworld starter island chests. This overwrites all items in the targeted chest. Use with care!");
					sender.sendMessage(ChatColor.YELLOW + "/dev regenIsland Run while visiting an island to regenerate the starting island. This does not delete blocks. Use with care!");
					//sender.sendMessage(ChatColor.WHITE + "/dev setowner playername - Sets the owner of the island you are visiting. Use with care!");
					//sender.sendMessage(ChatColor.WHITE + "/dev lock|unlock - Run while visiting an island to lock or unlock the island. Only the island's members and trusted players will be able to enter it(including players who can run /dev).");
					sender.sendMessage(ChatColor.GREEN + "More sub-commands are coming! Stay tuned. :)");
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("debug")) {
					if(args.length == 1) {
						user.sendMessage(ChatColor.GREEN.toString().concat("[Entei's Skyblock] Skyblock debug mode is currently: ").concat(getDebugMode(user) ? ChatColor.DARK_GREEN.toString().concat("enabled") : ChatColor.DARK_GREEN.toString().concat("disabled")));
						user.sendMessage(ChatColor.GREEN.toString().concat("[Entei's Skyblock] To turn it on or off, type \"/dev debug [on|off]\"."));
						return true;
					}
					String usage = ChatColor.YELLOW.toString().concat("Usage: \"").concat(ChatColor.WHITE.toString()).concat("/dev debug [on|off]").concat(ChatColor.YELLOW.toString()).concat("\"");
					if(args.length == 2 && (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("enabled") || args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("disabled"))) {
						setDebugMode(user, args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("enabled"));
						return true;
					}
					user.sendMessage(usage);
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("info")) {
					Island island = Island.getIslandContaining(user.getLocation());
					if(args.length == 2) {
						Island check = null;
						boolean validIDFormat = false;
						final String id = args[1];
						if(id.indexOf("_") == id.lastIndexOf("_")) {
							String[] split = id.split(Pattern.quote("_"));
							if(split.length == 2 && Main.isInt(split[0]) && Main.isInt(split[1])) {
								check = Island.getIfExists(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
								validIDFormat = true;
							}
						}
						if(!validIDFormat) {
							sender.sendMessage(ChatColor.YELLOW.toString().concat("Syntax error: Invalid island ID provided: \"").concat(ChatColor.WHITE.toString()).concat(id).concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat("\""));
							sender.sendMessage(ChatColor.YELLOW.toString().concat("Island IDs are formatted like so: \"").concat(ChatColor.AQUA.toString()).concat("X_Z").concat(ChatColor.YELLOW.toString()).concat("\"; where X is the island's X cardinal position, and Z is the island's Z cardinal position."));
							return true;
						}
						if(check == null) {
							sender.sendMessage(ChatColor.YELLOW.toString().concat("There are no islands with ID \"").concat(ChatColor.AQUA.toString()).concat(id).concat(ChatColor.YELLOW.toString()).concat("\"."));
							return true;
						}
						island = check;
					}
					if(island == null) {
						sender.sendMessage(ChatColor.YELLOW + "You aren't standing on an active island. Move closer to one and try again.");
						return true;
					}
					String id = island.getID();
					String location = "(".concat(island.getLocation().toVector().toString()).concat(")");
					String type = island.getType();
					String spawnLoc = "(".concat(island.getSpawnLocation().toVector().toString()).concat(")");
					String warpLoc = "(".concat(island.getWarpLocation().toVector().toString()).concat(")");
					int[] minXminZmaxXmaxZ = island.getBounds();
					int minX = minXminZmaxXmaxZ[0], minZ = minXminZmaxXmaxZ[1],
							maxX = minXminZmaxXmaxZ[2],
							maxZ = minXminZmaxXmaxZ[3];
					String islandBounds = "[minX=".concat(Integer.toString(minX)).concat(",minZ=").concat(Integer.toString(minZ)).concat(",maxX=").concat(Integer.toString(maxX)).concat(",maxZ=").concat(Integer.toString(maxZ)).concat("]");
					String ownerUUID = island.getOwner().toString();
					String ownerName = island.getOwnerNamePlain();
					String memberCount = Integer.toString(island.getMembers().size()).concat("/").concat(Integer.toString(island.getMemberLimit()));
					String trustedPlayersCount = Integer.toString(island.getTrustedPlayers().size());
					island.getLevel();
					island.getBiome();
					Location netherPortalLoc = island.getNetherPortal();
					String netherPortal = netherPortalLoc == null ? "null" : netherPortalLoc.toString();
					island.getNetherPortalOrientation();
					//check.getSkyNetherPortal(); //Manually searches the entire nether region; resource intensive
					island.getNextRestartTime();
					island.getOnlineJoinRequests();
					island.getPlayersOnIsland().size();
					sender.sendMessage(ChatColor.DARK_RED + "Brian" + ChatColor.BLACK + "_" + ChatColor.GOLD + "Entei" + ChatColor.WHITE + " stopped coding this command half-way through and started working on something else. Go bug him about it.");
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("update")) {
					Island check = Island.getIslandContaining(user.getLocation());
					if(check == null) {
						sender.sendMessage(ChatColor.YELLOW + "You aren't standing on an active island. Move closer to one and try again.");
						return true;
					}
					check.update();
					sender.sendMessage(ChatColor.GREEN + "Successfully told the island you're visiting to update(regions, invitations, etc...).");
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("restart")) {
					if(!user.hasPermission("skyblock.admin")) {
						sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to restart islands.");
						return true;
					}
					Island check = Island.getIslandNearest(user.getLocation());
					if(check == null) {
						sender.sendMessage(ChatColor.YELLOW + "You aren't standing near an active island. Move closer to one and try again.");
						return true;
					}
					if(!check.canRestart()) {
						if(args.length == 1 || (args.length >= 2 && !args[1].equalsIgnoreCase("confirm"))) {
							sender.sendMessage(ChatColor.YELLOW.toString().concat("This island's restart cooldown ends in ").concat(Main.getLengthOfTime(check.getNextRestartTime() - System.currentTimeMillis())).concat(".\nAre you sure you wish to restart it now? This action cannot be undone!\nType \"").concat(ChatColor.WHITE.toString()).concat("/dev restart confirm").concat(ChatColor.YELLOW.toString()).concat("\" to confirm."));
							return true;
						}
					} else {
						if(args.length == 1 || (args.length >= 2 && !args[1].equalsIgnoreCase("confirm"))) {
							sender.sendMessage(ChatColor.YELLOW.toString().concat("Are you sure you wish to restart this island? This action cannot be undone!\nType \"").concat(ChatColor.WHITE.toString()).concat("/dev restart confirm").concat(ChatColor.YELLOW.toString()).concat("\" to confirm."));
							return true;
						}
					}
					check.sendMessage(ChatColor.YELLOW.toString().concat("The island you are a member of(ID: ").concat(ChatColor.WHITE.toString()).concat(check.getID()).concat(ChatColor.YELLOW.toString()).concat(") was just restarted by a skyblock server administrator."));
					check.restart(true);
					sender.sendMessage(ChatColor.GREEN + "Successfully restarted the island nearest you.");
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("setType")) {
					/*if(!user.hasPermission("skyblock.admin")) {
						sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to set island types.");
						sender.sendMessage(ChatColor.YELLOW + "Required permission node: \"" + ChatColor.WHITE + "skyblock.admin" + ChatColor.YELLOW + "\"");
						return true;
					}*/
					final String usage = ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/dev setType [normal|square|schematic]" + ChatColor.YELLOW + " - Changes the island's starting type\nChanges take place after restarting or regenerating the island.";
					Island check = Island.getIslandNearest(user.getLocation());
					if(check == null) {
						sender.sendMessage(ChatColor.YELLOW + "You aren't standing near an active island. Move closer to one and try again.");
						return true;
					}
					if((args.length == 1 || args.length > 2) || (!args[1].equalsIgnoreCase("normal") && !args[1].equalsIgnoreCase("square") && !args[1].equalsIgnoreCase("schematic"))) {
						sender.sendMessage(usage);
						return true;
					}
					if(args[1].equalsIgnoreCase("normal")) {
						check.setType("normal");
					} else if(args[1].equalsIgnoreCase("square")) {
						check.setType("square");
					} else if(args[1].equalsIgnoreCase("schematic")) {
						check.setType("schematic");
					} else {
						sender.sendMessage(usage);
						return true;
					}
					sender.sendMessage(ChatColor.GREEN + "Successfully set the island's type to \"".concat(check.getType()).concat("\"."));
					sender.sendMessage(ChatColor.GREEN + "Changes will take place the next time the island is restarted or regenerated.");
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("regenIsland")) {//TODO add command arguments for the boolean values in the restart(...) method, and make this subcommand use the CommandConfirmation class
					if(!sender.hasPermission("skyblock.admin")) {
						sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to regenerate skyblock starter islands.");
						sender.sendMessage(ChatColor.YELLOW + "Required permission node: \"" + ChatColor.WHITE + "skyblock.admin" + ChatColor.YELLOW + "\"");
						return true;
					}
					Island check = Island.getIslandNearest(user.getLocation());
					if(check == null) {
						sender.sendMessage(ChatColor.YELLOW + "You aren't standing near an active island. Move closer to one and try again.");
						return true;
					}
					if(args.length == 1 || (args.length >= 2 && !args[1].equalsIgnoreCase("confirm"))) {
						sender.sendMessage(ChatColor.YELLOW.toString().concat("Are you sure you wish to regenerate this island's blocks now? This action cannot be undone!\nType \"").concat(ChatColor.WHITE.toString()).concat("/dev regenIsland confirm").concat(ChatColor.YELLOW.toString()).concat("\" to confirm."));
						return true;
					}
					check.sendMessage(ChatColor.YELLOW.toString().concat("The island you are a member of(ID: ").concat(ChatColor.WHITE.toString()).concat(check.getID()).concat(ChatColor.YELLOW.toString()).concat(")'s starter island was just regenerated by a skyblock server administrator."));
					check.restart(false, false, false, true);
					sender.sendMessage(ChatColor.GREEN + "Successfully had the island nearest you regenerate its starting island.\nNo player inventories were wiped, and old blocks were not deleted.");
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("deleteIsland")) {
					if(!sender.hasPermission("skyblock.admin")) {
						sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to delete islands.");
						sender.sendMessage(ChatColor.YELLOW + "Required permission node: \"" + ChatColor.WHITE + "skyblock.admin" + ChatColor.YELLOW + "\"");
						return true;
					}
					Island check = Island.getIslandNearest(user.getLocation());
					final Island C = check;
					final String[] ARGS = args;
					if(args.length >= 2 && args[1].equalsIgnoreCase("blocks")) {
						Runnable code = () -> {
							Island c = C;
							if(c == null) {
								c = Island.getOrCreateIslandNearest(user.getLocation());
								Island.islands.remove(c);
							}
							c.deleteBlocks(ARGS.length == 3 && ARGS[2].equalsIgnoreCase("true"));
							sender.sendMessage(ChatColor.YELLOW + "Successfully wiped all blocks within the island at " + ChatColor.WHITE + c.getID() + ChatColor.YELLOW + ".");
							sender.sendMessage(ChatColor.YELLOW + "If the island had members, their inventories were not affected.");
						};
						CommandConfirmation.addConfirmationFor(sender, code, MINUTE, "dev", new String[] {"deleteIsland", "blocks", (args.length == 3 ? args[2] : "")}, false);
						sender.sendMessage(ChatColor.YELLOW.toString().concat("Are you sure you wish to delete this island now? This action cannot be undone!\nType \"").concat(ChatColor.WHITE.toString()).concat("/dev deleteIsland blocks ".concat(args.length == 3 ? (args[2].concat(" ")) : "").concat("confirm")).concat(ChatColor.YELLOW.toString()).concat("\" to confirm."));
						return true;
					} else if(args.length == 1) {
						if(check != null) {
							Runnable code = () -> {
								String id = check.getID();
								C.deleteCompletely();
								if(!user.isFlying()) {
									safeTeleport(user, GeneratorMain.getSkyworldSpawnLocation());
								}
								sender.sendMessage(ChatColor.YELLOW + "Successfully deleted the island at " + ChatColor.WHITE + id + ChatColor.YELLOW + ".");
							};
							CommandConfirmation.addConfirmationFor(sender, code, MINUTE, "dev", new String[] {"deleteIsland", "confirm"}, false);
							sender.sendMessage(ChatColor.YELLOW.toString().concat("Are you sure you wish to delete this island now? This action cannot be undone!\nType \"").concat(ChatColor.WHITE.toString()).concat("/dev deleteIsland blocks ".concat(args.length == 3 ? (args[2].concat(" ")) : "").concat("confirm")).concat(ChatColor.YELLOW.toString()).concat("\" to confirm."));
							return true;
						}
						sender.sendMessage(ChatColor.DARK_RED + "The island location nearest you does not have an island.");
						sender.sendMessage(ChatColor.YELLOW + "If there are leftover blocks in the island area, then type \"" + ChatColor.WHITE + "/delete blocks" + ChatColor.YELLOW + "\" instead, which only deletes blocks.");
					} else {
						sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/dev deleteIsland [blocks] [restoreBiome:true|false]" + ChatColor.YELLOW + " - The restoreBiome option is a boolean value, true or false.");
					}
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("delete")) {
					if(!sender.hasPermission("skyblock.admin")) {
						sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to delete islands.");
						sender.sendMessage(ChatColor.YELLOW + "Required permission node: \"" + ChatColor.WHITE + "skyblock.admin" + ChatColor.YELLOW + "\"");
						return true;
					}
					if(args.length >= 2) {
						@SuppressWarnings("deprecation")
						OfflinePlayer target = Main.server.getOfflinePlayer(args[1]);
						final Island check = Island.getMainIslandFor(target);
						if(target.hasPlayedBefore() || check != null) {
							final String[] ARGS = args;
							if(args.length >= 3 && args[2].equalsIgnoreCase("blocks")) {
								Runnable code = () -> {
									Island c = check;
									if(c == null) {
										c = Island.getOrCreateIslandNearest(user.getLocation());
										Island.islands.remove(c);
									}
									c.deleteBlocks(ARGS.length == 4 && ARGS[3].equalsIgnoreCase("true"));
									sender.sendMessage(ChatColor.YELLOW + "Successfully wiped all blocks within the island at " + ChatColor.WHITE + c.getID() + ChatColor.YELLOW + ".");
									sender.sendMessage(ChatColor.YELLOW + "If the island had members, their inventories were not affected.");
								};
								String[] mkArgs = new String[args.length + 1];
								String strArgs = "";
								for(int i = 0; i < args.length; i++) {
									mkArgs[i] = args[i];
									strArgs = strArgs.concat(args[i]).concat(" ");
								}
								mkArgs[mkArgs.length - 1] = "confirm";
								strArgs = strArgs.concat("confirm");
								CommandConfirmation.addConfirmationFor(sender, code, MINUTE, "dev", mkArgs, false);
								sender.sendMessage(ChatColor.YELLOW.toString().concat("Are you sure you wish to delete this island now? This action cannot be undone!\nType \"").concat(ChatColor.WHITE.toString()).concat("/dev ").concat(strArgs).concat(ChatColor.YELLOW.toString()).concat("\" to confirm."));
								return true;
							} else if(args.length == 2) {
								if(check != null) {
									Runnable code = () -> {
										String id = check.getID();
										check.deleteCompletely();
										if(!user.isFlying()) {
											safeTeleport(user, GeneratorMain.getSkyworldSpawnLocation());
										}
										sender.sendMessage(ChatColor.YELLOW + "Successfully deleted the island at " + ChatColor.WHITE + id + ChatColor.YELLOW + ".");
									};
									CommandConfirmation.addConfirmationFor(sender, code, MINUTE, "dev", new String[] {"delete", args[1], "confirm"}, false);
									sender.sendMessage(ChatColor.YELLOW.toString().concat("Are you sure you wish to delete this island now? This action cannot be undone!\nType \"").concat(ChatColor.WHITE.toString()).concat("/dev delete ").concat(args[1]).concat(" confirm").concat(ChatColor.YELLOW.toString()).concat("\" to confirm."));
									return true;
								}
								sender.sendMessage(ChatColor.DARK_RED + "The island location nearest you does not have an island.");
								sender.sendMessage(ChatColor.YELLOW + "If there are leftover blocks in the island area, then type \"" + ChatColor.WHITE + "/delete blocks" + ChatColor.YELLOW + "\" instead, which only deletes blocks.");
							} else {
								sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/dev delete {player} [blocks] [restoreBiome:true|false]" + ChatColor.YELLOW + " - The restoreBiome option is a boolean value, true or false.");
							}
						} else {
							if(!target.hasPlayedBefore()) {
								sender.sendMessage(ChatColor.RED + "Player " + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.YELLOW + " either has not played before or does not exist.");
							} else {
								sender.sendMessage(ChatColor.RED + "Player " + ChatColor.WHITE + target.getName() + ChatColor.RESET + ChatColor.YELLOW + " does not have an island.");
							}
						}
					} else {
						sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/dev delete {player} [blocks] [restoreBiome:true|false]" + ChatColor.YELLOW + " - The restoreBiome option is a boolean value, true or false.");
					}
					/*Island check = Island.getIslandNearest(user.getLocation());
					if(args.length >= 2 && args[1].equalsIgnoreCase("blocks")) {
						if(check == null) {
							check = Island.getOrCreateIslandNearest(user.getLocation());
							Island.islands.remove(check);
						}
						check.deleteBlocks(args.length == 3 && args[2].equalsIgnoreCase("true"));
						sender.sendMessage(ChatColor.YELLOW + "Successfully wiped all blocks within the island at " + ChatColor.WHITE + check.getID() + ChatColor.YELLOW + ".");
						sender.sendMessage(ChatColor.YELLOW + "If the island had members, their inventories were not affected.");
					} else if(args.length == 1) {
						if(check != null) {
							String id = check.getID();
							check.deleteCompletely();
							check = null;
							if(!user.isFlying()) {
								safeTeleport(user, GeneratorMain.getSkyworldSpawnLocation());
							}
							sender.sendMessage(ChatColor.YELLOW + "Successfully deleted the island at " + ChatColor.WHITE + id + ChatColor.YELLOW + ".");
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "The island location nearest you does not have an island.");
							sender.sendMessage(ChatColor.YELLOW + "If there are leftover blocks in the island area, then type \"" + ChatColor.WHITE + "/delete blocks" + ChatColor.YELLOW + "\" instead, which only deletes blocks.");
						}
					} else {
						sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/dev delete {player} [blocks] [restoreBiome:true|false]" + ChatColor.YELLOW + " - The restoreBiome option is a boolean value, true or false.");
					}*/
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("regenerate")) {
					if(!sender.hasPermission("skyblock.admin")) {
						sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to regenerate the skyworld chunks.");
						sender.sendMessage(ChatColor.YELLOW + "Required permission node: \"" + ChatColor.WHITE + "skyblock.admin" + ChatColor.YELLOW + "\"");
						return true;
					}
					if(user.getWorld() != GeneratorMain.getSkyworld()) {
						user.sendMessage(ChatColor.RED + "You can only regenerate skyblock chunks in the skyworld.");
						return true;
					}
					if(args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
						ArrayList<Chunk> chunks = new ArrayList<>(25);
						for(int x = -32; x < 33; x++) {
							for(int z = -32; z < 33; z++) {
								Chunk chunk = getChunkAtWorldCoords(GeneratorMain.getSkyworld(), user.getLocation().getBlockX() + x, user.getLocation().getBlockZ() + z);
								if(chunk != null && !chunks.contains(chunk)) {
									chunks.add(chunk);
								}
							}
						}
						for(Chunk chunk : chunks) {
							generateChunk(GeneratorMain.getSkyworld(), random, chunk);
						}
						user.sendMessage(ChatColor.GREEN + "Regenerated a 5x5 area of chunks around you.");
					} else {
						user.sendMessage(ChatColor.YELLOW + "Are you sure that you wish to regenerate the chunks around you?");
						user.sendMessage(ChatColor.YELLOW + "This will overwrite any existing blocks, regardless if they are part of an island.");
						user.sendMessage(ChatColor.YELLOW + "Type \"" + ChatColor.WHITE + "/regenerate confirm" + ChatColor.YELLOW + "\" to continue.");
					}
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("setOwner")) {
					if(!sender.hasPermission("skyblock.admin")) {
						sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to set island owners.");
						sender.sendMessage(ChatColor.YELLOW + "Required permission node: \"" + ChatColor.WHITE + "skyblock.admin" + ChatColor.YELLOW + "\"");
						return true;
					}
					if(user.getWorld() != GeneratorMain.getSkyworld()) {
						user.sendMessage(ChatColor.RED + "You can only set skyblock island owners in the skyworld.");
						return true;
					}
					if(args.length == 1 || args.length > 3) {
						sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/dev setOwner {player}" + ChatColor.YELLOW + " - Sets the owner of the island nearset you to the player that you specify.");
						return true;
					}
					Island island = Island.getIslandNearest(user.getLocation());
					if(island == null) {
						sender.sendMessage(ChatColor.YELLOW + "You are not standing on or near any islands. Move closer to the target island and try again.");
						return true;
					}
					OfflinePlayer newOwner = Main.server.getOfflinePlayer(args[1]);
					if(args.length <= 2) {
						Island check = Island.getMainIslandFor(newOwner);
						if(check != null && check != island) {
							sender.sendMessage(ChatColor.YELLOW + "[!] Player: \"" + ChatColor.WHITE + check.getOwnerName() + ChatColor.RESET + ChatColor.YELLOW + "\" already owns an island(ID: " + check.getID() + ").");
						}
					}
					if(args.length == 3 && args[2].equalsIgnoreCase("confirm")) {
						UUID oldOwner = island.getOwner();
						if(oldOwner.toString().equals(newOwner.getUniqueId().toString())) {
							user.sendMessage(ChatColor.YELLOW + "The player \"" + ChatColor.WHITE + island.getOwnerName() + ChatColor.RESET + ChatColor.YELLOW + "\" is already the owner of this island.");
							return true;
						}
						String oldOwnerName = island.getOwnerName();
						island.setOwner(newOwner);
						island.setOwnerName(island.getOwnerNamePlain().equals("<unknown>") ? args[1] : newOwner.getName());
						island.addMember(oldOwner, true);
						island.update();
						user.sendMessage(ChatColor.GREEN + "The owner of the island(ID: " + island.getID() + ") is now: \"" + ChatColor.WHITE + island.getOwnerName() + ChatColor.RESET + ChatColor.GREEN + "\".");
						user.sendMessage(ChatColor.GREEN + "The previous owner(\"" + ChatColor.WHITE + oldOwnerName + ChatColor.RESET + ChatColor.GREEN + "\") has been added as a member of the island.");
					} else {
						user.sendMessage(ChatColor.YELLOW + "Are you sure that you wish to set the owner of \"".concat(ChatColor.WHITE.toString()).concat(island.getOwnerName()).concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat("\"'s island (ID ").concat(island.getID()).concat(") to \"").concat(ChatColor.WHITE.toString()).concat(newOwner.getName()).concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat("\"?"));
						user.sendMessage(ChatColor.YELLOW + "Type \"" + ChatColor.WHITE + "/dev setOwner " + newOwner.getName() + " confirm" + ChatColor.YELLOW + "\" to continue.");
					}
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("create")) {
					if(!sender.hasPermission("skyblock.admin")) {
						sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to create skyworld islands.");
						sender.sendMessage(ChatColor.YELLOW + "Required permission node: \"" + ChatColor.WHITE + "skyblock.admin" + ChatColor.YELLOW + "\"");
						return true;
					}
					if(args.length < 3) {
						sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/dev create {ownerName} [normal|square|schematic]" + ChatColor.YELLOW + " - Creates an island of the specified type with the specified owner at the island location nearest you.");
						return true;
					}
					final Location location = Island.getIslandLocationNearest(user.getLocation());
					Island check = Island.getIslandNearest(location);
					if(check != null && check.getOwner() == null) {
						user.sendMessage(ChatColor.YELLOW.toString().concat("The island nearest you(ID: ").concat(check.getID()).concat(") exists, but is an orphaned island. Try deleting it first with \"").concat(ChatColor.WHITE.toString()).concat("/dev deleteIsland").concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat("\"."));
						return true;
					}
					if(check != null) {
						user.sendMessage(ChatColor.YELLOW.toString().concat("The island nearest you(ID: ").concat(check.getID()).concat(") already exists. It is owned by \"").concat(ChatColor.WHITE.toString()).concat(check.getOwnerName()).concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat("\"."));
						return true;
					}
					String islandType = args[2];
					if(!islandType.equalsIgnoreCase("normal") && !islandType.equalsIgnoreCase("square") && !islandType.equalsIgnoreCase("schematic")) {
						return this.onCommand(sender, cmd, "dev", new String[] {"create"});
					}
					final String type = islandType.toLowerCase();
					final OfflinePlayer owner = Main.server.getOfflinePlayer(args[1]);
					
					Runnable code = () -> {
						final Island existingIsland = Island.getMainIslandFor(owner);
						final Island island = Island.getOrCreateIslandNearest(location);
						island.setOwner(owner);
						boolean teleported = false;
						if(type.equals("normal")) {
							island.generateIsland(true, true);
						} else if(type.equals("square")) {
							island.generateSquareIsland(true, true);
						} else if(type.equals("schematic")) {
							if(GeneratorMain.island_schematic.equalsIgnoreCase("none")) {
								island.generateIsland(true, true);
								island.setType("schematic");
							} else {
								island.generateSchematicIsland(true, true, (teleported = true));//Intentional use of '=' instead of '=='
							}
						} else {
							sender.sendMessage(ChatColor.RED + "Error: Unknown/unimplemented island type \"".concat(ChatColor.WHITE.toString()).concat(type).concat(ChatColor.RED.toString()).concat("\"."));
							island.setOwner((UUID) null).deleteCompletely();
							return;
						}
						if(existingIsland == null) {
							if(!teleported) {
								if(owner.isOnline() && Island.isInSkyworld(owner.getPlayer()) && owner.getPlayer().getGameMode() == GameMode.SURVIVAL) {
									Main.safeTeleport(owner.getPlayer(), island.getSpawnLocation());
									teleported = true;
								}
								for(UUID uuid : island.getMembers()) {
									if(uuid.toString().equals(owner.getUniqueId().toString())) {
										continue;
									}
									OfflinePlayer member = Main.server.getOfflinePlayer(uuid);
									if(member.isOnline() && Island.isInSkyworld(member.getPlayer()) && member.getPlayer().getGameMode() == GameMode.SURVIVAL) {
										Main.safeTeleport(member.getPlayer(), island.getSpawnLocation());
										teleported = true;
									}
								}
							}
							island.wipeMembersInventories(ChatColor.GREEN + "Welcome to your new skyblock island, courtesy of \"" + ChatColor.WHITE + sender.getName() + ChatColor.RESET + ChatColor.GREEN + "\".");
						}
						sender.sendMessage(ChatColor.GREEN + "Successfully created a " + ChatColor.WHITE + type + ChatColor.GREEN + " island at (" + island.getLocation().toVector().toString() + ") for player \"" + ChatColor.WHITE + island.getOwnerName() + ChatColor.RESET + ChatColor.GREEN + "\".");
						if(!teleported) {
							sender.sendMessage(ChatColor.GREEN + "They have not been teleported to their island.");
						}
						if(existingIsland != null) {
							sender.sendMessage(ChatColor.YELLOW + "[!] Note that player \"" + ChatColor.WHITE + existingIsland.getOwnerName() + ChatColor.RESET + ChatColor.YELLOW + "\" already owns an island(ID: " + existingIsland.getID() + ") at " + ChatColor.WHITE + "(" + existingIsland.getLocation().toVector().toString() + ")" + ChatColor.YELLOW + ".");
						}
						int islandCount = 0;
						for(Island checkForDuplicates : Island.getAllIslands()) {
							if(checkForDuplicates.getOwner() != null && checkForDuplicates.isOwner(owner)) {
								islandCount++;
							}
						}
						if(islandCount > 1) {
							sender.sendMessage(ChatColor.YELLOW + "[!] Player \"" + ChatColor.WHITE + island.getOwnerName() + ChatColor.RESET + ChatColor.YELLOW + "\" now owns " + ChatColor.WHITE + Integer.toString(islandCount) + ChatColor.RESET + ChatColor.YELLOW + " island" + (islandCount == 1 ? "" : "s") + ".");
						}
					};
					if(args.length == 3) {
						check = Island.getMainIslandFor(owner);
						if(check != null) {
							sender.sendMessage(ChatColor.YELLOW + "[!] Player: \"" + ChatColor.WHITE + check.getOwnerName() + ChatColor.RESET + ChatColor.YELLOW + "\" already owns an island(ID: " + check.getID() + ").");
							sender.sendMessage(ChatColor.YELLOW + "Are you sure you want to create another island with them as the owner? Type \"" + ChatColor.WHITE + "/dev create " + owner.getName() + " " + islandType + " confirm" + ChatColor.RESET + ChatColor.YELLOW + "\" to confirm.");
							CommandConfirmation.addConfirmationFor(sender, code, MINUTE, command, new String[] {"create", owner.getName(), islandType, "confirm"}, false);
							return true;
						}
					}
					code.run();
					return true;
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("regenChest")) {
					if(!sender.hasPermission("skyblock.admin")) {
						sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to regenerate skyworld island starter chests.");
						sender.sendMessage(ChatColor.YELLOW + "Required permission node: \"" + ChatColor.WHITE + "skyblock.admin" + ChatColor.YELLOW + "\"");
						return true;
					}
					Block target = user.getTargetBlockExact(4);
					if(target != null && target.getState() instanceof Chest) {
						Chest chest = (Chest) target.getState();
						Island.setChestContents(chest.getInventory());
						Island.setChestContents(chest.getBlockInventory());
						sender.sendMessage(ChatColor.YELLOW + "The targeted chest has been cleared, and its' contents set to that of the default island starter chest.");
					} else {
						if(target == null || target.getType() == Material.AIR) {
							sender.sendMessage(ChatColor.YELLOW + "No targeted block detected. Look at a chest within 4 blocks of you and try again.");
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Targeted block is not a chest. Look at a chest within 4 blocks of you and try again.");
						}
					}
				} else {
					sender.sendMessage(ChatColor.YELLOW + "Usage:");
					return this.onCommand(sender, null, "dev", new String[] {"help"});
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "This command can only be used by players.");
			}
			return true;
		}
		if(command.equalsIgnoreCase("save-all")) {
			saveAll();
		}
		return false;
	}
	
	public static final class CommandConfirmation {
		
		private static final ConcurrentLinkedDeque<CommandConfirmation> confirmations = new ConcurrentLinkedDeque<>();
		
		public static final CommandConfirmation addConfirmationFor(CommandSender sender, Runnable codeToConfirm, long timeUntilExpiration, String confirmationCommand, String[] confirmationArgs, boolean caseSensitive) {
			if(sender == null || codeToConfirm == null || timeUntilExpiration <= 0 || confirmationCommand == null || confirmationArgs == null) {
				return null;
			}
			List<CommandConfirmation> confirmations = getConfirmationsFor(sender);
			for(CommandConfirmation confirmation : new ArrayList<>(confirmations)) {
				if(confirmation.isExpired()) {
					confirmations.remove(confirmation);
				}
			}
			if(!confirmations.isEmpty()) {
				for(CommandConfirmation confirmation : confirmations) {
					if(confirmation.matches(confirmationCommand, confirmationArgs)) {
						CommandConfirmation.confirmations.remove(confirmation);
					}
				}
			}
			CommandConfirmation confirmation = new CommandConfirmation(sender, codeToConfirm, timeUntilExpiration, confirmationCommand, confirmationArgs, caseSensitive);
			CommandConfirmation.confirmations.addFirst(confirmation);
			return confirmation;
		}
		
		public static final List<CommandConfirmation> getConfirmationsFor(CommandSender sender) {
			List<CommandConfirmation> list = new ArrayList<>();
			for(CommandConfirmation confirmation : confirmations) {
				if(confirmation.isExpired()) {
					confirmations.remove(confirmation);
					continue;
				}
				if(confirmation.matches(sender)) {
					list.add(confirmation);
				}
			}
			return list;
		}
		
		/** @param cmd unused */
		public static final boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
			List<CommandConfirmation> confirmations = getConfirmationsFor(sender);
			for(CommandConfirmation confirmation : new ArrayList<>(confirmations)) {
				if(confirmation.isExpired()) {
					confirmations.remove(confirmation);
				}
			}
			if(confirmations.isEmpty()) {
				return false;
			}
			for(CommandConfirmation confirmation : confirmations) {
				if(confirmation.matches(command, args)) {
					Throwable ex = confirmation.execute();
					if(ex != null) {
						//ex.printStackTrace(System.err);
						//System.err.flush();
						Main.getPluginLogger().log(Level.WARNING, "An error occurred while command sender \"".concat(sender.getName()).concat("\" attempted to confirm command \"").concat(command).concat("\""), ex);
					}
					return true;
				}
			}
			return false;
		}
		
		private final CommandSender sender;
		private volatile Runnable codeToConfirm;
		private final long expirationTime;
		private final String confirmationCommand;
		private final String[] confirmationArgs;
		private final boolean caseSensitive;
		
		private CommandConfirmation(CommandSender sender, Runnable codeToConfirm, long timeUntilExpiration, String confirmationCommand, String[] confirmationArgs, boolean caseSensitive) {
			this.expirationTime = System.currentTimeMillis() + timeUntilExpiration;
			this.sender = sender;
			this.codeToConfirm = codeToConfirm;
			this.confirmationCommand = confirmationCommand;
			this.confirmationArgs = confirmationArgs;
			this.caseSensitive = caseSensitive;
		}
		
		public CommandSender getCommandSender() {
			return this.sender;
		}
		
		public Throwable execute() {
			confirmations.remove(this);
			if(this.codeToConfirm == null) {
				return new NullPointerException("Confirmation runnable is null!");
			}
			try {
				this.codeToConfirm.run();
				return null;
			} catch(Throwable ex) {
				return ex;
			} finally {
				this.codeToConfirm = null;
			}
		}
		
		public long getExpireTime() {
			return this.expirationTime;
		}
		
		public boolean isExpired() {
			return System.currentTimeMillis() - this.expirationTime >= 0;
		}
		
		public boolean matches(String command, String[] args) {
			if(this.caseSensitive ? this.confirmationCommand.equals(command) : this.confirmationCommand.equalsIgnoreCase(command)) {
				if(args.length == this.confirmationArgs.length) {
					boolean allMatched = true;
					for(int i = 0; i < args.length; i++) {
						if(args[i] == null) {
							if(this.confirmationArgs[i] == null) {
								continue;
							}
							allMatched = false;
							break;
						}
						if(this.confirmationArgs[i] == null) {
							allMatched = false;
							break;
						}
						allMatched &= this.caseSensitive ? args[i].equals(this.confirmationArgs[i]) : args[i].equalsIgnoreCase(this.confirmationArgs[i]);
						if(!allMatched) {
							break;
						}
					}
					return allMatched;
				}
			}
			return false;
		}
		
		public boolean matches(CommandSender sender) {
			if(sender instanceof OfflinePlayer) {
				if(this.sender instanceof OfflinePlayer) {
					if(((OfflinePlayer) sender).getUniqueId().toString().equals(((OfflinePlayer) this.sender).getUniqueId().toString())) {
						return true;
					}
				}
			} else if(!(this.sender instanceof OfflinePlayer)) {
				if(this.sender == sender || this.sender.equals(sender)) {
					return true;
				}
			}
			return false;
		}
		
	}
	
	private final String getStringColor(String path, String def) {
		return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(path, def));
	}
	
	private static final List<String> colorize(String... list) {
		for(int i = 0; i < list.length; i++) {
			list[i] = ChatColor.translateAlternateColorCodes('&', list[i]);
		}
		return Arrays.asList(list);
	}
	
	private static final List<String> colorize(List<String> list) {
		List<String> old = new ArrayList<>(list);
		list.clear();
		for(String line : old) {
			list.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		return list;
	}
	
	private final List<String> getStringList(String path, String... def) {
		List<String> list = this.getConfig().getStringList(path);
		if(list == null || list.isEmpty()) {
			String test = this.getConfig().getString(path);
			//System.out.println("Couldn't load \"" + path + "\" as a string list, but it loaded as string like this: \"" + test + "\"");
			String[] split = test.split(Pattern.quote("\n"));
			if(split.length > 1) {
				list = new ArrayList<>();
				for(String line : split) {
					list.add(line.trim());
				}
				return colorize(list);
			}
		}
		if(list == null || list.isEmpty()) {
			return colorize(def);
		}
		return colorize(list);
	}
	
	@SuppressWarnings("deprecation")
	public static final void generateChunk(World world, Random random, Chunk chunk) {
		/*byte[][] result = SkyworldGenerator._generateBlockSections(world, random, chunk.getX(), chunk.getZ(), null);
		for(int x = 0; x < 16; x++) {
			for(int y = 0; y < world.getMaxHeight(); y++) {
				for(int z = 0; z < 16; z++) {
					final Vector coords = SkyworldGenerator.getWorldCoordsFor(chunk.getX(), chunk.getZ(), x, y, z);
					if(Island.getIslandContaining(coords.toLocation(world)) == null) {
						chunk.getBlock(x, y, z).setType(SkyworldGenerator.getMaterial(result, x, y, z), true);
					}
				}
			}
		}*/
		for(int x = 0; x < 16; x++) {
			for(int y = 0; y < world.getMaxHeight(); y++) {
				for(int z = 0; z < 16; z++) {
					final Vector coords = SkyworldGenerator.getWorldCoordsFor(chunk.getX(), chunk.getZ(), x, y, z);
					if(Island.getIslandContaining(coords.toLocation(world)) == null) {
						chunk.getBlock(x, y, z).setType(SkyworldGenerator.getBlockDataFor(coords, true), true);
					}
				}
			}
		}
		SkyworldBlockPopulator.populate(world, random, chunk, true, (world1, x, y, z) -> Island.getIslandContaining(new Location(world1, x, y, z)) == null);
		world.refreshChunk(chunk.getX(), chunk.getZ());
		for(Player player : world.getPlayers()) {
			Location loc = chunk.getBlock(0, 0, 0).getLocation();
			player.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
		}
	}
	
	protected static final double MIN_VALUE = -Double.MAX_VALUE;
	
	/** @param <T> The object type of the array
	 * @param args The array to clean
	 * @return A cleaned copy of the given array */
	@SuppressWarnings("unchecked")
	public static final <T> T[] clean(T... args) {
		Class<T> clazz = null;
		T[] tmp = args;
		int i = 0;
		for(T arg : args) {
			if(arg != null) {
				clazz = (Class<T>) arg.getClass();
				tmp[i++] = arg;
			}
		}
		if(clazz == String.class) {
			return (T[]) clean((String[]) args);
		}
		if(clazz == null) {
			return args;
		}
		args = (T[]) Array.newInstance(clazz, i);
		i = 0;
		for(T arg : tmp) {
			if(arg != null) {
				args[i++] = arg;
			}
		}
		return args;
	}
	
	/** @param args The array to clean
	 * @return A cleaned copy of the given array */
	public static final String[] clean(String... args) {
		String[] tmp = args;
		int i = 0;
		for(String arg : args) {
			if(arg != null && !arg.trim().isEmpty()) {
				tmp[i++] = arg;
			}
		}
		args = new String[i];
		i = 0;
		for(String arg : tmp) {
			if(arg != null && !arg.trim().isEmpty()) {
				args[i++] = arg;
			}
		}
		return args;
	}
	
	/** {@inheritDoc} */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String command, String[] args) {
		args = clean(args);
		command = command.startsWith("enteisskyblock:") ? command.substring("enteisskyblock:".length()) : command;
		if(command.equalsIgnoreCase("spawn") || command.equalsIgnoreCase("skyworld") || command.equalsIgnoreCase("test") || command.equalsIgnoreCase("delete")) {
			return new ArrayList<>();
		}
		if(command.equalsIgnoreCase("enteisskyblock") || command.equalsIgnoreCase("esb")) {
			if(args.length == 0) {
				return new ArrayList<>(Arrays.asList(new String[] {"version", "help", "reload", "save", "damageTest"}));
			}
			if(args.length >= 1 && args[0].equalsIgnoreCase("damageTest")) {
				List<String> list = new ArrayList<>();
				if(args.length == 1) {
					list.add("ALL");
					for(DamageCause cause : DamageCause.values()) {
						list.add(cause.name());
					}
				} else {
					for(DamageCause cause : DamageCause.values()) {
						if(cause.name().toLowerCase().startsWith(args[1].toLowerCase())) {
							list.add(cause.name());
						}
					}
				}
				return list;
			}
			return new ArrayList<>();
		}
		if(command.equalsIgnoreCase("iw")) {
			ArrayList<String> list = new ArrayList<>();
			if(args.length == 0) {
				for(Island island : Island.getAllIslands()) {
					if(island.getOwner() != null) {
						list.add(island.getOwnerNamePlain());
					}
				}
			} else if(args.length == 1) {
				for(Island island : Island.getAllIslands()) {
					if(island.getOwner() != null) {
						String ownerName = island.getOwnerNamePlain();
						if(ownerName.toLowerCase().toLowerCase().startsWith(args[0])) {
							list.add(ownerName);
						}
					}
				}
			}
			return list;
		}
		if(command.equalsIgnoreCase("island") || command.equalsIgnoreCase("is")) {
			Player user = sender instanceof Player ? (Player) sender : null;
			Island island = Island.getMainIslandFor(user);
			ArrayList<String> list = new ArrayList<>();
			if(user != null) {
				if(args.length == 0) {
					list.add("help");
					list.add("warp");
					if(island != null) {
						list.add("invite");
						list.add("home");
						list.add("sethome");
						list.add("setwarp");
						list.add("info");
						list.add("level");
						list.add("lock");
						list.add("unlock");
						list.add("biome");
						list.add("leave");
						if(island.isOwner(user)) {
							list.add("kick");
							list.add("restart");
							list.add("delete");
						}
					} else {
						list.add("create");
						list.add("join");
					}
				} else if(args.length >= 1) {
					if(args[0].equalsIgnoreCase("create")) {
						list.add("normal");
						list.add("square");
						if(!GeneratorMain.island_schematic.equalsIgnoreCase("none")) {
							list.add("schematic");
						}
					} else if(args[0].equalsIgnoreCase("join")) {
						for(Island is : Island.getAllIslands()) {
							if(island.getOwner() != null && !is.hasReachedMemberCapacity()) {
								list.add(is.getOwnerNamePlain());
							}
						}
					} else if(args[0].equalsIgnoreCase("kick") && island != null && island.isOwner(user)) {
						for(UUID member : island.getMembers()) {
							if(island.isOwner(member)) {
								continue;
							}
							OfflinePlayer player = Main.server.getOfflinePlayer(member);
							list.add(player.getName());
						}
					} else if(args[0].equalsIgnoreCase("invite") && island != null) {
						for(Player player : Main.server.getOnlinePlayers()) {
							Island check = Island.getMainIslandFor(player);
							if(check == null) {
								list.add(player.getName());
							}
						}
					} else if(args[0].equalsIgnoreCase("warp")) {
						for(Island is : Island.getAllIslands()) {
							if(is.getOwner() != null) {
								list.add(is.getOwnerNamePlain());
							}
						}
					} else if(args[0].equalsIgnoreCase("lock") && island != null && island.isOwner(user)) {
						list.add("on");
						list.add("off");
					} else if(args[0].equalsIgnoreCase("biome") && island != null && island.isOwner(user)) {
						for(Biome biome : Biome.values()) {
							list.add(biome.name());
						}
					}
				}
			}
			return list;
		}
		return null;
	}
	
	/** Gets the command with the given name, specific to this plugin. Commands
	 * need to be registered in the {@link PluginDescriptionFile#getCommands()
	 * PluginDescriptionFile} to exist at runtime.
	 *
	 * @param name name or alias of the command
	 * @return the plugin command if found, otherwise null */
	@Override
	public PluginCommand getCommand(String name) {
		String alias = name.toLowerCase(java.util.Locale.ENGLISH);
		PluginCommand command = this.getServer().getPluginCommand(alias);
		
		if(command == null || command.getPlugin() != this) {
			command = this.getServer().getPluginCommand(this.getDescription().getName().toLowerCase(java.util.Locale.ENGLISH) + ":" + alias);
		}
		
		if(command != null && command.getPlugin() == this) {
			return command;
		}
		return null;
	}
	
	public static final void main(String[] args) {
		ConcurrentHashMap<Material, Integer> sortMePlz = new ConcurrentHashMap<>();
		for(Material material : Material.values()) {
			if(material.isLegacy()) {
				continue;
			}
			sortMePlz.put(material, Integer.valueOf(0));
		}
		for(Entry<Material, Integer> entry : sortMePlz.entrySet()) {
			Material material = entry.getKey();
			System.out.println(material.name());
		}
		/*System.out.println(capitalizeFirstLetterOfEachWord("lava buckets r cool"));
		System.out.println(capitalizeFirstLetterOfEachWord("SMOOTH_STONE".toLowerCase(), '_', ' '));
		
		convertMaterialValuesFromUSkyblock(new File(new File(System.getProperty("user.dir") + File.separator + "plugins" + File.separator + "uSkyblock"), "levelConfig.yml"));
		*/
	}
	
	/** @param str The String to capitalize each word's first letter of
	 * @return The resulting String */
	public static final String capitalizeFirstLetterOfEachWord(String str) {
		return capitalizeFirstLetterOfEachWord(str, ' ');
	}
	
	/** @param str The String to capitalize each word's first letter of
	 * @param separator The separator to use to define the boundary between
	 *            words
	 * @return The resulting String */
	public static final String capitalizeFirstLetterOfEachWord(String str, char separator) {
		return capitalizeFirstLetterOfEachWord(str, separator, separator);
	}
	
	/** @param str The String to capitalize each word's first letter of
	 * @param separator The separator to use to define the boundary between
	 *            words
	 * @param replacementSeparator The character that will replace all instances
	 *            of the provided separator character
	 * @return The resulting String */
	public static final String capitalizeFirstLetterOfEachWord(String str, char separator, char replacementSeparator) {
		StringBuilder sb = new StringBuilder(str.length());
		int i = 0;
		for(char c : str.toCharArray()) {
			if(i == 0 && c != separator) {
				c = Character.toUpperCase(c);
			}
			if(c == separator) {
				i = -1;
				c = replacementSeparator;
			}
			sb.append(c);
			i++;
		}
		return sb.toString();
	}
	
	/** @param str The String to check
	 * @return Whether or not the given String represents a valid Integer */
	public static final boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch(Throwable ignored) {
			return false;
		}
	}
	
	/** @param str The String to check
	 * @return Whether or not the given String represents a valid Double */
	public static final boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch(Throwable ignored) {
			return false;
		}
	}
	
	/** @param str The String to check
	 * @return Whether or not the given String represents a valid Float */
	public static final boolean isFloat(String str) {
		try {
			Float.parseFloat(str);
			return true;
		} catch(Throwable ignored) {
			return false;
		}
	}
	
	/** @param str The string to check
	 * @return Whether or not the given string is a valid UUID */
	public static final boolean isUUID(String str) {
		try {
			UUID.fromString(str).toString();
			return true;
		} catch(Throwable ignored) {
			return false;
		}
	}
	
	/** The number of milliseconds in a second */
	public static final int SECOND = 1000;
	/** The number of milliseconds in a minute */
	public static final int MINUTE = 60 * Main.SECOND;
	/** The number of milliseconds in an hour */
	public static final int HOUR = 60 * Main.MINUTE;
	/** The number of milliseconds in a day */
	public static final double DAY = 24.0000006 * Main.HOUR;
	/** The number of milliseconds in a year */
	public static final double YEAR = 365.2421891 * Main.DAY;
	
	/** @param number The number to convert
	 * @return The given number, in Roman numerals */
	public static final String toRomanNumerals(long number) {
		StringBuilder br = new StringBuilder("");
		while(number != 0) {
			while(number >= 1000) {
				br.append("M");
				number -= 1000;
			}
			while(number >= 900) {
				br.append("CM");
				number -= 900;
			}
			while(number >= 500) {
				br.append("D");
				number -= 500;
			}
			while(number >= 400) {
				br.append("CD");
				number -= 400;
			}
			while(number >= 100) {
				br.append("C");
				number -= 100;
			}
			while(number >= 90) {
				br.append("XC");
				number -= 90;
			}
			while(number >= 50) {
				br.append("L");
				number -= 50;
			}
			while(number >= 40) {
				br.append("XL");
				number -= 40;
			}
			while(number >= 10) {
				br.append("X");
				number -= 10;
			}
			while(number >= 9) {
				br.append("IX");
				number -= 9;
			}
			while(number >= 5) {
				br.append("V");
				number -= 5;
			}
			while(number >= 4) {
				br.append("IV");
				number -= 4;
			}
			while(number >= 1) {
				br.append("I");
				number -= 1;
			}
		}
		return br.toString();
	}
	
	/** @param milliseconds The amount of time, in milliseconds
	 * @return The given length of time, in human-readable format */
	public static final String getLengthOfTime(long milliseconds) {
		StringBuffer text = new StringBuffer("");
		boolean yearsDaysOrHours = false;
		if(milliseconds >= Main.YEAR) {//dang. That is a long time. Haha lol I made an accidental funny.
			double years = milliseconds / Main.YEAR;
			text.append(years).append(years == 1L ? " year " : " years ");//I don't think that a Java long value can hold more than one year tbh... oh well. To think I wanted to add decades and centuries too XD
			milliseconds %= Main.YEAR;
			yearsDaysOrHours = true;
		}
		if(milliseconds >= Main.DAY) {
			double days = milliseconds / Main.DAY;
			text.append(days).append(days == 1 ? " day " : " days ");
			milliseconds %= Main.DAY;
			yearsDaysOrHours = true;
		}
		if(milliseconds >= Main.HOUR) {
			long hours = milliseconds / Main.HOUR;
			text.append(hours).append(hours == 1 ? " hours " : " hours ");
			milliseconds %= Main.HOUR;
			yearsDaysOrHours = true;
		}
		boolean minutes = false;
		boolean plural = true;
		if(milliseconds >= Main.MINUTE) {
			if(yearsDaysOrHours) {
				text.append("and ");
			} else {
				text.append("00:");
			}
			long mins = milliseconds / Main.MINUTE;
			text.append((mins >= 10 ? "" : "0") + mins).append(":");
			milliseconds %= Main.MINUTE;
			minutes = true;
			plural = mins != 1;
		} else {
			if(yearsDaysOrHours) {
				text.append("and 00:");
			} else {
				text.append("00:00:");
			}
		}
		if(milliseconds >= Main.SECOND) {
			long sec = milliseconds / Main.SECOND;
			text.append((sec >= 10 ? "" : "0") + sec);
			milliseconds %= Main.SECOND;
			plural = !minutes ? sec != 1 : plural;
		} else {
			text.append("00");
		}
		text.append(plural ? (minutes ? " minutes" : " seconds") : (minutes ? " minute" : " second"));
		return text.toString();
	}
	
	/** @param time The time to convert
	 * @return The given time and date, in standard format */
	public static final String getTimeAndDate(long time) {
		return new SimpleDateFormat("MMM dd, yyyy @ HH:mm:ss").format(new Date(time));
	}
	
	/** @param getTimeOnly Whether or not the result should only include the
	 *            time
	 * @return The given time, in standard format */
	public static String getSystemTime(boolean getTimeOnly) {
		return new SimpleDateFormat(getTimeOnly ? "HH:mm:ss" : "MM/dd/yyyy HH:mm:ss").format(new Date());
	}
	
	/** @param args The arguments
	 * @param start The start index
	 * @param c The separator character
	 * @return The resulting String */
	public static final String stringArrayToString(String[] args, int start, char c) {
		return stringArrayToString(args, start, args.length, c);
	}
	
	/** @param args The arguments
	 * @param start The start index
	 * @param end The end index
	 * @param c The separator character
	 * @return The resulting String */
	public static final String stringArrayToString(String[] args, int start, int end, char c) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(String arg : args) {
			if(i >= end) {
				break;
			}
			if(i < start) {
				i++;
				continue;
			}
			sb.append(arg);
			sb.append(c);
		}
		if(sb.charAt(sb.length() - 1) == c) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}
	
	/** @return The WorldEdit plugin, if loaded */
	public static final Plugin getWorldEdit() {
		Plugin plugin = Main.pluginMgr.getPlugin("FastAsyncWorldEdit");
		return plugin == null ? Main.pluginMgr.getPlugin("WorldEdit") : plugin;
	}
	
	/** @return The WorldGuard plugin, if loaded */
	public static final Plugin getWorldGuard() {
		return Main.pluginMgr.getPlugin("WorldGuard");
	}
	
	@Override
	public void onLoad() {
		plugin = this;
		server = this.getServer();
		console = server.getConsoleSender();
		scheduler = server.getScheduler();
		pluginMgr = server.getPluginManager();
	}
	
	@Override
	public void onDisable() {
		Island.stopSpawnTask();
		
		saveAll();
		Island.islands.clear();
		if(this.entityDmgListener != null) {
			HandlerList.unregisterAll(this.entityDmgListener);
			this.entityDmgListener = null;
		}
		HandlerList.unregisterAll((Plugin) this);
		scheduler.cancelTasks(this);
		if(vaultEnabled) {
			VaultHandler.onDisable();
		}
	}
	
	private static final void getCommandMap() {
		SimplePluginManager pluginMgr = (SimplePluginManager) Main.pluginMgr;
		try {
			Field commandMapField = null;
			for(Field field : SimplePluginManager.class.getDeclaredFields()) {
				if(field.getName().equals("commandMap")) {
					commandMapField = field;
					break;
				}
			}
			if(commandMapField != null) {
				boolean wasAccessible = commandMapField.isAccessible();
				commandMapField.setAccessible(true);
				commandMap = (SimpleCommandMap) commandMapField.get(pluginMgr);
				commandMapField.setAccessible(wasAccessible);
			}
		} catch(IllegalAccessException ignored) {
		}
	}
	
	private static final void fightDemOtherPluginsGrrr() {
		Main.scheduler.runTaskLater(getPlugin(), () -> {
			final boolean[] foundOne = {false};
			final DamageCause[] dc = new DamageCause[] {null};
			for(Plugin plugin : pluginMgr.getPlugins()) {
				if(plugin == Main.getPlugin()) {
					continue;
				}
				List<RegisteredListener> listeners = new ArrayList<>(HandlerList.getRegisteredListeners(plugin));
				Runnable code = () -> {
					for(RegisteredListener registeredListener : listeners) {
						Listener listener = registeredListener.getListener();
						test_player.teleport(GeneratorMain.getSkyworldSpawnLocation());
						test_player.setGameMode(GameMode.SURVIVAL);
						test_player.setFlying(false);
						test_player.setAllowFlight(false);
						EntityDamageEvent event = new EntityDamageEvent(test_player, dc[0], 1.0);
						event.setCancelled(false);
						try {
							registeredListener.callEvent(event);
						} catch(Throwable ex) {
							Main.server.getLogger().log(Level.SEVERE, "Could not pass event ".concat(event.getEventName()).concat(" to ").concat(plugin.getDescription().getFullName()), ex);
						}
						if(event.isCancelled()) {
							foundOne[0] = true;
							String msg1 = ChatColor.RED.toString().concat(" /!\\  Plugin \"").concat(ChatColor.WHITE.toString()).concat(plugin.getName()).concat(ChatColor.RED.toString()).concat("\" is currently cancelling a damage event(damage cause: \"").concat(ChatColor.WHITE.toString()).concat(dc[0].name()).concat(ChatColor.RED.toString()).concat("\") in skyworld!");
							String msg2;
							if(!listener.getClass().getSimpleName().contains("Afk") && plugin != Main.getWorldEdit() && plugin != Main.getWorldGuard() && plugin != Main.getPlugin()) {
								msg2 = ChatColor.RED.toString().concat("/___\\ The event listener \"").concat(ChatColor.WHITE.toString()).concat(listener.getClass().getSimpleName()).concat(ChatColor.RED.toString()).concat("\" has been unregistered to prevent this.");
								HandlerList.unregisterAll(listener);
							} else {
								msg2 = ChatColor.RED.toString().concat("/___\\ The event listener \"").concat(ChatColor.WHITE.toString()).concat(listener.getClass().getSimpleName()).concat(ChatColor.RED.toString()).concat("\" was not unregistered, as it is either this plugin, WorldEdit or WorldGuard.");
							}
							if(listener.getClass().getSimpleName().contains("Afk")) {
								msg2 = ChatColor.RED.toString().concat("/___\\ The event listener \"").concat(ChatColor.WHITE.toString()).concat(listener.getClass().getSimpleName()).concat(ChatColor.RED.toString()).concat("\" was not unregistered, as it appears to be beneficial.");
							}
							Main.getPluginLogger().warning(msg1);
							Main.getPluginLogger().warning(msg2);
						}
					}
				};
				for(DamageCause c : DamageCause.values()) {
					dc[0] = c;
					code.run();
				}
				
			}
			if(!foundOne[0]) {
				Main.getPluginLogger().info(ChatColor.GREEN.toString().concat("No plugins cancelled any EntityDamageEvents within skyworld. Aweseome!"));
			}
			unregisterDumbCommands();
		}, 100L);
	}
	
	private static final void unregisterDumbCommands() {
		getCommandMap();
		if(commandMap != null) {
			PluginCommand command = Main.server.getPluginCommand("gamemode");
			if(command != null) {
				command.unregister(commandMap);
			}
			command = Main.server.getPluginCommand("teleport");
			if(command != null) {
				command.unregister(commandMap);
			}
			command = Main.server.getPluginCommand("tp");
			if(command != null) {
				command.unregister(commandMap);
			}
			/*command = Main.server.getPluginCommand("time");
			if(command != null) {
				command.unregister(commandMap);
			}*/
		}
	}
	
	private volatile DamageEventListeners entityDmgListener = null;
	private static volatile Player test_player;
	
	@Override
	public void onEnable() {
		System.out.println(capitalizeFirstLetterOfEachWord("lava buckets r cool"));//main(new String[0]);
		File folder = this.getDataFolder();
		folder.mkdirs();
		getCommandMap();
		pluginMgr.registerEvents(this, this);
		if(this.entityDmgListener != null) {
			HandlerList.unregisterAll(this.entityDmgListener);
			this.entityDmgListener = null;
		}
		pluginMgr.registerEvents((this.entityDmgListener = new DamageEventListeners()), this);
		//if(getWorldEdit() != null) {
		//	HandlerList.unregisterAll(getWorldEdit());//prevents "/tell" server crash ... :/
		//}
		updateSpawnRegions();
		loadAll();
		
		try {
			VaultHandler.setupVault();
			vaultEnabled = true;
		} catch(ClassNotFoundException ignored) {
			vaultEnabled = false;
		}
		Main.scheduler.runTaskLater(getPlugin(), () -> {
			test_player = new PlayerAdapter(UUID.fromString("91c2ca97-7a9f-4833-b66f-e39c9b66e690"), "Brian_Entei", GeneratorMain.getSkyworldSpawnLocation(), GameMode.SURVIVAL);
		}, 5L);
		//fightDemOtherPluginsRawr();
		
		//====================================
		/*Island test = Island.getOrCreate(0, 2);
		test.deleteIsland();
		Island.islands.add(test);
		test.updateRegion();
		test.generateIsland();*/
		
		ShapelessRecipe mossyCobblestone = new ShapelessRecipe(new NamespacedKey(this, "0241139d-8c6a-4093-adad-fec4fe2b8b15"), new ItemStack(Material.MOSSY_COBBLESTONE, 1)).addIngredient(1, Material.COBBLESTONE).addIngredient(1, Material.VINE);
		try {
			server.addRecipe(mossyCobblestone);
		} catch(IllegalStateException ignored) {
		}
		ShapelessRecipe mossyBricks = new ShapelessRecipe(new NamespacedKey(this, "f53a557e-7d16-4449-9bcb-8b330d856b13"), new ItemStack(Material.MOSSY_STONE_BRICKS, 1)).addIngredient(1, Material.STONE_BRICKS).addIngredient(1, Material.VINE);
		try {
			server.addRecipe(mossyBricks);
		} catch(IllegalStateException ignored) {
		}
		ShapelessRecipe mossyCobbleWall = new ShapelessRecipe(new NamespacedKey(this, "372baa13-9865-48e1-a69e-f45db6d2e4e0"), new ItemStack(Material.MOSSY_COBBLESTONE_WALL, 1)).addIngredient(1, Material.COBBLESTONE_WALL).addIngredient(1, Material.VINE);
		try {
			server.addRecipe(mossyCobbleWall);
		} catch(IllegalStateException ignored) {
		}
		
		ShapelessRecipe melonSlices = new ShapelessRecipe(new NamespacedKey(this, "1083c12e-0614-41cf-b33d-76e88a410add"), new ItemStack(Material.MELON_SLICE, 9)).addIngredient(1, Material.MELON);
		try {
			server.addRecipe(melonSlices);
		} catch(IllegalStateException ignored) {
		}
		
		ChallengeCommand.register();
		//UnsafeEnchantCommand.class.getName();//Prevents java.lang.NoClassDefFoundError: com/gmail/br45entei/commands/UnsafeEnchantCommand in onDisable...
		/*PluginCommand command = this.getCommand("unsafeEnchant");
		if(command != null) {
			command.setExecutor((sender, command1, label, args) -> UnsafeEnchantCommand.onCommand(sender, label, args));
		}*/
		for(Player player : server.getOnlinePlayers()) {
			InventoryView view = player.getOpenInventory();
			if(InventoryGUI.isOldChestView(view)) {
				InventoryGUI.fixPlayersOldChestView(player);
			}
		}
		
		Island.startSpawnTask();
	}
	
	public static final void saveAll() {
		InventoryGUI.savePerPlayerInventories();
		Island.saveAllIslands();
		GeneratorMain.saveAll();
		if(!Challenge.saveChallenges()) {
			getPluginLogger().warning("Some challenges did not save successfully! Check the log for details...");
		} else {
			getPluginLogger().info("Saved ".concat(Integer.toString(Challenge.getChallenges().size())).concat(" challenge").concat(Challenge.getChallenges().size() == 1 ? "" : "s").concat(" to file."));
		}
		getPlugin().saveMaterialConfig();
		getPlugin().saveConfig();
	}
	
	public static final void loadAll() {
		getPlugin().saveDefaultConfig();
		getPlugin().reloadConfig();
		getPlugin().loadMaterialConfig();
		int loadedChallenges = Challenge.loadChallenges();
		getPluginLogger().info("Loaded ".concat(Integer.toString(loadedChallenges)).concat(" challenge").concat(loadedChallenges == 1 ? "" : "s").concat(" from file."));
		GeneratorMain.loadAll();
		Island.loadAllIslands();
		InventoryGUI.loadPerPlayerInventories();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockSpreadEvent(BlockSpreadEvent event) {
		if(event.getBlock().getWorld() != GeneratorMain.getSkyworld() && event.getBlock().getWorld() != GeneratorMain.getSkyworldNether()) {
			return;
		}
		Block spreading = event.getBlock();
		Island island = Island.getIslandContaining(spreading.getLocation());
		if(island != null) {
			if(event.isCancelled()) {
				event.setCancelled(false);
			}
		} else {
			if(event.getNewState().getType() != Material.WATER && event.getNewState().getType() != Material.LAVA) {
				event.setCancelled(!isInSpawn(spreading.getLocation()));
				if(event.getNewState().getType() == Material.FIRE) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockBurnEvent(BlockBurnEvent event) {
		if(event.getBlock().getWorld() != GeneratorMain.getSkyworld() && event.getBlock().getWorld() != GeneratorMain.getSkyworldNether()) {
			return;
		}
		//Block igniterBlock = event.getIgnitingBlock();
		Block burned = event.getBlock();
		Island island = Island.getIslandContaining(burned.getLocation());
		if(island != null) {
			if(event.isCancelled()) {
				event.setCancelled(false);
			}
		} else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockIgniteEvent(BlockIgniteEvent event) {
		if(event.getBlock().getWorld() != GeneratorMain.getSkyworld() && event.getBlock().getWorld() != GeneratorMain.getSkyworldNether()) {
			return;
		}
		//Player igniter = event.getPlayer();
		//Block igniterBlock = event.getIgnitingBlock();
		Block ignited = event.getBlock();
		Island island = Island.getIslandContaining(ignited.getLocation());
		if(island != null) {
			if(event.isCancelled()) {
				event.setCancelled(false);
			}
		} else {
			event.setCancelled(!isInSpawn(ignited.getLocation()));
			Player pyromaniac = event.getPlayer();
			if(pyromaniac != null) {// && event.getCause() == IgniteCause.FLINT_AND_STEEL) {
				if(!pyromaniac.hasPermission("skyblock.admin")) {
					event.setCancelled(true);
					pyromaniac.sendMessage(ChatColor.RED.toString().concat("You do not have permission to set fire to blocks outside of your island."));
					return;
				}
				event.setCancelled(false);
			}
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockFadeEvent(BlockFadeEvent event) {
		if(event.getBlock().getWorld() != GeneratorMain.getSkyworld() && event.getBlock().getWorld() != GeneratorMain.getSkyworldNether()) {
			return;
		}
		Block fading = event.getBlock();
		Material newType = event.getNewState() == null ? null : event.getNewState().getType();
		if(fading != null && fading.getType() != newType) {
			Island island = Island.getIslandContaining(fading.getLocation());
			if(island != null) {
				if(event.isCancelled()) {
					event.setCancelled(false);
				}
			} else {
				event.setCancelled(event.getBlock().getWorld() != GeneratorMain.getSkyworldNether());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onInventoryClickEvent(InventoryClickEvent event) {
		Player clicker = event.getWhoClicked() instanceof Player ? (Player) event.getWhoClicked() : null;
		Inventory inv = event.getClickedInventory();
		String title = event.getView().getTitle();
		if(clicker != null && title.equals(Challenge.getChallengeScreenTitle())) {
			if((inv == event.getView().getBottomInventory() && event.getClick().isShiftClick()) || (inv == event.getView().getTopInventory())) {
				event.setCancelled(true);
			}
			int slot = event.getRawSlot();
			Challenge selected = Challenge.getChallengeBySlot(slot);
			if(selected != null) {
				clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 3f, 1f);
				Island island = Island.getMainIslandFor(clicker);
				
				ItemStack icon = selected.getIconFor(clicker);
				boolean debug1 = icon.hasItemMeta();
				boolean debug2 = debug1 && icon.getItemMeta().hasDisplayName();
				String debugTitle = debug2 ? ChatColor.stripColor(icon.getItemMeta().getDisplayName()).trim() : null;
				boolean debug3 = debugTitle != null && debugTitle.endsWith(" - Locked");
				if(debug3) {
					clicker.sendMessage(ChatColor.RED.toString().concat("You must complete at least half of the challenges in the previous tier before you can attempt this one!"));
					return;
				}
				Main.sendDebugMsg(clicker, "debug1: ".concat(Boolean.toString(debug1)).concat("; debug2: ").concat(Boolean.toString(debug2)).concat("; debugTitle: \"").concat(ChatColor.WHITE.toString()).concat(debugTitle == null ? "<null>" : debugTitle).concat(ChatColor.RESET.toString()).concat("\"; debug3: ").concat(Boolean.toString(debug3)).concat(";"));
				if(selected.complete(clicker)) {
					clicker.sendMessage(ChatColor.GREEN + "You just completed the \"" + ChatColor.WHITE + selected.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" challenge!");
					final InventoryView view = event.getView();
					Main.scheduler.runTaskLater(getPlugin(), () -> {
						if(clicker.getOpenInventory() == view) {
							for(int slot1 = 0; slot1 < view.getTopInventory().getSize(); slot1++) {
								Challenge challenge = Challenge.getChallengeBySlot(slot1);
								if(challenge != null) {
									view.getTopInventory().setItem(slot1, challenge.getIconFor(clicker));//view.getTopInventory().setItem(slot1, challenge.getIcon(island != null ? island.hasMemberCompleted(clicker, challenge) : false));
								}
							}
							clicker.updateInventory();
						}
					}, 2L);
				} else {
					if(!selected.isRepeatable() && island != null && island.hasMemberCompleted(clicker, selected)) {
						clicker.sendMessage(ChatColor.YELLOW + "The challenge \"" + ChatColor.WHITE + selected.getDisplayName() + ChatColor.RESET + ChatColor.YELLOW + "\" is not repeatable.");
					} else {
						clicker.sendMessage(ChatColor.YELLOW + "You still have requirements to meet before you can complete the \"" + ChatColor.WHITE + selected.getDisplayName() + ChatColor.RESET + ChatColor.YELLOW + "\" challenge.");
					}
				}
			} else {
				if(slot == Challenge.getChallengeScreenSize() - 1) {
					event.setCancelled(true);
					clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 3f, 1f);
					//clicker.closeInventory();
					//clicker.updateInventory();
					Main.server.dispatchCommand(clicker, "island");
				}
			}
		} else if(clicker != null && ChatColor.stripColor(title.trim()).trim().endsWith("'s Challenges")) {
			if((inv == event.getView().getBottomInventory() && event.getClick().isShiftClick())) {
				event.setCancelled(true);
				return;
			}
			if(inv == event.getView().getTopInventory()) {
				event.setCancelled(true);
				InventoryHolder holder = inv.getHolder();
				if(holder instanceof Player) {
					final Player player = (Player) holder;
					Island island = Island.getMainIslandFor(player);
					if(island == null) {
						clicker.sendMessage(ChatColor.RED.toString().concat("Unable to toggle challenge state - this player doesn't have a main island where challenge information can be stored."));
						return;
					}
					int slot = event.getRawSlot();
					Challenge selected = Challenge.getChallengeBySlot(slot);
					if(selected != null) {
						int timesCompletedChallenge = island.getNumTimesChallengeCompletedBy(player.getUniqueId(), selected.getName());
						if(event.getClick() == ClickType.LEFT) {
							if(island.hasMemberCompleted(player, selected)) {
								island.setUncompleted(player, selected);
							} else {
								island.setCompleted(player, selected);
							}
							clicker.sendMessage(ChatColor.YELLOW.toString().concat("Toggled player \"").concat(player.getName()).concat("\"'s challenge \"").concat(selected.getName()).concat("\"'s completion state to: ").concat(island.hasMemberCompleted(player, selected) ? ChatColor.GREEN.toString().concat("Completed - their challenge completion count was incremented from 0 to 1") : ChatColor.RED.toString().concat("Uncompleted - their challenge completion count was reset to 0 from ").concat(Integer.toString(timesCompletedChallenge)).concat(".")));
						} else {
							boolean increment = event.getClick() == ClickType.MIDDLE || event.getClick() == ClickType.SHIFT_LEFT;
							boolean decrement = event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT;
							if(increment || decrement) {
								int beforeCount = island.getNumTimesChallengeCompletedBy(player, selected);
								(increment ? island.incrementNumTimesChallengeCompletedBy(player, selected) : island.decrementNumTimesChallengeCompletedBy(player, selected)).toString();
								int afterCount = island.getNumTimesChallengeCompletedBy(player, selected);
								clicker.sendMessage(ChatColor.YELLOW.toString().concat(increment ? "In" : "De").concat("cremented the number of times that player \"").concat(player.getName()).concat("\" has completed challenge \"").concat(selected.getName()).concat("\" from ").concat(Integer.toString(beforeCount)).concat(" to ").concat(Integer.toString(afterCount)).concat("."));
							}
						}
						//final InventoryView view = event.getView();
						Main.scheduler.runTaskLater(getPlugin(), () -> {
							if(clicker.getOpenInventory().getTitle().equals(title)) {
								for(int slot1 = 0; slot1 < inv.getSize(); slot1++) {
									Challenge challenge = Challenge.getChallengeBySlot(slot1);
									if(challenge != null) {
										inv.setItem(slot1, challenge.getIconFor(player));//view.getTopInventory().setItem(slot1, challenge.getIcon(island != null ? island.hasMemberCompleted(clicker, challenge) : false));
									}
								}
								clicker.updateInventory();
							}
						}, 2L);
						return;
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public static final void onChallengeCompleteEventMonitor(ChallengeCompleteEvent event) {
		Player player = event.getPlayer();
		Island island = event.getIsland();
		Challenge challenge = event.getChallenge();
		String msg = ChatColor.WHITE + player.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " has just completed the \"" + ChatColor.WHITE + challenge.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" challenge!";
		if(island != null && !island.hasMemberCompleted(player, challenge)) {
			for(Player p : Main.server.getOnlinePlayers()) {
				if(!p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
					p.sendMessage(msg);
				}
			}
			Main.console.sendMessage(msg);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public static final void onChallengeTierCompleteEventMonitor(ChallengeTierCompleteEvent event) {
		Player player = event.getPlayer();
		Island island = event.getIsland();
		Challenge challenge = event.getChallenge();
		boolean fullyOrHalfway = event.didPlayerCompleteEntireTier();
		String msg = ChatColor.WHITE + player.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " has just completed " + (fullyOrHalfway ? "all " : "half ") + "of the " + ChatColor.WHITE + Challenge.getTierTitle(event.getTier()).concat("s") + ChatColor.RESET + ChatColor.GREEN + (fullyOrHalfway ? "" : ", and has unlocked the next tier(which is: " + ChatColor.WHITE + Challenge.getTierTitle(event.getTier() + 1).concat("s") + ChatColor.RESET + ChatColor.GREEN + ")") + "!";
		if(island != null && island.hasMemberCompleted(player, challenge)) {
			for(Player p : Main.server.getOnlinePlayers()) {
				p.sendMessage(msg);
			}
			Main.console.sendMessage(msg);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockPhysicsEvent(BlockPhysicsEvent event) {
		if(Main.isInSpawn(event.getBlock().getLocation()) || Island.getIslandContaining(event.getBlock().getLocation()) == null) {
			if(event.getChangedType() == Material.SAND || event.getChangedType() == Material.GRAVEL || event.getChangedType() == Material.ANVIL || event.getChangedType() == Material.RED_SAND || event.getChangedType() == Material.DRAGON_EGG || event.getChangedType().name().endsWith("_CONCRETE_POWDER")) {
				Block down = event.getBlock().getRelative(BlockFace.DOWN);
				if(down != null && !down.getType().isSolid()) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();
		Island island = Island.getIslandContaining(location);
		if(Island.isInSkyworld(player) && (island == null || (!island.isMember(player)))) {
			if(!player.hasPermission("skyblock.admin")) {
				event.setCancelled(true);
				if(location.getWorld() == GeneratorMain.getSkyworldTheEnd()) {
					event.setCancelled(isInSpawn(location));
				}
				if(event.isCancelled()) {
					player.sendMessage(ChatColor.RED + "You do not have permission to edit blocks outside of your island.");
					return;
				}
			}
		}
		Block block = event.getBlock();
		if(player.getGameMode() != GameMode.ADVENTURE && player.getGameMode() != GameMode.SURVIVAL) {
			return;
		}
		ItemStack tool = null;
		ItemStack hand = player.getInventory().getItemInMainHand();
		ItemStack offHand = player.getInventory().getItemInOffHand();
		if(hand != null && Enchantment.DURABILITY.canEnchantItem(hand)) {
			tool = hand;
		} else if(offHand != null && Enchantment.DURABILITY.canEnchantItem(offHand)) {
			tool = offHand;
		}
		if((tool == null ? block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN : true)) {
			Block up = block.getRelative(0, 1, 0);
			Block down = block.getRelative(0, -1, 0);
			Block down2 = block.getRelative(0, -2, 0);
			Block left = block.getRelative(1, 0, 0);
			Block right = block.getRelative(-1, 0, 0);
			Block front = block.getRelative(0, 0, 1);
			Block back = block.getRelative(0, 0, -1);
			if(((up != null && (up.getType() == Material.LAVA || up.getType() == Material.LEGACY_STATIONARY_LAVA)) ||//
					(down != null && (down.getType() == Material.LAVA || down.getType() == Material.LEGACY_STATIONARY_LAVA)) ||//
					(left != null && (left.getType() == Material.LAVA || left.getType() == Material.LEGACY_STATIONARY_LAVA)) ||//
					(right != null && (right.getType() == Material.LAVA || right.getType() == Material.LEGACY_STATIONARY_LAVA)) ||//
					(front != null && (front.getType() == Material.LAVA || front.getType() == Material.LEGACY_STATIONARY_LAVA)) ||//
					(back != null && (back.getType() == Material.LAVA || back.getType() == Material.LEGACY_STATIONARY_LAVA))) ||//
					((down == null || (down.getType() == Material.WALL_SIGN || down.getType() == Material.SIGN || !down.getType().isSolid())) && (down2 == null || (down2.getType() == Material.WALL_SIGN || down2.getType() == Material.SIGN || !down2.getType().isSolid())))) {
				
				Collection<ItemStack> drops = null;
				if(tool != null) {
					drops = block.getDrops(tool);
				} else {
					drops = block.getDrops();
				}
				int expToDrop = event.getExpToDrop();
				expToDrop = expToDrop == 0 ? getExpForBlock(block) : expToDrop;
				if(expToDrop > 0) {
					event.setExpToDrop(0);//event.setCancelled(true);
					ExperienceOrb orb = dropExperience(player.getLocation(), expToDrop);
					orb.setInvulnerable(true);
					orb.setGravity(false);
					location.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3f, 1f);
				}
				if(drops != null) {
					event.setDropItems(false);//event.setCancelled(true);
					//event.getBlock().setType(Material.AIR, true);// Causes the player's tool to not take durability damage
					if(!drops.isEmpty()) {
						for(ItemStack drop : drops) {
							Item item = location.getWorld().dropItem(player.getLocation(), drop);
							item.setPickupDelay(0);
							/*try {
								item.setCanMobPickup(true);
							} catch(NoSuchMethodError ignored) {
							}*/
							item.setTicksLived(1);
						}
						location.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 3f, 1f + random.nextFloat() + random.nextFloat());
					}
				}
			}
		}
	}
	
	public static final boolean isLiquid(Material material) {
		return material == Material.LEGACY_STATIONARY_WATER || material == Material.LEGACY_STATIONARY_LAVA || material == Material.WATER || material == Material.LAVA;
	}
	
	public static final boolean isRedstone(Material material) {
		switch(material) {
		case LEVER:
		case STONE_PRESSURE_PLATE:
		case LEGACY_WOOD_PLATE:
		case HEAVY_WEIGHTED_PRESSURE_PLATE:
		case LIGHT_WEIGHTED_PRESSURE_PLATE:
		case LEGACY_TRAP_DOOR:
		case IRON_TRAPDOOR:
		case REDSTONE:
		case REDSTONE_BLOCK:
		case LEGACY_REDSTONE_COMPARATOR:
		case LEGACY_REDSTONE_COMPARATOR_OFF:
		case LEGACY_REDSTONE_COMPARATOR_ON:
		case COMPARATOR:
		case LEGACY_REDSTONE_LAMP_OFF:
		case LEGACY_REDSTONE_LAMP_ON:
		case LEGACY_REDSTONE_TORCH_OFF:
		case LEGACY_REDSTONE_TORCH_ON:
		case REDSTONE_TORCH:
		case REDSTONE_WIRE:
		case LEGACY_DIODE:
		case LEGACY_DIODE_BLOCK_OFF:
		case LEGACY_DIODE_BLOCK_ON:
		case DAYLIGHT_DETECTOR:
		case LEGACY_DAYLIGHT_DETECTOR_INVERTED:
		case STONE_BUTTON:
		case LEGACY_WOOD_BUTTON:
			return true;
		//$CASES-OMITTED$
		default:
			if(material.name().contains("_TRAPDOOR")) {
				return true;
			}
			return false;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockFromToEvent(BlockFromToEvent event) {
		Location location = event.getBlock().getLocation();
		if(Island.getIslandContaining(event.getBlock().getLocation()) != null) {
			if(isRedstone(event.getToBlock().getType()) && isLiquid(event.getBlock().getType())) {
				event.setCancelled(true);
				return;
			}
		}
		Location from = event.getBlock().getLocation();
		Location to = event.getToBlock().getLocation();
		Island fromIsland = Island.getIslandContaining(from);
		Island toIsland = Island.getIslandContaining(to);
		if(fromIsland == null) {
			if(toIsland == null) {
				return;
			}
			event.setCancelled(true);
			return;
		}
		if(toIsland == null) {
			event.setCancelled(true);
			return;
		}
		event.setCancelled(fromIsland != toIsland);//if borderSize == 0, this could happen
		if(event.getBlock().getType() == Material.ICE && event.getToBlock().getType() == Material.WATER) {
			if(location.getWorld() == GeneratorMain.getSkyworld() && Island.getIslandContaining(location) == null) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onLeavesDecayEvent(LeavesDecayEvent event) {
		Location location = event.getBlock().getLocation();
		if(location.getWorld() == GeneratorMain.getSkyworld() && Island.getIslandContaining(location) == null) {
			event.setCancelled(true);
		}
	}
	
	protected static final SecureRandom random = new SecureRandom();
	
	private static final int getExpForBlock(Block block) {
		if(block.getType().name().contains("_ORE")) {
			Collection<ItemStack> drops = block.getDrops(new ItemStack(Material.DIAMOND_PICKAXE));
			if(!drops.isEmpty() && drops.iterator().next().getType() != block.getType()) {//If the ore doesn't drop itself, then:
				return random.nextInt(17);
			}
		}
		return 0;
	}
	
	public static final ExperienceOrb dropExperience(Location location, int exp) {
		ExperienceOrb orb = location.getWorld().spawn(location, ExperienceOrb.class);
		orb.setExperience(exp);
		return orb;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onBlockBreakEventMonitor(BlockBreakEvent event) {
		if(event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		Block broken = event.getBlock();
		BlockState state = broken.getState();
		if(state instanceof CreatureSpawner) {
			event.setCancelled(true);
			EntityType type = ((CreatureSpawner) state).getSpawnedType();
			String name = Main.capitalizeFirstLetterOfEachWord(type.name().toLowerCase(), '_', ' ') + " Spawner";
			Material material = Material.getMaterial(type.name().concat("_SPAWN_EGG"));
			material = material == null ? Material.PIG_SPAWN_EGG : material;
			//EntityType.PIG.name(); = "PIG"; "PIG" + "_SPAWN_EGG" = "PIG_SPAWN_EGG"; Material.PIG_SPAWN_EGG exists; etc....
			ItemStack egg = new ItemStack(material, 1);
			ItemMeta meta = egg.getItemMeta();
			if(meta == null) {
				meta = Main.server.getItemFactory().getItemMeta(material);
			}
			meta.setDisplayName(name);
			egg.setItemMeta(meta);
			broken.setType(Material.AIR, true);
			player.getWorld().dropItemNaturally(broken.getLocation(), egg);
		}
	}
	
	/** @param player The player to give the item(s) to
	 * @param items The item(s) to give
	 * @return A list of any items that were dropped at the player's feet as a
	 *         result */
	public static final List<Item> giveItemToPlayer(Player player, ItemStack... items) {
		ArrayList<Item> list = new ArrayList<>();
		HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(items);
		for(ItemStack leftover : leftovers.values()) {
			list.add(player.getWorld().dropItem(player.getLocation(), leftover));
		}
		return list;
	}
	
	/** @param item The ItemStack to check for
	 * @param list The list to check through
	 * @return Whether or not the list contains the given ItemStack */
	public static boolean contains(ItemStack item, Collection<ItemStack> list) {
		for(ItemStack check : list) {
			if(check.equals(item)) {
				return true;
			}
		}
		return false;
	}
	
	/** @param item0 One of the items to compare
	 * @param item1 The other of the items to compare
	 * @return Whether or not the items are of the same type */
	@SuppressWarnings("deprecation")
	public static final boolean isSameType(ItemStack item0, ItemStack item1) {
		return item0.getType().getId() == item1.getType().getId() && (item0.getType().isItem() ? true : item0.getDurability() == item1.getDurability()) && item0.hasItemMeta() == item1.hasItemMeta() && (item0.hasItemMeta() ? Bukkit.getItemFactory().equals(item0.getItemMeta(), item1.getItemMeta()) : true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onCreatureSpawnEvent(CreatureSpawnEvent event) {
		Location location = event.getLocation();
		if(!Island.isInSkyworld(location)) {
			return;
		}
		SpawnReason reason = event.getSpawnReason();
		Island island = Island.getIslandContaining(location);
		if(island == null) {
			event.setCancelled(reason == SpawnReason.NATURAL || reason == SpawnReason.DEFAULT || reason == SpawnReason.CHUNK_GEN/* || reason == SpawnReason.SPAWNER*/ || reason == SpawnReason.SLIME_SPLIT || reason == SpawnReason.SILVERFISH_BLOCK);
			return;
		}
		if(reason == SpawnReason.BREEDING) {
			if(event.getEntity().getType() == EntityType.VILLAGER) {
				island.sendMessage("Some villagers have bred, and a baby was born! Location: " + location.toVector().toString());
				event.setCancelled(false);
				Villager villager = (Villager) event.getEntity();
				Profession profession = villager.getProfession();
				if(profession == Profession.NITWIT) {
					//... what was I going to do here?
				}
			}
			return;
		}
		if(reason == SpawnReason.DEFAULT || reason == SpawnReason.CHUNK_GEN || reason == SpawnReason.SHOULDER_ENTITY || reason == SpawnReason.SLIME_SPLIT || reason == SpawnReason.SPAWNER_EGG || reason == SpawnReason.DISPENSE_EGG || reason == SpawnReason.EGG || reason == SpawnReason.VILLAGE_DEFENSE || reason == SpawnReason.BUILD_IRONGOLEM || reason == SpawnReason.BUILD_SNOWMAN || reason == SpawnReason.BUILD_WITHER) {
			return;
		}
		event.setCancelled(!Island.checkBlockFor(event.getEntity().getClass(), location.getBlock()));
		if(event.isCancelled()) {
			location.getWorld().spawnParticle(Particle.BARRIER, location.add(0x0.0p0, 0.5, 0x0.0p0), 17);
			location.getWorld().spawnParticle(Particle.BARRIER, location.add(0x0.0p0, 1, 0x0.0p0), 17);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onEntityExplodeEvent(EntityExplodeEvent event) {
		if(event.getEntity().getWorld() == GeneratorMain.getSkyworld() || (Island.isInSkyworld(event.getEntity().getLocation()) && Main.isInSpawn(event.getEntity().getLocation()))) {
			event.setCancelled(false);
			event.blockList().clear();
			return;
		}
	}
	
	private static final ItemStack swappedEdibleItem(Player player, EquipmentSlot hand) {
		ItemStack check = hand == EquipmentSlot.HAND ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
		if(check != null && check.getType().isEdible() && check.hasItemMeta() && check.getItemMeta().isUnbreakable()) {
			ItemStack replacement = new ItemStack(check);
			replacement.getItemMeta().setUnbreakable(false);
			if(hand == EquipmentSlot.HAND) {
				player.getInventory().setItemInMainHand(replacement);
			} else {
				player.getInventory().setItemInOffHand(replacement);
			}
			player.updateInventory();
			//updatePWPInventory(player);
			return check;
		}
		return null;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onVehicleEnterEvent(VehicleEnterEvent event) {//public static final void onEntityMountEvent(EntityMountEvent event) {//XXX Spigot Event!
		Player player = event.getEntered() instanceof Player ? (Player) event.getEntered() : null;//event.getEntity() instanceof Player ? (Player) event.getEntity() : null;
		//Entity vehicle = event.getVehicle();//Entity mount = event.getMount();
		if(player == null) {
			return;
		}
		Island island = Island.getIslandNearest(player.getLocation());
		if(island != null && !island.isTrusted(player)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event) {
		//if(event.getPlayer().getWorld() != GeneratorMain.getSkyworld() && event.getPlayer().getWorld() != GeneratorMain.getSkyworldNether()) {
		//	return;
		//}
		final Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		Island island = Island.getIslandNearest(player.getLocation());
		if(island != null && !island.isTrusted(player)) {
			event.setCancelled(true);
			return;
		}
		final EquipmentSlot hand = event.getHand();
		ItemStack similar = hand == EquipmentSlot.HAND ? new ItemStack(player.getInventory().getItemInMainHand()) : (hand == EquipmentSlot.OFF_HAND ? new ItemStack(player.getInventory().getItemInOffHand()) : null);
		if(!event.isCancelled() && similar != null && entity instanceof Animals) {
			final ItemStack swapped = swappedEdibleItem(player, hand);
			if(swapped != null) {
				Main.scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
					if(hand == EquipmentSlot.HAND) {
						player.getInventory().setItemInMainHand(swapped);
					} else {
						player.getInventory().setItemInOffHand(swapped);
					}
					player.updateInventory();
					//updatePWPInventory(player);
				});
			} else {
				if(hand == EquipmentSlot.HAND) {
					Main.scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
						ItemStack check = player.getInventory().getItemInMainHand();
						if(check == null || check.getAmount() == 0 || check.getType() == Material.AIR) {
							int i = 0;
							int remaining = 0;
							ItemStack set = null;
							for(ItemStack item1 : player.getInventory().getContents()) {
								if(item1 == null || item1.getType() == Material.AIR) {
									i++;
									continue;
								}
								if(isSameType(item1, similar)) {
									player.getInventory().setItem(i, new ItemStack(Material.AIR));
									player.getInventory().setItemInMainHand(item1);
									set = item1;
									break;
								}
								i++;
							}
							if(set != null) {
								String title = similar.hasItemMeta() ? similar.getItemMeta().getDisplayName() : Main.capitalizeFirstLetterOfEachWord(similar.getType().name().toLowerCase(), '_', ' ');
								for(ItemStack item2 : player.getInventory().getContents()) {
									if(item2 == null || item2.getType() == Material.AIR) {
										continue;
									}
									if(isSameType(item2, similar)) {
										remaining += item2.getAmount();
									}
								}
								player.sendTitle("", ChatColor.GRAY + "You have " + ChatColor.GOLD + Integer.toString(remaining, 10) + ChatColor.GRAY + " of " + title + ChatColor.RESET + ChatColor.GRAY + " remaining.", 10, 70, 20);
							}
						}
					});
				} else if(hand == EquipmentSlot.OFF_HAND) {
					Main.scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
						ItemStack check = player.getInventory().getItemInOffHand();
						if(check == null || check.getAmount() == 0 || check.getType() == Material.AIR) {
							int i = 0;
							int remaining = 0;
							ItemStack set = null;
							for(ItemStack item1 : player.getInventory().getContents()) {
								if(item1 == null || item1.getType() == Material.AIR) {
									i++;
									continue;
								}
								if(isSameType(item1, similar)) {
									player.getInventory().setItem(i, new ItemStack(Material.AIR));
									player.getInventory().setItemInOffHand(item1);
									set = item1;
									break;
								}
								i++;
							}
							if(set != null) {
								String title = similar.hasItemMeta() ? similar.getItemMeta().getDisplayName() : Main.capitalizeFirstLetterOfEachWord(similar.getType().name().toLowerCase(), '_', ' ');
								for(ItemStack item2 : player.getInventory().getContents()) {
									if(item2 == null || item2.getType() == Material.AIR) {
										continue;
									}
									if(isSameType(item2, similar)) {
										remaining += item2.getAmount();
									}
								}
								player.sendTitle("", ChatColor.GRAY + "You have " + ChatColor.GOLD + Integer.toString(remaining, 10) + ChatColor.GRAY + " of " + title + ChatColor.RESET + ChatColor.GRAY + " remaining.", 10, 70, 20);
							}
						}
					});
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onBlockPlaceEvent(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();
		Island island = Island.getIslandContaining(location);
		if(island == null || (!island.isMember(player))) {
			event.setCancelled(!player.hasPermission("skyblock.admin"));
			if(event.isCancelled()) {
				player.sendMessage(ChatColor.RED.toString().concat("You do not have permission to edit blocks outside of your island."));
				return;
			}
			if(location.getWorld() == GeneratorMain.getSkyworldTheEnd()) {
				event.setCancelled(isInSpawn(location));
			}
		}
		if(!event.isCancelled()) {
			ItemStack similar = new ItemStack(event.getItemInHand());
			EquipmentSlot hand = event.getHand();
			if(hand == EquipmentSlot.HAND) {
				Main.scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
					ItemStack check = player.getInventory().getItemInMainHand();
					if(check == null || check.getAmount() == 0 || check.getType() == Material.AIR) {
						int i = 0;
						int remaining = 0;
						ItemStack set = null;
						for(ItemStack item1 : player.getInventory().getContents()) {
							if(item1 == null || item1.getType() == Material.AIR) {
								i++;
								continue;
							}
							if(isSameType(item1, similar)) {
								player.getInventory().setItem(i, new ItemStack(Material.AIR));
								player.getInventory().setItemInMainHand(item1);
								set = item1;
								break;
							}
							i++;
						}
						if(set != null) {
							String title = similar.hasItemMeta() ? similar.getItemMeta().getDisplayName() : Main.capitalizeFirstLetterOfEachWord(similar.getType().name().toLowerCase(), '_', ' ');
							for(ItemStack item2 : player.getInventory().getContents()) {
								if(item2 == null || item2.getType() == Material.AIR) {
									continue;
								}
								if(isSameType(item2, similar)) {
									remaining += item2.getAmount();
								}
							}
							player.sendTitle("", ChatColor.GRAY + "You have " + ChatColor.GOLD + Integer.toString(remaining, 10) + ChatColor.GRAY + " of " + title + ChatColor.RESET + ChatColor.GRAY + " remaining.", 10, 70, 20);
						}
					}
				});
			} else if(hand == EquipmentSlot.OFF_HAND) {
				Main.scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
					ItemStack check = player.getInventory().getItemInOffHand();
					if(check == null || check.getAmount() == 0 || check.getType() == Material.AIR) {
						int i = 0;
						int remaining = 0;
						ItemStack set = null;
						for(ItemStack item1 : player.getInventory().getContents()) {
							if(item1 == null || item1.getType() == Material.AIR) {
								i++;
								continue;
							}
							if(isSameType(item1, similar)) {
								player.getInventory().setItem(i, new ItemStack(Material.AIR));
								player.getInventory().setItemInOffHand(item1);
								set = item1;
								break;
							}
							i++;
						}
						if(set != null) {
							String title = similar.hasItemMeta() ? similar.getItemMeta().getDisplayName() : Main.capitalizeFirstLetterOfEachWord(similar.getType().name().toLowerCase(), '_', ' ');
							for(ItemStack item2 : player.getInventory().getContents()) {
								if(item2 == null || item2.getType() == Material.AIR) {
									continue;
								}
								if(isSameType(item2, similar)) {
									remaining += item2.getAmount();
								}
							}
							player.sendTitle("", ChatColor.GRAY + "You have " + ChatColor.GOLD + Integer.toString(remaining, 10) + ChatColor.GRAY + " of " + title + ChatColor.RESET + ChatColor.GRAY + " remaining.", 10, 70, 20);
						}
					}
				});
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public static final void onPlayerItemBreakEventMonitor(PlayerItemBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getBrokenItem();
		if(item != null && item.getType() != Material.AIR) {
			ItemStack main = player.getInventory().getItemInMainHand();
			ItemStack off = player.getInventory().getItemInOffHand();
			EquipmentSlot h = EquipmentSlot.HAND;
			if(off == item) {
				h = EquipmentSlot.OFF_HAND;
			}
			if(main == item) {
				h = EquipmentSlot.HAND;
			}
			final EquipmentSlot hand = h;
			final ItemStack similar = new ItemStack(item);
			Main.scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
				int i = 0;
				boolean replaced = false;
				int remaining = -1;
				for(ItemStack item1 : player.getInventory().getContents()) {
					if(item1 == null || item1.getType() == Material.AIR) {
						i++;
						continue;
					}
					if(item1.getType() == similar.getType()) {
						player.getInventory().setItem(i, new ItemStack(Material.AIR));
						if(hand == EquipmentSlot.HAND) {
							player.getInventory().setItemInMainHand(item1);
						} else if(hand == EquipmentSlot.OFF_HAND) {
							player.getInventory().setItemInOffHand(item1);
						} else {
							player.getInventory().setItem(i, item1);
						}
						break;
					}
					i++;
				}
				if(replaced) {
					String title = similar.hasItemMeta() ? similar.getItemMeta().getDisplayName() : Main.capitalizeFirstLetterOfEachWord(similar.getType().name().toLowerCase(), '_', ' ');
					for(ItemStack item2 : player.getInventory().getContents()) {
						if(item2 == null || item2.getType() == Material.AIR) {
							continue;
						}
						if(item2.getType() == similar.getType()) {
							remaining++;
							break;
						}
					}
					player.sendTitle("", ChatColor.GRAY + "You have " + ChatColor.GOLD + Integer.toString(remaining, 10) + ChatColor.GRAY + " of " + title + ChatColor.RESET + ChatColor.GRAY + " remaining.", 10, 70, 20);
				}
				player.updateInventory();
				//updatePWPInventory(player);
			});
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if(item != null && item.getType() != Material.AIR) {
			ItemStack main = player.getInventory().getItemInMainHand();
			ItemStack off = player.getInventory().getItemInOffHand();
			EquipmentSlot h = EquipmentSlot.HAND;
			if(isSameType(off, item)) {
				h = EquipmentSlot.OFF_HAND;
			}
			if(isSameType(main, item)) {
				h = EquipmentSlot.HAND;
			}
			final EquipmentSlot hand = h;
			final ItemStack similar = new ItemStack(item);
			Main.scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
				ItemStack check;
				switch(hand) {
				case OFF_HAND:
					check = player.getInventory().getItemInOffHand();
					break;
				//$CASES-OMITTED$
				default:
				case HAND:
					check = player.getInventory().getItemInMainHand();
					break;
				}
				if(check == null || check.getType() == Material.AIR) {
					int i = 0;
					boolean replaced = false;
					int remaining = 0;
					for(ItemStack item1 : player.getInventory().getContents()) {
						if(item1 == null || item1.getType() == Material.AIR) {
							i++;
							continue;
						}
						if(item1.getType() == similar.getType()) {
							player.getInventory().setItem(i, new ItemStack(Material.AIR));
							if(hand == EquipmentSlot.HAND) {
								player.getInventory().setItemInMainHand(item1);
							} else if(hand == EquipmentSlot.OFF_HAND) {
								player.getInventory().setItemInOffHand(item1);
							} else {
								player.getInventory().setItem(i, item1);
							}
							break;
						}
						i++;
					}
					if(replaced) {
						String title = similar.hasItemMeta() ? similar.getItemMeta().getDisplayName() : Main.capitalizeFirstLetterOfEachWord(similar.getType().name().toLowerCase(), '_', ' ');
						for(ItemStack item2 : player.getInventory().getContents()) {
							if(item2 == null || item2.getType() == Material.AIR) {
								continue;
							}
							if(item2.getType() == similar.getType()) {
								remaining += item2.getAmount();
								break;
							}
						}
						player.sendTitle("", ChatColor.GRAY + "You have " + ChatColor.GOLD + Integer.toString(remaining, 10) + ChatColor.GRAY + " of " + title + ChatColor.RESET + ChatColor.GRAY + " remaining.", 10, 70, 20);
					}
					player.updateInventory();
					//updatePWPInventory(player);
				}
			});
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		if(event.isCancelled()) {
			return;
		}
		if(Island.isInSkyworld(event.getBlock().getLocation()) && (event.getBlock().getWorld() == GeneratorMain.getSkyworldTheEnd() ? isInSpawn(event.getBlock().getLocation()) : true) && Island.getIslandContaining(event.getBlock().getLocation()) == null) {
			event.setCancelled(true);
			return;
		}
		if(event.getEntity().getType() == EntityType.ENDERMAN && event.getBlock().getWorld() == GeneratorMain.getSkyworld()) {
			event.setCancelled(true);
		}
	}
	
	public static final Collection<Player> getNearbyPlayers(Location location, double radius) {
		return getNearbyPlayers(location, radius, radius, radius);
	}
	
	public static final Collection<Player> getNearbyPlayers(Location location, double xRadius, double yRadius, double zRadius) {
		Collection<Player> list = new ArrayList<>();
		for(Entity entity : location.getWorld().getNearbyEntities(location, xRadius, yRadius, zRadius)) {
			if(entity instanceof Player) {
				list.add((Player) entity);
			}
		}
		return list;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onEntityCombustEvent(EntityCombustEvent event) {
		if(!Island.isInSkyworld(event.getEntity().getLocation())) {
			return;
		}
		if(event.getEntity() instanceof Item) {
			Item item = (Item) event.getEntity();
			Island island = Island.getIslandContaining(item.getLocation());
			if(island != null) {
				Player closestMember = null;
				for(Player player : getNearbyPlayers(item.getLocation(), 16.0)) {
					if(island.isMember(player)) {
						if(closestMember == null) {
							closestMember = player;
							continue;
						}
						if(player.getLocation().distance(item.getLocation()) < closestMember.getLocation().distance(item.getLocation())) {
							closestMember = player;
						}
					}
				}
				if(closestMember != null) {
					event.setCancelled(true);
					ItemStack recovered = new ItemStack(item.getItemStack());
					//item.setItemStack(new ItemStack(Material.AIR)); //Throws "java.lang.IllegalArgumentException: Cannot drop air" as of 1.13+
					item.teleport(new Location(item.getWorld(), 0, -64, 0));
					item.remove();
					HashMap<Integer, ItemStack> leftovers = closestMember.getInventory().addItem(recovered);
					for(ItemStack leftover : leftovers.values()) {
						closestMember.getWorld().dropItem(closestMember.getLocation(), leftover);
					}
				} else {
					//server.getPlayer("Brian_Entei").sendMessage("Closest member == null");
				}
			} else {
				//server.getPlayer("Brian_Entei").sendMessage("Island == null");
			}
		} else {
			//server.getPlayer("Brian_Entei").sendMessage("event.getEntity() is not instanceof org.bukkit.entity.Item: " + event.getEntity().getClass().getName());
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onEntityPortalEvent(EntityPortalEvent event) {
		Entity entity = event.getEntity();
		Location portal = findNetherPortal(entity.getLocation(), 4);
		if(portal == null) {
			//Main.println("Cancelled 1");
			event.setCancelled(true);
			return;
		}
		if(entity.getWorld() == GeneratorMain.getSkyworld()) {
			//Main.println("Skyworld 1");
			Island island = Island.getIslandNearest(entity.getLocation());
			//Location loc = island == null ? Island.getIslandLocationNearest(player.getLocation()) : island.getLocation();
			int orientation = getPortalOrientation(portal);
			if(island != null) {
				//Main.println("Skyworld 2");
				island.setNetherPortal(portal);
				if(island.getNetherPortal() == null) {
					throw new IllegalStateException("Islands be not saving portals, eh?!");
				}
			}
			//int x = loc.getBlockX() * 8;
			//int z = loc.getBlockZ() * 8;
			Location portalLocation = island == null ? null : island.getSkyNetherPortal();
			if(portalLocation == null) {
				portalLocation = new Location(GeneratorMain.getSkyworldNether(), portal.getBlockX(), Math.min(portal.getBlockY(), 115), portal.getBlockZ());//final Location portalLocation = new Location(GeneratorMain.getSkyworldNether(), x, 64, z);
				Block block = portalLocation.getBlock();
				if(block.getType() != Material.NETHER_PORTAL) {
					if(orientation == 0) {
						portalLocation = createXAxisNetherPortal(portalLocation);
					} else if(orientation == 1) {
						portalLocation = createZAxisNetherPortal(portalLocation);
					}
				}
			}
			event.useTravelAgent(false);
			event.setTo(getNetherPortalEntryLocation(portalLocation));
			event.setCancelled(false);
		} else if(entity.getWorld() == GeneratorMain.getSkyworldNether()) {
			Island island = Island.getIslandNearest(new Location(GeneratorMain.getSkyworld(), entity.getLocation().getBlockX(), entity.getLocation().getBlockY(), entity.getLocation().getBlockZ()));
			if(island == null || island.getNetherPortal() == null) {
				if(entity instanceof Player) {
					island = Island.getMainIslandFor((Player) entity);
					if(island == null || island.getNetherPortal() == null) {
						event.setCancelled(true);
						return;
					}
				} else {
					event.setCancelled(true);
					return;
				}
			}
			//event.setCancelled(false);
			//event.useTravelAgent(false);
			//event.setTo(getNetherPortalEntryLocation(island.getNetherPortal()));
			final Island is = island;
			Main.scheduler.runTaskLater(getPlugin(), () -> {
				entity.teleport(getNetherPortalEntryLocation(is.getNetherPortal()));
			}, 2L);
		} else {
			//Main.println("Fail 1");
		}
	}
	
	//public static final ConcurrentHashMap<String, Long> whyDoINeedToDoThis = new ConcurrentHashMap<>();
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onPlayerPortalEvent(PlayerPortalEvent event) {
		EntityPortalEvent spoof = new EntityPortalEvent(event.getPlayer(), event.getFrom(), event.getTo(), event.getPortalTravelAgent()) {
			@Override
			public void setTo(Location to) {
				if(to == null) {
					Main.getPluginLogger().log(Level.WARNING, "Nether portal was set to null!", new NullPointerException());
				} else {
					super.setTo(to);
				}
			}
		};
		onEntityPortalEvent(spoof);
		if(spoof.getFrom() != null) {//Weird that I have to check for null for these...
			event.setFrom(spoof.getFrom());
		}
		if(spoof.getTo() != null) {//Weird that I have to check for null for these...
			event.setTo(spoof.getTo());
		}
		event.setPortalTravelAgent(event.getPortalTravelAgent());
		event.useTravelAgent(spoof.useTravelAgent());
		event.setCancelled(spoof.isCancelled());
	}
	
	/** @param portal The nether portal
	 * @param yaw
	 * @return The given nether portal's entryway(the space in front of the
	 *         portal blocks), or the given portal if the entryway is blocked */
	public static final Location getNetherPortalEntryLocation(Location portal) {
		portal = getNetherPortal(portal, true);
		if(portal == null) {
			throw new IllegalArgumentException("Given location was not a nether portal! (It was null!)");
		}
		Block check = portal.getBlock().getRelative(-1, 0, 0);
		if(check != null && check.getType() == Material.AIR) {
			//Main.println("x-1");
			portal = check.getLocation();
			portal.setYaw(0);
			portal.setPitch(0);
		} else {
			check = portal.getBlock().getRelative(1, 0, 0);
			if(check != null && check.getType() == Material.AIR) {
				//Main.println("x+1");
				portal = check.getLocation();
				portal.setYaw(180);
				portal.setPitch(0);
				portal.setZ(portal.getZ() + 0.5);
			} else {
				check = portal.getBlock().getRelative(0, 0, -1);
				if(check != null && check.getType() == Material.AIR) {
					//Main.println("z-1");
					portal = check.getLocation();
					portal.setYaw(90);
					portal.setPitch(0);
				} else {
					check = portal.getBlock().getRelative(0, 0, 1);
					if(check != null && check.getType() == Material.AIR) {
						//Main.println("z+1");
						portal = check.getLocation();
						portal.setYaw(270);
						portal.setPitch(0);
					} else {
						//Main.println("erm.");
						//Erm. Do nothing and let the player spawn in the portal blocks.
						portal.setYaw(getPortalOrientation(portal) == 0 ? 0 : 180);
						portal.setPitch(50);
					}
				}
			}
		}
		return portal;
	}
	
	/** @param location The location to verify
	 * @return The location of a nether portal if the given location represents
	 *         one, or <b><code>null</code></b> */
	public static final Location getNetherPortal(Location location, boolean logIfNull) {
		return location == null ? null : getNetherPortal(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), logIfNull);
	}
	
	/** @param world The world
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @return The location of a nether portal if the given location represents
	 *         one, or <b><code>null</code></b> */
	public static final Location getNetherPortal(World world, int x, int y, int z, boolean logIfNull) {
		Block block = world.getBlockAt(x, y, z);
		if(block != null && block.getType() == Material.NETHER_PORTAL) {
			Block obsidian = block.getRelative(0, -1, 0);
			if(obsidian != null && obsidian.getType() == Material.OBSIDIAN) {
				boolean portalBlocksPresent = true;
				for(int i = 1; i < 3; i++) {
					Block portal = block.getRelative(0, i, 0);
					portalBlocksPresent &= portal != null && portal.getType() == Material.NETHER_PORTAL;
				}
				if(portalBlocksPresent) {
					Location found = new Location(world, x, y, z);
					Block check = found.getBlock().getRelative(-1, 0, 0);
					if(check != null && check.getType() == Material.NETHER_PORTAL) {
						return check.getLocation();
					}
					check = found.getBlock().getRelative(0, 0, -1);
					if(check != null && check.getType() == Material.NETHER_PORTAL) {
						return check.getLocation();
					}
					return found;
				}
			}
		}
		if(logIfNull) {
			Main.getPluginLogger().warning("Error: getNetherPortal(world=".concat(world.getName()).concat(", x=").concat(Integer.toString(x)).concat(", y=").concat(Integer.toString(y)).concat(", z=").concat(Integer.toString(z)).concat(") is returning null!"));
		}
		return null;
	}
	
	/** @param world The world to search in
	 * @param bounds The bounds to limit the search to
	 * @return The nether portal location if found, or <b><code>null</code></b>
	 *         otherwise */
	public static final Location findNetherPortal(World world, int[] bounds) {
		for(int x = bounds[0]; x <= bounds[2]; x++) {
			for(int y = 0; y < world.getMaxHeight(); y++) {
				for(int z = bounds[1]; z <= bounds[3]; z++) {
					Location found = getNetherPortal(world, x, y, z, false);
					if(found != null) {
						return found;
					}
				}
			}
		}
		return null;
	}
	
	/** @param location The location to start the search from
	 * @param searchRadius The radius of the sphere to limit the search to
	 * @return The nether portal location if found, or <b><code>null</code></b>
	 *         otherwise */
	public static final Location findNetherPortal(Location location, int searchRadius) {
		World world = location.getWorld();
		for(int x = location.getBlockX() - searchRadius; x <= location.getBlockX() + searchRadius; x++) {
			for(int y = location.getBlockY() - searchRadius; y <= location.getBlockY() + searchRadius; y++) {
				for(int z = location.getBlockZ() - searchRadius; z <= location.getBlockZ() + searchRadius; z++) {
					Location found = getNetherPortal(world, x, y, z, false);
					if(found != null) {
						return found;
					}
				}
			}
		}
		return null;
	}
	
	/** @param portalLocation The location of the portal
	 * @return 0 for x-axis aligned, 1 for z-axis aligned, or -1 for no portal
	 *         found/indeterminate. */
	public static final int getPortalOrientation(Location portalLocation) {
		portalLocation = getNetherPortal(portalLocation, true);
		if(portalLocation == null) {
			return -1;
		}
		Block portal = portalLocation.getBlock();
		Block checkX = portal.getRelative(1, 0, 0);
		Block checkZ = portal.getRelative(0, 0, 1);
		return checkX != null && checkX.getType() == Material.NETHER_PORTAL ? 0 : (checkZ.getType() == Material.NETHER_PORTAL ? 1 : -1);
	}
	
	/** @param portalLocation The location to create an x-axis aligned nether
	 *            portal at
	 * @return The portal's location, if successful */
	public static final Location createXAxisNetherPortal(Location portalLocation) {
		//Main.println("Create X");
		World world = portalLocation.getWorld();
		int x = portalLocation.getBlockX();
		int y = portalLocation.getBlockY();
		int z = portalLocation.getBlockZ();
		for(int i = -1; i < 2; i++) {
			if(i == 0) {
				world.getBlockAt(x - 1, y - 1, z).setType(Material.OBSIDIAN, false);
				world.getBlockAt(x, y - 1, z).setType(Material.OBSIDIAN, false);
				world.getBlockAt(x + 1, y - 1, z).setType(Material.OBSIDIAN, false);
				world.getBlockAt(x + 2, y - 1, z).setType(Material.OBSIDIAN, false);
			}
			world.getBlockAt(x, y + 3, z + i).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + 1, y + 3, z + i).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x - 1, y, z + i).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x - 1, y + 1, z + i).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x - 1, y + 2, z + i).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x - 1, y + 3, z + i).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + 2, y, z + i).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + 2, y + 1, z + i).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + 2, y + 2, z + i).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + 2, y + 3, z + i).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x, y, z + i).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
			world.getBlockAt(x + 1, y, z + i).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
			world.getBlockAt(x, y + 1, z + i).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
			world.getBlockAt(x + 1, y + 1, z + i).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
			world.getBlockAt(x, y + 2, z + i).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
			world.getBlockAt(x + 1, y + 2, z + i).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
		}
		Block check = world.getBlockAt(x, y - 1, z - 1);
		if(check != null && check.getType() == Material.AIR) {
			check.setType(Material.COBBLESTONE, true);
		}
		check = world.getBlockAt(x, y - 1, z + 1);
		if(check != null && check.getType() == Material.AIR) {
			check.setType(Material.COBBLESTONE, true);
		}
		check = world.getBlockAt(x + 1, y - 1, z - 1);
		if(check != null && check.getType() == Material.AIR) {
			check.setType(Material.COBBLESTONE, true);
		}
		check = world.getBlockAt(x + 1, y - 1, z + 1);
		if(check != null && check.getType() == Material.AIR) {
			check.setType(Material.COBBLESTONE, true);
		}
		return getNetherPortal(portalLocation, true);
	}
	
	/** @param portalLocation The location to create a z-axis aligned nether
	 *            portal at
	 * @return The portal's location, if successful */
	public static final Location createZAxisNetherPortal(Location portalLocation) {
		//Main.println("Create Z");
		World world = portalLocation.getWorld();
		int x = portalLocation.getBlockX();
		int y = portalLocation.getBlockY();
		int z = portalLocation.getBlockZ();
		for(int i = -1; i < 2; i++) {
			if(i == 0) {
				world.getBlockAt(x + i, y - 1, z - 1).setType(Material.OBSIDIAN, false);
				world.getBlockAt(x + i, y - 1, z).setType(Material.OBSIDIAN, false);
				world.getBlockAt(x + i, y - 1, z + 1).setType(Material.OBSIDIAN, false);
				world.getBlockAt(x + i, y - 1, z + 2).setType(Material.OBSIDIAN, false);
			}
			world.getBlockAt(x + i, y + 3, z).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + i, y + 3, z + 1).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + i, y, z - 1).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + i, y + 1, z - 1).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + i, y + 2, z - 1).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + i, y + 3, z - 1).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + i, y, z + 2).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + i, y + 1, z + 2).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + i, y + 2, z + 2).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			world.getBlockAt(x + i, y + 3, z + 2).setType(i != 0 ? Material.AIR : Material.OBSIDIAN, false);
			
			world.getBlockAt(x + i, y, z).setType(Material.AIR, true);
			world.getBlockAt(x + i, y, z + 1).setType(Material.AIR, true);
			world.getBlockAt(x + i, y + 1, z).setType(Material.AIR, true);
			world.getBlockAt(x + i, y + 1, z + 1).setType(Material.AIR, true);
			world.getBlockAt(x + i, y + 2, z).setType(Material.AIR, true);
			world.getBlockAt(x + i, y + 2, z + 1).setType(Material.AIR, true);
			
			//This works in 1.12.2
			//world.getBlockAt(x + i, y, z).setType(i != 0 ? Material.AIR : Material.FIRE, true);
			
			world.getBlockAt(x + i, y, z).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
			setAxisForNetherPortal(world, x + i, y, z, Axis.Z, false);
			world.getBlockAt(x + i, y, z + 1).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
			setAxisForNetherPortal(world, x + i, y, z + 1, Axis.Z, false);
			world.getBlockAt(x + i, y + 1, z).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
			setAxisForNetherPortal(world, x + i, y + 1, z, Axis.Z, false);
			world.getBlockAt(x + i, y + 1, z + 1).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
			setAxisForNetherPortal(world, x + i, y + 1, z + 1, Axis.Z, false);
			world.getBlockAt(x + i, y + 2, z).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
			setAxisForNetherPortal(world, x + i, y + 2, z, Axis.Z, false);
			world.getBlockAt(x + i, y + 2, z + 1).setType(i != 0 ? Material.AIR : Material.NETHER_PORTAL, false);
			setAxisForNetherPortal(world, x + i, y + 2, z + 1, Axis.Z, false);
			
			//world.getBlockAt(x + i, y - 1, z).setType(Material.OBSIDIAN, true);
		}
		Block check = world.getBlockAt(x - 1, y - 1, z);
		if(check != null && check.getType() == Material.AIR) {
			check.setType(Material.COBBLESTONE, true);
		}
		check = world.getBlockAt(x + 1, y - 1, z);
		if(check != null && check.getType() == Material.AIR) {
			check.setType(Material.COBBLESTONE, true);
		}
		check = world.getBlockAt(x - 1, y - 1, z + 1);
		if(check != null && check.getType() == Material.AIR) {
			check.setType(Material.COBBLESTONE, true);
		}
		check = world.getBlockAt(x + 1, y - 1, z + 1);
		if(check != null && check.getType() == Material.AIR) {
			check.setType(Material.COBBLESTONE, true);
		}
		return getNetherPortal(portalLocation, true);
	}
	
	private static final void setAxisForNetherPortal(World world, int x, int y, int z, Axis axis) {
		setAxisForNetherPortal(world, x, y, z, axis, true);
	}
	
	private static final void setAxisForNetherPortal(World world, int x, int y, int z, Axis axis, boolean applyPhysics) {
		Block block = world.getBlockAt(x, y, z);
		if(block.getType() == Material.NETHER_PORTAL) {
			CraftPortal portal = new CraftPortal(((CraftBlockState) block.getState()).getHandle());
			portal.setAxis(axis);
			block.setBlockData(portal, applyPhysics);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public static final void onPlayerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		playerDeathLocations.put(player.getUniqueId(), player.getLocation());
		DamageEventListeners.forgivePlayer(player);
		Island island = Island.getMainIslandFor(player);
		if(island != null) {
			event.setKeepLevel(true);
			event.setDroppedExp(0);
			if(player.getBedSpawnLocation() == null) {
				player.setBedSpawnLocation(island.getHomeFor(player.getUniqueId()), true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if(Island.isInSkyworld(playerDeathLocations.get(player.getUniqueId()))) {
			Island island = Island.getMainIslandFor(player);
			event.setRespawnLocation(island == null || !Main.isSafeForTeleporting(island.getHomeFor(player.getUniqueId())) ? (!Main.isSafeForTeleporting(event.getRespawnLocation()) ? (island == null || !Main.isSafeForTeleporting(island.getSpawnLocation()) ? GeneratorMain.getSkyworldSpawnLocation() : island.getSpawnLocation()) : event.getRespawnLocation()) : island.getHomeFor(player.getUniqueId()));
		} else {
			Location lastDeath = getLastDeathLocation(player);
			World world = lastDeath == null ? Main.server.getWorld("world") : lastDeath.getWorld();
			if(Island.isInSkyworld(event.getRespawnLocation())) {
				if(world != null) {
					event.setRespawnLocation(world.getSpawnLocation().add(0.5, 0x0.0p0, 0.5));
				} else {
					event.setRespawnLocation(GeneratorMain.getSkyworldSpawnLocation());
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public static final void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		if(event.getPlayer().getGameMode() == GameMode.SPECTATOR && event.getNewGameMode() != GameMode.SPECTATOR) {
			if(getTimeSinceLastWorldChange(player) <= 5000L) {
				event.setCancelled(true);
				return;
			}
		}
		if(event.getNewGameMode() != GameMode.SURVIVAL) {
			if(Island.isInSkyworld(player) && !(player.hasPermission("skyblock.gamemode." + event.getNewGameMode().name().toLowerCase()) || player.isOp())) {
				event.setCancelled(true);
				player.setGameMode(GameMode.SURVIVAL);
			}
		} else {
			event.setCancelled(false);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public static final void onPlayerGameModeChangeEventMonitor(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		if(Island.isInSkyworld(player)) {
			if(event.getNewGameMode() == GameMode.CREATIVE && !event.isCancelled()) {
				player.sendTitle(ChatColor.DARK_GRAY + "Creative in skyworld?", ChatColor.YELLOW + "Don't take out unobtainable blocks!", 20, 140, 40);
				return;
			}
		}
	}
	
	/** @param player The player to check
	 * @return Whether or not the given player is visiting the skyworld's spawn
	 *         area */
	public static final boolean isInSpawn(Player player) {
		for(UUID visitor : spawnVisitingPlayers) {
			OfflinePlayer check = Main.server.getOfflinePlayer(visitor);
			if(check == null || !check.isOnline()) {
				spawnVisitingPlayers.remove(visitor);
				continue;
			}
			if(check.getUniqueId().toString().equals(player.getUniqueId().toString())) {
				return true;
			}
		}
		return false;
	}
	
	/** @param visitor The player to mark as visiting the skyworld's spawn
	 *            area */
	public static final void addVisitor(Player visitor) {
		removeVisitor(visitor);
		spawnVisitingPlayers.add(visitor.getUniqueId());
	}
	
	/** @param player The player to mark as not visiting the skyworld's spawn
	 *            area */
	public static final void removeVisitor(Player player) {
		for(UUID visitor : spawnVisitingPlayers) {
			if(visitor.toString().equals(player.getUniqueId().toString())) {
				spawnVisitingPlayers.remove(visitor);
			}
		}
	}
	
	protected static final ConcurrentHashMap<String, Long> lastChangedWorldTimes = new ConcurrentHashMap<>();
	
	public static final long getTimeSinceLastWorldChange(OfflinePlayer player) {
		if(player == null) {
			return Long.MAX_VALUE;
		}
		Long lastChangedWorldTime = lastChangedWorldTimes.get(player.getUniqueId().toString());
		return lastChangedWorldTime == null ? Long.MAX_VALUE : System.currentTimeMillis() - lastChangedWorldTime.longValue();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public static final void onPlayerChangedWorldEventMonitor(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		Island island = Island.getMainIslandFor(player);
		if(island != null) {
			island.wipeMembersInventoriesIfRequired();
		}
		island = Island.getIslandContaining(player.getLocation());
		if(island != null) {
			Island leaving = Island.getIslandPlayerIsVisiting(player);
			if(leaving != null) {
				Environment environment = player.getWorld().getEnvironment();
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou are leaving &f" + leaving.getOwnerName() + "&a's " + (environment == Environment.NORMAL ? "island" : (environment == Environment.NETHER ? "nether area" : "end area")) + "."));
			}
			Island.removeVisitor(player);
			island.addVisitor(player);
			Environment environment = player.getWorld().getEnvironment();
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou are entering &f" + island.getOwnerName() + "&a's " + (environment == Environment.NORMAL ? "island" : (environment == Environment.NETHER ? "nether area" : "end area")) + "."));
		} else if(Island.isWithinSpawnArea(player.getLocation()) && !isInSpawn(player)) {
			addVisitor(player);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou are entering the spawn area."));
		} else if(!Island.isWithinSpawnArea(player.getLocation()) && isInSpawn(player)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou are leaving the spawn area."));
			removeVisitor(player);
		}
		lastChangedWorldTimes.put(player.getUniqueId().toString(), Long.valueOf(System.currentTimeMillis()));
		Main.scheduler.runTaskLater(getPlugin(), () -> {//If you change the gamemode right away, there is a chance that PerWorldInventories will glitch out and update your inventory to the wrong gamemode. Say, creative inventory for survival mode.
			if(player.getGameMode() != GameMode.SURVIVAL && Island.isInSkyworld(player) && !(player.hasPermission("skyblock.gamemode." + player.getGameMode().name().toLowerCase()) || player.isOp())) {
				player.setGameMode(GameMode.SURVIVAL);
			}
		}, 2L);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onPlayerJoinEvent(PlayerJoinEvent event) {
		Island.updateAllIslands();
		Player player = event.getPlayer();
		final Island island = Island.getMainIslandFor(player);
		if(island != null) {
			Main.scheduler.runTaskLater(getPlugin(), () -> {
				if(island.hasAnyJoinRequests()) {
					List<OfflinePlayer> requesters = island.getOnlineJoinRequests();
					if(!requesters.isEmpty()) {
						player.sendMessage(ChatColor.BLUE + "[Island]: " + ChatColor.GREEN + "The following player(s) have active requests to join the island. To accept, type \"" + ChatColor.WHITE + "/island invite {name}" + ChatColor.GREEN + "\".");
						for(OfflinePlayer requester : requesters) {
							player.sendMessage(ChatColor.BLUE + "[Island]: " + ChatColor.WHITE + requester.getName());
						}
					}
				}
			}, 10L);
			island.wipeMembersInventoriesIfRequired();
		} else {
			for(Island check : Island.getAllIslands()) {
				if(check.hasInvitationFor(player)) {
					player.sendMessage(ChatColor.GREEN + "You've been invited to join " + ChatColor.WHITE + check.getOwnerName() + ChatColor.RESET + ChatColor.GREEN + "'s island! To accept, type \"" + ChatColor.WHITE + "/island join " + check.getOwnerNamePlain() + ChatColor.GREEN + "\".");
					player.sendMessage(ChatColor.GREEN + "To decline, type \"" + ChatColor.WHITE + "/island decline " + check.getOwnerNamePlain() + ChatColor.GREEN + "\".");
				}
			}
		}
		if(player.getGameMode() != GameMode.SURVIVAL && Island.isInSkyworld(player) && !(player.hasPermission("skyblock.gamemode." + player.getGameMode().name().toLowerCase()) || player.isOp())) {
			player.setGameMode(GameMode.SURVIVAL);
		}
		playerLoginTimes.put(player.getUniqueId().toString(), Long.valueOf(System.currentTimeMillis()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(Island.isSkyworld(player.getWorld())) {
			Island island = Island.getMainIslandFor(player);
			{
				Island check = Island.getIslandContaining(player.getLocation());
				if(check != null && check.isMember(player)) {
					island = check;
				}
			}
			if(island != null) {
				island.wipeMembersInventoriesIfRequired();
			}
			if(player.getGameMode() != GameMode.SURVIVAL && !(player.hasPermission("skyblock.gamemode." + player.getGameMode().name().toLowerCase()) || player.isOp())) {
				player.setGameMode(GameMode.SURVIVAL);
			}
			//XXX Disabled island mob GUI scoreboard
			/*if(island != null) {
				Scoreboard check = player.getScoreboard();
				if(!island.isPlayerOnIsland(player)) {
					if(check != null && check.getObjective("MobTracker") != null) {
						player.setScoreboard(Main.server.getScoreboardManager().getNewScoreboard());
					}
				} else {
					if(check == null || check.getObjective("MobTracker") == null) {
						island.updateScoreboardFor(player);
					}
				}
			}*/
			island = Island.getIslandContaining(event.getTo());
			if(island != null) {
				/*try {
					player.setAffectsSpawning(player.getLocation().getWorld() == GeneratorMain.getSkyworld() ? island.isTrusted(player) : true);
				} catch(NoSuchMethodError ignored) {
				}*/
				if(island.isLocked()) {
					if(!island.isTrusted(player) && !player.hasPermission("skyblock.dev")) {
						if(island.isOnIsland(event.getTo())) {
							event.setCancelled(true);
						}
						if(island.isOnIsland(player.getLocation())) {
							safeTeleport(player, GeneratorMain.getSkyworldSpawnLocation().add(0.5, 0, 0.5));
							//TODO make this teleport the player to the border outside of the island if borderSize > 0 instead of the spawn
							player.sendMessage(ChatColor.YELLOW + "The island you were just on is locked.");
						}
					}
				} else {
					if(!island.isVisiting(player)) {
						island.addVisitor(player);
						Environment environment = player.getWorld().getEnvironment();
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou are entering &f" + island.getOwnerName() + "&a's " + (environment == Environment.NORMAL ? "island" : (environment == Environment.NETHER ? "nether area" : "end area")) + "."));
					}
				}
			} else {
				/*try {
					player.setAffectsSpawning(player.getLocation().getWorld() == GeneratorMain.getSkyworld() ? Main.isInSpawn(player) : false);
				} catch(NoSuchMethodError ignored) {
				}*/
				Island leaving = Island.getIslandPlayerIsVisiting(player);
				if(leaving != null) {
					Environment environment = player.getWorld().getEnvironment();
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou are leaving &f" + leaving.getOwnerName() + "&a's " + (environment == Environment.NORMAL ? "island" : (environment == Environment.NETHER ? "nether area" : "end area")) + "."));
				}
				Island.removeVisitor(player);
			}
			if(Island.isWithinSpawnArea(player.getLocation()) && !isInSpawn(player)) {
				addVisitor(player);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You have entered the spawn area."));
			} else if(!Island.isWithinSpawnArea(player.getLocation()) && isInSpawn(player)) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5You have left the spawn area."));
				removeVisitor(player);
			}
		} else {
			Scoreboard check = player.getScoreboard();
			if(check != null && check.getObjective("MobTracker") != null) {
				player.setScoreboard(Main.server.getScoreboardManager().getNewScoreboard());
			}
			/*try {
				player.setAffectsSpawning(true);
			} catch(NoSuchMethodError ignored) {
			}*/
			Island.removeVisitor(player);
			removeVisitor(player);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onEntityTeleportEvent(EntityTeleportEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Tameable) {
			Tameable pet = (Tameable) entity;
			AnimalTamer tamer = pet.getOwner();
			if(tamer instanceof Player) {
				Player owner = (Player) tamer;
				Island destination = Island.getIslandContaining(event.getTo());
				event.setCancelled(destination == null || !destination.isTrusted(owner));
			} else {
				event.setCancelled(Island.isInSkyworld(entity.getLocation()));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Island island = Island.getMainIslandFor(player);
		if(island != null && !island.isTrusted(player)) {
			Block interacted = event.getClickedBlock();
			event.setCancelled(interacted == null || !(interacted.getType().name().endsWith("_DOOR")));
			return;
		}
		if(island != null && island.isPlayerOnIsland(player)) {
			if(event.getClickedBlock() != null && event.getClickedBlock().getType().name().endsWith("_BED")) {
				Location old = player.getBedSpawnLocation();
				player.setBedSpawnLocation(event.getClickedBlock().getLocation());
				if(old == null || old.distance(player.getBedSpawnLocation()) > 3) {
					player.sendMessage(ChatColor.GOLD.toString().concat("Bed spawn set!"));
				}
				return;
			}
			if(player.getInventory().getItemInMainHand().getType() == Material.BUCKET || player.getInventory().getItemInOffHand().getType() == Material.BUCKET) {
				Block block = event.getClickedBlock();
				if(block != null) {
					Location loc = block.getLocation();
					if(block.getType() == Material.OBSIDIAN) {
						int obsidianBlocksNearby = 0;
						int waterBlocksNearby = 0;
						for(int x = -2; x < 3; x++) {
							for(int y = -2; y < 3; y++) {
								for(int z = -2; z < 3; z++) {
									Block check = block.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
									if(check != null && check.getType() == Material.WATER) {
										waterBlocksNearby++;
									}
									if(x == 0 && y == 0 && z == 0) {
										continue;
									}
									if(check != null && check.getType() == Material.OBSIDIAN) {
										obsidianBlocksNearby++;
									}
								}
							}
						}
						if(obsidianBlocksNearby >= 4) {
							player.playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f);
							player.sendMessage(ChatColor.YELLOW + "Too many other obsidian blocks nearby to recapture lava. Try removing some first.");
						} else {
							if(player.getInventory().getItemInMainHand().getType() == Material.BUCKET) {
								if(event.getHand() == EquipmentSlot.OFF_HAND) {
									int amount = player.getInventory().getItemInMainHand().getAmount();
									if(amount - 1 <= 0) {
										player.getInventory().setItemInMainHand(new ItemStack(Material.LAVA_BUCKET, 1));
									} else {
										player.getInventory().setItemInMainHand(new ItemStack(Material.BUCKET, amount - 1));
										giveItemToPlayer(player, new ItemStack(Material.LAVA_BUCKET, 1));
									}
									block.setType(Material.AIR, true);
									player.playSound(loc, Sound.BLOCK_FIRE_AMBIENT, 1f, 1f);
									player.sendMessage(ChatColor.GREEN + "Successfully recaptured your lava." + (waterBlocksNearby > 0 ? " Try to be careful!" : ""));
								}
							} else if(player.getInventory().getItemInOffHand().getType() == Material.BUCKET) {
								if(event.getHand() == EquipmentSlot.HAND) {
									int amount = player.getInventory().getItemInOffHand().getAmount();
									if(amount - 1 <= 0) {
										player.getInventory().setItemInOffHand(new ItemStack(Material.LAVA_BUCKET, 1));
									} else {
										player.getInventory().setItemInOffHand(new ItemStack(Material.BUCKET, amount - 1));
										giveItemToPlayer(player, new ItemStack(Material.LAVA_BUCKET, 1));
									}
									block.setType(Material.AIR, true);
									player.playSound(loc, Sound.BLOCK_FIRE_AMBIENT, 1f, 1f);
									player.sendMessage(ChatColor.GREEN + "Successfully recaptured your lava." + (waterBlocksNearby > 0 ? " Try to be careful!" : ""));
								}
							} else {
								player.playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 1f);
								player.sendMessage(ChatColor.YELLOW + "Too many other obsidian blocks nearby to recapture lava. Try removing some first.");
								return;
							}
						}
					}
				}
			}
		}
		if(Island.isInSkyworld(player) && island != null) {
			island.wipeMembersInventoriesIfRequired();
		}
		island = Island.getIslandNearest(player.getLocation());
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK && (player.getLocation().getWorld() == GeneratorMain.getSkyworld() ? island != null && island.isPlayerOnIsland(player) : true)) {
			ItemStack item = event.getItem();
			if(item != null) {
				if(item.getType().name().endsWith("_SPAWN_EGG")) {
					if(item.hasItemMeta()) {
						if(item.getItemMeta().hasDisplayName()) {
							String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
							if(name.endsWith(" Spawner")) {
								String typeName = name.substring(0, name.length() - 8);
								EntityType type = EntityType.PIG;
								for(EntityType t : EntityType.values()) {
									if(t.name().equalsIgnoreCase(typeName.replace(" ", "_"))) {
										type = t;
										break;
									}
								}
								Block clicked = event.getClickedBlock();
								if(!(clicked.getState() instanceof InventoryHolder) || (clicked.getState() instanceof InventoryHolder && player.isSneaking())) {
									Block target = clicked.getRelative(event.getBlockFace());
									if(target.getType() == Material.AIR) {
										target.setType(Material.SPAWNER, false);
										CreatureSpawner spawner = (CreatureSpawner) target.getState();
										spawner.setSpawnedType(type);
										spawner.setSpawnCount(3);
										spawner.setRequiredPlayerRange(8);
										spawner.setMaxNearbyEntities(8);
										spawner.setMinSpawnDelay(20);//one second
										spawner.setMaxSpawnDelay(1200);//one minute
										spawner.setDelay(-1);
										spawner.update(true, true);
										event.setCancelled(true);
										if(player.getGameMode() != GameMode.CREATIVE) {
											if(event.getHand() == EquipmentSlot.HAND) {
												ItemStack hand = player.getInventory().getItemInMainHand();
												if(hand.getAmount() > 1) {
													hand.setAmount(hand.getAmount() - 1);
													player.getInventory().setItemInMainHand(hand);
												} else {
													player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
												}
											} else {
												ItemStack hand = player.getInventory().getItemInOffHand();
												if(hand.getAmount() > 1) {
													hand.setAmount(hand.getAmount() - 1);
													player.getInventory().setItemInOffHand(hand);
												} else {
													player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
												}
											}
											player.updateInventory();
											//updatePWPInventory(player);
										}
										return;
									}
								}
							}
						}
					}
				}
			}
		}
		if(event.getAction() == Action.PHYSICAL) {
			Block block = event.getClickedBlock();
			if(block != null) {
				Location location = block.getLocation();
				island = Island.getIslandContaining(location);
				if(island != null && !island.isMember(player)) {
					if(block.getType() == Material.FARMLAND) {
						event.setUseInteractedBlock(Result.DENY);
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		island = Island.getIslandNearest(player.getLocation());
		if(island != null && !island.isMember(player)) {
			if(event.getAction() == Action.LEFT_CLICK_BLOCK && player.isSneaking()) {
				//if(Main.pluginMgr.getPlugin("EnteisPermissions") != null) {
				event.setCancelled(!player.hasPermission("skyblock.admin"));
				if(event.isCancelled()) {
					return;
				}
				//}
			}
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Block clicked = event.getClickedBlock();
				if(clicked != null && clicked.getState() instanceof InventoryHolder) {
					if(!island.isTrusted(player)) {
						event.setCancelled(!player.hasPermission("skyblock.admin"));
						if(event.isCancelled()) {
							player.sendMessage(ChatColor.RED.toString().concat("You do not have permission to open containers outside of your island."));
							return;
						}
					}
				}
			}
		}
	}
	
	/*public static final void updatePWPInventory(Player player) {
		Main.scheduler.runTask(getPlugin(), () -> {
			updatePWPInventory(player, player.getWorld(), player.getGameMode());
		});
	}
	
	public static final void updatePWPInventory(Player player, World world, GameMode gameMode) {
		Plugin plugin = Main.pluginMgr.getPlugin("PerWorldInventory");
		if(plugin != null && plugin.isEnabled()) {
			me.gnat008.perworldinventory.PerWorldInventory pwi = (me.gnat008.perworldinventory.PerWorldInventory) plugin;
			me.gnat008.perworldinventory.api.PerWorldInventoryAPI api = pwi.getAPI();
			if(api != null) {
				me.gnat008.perworldinventory.groups.Group group = api.getGroupFromWorld(world.getName());
				if(group != null) {
					if(api.isPlayerCached(group, gameMode, player)) {
						me.gnat008.perworldinventory.data.players.PWIPlayer pwp = api.getCachedPlayer(group, player);
						if(pwp != null) {
							try {
								Field playerManagerField = getDeclaredField("playerManager", me.gnat008.perworldinventory.api.PerWorldInventoryAPI.class);
								playerManagerField.setAccessible(true);
								me.gnat008.perworldinventory.data.players.PWIPlayerManager manager = (me.gnat008.perworldinventory.data.players.PWIPlayerManager) playerManagerField.get(api);
								if(manager != null) {
									manager.updateCache(player, pwp);
								}
							} catch(SecurityException | NoSuchFieldException | IllegalAccessException ex) {
							}
						}
					}
				}
			}
		}
	}*/
	
	public static final void cleanOldBukkitPluginCommands() {
		for(Command cmd : Main.commandMap.getCommands()) {
			Plugin plugin = null;
			if(cmd instanceof PluginIdentifiableCommand) {
				plugin = ((PluginIdentifiableCommand) cmd).getPlugin();
			}
			if(plugin != null || (cmd instanceof PluginCommand)) {
				if(plugin == null || !Main.pluginMgr.isPluginEnabled(plugin)) {
					cmd.unregister(Main.commandMap);
				}
			}
		}
	}
	
	public static final boolean onOverriddenCommand(CommandSender sender, String command, String[] args) {
		PluginCommand checkCmd = Main.server.getPluginCommand(command);
		if(checkCmd != null && checkCmd.getPlugin() == Main.getPlugin()) {
			return Main.getPlugin().onCommand(sender, checkCmd, command, args);//Prevents other plugins from overriding this plugin's commands.
		}
		if(command.equalsIgnoreCase("opsudo") || command.equalsIgnoreCase("sudoop")) {
			if(Main.server.getPluginCommand("opsudo") != null) {
				return false;
			}
			if(!sender.hasPermission("rescudoplugin.opsudo")) {
				sender.sendMessage("Unknown command. Type \"/help\" for help.");
				return true;
			}
			if(args.length < 1) {
				sender.sendMessage(ChatColor.YELLOW.toString().concat("Usage: \"/").concat(command.toLowerCase()).concat(" <playerName> <command ...>\""));
				return true;
			}
			Player target = Main.server.getPlayer(args[0]);
			if(target == null) {
				sender.sendMessage(ChatColor.YELLOW.toString().concat("Player \"/").concat(ChatColor.WHITE.toString()).concat(args[0]).concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat("\" is not online or doesn't exist."));
				return true;
			}
			String cmd = "/";
			for(int i = 1; i < args.length; i++) {
				cmd = cmd.concat(i == 1 ? "" : " ").concat(args[i]);
			}
			boolean wasOp = target.isOp();
			target.setOp(true);
			Main.server.dispatchCommand(target, cmd.substring(1));
			target.setOp(wasOp);
			sender.sendMessage(ChatColor.GREEN.toString().concat("Made player ").concat(ChatColor.WHITE.toString()).concat(target.getDisplayName()).concat(ChatColor.RESET.toString()).concat(ChatColor.GREEN.toString()).concat(" run command \"").concat(ChatColor.WHITE.toString()).concat(cmd).concat(ChatColor.RESET.toString()).concat(ChatColor.GREEN.toString()).concat("\" with operator privileges."));
			return true;
		}
		return false;
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public static final void onServerCommandEvent(ServerCommandEvent event) {
		cleanOldBukkitPluginCommands();
		String command = event.getCommand();
		CommandSender sender = event.getSender();
		String[] args;
		{
			String[] split = command.split(Pattern.quote(" "));
			command = split[0];
			args = Arrays.copyOfRange(split, 1, split.length);
		}
		if(onOverriddenCommand(sender, command, args)) {
			event.setCancelled(true);
			return;
		}
		if(plugin.getCommand(command) == null && Main.server.getPluginCommand(command) == null) {
			if(plugin.onCommand(sender, null, command, args)) {
				event.setCancelled(true);
			}
		} else if(command.equalsIgnoreCase("save-all")) {
			Main.saveAll();
			GeneratorMain.saveAll();
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public static final void onPlayerCommandPreProcessEvent(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage().substring(1);
		CommandSender sender = event.getPlayer();
		String[] args;
		{
			String[] split = command.split(Pattern.quote(" "));
			command = split[0];
			args = Arrays.copyOfRange(split, 1, split.length);
		}
		if(onOverriddenCommand(sender, command, args)) {
			event.setCancelled(true);
			return;
		}
		if(plugin.getCommand(command) == null) {
			if(plugin.onCommand(sender, null, command, args)) {
				event.setCancelled(true);
			}
		}
	}
	
	/** @param x The x coordinate of the block whose chunk coordinates is
	 *            requested
	 * @param z The x coordinate of the block whose chunk coordinates is
	 *            requested
	 * @return The chunk coordinates */
	public static final int[] getWorldToChunkCoords(int x, int z) {
		if(x < 0 && z >= 0) {//X is negative
			return new int[] {(x / 16) - 1, z / 16};
		} else if(x >= 0 && z < 0) {//Z is negative
			return new int[] {x / 16, (z / 16) - 1};
		} else if(x < 0 && z < 0) {//X and Z are negative
			return new int[] {(x / 16) - 1, (z / 16) - 1};
		} else {//X and Z are positive, do things normally
			return new int[] {x / 16, z / 16};
		}
	}
	
	public final static Chunk getChunkAtWorldCoords(World world, int x, int z) {
		Chunk chunk;
		if(x < 0 && z >= 0) {//X is negative
			chunk = world.getChunkAt((x / 16) - 1, z / 16);
		} else if(x >= 0 && z < 0) {//Z is negative
			chunk = world.getChunkAt(x / 16, (z / 16) - 1);
		} else if(x < 0 && z < 0) {//X and Z are negative
			chunk = world.getChunkAt((x / 16) - 1, (z / 16) - 1);
		} else {//X and Z are positive, do things normally
			chunk = world.getChunkAt(x / 16, z / 16);
		}
		return chunk;
	}
	
	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return new SkyworldGenerator();
	}
	
	private static volatile boolean printedWTF = false;
	
	public static final Object getRegionManagerFor(World world) {
		if(getWorldEdit() != null && getWorldGuard() != null) {
			return _getRegionManagerFor(world);
		}
		return null;
	}
	
	private static final Object _getRegionManagerFor(World world) {
		if(getWorldGuard() == null) {
			return null;
		}
		if(getWorldEdit() == null) {
			return null;
		}
		try {
			//Older way was MUCH simpler >_>
			//com.sk89q.worldguard.bukkit.WorldGuardPlugin wg = com.sk89q.worldguard.bukkit.WorldGuardPlugin.inst();
			//return wg.getRegionManager(world);
			com.sk89q.worldguard.bukkit.WorldGuardPlugin.inst();
			com.sk89q.worldguard.protection.regions.RegionContainer container = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer();
			com.sk89q.worldedit.bukkit.BukkitWorld wrld = new com.sk89q.worldedit.bukkit.BukkitWorld(world);
			return container.get(wrld);
		} catch(NoClassDefFoundError ex) {
			ex.printStackTrace(System.err);
			System.err.flush();
			return null;
		}
	}
	
	public static final int[] getSpawnBounds() {
		return new int[] {-(GeneratorMain.getSpawnRegion() + 1), -(GeneratorMain.getSpawnRegion() + 1), GeneratorMain.getSpawnRegion() - 1, GeneratorMain.getSpawnRegion() - 1};
	}
	
	public static final boolean isInSpawn(Location location) {
		int[] bounds = getSpawnBounds();
		return location.getBlockX() <= bounds[0] && location.getBlockX() >= bounds[2] && location.getBlockZ() <= bounds[1] && location.getBlockZ() >= bounds[3];
	}
	
	private static final void updateSpawnRegionFor(World world) {
		if(world.getEnvironment() == Environment.NORMAL) {
			Island.setBiome(new int[] {-(GeneratorMain.getSpawnRegion() + 1), -(GeneratorMain.getSpawnRegion() + 1), GeneratorMain.getSpawnRegion() - 1, GeneratorMain.getSpawnRegion() - 1}, world, Biome.WARM_OCEAN);
		}
		if(getWorldEdit() != null && getWorldGuard() != null) {
			//_updateSpawnRegionFor(world);
		}
	}
	
	private static final void _updateSpawnRegionFor(World world) {
		if(getWorldGuard() == null) {
			return;
		}
		if(getWorldEdit() == null) {
			return;
		}
		try {
			try {
				com.sk89q.worldguard.protection.managers.RegionManager rm = (com.sk89q.worldguard.protection.managers.RegionManager) getRegionManagerFor(world);
				com.sk89q.worldguard.protection.regions.ProtectedRegion global = rm.getRegion(com.sk89q.worldguard.protection.regions.ProtectedRegion.GLOBAL_REGION);
				if(global == null) {
					//FAWE - new com.sk89q.worldedit.BlockVector(x, y, z);
					//WE - com.sk89q.worldedit.math.BlockVector3.at(x, y, z);
					global = new com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion(com.sk89q.worldguard.protection.regions.ProtectedRegion.GLOBAL_REGION, new com.sk89q.worldedit.BlockVector(-29999985, 0, -29999985), new com.sk89q.worldedit.BlockVector(29999984, world.getMaxHeight(), 29999984));
					rm.addRegion(global);
				}
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.BUILD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.DAMAGE_ANIMALS, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENDERDRAGON_BLOCK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.CHEST_ACCESS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.CHORUS_TELEPORT, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.CREEPER_EXPLOSION, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.DAMAGE_ANIMALS, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.DESTROY_VEHICLE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENDER_BUILD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENDERDRAGON_BLOCK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENDERPEARL, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENTITY_ITEM_FRAME_DESTROY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENTITY_PAINTING_DESTROY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENTRY, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.EXIT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.EXIT_VIA_TELEPORT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.EXP_DROPS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.FALL_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.FIRE_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.FIREWORK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.GHAST_FIREBALL, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.GRASS_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ICE_FORM, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ICE_MELT, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.INTERACT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ITEM_DROP, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ITEM_PICKUP, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.LAVA_FIRE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.LAVA_FLOW, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.LEAF_DECAY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.LIGHTER, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.LIGHTNING, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.MOB_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.MOB_SPAWNING, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.MUSHROOMS, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.MYCELIUM_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.OTHER_EXPLOSION, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.PASSTHROUGH, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.PISTONS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.PLACE_VEHICLE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.POTION_SPLASH, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.PVP, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.RIDE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.SLEEP, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.SNOW_FALL, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.SNOW_MELT, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.SOIL_DRY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.TNT, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.USE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.VINE_GROWTH, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.WATER_FLOW, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.WITHER_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
				global.setPriority(0);
				com.sk89q.worldguard.protection.regions.ProtectedRegion spawnRegion = new com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion("spawn", new com.sk89q.worldedit.BlockVector(-(GeneratorMain.getSpawnRegion() + 1), 0, -(GeneratorMain.getSpawnRegion() + 1)), new com.sk89q.worldedit.BlockVector(GeneratorMain.getSpawnRegion(), world.getMaxHeight(), GeneratorMain.getSpawnRegion()));
				rm.removeRegion("spawn");
				//spawnRegion.setFlag(com.sk89q.worldguard.protection.flags.Flags.GREET_MESSAGE, "&5You have entered the spawn area.");
				//spawnRegion.setFlag(com.sk89q.worldguard.protection.flags.Flags.FAREWELL_MESSAGE, "&5You have left the spawn area.");
				spawnRegion.setPriority(1);
				rm.addRegion(spawnRegion);
				try {
					spawnRegion.setParent(global);
				} catch(Exception e) {//} catch(com.sk89q.worldguard.protection.regions.ProtectedRegion.CircularInheritanceException e) {
					throw new RuntimeException(e);
				}
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ITEM_DROP, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
				global.setFlag(com.sk89q.worldguard.protection.flags.Flags.ITEM_PICKUP, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			} catch(NullPointerException wtf) {
				Main.getPluginLogger().warning("Unable to update WorldGuard spawn region for world " + world.getName());
				if(!printedWTF) {
					printedWTF = true;
					Main.getPluginLogger().log(Level.WARNING, "Stack trace:", wtf);
				}
			}
		} catch(NoClassDefFoundError ex) {
			Main.getPluginLogger().warning("Unable to update WorldGuard spawn region for world " + world.getName());
			Main.getPluginLogger().log(Level.WARNING, "Stack trace:", ex);
		}
	}
	
	/** Updates the protected WorldGuard region around the spawn area for the
	 * skyworld and its' dimensions. */
	public static final void updateSpawnRegions() {
		updateSpawnRegionFor(GeneratorMain.getSkyworld());
		updateSpawnRegionFor(GeneratorMain.getSkyworldNether());
		updateSpawnRegionFor(GeneratorMain.getSkyworldTheEnd());
	}
	
	public static final Comparator<Material> MATERIAL_CASE_INSENSITIVE_ORDER = new Comparator<Material>() {
		@Override
		public int compare(Material mat1, Material mat2) {
			return String.CASE_INSENSITIVE_ORDER.compare(mat1.name(), mat2.name());
		}
	};
	
	/** All material enums, sorted alphabetically */
	private static final ArrayList<Material> sortedMaterials;
	
	static {
		sortedMaterials = new ArrayList<>(Arrays.asList(Material.values()));
		sortedMaterials.sort(MATERIAL_CASE_INSENSITIVE_ORDER);
	}
	
	public static final Material[] BY_ID;
	
	@Deprecated
	public static final Material getMaterial(int id) {
		return id >= 0 && id < BY_ID.length ? BY_ID[id] : null;
	}
	
	static {
		/*//List<Material> materials = new ArrayList<>();
		int largestID = -1;
		for(Material material : Material.values()) {
			//if(!material.isLegacy()) {
			//materials.add(material);
			if(material.getId() > largestID) {
				largestID = material.getId();
			}
			//}
		}*/
		BY_ID = new Material[Short.MAX_VALUE];//largestID == -1 ? Short.MAX_VALUE : largestID + 1];
		for(Material_1_12_2 material : Material_1_12_2.values()) {
			int id = material.getId();
			if(id >= 0 && id < BY_ID.length) {
				//Material_1_12_2.ENDER_STONE;
				//Material.LEGACY_ENDER_STONE;
				//etc....
				try {
					BY_ID[id] = Material.getMaterial(material.name(), true);
				} catch(NullPointerException ex) {
					ex.printStackTrace();
					break;
				}
			}
		}
		for(Material material : Material.values()) {//materials) {
			int id = material.getId();
			if(id >= 0 && id < BY_ID.length) {
				BY_ID[id] = material;
			}
		}
	}
	
	/** @return The root folder or jar file that the class loader loaded from */
	public static final File getClasspathFile() {
		return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile());//new File(getProperty("user.dir") + File.separator + getJarFileName());
	}
	
	/** @param resource The path to the resource
	 * @return An InputStream containing the resource's contents, or
	 *         <b><code>null</code></b> if the resource does not exist */
	public static final InputStream getResourceAsStream(String resource) {
		resource = resource.startsWith("/") ? resource : (resource.startsWith("assets") ? "/" + resource : "/assets" + resource);
		if(getClasspathFile().isDirectory()) {//Development environment.(Much simpler... >_>)
			return Main.class.getResourceAsStream(resource);
		}
		final String res = resource;
		return AccessController.doPrivileged(new PrivilegedAction<InputStream>() {
			@SuppressWarnings("resource")
			@Override
			public InputStream run() {
				try {
					final JarFile jar = new JarFile(getClasspathFile());
					String resource = res.startsWith("/") ? res.substring(1) : res;
					if(resource.endsWith("/")) {//Directory; list direct contents:(Mimics normal getResourceAsStream("someFolder/") behaviour)
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						Enumeration<JarEntry> entries = jar.entries();
						while(entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							if(entry.getName().startsWith(resource) && entry.getName().length() > resource.length()) {
								String name = entry.getName().substring(resource.length());
								if(name.contains("/") ? (name.endsWith("/") && (name.indexOf("/") == name.lastIndexOf("/"))) : true) {//If it's a folder, we don't want the children's folders, only the parent folder's children!
									name = name.endsWith("/") ? name.substring(0, name.length() - 1) : name;
									baos.write(name.getBytes(StandardCharsets.UTF_8));
									baos.write('\r');
									baos.write('\n');
								}
							}
						}
						jar.close();
						return new ByteArrayInputStream(baos.toByteArray());
					}
					JarEntry entry = jar.getJarEntry(resource);
					InputStream in = entry != null ? jar.getInputStream(entry) : null;
					if(in == null) {
						jar.close();
						return in;
					}
					final InputStream stream = in;//Don't manage 'jar' with try-with-resources or close jar until the
					return new InputStream() {//returned stream is closed(closing jar closes all associated InputStreams):
						@Override
						public int read() throws IOException {
							return stream.read();
						}
						
						@Override
						public int read(byte b[]) throws IOException {
							return stream.read(b);
						}
						
						@Override
						public int read(byte b[], int off, int len) throws IOException {
							return stream.read(b, off, len);
						}
						
						@Override
						public long skip(long n) throws IOException {
							return stream.skip(n);
						}
						
						@Override
						public int available() throws IOException {
							return stream.available();
						}
						
						@Override
						public void close() throws IOException {
							try {
								jar.close();
							} catch(IOException ignored) {
							}
							stream.close();
						}
						
						@Override
						public synchronized void mark(int readlimit) {
							stream.mark(readlimit);
						}
						
						@Override
						public synchronized void reset() throws IOException {
							stream.reset();
						}
						
						@Override
						public boolean markSupported() {
							return stream.markSupported();
						}
					};
				} catch(Throwable e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}
	
}
