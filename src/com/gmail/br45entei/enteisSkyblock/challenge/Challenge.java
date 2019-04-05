package com.gmail.br45entei.enteisSkyblock.challenge;

import com.gmail.br45entei.enteisSkyblock.event.ChallengeCompleteEvent;
import com.gmail.br45entei.enteisSkyblock.event.ChallengeTierCompleteEvent;
import com.gmail.br45entei.enteisSkyblock.main.InventoryGUI;
import com.gmail.br45entei.enteisSkyblock.main.Island;
import com.gmail.br45entei.enteisSkyblock.main.Main;
import com.gmail.br45entei.enteisSkyblock.vault.VaultHandler;
import com.gmail.br45entei.enteisSkyblockGenerator.main.GeneratorMain;
import com.gmail.br45entei.util.PlayerAdapter;
import com.gmail.br45entei.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/** @author Brian_Entei */
public class Challenge {
	
	private static volatile YamlConfiguration challengeConfig = null;
	
	public static final ConfigurationSection getChallengeConfig() {
		if(challengeConfig == null) {
			loadChallengeConfig();
			if(challengeConfig == null) {
				return new YamlConfiguration();// :/
			}
		}
		return challengeConfig;
	}
	
	public static final File getChallengeConfigFile() {
		return new File(Main.getPlugin().getDataFolder(), "challengeConfig.yml");
	}
	
	public static final void saveDifficultyDataTo(ConfigurationSection config, int difficulty, String name, String title, List<String> description) {
		String key = "difficulty.".concat(Integer.toString(difficulty));
		ConfigurationSection mem = config.createSection(key);
		mem.set(key.concat(".name"), name);
		mem.set(key.concat(".title"), title);
		mem.set(key.concat(".description"), description);
	}
	
	protected static final YamlConfiguration saveDefaultChallengeConfig(File file) throws IOException {
		YamlConfiguration config = new YamlConfiguration();
		config.set("challengeDifficultiesAreHalfPercentagedTiers", Boolean.TRUE);
		for(int difficulty = 0; difficulty < max_rows; difficulty++) {
			saveDifficultyDataTo(config, difficulty, getDefaultDifficultyName(difficulty), getDefaultDifficultyTitle(difficulty), getDefaultDifficultyDescription(difficulty));
		}
		config.save(file);
		return config;
	}
	
	public static final boolean saveChallengeConfig() {
		File file = getChallengeConfigFile();
		YamlConfiguration config = challengeConfig;
		if(config == null) {
			try {
				challengeConfig = saveDefaultChallengeConfig(file);
				return true;
			} catch(IOException ex) {
				Main.getPluginLogger().log(Level.WARNING, "Unable to save default challenge configuration to file \"".concat(file.getAbsolutePath()).concat("\""), ex);
				return false;
			}
		}
		try {
			config.save(file);
			return true;
		} catch(IOException ex) {
			Main.getPluginLogger().log(Level.WARNING, "Unable to save to challenge configuration file \"".concat(file.getAbsolutePath()).concat("\""), ex);
			return false;
		}
	}
	
	public static final boolean loadChallengeConfig() {
		File file = getChallengeConfigFile();
		if(!file.exists()) {
			return saveChallengeConfig();
		}
		YamlConfiguration config = (challengeConfig = new YamlConfiguration());
		try {
			config.load(file);
			return true;
		} catch(IOException | InvalidConfigurationException ex) {
			Main.getPluginLogger().log(Level.WARNING, "Unable to save to challenge configuration file \"".concat(file.getAbsolutePath()).concat("\""), ex);
			return false;
		}
	}
	
	/** Class used to implement the /challenge command.
	 *
	 * @author Brian_Entei */
	public static final class ChallengeCommand extends Command implements CommandExecutor {
		
		/** @param command The command to check
		 * @return Whether or not the command is the challenge command */
		public static final boolean isChallengeCommand(String command) {
			return command.equalsIgnoreCase(name) || aliases.contains(command.toLowerCase());
		}
		
		private static final String name = "challenge",
				description = "View or complete Island challenges",
				usageMessage = "/challenge complete {challengeName}";
		private static final List<String> aliases = new ArrayList<>();
		
		static {
			aliases.add("c");
			aliases.add("cc");
		}
		
		/** Register this command executor */
		public static final void register() {
			PluginCommand command = Main.getPlugin().getCommand(name);
			if(command != null) {
				command.setExecutor(new ChallengeCommand());
			}
		}
		
		/** Unregister this command executor */
		public static final void unregister() {
			Main.getPlugin().getCommand(name).setExecutor(Main.getPlugin());//Using null will just set it to the plugin anyway.
		}
		
		private ChallengeCommand() {
			super(ChallengeCommand.name, ChallengeCommand.description, ChallengeCommand.usageMessage, ChallengeCommand.aliases);
		}
		
		@Override
		public boolean execute(CommandSender sender, String command, String[] args) {
			return onCommand(sender, command, args);
		}
		
		public static final void sendPlayersOnlyMsg(CommandSender sender, String command) {
			sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
			sender.sendMessage(ChatColor.YELLOW + "Alternately, you may manage players' challenges via:");
			sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + "/" + command + " canComplete {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
			sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + "/" + command + " setComplete {playerName} {challengeName} {timesCompleted}" + ChatColor.YELLOW + "\".");
			sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + "/" + command + " completedTimes {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
			sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + "/" + command + " takeItems {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
			sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + "/" + command + " reward {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
		}
		
		/** Executes the command, returning its success
		 *
		 * @param sender Source object which is executing this command
		 * @param command The alias of the command used
		 * @param args All arguments passed to the command, split via ' '
		 * @return true if the command was successful, otherwise false */
		public static final boolean onCommand(CommandSender sender, String command, String[] args) {
			Player user = sender instanceof Player ? (Player) sender : null;
			if(command.equalsIgnoreCase("cc")) {
				command = "challenge";
				String[] tmp = args;
				args = new String[tmp.length + 1];
				args[0] = "complete";
				for(int i = 1; i < args.length; i++) {
					args[i] = tmp[i - 1];
				}
			}
			if(command.equalsIgnoreCase("challenge") || command.equalsIgnoreCase("c")) {
				if(args.length >= 1 && args[0].equalsIgnoreCase("help")) {
					
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("page")) {
					if(user == null) {
						sendPlayersOnlyMsg(sender, command);
						return true;
					}
					int maxPages = getMaximumChallengeScreenPages(false);
					if(args.length == 2 && Main.isInt(args[1]) && Integer.parseInt(args[1]) > 0) {
						int page = Integer.parseInt(args[1]);
						if(page > maxPages) {
							sender.sendMessage(ChatColor.YELLOW + "Page " + Integer.toString(page) + " is too big.");
							sender.sendMessage(ChatColor.YELLOW + "There are currently " + Integer.toString(maxPages) + " pages in total.");
							return true;
						}
						Challenge.setLastChallengeScreenPageFor(user.getUniqueId(), page - 1, false);
						user.openInventory(Challenge.getChallengeScreen(user));
						return true;
					}
					sender.sendMessage(ChatColor.YELLOW + "Usage: \"" + ChatColor.WHITE + "/" + command + " page {pageNum}" + ChatColor.YELLOW + "\"; where {pageNum} is a positive integer from 1 to the maximum page count.");
					sender.sendMessage(ChatColor.YELLOW + "There are currently " + Integer.toString(maxPages) + " pages in total.");
					return true;
				}
				if(args.length >= 1 && (args[0].equalsIgnoreCase("viewPlayerChallenges") || args[0].equalsIgnoreCase("editPlayerChallenges") || args[0].equalsIgnoreCase("viewPlayer") || args[0].equalsIgnoreCase("editPlayer"))) {
					if(!sender.hasPermission("skyblock.challenge.manage")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
						return true;
					}
					if(user == null) {
						sendPlayersOnlyMsg(sender, command);
						return true;
					}
					if(args.length < 2) {
						sender.sendMessage(ChatColor.YELLOW + "Usage: \"" + ChatColor.WHITE + "/" + command + " viewPlayerChallenges {playerName}" + ChatColor.YELLOW + "\".");
						return true;
					}
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Main.server.getOfflinePlayer(args[1]);
					if(target == null || Island.getMainIslandFor(target.getUniqueId(), false) == null) {
						sender.sendMessage(ChatColor.YELLOW + "Player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.YELLOW + "\" does not exist or isn't on any islands.");
						return true;
					}
					if(args.length >= 3) {
						int maxPages = getMaximumChallengeScreenPages(true);
						if(args.length == 4 && Main.isInt(args[3]) && Integer.parseInt(args[3]) > 0) {
							int page = Integer.parseInt(args[3]);
							if(page > maxPages) {
								sender.sendMessage(ChatColor.YELLOW + "Page " + Integer.toString(page) + " is too big.");
								sender.sendMessage(ChatColor.YELLOW + "There are currently " + Integer.toString(maxPages) + " pages in total.");
								sender.sendMessage(ChatColor.YELLOW + "Tip: To return to the last page you were viewing quickly, simply omit the page parameters.");
								return true;
							}
							Challenge.setLastChallengeScreenPageFor(user.getUniqueId(), page - 1, true);
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Usage: \"" + ChatColor.WHITE + "/" + command + " " + args[0] + "{playerName} page {pageNum}" + ChatColor.YELLOW + "\"; where {pageNum} is a positive integer from 1 to the maximum page count.");
							sender.sendMessage(ChatColor.YELLOW + "There are currently " + Integer.toString(maxPages) + " pages in total.");
							sender.sendMessage(ChatColor.YELLOW + "Tip: To return to the last page you were viewing quickly, simply omit the page parameters.");
						}
					}
					user.openInventory(Challenge.getEditPlayersChallengesScreen(target));
				}
				if(args.length >= 1 && (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("viewChallenges") || args[0].equalsIgnoreCase("review") || args[0].equalsIgnoreCase("reviewChallenges") || args[0].equalsIgnoreCase("showAll") || args[0].equalsIgnoreCase("showAllChallenges"))) {
					if(!sender.hasPermission("skyblock.challenge.showAll") && !sender.hasPermission("skyblock.challenge.showall")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
						return true;
					}
					if(user == null) {
						sendPlayersOnlyMsg(sender, command);
						return true;
					}
					if(args.length == 3) {
						int maxPages = getMaximumChallengeScreenPages(true);
						if(args[1].equalsIgnoreCase("page")) {
							if(Main.isInt(args[2]) && Integer.parseInt(args[2]) > 0) {
								int page = Integer.parseInt(args[2]);
								if(page > maxPages) {
									sender.sendMessage(ChatColor.YELLOW + "Page " + Integer.toString(page) + " is too big.");
									sender.sendMessage(ChatColor.YELLOW + "There are currently " + Integer.toString(maxPages) + " pages in total.");
									sender.sendMessage(ChatColor.YELLOW + "Tip: To return to the last page you were viewing quickly, simply omit the page parameters.");
									return true;
								}
								Challenge.setLastChallengeScreenPageFor(user.getUniqueId(), page - 1, true);
							} else {
								sender.sendMessage(ChatColor.YELLOW + "Usage: \"" + ChatColor.WHITE + "/" + command + " " + args[0] + " page {pageNum}" + ChatColor.YELLOW + "\"; where {pageNum} is a positive integer from 1 to the maximum page count.");
								sender.sendMessage(ChatColor.YELLOW + "There are currently " + Integer.toString(maxPages) + " pages in total.");
								sender.sendMessage(ChatColor.YELLOW + "Tip: To return to the last page you were viewing quickly, simply omit the page parameters.");
							}
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Usage: \"" + ChatColor.WHITE + "/" + command + " " + args[0] + " page {pageNum}" + ChatColor.YELLOW + "\"; where {pageNum} is a positive integer from 1 to the maximum page count.");
							sender.sendMessage(ChatColor.YELLOW + "There are currently " + Integer.toString(maxPages) + " pages in total.");
							sender.sendMessage(ChatColor.YELLOW + "Tip: To return to the last page you were viewing quickly, simply omit the page parameters.");
						}
					}
					user.openInventory(Challenge.getReviewChallengesScreen(user));
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("canComplete")) {
					if(args.length == 1) {
						
						return true;
					}
					if(args.length == 2) {
						if(user == null) {
							sendPlayersOnlyMsg(sender, command);
							return true;
						}
						Challenge challenge = Challenge.getChallengeByName(args[1]);
						if(challenge == null) {
							sender.sendMessage(ChatColor.RED + "There is no challenge with the name \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\".");
							return true;
						}
						if(!Island.isInSkyworld(user)) {
							sender.sendMessage(ChatColor.RED + "This command can only be used in the skyworld!");
							return true;
						}
						sender.sendMessage(ChatColor.GREEN.toString().concat("You ").concat(challenge.canComplete(user) ? ChatColor.DARK_GREEN.toString().concat("can ") : ChatColor.RED.toString().concat("can not")).concat(ChatColor.GREEN.toString()).concat(" complete the challenge \"").concat(ChatColor.WHITE.toString()).concat(challenge.getDisplayName()).concat(ChatColor.GREEN.toString()).concat("\"."));
						return true;
					}
					if(!sender.hasPermission("skyblock.challenge.manage")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
						return true;
					}
					if(args.length != 3) {
						sender.sendMessage(ChatColor.YELLOW + "Usage: \"" + ChatColor.WHITE + "/" + command + " canComplete {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
						return true;
					}
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Main.server.getOfflinePlayer(args[1]);
					if(target == null || !target.isOnline()) {
						sender.sendMessage(ChatColor.YELLOW + "Player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.YELLOW + "\" does not exist or is not online.");
						return true;
					}
					Challenge challenge = Challenge.getChallengeByName(args[2]);
					if(challenge == null) {
						sender.sendMessage(ChatColor.RED + "There is no challenge with the name \"" + ChatColor.WHITE + args[2] + ChatColor.RESET + ChatColor.RED + "\".");
						return true;
					}
					if(!Island.isInSkyworld(target.getPlayer())) {
						sender.sendMessage(ChatColor.YELLOW + "Player \"" + ChatColor.WHITE + target.getPlayer().getDisplayName() + ChatColor.RESET + ChatColor.YELLOW + "\" is not in the skyworld or one of its dimensions!");
						return true;
					}
					challenge.takeItems(target.getPlayer());
					sender.sendMessage(ChatColor.GREEN + "Took challenge requirements from player \"" + ChatColor.WHITE + target.getPlayer().getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" using challenge \"" + ChatColor.WHITE + challenge.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\"'s requirements.");
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("takeRequirements")) {
					if(!sender.hasPermission("skyblock.challenge.manage")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
						return true;
					}
					if(args.length != 3) {
						sender.sendMessage(ChatColor.YELLOW + "Usage: \"" + ChatColor.WHITE + "/" + command + " takeRequirements {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
						return true;
					}
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Main.server.getOfflinePlayer(args[1]);
					if(target == null || !target.isOnline()) {
						sender.sendMessage(ChatColor.YELLOW + "Player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.YELLOW + "\" does not exist or is not online.");
						return true;
					}
					Challenge challenge = Challenge.getChallengeByName(args[2]);
					if(challenge == null) {
						sender.sendMessage(ChatColor.RED + "There is no challenge with the name \"" + ChatColor.WHITE + args[2] + ChatColor.RESET + ChatColor.RED + "\".");
						return true;
					}
					if(!Island.isInSkyworld(target.getPlayer())) {
						sender.sendMessage(ChatColor.YELLOW + "Player \"" + ChatColor.WHITE + target.getPlayer().getDisplayName() + ChatColor.RESET + ChatColor.YELLOW + "\" is not in the skyworld or one of its dimensions!");
						return true;
					}
					challenge.takeItems(target.getPlayer());
					sender.sendMessage(ChatColor.GREEN + "Took challenge requirements from player \"" + ChatColor.WHITE + target.getPlayer().getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" using challenge \"" + ChatColor.WHITE + challenge.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\"'s requirements.");
					return true;
				}
				if(args.length >= 1 && args[0].equalsIgnoreCase("reward")) {
					if(!sender.hasPermission("skyblock.challenge.manage")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
						return true;
					}
					if(args.length != 3) {
						sender.sendMessage(ChatColor.YELLOW + "Usage: \"" + ChatColor.WHITE + "/" + command + " reward {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
						return true;
					}
					@SuppressWarnings("deprecation")
					OfflinePlayer target = Main.server.getOfflinePlayer(args[1]);
					if(target == null || !target.isOnline()) {
						sender.sendMessage(ChatColor.YELLOW + "Player \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.YELLOW + "\" does not exist or is not online.");
						return true;
					}
					Challenge challenge = Challenge.getChallengeByName(args[2]);
					if(challenge == null) {
						sender.sendMessage(ChatColor.RED + "There is no challenge with the name \"" + ChatColor.WHITE + args[2] + ChatColor.RESET + ChatColor.RED + "\".");
						return true;
					}
					if(!Island.isInSkyworld(target.getPlayer())) {
						sender.sendMessage(ChatColor.YELLOW + "Player \"" + ChatColor.WHITE + target.getPlayer().getDisplayName() + ChatColor.RESET + ChatColor.YELLOW + "\" is not in the skyworld or one of its dimensions!");
						return true;
					}
					challenge.reward(target.getPlayer());
					sender.sendMessage(ChatColor.GREEN + "Awarded player \"" + ChatColor.WHITE + target.getPlayer().getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" with challenge \"" + ChatColor.WHITE + challenge.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\"'s reward items.");
					return true;
				}
				if(user == null) {
					sendPlayersOnlyMsg(sender, command);
					return true;
				}
				if(!Island.isInSkyworld(user)) {
					sender.sendMessage(ChatColor.RED + "This command can only be used in the skyworld!");
					return true;
				}
				if(args.length == 0) {
					user.openInventory(Challenge.getChallengeScreen(user));
				} else if(args.length >= 1) {
					if(args[0].equalsIgnoreCase("complete")) {
						if(args.length == 1) {
							sender.sendMessage(ChatColor.YELLOW + "Usage: \"" + ChatColor.WHITE + "/" + command + " complete {challengeName}" + ChatColor.YELLOW + "\".");
							return true;
						}
						Challenge challenge = Challenge.getChallengeByName(args[1]);
						if(challenge == null) {
							sender.sendMessage(ChatColor.RED + "There is no challenge with the name \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\".");
							return true;
						}
						Island island = Island.getMainIslandFor(user);
						ItemStack icon = challenge.getIconFor(user);
						boolean debug1 = icon.hasItemMeta();
						boolean debug2 = debug1 && icon.getItemMeta().hasDisplayName();
						String title = debug2 ? ChatColor.stripColor(icon.getItemMeta().getDisplayName()).trim() : null;
						boolean debug3 = title != null && title.endsWith(" - Locked");
						if(debug3) {
							sender.sendMessage(ChatColor.RED.toString().concat("You must complete at least half of the challenges in the previous tier before you can attempt this one!"));
							return true;
						}
						Main.sendDebugMsg(user, "debug1: ".concat(Boolean.toString(debug1)).concat("; debug2: ").concat(Boolean.toString(debug2)).concat("; title: \"").concat(ChatColor.WHITE.toString()).concat(title == null ? "<null>" : title).concat(ChatColor.RESET.toString()).concat("\"; debug3: ").concat(Boolean.toString(debug3)).concat(";"));
						if(challenge.complete(user)) {
							sender.sendMessage(ChatColor.GREEN + "You just completed the \"" + ChatColor.WHITE + challenge.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" challenge!");
						} else {
							if(!challenge.isRepeatable() && island != null && island.hasMemberCompleted(user, challenge)) {
								sender.sendMessage(ChatColor.YELLOW + "The challenge \"" + ChatColor.WHITE + challenge.getDisplayName() + ChatColor.RESET + ChatColor.YELLOW + "\" is not repeatable.");
							} else {
								sender.sendMessage(ChatColor.YELLOW + "You still have requirements to meet before you can complete the \"" + ChatColor.WHITE + challenge.getDisplayName() + ChatColor.RESET + ChatColor.YELLOW + "\" challenge.");
							}
						}
						return true;
					}
					
					return true;
				}
				Main.server.dispatchCommand(sender, "challenge help");
				return true;
			}
			return false;
		}
		
		@Override
		public List<String> tabComplete(CommandSender sender, String command, String[] args) throws IllegalArgumentException {
			List<String> list = new ArrayList<>();
			if(command.equalsIgnoreCase("cc")) {
				command = "challenge";
				String[] tmp = args;
				args = new String[tmp.length + 1];
				args[0] = "complete";
				for(int i = 1; i < args.length; i++) {
					args[i] = tmp[i - 1];
				}
			}
			command = name;
			if(command.equalsIgnoreCase("challenge")) {
				if(args.length == 0) {
					list.add("complete");
				} else if(args.length == 1 && args[0].equalsIgnoreCase("complete")) {
					for(Challenge challenge : Challenge.getChallenges()) {
						list.add(challenge.getName());
					}
				}
			}
			return list;
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
			return this.execute(sender, command, args);
		}
		
	}
	
	private static final ConcurrentLinkedDeque<Challenge> challenges = new ConcurrentLinkedDeque<>();
	private static final int max_rows = 5;//9 * 6 = 54; last row of slots is reserved for gui buttons; 5 usable rows
	
	public static final boolean challengeDifficultiesAreHalfPercentagedTiers() {
		return getChallengeConfig().getBoolean("challengeDifficultiesAreHalfPercentagedTiers", true);
	}
	
	/** If no challenge files are saved in the challenge save folder, this will
	 * save the default challenges to the folder.
	 * 
	 * @return The list of challenges that were saved, or an empty list if there
	 *         were already challenges saved */
	public static final ArrayList<Challenge> saveDefaultChallenges() {
		File folder = getChallengeSaveFolder();
		boolean areThereYmlFiles = false;
		for(File file : folder.listFiles()) {
			if(file.isFile() && file.getName().endsWith(".yml")) {
				areThereYmlFiles = true;
				break;
			}
		}
		ArrayList<Challenge> challenges = new ArrayList<>();
		if(!areThereYmlFiles) {
			challenges.add(new Challenge("cobblestone", "&8Cobblestone &7Generator", new String[] {"Mine a stack of cobble from a generator"}, Material.COBBLESTONE, (short) 0, 0, 0, new ItemStack[] {new ItemStack(Material.COBBLESTONE, Material.COBBLESTONE.getMaxStackSize())}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.IRON_NUGGET, 3)}, 0.14, 0.141421356F, new String[0], new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.FAST_DIGGING, 300 * 20, 2)}));
			challenges.add(new Challenge("cactus", "&aCactus &6Farmer", new String[] {"Harvest an entire stack of cacti"}, Material.CACTUS, (short) 0, 0, 1, new ItemStack[] {new ItemStack(Material.CACTUS, Material.CACTUS.getMaxStackSize())}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.SAND, 3)}, 0.14, 0.141421356F, new String[0], new String[0]));
			challenges.add(new Challenge("wheat_farmer", "&2Wheat &6Farmer", new String[] {"Harvest three stacks of wheat"}, Material.HAY_BLOCK, (short) 0, 0, 2, new ItemStack[] {new ItemStack(Material.WHEAT, Material.WHEAT.getMaxStackSize()), new ItemStack(Material.WHEAT, Material.WHEAT.getMaxStackSize()), new ItemStack(Material.WHEAT, Material.WHEAT.getMaxStackSize())}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.DIRT, 3)}, 0.14, 0.141421356F, new String[0], new String[0]));
			challenges.add(new Challenge("apple_farmer", "&cApple &2Farmer", new String[] {"Harvest 32 apples from", "oak trees"}, Material.APPLE, (short) 0, 0, 3, new ItemStack[] {new ItemStack(Material.APPLE, 32)}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.OAK_SAPLING, 1), new ItemStack(Material.BIRCH_SAPLING, 1), new ItemStack(Material.SPRUCE_SAPLING, 1), new ItemStack(Material.JUNGLE_SAPLING, 2), new ItemStack(Material.ACACIA_SAPLING, 1), new ItemStack(Material.DARK_OAK_SAPLING, 2)}, 0.14, 0.141421356F, new String[0], new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.FAST_DIGGING, 300 * 20, 2)}));
			challenges.add(new Challenge("gold_miner", "&6Gold &7Miner", new String[] {"Obtain 12 gold bars"}, Material.GOLD_ORE, (short) 0, 0, 4, new ItemStack[] {new ItemStack(Material.GOLD_INGOT, 12)}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.CLAY_BALL, 16), new ItemStack(Material.COCOA_BEANS, 3), new ItemStack(Material.LILY_PAD, 6), new ItemStack(Material.VINE, 6), new ItemStack(Material.DEAD_BUSH, 4), new ItemStack(Material.SUNFLOWER, 1), new ItemStack(Material.LILAC, 1), new ItemStack(Material.TALL_GRASS, 1), new ItemStack(Material.LARGE_FERN, 1), new ItemStack(Material.ROSE_BUSH, 1), new ItemStack(Material.PEONY, 1)}, 0.14, 0.141421356F, new String[0], new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.HEALTH_BOOST, 300 * 20, 0)}));
			challenges.add(new Challenge("emerald_miner", "&2Emerald &7Miner", new String[] {"Obtain 5 emeralds"}, Material.EMERALD_ORE, (short) 0, 0, 5, new ItemStack[] {new ItemStack(Material.EMERALD, 5)}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.OBSIDIAN, 14)}, 0.14, 0.141421356F, new String[0], new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300 * 20, 0)}));
			challenges.add(new Challenge("diamond_miner", "&bDiamond &7Miner", new String[] {"Obtain 10 diamonds"}, Material.DIAMOND_ORE, (short) 0, 0, 6, new ItemStack[] {new ItemStack(Material.DIAMOND, 10)}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.GRAVEL, 6), new ItemStack(Material.PRISMARINE, 1), new ItemStack(Material.PRISMARINE_BRICKS, 1), new ItemStack(Material.DARK_PRISMARINE, 1), new ItemStack(Material.SEA_LANTERN, 1), new ItemStack(Material.ICE, 2)}, 0.14, 0.141421356F, new String[0], new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300 * 20, 0)}));
			challenges.add(new Challenge("blaze_hunter", "&6Blaze &cHunter", new String[] {"Retrieve 32 blaze rods", "from their owners"}, Material.BLAZE_ROD, (short) 0, 1, 0, new ItemStack[] {new ItemStack(Material.BLAZE_ROD, 32)}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.END_PORTAL_FRAME, 1)}, 0.14, 0.141421356F, new String[0], new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300 * 20, 0), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300 * 20, 0)}));
			challenges.add(new Challenge("homestead", "&6Homestead", new String[] {"Build yourself a nice", "place to live"}, Material.BOOKSHELF, (short) 0, 1, 1, new ItemStack[] {}, false, 0, false, new ItemStack[0], new ItemStack[0], 0.28, 0.282842712F, new String[0], new String[0], new PotionEffect[0]));
			
			getChallengeByName("blaze_hunter", challenges)//
			.addRequiredChallenge(getChallengeByName("cobblestone", challenges), CompletionType.FIRST)//
			.addRequiredChallenge(getChallengeByName("cactus", challenges), CompletionType.FIRST)//
			.addRequiredChallenge(getChallengeByName("wheat_farmer", challenges), CompletionType.FIRST)//
			.addRequiredChallenge(getChallengeByName("apple_farmer", challenges), CompletionType.FIRST)//
			.addRequiredChallenge(getChallengeByName("gold_miner", challenges), CompletionType.FIRST)//
			.addRequiredChallenge(getChallengeByName("emerald_miner", challenges), CompletionType.FIRST)//
			.addRequiredChallenge(getChallengeByName("diamond_miner", challenges), CompletionType.FIRST);
			getChallengeByName("homestead", challenges).firstRewards.requiredChallenges.addAll(getChallengeByName("blaze_hunter", challenges).firstRewards.requiredChallenges);
			saveChallenges(challenges, folder, true);
		}
		return challenges;
	}
	
	/** @return The challenges save folder */
	public static final File getChallengeSaveFolder() {
		File folder = new File(Main.getPlugin().getDataFolder(), "Challenges");
		folder.mkdirs();
		return folder;
	}
	
	/** @return Whether or not all the challenges saved successfully. */
	public static final boolean saveChallenges() {
		boolean success = saveChallengeConfig();
		return success & saveChallenges(challenges, getChallengeSaveFolder(), true);
	}
	
	/** @param challenges The list of challenges to save
	 * @param challengeSaveFolder The folder to save the challenges in
	 * @param deleteAllFilesFirst Whether or not all of the files in the given
	 *            folder whose name ends with &quot;.yml&quot; should be deleted
	 * @return Whether or not all the challenges saved successfully. */
	public static final boolean saveChallenges(Collection<Challenge> challenges, File challengeSaveFolder, boolean deleteAllFilesFirst) {
		challengeSaveFolder.mkdirs();
		boolean success = true;
		if(deleteAllFilesFirst) {
			for(File file : challengeSaveFolder.listFiles()) {
				if(file.isFile() && file.getName().endsWith(".yml")) {
					file.delete();
				}
			}
		}
		for(Challenge challenge : challenges) {
			success &= challenge.saveIn(challengeSaveFolder);
		}
		return success;
	}
	
	public static final int loadChallenges() {
		loadChallengeConfig();
		Challenge.saveDefaultChallenges();
		return loadChallenges(challenges, getChallengeSaveFolder());
	}
	
	public static final int loadChallenges(Collection<Challenge> challenges, File challengeSaveFolder) {
		challenges.clear();
		for(File file : challengeSaveFolder.listFiles()) {
			if(file.isFile() && file.getName().endsWith(".yml")) {
				Challenge challenge = null;
				try {
					challenge = loadFrom(file, challenges);
				} catch(IOException | InvalidConfigurationException ex) {
					Main.getPluginLogger().log(Level.WARNING, "\n /!\\  Unable to load challenge from config file \"".concat(file.getAbsolutePath()).concat("\"\n/___\\ "), ex);
					continue;
				}
				if(challenge != null) {
					challenges.add(challenge);
				} else {
					Main.getPluginLogger().warning("\n /!\\  Unable to load challenge from config file \"".concat(file.getAbsolutePath()).concat("\":\n/___\\ One of the following fields was set improperly: name(type=String); index(type=Integer); difficulty(type=Integer); icon(type=Material ENUM_NAME)\nCheck the file for errors, then reload the plugin settings with \"/esb reload\" to try again."));
				}
			}
		}
		for(Challenge challenge : new ArrayList<>(challenges)) {
			challenge.firstRewards.requiredChallenges.clear();
			for(String requiredChallenge : new ArrayList<>(challenge.firstRewards.loadedRequiredChallenges)) {
				if(challenge.getName().equals(requiredChallenge)) {
					challenge.firstRewards.loadedRequiredChallenges.remove(requiredChallenge);
					//XXX add warning log about challenge requiring itself for first time completion
					continue;
				}
				Challenge requirement = getChallengeByName(requiredChallenge, challenges);
				if(requirement != null) {
					challenge.firstRewards.requiredChallenges.add(requirement);
				} else {
					Main.getPluginLogger().warning("\n /!\\  Unable set a first-time required challenge for challenge \"".concat(challenge.name).concat("\":\n/___\\ Unknown challenge requirement \"").concat(requiredChallenge).concat("\"!"));
				}
			}
			challenge.repeatRewards.requiredChallenges.clear();
			for(String requiredChallenge : new ArrayList<>(challenge.repeatRewards.loadedRequiredChallenges)) {
				if(challenge.getName().equals(requiredChallenge)) {
					challenge.repeatRewards.loadedRequiredChallenges.remove(requiredChallenge);
					//XXX add warning log about challenge requiring itself for repeated completions
					continue;
				}
				Challenge requirement = getChallengeByName(requiredChallenge, challenges);
				if(requirement != null) {
					challenge.repeatRewards.requiredChallenges.add(requirement);
				} else {
					Main.getPluginLogger().warning("\n /!\\  Unable set a repeat-time required challenge for challenge \"".concat(challenge.name).concat("\":\n/___\\ Unknown challenge requirement \"").concat(requiredChallenge).concat("\"!"));
				}
			}
		}
		return challenges.size();
	}
	
	/** @return The inventory title used for the /challenge screen */
	public static final String getChallengeScreenTitle() {
		return ChatColor.GOLD.toString().concat("Island Challenges");
	}
	
	/** @return The inventory title used for the /c editor screen */
	public static final String getManageChallengesTitle() {
		return ChatColor.GOLD.toString().concat("Challenge Editor");
	}
	
	/** @return The inventory title used for the /c showAll */
	public static final String getReviewChallengesTitle() {
		return ChatColor.GOLD.toString().concat("Island Challenges Review");
	}
	
	/** @param playerName The player whose challenge completion states will be
	 *            edited by someone else
	 * @return */
	public static final String getEditPlayerChallengesTitle(String playerName) {
		return ChatColor.GOLD.toString().concat(playerName.length() > (32 - 13) ? (playerName.length() >= (32 - 16) ? playerName.substring(0, playerName.length() - (32 - 16)).concat("...") : playerName.substring(0, playerName.length() - (32 - 13))) : playerName).concat("'s Challenges");
	}
	
	/** @return The size of the inventory used for the challenge screen */
	public static final int getChallengeScreenSize() {
		return Math.min(54, (max_rows + 1) * 9);
	}
	
	/** @param player The player who will be viewing the screen, or
	 *            <b><code>null</code></b> for a generic audience
	 * @return The resulting challenge screen */
	public static final Inventory getChallengeScreen(Player player) {
		return getChallengeScreen(player, false, false, false);
	}
	
	/** @param player The player who will be creating/editing/deleting
	 *            challenges with this screen
	 * @return The resulting challenge screen */
	public static final Inventory getManageChallengesScreen(OfflinePlayer player) {
		return getChallengeScreen(player, true, false, false);
	}
	
	/** @param player The player whose challenge completion states will be
	 *            edited by another player
	 * @return The resulting challenge screen */
	public static final Inventory getReviewChallengesScreen(OfflinePlayer player) {
		return getChallengeScreen(player, false, true, false);
	}
	
	/** @param player The player whose challenge completion states will be
	 *            edited by another player
	 * @return The resulting challenge screen */
	public static final Inventory getEditPlayersChallengesScreen(OfflinePlayer player) {
		return getChallengeScreen(player, false, false, true);
	}
	
	private static final ConcurrentHashMap<String, ConcurrentHashMap<Boolean, Integer>> lastPlayerChallengeScreenPages = new ConcurrentHashMap<>();
	
	public static final int getMaximumChallengeScreenPages(boolean managing) {
		int total = 1;
		if(managing) {
			//TODO
		} else {
			//Probably a faster way to do this, but the above code doesn't account for the main menu sign in the lower right...
			int page = 0, i = 0;
			int invSize = getChallengeScreenSize();
			for(int j = 0; j < challenges.size(); j++) {
				if(i >= invSize - 1) {
					i = 0;
					page++;
				}
				i++;
			}
			total = Math.max(1, page);
		}
		return total;
	}
	
	public static final int getLastChallengeScreenPageFor(OfflinePlayer player, boolean editingPlayers) {
		if(player != null) {
			return getLastChallengeScreenPageFor(player.getUniqueId(), editingPlayers);
		}
		return -1;
	}
	
	public static final int getLastChallengeScreenPageFor(UUID player, boolean editingPlayers) {
		if(player != null) {
			ConcurrentHashMap<Boolean, Integer> editingPlayersOrNormal = lastPlayerChallengeScreenPages.get(player.toString());
			Integer lastPage = (editingPlayersOrNormal = editingPlayersOrNormal == null ? new ConcurrentHashMap<>() : editingPlayersOrNormal).get(Boolean.valueOf(editingPlayers));
			editingPlayersOrNormal.put(Boolean.valueOf(editingPlayers), (lastPage = lastPage == null ? Integer.valueOf(0) : lastPage));
			return lastPage.intValue();
		}
		return -1;
	}
	
	public static final void setLastChallengeScreenPageFor(OfflinePlayer player, int page, boolean editingPlayers) {
		if(player != null) {
			setLastChallengeScreenPageFor(player.getUniqueId(), page, editingPlayers);
		}
	}
	
	public static final void setLastChallengeScreenPageFor(UUID player, int page, boolean editingPlayers) {
		if(player != null) {
			page = Math.max(0, Math.min(getMaximumChallengeScreenPages(editingPlayers), page));
			ConcurrentHashMap<Boolean, Integer> editingPlayersOrNormal = lastPlayerChallengeScreenPages.get(player.toString());
			lastPlayerChallengeScreenPages.put(player.toString(), (editingPlayersOrNormal = editingPlayersOrNormal == null ? new ConcurrentHashMap<>() : editingPlayersOrNormal));
			editingPlayersOrNormal.put(Boolean.valueOf(editingPlayers), Integer.valueOf(page));
		}
	}
	
	public static final Challenge[] getChallengesInSlotOrder() {
		Challenge[] array;// = new Challenge[challenges.size()];
		int arraySize = 0;
		HashMap<Integer, Challenge> map = new HashMap<>();
		for(Challenge challenge : challenges) {
			int slot = challenge.getInvSlot();
			map.put(Integer.valueOf(slot), challenge);
			arraySize = slot + 1 > arraySize ? slot + 1 : arraySize;
		}
		array = new Challenge[arraySize];
		for(int i = 0; i < array.length; i++) {
			array[i] = map.get(Integer.valueOf(i));
		}
		return array;
	}
	
	public static final Challenge[] getChallengesOnScreenPageRaw(int page) {
		final int invSize = getChallengeScreenSize();
		Challenge[] array = new Challenge[invSize];
		int curPage = 0, i = 0;
		for(Challenge challenge : getChallengesInSlotOrder()) {
			if(i >= invSize) {
				i = 0;
				curPage++;
			}
			if(page == curPage && challenge != null) {
				int slot = challenge.getInvSlot() % invSize;
				if(slot >= invSize - 9 && slot <= invSize - 1) {
					Main.getPluginLogger().warning(ChatColor.YELLOW.toString().concat("The challenge \"").concat(challenge.getName()).concat("\" is using inv slot ").concat(Integer.toString(challenge.getInvSlot())).concat("; which is a slot reserved for the GUI navigation buttons!\nThis challenge won't show up in the GUI, and therefore will only be usable via commands.\nTo fix this, change the challenge's index and/or difficulty and then type \"/esb reload\" to reload all challenges from file."));
					i++;
					continue;
				}
				if(slot >= array.length) {//Shouldn't happen, but...
					Main.getPluginLogger().warning(ChatColor.RED.toString().concat("\n /!\\  Oops! Brian made a mistake when coding his skyblock plugin: getChallengesOnScreenPage(").concat(Integer.toString(page)).concat("): invSize: ").concat(Integer.toString(invSize)).concat("; array.length: ").concat(Integer.toString(array.length)).concat("; slot: ").concat(Integer.toString(slot)).concat("; curPage: ").concat(Integer.toString(curPage)).concat("; i: ").concat(Integer.toString(i)).concat(";\n/___\\ Send this debug message to him so he can fix it please."));
					i++;
					continue;
				}
				array[slot] = challenge;
			}
			i++;
		}
		return array;
	}
	
	public static final List<Challenge> getChallengesOnScreenPage(int page) {
		List<Challenge> list = new ArrayList<>();
		for(Challenge challenge : getChallengesOnScreenPageRaw(page)) {
			if(challenge != null) {
				list.add(challenge);
			}
		}
		return list;
	}
	
	/** @param page The zero-based challenge screen page
	 * @return Whether or not this challenge is on the given page */
	public final boolean isOnChallengeScreenPage(int page) {
		final int invSize = getChallengeScreenSize();
		int curPage = 0, i = 0;
		for(Challenge challenge : getChallengesInSlotOrder()) {
			if(i >= invSize) {
				i = 0;
				curPage++;
			}
			if(challenge == this) {
				return page == curPage;
			}
			i++;
		}
		return false;
	}
	
	/** @param player The player
	 * @param managing Whether or not the given player will be managing
	 *            challenges with this screen
	 * @param viewing Whether or not the given player will just be viewing all
	 *            challenges(challenges will all be unlocked, but not usable)
	 * @param editingPlayers Whether or not a player will be editing the given
	 *            player's challenge completion states
	 * @return The resulting challenge screen */
	public static final Inventory getChallengeScreen(OfflinePlayer player, boolean managing, boolean viewing, boolean editingPlayers) {
		//Island island = Island.getIslandFor(player);
		int maxPages = getMaximumChallengeScreenPages(managing);
		int page = getLastChallengeScreenPageFor(player, managing);
		String title = getChallengeScreenTitle();
		title = viewing ? getReviewChallengesTitle() : title;
		title = editingPlayers ? getEditPlayerChallengesTitle(player.getName()) : title;
		title = managing ? getManageChallengesTitle() : title;
		Inventory screen = Main.server.createInventory(player == null ? null : (player.isOnline() ? player.getPlayer() : new PlayerAdapter(player.getUniqueId(), player.getName(), player.getBedSpawnLocation(), GameMode.SURVIVAL)), getChallengeScreenSize(), title);
		if(managing) {
			//TODO
			InventoryGUI.setMainMenuSign(screen, screen.getSize() - 1);
		} else {
			for(Challenge challenge : getChallengesOnScreenPage(page)) {
				ItemStack icon = viewing ? challenge.getIcon(Integer.MIN_VALUE) : challenge.getIconFor(player);//ItemStack icon = challenge.getIcon(island == null ? false : island.hasMemberCompleted(player, challenge));
				screen.setItem(challenge.getInvSlot(), icon);
			}
			for(int difficulty = 0; difficulty < max_rows; difficulty++) {
				int slot = 8 + (9 * difficulty);
				screen.setItem(slot, getDifficultyIcon(difficulty));
			}
			// Page/Island Menu Navigation
			if(page == maxPages - 1) {
				InventoryGUI.setMainMenuSign(screen, screen.getSize() - (page == 0 ? 9 : 1));
			} else {
				if(page == 0) {
					InventoryGUI.setMainMenuSign(screen, screen.getSize() - 9);
					if(maxPages > 1) {
						InventoryGUI.setPageButton(screen, screen.getSize() - 1, true, page);
					}//else remove screen.getSize() - 1
					
				} else {
					if(page < maxPages - 1) {
						InventoryGUI.setPageButton(screen, screen.getSize() - 1, true, page);
					}//else remove screen.getSize() - 1
					InventoryGUI.setPageButton(screen, screen.getSize() - 9, false, page);
				}
			}
		}
		return screen;
	}
	
	/** @return A list containing all of the currently loaded challenges */
	public static final Collection<Challenge> getChallenges() {
		return new ArrayList<>(challenges);
	}
	
	/** @param challengeName The challenge to get
	 * @return The challenge matching the given name, or
	 *         <b><code>null</code></b> if none matched */
	public static final Challenge getChallengeByName(String challengeName) {
		return getChallengeByName(challengeName, null);
	}
	
	public static final Challenge getChallengeByName(String challengeName, Collection<Challenge> challenges) {
		challenges = challenges == null ? Challenge.challenges : challenges;
		for(Challenge challenge : challenges) {
			if(challenge.getName().equalsIgnoreCase(challengeName)) {
				return challenge;
			}
		}
		return null;
	}
	
	/** @param slot The slot that was selected
	 * @return The challenge with the given slot number, or
	 *         <b><code>null</code></b> if none matched */
	public static final Challenge getChallengeBySlot(int slot) {
		return getChallengeBySlot(slot, null);
	}
	
	public static final Challenge getChallengeBySlot(int slot, Collection<Challenge> challenges) {
		challenges = challenges == null ? Challenge.challenges : challenges;
		for(Challenge challenge : challenges) {
			if(challenge.getInvSlot() == slot) {
				return challenge;
			}
		}
		return null;
	}
	
	/** @param difficulty The difficulty(or tier) that is desired(horizontal
	 *            columns)
	 * @return All challenges with the given difficulty, or an empty list if
	 *         none matched */
	public static final List<Challenge> getChallengesByDifficulty(int difficulty) {
		return getChallengesByDifficulty(difficulty, null);
	}
	
	public static final List<Challenge> getChallengesByDifficulty(int difficulty, Collection<Challenge> challenges) {
		challenges = challenges == null ? Challenge.challenges : challenges;
		List<Challenge> list = new ArrayList<>();
		for(Challenge challenge : challenges) {
			if(challenge.difficulty == difficulty) {
				list.add(challenge);
			}
		}
		return list;
	}
	
	/** @param index The index that is desired(vertical rows)
	 * @return All challenges with the given index, or an empty list if
	 *         none matched */
	public static final List<Challenge> getChallengesByIndex(int index) {
		return getChallengesByIndex(index, null);
	}
	
	public static final List<Challenge> getChallengesByIndex(int index, Collection<Challenge> challenges) {
		challenges = challenges == null ? Challenge.challenges : challenges;
		List<Challenge> list = new ArrayList<>();
		for(Challenge challenge : challenges) {
			if(challenge.index == index) {
				list.add(challenge);
			}
		}
		return list;
	}
	
	private volatile String name;
	private volatile String displayName;
	private volatile String[] description = new String[0];
	private volatile Material icon;
	private volatile short iconData = 0x0;
	private volatile int difficulty = 0x0, index = 0x0;
	private volatile boolean repeatable = false;
	
	protected final ChallengeInfo firstRewards;
	protected final ChallengeInfo repeatRewards;
	
	public static enum CompletionType {
		FIRST,
		REPEAT;
	}
	
	protected static final class ChallengeInfo {
		
		protected volatile double requiredLevel = 0x0.0p0;
		protected volatile boolean takeItems = false;
		protected volatile ItemStack[] requiredItems = new ItemStack[0];
		protected volatile ItemStack[] requiredBlocks = new ItemStack[0];
		
		protected final ConcurrentLinkedDeque<Challenge> requiredChallenges = new ConcurrentLinkedDeque<>();
		protected final ArrayList<String> loadedRequiredChallenges = new ArrayList<>();
		
		protected volatile ItemStack[] rewardItems = new ItemStack[0];
		protected volatile float rewardExp = 0x0.0p0F;
		protected volatile PotionEffect[] rewardEffects = new PotionEffect[0];
		protected volatile double rewardMoney = 0x0.0p0;
		protected volatile String[] rewardPermissions = new String[0];
		protected volatile String[] rewardCommands = new String[0];
		
		public Challenge getParentChallenge() {
			for(Challenge challenge : Challenge.getChallenges()) {
				if(challenge.firstRewards == this || challenge.repeatRewards == this) {
					return challenge;
				}
			}
			return null;
		}
		
		protected final void saveTo(ConfigurationSection mem, CompletionType type) {
			type.name();
			ConfigurationSection sec = mem.getConfigurationSection(type == CompletionType.REPEAT ? "repeatRewards" : "firstRewards");
			sec = sec == null ? mem.createSection(type == CompletionType.REPEAT ? "repeatRewards" : "firstRewards") : sec;
			Challenge parent = this.getParentChallenge();
			for(Challenge challenge : this.requiredChallenges) {
				if(challenge == parent || challenge.getName().equals(parent.getName())) {
					this.requiredChallenges.remove(challenge);
					continue;
				}
				if(!this.loadedRequiredChallenges.contains(challenge.getName())) {
					this.loadedRequiredChallenges.add(challenge.getName());
				}
			}
			for(String requiredChallenge : this.loadedRequiredChallenges) {
				if(parent.getName().equals(requiredChallenge)) {
					this.loadedRequiredChallenges.remove(requiredChallenge);
					continue;
				}
			}
			
			sec.set("requiredLevel", Double.valueOf(this.requiredLevel));
			sec.set("takeItems", Boolean.valueOf(this.takeItems));
			saveItemStackArrayTo(sec, "requiredItems", this.requiredItems);
			saveItemStackArrayTo(sec, "requiredBlocks", this.requiredBlocks);
			sec.set("requiredChallenges", this.loadedRequiredChallenges);
			saveItemStackArrayTo(sec, "rewardItems", this.rewardItems);
			sec.set("rewardExp", Double.valueOf(this.rewardExp));
			saveConfigSerializableArrayTo(sec, "rewardEffects", this.rewardEffects);//sec.set("rewardEffects", Arrays.asList(this.rewardEffects));
			sec.set("rewardMoney", Double.valueOf(this.rewardMoney));
			sec.set("rewardPermissions", Arrays.asList(this.rewardPermissions));
			sec.set("rewardCommands", Arrays.asList(this.rewardCommands));
			
		}
		
		protected final void loadFrom(ConfigurationSection mem, CompletionType type) {
			type.name();
			ConfigurationSection sec = mem.getConfigurationSection(type == CompletionType.REPEAT ? "repeatRewards" : "firstRewards");
			if(sec != null) {
				this.requiredLevel = sec.getDouble("requiredLevel", 0x0.0p0);
				this.takeItems = sec.getBoolean("takeItems", false);
				this.requiredItems = loadItemStackArrayFrom(sec, "requiredItems", new ItemStack[0]);
				this.requiredBlocks = loadItemStackArrayFrom(sec, "requiredBlocks", new ItemStack[0]);
				
				this.loadedRequiredChallenges.addAll(sec.getStringList("requiredChallenges"));
				
				this.rewardItems = loadItemStackArrayFrom(sec, "rewardItems", new ItemStack[0]);
				this.rewardExp = Double.valueOf(sec.getDouble("rewardExp", 0x0.0p0)).floatValue();
				this.rewardEffects = loadConfigSerializableArrayFrom(sec, "rewardEffects", new PotionEffect[0]);
				this.rewardMoney = sec.getDouble("rewardMoney", 0x0.0p0);
				this.rewardPermissions = sec.getStringList("rewardPermissions").toArray(new String[sec.getStringList("rewardPermissions").size()]);
				this.rewardCommands = sec.getStringList("rewardCommands").toArray(new String[sec.getStringList("rewardCommands").size()]);
				
			}
		}
		
	}
	
	public final File getFile(File folder) {
		return new File(folder, StringUtil.makeFileNameFileSystemSafe(this.name.concat(".yml")));
	}
	
	public final boolean saveIn(File folder) {
		File file = this.getFile(folder);
		YamlConfiguration config = new YamlConfiguration();
		this.saveTo(config);
		try {
			config.save(file);
			return true;
		} catch(IOException ex) {
			Main.getPluginLogger().log(Level.WARNING, "Unable to save challenge \"".concat(this.name).concat("\" to file \"").concat(file.getAbsolutePath()).concat("\""), ex);
			return false;
		}
	}
	
	public final void saveTo(ConfigurationSection mem) {
		mem.set("name", this.name);
		mem.set("displayName", this.displayName);
		mem.set("description", Arrays.asList(this.description));
		mem.set("icon", this.icon.name());
		mem.set("iconData", Integer.valueOf(this.iconData));
		mem.set("difficulty", Integer.valueOf(this.difficulty));
		mem.set("index", Integer.valueOf(this.index));
		mem.set("repeatable", Boolean.valueOf(this.repeatable));
		
		this.firstRewards.saveTo(mem, CompletionType.FIRST);
		this.repeatRewards.saveTo(mem, CompletionType.REPEAT);
	}
	
	public static final void saveItemStackArrayTo(ConfigurationSection mem, String name, ItemStack[] items) {
		ConfigurationSection sec = mem.getConfigurationSection(name);
		sec = sec == null ? mem.createSection(name) : sec;
		int i = 0;
		for(ItemStack item : items) {
			if(item == null) {
				continue;
			}
			sec.set(Integer.toString(i++), item);
		}
	}
	
	public static final ItemStack[] loadItemStackArrayFrom(ConfigurationSection mem, String name, ItemStack... def) {
		ConfigurationSection sec = mem.getConfigurationSection(name);
		ItemStack[] items = null;
		if(sec != null) {
			ArrayList<ItemStack> list = new ArrayList<>();
			for(String key : sec.getKeys(false)) {
				ItemStack item = sec.getItemStack(key, null);
				if(item != null) {
					list.add(item);
				}
			}
			items = list.toArray(new ItemStack[list.size()]);
		}
		return items == null || items.length == 0 ? def : items;
	}
	
	public static final <T extends ConfigurationSerializable> void saveConfigSerializableArrayTo(ConfigurationSection mem, String name, T[] items) {
		ConfigurationSection sec = mem.getConfigurationSection(name);
		sec = sec == null ? mem.createSection(name) : sec;
		int i = 0;
		for(T item : items) {
			if(item == null) {
				continue;
			}
			sec.set(Integer.toString(i++), item);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static final <T extends ConfigurationSerializable> T[] loadConfigSerializableArrayFrom(ConfigurationSection mem, String name, T... def) {
		ConfigurationSection sec = mem.getConfigurationSection(name);
		T[] items = null;
		if(sec != null) {
			ArrayList<T> list = new ArrayList<>();
			for(String key : sec.getKeys(false)) {
				Object obj = sec.get(key, null);
				if(obj != null && obj instanceof ConfigurationSerializable) {
					list.add((T) obj);
				}
			}
			Class<?> clazz = list.isEmpty() ? null : list.get(0).getClass();
			if(clazz != null) {
				items = (T[]) Array.newInstance(clazz, list.size());
			}
			if(items != null && items.length == list.size()) {
				for(int i = 0; i < items.length; i++) {
					items[i] = list.get(i);
				}
			}
		}
		return items == null || items.length == 0 ? def : items;
	}
	
	public static final Challenge loadFrom(File file, Collection<Challenge> challenges) throws IOException, InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();
		config.load(file);
		Challenge challenge = loadFrom(config);
		if(challenge == null) {
			return null;
		}
		Challenge checkDuplicatedSlot = getChallengeBySlot(challenge.getInvSlot(), challenges);
		if(checkDuplicatedSlot != null) {
			throw new InvalidConfigurationException("Challenge \"".concat(challenge.name).concat("\" is attempting to use the inv slot ").concat(Integer.toString(challenge.getInvSlot())).concat("(difficulty=").concat(Integer.toString(challenge.difficulty)).concat(", index=").concat(Integer.toString(challenge.index)).concat("), which is already in use by challenge \"").concat(checkDuplicatedSlot.name).concat("\"!"));
		}
		return challenge;
	}
	
	public static final Challenge loadFrom(ConfigurationSection mem) {
		String name = mem.getString("name", null);
		String displayName = mem.getString("displayName", name);
		String[] description = mem.getStringList("description").toArray(new String[mem.getStringList("description").size()]);
		short iconData = Integer.valueOf(mem.getInt("iconData", 0x0)).shortValue();
		int difficulty = mem.getInt("difficulty", Integer.MAX_VALUE);
		int index = mem.getInt("index", Integer.MAX_VALUE);
		boolean repeatable = mem.getBoolean("repeatable", false);
		
		Material icon = Material.getMaterial(mem.getString("icon", ""), false);
		if(name == null || index == Integer.MAX_VALUE || difficulty == Integer.MAX_VALUE || icon == null) {
			return null;
		}
		
		ChallengeInfo firstRewards = new ChallengeInfo();
		ChallengeInfo repeatRewards = new ChallengeInfo();
		firstRewards.loadFrom(mem, CompletionType.FIRST);
		repeatRewards.loadFrom(mem, CompletionType.REPEAT);
		
		return new Challenge(name, displayName, description, icon, iconData, difficulty, index, repeatable, firstRewards, repeatRewards);
	}
	
	/** @param commandName This challenge's configuration/command line name
	 * @param displayName This challenge's display name
	 * @param description This challenge's description
	 * @param icon This challenge's material icon(used in the challenge
	 *            inventory screen)
	 * @param iconData This challenge's icon data(used in the challenge
	 *            inventory screen)
	 * @param difficulty This challenge's difficulty level. 0 = easy, 1 =
	 *            medium, 2 = hard, 3 = expert, 4 = master
	 * @param index The index for this challenge
	 * @param requiredItems Any items that are required to complete this
	 *            challenge
	 * @param takeItems Whether or not the required items will be taken - if
	 *            this is false, and there are required items, this challenge
	 *            will be unrepeatable, regardless of the
	 *            <code>repeatable</code> parameter
	 * @param requiredLevel The Island level required to complete this challenge
	 *            - zero for no level required
	 * @param repeatable Whether or not this challenge is repeatable - note that
	 *            this is overridden in the event that blocks are required to
	 *            complete this challenge, as blocks cannot be taken
	 * @param requiredBlocks Any blocks that are required to be on the Island
	 *            for this challenge to be completable
	 * @param rewardItems Any items given to the player as a reward for
	 *            completing this challenge
	 * @param rewardMoney Money given to the player as a reward for completing
	 *            this challenge
	 * @param rewardPermissions Special permissions granted to the player as a
	 *            reward for completing this challenge
	 * @param rewardEffects Effects given to the player as a reward for
	 *            completing this challenge */
	protected Challenge(String commandName, String displayName, String[] description, Material icon, short iconData, int difficulty, int index, ItemStack[] requiredItems, boolean takeItems, double requiredLevel, boolean repeatable, ItemStack[] requiredBlocks, ItemStack[] rewardItems, double rewardMoney, float rewardExp, String[] rewardPermissions, String[] rewardCommands, PotionEffect... rewardEffects) {
		this.name = commandName;
		this.displayName = displayName == null || displayName.trim().isEmpty() ? this.name : displayName;
		this.description = description;
		this.icon = icon == null ? Material.STONE : icon;
		this.iconData = iconData;
		this.difficulty = Math.max(0, Math.min(max_rows - 1, difficulty));
		this.index = Math.max(0, Math.min(8, index));
		this.repeatable = repeatable;
		this.firstRewards = new ChallengeInfo();
		this.firstRewards.requiredItems = Main.clean(requiredItems);
		this.firstRewards.takeItems = takeItems;
		this.firstRewards.requiredLevel = requiredLevel;
		this.firstRewards.requiredBlocks = Main.clean(requiredBlocks);
		this.firstRewards.rewardItems = Main.clean(rewardItems);
		this.firstRewards.rewardMoney = Math.abs(rewardMoney);
		this.firstRewards.rewardExp = Math.abs(rewardExp);
		this.firstRewards.rewardPermissions = Main.clean(rewardPermissions);
		this.firstRewards.rewardCommands = Main.clean(rewardCommands);
		this.firstRewards.rewardEffects = Main.clean(rewardEffects);
		if(this.firstRewards.requiredBlocks.length > 0 || this.firstRewards.requiredLevel > 0x0.0p0) {
			this.repeatable = this.firstRewards.requiredItems.length > 0 && this.firstRewards.takeItems ? repeatable : false;
		}
		if(this.firstRewards.requiredItems.length > 0 && !this.firstRewards.takeItems) {
			this.repeatable = false;
		}
		if(this.firstRewards.rewardPermissions.length > 0) {
			this.repeatable = false;
		}
		this.repeatRewards = new ChallengeInfo();
	}
	
	/** @param commandName This challenge's configuration/command line name
	 * @param displayName This challenge's display name
	 * @param description This challenge's description
	 * @param icon This challenge's material icon(used in the challenge
	 *            inventory screen)
	 * @param iconData This challenge's icon data(used in the challenge
	 *            inventory screen)
	 * @param difficulty This challenge's difficulty level. 0 = easy, 1 =
	 *            medium, 2 = hard, 3 = expert, 4 = master
	 * @param index The index for this challenge
	 * @param repeatable Whether or not this challenge is repeatable - note that
	 *            this is overridden in the event that blocks are required to
	 *            complete this challenge, as blocks cannot be taken
	 * @param firstRewards The requirements and rewards that will affect players
	 *            who have never completed this challenge before
	 * @param repeatRewards The requirements and rewards that will affect
	 *            players who have already completed this challenge at least
	 *            once */
	public Challenge(String commandName, String displayName, String[] description, Material icon, short iconData, int difficulty, int index, boolean repeatable, ChallengeInfo firstRewards, ChallengeInfo repeatRewards) {
		this.name = commandName;
		this.displayName = displayName;
		this.description = description;
		this.icon = icon;
		this.iconData = iconData;
		this.difficulty = difficulty;
		this.index = index;
		this.repeatable = repeatable;
		this.firstRewards = firstRewards;
		this.repeatRewards = repeatRewards;
	}
	
	/** @param challenge The challenge to check
	 * @param type The challenge completion type(information about the challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return Whether or not this challenge requires the given one to be
	 *         completed in order to be completable */
	public boolean doesRequire(Challenge challenge, CompletionType type) {
		type.name();
		switch(type) {
		case FIRST:
			return this.firstRewards.requiredChallenges.contains(challenge);
		case REPEAT:
			return this.repeatRewards.requiredChallenges.contains(challenge);
		default:
			throw new IllegalStateException("Unknown/unimplemented CompletionType: ".concat(type.name()));
		}
	}
	
	/** @param requirement The challenge to add as a requirement
	 * @param type The challenge completion type(information about the challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return This challenge */
	public Challenge addRequiredChallenge(Challenge requirement, CompletionType type) {
		if(requirement == null || requirement == this || requirement.doesRequire(this, type) || this.doesRequire(requirement, type)) {
			return this;
		}
		(type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards).requiredChallenges.addLast(requirement);
		return this;
	}
	
	/** @param requirement The challenge to remove as a requirement
	 * @param type The challenge completion type(information about the challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return This challenge */
	public Challenge removeRequiredChallenge(Challenge requirement, CompletionType type) {
		if(requirement == null) {
			return this;
		}
		ChallengeInfo info = type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards;
		while(info.requiredChallenges.contains(requirement)) {
			info.requiredChallenges.remove(requirement);
		}
		return this;
	}
	
	private static final ItemStack[] safeCopy(ItemStack[] copy) {
		ItemStack[] items = new ItemStack[copy.length];
		int i = 0;
		for(ItemStack item : copy) {
			items[i++] = new ItemStack(item);
		}
		return items;
	}
	
	/** @return This challenge's configuration/command line name */
	public final String getName() {
		return this.name;
	}
	
	/** @return This challenge's display name */
	public final String getDisplayName() {
		return ChatColor.translateAlternateColorCodes('&', this.displayName);
	}
	
	/** @return The slot that this challenge will appear in in the challenge
	 *         inventory screen */
	public final int getInvSlot() {
		return this.index + (this.difficulty * 9);
	}
	
	public final int getInvSlotModInvSize() {
		return this.getInvSlot() % getChallengeScreenSize();
	}
	
	public static final boolean isInvSlotReserved(int invSlot, boolean manage) {
		if(manage) {
			
			return false;
		}
		int invSize = getChallengeScreenSize();
		int slot = invSlot % invSize;
		if(slot == invSize - 1 || slot == invSize - 9) {
			return true;
		}
		return false;
	}
	
	public static final String getDefaultDifficultyTitle(int difficulty) {
		switch(difficulty) {
		default:
			return ChatColor.WHITE.toString().concat(ChatColor.BOLD.toString()).concat("Non-existant Challenge Tier(Error!)");
		case 0://Easy
			return ChatColor.GREEN.toString().concat(ChatColor.BOLD.toString()).concat("Easy Challenge Tier");
		case 1://Medium
			return ChatColor.YELLOW.toString().concat(ChatColor.BOLD.toString()).concat("Medium Challenge Tier");
		case 2://Hard
			return ChatColor.GOLD.toString().concat(ChatColor.BOLD.toString()).concat("Hard Challenge Tier");
		case 3://Expert
			return ChatColor.DARK_RED.toString().concat(ChatColor.BOLD.toString()).concat("Expert Challenge Tier");
		case 4://Master
			return ChatColor.DARK_PURPLE.toString().concat(ChatColor.BOLD.toString()).concat("Master Challenge Tier");
		case 5://Impossible
			return ChatColor.DARK_GRAY.toString().concat(ChatColor.BOLD.toString()).concat("(Im)possible Challenge Tier");
		}
	}
	
	public static final String getDifficultyTitle(int difficulty) {
		String def = getDefaultDifficultyTitle(difficulty);
		return getChallengeConfig().getString("difficulty.".concat(Integer.toString(difficulty)).concat(".title"), def);
	}
	
	public static final List<String> getDefaultDifficultyDescription(int difficulty) {
		String[] desc;
		switch(difficulty) {
		default://Error
			desc = new String[] {//
					ChatColor.WHITE.toString().concat("This is the non-existent tier of challenges."),//
					ChatColor.BOLD.toString().concat("(Contact a staff member for assistance; this is an error.)")
			};
			break;
		case 0://Easy
			desc = new String[] {//
					ChatColor.GREEN.toString().concat("Challenges in this tier are the easiest"),//
					ChatColor.GREEN.toString().concat("to complete, and serve to help you get"),//
					ChatColor.GREEN.toString().concat("your island started."),//
			};
			break;
		case 1://Medium
			desc = new String[] {//
					ChatColor.YELLOW.toString().concat("Challenges in this tier take a little"),//
					ChatColor.YELLOW.toString().concat("bit of effort to complete."),//
			};
			break;
		case 2://Hard
			desc = new String[] {//
					ChatColor.GOLD.toString().concat("Challenges in this tier are tougher to"),//
					ChatColor.GOLD.toString().concat("complete. They may take a longer time to"),//
					ChatColor.GOLD.toString().concat("complete, but they'll be worth it!"),//
			};
			break;
		case 3://Expert
			desc = new String[] {//
					ChatColor.DARK_RED.toString().concat("These challenges are not for the ametuer"),//
					ChatColor.DARK_RED.toString().concat("skyblocker, as they require that you put"),//
					ChatColor.DARK_RED.toString().concat("in a lot of work and time. Once completed,"),//
					ChatColor.DARK_RED.toString().concat("you'll be the envy of other players!"),//
			};
			break;
		case 4://Master
			desc = new String[] {//
					ChatColor.DARK_PURPLE.toString().concat("These challenges are so hard that you have"),//
					ChatColor.DARK_PURPLE.toString().concat("to be ").concat(ChatColor.BOLD.toString()).concat("very").concat(ChatColor.RESET.toString()).concat(ChatColor.DARK_PURPLE.toString()).concat(" dedicated to completing them."),//
					ChatColor.DARK_PURPLE.toString().concat("Once completed, bragging rights won't be the"),//
					ChatColor.DARK_PURPLE.toString().concat("only thing in your possession for sure!"),//
			};
			break;
		case 5://Impossible
			desc = new String[] {//
					ChatColor.DARK_GRAY.toString().concat("These challenges are so hard that you"),//
					ChatColor.DARK_GRAY.toString().concat("might say they're impossible. They aren't"),//
					ChatColor.DARK_GRAY.toString().concat("called challenges for nothing!"),//
			};
			break;
		}
		return Arrays.asList(desc);
	}
	
	public static final List<String> getDifficultyDescription(int difficulty) {
		List<String> def = getDefaultDifficultyDescription(difficulty);
		List<String> list = getChallengeConfig().getStringList("difficulty.".concat(Integer.toString(difficulty)).concat(".description"));
		return list.isEmpty() ? def : list;
	}
	
	public final ItemStack getLockedIcon() {
		List<String> lore = new ArrayList<>();
		Material material;
		String tierOrRow = Challenge.challengeDifficultiesAreHalfPercentagedTiers() ? "tier" : "row";
		String unlockMsg1 = "Unlock at least half of the previous";
		String unlockMsg2 = tierOrRow.concat(" of challenges to unlock these.");
		lore.add(getDifficultyTitle(this.difficulty));
		switch(this.difficulty) {
		case 0://Easy
		default:
			material = Material.LIME_STAINED_GLASS_PANE;
			lore.add(ChatColor.GREEN.toString().concat("This ").concat(getDifficultyName(this.difficulty).toLowerCase()).concat(" challenge is locked for some reason. Hrrm."));
			lore.add(ChatColor.GREEN.toString().concat("Unlock at least half of the non-existant"));
			lore.add(ChatColor.GREEN.toString().concat("previous ").concat(tierOrRow).concat(" of challenges to unlock these."));
			lore.add(ChatColor.WHITE.toString().concat(ChatColor.BOLD.toString()).concat("(Contact a staff member for assistance; this is an error.)"));
			break;
		case 1://Medium
			material = Material.YELLOW_STAINED_GLASS_PANE;
			lore.add(ChatColor.YELLOW.toString().concat("This ").concat(getDifficultyName(this.difficulty).toLowerCase()).concat(" challenge is locked."));
			lore.add(ChatColor.YELLOW.toString().concat(unlockMsg1));
			lore.add(ChatColor.YELLOW.toString().concat(unlockMsg2));
			break;
		case 2://Hard
			material = Material.ORANGE_STAINED_GLASS_PANE;
			lore.add(ChatColor.GOLD.toString().concat("This ").concat(getDifficultyName(this.difficulty).toLowerCase()).concat(" challenge is locked."));
			lore.add(ChatColor.GOLD.toString().concat(unlockMsg1));
			lore.add(ChatColor.GOLD.toString().concat(unlockMsg2));
			break;
		case 3://Expert
			material = Material.RED_STAINED_GLASS_PANE;
			lore.add(ChatColor.DARK_RED.toString().concat("This ").concat(getDifficultyName(this.difficulty).toLowerCase()).concat(" challenge is locked."));
			lore.add(ChatColor.DARK_RED.toString().concat(unlockMsg1));
			lore.add(ChatColor.DARK_RED.toString().concat(unlockMsg2));
			break;
		case 4://Master
			material = Material.PURPLE_STAINED_GLASS_PANE;
			lore.add(ChatColor.DARK_PURPLE.toString().concat("This ").concat(getDifficultyName(this.difficulty).toLowerCase()).concat(" challenge is locked."));
			lore.add(ChatColor.DARK_PURPLE.toString().concat(unlockMsg1));
			lore.add(ChatColor.DARK_PURPLE.toString().concat(unlockMsg2));
			break;
		case 5://Impossible
			material = Material.BLACK_STAINED_GLASS_PANE;
			lore.add(ChatColor.DARK_GRAY.toString().concat("This ").concat(getDifficultyName(this.difficulty).toLowerCase()).concat(" challenge is locked."));
			lore.add(ChatColor.DARK_GRAY.toString().concat(unlockMsg1));
			lore.add(ChatColor.DARK_GRAY.toString().concat(unlockMsg2));
			break;
		}
		ItemStack icon = new ItemStack(material, 1);
		ItemMeta meta = Main.server.getItemFactory().getItemMeta(this.icon);
		meta.setDisplayName(this.getDisplayName().concat(ChatColor.RESET.toString()).concat(ChatColor.RED.toString()).concat(" - Locked"));
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.values());//lol, even though all I needed to put here was ItemFlag.HIDE_ENCHANTS
		icon.setItemMeta(meta);
		return icon;
	}
	
	public static final int getNumberOfChallengesInDifficulty(int difficulty) {
		return Challenge.getChallengesByDifficulty(difficulty).size();
	}
	
	public static final int getMinimumNumberOfChallengesRequiredToCompleteDifficulty(int totalChallengesInTier) {//(int tier) {
		if(!Challenge.challengeDifficultiesAreHalfPercentagedTiers()) {
			return 0;
		}
		return (totalChallengesInTier / 2) + (totalChallengesInTier % 2 == 0 ? 0 : 1);
	}
	
	public static final void main(String[] args) {
		for(int i = 9; i >= 0; i--) {
			System.out.println(Integer.toString(i).concat(": ").concat(Integer.toString(getMinimumNumberOfChallengesRequiredToCompleteDifficulty(i))));
		}
	}
	
	public static final int getNumberOfChallengesLeftUntilDifficultyIsComplete(Player player, int difficulty, boolean fully) {
		int totalChallenges = 0, numCompleted = 0;
		for(Challenge challenge : Challenge.getChallengesByDifficulty(difficulty)) {
			totalChallenges++;
			if(challenge.hasCompleted(player)) {
				numCompleted++;
			}
		}
		return (fully ? totalChallenges : getMinimumNumberOfChallengesRequiredToCompleteDifficulty(totalChallenges)) - numCompleted;
	}
	
	public static final boolean hasCompletedDifficulty(OfflinePlayer player, int difficulty, boolean fully) {
		if(!Challenge.challengeDifficultiesAreHalfPercentagedTiers() || difficulty < 0) {
			return true;
		}
		List<Challenge> previousTiers = Challenge.getChallengesByDifficulty(difficulty);
		int totalChallenges = 0, numCompleted = 0;
		for(Challenge challenge : previousTiers) {
			totalChallenges++;
			if(challenge.hasCompleted(player)) {
				numCompleted++;
			}
		}
		return numCompleted >= (fully ? totalChallenges : getMinimumNumberOfChallengesRequiredToCompleteDifficulty(totalChallenges));
	}
	
	public static final boolean hasUnlockedNextDifficulty(OfflinePlayer player, int difficulty) {
		return hasCompletedDifficulty(player, difficulty, false);
	}
	
	public static final ItemStack getDifficultyIcon(int difficulty) {
		if(difficulty < 0) {
			return null;
		}
		List<String> lore = new ArrayList<>();
		Material material;
		switch(difficulty) {
		case 0://Easy
		default:
			material = Material.LIME_STAINED_GLASS_PANE;
			break;
		case 1://Medium
			material = Material.YELLOW_STAINED_GLASS_PANE;
			break;
		case 2://Hard
			material = Material.ORANGE_STAINED_GLASS_PANE;
			break;
		case 3://Expert
			material = Material.RED_STAINED_GLASS_PANE;
			break;
		case 4://Master
			material = Material.PURPLE_STAINED_GLASS_PANE;
			break;
		case 5://Impossible
			material = Material.BLACK_STAINED_GLASS_PANE;
			break;
		}
		lore.addAll(getDifficultyDescription(difficulty));
		ItemStack icon = new ItemStack(material, 1);
		ItemMeta meta = Main.server.getItemFactory().getItemMeta(material);
		meta.setDisplayName(getDifficultyTitle(difficulty));
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.values());//lol, even though all I needed to put here was ItemFlag.HIDE_ENCHANTS
		icon.setItemMeta(meta);
		return icon;
	}
	
	/** @param player The player whose challenge icon will be returned
	 * @return The icon representing this challenge */
	public final ItemStack getIconFor(OfflinePlayer player) {
		if(player != null) {
			Island island = Island.getMainIslandFor(player);//Island.getIslandPlayerIsUsing(player, true);
			if(island == null) {
				return this.getLockedIcon();
			}
			if(this.difficulty > 0 && Challenge.challengeDifficultiesAreHalfPercentagedTiers()) {
				if(!hasUnlockedNextDifficulty(player, this.difficulty - 1)) {
					return this.getLockedIcon();
				}
			}
		}
		return this.getIcon(this.getTimesCompleted(player));//this.hasCompleted(player));
	}
	
	/** @param player The player to use
	 * @return The number of times that the given player has completed this
	 *         challenge */
	public final int getTimesCompleted(OfflinePlayer player) {
		Island island = Island.getMainIslandFor(player);
		if(island == null) {
			return -1;
		}
		return island.getNumTimesChallengeCompletedBy(player.getUniqueId(), this.name);
	}
	
	/** @param timesCompleted The number of times that the player viewing the
	 *            challenge screen has completed this challenge
	 * @return The icon representing this challenge */
	public final ItemStack getIcon(int timesCompleted) {
		boolean completed = timesCompleted >= 1;
		@SuppressWarnings("deprecation")
		ItemStack icon = new ItemStack(this.icon, 1, this.iconData);
		ItemMeta meta = Main.server.getItemFactory().getItemMeta(this.icon);
		meta.setDisplayName(this.getDisplayName());
		List<String> lore = new ArrayList<>();
		for(String line : this.description) {
			lore.add(ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', line));
		}
		
		lore.add(this.repeatable ? ChatColor.AQUA.toString().concat("Times completed: ").concat(ChatColor.GOLD.toString()).concat(Integer.toString(timesCompleted)) : ChatColor.DARK_GRAY.toString().concat("Not repeatable."));
		
		//==
		lore.add(ChatColor.GRAY + "Requirements:");
		if(this.repeatable) {
			String[] firstRequirements = this.getRequirements(CompletionType.FIRST);
			String[] repeatRequirements = this.getRequirements(CompletionType.REPEAT);
			boolean firstRequirementsAreSameAsRepeat = repeatRequirements.length == firstRequirements.length;
			if(firstRequirementsAreSameAsRepeat) {
				for(int i = 1; i < repeatRequirements.length; i++) {//skip the first line; they're always different between FIRST and REPEAT
					if(i == 0) {
						continue;
					}
					firstRequirementsAreSameAsRepeat &= repeatRequirements[i].equals(firstRequirements[i]);
				}
			}
			if(!firstRequirementsAreSameAsRepeat) {
				if(timesCompleted <= 0 || timesCompleted == Integer.MIN_VALUE) {
					for(String requirement : firstRequirements) {
						lore.add(requirement);
					}
				}
				if(timesCompleted >= 1 || timesCompleted == Integer.MIN_VALUE) {
					for(String requirement : repeatRequirements) {
						lore.add(requirement);
					}
				}
			} else {
				for(int i = 1; i < firstRequirements.length; i++) {
					if(i == 0) {
						continue;
					}
					lore.add(firstRequirements[i]);
				}
			}
		} else {
			String[] firstRequirements = this.getRequirements(CompletionType.FIRST);
			for(int i = 1; i < firstRequirements.length; i++) {
				if(i == 0) {
					continue;
				}
				lore.add(firstRequirements[i]);
			}
		}
		//==
		
		//==
		lore.add(ChatColor.GOLD + "Rewards:");
		if(this.repeatable) {
			String[] firstRewards = this.getRewards(CompletionType.FIRST);
			String[] repeatRewards = this.getRewards(CompletionType.REPEAT);
			boolean firstRewardsAreSameAsRepeat = repeatRewards.length == firstRewards.length;
			if(firstRewardsAreSameAsRepeat) {
				for(int i = 1; i < repeatRewards.length; i++) {//skip the first line; they're always different between FIRST and REPEAT
					if(i == 0) {
						continue;
					}
					firstRewardsAreSameAsRepeat &= repeatRewards[i].equals(firstRewards[i]);
				}
			}
			if(!firstRewardsAreSameAsRepeat) {
				if(timesCompleted <= 0 || timesCompleted == Integer.MIN_VALUE) {
					for(String reward : firstRewards) {
						lore.add(reward);
					}
				}
				if(timesCompleted >= 1 || timesCompleted == Integer.MIN_VALUE) {
					for(String reward : repeatRewards) {
						lore.add(reward);
					}
				}
			} else {
				for(int i = 1; i < firstRewards.length; i++) {
					if(i == 0) {
						continue;
					}
					lore.add(firstRewards[i]);
				}
			}
		} else {
			String[] firstRewards = this.getRewards(CompletionType.FIRST);
			for(int i = 1; i < firstRewards.length; i++) {
				if(i == 0) {
					continue;
				}
				lore.add(firstRewards[i]);
			}
		}
		
		//==
		
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.values());//lol, even though all I needed to put here was ItemFlag.HIDE_ENCHANTS
		if(completed) {
			meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		}
		icon.setItemMeta(meta);
		if(completed) {
			icon.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
		}
		return icon;
	}
	
	/** @param type The challenge completion type(information about the
	 *            challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return An array containing the requirements of this challenge */
	public final String[] getRequirements(CompletionType type) {
		ChallengeInfo info = type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards;
		String[] requirements = new String[1];
		requirements[0] = ChatColor.GRAY + (type == CompletionType.FIRST ? "[First time completion]" : "[Repeated completions]");
		if(!info.requiredChallenges.isEmpty()) {
			requirements = Arrays.copyOf(requirements, requirements.length + 1);
			requirements[requirements.length - 1] = ChatColor.DARK_GRAY + "Required Challenges:";
			for(Challenge challenge : info.requiredChallenges) {
				requirements = Arrays.copyOf(requirements, requirements.length + 1);
				requirements[requirements.length - 1] = ChatColor.GRAY + "* " + ChatColor.WHITE + challenge.getDisplayName();
			}
		}
		if(info.requiredLevel > 0x0.0p0) {
			requirements = Arrays.copyOf(requirements, requirements.length + 1);
			requirements[requirements.length - 1] = ChatColor.BLUE + "An island level of " + ChatColor.GOLD + Main.limitDecimalToNumberOfPlaces(info.requiredLevel, 2) + ChatColor.BLUE + " or higher";
		}
		if(info.requiredItems.length > 0) {
			requirements = Arrays.copyOf(requirements, requirements.length + 1);
			requirements[requirements.length - 1] = ChatColor.GREEN + "Items:";
			for(ItemStack item : info.requiredItems) {
				requirements = Arrays.copyOf(requirements, requirements.length + 1);
				requirements[requirements.length - 1] = ChatColor.GRAY + "* " + ChatColor.WHITE + Main.getItemName(item) + ChatColor.WHITE + " x" + item.getAmount();
			}
		}
		if(info.requiredBlocks.length > 0) {
			requirements = Arrays.copyOf(requirements, requirements.length + 1);
			requirements[requirements.length - 1] = ChatColor.LIGHT_PURPLE + "Blocks(placed or held):";
			for(ItemStack item : info.requiredBlocks) {
				requirements = Arrays.copyOf(requirements, requirements.length + 1);
				requirements[requirements.length - 1] = ChatColor.GRAY + "* " + ChatColor.WHITE + Main.getItemName(item) + ChatColor.WHITE + " x" + item.getAmount();
			}
		}
		if(requirements.length == 1) {
			requirements = new String[] {requirements[0], ChatColor.GRAY + "None."};
		}
		return requirements;
	}
	
	/** @param type The challenge completion type(information about the
	 *            challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return An array containing the rewards for completing this challenge */
	public final String[] getRewards(CompletionType type) {
		ChallengeInfo info = type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards;
		String[] rewards = new String[1];
		rewards[0] = ChatColor.GOLD + (type == CompletionType.FIRST ? "[First time completion]" : "[Repeated competions]");
		if(info.rewardItems.length > 0) {
			rewards = Arrays.copyOf(rewards, rewards.length + 1);
			rewards[rewards.length - 1] = ChatColor.GREEN + "Items:";
			for(ItemStack item : info.rewardItems) {
				rewards = Arrays.copyOf(rewards, rewards.length + 1);
				rewards[rewards.length - 1] = ChatColor.DARK_GREEN + "* " + ChatColor.WHITE + Main.getItemName(item) + ChatColor.WHITE + " x" + item.getAmount();
			}
		}
		if(info.rewardExp > 0x0.0p0F) {
			rewards = Arrays.copyOf(rewards, rewards.length + 1);
			rewards[rewards.length - 1] = ChatColor.YELLOW + "Experience: " + ChatColor.GREEN + Main.limitDecimalToNumberOfPlaces(info.rewardExp, 2);
		}
		if(info.rewardMoney > 0x0.0p0) {
			rewards = Arrays.copyOf(rewards, rewards.length + 1);
			rewards[rewards.length - 1] = ChatColor.GREEN + "Money: " + ChatColor.GOLD + Main.limitDecimalToNumberOfPlaces(info.rewardMoney, 2);
		}
		if(info.rewardEffects.length > 0) {
			rewards = Arrays.copyOf(rewards, rewards.length + 1);
			rewards[rewards.length - 1] = ChatColor.GREEN + "Temporary effects:";
			for(PotionEffect effect : info.rewardEffects) {
				String name = Main.capitalizeFirstLetterOfEachWord(effect.getType().getName().toLowerCase(), '_', ' ');
				String duration = Main.getLengthOfTime(effect.getDuration() * 50L);
				String amplifier = Main.toRomanNumerals(effect.getAmplifier() + 1);
				rewards = Arrays.copyOf(rewards, rewards.length + 1);
				rewards[rewards.length - 1] = ChatColor.DARK_GREEN + "* " + ChatColor.YELLOW + name + " " + ChatColor.GOLD + amplifier + ChatColor.DARK_GREEN + " for " + ChatColor.YELLOW + duration;
			}
		}
		if(info.rewardPermissions.length > 0) {
			rewards = Arrays.copyOf(rewards, rewards.length + 1);
			rewards[rewards.length - 1] = ChatColor.GOLD + "Permissions:";
			for(String permission : info.rewardPermissions) {
				rewards = Arrays.copyOf(rewards, rewards.length + 1);
				rewards[rewards.length - 1] = ChatColor.YELLOW + "\"" + ChatColor.WHITE + permission + ChatColor.YELLOW + "\"";
			}
		}
		if(info.rewardCommands.length > 0) {
			rewards = Arrays.copyOf(rewards, rewards.length + 1);
			rewards[rewards.length - 1] = ChatColor.GOLD + "Commands to be executed: " + ChatColor.AQUA + Integer.toString(info.rewardCommands.length) + ChatColor.GOLD + " Command" + (info.rewardCommands.length == 1 ? "" : "s");
		}
		if(rewards.length == 1) {
			rewards = new String[] {rewards[0], ChatColor.BLUE + "None."};
		}
		return rewards;
	}
	
	/** @return The index of this challenge(used to determine display order when
	 *         displaying this challenge in the challenge inventory screen) */
	public final int getIndex() {
		return this.index;
	}
	
	/** @return This challenge's difficulty level */
	public final int getDifficulty() {
		return this.difficulty;
	}
	
	/** @return This challenge's difficulty level, in readable format */
	public final String getDifficultyString() {
		return getDifficultyName(this.difficulty);
	}
	
	public final String getDifficultyTitle() {
		return getDifficultyTitle(this.difficulty);
	}
	
	public final List<String> getDifficultyDescription() {
		return getDifficultyDescription(this.difficulty);
	}
	
	/** @param difficulty The difficulty level to translate
	 * @return The translated level */
	public static final String getDefaultDifficultyName(int difficulty) {
		difficulty = Math.max(0, Math.min(4, difficulty));
		switch(difficulty) {
		case 0:
		default:
			return "Easy";
		case 1:
			return "Medium";
		case 2:
			return "Hard";
		case 3:
			return "Expert";
		case 4:
			return "Master";
		}
	}
	
	public static final String getDifficultyName(int difficulty) {
		ConfigurationSection mem = getChallengeConfig();
		return mem.getString("difficulty.".concat(Integer.toString(difficulty)).concat(".name"), getDefaultDifficultyName(difficulty));
	}
	
	/** @param type
	 * @return The items required to be in a player's inventory in order to
	 *         complete this challenge */
	public final ItemStack[] getRequiredItems(CompletionType type) {
		type.name();
		return safeCopy((type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards).requiredItems);
	}
	
	/** @param type The challenge completion type(information about the
	 *            challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return Whether or not this challenge will take the required items from
	 *         a player's inventory */
	public final boolean doesTakeItems(CompletionType type) {
		type.name();
		return (type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards).takeItems;
	}
	
	/** @param type The challenge completion type(information about the
	 *            challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return The island level that a player is required to be at in order
	 *         to complete this challenge */
	public final double getRequiredLevel(CompletionType type) {
		type.name();
		return (type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards).requiredLevel;
	}
	
	/** @return Whether or not this challenge is repeatable */
	public final boolean isRepeatable() {
		return this.repeatable;
	}
	
	/** @param type The challenge completion type(information about the
	 *            challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return The blocks required to be placed on a player's island in order
	 *         to complete this challenge */
	public final ItemStack[] getRequiredBlocks(CompletionType type) {
		type.name();
		return safeCopy((type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards).requiredBlocks);
	}
	
	/** @param type The challenge completion type(information about the
	 *            challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return The items that will be awarded to a player that completes this
	 *         challenge */
	public final ItemStack[] getRewardItems(CompletionType type) {
		type.name();
		return safeCopy((type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards).rewardItems);
	}
	
	/** @param type The challenge completion type(information about the
	 *            challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return The money that will be awarded to a player that completes this
	 *         challenge */
	public final double getRewardMoney(CompletionType type) {
		type.name();
		return (type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards).rewardMoney;
	}
	
	/** @param type The challenge completion type(information about the
	 *            challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return The amount of experience that will be awarded to a player when
	 *         they complete this challenge */
	public final float getRewardExperience(CompletionType type) {
		type.name();
		ChallengeInfo info = type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards;
		return info.rewardExp;// - Math.round(info.rewardExp);
	}
	
	/** @param type The challenge completion type(information about the
	 *            challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return The special permissions that will be awarded to the player that
	 *         completes this challenge */
	public final String[] getRewardPermissions(CompletionType type) {
		type.name();
		return (type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards).rewardPermissions;
	}
	
	/** Command replacement strings:<br>
	 * <tt>%UUID% - the player's UUID<br>
	 * %NAME% - the player's plain text name<br>
	 * %LOCATION% - the player's current location(replacement example: "world=worldName,x=X,y=Y,z=Z,yaw=Yaw,pitch=Pitch"; without quotes)<br>
	 * %GAMEMODE% - the player's current gamemode<br>
	 * %REPEATABLE% - this challenge's repeatable state(replacement example: One of: "true", "false"; without quotes)<br>
	 * %ISLANDID% - the island that the player is currently on's ID(replacement example: "1_2"; without quotes)<br>
	 * %ISLANDSPAWN% - the island that the player is currently on's spawn location(replacement example: see location above)<br>
	 * %ISLANDWARP% - the island that the player is currently on's warp location(replacement example: see location above)<br>
	 * %ISLANDLOC% - the island that the player is currently on's starting obsidian location(replacement example: see location above)<br>
	 * %ISLANDHOME% - the player's home location for the island that the player is currently on(replacement example: see location above)<br>
	 * </tt>
	 * Add <tt>MAIN</tt> onto the front of an island variable name to
	 * distinguish between the player's main island(the first island that<br>
	 * the player created[or joined if the player owns no islands]) and the
	 * island that the player is currently playing on(may be the same as the<br>
	 * main island if the player isn't on a secondary island).
	 * 
	 * 
	 * @param type The challenge completion type(information about the challenge
	 *            when completing it the first time, or repeating the challenge
	 *            after already having completed it)
	 * @return The special commands that will be executed via the server console
	 *         when a player completes this challenge */
	public final String[] getRewardCommands(CompletionType type) {
		type.name();
		return (type == CompletionType.FIRST ? this.firstRewards : this.repeatRewards).rewardCommands;
	}
	
	/** @param offlinePlayer The player to check
	 * @return A list containing the items that this player needs to get before
	 *         they can complete this challenge */
	public final List<ItemStack> getRemainingRequiredItems(OfflinePlayer offlinePlayer) {
		if(!offlinePlayer.isOnline()) {
			return null;
		}
		Player player = offlinePlayer.getPlayer();
		ChallengeInfo info = this.hasCompleted(player) ? this.repeatRewards : this.firstRewards;
		ArrayList<ItemStack> requiredItems = new ArrayList<>();
		for(ItemStack required : info.requiredItems) {
			requiredItems.add(new ItemStack(required));
		}
		if(requiredItems.isEmpty()) {
			return requiredItems;
		}
		Main.sendDebugMsg(player, "RequiredItems: " + requiredItems.size());
		for(ItemStack required : requiredItems) {
			Main.sendDebugMsg(player, Main.getItemName(required) + " x" + required.getAmount());
		}
		for(ItemStack item : player.getInventory().getContents()) {
			if(item == null) {
				continue;
			}
			for(ItemStack required : new ArrayList<>(requiredItems)) {
				if(Main.isSameType(required, item)) {
					int newAmt = required.getAmount() - item.getAmount();
					if(newAmt > 0) {
						Main.sendDebugMsg(player, "Reduced requirement " + Main.getItemName(required) + " from x" + required.getAmount() + " to x" + newAmt + " and item amount: " + item.getAmount());
						required.setAmount(newAmt);
						break;
					}
					requiredItems.remove(required);
					Main.sendDebugMsg(player, "Removed requirement " + Main.getItemName(required) + " x" + required.getAmount() + " and item amount: " + item.getAmount());
					break;
				}
			}
		}
		Main.sendDebugMsg(player, "RequiredItems: " + requiredItems.size());
		for(ItemStack required : requiredItems) {
			Main.sendDebugMsg(player, Main.getItemName(required) + " x" + required.getAmount());
		}
		for(ItemStack required : new ArrayList<>(requiredItems)) {
			if(required.getAmount() <= 0) {
				requiredItems.remove(required);
			}
		}
		Main.sendDebugMsg(player, "RequiredItems: " + requiredItems.size());
		for(ItemStack required : requiredItems) {
			Main.sendDebugMsg(player, Main.getItemName(required) + " x" + required.getAmount());
		}
		Main.sendDebugMsg(player, "Items left: " + requiredItems.size());
		for(ItemStack requirement : requiredItems) {
			Main.sendDebugMsg(player, requirement.getType().name().toLowerCase().replace('_', ' ') + "x" + requirement.getAmount());
		}
		return requiredItems;
	}
	
	/** @param player The player to check
	 * @return Whether or not the player has all of the required items in order
	 *         to complete this challenge */
	public boolean hasRequiredItems(OfflinePlayer player) {
		if(!player.isOnline()) {
			return false;
		}
		return this.getRemainingRequiredItems(player).isEmpty();
	}
	
	/** @param offlinePlayer The player to check
	 * @return A list of the blocks that this player still has to obtain before
	 *         they can complete this challenge */
	public final List<ItemStack> getRemainingRequiredBlocks(OfflinePlayer offlinePlayer) {
		if(!offlinePlayer.isOnline()) {
			return null;
		}
		Player player = offlinePlayer.getPlayer();
		ChallengeInfo info = this.hasCompleted(player) ? this.repeatRewards : this.firstRewards;
		ArrayList<ItemStack> requiredBlocks = new ArrayList<>();
		for(ItemStack required : info.requiredBlocks) {
			requiredBlocks.add(required);
		}
		if(requiredBlocks.isEmpty()) {
			return requiredBlocks;
		}
		for(ItemStack item : player.getInventory().getContents()) {
			if(item == null) {
				continue;
			}
			for(ItemStack required : new ArrayList<>(requiredBlocks)) {
				if(Main.isSameType(required, item)) {
					int newAmt = required.getAmount() - item.getAmount();
					if(newAmt > 0) {
						required.setAmount(newAmt);
					} else {
						requiredBlocks.remove(required);
					}
					break;
				}
			}
		}
		for(ItemStack required : new ArrayList<>(requiredBlocks)) {
			if(required.getAmount() <= 0) {
				requiredBlocks.remove(required);
			}
		}
		if(!requiredBlocks.isEmpty()) {
			Island island = Island.getMainIslandFor(player);
			if(island != null) {
				int[] bounds = island.getBounds();
				for(int x = bounds[0]; x <= bounds[2]; x++) {
					for(int y = 0; y < GeneratorMain.getSkyworld().getMaxHeight(); y++) {
						for(int z = bounds[1]; z <= bounds[3]; z++) {
							Block block = GeneratorMain.getSkyworld().getBlockAt(new Location(GeneratorMain.getSkyworld(), x, y, z, 0.0F, 0.0F));
							if(block != null && block.getType() != Material.AIR) {
								@SuppressWarnings("deprecation")
								ItemStack item = new ItemStack(block.getType(), 1, block.getData());
								for(ItemStack required : new ArrayList<>(requiredBlocks)) {
									if(Main.isSameType(required, item)) {
										int newAmt = required.getAmount() - item.getAmount();
										if(newAmt > 0) {
											required.setAmount(newAmt);
										} else {
											requiredBlocks.remove(required);
										}
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		for(ItemStack required : new ArrayList<>(requiredBlocks)) {
			if(required.getAmount() <= 0) {
				requiredBlocks.remove(required);
			}
		}
		return requiredBlocks;
	}
	
	/** @param player The player to check
	 * @return Whether or not the player has all of the required blocks in order
	 *         to complete this challenge. The blocks may either be in the
	 *         player's inventory, or placed anywhere on their island */
	public boolean hasRequiredBlocks(OfflinePlayer player) {
		if(!player.isOnline()) {
			return false;
		}
		return this.getRemainingRequiredBlocks(player).isEmpty();
	}
	
	/** @param player The player to check
	 * @return The remaining amount of level(s) that this player needs to obtain
	 *         before they can complete this challenge */
	public double getRemainingRequiredLevel(OfflinePlayer player) {
		ChallengeInfo info = this.hasCompleted(player) ? this.repeatRewards : this.firstRewards;
		Island island = Island.getMainIslandFor(player);
		return this.hasRequiredLevel(player) ? 0 : (island == null ? -1 : info.requiredLevel - island.getLevel());
	}
	
	/** @param player The player whose Island will be checked
	 * @return Whether or not the player's Island is of the required level */
	public boolean hasRequiredLevel(OfflinePlayer player) {
		ChallengeInfo info = this.hasCompleted(player) ? this.repeatRewards : this.firstRewards;
		Island island = Island.getMainIslandFor(player);
		if(island != null) {
			return info.requiredLevel > 0x0.0p0 ? island.getLevel() >= info.requiredLevel : true;
		}
		return false;
	}
	
	/** @param player The player to check
	 * @return Whether or not the given player has completed this challenge
	 *         before */
	public final boolean hasCompleted(OfflinePlayer player) {
		Island island = Island.getMainIslandFor(player);
		return island == null ? false : island.hasMemberCompleted(player, this);
	}
	
	/** @param player The player to check
	 * @return Whether or not the player can complete this challenge */
	public boolean canComplete(Player player) {
		if(!this.isRepeatable() && this.hasCompleted(player)) {
			return false;
		}
		ChallengeInfo info = this.hasCompleted(player) ? this.repeatRewards : this.firstRewards;
		Island island = Island.getMainIslandFor(player);
		boolean repeatCheck = this.isRepeatable() ? true : !island.hasMemberCompleted(player, this);
		boolean world = Island.isInSkyworld(player);
		boolean items = this.hasRequiredItems(player);
		boolean blocks = this.hasRequiredBlocks(player);
		boolean level = this.hasRequiredLevel(player);
		boolean requiredChallenges = true;
		for(Challenge requirement : info.requiredChallenges) {
			if(!island.hasMemberCompleted(player, requirement)) {
				requiredChallenges = false;
				break;
			}
		}
		Main.sendDebugMsg(player, "Island: " + (island != null) + " world: " + world + " items: " + items + " blocks: " + blocks + " level: " + level + " requiredChallenges: " + requiredChallenges + " ");
		return island != null && repeatCheck && world && items && blocks && level && requiredChallenges;
	}
	
	/** @param player The player whose items matching this challenge's required
	 *            items will be taken */
	public final void takeItems(Player player) {
		ChallengeInfo info = this.hasCompleted(player) ? this.repeatRewards : this.firstRewards;
		if(info.takeItems) {
			ItemStack[] required = safeCopy(info.requiredItems);
			for(int index = 0; index < required.length; index++) {
				ItemStack take = required[index];
				if(take == null) {
					continue;
				}
				for(int i = 0; i < player.getInventory().getSize(); i++) {
					ItemStack item = player.getInventory().getItem(i);
					if(item == null) {
						continue;
					}
					if(Main.isSameType(item, take)) {
						if(item.getAmount() > take.getAmount()) {
							item.setAmount(item.getAmount() - take.getAmount());
							required[index] = null;
							player.getInventory().setItem(i, item);
							break;
						}
						if(item.getAmount() < take.getAmount()) {
							take.setAmount(take.getAmount() - item.getAmount());
							player.getInventory().setItem(i, new ItemStack(Material.AIR));
							required[index] = take;
						} else if(item.getAmount() == take.getAmount()) {
							required[index] = null;
							player.getInventory().setItem(i, new ItemStack(Material.AIR));
							break;
						}
					}
				}
			}
			player.updateInventory();
		}
	}
	
	/** @param player The player who will attempt to complete this challenge
	 * @return Whether or not the player successfully completed this
	 *         challenge */
	public boolean complete(Player player) {
		if(this.canComplete(player)) {
			int tier = this.difficulty;
			boolean completedTier = Challenge.hasCompletedDifficulty(player, tier, false);
			if(!ChallengeCompleteEvent.fire(player, this).isCancelled()) {
				this.takeItems(player);
				this.reward(player);
				Island island = Island.getMainIslandFor(player);
				if(island != null) {
					island.setCompleted(player, this);
				}
				
				Main.sendDebugMsg(player, "completedTier_Before: ".concat(Boolean.toString(completedTier)));
				if(!completedTier && Challenge.hasCompletedDifficulty(player, tier, false)) {
					Main.sendDebugMsg(player, "completedTier_After: true");
					ChallengeTierCompleteEvent completeTierEvent = new ChallengeTierCompleteEvent(this, !Challenge.hasCompletedDifficulty(player, tier, true), player);
					Main.pluginMgr.callEvent(completeTierEvent);
					Main.sendDebugMsg(player, "called event ChallengeTierCompleteEvent!");
				} else {
					Main.sendDebugMsg(player, "completedTier_After: false");
				}
			}
			return true;
		}
		return false;
	}
	
	/** @param player The player who will receive all of this challenge's
	 *            rewards */
	public final void reward(Player player) {
		ChallengeInfo info = this.hasCompleted(player) ? this.repeatRewards : this.firstRewards;
		if(info.rewardExp > 0x0.0p0F) {
			/*float exp = player.getExp() + this.getRewardExperience(info == this.firstRewards ? CompletionType.FIRST : CompletionType.REPEAT);
			while(true) {
				if(exp > 1.0F) {
					exp -= 1.0F;
					player.setLevel(player.getLevel() + 1);
				} else {
					player.setExp(exp);
					break;
				}
			}*/
			player.giveExp(Math.round(info.rewardExp));
		}
		for(PotionEffect effect : info.rewardEffects) {
			PotionEffect current = player.getPotionEffect(effect.getType());
			if(current != null && current.getAmplifier() <= effect.getAmplifier()) {
				effect = new PotionEffect(effect.getType(), Math.min(effect.getDuration() + current.getDuration(), 24000), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles());
			}
			player.addPotionEffect(effect, true);
		}
		if(Main.isVaultEnabled() && info.rewardPermissions.length > 0) {
			net.milkbowl.vault.permission.Permission perm = VaultHandler.getPermissionsHandler();
			if(perm != null) {
				for(String permission : info.rewardPermissions) {
					perm.playerAdd(GeneratorMain.getSkyworld().getName(), player, permission);
				}
			}
		}
		Main.giveItemToPlayer(player, info.rewardItems);
		if(Main.isVaultEnabled() && info.rewardMoney > 0x0.0p0) {
			net.milkbowl.vault.economy.Economy eco = VaultHandler.getEconomyHandler();
			if(eco != null) {
				eco.depositPlayer(player, GeneratorMain.getSkyworld().getName(), info.rewardMoney);
			}
		}
		
		Island island = Island.getIslandContaining(player.getLocation());
		Island mainIsland = Island.getMainIslandFor(player);
		island = island == null || !island.isMember(player) ? mainIsland : island;
		for(String cmd : info.rewardCommands) {
			cmd = cmd.replace("%UUID", player.getUniqueId().toString())//
					.replace("%NAME%", player.getName())//
					.replace("%DISPLAYNAME%", player.getDisplayName())//
					.replace("%LOCATION%", Main.locationToString(player.getLocation(), 8, true, true).replace(", ", ","))//
					.replace("%GAMEMODE%", player.getGameMode().name())//
					.replace("%REPEATABLE%", Boolean.toString(this.repeatable))//
					.replace("%ISLANDID%", island == null ? "null" : island.getID());//
			if(island != null) {
				cmd = cmd.replace("%ISLANDSPAWN%", Main.locationToString(island.getSpawnLocation(), 8, true, true).replace(", ", ","))//
						.replace("%ISLANDWARP%", Main.locationToString(island.getWarpLocation(), 8, true, true).replace(", ", ","))//
						.replace("%ISLANDLOC%", Main.locationToString(island.getLocation(), 8, true, true).replace(", ", ","))//
						.replace("%ISLANDHOME%", Main.locationToString(island.getHomeFor(player.getUniqueId()), 8, true, true).replace(", ", ","));
			} else {
				cmd = cmd.replace("%ISLANDSPAWN%", "null")//
						.replace("%ISLANDWARP%", "null")//
						.replace("%ISLANDLOC%", "null")//
						.replace("%ISLANDHOME%", "null");
			}
			if(mainIsland != null) {
				cmd = cmd.replace("%MAINISLANDSPAWN%", Main.locationToString(mainIsland.getSpawnLocation(), 8, true, true).replace(", ", ","))//
						.replace("%MAINISLANDWARP%", Main.locationToString(mainIsland.getWarpLocation(), 8, true, true).replace(", ", ","))//
						.replace("%MAINISLANDLOC%", Main.locationToString(mainIsland.getLocation(), 8, true, true).replace(", ", ","))//
						.replace("%MAINISLANDHOME%", Main.locationToString(mainIsland.getHomeFor(player.getUniqueId()), 8, true, true).replace(", ", ","));
			} else {
				cmd = cmd.replace("%MAINISLANDSPAWN%", "null")//
						.replace("%MAINISLANDWARP%", "null")//
						.replace("%MAINISLANDLOC%", "null")//
						.replace("%MAINISLANDHOME%", "null");
			}
			Main.server.dispatchCommand(Main.console, cmd);
		}
	}
	
}
