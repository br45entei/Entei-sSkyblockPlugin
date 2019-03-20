package com.gmail.br45entei.enteisSkyblock.challenge;

import com.gmail.br45entei.enteisSkyblock.event.ChallengeCompleteEvent;
import com.gmail.br45entei.enteisSkyblock.main.Island;
import com.gmail.br45entei.enteisSkyblock.main.Main;
import com.gmail.br45entei.enteisSkyblock.vault.VaultHandler;
import com.gmail.br45entei.enteisSkyblockGenerator.main.GeneratorMain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
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
				if(args.length >= 1 && args[0].equalsIgnoreCase("canComplete")) {
					if(args.length == 2) {
						if(user == null) {
							sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
							sender.sendMessage(ChatColor.YELLOW + "Alternately, you may manage players' challenges via:");
							sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + "/" + command + " canComplete {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
							sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + "/" + command + " takeItems {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
							sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + "/" + command + " reward {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
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
					}
					if(!sender.hasPermission("challenge.manage")) {
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
					if(!sender.hasPermission("challenge.manage")) {
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
					if(!sender.hasPermission("challenge.manage")) {
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
					sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
					sender.sendMessage(ChatColor.YELLOW + "Alternately, you may manage players' challenges via:");
					sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + "/" + command + " canComplete {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
					sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + "/" + command + " takeItems {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
					sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.WHITE + "/" + command + " reward {playerName} {challengeName}" + ChatColor.YELLOW + "\".");
					return true;
				}
				if(!Island.isInSkyworld(user)) {
					sender.sendMessage(ChatColor.RED + "This command can only be used in the skyworld!");
					return true;
				}
				if(args.length == 0) {
					user.openInventory(Challenge.getChallengeScreen(user));
				} else if(args.length == 2) {
					if(args[0].equalsIgnoreCase("complete")) {
						Challenge challenge = Challenge.getChallengeByName(args[1]);
						if(challenge == null) {
							sender.sendMessage(ChatColor.RED + "There is no challenge with the name \"" + ChatColor.WHITE + args[1] + ChatColor.RESET + ChatColor.RED + "\".");
							return true;
						}
						Island island = Island.getIslandFor(user);
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
					sender.sendMessage(ChatColor.YELLOW + "Usage: \"" + ChatColor.WHITE + "/" + command + " complete {challengeName}" + ChatColor.YELLOW + "\".");
					return true;
				}
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
	private static final int max_levels = 5;
	
	static {
		challenges.add(new Challenge("cobblestone", "&8Cobblestone &7Generator", new String[] {"Mine a stack of cobble from a generator"}, Material.COBBLESTONE, (short) 0, 0, 0, new ItemStack[] {new ItemStack(Material.COBBLESTONE, Material.COBBLESTONE.getMaxStackSize())}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.IRON_NUGGET, 3)}, 0.14, 0.141421356F, new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.FAST_DIGGING, 300 * 20, 2)}));
		challenges.add(new Challenge("cactus", "&aCactus &6Farmer", new String[] {"Harvest an entire stack of cacti"}, Material.CACTUS, (short) 0, 0, 1, new ItemStack[] {new ItemStack(Material.CACTUS, Material.CACTUS.getMaxStackSize())}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.SAND, 3)}, 0.14, 0.141421356F, new String[0]));
		challenges.add(new Challenge("wheat_farmer", "&2Wheat &6Farmer", new String[] {"Harvest three stacks of wheat"}, Material.HAY_BLOCK, (short) 0, 0, 2, new ItemStack[] {new ItemStack(Material.WHEAT, Material.WHEAT.getMaxStackSize()), new ItemStack(Material.WHEAT, Material.WHEAT.getMaxStackSize()), new ItemStack(Material.WHEAT, Material.WHEAT.getMaxStackSize())}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.DIRT, 3)}, 0.14, 0.141421356F, new String[0]));
		challenges.add(new Challenge("apple_farmer", "&cApple &2Farmer", new String[] {"Harvest 32 apples from", "oak trees"}, Material.APPLE, (short) 0, 0, 3, new ItemStack[] {new ItemStack(Material.APPLE, 32)}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.OAK_SAPLING, 1), new ItemStack(Material.BIRCH_SAPLING, 1), new ItemStack(Material.SPRUCE_SAPLING, 1), new ItemStack(Material.JUNGLE_SAPLING, 2), new ItemStack(Material.ACACIA_SAPLING, 1), new ItemStack(Material.DARK_OAK_SAPLING, 2)}, 0.14, 0.141421356F, new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.FAST_DIGGING, 300 * 20, 2)}));
		challenges.add(new Challenge("gold_miner", "&6Gold &7Miner", new String[] {"Obtain 12 gold bars from", "mining cobblestone"}, Material.GOLD_ORE, (short) 0, 0, 4, new ItemStack[] {new ItemStack(Material.GOLD_INGOT, 12)}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.CLAY_BALL, 16), new ItemStack(Material.LEGACY_INK_SACK, 3, (short) 3), new ItemStack(Material.LILY_PAD, 6), new ItemStack(Material.VINE, 6), new ItemStack(Material.DEAD_BUSH, 4), new ItemStack(Material.LEGACY_DOUBLE_PLANT, 1, (short) 0), new ItemStack(Material.LEGACY_DOUBLE_PLANT, 1, (short) 1), new ItemStack(Material.LEGACY_DOUBLE_PLANT, 1, (short) 2), new ItemStack(Material.LEGACY_DOUBLE_PLANT, 1, (short) 3), new ItemStack(Material.LEGACY_DOUBLE_PLANT, 1, (short) 4), new ItemStack(Material.LEGACY_DOUBLE_PLANT, 1, (short) 5)}, 0.14, 0.141421356F, new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.HEALTH_BOOST, 300 * 20, 0)}));
		challenges.add(new Challenge("emerald_miner", "&2Emerald &7Miner", new String[] {"Obtain 5 emeralds from", "mining cobblestone"}, Material.EMERALD_ORE, (short) 0, 0, 5, new ItemStack[] {new ItemStack(Material.EMERALD, 5)}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.OBSIDIAN, 14)}, 0.14, 0.141421356F, new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300 * 20, 0)}));
		challenges.add(new Challenge("diamond_miner", "&bDiamond &7Miner", new String[] {"Obtain 10 diamonds from", "mining cobblestone"}, Material.DIAMOND_ORE, (short) 0, 0, 6, new ItemStack[] {new ItemStack(Material.DIAMOND, 10)}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.GRAVEL, 6), new ItemStack(Material.PRISMARINE, 1), new ItemStack(Material.PRISMARINE, 1, (short) 1), new ItemStack(Material.PRISMARINE, 1, (short) 2), new ItemStack(Material.SEA_LANTERN, 1), new ItemStack(Material.ICE, 2)}, 0.14, 0.141421356F, new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300 * 20, 0)}));
		challenges.add(new Challenge("blaze_hunter", "&6Blaze &cHunter", new String[] {"Retrieve 32 blaze rods", "from their owners"}, Material.BLAZE_ROD, (short) 0, 1, 0, new ItemStack[] {new ItemStack(Material.BLAZE_ROD, 32)}, true, 0, true, new ItemStack[0], new ItemStack[] {new ItemStack(Material.LEGACY_ENDER_PORTAL_FRAME, 1)}, 0.14, 0.141421356F, new String[0], new PotionEffect[] {new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300 * 20, 0), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300 * 20, 0)}));
		
	}
	
	/** @return The inventory title used for the challenge screen */
	public static final String getChallengeScreenTitle() {
		return ChatColor.GOLD + "Island Challenges";
	}
	
	/** @return The size of the inventory used for the challenge screen */
	public static final int getChallengeScreenSize() {
		return max_levels * 9;
	}
	
	/** @param player The player who will be viewing the screen, or
	 *            <b><code>null</code></b> for a generic audience
	 * @return The resulting challenge screen */
	public static final Inventory getChallengeScreen(Player player) {
		Island island = Island.getIslandFor(player);
		Inventory screen = Main.server.createInventory(player, getChallengeScreenSize(), getChallengeScreenTitle());
		for(Challenge challenge : challenges) {
			ItemStack icon = challenge.getIcon(island == null ? false : island.hasMemberCompleted(player, challenge));
			screen.setItem(challenge.getInvSlot(), icon);
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
		for(Challenge challenge : challenges) {
			if(challenge.getInvSlot() == slot) {
				return challenge;
			}
		}
		return null;
	}
	
	private volatile String name;
	private volatile String displayName;
	private volatile String[] description = new String[0];
	private volatile Material icon;
	private volatile short iconData = 0;
	private volatile int difficulty = 0, index = 0;
	private volatile ItemStack[] requiredItems = new ItemStack[0];
	private volatile boolean takeItems = false;
	private volatile double requiredLevel = 0x0.0p0;
	private volatile boolean repeatable = false;
	private volatile ItemStack[] requiredBlocks = new ItemStack[0];
	
	private volatile ItemStack[] rewardItems = new ItemStack[0];
	private volatile double rewardMoney = 0x0.0p0;
	private volatile float rewardExp = 0x0.0p0F;
	private volatile String[] rewardPermissions = new String[0];
	private volatile PotionEffect[] rewardEffects = new PotionEffect[0];
	
	/** @param name This challenge's configuration/command line name
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
	protected Challenge(String name, String displayName, String[] description, Material icon, short iconData, int difficulty, int index, ItemStack[] requiredItems, boolean takeItems, double requiredLevel, boolean repeatable, ItemStack[] requiredBlocks, ItemStack[] rewardItems, double rewardMoney, float rewardExp, String[] rewardPermissions, PotionEffect... rewardEffects) {
		this.name = name;
		this.displayName = displayName == null || displayName.trim().isEmpty() ? this.name : displayName;
		this.description = description;
		this.icon = icon == null ? Material.STONE : icon;
		this.iconData = iconData;
		this.difficulty = Math.max(0, Math.min(max_levels - 1, difficulty));
		this.index = Math.max(0, Math.min(8, index));
		this.requiredItems = Main.clean(requiredItems);
		this.takeItems = takeItems;
		this.requiredLevel = requiredLevel;
		this.repeatable = repeatable;
		this.requiredBlocks = Main.clean(requiredBlocks);
		this.rewardItems = Main.clean(rewardItems);
		this.rewardMoney = Math.abs(rewardMoney);
		this.rewardExp = Math.abs(rewardExp);
		this.rewardPermissions = Main.clean(rewardPermissions);
		this.rewardEffects = Main.clean(rewardEffects);
		if(this.requiredBlocks.length > 0 || this.requiredLevel > 0x0.0p0) {
			this.repeatable = this.requiredItems.length > 0 && this.takeItems ? repeatable : false;
		}
		if(this.requiredItems.length > 0 && !this.takeItems) {
			this.repeatable = false;
		}
		if(this.rewardPermissions.length > 0) {
			this.repeatable = false;
		}
		
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
	
	/** @param completed Whether or not the player viewing the challenge screen
	 *            has completed this challenge
	 * @return The icon representing this challenge */
	public final ItemStack getIcon(boolean completed) {
		ItemStack icon = new ItemStack(this.icon, 1, this.iconData);
		ItemMeta meta = Main.server.getItemFactory().getItemMeta(this.icon);
		meta.setDisplayName(this.getDisplayName());
		List<String> lore = new ArrayList<>();
		for(String line : this.description) {
			lore.add(ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', line));
		}
		lore.add(ChatColor.GRAY + "Requirements:");
		for(String requirement : this.getRequirements()) {
			lore.add(requirement);
		}
		lore.add(ChatColor.GOLD + "Rewards:");
		for(String reward : this.getRewards()) {
			lore.add(reward);
		}
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
	
	/** @return An array containing the requirements of this challenge */
	public final String[] getRequirements() {
		String[] requirements = new String[0];
		if(this.requiredLevel > 0x0.0p0) {
			requirements = Arrays.copyOf(requirements, requirements.length + 1);
			requirements[requirements.length - 1] = ChatColor.BLUE + "An island level of " + ChatColor.GOLD + Main.limitDecimalToNumberOfPlaces(this.requiredLevel, 2) + ChatColor.BLUE + " or higher";
		}
		if(this.requiredItems.length > 0) {
			requirements = Arrays.copyOf(requirements, requirements.length + 1);
			requirements[requirements.length - 1] = ChatColor.GREEN + "Items:";
			for(ItemStack item : this.requiredItems) {
				requirements = Arrays.copyOf(requirements, requirements.length + 1);
				requirements[requirements.length - 1] = ChatColor.GRAY + "* " + ChatColor.WHITE + Main.getItemName(item) + ChatColor.WHITE + " x" + item.getAmount();
			}
		}
		if(this.requiredBlocks.length > 0) {
			requirements = Arrays.copyOf(requirements, requirements.length + 1);
			requirements[requirements.length - 1] = ChatColor.LIGHT_PURPLE + "Blocks(placed or held):";
			for(ItemStack item : this.requiredBlocks) {
				requirements = Arrays.copyOf(requirements, requirements.length + 1);
				requirements[requirements.length - 1] = ChatColor.GRAY + "* " + ChatColor.WHITE + Main.getItemName(item) + ChatColor.WHITE + " x" + item.getAmount();
			}
		}
		if(requirements.length == 0) {
			requirements = new String[] {ChatColor.GRAY + "None."};
		}
		return requirements;
	}
	
	/** @return An array containing the rewards for completing this challenge */
	public final String[] getRewards() {
		String[] rewards = new String[0];
		if(this.rewardItems.length > 0) {
			rewards = Arrays.copyOf(rewards, rewards.length + 1);
			rewards[rewards.length - 1] = ChatColor.GREEN + "Items:";
			for(ItemStack item : this.rewardItems) {
				rewards = Arrays.copyOf(rewards, rewards.length + 1);
				rewards[rewards.length - 1] = ChatColor.DARK_GREEN + "* " + ChatColor.WHITE + Main.getItemName(item) + ChatColor.WHITE + " x" + item.getAmount();
			}
		}
		if(this.rewardExp > 0x0.0p0F) {
			rewards = Arrays.copyOf(rewards, rewards.length + 1);
			rewards[rewards.length - 1] = ChatColor.YELLOW + "Experience: " + ChatColor.GREEN + Main.limitDecimalToNumberOfPlaces(this.rewardExp, 2);
		}
		if(this.rewardMoney > 0x0.0p0) {
			rewards = Arrays.copyOf(rewards, rewards.length + 1);
			rewards[rewards.length - 1] = ChatColor.GREEN + "Money: " + ChatColor.GOLD + Main.limitDecimalToNumberOfPlaces(this.rewardMoney, 2);
		}
		if(this.rewardEffects.length > 0) {
			rewards = Arrays.copyOf(rewards, rewards.length + 1);
			rewards[rewards.length - 1] = ChatColor.GREEN + "Temporary effects:";
			for(PotionEffect effect : this.rewardEffects) {
				String name = Main.capitalizeFirstLetterOfEachWord(effect.getType().getName().toLowerCase(), '_', ' ');
				String duration = Main.getLengthOfTime(effect.getDuration() * 50L);
				String amplifier = Main.toRomanNumerals(effect.getAmplifier() + 1);
				rewards = Arrays.copyOf(rewards, rewards.length + 1);
				rewards[rewards.length - 1] = ChatColor.DARK_GREEN + "* " + ChatColor.YELLOW + name + " " + ChatColor.GOLD + amplifier + ChatColor.DARK_GREEN + " for " + ChatColor.YELLOW + duration;
			}
		}
		if(this.rewardPermissions.length > 0) {
			rewards = Arrays.copyOf(rewards, rewards.length + 1);
			rewards[rewards.length - 1] = ChatColor.GOLD + "Permissions:";
			for(String permission : this.rewardPermissions) {
				rewards = Arrays.copyOf(rewards, rewards.length + 1);
				rewards[rewards.length - 1] = ChatColor.YELLOW + "\"" + ChatColor.WHITE + permission + ChatColor.YELLOW + "\"";
			}
		}
		if(rewards.length == 0) {
			rewards = new String[] {ChatColor.BLUE + "None."};
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
		return difficultyToString(this.getDifficulty());
	}
	
	/** @param difficulty The difficulty level to translate
	 * @return The translated level */
	public static final String difficultyToString(int difficulty) {
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
	
	/** @return The items required to be in a player's inventory in order to
	 *         complete this challenge */
	public final ItemStack[] getRequiredItems() {
		return safeCopy(this.requiredItems);
	}
	
	/** @return Whether or not this challenge will take the required items from
	 *         a player's inventory */
	public final boolean doesTakeItems() {
		return this.takeItems;
	}
	
	/** @return The island level that a player is required to be at in order
	 *         to complete this challenge */
	public final double getRequiredLevel() {
		return this.requiredLevel;
	}
	
	/** @return Whether or not this challenge is repeatable */
	public final boolean isRepeatable() {
		return this.repeatable;
	}
	
	/** @return The blocks required to be placed on a player's island in order
	 *         to complete this challenge */
	public final ItemStack[] getRequiredBlocks() {
		return safeCopy(this.requiredBlocks);
	}
	
	/** @return The items that will be awarded to a player that completes this
	 *         challenge */
	public final ItemStack[] getRewardItems() {
		return safeCopy(this.rewardItems);
	}
	
	/** @return The money that will be awarded to a player that completes this
	 *         challenge */
	public final double getRewardMoney() {
		return this.rewardMoney;
	}
	
	/** @return The amount of experience that will be awarded to a player when
	 *         they complete this challenge */
	public final float getRewardExperience() {
		return this.rewardExp - Math.round(this.rewardExp);
	}
	
	/** @return The special permissions that will be awarded to the player that
	 *         completes this challenge */
	public final String[] getRewardPermissions() {
		return this.rewardPermissions;
	}
	
	/** @param player The player to check
	 * @return A list containing the items that this player needs to get before
	 *         they can complete this challenge */
	public final List<ItemStack> getRemainingRequiredItems(Player player) {
		ArrayList<ItemStack> requiredItems = new ArrayList<>();
		for(ItemStack required : this.requiredItems) {
			requiredItems.add(new ItemStack(required));
		}
		if(requiredItems.isEmpty()) {
			return requiredItems;
		}
		if(player.getName().equals("Brian_Entei")) {
			player.sendMessage("RequiredItems: " + requiredItems.size());
			for(ItemStack required : requiredItems) {
				player.sendMessage(Main.getItemName(required) + " x" + required.getAmount());
			}
		}
		for(ItemStack item : player.getInventory().getContents()) {
			if(item == null) {
				continue;
			}
			for(ItemStack required : new ArrayList<>(requiredItems)) {
				if(Main.isSameType(required, item)) {
					int newAmt = required.getAmount() - item.getAmount();
					if(newAmt > 0) {
						if(player.getName().equals("Brian_Entei")) {
							player.sendMessage("Reduced requirement " + Main.getItemName(required) + " from x" + required.getAmount() + " to x" + newAmt + " and item amount: " + item.getAmount());
						}
						required.setAmount(newAmt);
						break;
					}
					requiredItems.remove(required);
					if(player.getName().equals("Brian_Entei")) {
						player.sendMessage("Removed requirement " + Main.getItemName(required) + " x" + required.getAmount() + " and item amount: " + item.getAmount());
					}
					break;
				}
			}
		}
		if(player.getName().equals("Brian_Entei")) {
			player.sendMessage("RequiredItems: " + requiredItems.size());
			for(ItemStack required : requiredItems) {
				player.sendMessage(Main.getItemName(required) + " x" + required.getAmount());
			}
		}
		for(ItemStack required : new ArrayList<>(requiredItems)) {
			if(required.getAmount() <= 0) {
				requiredItems.remove(required);
			}
		}
		if(player.getName().equals("Brian_Entei")) {
			player.sendMessage("RequiredItems: " + requiredItems.size());
			for(ItemStack required : requiredItems) {
				player.sendMessage(Main.getItemName(required) + " x" + required.getAmount());
			}
		}
		/*player.sendMessage("Items left: " + requiredItems.size());
		for(ItemStack requirement : requiredItems) {
			player.sendMessage(requirement.getType().name().toLowerCase().replace('_', ' ') + "x" + requirement.getAmount());
		}*/
		return requiredItems;
	}
	
	/** @param player The player to check
	 * @return Whether or not the player has all of the required items in order
	 *         to complete this challenge */
	public boolean hasRequiredItems(Player player) {
		return this.getRemainingRequiredItems(player).isEmpty();
	}
	
	/** @param player The player to check
	 * @return A list of the blocks that this player still has to obtain before
	 *         they can complete this challenge */
	public final List<ItemStack> getRemainingRequiredBlocks(Player player) {
		ArrayList<ItemStack> requiredBlocks = new ArrayList<>();
		for(ItemStack required : this.requiredBlocks) {
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
			Island island = Island.getIslandFor(player);
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
	public boolean hasRequiredBlocks(Player player) {
		return this.getRemainingRequiredBlocks(player).isEmpty();
	}
	
	/** @param player The player to check
	 * @return The remaining amount of level(s) that this player needs to obtain
	 *         before they can complete this challenge */
	public double getRemainingRequiredLevel(Player player) {
		Island island = Island.getIslandFor(player);
		return this.hasRequiredLevel(player) ? 0 : (island == null ? -1 : this.requiredLevel - island.getLevel());
	}
	
	/** @param player The player whose Island will be checked
	 * @return Whether or not the player's Island is of the required level */
	public boolean hasRequiredLevel(Player player) {
		Island island = Island.getIslandFor(player);
		if(island != null) {
			return this.requiredLevel > 0x0.0p0 ? island.getLevel() >= this.requiredLevel : true;
		}
		return false;
	}
	
	/** @param player The player to check
	 * @return Whether or not the given player has completed this challenge
	 *         before */
	public final boolean hasCompleted(Player player) {
		Island island = Island.getIslandFor(player);
		return island == null ? false : island.hasMemberCompleted(player, this);
	}
	
	/** @param player The player to check
	 * @return Whether or not the player can complete this challenge */
	public boolean canComplete(Player player) {
		Island island = Island.getIslandFor(player);
		boolean repeatCheck = this.isRepeatable() ? true : !island.hasMemberCompleted(player, this);
		boolean world = Island.isInSkyworld(player);
		boolean items = this.hasRequiredItems(player);
		boolean blocks = this.hasRequiredBlocks(player);
		boolean level = this.hasRequiredLevel(player);
		//player.sendMessage("Island: " + (island != null) + " world: " + world + " items: " + items + " blocks: " + blocks + " level: " + level + " ");
		return island != null && repeatCheck && world && items && blocks && level;
	}
	
	/** @param player The player whose items matching this challenge's required
	 *            items will be taken */
	public final void takeItems(Player player) {
		if(this.takeItems) {
			ItemStack[] required = safeCopy(this.requiredItems);
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
			if(!ChallengeCompleteEvent.fire(player, this).isCancelled()) {
				this.takeItems(player);
				this.reward(player);
				Island island = Island.getIslandFor(player);
				if(island != null) {
					island.setCompleted(player, this);
				}
			}
			return true;
		}
		return false;
	}
	
	/** @param player The player who will receive all of this challenge's
	 *            rewards */
	public final void reward(Player player) {
		if(this.rewardExp > 0x0.0p0F) {
			float exp = player.getExp() + this.getRewardExperience();
			while(true) {
				if(exp > 1.0F) {
					exp -= 1.0F;
					player.setLevel(player.getLevel() + 1);
				} else {
					player.setExp(exp);
					break;
				}
			}
		}
		for(PotionEffect effect : this.rewardEffects) {
			PotionEffect current = player.getPotionEffect(effect.getType());
			if(current != null && current.getAmplifier() <= effect.getAmplifier()) {
				effect = new PotionEffect(effect.getType(), Math.min(effect.getDuration() + current.getDuration(), 24000), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles());
			}
			player.addPotionEffect(effect, true);
		}
		if(Main.isVaultEnabled() && this.rewardPermissions.length > 0) {
			net.milkbowl.vault.permission.Permission perm = VaultHandler.getPermissionsHandler();
			if(perm != null) {
				for(String permission : this.rewardPermissions) {
					perm.playerAdd(GeneratorMain.getSkyworld().getName(), player, permission);
				}
			}
		}
		Main.giveItemToPlayer(player, this.rewardItems);
		if(Main.isVaultEnabled() && this.rewardMoney > 0x0.0p0) {
			net.milkbowl.vault.economy.Economy eco = VaultHandler.getEconomyHandler();
			if(eco != null) {
				eco.depositPlayer(player, GeneratorMain.getSkyworld().getName(), this.rewardMoney);
			}
		}
	}
	
}
