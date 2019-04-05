package com.gmail.br45entei.enteisSkyblock.event.listeners;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

import com.gmail.br45entei.enteisSkyblock.main.Island;
import com.gmail.br45entei.enteisSkyblock.main.Main;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/** @author Brian_Entei */
public class AutomaticInventoryListener implements Listener {
	
	/** Constructor for event registration */
	public AutomaticInventoryListener() {
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public static final void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(Main.pluginMgr.getPlugin("EnteisCommands") != null || event.isCancelled() || event.getAction() != Action.LEFT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND) {
			return;
		}
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if(player.isSneaking() && block != null && block.getState() instanceof InventoryHolder) {
			InventoryHolder chest = (InventoryHolder) block.getState();
			Inventory inv = chest.getInventory();
			if(inv != null) {
				boolean infinityBow = false;
				int arrowSlot = -1;
				for(int i = 0; i < player.getInventory().getSize(); i++) {
					ItemStack item = player.getInventory().getItem(i);
					if(item == null) {
						continue;
					}
					if(item.getType() == Material.BOW && item.containsEnchantment(Enchantment.ARROW_INFINITE)) {
						infinityBow = true;
						break;
					}
				}
				if(infinityBow) {
					for(int i = 0; i < player.getInventory().getSize(); i++) {
						ItemStack item = player.getInventory().getItem(i);
						if(item == null) {
							continue;
						}
						if(item.getType() == Material.ARROW && item.getAmount() >= 1) {
							item.setAmount(item.getAmount() - 1);
							if(item.getAmount() == 0) {
								player.getInventory().clear(i);
							} else {
								player.getInventory().setItem(i, item);
							}
							arrowSlot = i;
							break;
						}
					}
					if(arrowSlot == -1) {
						infinityBow = false;
					}
				}
				int stacksStored = 0;
				for(ItemStack item : inv.getContents()) {
					if(item == null) {
						continue;
					}
					int i = 0;
					for(ItemStack pItem : player.getInventory().getContents()) {
						if(pItem == null) {
							i++;
							continue;
						}
						if(Main.isSameType(pItem, item)) {
							HashMap<Integer, ItemStack> leftovers = inv.addItem(pItem);
							if(!Main.contains(pItem, leftovers.values())) {
								stacksStored++;
								player.getInventory().clear(i);
								if(leftovers.size() > 0) {
									for(ItemStack leftover : leftovers.values()) {
										player.getInventory().addItem(leftover);
									}
								}
							}
						}
						i++;
					}
				}
				if(stacksStored > 0) {
					block.getLocation().getWorld().spawnParticle(Particle.PORTAL, block.getLocation().add(0.5, 0.75, 0.5), 96);
					Main.scheduler.scheduleSyncDelayedTask(Main.getPlugin(), () -> {
						block.getLocation().getWorld().spawnParticle(Particle.PORTAL, block.getLocation().add(0.5, 0.75, 0.5), 96);
					}, 10L);
					Main.scheduler.scheduleSyncDelayedTask(Main.getPlugin(), () -> {
						block.getLocation().getWorld().spawnParticle(Particle.PORTAL, block.getLocation().add(0.5, 0.75, 0.5), 96);
					}, 20L);
					Main.scheduler.scheduleSyncDelayedTask(Main.getPlugin(), () -> {
						block.getLocation().getWorld().spawnParticle(Particle.PORTAL, block.getLocation().add(0.5, 0.75, 0.5), 96);
					}, 30L);
					Main.scheduler.scheduleSyncDelayedTask(Main.getPlugin(), () -> {
						block.getLocation().getWorld().playSound(block.getLocation().add(0.5, 0.75, 0.5), Sound.ENTITY_ENDERMAN_TELEPORT, 3f, 1f);
					}, 37L);//40L);
					player.sendMessage(GREEN + "Stored " + GOLD + Integer.toString(stacksStored, 10) + GREEN + " stack" + (stacksStored == 1 ? "" : "s") + ".");
					player.updateInventory();
				}
				if(infinityBow) {
					ItemStack check = player.getInventory().getItem(arrowSlot);
					if(check != null && check.getType() == Material.ARROW) {
						check.setAmount(check.getAmount() + 1);
						if(check.getAmount() > Material.ARROW.getMaxStackSize()) {
							check.setAmount(Material.ARROW.getMaxStackSize());
							player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
						} else {
							player.getInventory().setItem(arrowSlot, check);
						}
					} else if(check == null || check.getType() == Material.AIR) {
						player.getInventory().setItem(arrowSlot, new ItemStack(Material.ARROW, 1));
					} else {
						player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
					}
					player.updateInventory();
				}
			}
			event.setCancelled(true);
		} else if(block != null && block.getState() instanceof InventoryHolder) {
			//TODO Retrieve similar items from chest to inv!
			
			//if(stacksRetrieved > 0) {event.setCancelled(true);}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public static final void onBlockPlaceEvent(BlockPlaceEvent event) {
		if(!Island.isInSkyworld(event.getBlock().getLocation())) {
			return;
		}
		if(!event.isCancelled()) {
			Player player = event.getPlayer();
			ItemStack similar = new ItemStack(event.getItemInHand());
			EquipmentSlot hand = event.getHand();
			if(hand == EquipmentSlot.HAND) {
				Main.scheduler.scheduleSyncDelayedTask(Main.getPlugin(), () -> {
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
							if(Main.isSameType(item1, similar)) {
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
								if(Main.isSameType(item2, similar)) {
									remaining += item2.getAmount();
								}
							}
							player.sendTitle("", ChatColor.GRAY + "You have " + ChatColor.GOLD + Integer.toString(remaining, 10) + ChatColor.GRAY + " of " + title + ChatColor.RESET + ChatColor.GRAY + " remaining.", 10, 70, 20);
						}
					}
				});
			} else if(hand == EquipmentSlot.OFF_HAND) {
				Main.scheduler.scheduleSyncDelayedTask(Main.getPlugin(), () -> {
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
							if(Main.isSameType(item1, similar)) {
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
								if(Main.isSameType(item2, similar)) {
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
	public static final void onBlockBreakEvent(BlockBreakEvent event) {
		if(!Island.isInSkyworld(event.getBlock().getLocation())) {
			return;
		}
		if(!event.isCancelled()) {
			Player player = event.getPlayer();
			Block block = event.getBlock();
			Location location = block.getLocation();
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
					expToDrop = expToDrop == 0 ? Main.getExpForBlock(block) : expToDrop;
					if(expToDrop > 0) {
						event.setExpToDrop(0);//event.setCancelled(true);
						ExperienceOrb orb = Main.dropExperience(player.getLocation(), expToDrop);
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
							location.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 3f, 1f + Main.random.nextFloat() + Main.random.nextFloat());
						}
					}
				}
			}
		}
	}
	
}
