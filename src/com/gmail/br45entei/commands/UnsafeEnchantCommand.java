package com.gmail.br45entei.commands;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

@SuppressWarnings("javadoc")
public class UnsafeEnchantCommand extends Command {
	
	static final int MAX_COORD = 30000000;
	static final int MIN_COORD_MINUS_ONE = -30000001;
	static final int MIN_COORD = -30000000;
	
	private static final List<String> ENCHANTMENT_NAMES = new ArrayList<>();
	
	private static final String name = "unsafeenchant";
	
	public UnsafeEnchantCommand() {
		super(name);
		this.description = "Adds enchantments to the item the player is currently holding. Specify 0 for the level to remove an enchantment. Specify force to ignore normal enchantment restrictions";
		this.usageMessage = "/enchant <player> <enchantment> [level|max|0] [force]";
		this.setPermission("bukkit.command.unsafeenchant");
	}
	
	public static final boolean isUnsafeEnchantCommand(String command) {
		return command.equalsIgnoreCase(name);
	}
	
	/** @param commandLabel */
	public static final boolean onCommand(CommandSender sender, String commandLabel, String[] args) {
		if(!sender.hasPermission("bukkit.command.unsafeenchant")) {
			sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
			return true;
		}
		if(args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: " + "/enchant <player> <enchantment> [level|max|0] [force]");
			return false;
		}
		
		boolean force = false;
		if(args.length > 2) {
			force = args[args.length > 3 ? 3 : 2].equalsIgnoreCase("force");
		}
		
		Player player = Bukkit.getPlayerExact(args[0]);
		if(player == null) {
			sender.sendMessage("Can't find player " + args[0]);
		} else {
			ItemStack item = player.getInventory().getItemInMainHand();
			if(item.getType() == Material.AIR) {
				sender.sendMessage("The player isn't holding an item.");
			} else {
				String itemName = item.getType().toString().replaceAll("_", " ");
				itemName = WordUtils.capitalizeFully(itemName);
				
				Enchantment enchantment = getEnchantment(args[1].toUpperCase());
				if(enchantment == null) {
					sender.sendMessage(String.format("Enchantment does not exist: %s", args[1]));
				} else {
					String enchantmentName = enchantment.getName().replaceAll("_", " ");
					enchantmentName = WordUtils.capitalizeFully(enchantmentName);
					
					if(!force && !enchantment.canEnchantItem(item)) {
						sender.sendMessage(String.format("%s cannot be applied to %s. Add \"force\" onto the end of the command to force the enchantment.", enchantmentName, itemName));
					} else {
						int level = 1;
						if(args.length > 2) {
							Integer integer = getInteger(args[2]);
							int minLevel = enchantment.getStartLevel();
							int maxLevel = force ? Short.MAX_VALUE : enchantment.getMaxLevel();
							
							if(integer != null) {
								if(integer.intValue() == 0) {
									item.removeEnchantment(enchantment);
									Command.broadcastCommandMessage(sender, String.format("Removed %s on %s's %s.", enchantmentName, player.getName(), itemName));
									return true;
								}
								
								if(integer.intValue() < minLevel || integer.intValue() > maxLevel) {
									sender.sendMessage(String.format("Level for enchantment %s must be between %d and %d. Add \"force\" onto the end of the command to force the level.", enchantmentName, Integer.valueOf(minLevel), Integer.valueOf(maxLevel)));
									sender.sendMessage("Specify 0 for level to remove an enchantment.");
									return true;
								}
								
								level = integer.intValue();
							}
							
							if("max".equals(args[2])) {
								level = maxLevel;
							}
						}
						item.addUnsafeEnchantment(enchantment, level);
						Command.broadcastCommandMessage(sender, String.format("Applied %s (Lvl %d) on %s's %s.", enchantmentName, Integer.valueOf(level), player.getName(), itemName), false);
						sender.sendMessage(String.format("Enchanting succeeded, applied %s (Lvl %d) on %s's %s.", enchantmentName, Integer.valueOf(level), player.getName(), itemName));
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		//if(!testPermission(sender)) return true;
		return onCommand(sender, commandLabel, args);
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(args, "Arguments cannot be null");
		Validate.notNull(alias, "Alias cannot be null");
		
		if(args.length == 1) {
			return super.tabComplete(sender, alias, args);
		}
		
		if(args.length == 2) {
			return StringUtil.copyPartialMatches(args[1], ENCHANTMENT_NAMES, new ArrayList<String>(ENCHANTMENT_NAMES.size()));
		}
		
		if(args.length == 3 || args.length == 4) {
			if(!args[args.length - 2].equalsIgnoreCase("force")) {
				return ImmutableList.of("force");
			}
		}
		
		return ImmutableList.of();
	}
	
	@SuppressWarnings("deprecation")
	private static Enchantment getEnchantment(String lookup) {
		Enchantment enchantment = Enchantment.getByName(lookup);
		
		/*if(enchantment == null) {
			Integer id = getInteger(lookup);
			if(id != null) {
				enchantment = Enchantment.getById(id.intValue());
			}
		}*/
		
		return enchantment;
	}
	
	static {
		
		for(Enchantment enchantment : Enchantment.values()) {
			ENCHANTMENT_NAMES.add(enchantment.getName());
		}
		
		Collections.sort(ENCHANTMENT_NAMES);
	}
	
	public static boolean matches(String input) {
		return input.equalsIgnoreCase(name);
	}
	
	protected static int getInteger(String value, int min) {
		return getInteger(value, min, Integer.MAX_VALUE);
	}
	
	static int getInteger(String value, int min, int max) {
		return getInteger(value, min, max, false);
	}
	
	static int getInteger(String value, int min, int max, boolean Throws) {
		int i = min;
		
		try {
			i = Integer.parseInt(value);
		} catch(NumberFormatException ex) {
			if(Throws) {
				throw new NumberFormatException(String.format("%s is not a valid number", value));
			}
		}
		
		if(i < min) {
			i = min;
		} else if(i > max) {
			i = max;
		}
		
		return i;
	}
	
	static Integer getInteger(String value) {
		try {
			return Integer.valueOf(value);
		} catch(NumberFormatException ex) {
			return null;
		}
	}
	
	public static double getRelativeDouble(double original, String input) {
		if(input.startsWith("~")) {
			double value = getDouble(input.substring(1));
			if(value == MIN_COORD_MINUS_ONE) {
				return MIN_COORD_MINUS_ONE;
			}
			return original + value;
		}
		return getDouble(input);
	}
	
	public static double getDouble(String input) {
		try {
			return Double.parseDouble(input);
		} catch(NumberFormatException ex) {
			return MIN_COORD_MINUS_ONE;
		}
	}
	
	public static double getDouble(String input, double min, double max) {
		double result = getDouble(input);
		
		// TODO: This should throw an exception instead.
		if(result < min) {
			result = min;
		} else if(result > max) {
			result = max;
		}
		
		return result;
	}
	
	static String createString(String[] args, int start) {
		return createString(args, start, " ");
	}
	
	static String createString(String[] args, int start, String glue) {
		StringBuilder string = new StringBuilder();
		
		for(int x = start; x < args.length; x++) {
			string.append(args[x]);
			if(x != args.length - 1) {
				string.append(glue);
			}
		}
		
		return string.toString();
	}
	
}
