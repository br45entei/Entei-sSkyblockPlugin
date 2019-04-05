package com.gmail.br45entei.enteisSkyblock.event.listeners;

import com.gmail.br45entei.enteisSkyblock.main.Island;
import com.gmail.br45entei.enteisSkyblock.main.Main;
import com.gmail.br45entei.enteisSkyblockGenerator.main.GeneratorMain;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Redstone;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

/** @author Brian_Entei */
@SuppressWarnings({"javadoc"})
public class DamageEventListeners implements Listener {
	
	/** Event listener constructor */
	public DamageEventListeners() {
	}
	
	public static final ConcurrentHashMap<String, ConcurrentLinkedDeque<Integer>> playerAttackedEntities = new ConcurrentHashMap<>();
	
	public static final void markPlayerAsHavingHitEntity(Entity victim, Player damager) {
		if(victim instanceof Player) {
			return;
		}
		int entityID = victim.getEntityId();
		String key = damager.getUniqueId().toString();
		ConcurrentLinkedDeque<Integer> entityIDs = playerAttackedEntities.get(key);
		if(entityIDs == null) {
			entityIDs = new ConcurrentLinkedDeque<>();
			playerAttackedEntities.put(key, entityIDs);
		}
		for(Integer id : entityIDs) {
			if(id.intValue() == entityID) {
				return;
			}
		}
		entityIDs.add(Integer.valueOf(entityID));
	}
	
	public static final boolean canEntityAttackVisitingPlayer(Entity entity, Player target) {
		if(entity instanceof Player) {
			throw new UnsupportedOperationException("Unable to determine if a player entity can attack another player without the required island information!");
		}
		int entityID = entity.getEntityId();
		String key = target.getUniqueId().toString();
		ConcurrentLinkedDeque<Integer> entityIDs = playerAttackedEntities.get(key);
		if(entityIDs == null) {
			entityIDs = new ConcurrentLinkedDeque<>();
			playerAttackedEntities.put(key, entityIDs);
			return false;
		}
		if(entityIDs.isEmpty()) {
			return false;
		}
		for(Integer id : entityIDs) {
			if(id.intValue() == entityID) {
				return true;
			}
		}
		return false;
	}
	
	public static final void forgivePlayer(Player player) {
		playerAttackedEntities.remove(player.getUniqueId().toString());
	}
	
	public static final boolean sendTeleportCooldownMessageTo(Player player) {
		long timeRemaining = Main.getTeleportCooldownRemainingFor(player);
		if(timeRemaining >= 0) {
			player.sendMessage(ChatColor.YELLOW.toString().concat("[!] (Skyblock) Your last teleport/gm change/respawn's cooldown ends in ").concat(ChatColor.AQUA.toString()).concat(Main.getLengthOfTime(timeRemaining)).concat(ChatColor.YELLOW.toString()).concat(". You'll be able to take damage normally after that."));
			return true;
		}
		return false;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public static final void onEntityDamageEvent(EntityDamageEvent event) {
		Location location = event.getEntity().getLocation();
		if(!Island.isInSkyworld(location)) {
			return;
		}
		event.setCancelled(false);
		
		//Cancel Void damage and teleport to safety
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(Main.isTeleportCooldownInEffectFor(player) && sendTeleportCooldownMessageTo(player)) {
				event.setCancelled(true);
				return;
			}
			if(event.getCause() == DamageCause.VOID) {
				event.setCancelled(true);
				Island home = Island.getIslandPlayerIsUsing(player, true, false);
				Location homeLocation = home == null ? GeneratorMain.getSkyworldSpawnLocation() : home.getHomeFor(player.getUniqueId());
				homeLocation = homeLocation == null ? (home == null ? GeneratorMain.getSkyworld().getSpawnLocation() : home.getSpawnLocation()) : homeLocation;
				if(homeLocation == null) {
					Main.getPlugin().onCommand(player, null, "spawn", new String[0]);
				} else {
					final Location loc = homeLocation;
					Main.safeTeleport(player, homeLocation);
					Main.scheduler.runTaskLater(Main.getPlugin(), () -> {//If the player's island home is on farmland, they seem to get stuck on the block after being teleported...
						Main.safeTeleport(player, loc);
						player.setVelocity(new Vector(0.0, 0.1, 0.0));
					}, 5L);
				}
				return;
			}
		}
		
		//Cancel if the player is not on an island; otherwise cancel if the player just fell and had recently teleported
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Island island = Island.getIslandContaining(location);
			if(island == null) {
				event.setCancelled(true);
				return;
			}
			if(event.getCause() == DamageCause.FALL) {
				Long lastTeleportTime = Main.lastTeleportTimes.get(player.getUniqueId().toString());
				if(lastTeleportTime != null && System.currentTimeMillis() - lastTeleportTime.longValue() < 0) {
					event.setCancelled(true);
				} else if(lastTeleportTime != null) {
					Main.lastTeleportTimes.remove(player.getUniqueId().toString());
				}
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
		Location location = event.getEntity().getLocation();
		if(Island.isInSkyworld(location)) {
			event.setCancelled(false);
			if(event.getDamager() instanceof Player && !(event.getEntity() instanceof Player) && Island.getIslandContaining(event.getEntity().getLocation()) == null) {
				if(((Player) event.getDamager()).getGameMode() == GameMode.CREATIVE) {
					event.setCancelled(false);
				} else {
					event.setCancelled(true);
				}
				return;
			}
		}
		//Fix entities standing on borders/in the spawn region being able to shoot players on islands
		if(event.getEntity() instanceof Player && Island.isInSkyworld(event.getDamager().getLocation()) && Island.getIslandContaining(event.getDamager().getLocation()) == null) {
			event.setCancelled(true);
			return;
		}
		if(event.getEntity() instanceof Player && Island.isInSkyworld(location)) {
			Player victim = (Player) event.getEntity();
			Entity damager = event.getDamager();
			Projectile projectile = null;
			if(damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Entity) {
				projectile = (Projectile) damager;
				damager = (Entity) projectile.getShooter();
				Main.sendDebugMsg(victim, "onEntityDamageEntity: attacked_by_entity_projectile");
			}
			if(Main.isTeleportCooldownInEffectFor(victim) && sendTeleportCooldownMessageTo(victim)) {
				event.setCancelled(true);
				Main.sendDebugMsg(victim, "onEntityDamageEntity: victim_in_cooldown_mode: event cancelled");
				if(damager instanceof Player) {
					Main.sendDebugMsg(victim, "onEntityDamageEntity: victim_in_cooldown_mode_attacker_notified");
					Player attacker = (Player) damager;
					attacker.sendMessage(ChatColor.WHITE.toString().concat(victim.getDisplayName()).concat(ChatColor.RESET.toString()).concat(ChatColor.YELLOW.toString()).concat(" is in cooldown mode, and cannot take damage. Wait a bit."));
				}
				return;
			}
			Island island = Island.getIslandContaining(location);
			if(damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof BlockProjectileSource) {
				Location blockLoc = ((BlockProjectileSource) ((Projectile) damager).getShooter()).getBlock().getLocation();
				event.setCancelled(island == null || !island.isOnIsland(location) || !island.isOnIsland(blockLoc));
				Main.sendDebugMsg(victim, "onEntityDamageEntity: attacked_by_block_projectile".concat(event.isCancelled() ? ": event cancelled" : ""));
				return;
			} else if(damager instanceof Projectile) {
				event.setCancelled(true);
				Main.sendDebugMsg(victim, "onEntityDamageEntity: attacked_by_unknown_projectile: event cancelled");
				return;
			}
			if(damager instanceof Player) {
				if(island == null || !island.isOnIsland(location)) {
					event.setCancelled(true);
					//negateThrownPotion(projectile);
					Main.sendDebugMsg(victim, "onEntityDamageEntity: not_on_island_attacked_by_player: event cancelled");
					return;
				} else if(!island.isTrusted((Player) damager)) {
					event.setCancelled(!island.allowPVP());
					//negateThrownPotion(projectile);
					Main.sendDebugMsg(victim, "onEntityDamageEntity: on_island_attacked_by_visitor: event cancelled if pvp not allowed");
					return;
				} else {
					event.setCancelled(!island.allowPVP());
					//if(event.isCancelled()) {
					//	negateThrownPotion(projectile);
					//}
					Main.sendDebugMsg(victim, "onEntityDamageEntity: on_island_attacked_by_player: event cancelled if pvp not allowed");
					return;
				}
			}
			if(island != null && !island.isTrusted(victim)) {
				if(damager instanceof Monster) {
					boolean canEntityHitVictim = canEntityAttackVisitingPlayer(damager, victim);
					event.setCancelled(!canEntityHitVictim);//((Monster) damager).getTarget() != victim);//Allow monsters to attack island visitors if the monsters targeted them
					Main.sendDebugMsg(victim, "onEntityDamageEntity: visiting_island_attacked_by_monster: event ".concat(event.isCancelled() ? "cancelled" : "allowed(player has hit entity)"));
					if(event.isCancelled()) {
						//negateThrownPotion(projectile);
						((Monster) damager).setTarget(null);
					}
					return;
				}
				event.setCancelled(true);
				if(damager instanceof IronGolem) {
					event.setCancelled(false);
					Main.sendDebugMsg(victim, "onEntityDamageEntity: on_island_attacked_by_iron_golem");
					return;
				}
				//negateThrownPotion(projectile);
				return;
			}
			if(island != null) {
				//Victim is trusted on the island, and the damager is not a player. Allow vanilla behaviour to occur.
				event.setCancelled(false);
				Main.sendDebugMsg(victim, "onEntityDamageEntity: on_island_attacked_by_entity");
				return;
			}
			//If island is null, cancel the event
			Main.sendDebugMsg(victim, "onEntityDamageEntity: not_on_island_attacked_by_entity: event cancelled");
			event.setCancelled(true);
			//negateThrownPotion(projectile);
			if(damager instanceof Monster) {
				((Monster) damager).setTarget(null);
			}
			return;
		}
		if(event.getDamager() instanceof Arrow && Island.isInSkyworld(location)) {
			Arrow arrow = (Arrow) event.getDamager();
			if(event.getEntity() instanceof Creeper && arrow.getShooter() instanceof Skeleton) {//XXX Hack to make music discs slightly easier to obtain
				Creeper creeper = (Creeper) event.getEntity();
				Skeleton skeleton = (Skeleton) arrow.getShooter();
				skeleton.setTarget(creeper);
				creeper.setTarget(null);//creeper.setTarget(creeper); XDDDD (it blows itself up lmaoooo)
			}
		} else if(event.getDamager() instanceof Player && Island.isInSkyworld(location)) {
			Player player = (Player) event.getDamager();
			Island island = Island.getIslandContaining(location);
			if(island != null && !island.isTrusted(player)) {
				event.setCancelled(true);//Animals, ambient mobs, and entities such as item frames are protected here
				if(!island.isLocked() && (event.getEntity() instanceof Monster)) {//(Allows players to attack hostile mobs on unlocked islands)
					event.setCancelled(false);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onPotionSplashEvent(PotionSplashEvent event) {
		if(!Island.isInSkyworld(event.getEntity().getLocation()) || event.getEntity().getLocation().getWorld() == GeneratorMain.getSkyworldTheEnd()) {
			return;
		}
		event.setCancelled(false);
		ThrownPotion potion = event.getEntity();
		ProjectileSource thrower = potion.getShooter();
		if(thrower instanceof BlockProjectileSource) {
			BlockProjectileSource bps = (BlockProjectileSource) thrower;
			Location blockLoc = bps.getBlock().getLocation();
			Island srcIsland = Island.getIslandContaining(blockLoc);
			for(LivingEntity victim : event.getAffectedEntities()) {
				if(victim instanceof Player) {
					Player player = (Player) victim;
					Island island = Island.getIslandContaining(player.getLocation());
					if(srcIsland == null ? island != null : (srcIsland != island || (island == null || !island.isTrusted(player)))) {
						event.setIntensity(player, 0);
						continue;
					}
				}
			}
			return;
		}
		if(!(thrower instanceof Entity)) {
			String message = "onPotionSplashEvent cancelled: unknown_potion_thrower_class_type: ".concat(thrower == null ? "null" : thrower.getClass().getName());
			Main.sendDebugMsg(Main.console, message);
			for(LivingEntity victim : event.getAffectedEntities()) {
				if(victim instanceof Player) {
					Player player = (Player) victim;
					Main.sendDebugMsg(player, message);
				}
			}
			event.setCancelled(true);
			return;
		}
		Entity entity = (Entity) thrower;
		for(LivingEntity victim : event.getAffectedEntities()) {
			if(entity instanceof Player) {
				Player player = (Player) victim;
				Island island = Island.getIslandContaining(player.getLocation());
				if(island == null) {
					event.setIntensity(player, 0);
					continue;
				}
				if(!island.isTrusted(player)) {
					if(!canEntityAttackVisitingPlayer(entity, player)) {
						event.setIntensity(player, 0);
						continue;
					}
				}
				if(Main.isTeleportCooldownInEffectFor(player) && sendTeleportCooldownMessageTo(player)) {
					event.setIntensity(player, 0);
					continue;
				}
			}
		}
	}
	
	@Deprecated
	protected static final void negateThrownPotion(Projectile projectile) {
		if(projectile instanceof ThrownPotion) {
			ThrownPotion potion = (ThrownPotion) projectile;
			potion.setItem(new ItemStack(Material.SPLASH_POTION, 1));//Remove the effects, leaving only a dummy splash potion
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public static final void onProjectileHitEvent(ProjectileHitEvent event) {
		if(event.getEntity() instanceof Arrow && event.getHitBlock() != null) {
			final Arrow arrow = (Arrow) event.getEntity();
			final Location teleport = arrow.getLocation();
			teleport.setY(-64);
			if(arrow.getPickupStatus() != Arrow.PickupStatus.ALLOWED) {
				Main.scheduler.scheduleSyncDelayedTask(Main.getPlugin(), () -> {
					arrow.setGravity(true);
					if(!arrow.teleport(teleport)) {
						arrow.remove();
					}
				}, 100L);//20 = 1 second, 100 = 20 * 5; 100 = 5 seconds
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onEntityDeathEvent(EntityDeathEvent event) {
		if(event.getEntity() instanceof Player) {
			return;
		}
		for(Entry<String, ConcurrentLinkedDeque<Integer>> entry : playerAttackedEntities.entrySet()) {
			ConcurrentLinkedDeque<Integer> entityIDs = entry.getValue();
			for(Integer entityID : entityIDs) {
				if(entityID.intValue() == event.getEntity().getEntityId()) {
					entityIDs.remove(entityID);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onEntityTargetEvent(EntityTargetEvent event) {
		TargetReason reason = event.getReason();
		Entity entity = event.getEntity();
		Entity target = event.getTarget();
		if(Island.isInSkyworld(entity.getLocation()) && (reason == TargetReason.OWNER_ATTACKED_TARGET || reason == TargetReason.DEFEND_VILLAGE)) {
			event.setCancelled(false);
			if(target instanceof Player) {
				markPlayerAsHavingHitEntity(entity, (Player) target);
			}
			return;
		}
		if(entity instanceof Monster && target != null && target.getLocation().getWorld() == GeneratorMain.getSkyworld()) {
			if(Island.getIslandContaining(entity.getLocation()) == null) {
				event.setCancelled(true);
				return;
			}
		}
		if(target instanceof Player && entity instanceof Monster && Island.isInSkyworld(target.getLocation())) {
			Player player = (Player) target;
			event.setCancelled(true);
			//Monster monster = (Monster) entity;
			if(reason == TargetReason.TARGET_ATTACKED_ENTITY || reason == TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
				event.setCancelled(false);
				markPlayerAsHavingHitEntity(entity, player);
				return;
			}
			Island island = Island.getIslandContaining(target.getLocation());
			if(island == null) {
				event.setCancelled(target.getLocation().getWorld() == GeneratorMain.getSkyworld());//If in skyworld, cancel the event, otherwise, allow it for skyworld_nether and skyworld_the_end.
				return;
			}
			if(!island.isTrusted(player)) {
				event.setCancelled(true);//Visiting players get protection so long as they didn't attack the entity or it's friends/owner.
			} else {
				event.setCancelled(false);//Trusted players get vanilla behaviour.
				markPlayerAsHavingHitEntity(entity, player);
			}
			return;
		}
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
					return;
				}
			}
		} else {
			if(Island.isInSkyworld(entity.getLocation())) {
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
		HumanEntity hooman = event.getEntity();
		if(!Island.isInSkyworld(hooman.getLocation())) {
			return;
		}
		event.setCancelled(false);
		if(hooman instanceof Player && (Main.isInSpawn(hooman.getLocation()) || Island.getIslandContaining(hooman.getLocation()) == null)) {
			Player player = (Player) hooman;
			if(player.getFoodLevel() > event.getFoodLevel()) {
				event.setFoodLevel(Math.max(20, player.getFoodLevel() + 1));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
	public static final void onPlayerInteractEvent(PlayerInteractEvent event) {//XXX Crop trampling/bucket use prevention - players
		Block block = event.getClickedBlock();
		if(block == null || (block.getLocation().getWorld() != GeneratorMain.getSkyworld() && block.getLocation().getWorld() != GeneratorMain.getSkyworldNether())) {
			return;
		}
		Player player = event.getPlayer();
		Island island = Island.getIslandNearest(player.getLocation());
		if(event.getAction() == Action.PHYSICAL) {
			if(island != null && !island.isMember(player)) {
				if(block.getType().name().endsWith("_PRESSURE_PLATE")) {
					event.setCancelled(false);
					return;
				}
			}
			if(block.getType() == Material.FARMLAND || block.getType() == Material.WATER_BUCKET || block.getType() == Material.LAVA_BUCKET) {
				island = Island.getIslandPlayerIsUsing(event.getPlayer(), true, false);
				if(island != null) {
					if(island == Island.getMainIslandFor(event.getPlayer().getUniqueId(), false) && island != Island.getIslandContaining(block.getLocation())) {
						event.setUseInteractedBlock(Result.DENY);
						event.setCancelled(true);
						return;
					}
				}
				if(island == null) {
					event.setUseInteractedBlock(Result.DENY);
					event.setCancelled(true);
					return;
				}
				event.setUseInteractedBlock(Result.ALLOW);
				event.setCancelled(false);
			}
		}
		island = Island.getIslandNearest(player.getLocation());
		if(island == null || !island.isMember(player)) {
			if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
				Block inFrontOfClicked = event.getClickedBlock().getRelative(event.getBlockFace());
				if(inFrontOfClicked != null && inFrontOfClicked.getType() == Material.ITEM_FRAME) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED.toString().concat("You do not have permission to edit item frames outside of your island."));
					return;
				}
			}
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				ItemStack heldItem = player.getInventory().getItemInMainHand();
				heldItem = heldItem == null || heldItem.getType() == Material.AIR ? player.getInventory().getItemInOffHand() : heldItem;
				if(heldItem != null && heldItem.getType().name().endsWith("_SPAWN_EGG")) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED.toString().concat("You do not have permission to place mob spawners outside of your island."));
					return;
				}
			}
		}
		if(island != null && !island.isMember(player)) {
			if(event.getClickedBlock().getType() == Material.CAKE) {
				event.setCancelled(!player.hasPermission("skyblock.buildAnywhere") && !player.hasPermission("skyblock.buildanywhere"));
				if(event.isCancelled()) {
					player.sendMessage(ChatColor.RED.toString().concat("You do not have permission to eat other people's cake! C'mon man..."));
				}
				return;
			}
			if(event.getClickedBlock().getState() instanceof Redstone) {
				event.setCancelled(event.getClickedBlock().getType().name().endsWith("_BUTTON") || event.getClickedBlock().getType().name().endsWith("LEVER") || event.getClickedBlock().getType().name().endsWith("_DOOR"));
				if(event.isCancelled()) {
					return;
				}
			}
			if(event.getAction() == Action.LEFT_CLICK_BLOCK && player.isSneaking()) {
				//if(Main.pluginMgr.getPlugin("EnteisPermissions") != null) {
				event.setCancelled(!player.hasPermission("skyblock.buildAnywhere") && !player.hasPermission("skyblock.buildanywhere"));
				if(event.isCancelled()) {
					return;
				}
				//}
			}
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Block clicked = event.getClickedBlock();
				if(clicked != null && clicked.getState() instanceof InventoryHolder) {
					if(!island.isTrusted(player)) {
						event.setCancelled(!player.hasPermission("skyblock.buildAnywhere") && !player.hasPermission("skyblock.buildanywhere"));
						if(event.isCancelled()) {
							player.sendMessage(ChatColor.RED.toString().concat("You do not have permission to open containers outside of your island."));
							return;
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onEntityInteractEvent(EntityInteractEvent event) {//XXX Crop trampling prevention - entities
		Block block = event.getBlock();
		if(block == null || (block.getLocation().getWorld() != GeneratorMain.getSkyworld() && block.getLocation().getWorld() != GeneratorMain.getSkyworldNether())) {
			return;
		}
		event.setCancelled(block.getType() == Material.FARMLAND);
	}
	
	//Don't add @EventHandler here! It is handled via the two methods below this one.
	public static final void onPlayerBucketEvent(PlayerBucketEvent event) {
		Location location = event.getBlockClicked().getLocation();
		if(location.getWorld() != GeneratorMain.getSkyworld() && location.getWorld() != GeneratorMain.getSkyworldNether()) {
			return;
		}
		event.setCancelled(false);
		Player player = event.getPlayer();
		Island island = Island.getIslandContaining(location);
		if(island == null || !island.isTrusted(player)) {
			if(!player.hasPermission("skyblock.buildAnywhere") && !player.hasPermission("skyblock.buildanywhere")) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED.toString().concat("You do not have permission to use buckets outside of your island."));
				/*Main.scheduler.runTaskLater(Main.getPlugin(), () -> {
					player.updateInventory();
					player.sendBlockChange(location, location.getBlock().getBlockData());
				}, 2L);*/
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
		onPlayerBucketEvent(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
		onPlayerBucketEvent(event);
	}
	
	//Don't add @EventHandler here! It is handled via the two methods below this one.
	public static final void onPlayerBucketEventMonitor(PlayerBucketEvent event) {
		if(event.isCancelled()) {
			Player player = event.getPlayer();
			Location location = event.getBlockClicked().getLocation();
			final int[] timesUpdated = new int[] {0, 5, -1};
			timesUpdated[2] = Main.scheduler.runTaskTimer(Main.getPlugin(), () -> {
				player.updateInventory();
				player.sendBlockChange(location, location.getBlock().getBlockData());
				timesUpdated[0]++;
				if(timesUpdated[0] >= timesUpdated[1]) {
					Main.scheduler.cancelTask(timesUpdated[2]);
				}
			}, 1L, 5L).getTaskId();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public static final void onPlayerBucketEmptyEventMonitor(PlayerBucketEmptyEvent event) {
		onPlayerBucketEventMonitor(event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public static final void onPlayerBucketFillEventMonitor(PlayerBucketFillEvent event) {
		onPlayerBucketEventMonitor(event);
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
				event.setCancelled(!Main.isInSpawn(spreading.getLocation()));
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public static final void onBlockIgniteEvent(BlockIgniteEvent event) {
		if(event.getBlock().getWorld() != GeneratorMain.getSkyworld() && event.getBlock().getWorld() != GeneratorMain.getSkyworldNether()) {
			return;
		}
		event.setCancelled(false);
		Player pyromaniac = event.getPlayer();
		if(pyromaniac == null) {
			return;
		}
		//Block igniterBlock = event.getIgnitingBlock();
		Block ignited = event.getBlock();
		Island island = Island.getIslandContaining(ignited.getLocation());
		if(island != null) {
			if(!island.isMember(pyromaniac) && !(pyromaniac.hasPermission("skyblock.buildAnywhere") || pyromaniac.hasPermission("skyblock.buildanywhere"))) {
				event.setCancelled(true);
				pyromaniac.sendMessage(ChatColor.RED.toString().concat("You do not have permission to set fire to blocks on other players' islands."));
				return;
			}
			if(event.isCancelled()) {
				event.setCancelled(false);
			}
		} else {
			event.setCancelled(!Main.isInSpawn(ignited.getLocation()));
			//if(event.getCause() == IgniteCause.FLINT_AND_STEEL) {
			if(!pyromaniac.hasPermission("skyblock.buildAnywhere") && !pyromaniac.hasPermission("skyblock.buildanywhere")) {
				event.setCancelled(true);
				pyromaniac.sendMessage(ChatColor.RED.toString().concat("You do not have permission to set fire to blocks outside of your island."));
				return;
			}
			event.setCancelled(false);
			//}
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
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onBlockPlaceEvent(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();
		Island island = Island.getIslandContaining(location);
		if(island == null || (!island.isMember(player))) {
			event.setCancelled(!player.hasPermission("skyblock.buildAnywhere") && !player.hasPermission("skyblock.buildanywhere"));
			if(event.isCancelled()) {
				player.sendMessage(ChatColor.RED.toString().concat("You do not have permission to edit blocks outside of your island."));
				return;
			}
			if(location.getWorld() == GeneratorMain.getSkyworldTheEnd()) {
				event.setCancelled(Main.isInSpawn(location));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onBlockBreakEvent(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();
		Island island = Island.getIslandContaining(location);
		if(Island.isInSkyworld(player) && (island == null || (!island.isMember(player)))) {
			if(!player.hasPermission("skyblock.buildAnywhere") && !player.hasPermission("skyblock.buildanywhere")) {
				event.setCancelled(true);
				if(location.getWorld() == GeneratorMain.getSkyworldTheEnd()) {
					event.setCancelled(Main.isInSpawn(location));
				}
				if(event.isCancelled()) {
					player.sendMessage(ChatColor.RED + "You do not have permission to edit blocks outside of your island.");
					return;
				}
			}
		}
	}
	
}
