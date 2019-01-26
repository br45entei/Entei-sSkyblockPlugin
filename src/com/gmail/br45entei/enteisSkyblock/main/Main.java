package com.gmail.br45entei.enteisSkyblock.main;

import com.gmail.br45entei.commands.UnsafeEnchantCommand;
import com.gmail.br45entei.enteisSkyblock.challenge.Challenge;
import com.gmail.br45entei.enteisSkyblock.challenge.Challenge.ChallengeCommand;
import com.gmail.br45entei.enteisSkyblock.event.ChallengeCompleteEvent;
import com.gmail.br45entei.enteisSkyblock.main.Island.InvitationResult;
import com.gmail.br45entei.enteisSkyblock.main.Island.JoinRequestResult;
import com.gmail.br45entei.enteisSkyblock.vault.VaultHandler;
import com.gmail.br45entei.enteisSkyblockGenerator.main.GeneratorMain;
import com.gmail.br45entei.enteisSkyblockGenerator.main.SkyworldGenerator;
import com.gmail.br45entei.enteisSkyblockGenerator.main.SkyworldGenerator.SkyworldBlockPopulator;
import com.gmail.br45entei.util.CodeUtil;
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
import java.util.Collections;
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
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
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
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

/** @author Brian_Entei */
@SuppressWarnings("javadoc")
public strictfp class Main extends JavaPlugin implements Listener {
	
	protected static volatile Main plugin;
	public static volatile Server server;
	public static volatile ConsoleCommandSender console;
	public static volatile BukkitScheduler scheduler;
	public static volatile PluginManager pluginMgr;
	
	private static volatile boolean vaultEnabled = false;
	
	private static volatile double defaultLevel = 0.01;
	protected static volatile boolean checkContainers = true,
			checkItemStacks = true, checkEntities = true;
	protected static volatile double materialLevelDropOff = 5000.0;
	protected static final ConcurrentHashMap<Material, Double> materialLevels = new ConcurrentHashMap<>();
	private volatile ConfigurationSection materialLevelConfig = null;
	
	/** @param material The material whose level will be returned
	 * @return The level for the given material, or <code>0.01</code> if the
	 *         material was not found/configured. */
	public static final double getLevelFor(Material material) {
		if(material == null) {
			throw new NullPointerException("Material cannot be null!");
		}
		Double level = materialLevels.get(material);
		if(level == null) {
			Main.getPlugin().getLogger().warning("A material was not set in the configuration file materialLevels.yml: " + material.name());
			level = Double.valueOf(defaultLevel);
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
		net.minecraft.server.v1_12_R1.ItemStack nms = null;
		if(item instanceof org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack) {
			//nms = ((org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack) item).handle;
			try {
				Field _handle = getDeclaredField("handle", org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.class);
				_handle.setAccessible(true);
				nms = (net.minecraft.server.v1_12_R1.ItemStack) _handle.get(item);
			} catch(SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				return item.hasItemMeta() ? item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name().toLowerCase().replace("_", " ") : item.getType().name().toLowerCase().replace("_", " ");
			}
		}
		if(nms == null) {
			nms = org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(item);
		}
		return nms != null ? nms.getName() : null;
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
		HashMap<String, Integer> dataValues = new HashMap<>();
		
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
				@SuppressWarnings("deprecation")
				Material material = Material.getMaterial(Integer.parseInt(materialID));
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
							dataValues.put(material.name() + ":" + Integer.toString(i), value);
						}
					}
				} else {
					if(Main.isInt(dataValue)) {
						dataValues.put(material.name() + ":" + Integer.valueOf(dataValue), Integer.valueOf(blockValues.getInt(key, 0)));
					}
				}
			} else {
				if(!Main.isInt(materialID)) {
					System.err.println("Skipping bad material id \"" + materialID + "\"...");
					continue;
				}
				@SuppressWarnings("deprecation")
				Material material = Material.getMaterial(Integer.parseInt(materialID));
				Integer value = Integer.valueOf(blockValues.getInt(key, 0));
				dataValues.put(material.name() + ":0", value);
			}
		}
		List<String> keySet = new ArrayList<>(dataValues.keySet());
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
		ConfigurationSection config = this.getMaterialLevelConfig();
		if(config == null) {
			file.delete();
			config = this.saveDefaultMaterialConfig();
		}
		this.materialLevelConfig = config;
		if(config == null) {
			this.getLogger().warning("Unable to load or save configuration file materialLevels.yml!");
			this.getLogger().warning("Setting all materials to the default value of '" + new BigDecimal(defaultLevel).toPlainString() + "'.");
			for(Material material : Material.values()) {
				materialLevels.put(material, Double.valueOf(defaultLevel));
			}
			return false;
		}
		for(String materialName : config.getKeys(false)) {
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
			Material material = Material.getMaterial(materialName);
			if(material == null) {
				this.getLogger().warning("Material \"" + materialName + "\" specified in file materialLevels.yml does not exist! Ignoring...");
				continue;
			}
			double value = config.getDouble(materialName, defaultLevel);
			materialLevels.put(material, Double.valueOf(value));
		}
		return true;
	}
	
	public final boolean saveMaterialConfig() {
		File file = this.getMaterialConfigFile();
		YamlConfiguration config = new YamlConfiguration();
		config.set("checkContainers", Boolean.valueOf(checkContainers));
		config.set("checkItemStacks", Boolean.valueOf(checkItemStacks));
		config.set("checkEntities", Boolean.valueOf(checkEntities));
		for(Entry<Material, Double> entry : materialLevels.entrySet()) {
			config.set(entry.getKey().name(), Double.toString(getDoubleSafe(entry.getValue())));
		}
		try {
			config.save(file);
			return true;
		} catch(IOException ex) {
			this.getLogger().log(Level.SEVERE, "Failed to save to configuration file " + file.getName(), ex);
			return false;
		}
	}
	
	public final ConfigurationSection saveDefaultMaterialConfig() {
		File file = this.getMaterialConfigFile();
		if(file.isFile() && ResourceUtil.size(file) > 0) {
			return null;//Fail silently if file already exists
		}
		YamlConfiguration config = new YamlConfiguration();
		config.set("checkContainers", Boolean.valueOf(checkContainers));
		config.set("checkItemStacks", Boolean.valueOf(checkItemStacks));
		config.set("checkEntities", Boolean.valueOf(checkEntities));
		for(Material material : Material.values()) {
			config.set(material.name(), Double.valueOf(0.01));
		}
		try {
			config.save(file);
		} catch(IOException e) {
			System.err.print("main() threw an error: ");
			e.printStackTrace(System.err);
			System.err.flush();
		}
		return config;
	}
	
	public final ConfigurationSection getMaterialConfig() {
		if(this.materialLevelConfig == null) {
			this.materialLevelConfig = this.getMaterialLevelConfig();
		}
		return this.materialLevelConfig == null ? new YamlConfiguration() : this.materialLevelConfig;
	}
	
	private final ConfigurationSection getMaterialLevelConfig() {
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
	
	/** Teleports the player to the given location. If the player is riding a
	 * vehicle, it will be dismounted prior to teleportation.
	 *
	 * @param player The player to teleport
	 * @param location New location to teleport the player to
	 * @return <code>true</code> if the teleport was successful */
	public static final boolean safeTeleport(Player player, Location location) {
		if(!location.getBlock().getType().isTransparent() || !location.getBlock().getRelative(0, 1, 0).getType().isTransparent()) {
			while(location.getBlockY() <= location.getWorld().getMaxHeight()) {
				location = location.add(0, 1, 0);
				if(location.getBlock().getType().isTransparent() && location.getBlock().getRelative(0, 1, 0).getType().isTransparent() && location.getBlock().getRelative(0, 2, 0).getType().isTransparent()) {
					break;
				}
			}
			if(location.getBlockY() >= location.getWorld().getMaxHeight()) {
				while(location.getBlockY() >= 1) {
					location = location.subtract(0, 1, 0);
					if(location.getBlock().getType().isTransparent() && location.getBlock().getRelative(0, 1, 0).getType().isTransparent() && location.getBlock().getRelative(0, 2, 0).getType().isTransparent()) {
						break;
					}
				}
			}
		}
		player.setVelocity(new Vector(0.0, -player.getVelocity().getY(), 0.0));
		return player.teleport(location);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
		if(Challenge.ChallengeCommand.isChallengeCommand(command)) {
			return Challenge.ChallengeCommand.onCommand(sender, command, args);
		}
		if(UnsafeEnchantCommand.isUnsafeEnchantCommand(command)) {
			return UnsafeEnchantCommand.onCommand(sender, command, args);
		}
		Player user = sender instanceof Player ? (Player) sender : null;
		if(command.equalsIgnoreCase("enteisskyblock")) {
			if(sender.hasPermission("skyblock.admin")) {
				sender.sendMessage(ChatColor.GREEN.toString().concat("The island spawn task is ").concat(Island.isSpawnTaskRunning() ? "" : "not").concat(" currently running."));
				return true;
			}
			return false;
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
		if((command.equalsIgnoreCase("ih") || command.equalsIgnoreCase("hi") || command.equalsIgnoreCase("ig")) && args.length == 0) {
			command = "island";
			args = new String[] {"home"};
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
				if(args.length == 1 && args[0].equalsIgnoreCase("test")) {
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
				Island island = Island.getIslandFor(user);
				if(args.length >= 1 && (args[0].equalsIgnoreCase("mobs") || args[0].equalsIgnoreCase("animals"))) {
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
					} else {
						island.removeTrusted(target);
						sender.sendMessage(ChatColor.DARK_GREEN + "Player \"" + ChatColor.WHITE + (target.getName() == null ? args[1] : target.getName()) + ChatColor.RESET + ChatColor.DARK_GREEN + "\" is no longer trusted on this island.");
					}
					return true;
				} else if(args.length >= 1 && args[0].equalsIgnoreCase("create")) {
					if(island == null) {
						String type = "normal";
						if(args.length == 1) {
							island = Island.getNextAvailableIslandNearSpawn();
						} else if(args.length >= 2) {
							type = args[1];
							if(!type.equalsIgnoreCase("normal") && !type.equalsIgnoreCase("square")) {
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
						} else {
							sender.sendMessage(ChatColor.DARK_RED + "ERROR: Unknown/unimplemented island type provided: " + type);
							throw new IllegalStateException("Unknown/unimplemented island type provided: " + type);
						}
						safeTeleport(user, island.getSpawnLocation());
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
					island = Island.getIslandFor(target);
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
					island = Island.getIslandFor(target);
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
					if(Island.getIslandFor(target) != null) {
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
							if(item != null && item.getType() == Material.SAPLING) {
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
											if(block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2) {
												hasLeaves = true;
											} else if(block.getType() == Material.SAPLING) {
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
									if(item.getItemStack() != null && item.getItemStack().getType() == Material.SAPLING) {
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
											if(item.getType() == Material.SAPLING) {
												hasSaplings = true;
												break;
											} else if(item.getType() == Material.LEAVES || item.getType() == Material.LEAVES_2) {
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
							GeneratorMain.getSkyworld().dropItem(user.getLocation(), new ItemStack(Material.SAPLING, 1, (short) 0));
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
						island = Island.getIslandFor(target);
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
					if(!island.canRestart()) {
						sender.sendMessage(ChatColor.YELLOW + "You'll need to wait " + ChatColor.GOLD + getLengthOfTime(island.getNextRestartTime()) + ChatColor.YELLOW + " until you can restart the island again.");
						return true;
					}
					for(Player p : island.getPlayersOnIsland()) {
						safeTeleport(p, GeneratorMain.getSkyworldSpawnLocation());
						if(!island.isMember(p)) {
							p.sendMessage(ChatColor.YELLOW + "The island you were just at was just restarted.");
						}
					}
					island.restart();
					safeTeleport(user, island.getSpawnLocation());
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
					//TODO Finish this command
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
						island = Island.getIslandFor(player);
						if(island == null) {
							sender.sendMessage(ChatColor.RED + "That player is not a member of an island.");
							return true;
						}
						sender.sendMessage(ChatColor.WHITE + island.getOwnerName() + ChatColor.RESET + ChatColor.GREEN + "'s island's level is: " + ChatColor.BLUE + limitDecimalToNumberOfPlaces(island.getLevel(), 2));
						return true;
					}
					sender.sendMessage(ChatColor.GREEN + "The island's level is: " + ChatColor.BLUE + limitDecimalToNumberOfPlaces(island.calculateLevel(), 2));
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
						Island island = Island.getIslandFor(player);
						if(island == null) {
							switch(slot) {
							case 0:
								Main.server.dispatchCommand(sender, "island create normal nearspawn");
								break;
							case 1:
								Main.server.dispatchCommand(sender, "island create normal faraway");
								break;
							case 2:
								Main.server.dispatchCommand(sender, "island create normal random");
								break;
							case 3:
								Main.server.dispatchCommand(sender, "island create square nearspawn");
								break;
							case 4:
								Main.server.dispatchCommand(sender, "island create square faraway");
								break;
							case 5:
								Main.server.dispatchCommand(sender, "island create square random");
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
										if(clicked != null && clicked.getType() == Material.SKULL_ITEM && clicked.hasItemMeta()) {
											SkullMeta meta = (SkullMeta) clicked.getItemMeta();
											OfflinePlayer chosenOwner = meta.getOwningPlayer();
											Island chosen = Island.getIslandFor(chosenOwner);
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
									} else if(i + 1 % 53 == 0 || i + 1 % 45 == 0) {
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
					//TODO finish changing these to load from config!
					gui.setSlot(0, Material.GRASS, this.getStringColor("island.gui-lore.uncreated.normal-nearspawn.title", ChatColor.GREEN + "Create Normal Near Spawn"), this.getStringList("island.gui-lore.uncreated.normal-nearspawn.lore", ChatColor.GRAY + "Click to create a normal island", ChatColor.GRAY + "near the spawn area."));
					gui.setSlot(1, Material.GRASS, this.getStringColor("island.gui-lore.uncreated.normal-faraway.title", ChatColor.GREEN + "Create Normal Far Away"), this.getStringList("island.gui-lore.uncreated.normal-faraway.lore", ChatColor.GRAY + "Click to create a normal island", ChatColor.GRAY + "far away from other islands."));
					gui.setSlot(2, Material.GRASS, this.getStringColor("island.gui-lore.uncreated.normal-random.title", ChatColor.GREEN + "Create Normal Random"), this.getStringList("island.gui-lore.uncreated.normal-random.lore", ChatColor.GRAY + "Click to create a normal island", ChatColor.GRAY + "in a random location."));
					gui.setSlot(3, Material.GRASS, this.getStringColor("island.gui-lore.uncreated.square-nearspawn.title", ChatColor.GREEN + "Create Square Near Spawn"), this.getStringList("island.gui-lore.uncreated.square-nearspawn.lore", ChatColor.GRAY + "Click to create a square island", ChatColor.GRAY + "near the spawn area."));
					gui.setSlot(4, Material.GRASS, this.getStringColor("island.gui-lore.uncreated.square-faraway.title", ChatColor.GREEN + "Create Square Far Away"), this.getStringList("island.gui-lore.uncreated.square-faraway.lore", ChatColor.GRAY + "Click to create a square island", ChatColor.GRAY + "far away from other islands."));
					gui.setSlot(5, Material.GRASS, this.getStringColor("island.gui-lore.uncreated.square-random.title", ChatColor.GREEN + "Create Square Random"), this.getStringList("island.gui-lore.uncreated.square-random.lore", ChatColor.GRAY + "Click to create a square island", ChatColor.GRAY + "in a random location."));
					gui.setSlot(8, Material.SIGN, this.getStringColor("island.gui-lore.uncreated.join-island.title", ChatColor.GREEN + "Join an Island"), this.getStringList("island.gui-lore.uncreated.join-island.lore", ChatColor.GRAY + "Click to view a list of islands", ChatColor.GRAY + "that you can request to join."));
				} else {
					gui.setSlot(0, Material.SIGN, ChatColor.GREEN + "Island information", ChatColor.GRAY + "Click to view information about", ChatColor.GRAY + "your island.");
					gui.setSlotIcon(1, new ItemStack(Material.BED, 1, (short) 14)).setSlotTitle(1, ChatColor.GREEN + "Island home").setSlotLore(1, ChatColor.GRAY + "Click to teleport to your island's ", ChatColor.GRAY + "home.");
					gui.setSlot(2, Material.EXP_BOTTLE, ChatColor.GREEN + "Island level", ChatColor.GRAY + "Click to calculate the island's", ChatColor.GRAY + "level.");
					gui.setSlot(9, Material.GOLD_INGOT, ChatColor.GOLD + "Island Challenges", ChatColor.GRAY + "Click to view the island challenges");
					gui.setSlot(16, Material.SPRUCE_DOOR_ITEM, ChatColor.RED + "Leave the Island", ChatColor.YELLOW + "Click to leave this island");
					if(island.isOwner(user)) {
						gui.setSlot(8, Material.NETHER_STAR, ChatColor.GREEN + "Island warp", ChatColor.GRAY + "Click to set the island's warp", ChatColor.GRAY + "location.");
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
		if(command.equalsIgnoreCase("dev")) {
			if(user != null && user.hasPermission("skyblock.dev")) {
				if(args.length == 0 || (args.length == 1 && (args[0].equalsIgnoreCase("help") || args[0].equals("?")))) {
					sender.sendMessage(ChatColor.WHITE + "/dev setowner playername - Sets the owner of the island you are visiting");
					sender.sendMessage(ChatColor.WHITE + "/dev delete - Run while visiting an island to delete it. This will wipe the island's member's inventories.");
					sender.sendMessage(ChatColor.WHITE + "/dev deleteBlocks - Run while visiting an island to delete all of its' blocks. This does not remove the island.");
					sender.sendMessage(ChatColor.WHITE + "/dev restart - Run while visiting an island to restart it. This will wipe the island's member's inventories.");
					sender.sendMessage(ChatColor.WHITE + "/dev regenIsland [normal|square] - Run while visiting an island to regenerate the starting island. This does not delete blocks.");
					sender.sendMessage(ChatColor.WHITE + "/dev lock|unlock - Run while visiting an island to lock or unlock the island. Only the island's members and trusted players will be able to enter it(including players who can run /dev).");
					sender.sendMessage(ChatColor.WHITE + "/dev info - Run while visiting an island to view information about it.");
				}
				return true;
			}
			return false;
		}
		if(command.equalsIgnoreCase("chest")) {
			if(Island.isInSkyworld(user)) {
				InventoryGUI.getStorageChestForPlayer(user).show(user);
			} else {
				sender.sendMessage(ChatColor.RED + "You can only access your storage chest in the skyworld.");
			}
			return true;
		}
		if(command.equalsIgnoreCase("spawn")) {
			if(user != null) {
				World world = server.getWorld("world");
				Location location = world != null ? world.getSpawnLocation() : user.getWorld().getSpawnLocation();
				safeTeleport(user, location.add(0.5, 0x0.0p0, 0.5));
			} else {
				if(args.length == 1) {
					World world = server.getWorld("world");
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Main.server.getOfflinePlayer(args[0]);
					if(target != null && target.isOnline()) {
						Location location = world != null ? world.getSpawnLocation() : target.getPlayer().getWorld().getSpawnLocation();
						safeTeleport(target.getPlayer(), location.add(0.5, 0x0.0p0, 0.5));
						sender.sendMessage(ChatColor.GREEN + "Teleported player \"" + ChatColor.WHITE + target.getPlayer().getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" to the spawn.");
					}
				} else {
					sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/" + command + ChatColor.RESET + ChatColor.WHITE + " {playerName}" + ChatColor.DARK_GREEN + ": Teleport the specified player to the server spawn");
				}
			}
			return true;
		}
		if(command.equalsIgnoreCase("skyworld") || command.equalsIgnoreCase("skyblock")) {
			if(user != null) {
				safeTeleport(user, GeneratorMain.getSkyworld().getSpawnLocation().add(0.5, 0x0.0p0, 0.5));
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "This command can only be used by players.");
			}
			return true;
		}
		if(command.equalsIgnoreCase("test")) {
			if(user != null) {
				if(!user.hasPermission("skyblock.admin")) {
					sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to create test islands.");
					return true;
				}
				Island island = null;
				String type = "normal";
				if(args.length >= 1) {
					type = args[0];
					if(!type.equalsIgnoreCase("normal") && !type.equalsIgnoreCase("square")) {
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
				} else {
					throw new IllegalStateException("Unknown island type provided: " + type);
				}
				safeTeleport(user, island.getSpawnLocation());
				sender.sendMessage(ChatColor.GREEN + "Successfully created your island. Have fun!");
				sender.sendMessage(ChatColor.GREEN + "To invite other players, type \"" + ChatColor.WHITE + "/island invite {playername}" + ChatColor.GREEN + "\".");
				return true;
			}
			sender.sendMessage(ChatColor.DARK_RED + "This test command can only be used by players.");
			return true;
		}
		if(command.equalsIgnoreCase("delete")) {
			if(user != null) {
				if(!user.hasPermission("skyblock.admin")) {
					sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to delete islands.");
					return true;
				}
				Island check = Island.getIslandNearest(user.getLocation());
				if(args.length >= 1 && args[0].equalsIgnoreCase("blocks")) {
					if(check == null) {
						check = Island.getOrCreateIslandNearest(user.getLocation());
						Island.islands.remove(check);
					}
					check.deleteBlocks(args.length == 2 && args[1].equalsIgnoreCase("true"));
					sender.sendMessage(ChatColor.YELLOW + "Successfully wiped all blocks within the island at " + ChatColor.WHITE + check.getID() + ChatColor.YELLOW + ".");
					sender.sendMessage(ChatColor.YELLOW + "If the island had members, their inventories were not affected.");
				} else if(args.length == 0) {
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
					sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.WHITE + "/delete [blocks] [setBiomeToOcean]");
				}
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "This command can only be used by players.");
			}
			return true;
		}
		if(command.equalsIgnoreCase("regenerate")) {
			if(!sender.hasPermission("skyblock.admin")) {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to regenerate the skyworld chunks.");
				return true;
			}
			if(user == null) {
				sender.sendMessage(ChatColor.DARK_RED + "This command can only be used by players.");
				return true;
			}
			if(user.getWorld() != GeneratorMain.getSkyworld()) {
				user.sendMessage(ChatColor.RED + "You can only regenerate skyblock chunks in the skyworld.");
				return true;
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
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
			return true;
		}
		if(command.equalsIgnoreCase("save-all")) {
			saveAll();
		}
		return false;
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
		byte[][] result = SkyworldGenerator._generateBlockSections(world, random, chunk.getX(), chunk.getZ(), null);
		for(int x = 0; x < 16; x++) {
			for(int y = 0; y < world.getMaxHeight(); y++) {
				for(int z = 0; z < 16; z++) {
					final Vector coords = SkyworldGenerator.getWorldCoordsFor(chunk.getX(), chunk.getZ(), x, y, z);
					if(Island.getIslandContaining(coords.toLocation(world)) == null) {
						chunk.getBlock(x, y, z).setType(SkyworldGenerator.getMaterial(result, x, y, z), true);
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
		if(command.equalsIgnoreCase("spawn") || command.equalsIgnoreCase("skyworld") || command.equalsIgnoreCase("test") || command.equalsIgnoreCase("delete")) {
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
			}
			return list;
		}
		if(command.equalsIgnoreCase("island")) {
			Player user = sender instanceof Player ? (Player) sender : null;
			Island island = Island.getIslandFor(user);
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
							Island check = Island.getIslandFor(player);
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
		System.out.println(capitalizeFirstLetterOfEachWord("lava buckets r cool"));
		System.out.println(capitalizeFirstLetterOfEachWord("SMOOTH_STONE".toLowerCase(), '_', ' '));
		
		convertMaterialValuesFromUSkyblock(new File(new File(System.getProperty("user.dir") + File.separator + "plugins" + File.separator + "uSkyblock"), "levelConfig.yml"));
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
		return Main.pluginMgr.getPlugin("WorldEdit");
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
		HandlerList.unregisterAll((Plugin) this);
		scheduler.cancelTasks(this);
		if(vaultEnabled) {
			VaultHandler.onDisable();
		}
	}
	
	@Override
	public void onEnable() {
		File folder = this.getDataFolder();
		folder.mkdirs();
		pluginMgr.registerEvents(this, this);
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
		
		//====================================
		/*Island test = Island.getOrCreate(0, 2);
		test.deleteIsland();
		Island.islands.add(test);
		test.updateRegion();
		test.generateIsland();*/
		
		ShapelessRecipe mossyCobblestone = new ShapelessRecipe(new NamespacedKey(this, UUID.randomUUID().toString()), new ItemStack(Material.MOSSY_COBBLESTONE, 1)).addIngredient(1, Material.COBBLESTONE).addIngredient(1, Material.VINE);
		server.addRecipe(mossyCobblestone);
		ShapelessRecipe mossyBricks = new ShapelessRecipe(new NamespacedKey(this, UUID.randomUUID().toString()), new ItemStack(Material.SMOOTH_BRICK, 1, (short) 1)).addIngredient(1, Material.SMOOTH_BRICK).addIngredient(1, Material.VINE);
		server.addRecipe(mossyBricks);
		ShapelessRecipe mossyCobbleWall = new ShapelessRecipe(new NamespacedKey(this, UUID.randomUUID().toString()), new ItemStack(Material.COBBLE_WALL, 1, (short) 1)).addIngredient(1, Material.COBBLE_WALL).addIngredient(1, Material.VINE);
		server.addRecipe(mossyCobbleWall);
		
		//This one is not working.
		ShapelessRecipe melonSlices = new ShapelessRecipe(new NamespacedKey(this, UUID.randomUUID().toString()), new ItemStack(Material.MELON, 9)).addIngredient(1, Material.MELON_BLOCK);
		server.addRecipe(melonSlices);
		
		ChallengeCommand.register();
		UnsafeEnchantCommand.class.getName();//Prevents java.lang.NoClassDefFoundError: com/gmail/br45entei/commands/UnsafeEnchantCommand in onDisable...
		PluginCommand command = this.getCommand("unsafeEnchant");
		if(command != null) {
			command.setExecutor((sender, command1, label, args) -> UnsafeEnchantCommand.onCommand(sender, label, args));
		}
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
		getPlugin().saveMaterialConfig();
		getPlugin().saveConfig();
	}
	
	public static final void loadAll() {
		getPlugin().saveDefaultConfig();
		getPlugin().reloadConfig();
		getPlugin().loadMaterialConfig();
		GeneratorMain.loadAll();
		Island.loadAllIslands();
		InventoryGUI.loadPerPlayerInventories();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockSpreadEvent(BlockSpreadEvent event) {
		Block spreading = event.getBlock();
		Island island = Island.getIslandContaining(spreading.getLocation());
		if(island != null) {
			if(event.isCancelled()) {
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockBurnEvent(BlockBurnEvent event) {
		//Block igniterBlock = event.getIgnitingBlock();
		Block burned = event.getBlock();
		Island island = Island.getIslandContaining(burned.getLocation());
		if(island != null) {
			if(event.isCancelled()) {
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockIgniteEvent(BlockIgniteEvent event) {
		//Player igniter = event.getPlayer();
		//Block igniterBlock = event.getIgnitingBlock();
		Block ignited = event.getBlock();
		Island island = Island.getIslandContaining(ignited.getLocation());
		if(island != null) {
			if(event.isCancelled()) {
				event.setCancelled(false);
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
				Island island = Island.getIslandFor(clicker);
				if(selected.complete(clicker)) {
					clicker.sendMessage(ChatColor.GREEN + "You just completed the \"" + ChatColor.WHITE + selected.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" challenge!");
					final InventoryView view = event.getView();
					Main.scheduler.scheduleSyncDelayedTask(getPlugin(), () -> {
						if(clicker.getOpenInventory() == view) {
							for(int slot1 = 0; slot1 < view.getTopInventory().getSize(); slot1++) {
								Challenge challenge = Challenge.getChallengeBySlot(slot1);
								if(challenge != null) {
									view.getTopInventory().setItem(slot1, challenge.getIcon(island != null ? island.hasMemberCompleted(clicker, challenge) : false));
								}
							}
							clicker.updateInventory();
						}
					});
				} else {
					if(!selected.isRepeatable() && island != null && island.hasMemberCompleted(clicker, selected)) {
						clicker.sendMessage(ChatColor.YELLOW + "The challenge \"" + ChatColor.WHITE + selected.getDisplayName() + ChatColor.RESET + ChatColor.YELLOW + "\" is not repeatable.");
					} else {
						clicker.sendMessage(ChatColor.YELLOW + "You still have requirements to meet before you can complete the \"" + ChatColor.WHITE + selected.getDisplayName() + ChatColor.RESET + ChatColor.YELLOW + "\" challenge.");
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public static final void onChallengeCompleteEvent(ChallengeCompleteEvent event) {
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
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onEntityTargetEvent(EntityTargetEvent event) {
		TargetReason reason = event.getReason();
		Entity entity = event.getEntity();
		Entity target = event.getTarget();
		if(reason == TargetReason.TARGET_ATTACKED_ENTITY || reason == TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
			if(target != null && target instanceof Skeleton && entity instanceof Skeleton && Island.getIslandContaining(target.getLocation()) != null) {
				Skeleton dumbShooter = (Skeleton) target;
				Skeleton wounded = (Skeleton) entity;
				event.setTarget(null);
				double health = wounded.getHealth();
				wounded.setHealth(wounded.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
				dumbShooter.setHealth(Math.min(dumbShooter.getHealth(), health));
			} else if(target != null && target instanceof Skeleton && entity instanceof Creeper && Island.getIslandContaining(target.getLocation()) != null) {
				event.setTarget(null);
				//((Skeleton) target).setTarget((Creeper) event.getEntity());
			}
		} else if(reason != TargetReason.TARGET_ATTACKED_ENTITY && reason != TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
			if(target != null && target instanceof Player) {
				Player player = (Player) target;
				Location location = player.getLocation();
				Island island = Island.getIslandContaining(location);
				if(location.getWorld() == GeneratorMain.getSkyworld() && (island == null || !island.isMember(player))) {
					event.setCancelled(true);//event.setTarget(event.getEntity());//LMAO
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
				player.sendMessage(ChatColor.RED + "You do not have permission to edit blocks outside of your island.");
				return;
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
		if((tool == null ? block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST && block.getType() != Material.SIGN : true)) {
			Block up = block.getRelative(0, 1, 0);
			Block down = block.getRelative(0, -1, 0);
			Block down2 = block.getRelative(0, -2, 0);
			Block left = block.getRelative(1, 0, 0);
			Block right = block.getRelative(-1, 0, 0);
			Block front = block.getRelative(0, 0, 1);
			Block back = block.getRelative(0, 0, -1);
			if(((up != null && (up.getType() == Material.LAVA || up.getType() == Material.STATIONARY_LAVA)) ||//
					(down != null && (down.getType() == Material.LAVA || down.getType() == Material.STATIONARY_LAVA)) ||//
					(left != null && (left.getType() == Material.LAVA || left.getType() == Material.STATIONARY_LAVA)) ||//
					(right != null && (right.getType() == Material.LAVA || right.getType() == Material.STATIONARY_LAVA)) ||//
					(front != null && (front.getType() == Material.LAVA || front.getType() == Material.STATIONARY_LAVA)) ||//
					(back != null && (back.getType() == Material.LAVA || back.getType() == Material.STATIONARY_LAVA))) ||//
					((down == null || (down.getType() == Material.WALL_SIGN || down.getType() == Material.SIGN_POST || down.getType() == Material.SIGN || !down.getType().isSolid())) && (down2 == null || (down2.getType() == Material.WALL_SIGN || down2.getType() == Material.SIGN_POST || down2.getType() == Material.SIGN || !down2.getType().isSolid())))) {
				
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
		return material == Material.STATIONARY_WATER || material == Material.STATIONARY_LAVA || material == Material.WATER || material == Material.LAVA;
	}
	
	public static final boolean isRedstone(Material material) {
		switch(material) {
		case LEVER:
		case STONE_PLATE:
		case WOOD_PLATE:
		case GOLD_PLATE:
		case IRON_PLATE:
		case TRAP_DOOR:
		case IRON_TRAPDOOR:
		case REDSTONE:
		case REDSTONE_BLOCK:
		case REDSTONE_COMPARATOR:
		case REDSTONE_COMPARATOR_OFF:
		case REDSTONE_COMPARATOR_ON:
		case REDSTONE_LAMP_OFF:
		case REDSTONE_LAMP_ON:
		case REDSTONE_TORCH_OFF:
		case REDSTONE_TORCH_ON:
		case REDSTONE_WIRE:
		case DIODE:
		case DIODE_BLOCK_OFF:
		case DIODE_BLOCK_ON:
		case DAYLIGHT_DETECTOR:
		case DAYLIGHT_DETECTOR_INVERTED:
		case STONE_BUTTON:
		case WOOD_BUTTON:
			return true;
		//$CASES-OMITTED$
		default:
			return false;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockFromToEvent(BlockFromToEvent event) {
		if(Island.getIslandContaining(event.getBlock().getLocation()) != null) {
			if(isRedstone(event.getToBlock().getType()) && isLiquid(event.getBlock().getType())) {
				event.setCancelled(true);
			}
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
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
			ItemStack egg = new ItemStack(Material.MONSTER_EGG, 1);
			ItemMeta meta = egg.getItemMeta();
			if(meta == null) {
				meta = Main.server.getItemFactory().getItemMeta(Material.MONSTER_EGG);
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
		return item0.getTypeId() == item1.getTypeId() && (item0.getType().isItem() ? true : item0.getDurability() == item1.getDurability()) && item0.hasItemMeta() == item1.hasItemMeta() && (item0.hasItemMeta() ? Bukkit.getItemFactory().equals(item0.getItemMeta(), item1.getItemMeta()) : true);
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
		if(Island.isInSkyworld(event.getLocation())) {
			if(event.getEntity() instanceof Creeper || Island.getIslandContaining(event.getLocation()) == null) {
				event.setCancelled(true);
			} else {
				event.setYield(100.0f);
			}
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
			updatePWPInventory(player);
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
					updatePWPInventory(player);
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
		if(island != null && !island.isMember(player)) {
			event.setCancelled(!player.hasPermission("skyblock.admin"));
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
	public static final void onPlayerItemBreakEvent(PlayerItemBreakEvent event) {
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
				updatePWPInventory(player);
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
					updatePWPInventory(player);
				}
			});
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		if(event.isCancelled()) {
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
					item.setItemStack(new ItemStack(Material.AIR));
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
				if(block.getType() != Material.PORTAL) {
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
					island = Island.getIslandFor((Player) entity);
					if(island == null || island.getNetherPortal() == null) {
						event.setCancelled(true);
						return;
					}
				} else {
					event.setCancelled(true);
					return;
				}
			}
			portal = getNetherPortalEntryLocation(island.getNetherPortal());
			event.useTravelAgent(false);
			event.setTo(portal);
			event.setCancelled(false);
		} else {
			//Main.println("Fail 1");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onPlayerPortalEvent(PlayerPortalEvent event) {
		EntityPortalEvent spoof = new EntityPortalEvent(event.getPlayer(), event.getFrom(), event.getTo(), event.getPortalTravelAgent());
		onEntityPortalEvent(spoof);
		event.setFrom(spoof.getFrom());
		event.setTo(spoof.getTo());
		event.setPortalTravelAgent(event.getPortalTravelAgent());
		event.useTravelAgent(spoof.useTravelAgent());
		event.setCancelled(spoof.isCancelled());
	}
	
	/** @param portal The nether portal
	 * @param yaw
	 * @return The given nether portal's entryway(the space in front of the
	 *         portal blocks), or the given portal if the entryway is blocked */
	public static final Location getNetherPortalEntryLocation(Location portal) {
		portal = getNetherPortal(portal);
		if(portal == null) {
			throw new IllegalArgumentException("Given location was not a nether portal!");
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
	public static final Location getNetherPortal(Location location) {
		return location == null ? null : getNetherPortal(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	/** @param world The world
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @return The location of a nether portal if the given location represents
	 *         one, or <b><code>null</code></b> */
	public static final Location getNetherPortal(World world, int x, int y, int z) {
		Block block = world.getBlockAt(x, y, z);
		if(block != null && block.getType() == Material.PORTAL) {
			Block obsidian = block.getRelative(0, -1, 0);
			if(obsidian != null && obsidian.getType() == Material.OBSIDIAN) {
				boolean portalBlocksPresent = true;
				for(int i = 1; i < 3; i++) {
					Block portal = block.getRelative(0, i, 0);
					portalBlocksPresent &= portal != null && portal.getType() == Material.PORTAL;
				}
				if(portalBlocksPresent) {
					Location found = new Location(world, x, y, z);
					Block check = found.getBlock().getRelative(-1, 0, 0);
					if(check != null && check.getType() == Material.PORTAL) {
						return check.getLocation();
					}
					check = found.getBlock().getRelative(0, 0, -1);
					if(check != null && check.getType() == Material.PORTAL) {
						return check.getLocation();
					}
					return found;
				}
			}
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
					Location found = getNetherPortal(world, x, y, z);
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
					Location found = getNetherPortal(world, x, y, z);
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
		portalLocation = getNetherPortal(portalLocation);
		if(portalLocation == null) {
			return -1;
		}
		Block portal = portalLocation.getBlock();
		Block checkX = portal.getRelative(1, 0, 0);
		Block checkZ = portal.getRelative(0, 0, 1);
		return checkX != null && checkX.getType() == Material.PORTAL ? 0 : (checkZ.getType() == Material.PORTAL ? 1 : -1);
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
			world.getBlockAt(x, y, z + i).setType(i != 0 ? Material.AIR : Material.PORTAL, false);
			world.getBlockAt(x + 1, y, z + i).setType(i != 0 ? Material.AIR : Material.PORTAL, false);
			world.getBlockAt(x, y + 1, z + i).setType(i != 0 ? Material.AIR : Material.PORTAL, false);
			world.getBlockAt(x + 1, y + 1, z + i).setType(i != 0 ? Material.AIR : Material.PORTAL, false);
			world.getBlockAt(x, y + 2, z + i).setType(i != 0 ? Material.AIR : Material.PORTAL, false);
			world.getBlockAt(x + 1, y + 2, z + i).setType(i != 0 ? Material.AIR : Material.PORTAL, false);
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
		return getNetherPortal(portalLocation);
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
			
			world.getBlockAt(x + i, y, z).setType(i != 0 ? Material.AIR : Material.FIRE, true);
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
		return getNetherPortal(portalLocation);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public static final void onPlayerDeathEvent(PlayerDeathEvent event) {
		Player player = event.getEntity();
		playerDeathLocations.put(player.getUniqueId(), player.getLocation());
		Island island = Island.getIslandFor(player);
		if(island != null) {
			event.setKeepLevel(true);
			event.setDroppedExp(0);
			if(player.getBedSpawnLocation() == null) {
				player.setBedSpawnLocation(island.getSpawnLocation(), true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if(Island.isInSkyworld(playerDeathLocations.get(player.getUniqueId()))) {
			Island island = Island.getIslandFor(player);
			event.setRespawnLocation(island == null ? event.getRespawnLocation() : island.getSpawnLocation());
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
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onEntityDeathEvent(EntityDeathEvent event) {
		if(event.getEntity() instanceof Player) {
			return;
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public static final void onEntityDamageEvent(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Location location = player.getLocation();
			Island island = Island.getIslandContaining(location);
			if(location.getWorld() == GeneratorMain.getSkyworld() && island != null) {// && !island.isMember(player)) {
				event.setCancelled(island.allowPVP() ? event.isCancelled() : event.getCause() == DamageCause.ENTITY_ATTACK);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onEntityDamageByBlockEvent(EntityDamageByBlockEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Location location = player.getLocation();
			Island island = Island.getIslandContaining(location);
			if(island != null && !island.isMember(player)) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Location location = player.getLocation();
			Island island = Island.getIslandContaining(location);
			if(Island.isInSkyworld(location) && island != null) {// && !island.isMember(player)) {
				event.setCancelled(event.getDamager() instanceof Player ? !island.allowPVP() : false);
			}
		} else if(event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			Location location = event.getEntity().getLocation();
			Island island = Island.getIslandContaining(location);
			if(location.getWorld() == GeneratorMain.getSkyworld() && island != null && !island.isMember(player)) {
				event.setCancelled(true);
			}
		} else if(event.getDamager() instanceof Arrow && event.getEntity() instanceof Creeper) {
			Arrow arrow = (Arrow) event.getDamager();
			Creeper creeper = (Creeper) event.getEntity();
			if(arrow.getShooter() instanceof Skeleton) {
				Skeleton skeleton = (Skeleton) arrow.getShooter();
				skeleton.setTarget(creeper);
				creeper.setTarget(null);//creeper.setTarget(creeper); XDDDD
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public static final void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		if(Island.isInSkyworld(player) && !(player.hasPermission("skyblock.gamemode." + event.getNewGameMode().name().toLowerCase()) || player.isOp())) {
			if(event.getNewGameMode() != GameMode.SURVIVAL) {
				event.setCancelled(true);
				player.setGameMode(GameMode.SURVIVAL);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public static final void onPlayerGameModeChangeEventMonitor(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		if(Island.isInSkyworld(player)) {
			if(event.getNewGameMode() == GameMode.CREATIVE) {
				player.sendTitle(ChatColor.DARK_GRAY + "Creative in skyworld?", ChatColor.YELLOW + "Don't take out unobtainable blocks!", 20, 140, 40);
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
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		Island island = Island.getIslandFor(player);
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
		Main.scheduler.runTaskLater(getPlugin(), () -> {//If you change the gamemode right away, there is a chance that PerWorldInventories will glitch out and update your inventory to the wrong gamemode. Say, creative inventory for survival mode.
			if(player.getGameMode() != GameMode.SURVIVAL && Island.isInSkyworld(player) && !(player.hasPermission("skyblock.gamemode." + player.getGameMode().name().toLowerCase()) || player.isOp())) {
				player.setGameMode(GameMode.SURVIVAL);
			}
		}, 2L);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onPlayerJoinEvent(PlayerJoinEvent event) {
		Island.updateAllRegions();
		Player player = event.getPlayer();
		final Island island = Island.getIslandFor(player);
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
			}, 5L);
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
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public static final void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if(Island.isSkyworld(player.getWorld())) {
			Island island = Island.getIslandFor(player);
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
			if(island != null) {
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
			}
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
		Island island = Island.getIslandFor(player);
		if(island != null && island.isPlayerOnIsland(player)) {
			if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BED_BLOCK) {
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
									if(check != null && (check.getType() == Material.WATER || check.getType() == Material.STATIONARY_WATER)) {
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
				if(item.getType() == Material.MONSTER_EGG) {
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
										target.setType(Material.MOB_SPAWNER, false);
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
											updatePWPInventory(player);
										}
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
					if(block.getType() == Material.SOIL) {
						event.setUseInteractedBlock(Result.DENY);
						event.setCancelled(true);
					}
				}
			}
		}
		island = Island.getIslandNearest(player.getLocation());
		if(island != null && !island.isMember(player)) {
			if(event.getAction() == Action.LEFT_CLICK_BLOCK && player.isSneaking()) {
				if(Main.pluginMgr.getPlugin("EnteisPermissions") != null) {
					event.setCancelled(!player.hasPermission("skyblock.admin"));
					return;
				}
			}
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Block clicked = event.getClickedBlock();
				if(clicked != null && clicked.getState() instanceof InventoryHolder) {
					event.setCancelled(!player.hasPermission("skyblock.admin"));
					return;
				}
			}
		}
	}
	
	public static final void updatePWPInventory(Player player) {
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
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onEntityInteractEvent(EntityInteractEvent event) {
		Block block = event.getBlock();
		if(block == null) {
			return;
		}
		Location location = block.getLocation();
		Island island = Island.getIslandContaining(location);
		if(island != null) {
			if(block.getType() == Material.SOIL) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public static final void onServerCommandEvent(ServerCommandEvent event) {
		String command = event.getCommand();
		CommandSender sender = event.getSender();
		String[] args;
		{
			String[] split = command.split(Pattern.quote(" "));
			command = split[0];
			args = Arrays.copyOfRange(split, 1, split.length);
		}
		if(plugin.getCommand(command) == null) {
			if(plugin.onCommand(sender, null, command, args)) {
				event.setCancelled(true);
			}
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
	
	private static final void updateSpawnRegionFor(World world) {
		if(world.getEnvironment() == Environment.NORMAL) {
			Island.setBiome(new int[] {-(GeneratorMain.getSpawnRegion() + 1), -(GeneratorMain.getSpawnRegion() + 1), GeneratorMain.getSpawnRegion() + 1, GeneratorMain.getSpawnRegion() + 1}, world, Biome.OCEAN);
		}
		if(getWorldGuard() == null) {
			return;
		}
		try {
			com.sk89q.worldguard.bukkit.WorldGuardPlugin wg = com.sk89q.worldguard.bukkit.WorldGuardPlugin.inst();
			com.sk89q.worldguard.protection.managers.RegionManager rm = wg.getRegionManager(world);
			com.sk89q.worldguard.protection.regions.ProtectedRegion global = rm.getRegion(com.sk89q.worldguard.protection.regions.ProtectedRegion.GLOBAL_REGION);
			if(global == null) {
				global = new com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion(com.sk89q.worldguard.protection.regions.ProtectedRegion.GLOBAL_REGION, new com.sk89q.worldedit.BlockVector(-29999985, 0, -29999985), new com.sk89q.worldedit.BlockVector(29999984, world.getMaxHeight(), 29999984));
				rm.addRegion(global);
			}
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.BUILD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.DAMAGE_ANIMALS, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.CHEST_ACCESS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.CHORUS_TELEPORT, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.CREEPER_EXPLOSION, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.DAMAGE_ANIMALS, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.DESTROY_VEHICLE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENDER_BUILD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENDERPEARL, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENTITY_PAINTING_DESTROY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENTRY, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.EXIT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.EXIT_VIA_TELEPORT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.EXP_DROPS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.FALL_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.FIRE_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.FIREWORK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.GHAST_FIREBALL, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.GRASS_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ICE_FORM, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ICE_MELT, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.INTERACT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ITEM_DROP, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ITEM_PICKUP, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.LAVA_FIRE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.LAVA_FLOW, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.LEAF_DECAY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.LIGHTER, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.LIGHTNING, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MOB_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MOB_SPAWNING, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MUSHROOMS, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MYCELIUM_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.OTHER_EXPLOSION, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.PASSTHROUGH, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.PISTONS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.PLACE_VEHICLE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.POTION_SPLASH, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.PVP, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.RIDE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.SLEEP, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.SNOW_FALL, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.SNOW_MELT, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.SOIL_DRY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.TNT, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.USE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.VINE_GROWTH, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.WATER_FLOW, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.WITHER_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			global.setPriority(0);
			com.sk89q.worldguard.protection.regions.ProtectedRegion spawnRegion = new com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion("spawn", new com.sk89q.worldedit.BlockVector(-(GeneratorMain.getSpawnRegion() + 1), 0, -(GeneratorMain.getSpawnRegion() + 1)), new com.sk89q.worldedit.BlockVector(GeneratorMain.getSpawnRegion(), world.getMaxHeight(), GeneratorMain.getSpawnRegion()));
			rm.removeRegion("spawn");
			//spawnRegion.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.GREET_MESSAGE, "&5You have entered the spawn area.");
			//spawnRegion.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.FAREWELL_MESSAGE, "&5You have left the spawn area.");
			spawnRegion.setPriority(1);
			rm.addRegion(spawnRegion);
			try {
				spawnRegion.setParent(global);
			} catch(com.sk89q.worldguard.protection.regions.ProtectedRegion.CircularInheritanceException e) {
				throw new RuntimeException(e);
			}
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ITEM_DROP, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			global.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ITEM_PICKUP, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		} catch(NullPointerException wtf) {
			Main.getPlugin().getLogger().warning("Unable to update WorldGuard spawn region for world " + world.getName());
			if(!printedWTF) {
				printedWTF = true;
				Main.getPlugin().getLogger().log(Level.WARNING, "Stack trace:", wtf);
			}
		}
	}
	
	/** Updates the protected WorldGuard region around the spawn area for the
	 * skyworld and its' dimensions. */
	public static final void updateSpawnRegions() {
		updateSpawnRegionFor(GeneratorMain.getSkyworld());
		updateSpawnRegionFor(GeneratorMain.getSkyworldNether());
		updateSpawnRegionFor(GeneratorMain.getSkyworldTheEnd());
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
