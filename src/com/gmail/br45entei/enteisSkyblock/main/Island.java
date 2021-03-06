package com.gmail.br45entei.enteisSkyblock.main;

import com.gmail.br45entei.enteisSkyblock.challenge.Challenge;
import com.gmail.br45entei.enteisSkyblockGenerator.main.GeneratorMain;
import com.gmail.br45entei.enteisSkyblockGenerator.worldedit.WorldEditUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Horse;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Illager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/** Islands are floating chunks of land contained within a set border that
 * players can build upon.<br>
 * Players can invite other players to join them on their Islands to become
 * members.<br>
 * If a player is not a member of a particular Island, then that player
 * will(or at least should) be unable to edit anything on the Island.
 * If the owner of an Island so wishes, they can choose to lock the Island
 * so that no other players(excluding the members of course) may enter the
 * Island.
 *
 * @author Brian_Entei - The original idea for Skyblock was created by
 *         Talabrek, as far as I can tell. This is merely my take on it. */
public final class Island {
	
	protected static final String ext = ".txt";
	
	protected static final ConcurrentLinkedDeque<Island> islands = new ConcurrentLinkedDeque<>();
	
	protected static volatile int spawnTID = -1;
	
	protected static final double EVERY_TICK = 1000.0;
	protected static final double EVERY_SECOND = EVERY_TICK * 20.0;
	
	protected static volatile double spawnRate = EVERY_TICK;
	
	protected static final void stopSpawnTask() {
		state[0] = false;
		if(spawnTID != -1) {
			Main.scheduler.cancelTask(spawnTID);
			spawnTID = -1;
		}
		/*while(spawnThread != null && spawnThread.isAlive()) {
			try {
				Thread.sleep(10L);
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		spawnThread = null;*/
	}
	
	protected static final boolean[] state = new boolean[] {true};
	//protected static volatile Thread spawnThread = null;
	
	/** @return Whether or not the spawn task is running */
	public static final boolean isSpawnTaskRunning() {
		//return spawnThread != null && spawnThread.isAlive();
		return spawnTID == -1 ? false : Main.scheduler.isCurrentlyRunning(spawnTID);
	}
	
	//@SuppressWarnings("deprecation")
	protected static final void startSpawnTask() {
		stopSpawnTask();
		if("".equals("")) {//XXX Disabled mob spawning
			return;
		}
		state[0] = true;
		final boolean[] lastTaskType = new boolean[1];
		spawnTID = Main.scheduler.runTaskTimer(Main.plugin, () -> {
			//spawnThread = new Thread(() -> {
			//while(state[0]) {
			for(Island island : islands) {
				if(island.getOwner() == null || island.getMembersOnIsland().isEmpty()) {
					continue;
				}
				//Main.console.sendMessage(ChatColor.WHITE + "Attempting to spawn on island \"" + island.getID() + "(" + island.getOwnerName() + ChatColor.RESET + ChatColor.WHITE + ")\"...");
				if(lastTaskType[0]) {
					if(island.spawnAnimals) {
						int spawned = spawnAnimalsOn(island);
						if(spawned == 0) {
							spawned = spawnHostileMobsOn(island);
							lastTaskType[0] = !lastTaskType[0];
							//if(Main.server.getOfflinePlayer(island.getOwner()).isOnline()) {
							//	Main.server.getPlayer(island.getOwner()).sendMessage(".Spawned " + spawned + " hostile mobs.");
							//}
						}// else {
						//if(Main.server.getOfflinePlayer(island.getOwner()).isOnline()) {
						//	Main.server.getPlayer(island.getOwner()).sendMessage("Spawned " + spawned + " animals.");
						//}
						//}
					}
				} else {
					if(island.spawnMobs) {
						//int spawned = //
						spawnHostileMobsOn(island);
						//if(Main.server.getOfflinePlayer(island.getOwner()).isOnline()) {
						//	Main.server.getPlayer(island.getOwner()).sendMessage("Spawned " + spawned + " hostile mobs.");
						//}
					}
				}
			}
			lastTaskType[0] = !lastTaskType[0];
			/*String tickSpeed = GeneratorMain.getSkyworld().getGameRuleValue("randomTickSpeed");
			if(!Main.isInt(tickSpeed)) {
				tickSpeed = "20";
				GeneratorMain.getSkyworld().setGameRuleValue("randomTickSpeed", tickSpeed);
			}
			try {
				Thread.sleep(Math.round(Math.min(1000.0, Math.max(10.0, spawnRate * (1.0 / Integer.parseInt(tickSpeed))))));
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}*/
			//}
			//}, "Entei's Skyblock Mob Spawning Thread");
			//spawnThread.setDaemon(true);
			//spawnThread.start();
		}, 40L, 300L).getTaskId();// 300 / 20 = 15 seconds
	}
	
	/** @return A list containing all currently loaded Islands */
	public static final List<Island> getAllIslands() {
		return new ArrayList<>(islands);
	}
	
	/** Saves all loaded Islands to disk. */
	public static final void saveAllIslands() {
		int islandsSaved = 0;
		int islandsFailed = 0;
		for(Island island : islands) {
			if(island.save()) {
				islandsSaved++;
			} else {
				islandsFailed++;
				Main.console.sendMessage(ChatColor.DARK_RED + "Warning! The island " + ChatColor.WHITE + island.getID() + "(loc: " + island.getLocation().toString() + ")" + ChatColor.DARK_RED + " failed to save to file!");
			}
		}
		Main.console.sendMessage(ChatColor.GREEN + "Number of islands that saved successfully: " + ChatColor.WHITE + Integer.toString(islandsSaved) + ChatColor.GREEN + ".");
		if(islandsFailed > 0) {
			Main.console.sendMessage(ChatColor.GOLD + "Number of islands that failed to save: " + ChatColor.WHITE + Integer.toString(islandsFailed) + ChatColor.GOLD + ".");
		}
	}
	
	/** Loads all Islands from disk. If an Island is already loaded into
	 * memory, its' data is re-loaded from disk.<br>
	 * Therefore, it is necessary to {@link #saveAllIslands() save any
	 * loaded Islands} from memory to
	 * disk in order to prevent potential data loss due to overwriting. */
	public static final void loadAllIslands() {
		File folder = getSaveFolder();
		int islandsLoaded = 0;
		int islandsFailed = 0;
		for(String fileName : folder.list()) {
			if(fileName.endsWith(ext) && fileName.contains("_")) {
				String loc = fileName.substring(0, fileName.length() - ext.length());
				String[] split = loc.split(Pattern.quote("_"));
				if(isInt(split[0]) && isInt(split[1])) {
					//File file = new File(folder, fileName);
					int x = Integer.parseInt(split[0]);
					int z = Integer.parseInt(split[1]);
					Island check = null;
					for(Island island : islands) {
						if(island.x == x && island.z == z) {
							check = island;//Main.console.sendMessage(ChatColor.DARK_RED + "Warning! The island " + ChatColor.WHITE + island.getID() + ChatColor.DARK_RED + " is already loaded! Should it be loaded again?");
							break;
						}
					}
					if(check == null) {
						check = new Island(x, z);
					}
					if(check.load()) {
						islandsLoaded++;
					} else {
						islandsFailed++;
						Main.console.sendMessage(ChatColor.DARK_RED + "Warning! The island " + ChatColor.WHITE + check.getID() + "(loc: " + check.getLocation().toString() + ")" + ChatColor.DARK_RED + " failed to load! This may result in strange behaviour on that island.");
					}
				}
			}
		}
		Main.console.sendMessage(ChatColor.GREEN + "Number of islands that loaded successfully: " + ChatColor.WHITE + Integer.toString(islandsLoaded) + ChatColor.GREEN + ".");
		if(islandsFailed > 0) {
			Main.console.sendMessage(ChatColor.GOLD + "Number of islands that failed to load: " + ChatColor.WHITE + Integer.toString(islandsFailed) + ChatColor.GOLD + ".");
		}
		updateAllIslands();
	}
	
	/** Searches for and returns a list of all orphaned islands(Islands with
	 * no owner).<br>
	 * If an Island has no owner, but has members present, then the first
	 * member is promoted to be the owner and the Island is saved to disk,
	 * then skipped over.
	 * 
	 * @return A list of all orphaned Islands */
	public static final List<Island> getOrphanedIslands() {
		ArrayList<Island> list = new ArrayList<>();
		for(Island island : islands) {
			if(island.owner == null) {
				if(island.members.size() > 0) {
					island.owner = island.members.removeFirst();
					island.save();
					continue;
				}
				list.add(island);
			}
		}
		return list;
	}
	
	/** Updates all loaded Islands' WorldGuard regions */
	public static final void updateAllIslands() {
		for(Island island : islands) {
			if(island.getOwner() == null) {
				island.deleteCompletely();
				Main.getPluginLogger().info("Deleted orphaned island at ".concat(island.getID()).concat("."));
				continue;
			}
			island.update();
		}
	}
	
	/** Searches for the Island with the given coordinate IDs
	 * 
	 * @param x The x Island number<br>
	 *            Note that this is <b>not</b> a world coordinate!<br>
	 *            To get the world coordinate, multiply this number by the
	 *            island range.
	 * @param z The z Island number<br>
	 *            Note that this is <b>not</b> a world coordinate!<br>
	 *            To get the world coordinate, multiply this number by the
	 *            island range.
	 * @return The Island, if it exists, or <b>{@code null}</b> otherwise */
	public static final Island getIfExists(int x, int z) {
		for(Island island : islands) {
			if(island.x == x && island.z == z) {
				return island;
			}
		}
		return null;
	}
	
	/** Searches for the Island with the given coordinate IDs, or creates
	 * one if one wasn't found.
	 * 
	 * @param x The x Island number<br>
	 *            Note that this is <b>not</b> a world coordinate!<br>
	 *            To get the world coordinate, multiply this number by the
	 *            island range.
	 * @param z The z Island number<br>
	 *            Note that this is <b>not</b> a world coordinate!<br>
	 *            To get the world coordinate, multiply this number by the
	 *            island range.
	 * @return The Island */
	public static final Island getOrCreate(int x, int z) {
		for(Island island : islands) {
			if(island.x == x && island.z == z) {
				return island;
			}
		}
		Island island = new Island(x, z);
		if(island.getSaveFile().exists()) {
			island.load();
			island.update();
		}
		return island;
	}
	
	private static final int random(Random random, int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}
	
	/** Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int) */
	private static final int[] nextRandomID(int min, int max) {
		SecureRandom random = new SecureRandom();
		int x = random(random, min, max);
		int z = random(random, min, max);
		while(Math.abs(x) < GeneratorMain.getSpawnOffset() && Math.abs(z) < GeneratorMain.getSpawnOffset()) {
			x = random(random, min, max);
			z = random(random, min, max);
		}
		return new int[] {x, z};
	}
	
	/** @return The next far away(very isolated) Island */
	public static final Island getNextFarAwayIsland() {
		int[] id = nextRandomID(101, (29999904 / GeneratorMain.island_Range));
		Island randomlyChosen;
		while((randomlyChosen = getIfExists(id[0], id[1])) != null) {
			if(randomlyChosen.getOwner() == null) {
				break;
			}
			id = nextRandomID(101, (29999904 / GeneratorMain.island_Range));
		}
		return getOrCreate(id[0], id[1]);
	}
	
	/** @return The next randomly located(potentially isolated) Island */
	public static final Island getNextRandomlyLocatedIsland() {
		int maxRandomRange = 1000;
		int[] id = nextRandomID(0, maxRandomRange);
		int i = 0;
		Island randomlyChosen;
		while((randomlyChosen = getIfExists(id[0], id[1])) != null) {
			if(randomlyChosen.getOwner() == null) {
				randomlyChosen.deleteCompletely();
				Main.getPluginLogger().info("Deleted orphaned island at ".concat(randomlyChosen.getID()).concat("."));
				break;
			}
			id = nextRandomID(0, maxRandomRange + i);
			i++;
		}
		return getOrCreate(id[0], id[1]);
	}
	
	/** @return The next available Island near the spawn area */
	public static final Island getNextAvailableIslandNearSpawn() {
		int[] id = getNextAvailableIslandIDNearSpawn();
		Island island = getIfExists(id[0], id[1]);
		if(island != null) {
			throw new IllegalStateException("Cannot create an Island with duplicate coordinate IDs!");
		}
		return getOrCreate(id[0], id[1]);
	}
	
	protected static final int[] getNextAvailableIslandIDNearSpawn() {
		int x = 0;
		int z = GeneratorMain.getSpawnOffset();//2;
		int lastZ = z;
		int dir = 0;//0=right,1=down,2=left,3=up,4=right_check_x_equals_0
		while(true) {
			Island checkIfOrphaned = getIfExists(x, z);
			if(checkIfOrphaned != null && checkIfOrphaned.owner == null) {
				int[] id = new int[] {checkIfOrphaned.x, checkIfOrphaned.z};
				checkIfOrphaned.deleteCompletely();
				Main.getPluginLogger().info("Deleted orphaned island at ".concat(checkIfOrphaned.getID()).concat("."));
				checkIfOrphaned = null;
				return id;
			}
			boolean exists = checkIfOrphaned != null;
			if(exists) {
				switch(dir) {
				default:
				case 0:
					x++;
					if(x == lastZ) {
						dir++;
					}
					break;
				case 1:
					z--;
					if(-z == lastZ) {
						dir++;
					}
					break;
				case 2:
					x--;
					if(-x == lastZ) {
						dir++;
					}
					break;
				case 3:
					z++;
					if(z == lastZ) {
						dir++;
					}
					break;
				case 4:
					x++;
					if(x == 0) {
						lastZ++;
						z = lastZ;
						dir = 0;
					}
					break;
				}
			} else {
				return new int[] {x, z};
			}
		}
	}
	
	/*
	public static final Island getIslandPlayerIsUsing(Player player, boolean playerMustBeAMemberOfIsland, boolean playerMustOwnTheIsland) {
		Island island = Island.getIslandContaining(player.getLocation());
		Island mainIsland = Island.getMainIslandFor(player.getUniqueId(), playerMustOwnTheIsland);
		island = island == null ? mainIsland : island;
		return (playerMustOwnTheIsland && !island.isOwner(player)) || (playerMustBeAMemberOfIsland && !island.isMember(player)) ? mainIsland : island;
	}*/
	
	/** @param player The player to use
	 * @param playerMustBeAMemberOfIsland Whether or not the given player must
	 *            be a member of the returned island(player must not be visiting
	 *            an island for this to not return <tt><b>null</b></tt>)
	 * @param playerMustOwnTheIsland Whether or not the given player must own
	 *            the returned island(player must be visiting an island that
	 *            they own for this to not return <tt><b>null</b></tt>)
	 * @return The resulting Island if the above conditions are met,
	 *         <tt><b>null</b></tt> otherwise */
	public static final Island getIslandPlayerIsUsing(Player player, boolean playerMustBeAMemberOfIsland, boolean playerMustOwnTheIsland) {
		Island island = Island.getIslandContaining(player.getLocation());
		if(island != null) {
			if(playerMustOwnTheIsland && !island.isOwner(player)) {
				return Island.getMainIslandFor(player.getUniqueId(), playerMustOwnTheIsland);
			}
			if(playerMustBeAMemberOfIsland && !island.isMember(player)) {
				return Island.getMainIslandFor(player.getUniqueId(), playerMustOwnTheIsland);
			}
		}
		return island;
	}
	
	/** @param player The player whose Island will be searched for
	 * @return The player's Island, if they were a member of(or owned)
	 *         one */
	public static final Island getMainIslandFor(OfflinePlayer player) {
		return player == null ? null : getMainIslandFor(player.getUniqueId(), false);
	}
	
	/** @param player The UUID of the player whose Island will be searched
	 *            for
	 * @param onlyIfPlayerOwnsIsland Whether or not an island should only be
	 *            returned if it is owned by the given player
	 * @return The player's Island, if they were a member of(or owned)
	 *         one */
	public static final Island getMainIslandFor(UUID player, boolean onlyIfPlayerOwnsIsland) {
		if(player == null) {
			return null;
		}
		Island potentialMainIsland = null;
		for(final Island island : islands) {
			if(island.isTestIsland) {
				continue;
			}
			UUID owner = island.getOwner();
			if(owner == null) {
				Main.scheduler.runTaskLater(Main.getPlugin(), () -> {
					island.deleteCompletely();
					Main.getPluginLogger().info("Deleted orphaned island at ".concat(island.getID()).concat("."));
				}, 5L);
				continue;
			}
			if(owner.toString().equals(player.toString())) {
				potentialMainIsland = potentialMainIsland != null ? potentialMainIsland : island;
				if(island.isMainIslandFor(player)) {
					return island;
				}
			}
		}
		if(potentialMainIsland != null) {
			potentialMainIsland.setAsMainIslandFor(player);
		}
		if(!onlyIfPlayerOwnsIsland) {
			for(Island island : islands) {
				if(island.isTestIsland) {
					continue;
				}
				for(UUID member : island.getMembers()) {//XXX Do not use trusted players here.
					if(member.toString().equals(player.toString())) {
						return island;
					}
				}
			}
		}
		return potentialMainIsland;
	}
	
	/** @param player The player to check
	 * @return Whether or not the given player owns this Island and has chosen
	 *         this Island as their main one */
	public boolean isMainIslandFor(UUID player) {
		return this.isOwnersMainIsland && this.isOwner(player);
	}
	
	/** @param player The player(who must already own this Island) who has
	 *            chosen this Island as their main one
	 * @return This Island */
	public Island setAsMainIslandFor(UUID player) {
		this.isOwnersMainIsland = this.isOwner(player) ? true : this.isOwnersMainIsland;
		return this;
	}
	
	public static final List<Island> getSecondaryIslandsFor(UUID player) {
		Island mainIsland = getMainIslandFor(player, true);
		if(mainIsland == null) {
			return null;
		}
		List<Island> list = new ArrayList<>();
		boolean passedMainIslandInList = false;
		for(final Island island : islands) {
			if(island.isTestIsland) {
				continue;
			}
			if(island == mainIsland) {
				passedMainIslandInList = true;
				continue;
			}
			if(island.getOwner() == null && passedMainIslandInList) {
				Main.scheduler.runTaskLater(Main.getPlugin(), () -> {
					island.deleteCompletely();
					Main.getPluginLogger().info("Deleted orphaned island at ".concat(island.getID()).concat("."));
				}, 5L);
				continue;
			}
			for(UUID member : island.getMembers()) {
				if(member.toString().equals(player.toString())) {
					list.add(island);
				}
			}
		}
		return list;
	}
	
	/** @return A list containing all of the Islands in use that haven't reached
	 *         their member limits */
	public static final List<Island> getJoinableIslands() {
		List<Island> list = new ArrayList<>();
		for(Island check : Island.getAllIslands()) {
			if(check.getOwner() == null || check.isTestIsland) {
				continue;
			}
			if(check.getMembers().size() < check.getMemberLimit()) {
				list.add(check);
			}
		}
		return list;
	}
	
	/** @param location The location to use
	 * @return The Island owning the given location, or <b>{@code null}</b> if
	 *         the given location falls outside of any Island */
	public static final Island getIslandContaining(Location location) {
		if(Island.isInSkyworld(location) && location.getWorld() != GeneratorMain.getSkyworldTheEnd()) {
			for(Island island : Island.getAllIslands()) {
				int[] bounds = island.getBounds();
				if(location.getBlockX() >= bounds[0] && location.getBlockX() <= bounds[2] && location.getBlockZ() >= bounds[1] && location.getBlockZ() <= bounds[3]) {
					return island;
				}
			}
		}
		return null;
	}
	
	/** Uses the given location to find the Island whose bounds include(or
	 * is near) the specified location.
	 * 
	 * @param location The location to use
	 * @return The Island whose bounds include(or is near) the specified
	 *         location.<br>
	 *         <br>
	 *         Note that it is possible that an Island exists near the given
	 *         location but is not loaded, in which case this method will
	 *         return <b>{@code null}</b>. */
	public static final Island getIslandNearest(Location location) {
		Location islandLoc = getIslandLocationNearest(location);
		if(islandLoc != null) {
			int x = islandLoc.getBlockX() / GeneratorMain.island_Range;
			int z = islandLoc.getBlockZ() / GeneratorMain.island_Range;
			for(Island island : islands) {
				if(island.x == x && island.z == z) {
					return island;
				}
			}
		}
		return null;
	}
	
	/** Uses the given location to find the Island whose bounds include(or
	 * is near) the specified location.If the aforementioned Island is not
	 * loaded or does
	 * not exist, it is created.
	 * 
	 * @param location The location to use
	 * @return The Island whose bounds include(or is near) the specified
	 *         location. */
	public static final Island getOrCreateIslandNearest(Location location) {
		Location islandLoc = getIslandLocationNearest(location);
		if(islandLoc == null) {
			return null;
		}
		int x = islandLoc.getBlockX() / GeneratorMain.island_Range;
		int z = islandLoc.getBlockZ() / GeneratorMain.island_Range;
		for(Island island : islands) {
			if(island.x == x && island.z == z) {
				return island;
			}
		}
		Island island = new Island(x, z);
		if(island.getSaveFile().exists()) {
			island.load();
			island.update();
		}
		return island;
	}
	
	/** Uses the given location to find the location of an Island whose
	 * bounds include(or is near) the specified location.
	 * 
	 * @param location The location to use
	 * @return The location for an Island nearest the given location */
	public static final Location getIslandLocationNearest(Location location) {
		if(!Island.isInSkyworld(location) || location.getWorld() == GeneratorMain.getSkyworldTheEnd()) {
			return null;
		}
		if(location.getBlockX() % GeneratorMain.island_Range == 0) {
			if(location.getBlockZ() % GeneratorMain.island_Range == 0) {
				return new Location(GeneratorMain.getSkyworld(), location.getBlockX(), GeneratorMain.island_Height, location.getBlockZ());
			}
		}
		double x = location.getX();
		double z = location.getZ();
		double divX = x / GeneratorMain.island_Range;
		double divZ = z / GeneratorMain.island_Range;
		int X = Long.valueOf(Math.round(divX)).intValue() * GeneratorMain.island_Range;
		int Z = Long.valueOf(Math.round(divZ)).intValue() * GeneratorMain.island_Range;
		return new Location(GeneratorMain.getSkyworld(), X, GeneratorMain.island_Height, Z);
	}
	
	@SuppressWarnings("unchecked")
	private static final <T extends Entity> Collection<T> getNearbyEntitiesByTypeUnsafe(World world, Class<? extends T> clazz, Location loc, double radius) {
		try {
			Collection<T> list = new ArrayList<>();
			for(Entity entity : world.getNearbyEntities(loc, radius, radius, radius)) {
				if(entity != null && clazz.isAssignableFrom(entity.getClass())) {
					list.add((T) entity);
				}
			}
			return list;
		} catch(NoSuchElementException | NullPointerException | ArrayIndexOutOfBoundsException e) {//Asynchronous API access results in these
			return getNearbyEntitiesByTypeUnsafe(world, clazz, loc, radius);
		}
	}
	
	private static final Collection<? extends Entity> getNearbyEntitiesByType(World world, Class<? extends Entity> clazz, Location loc, double radius) {
		if(clazz == Monster.class) {
			Collection<Entity> list = new ArrayList<>();
			list.addAll(getNearbyEntitiesByType(world, Giant.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Zombie.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Creeper.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Skeleton.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Spider.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, ZombieVillager.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Slime.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Enderman.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, EnderDragon.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Witch.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Husk.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Stray.class, loc, radius));
			
			list.addAll(getNearbyEntitiesByType(world, Wither.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, WitherSkeleton.class, loc, radius));
			
			list.addAll(getNearbyEntitiesByType(world, MagmaCube.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Ghast.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Blaze.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, PigZombie.class, loc, radius));
			
			list.addAll(getNearbyEntitiesByType(world, Illager.class, loc, radius));
			//list.addAll(getNearbyEntitiesByType(world, Pillager.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Evoker.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, EvokerFangs.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Vex.class, loc, radius));
			return list;
		}
		if(clazz == Animals.class) {
			Collection<Entity> list = new ArrayList<>();
			list.addAll(getNearbyEntitiesByType(world, Cow.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Pig.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Sheep.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Chicken.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Horse.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, Donkey.class, loc, radius));
			list.addAll(getNearbyEntitiesByType(world, MushroomCow.class, loc, radius));
			return list;
		}
		return getNearbyEntitiesByTypeUnsafe(world, clazz, loc, radius);
	}
	
	@SuppressWarnings("unchecked")
	private static final <T extends Entity> Collection<T> getNearbyEntitiesByTypeUnsafe(World world, Class<? extends T> clazz, Location loc, double xRadius, double yRadius, double zRadius) {
		try {
			Collection<T> list = new ArrayList<>();
			for(Entity entity : world.getNearbyEntities(loc, xRadius, yRadius, zRadius)) {
				if(entity != null && clazz.isAssignableFrom(entity.getClass())) {
					list.add((T) entity);
				}
			}
			return list;
		} catch(NoSuchElementException | NullPointerException | ArrayIndexOutOfBoundsException e) {//Asynchronous API access results in these
			return getNearbyEntitiesByTypeUnsafe(world, clazz, loc, xRadius, yRadius, zRadius);
		}
	}
	
	private static final Collection<? extends Entity> getNearbyEntitiesByType(World world, Class<? extends Entity> clazz, Location loc, double xRadius, double yRadius, double zRadius) {
		if(clazz == Monster.class) {
			Collection<Entity> list = new ArrayList<>();
			list.addAll(getNearbyEntitiesByType(world, Giant.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Zombie.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Creeper.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Skeleton.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Spider.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, ZombieVillager.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Slime.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Enderman.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, EnderDragon.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Witch.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Husk.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Stray.class, loc, xRadius, yRadius, zRadius));
			
			list.addAll(getNearbyEntitiesByType(world, Wither.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, WitherSkeleton.class, loc, xRadius, yRadius, zRadius));
			
			list.addAll(getNearbyEntitiesByType(world, MagmaCube.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Ghast.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Blaze.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, PigZombie.class, loc, xRadius, yRadius, zRadius));
			
			list.addAll(getNearbyEntitiesByType(world, Illager.class, loc, xRadius, yRadius, zRadius));
			//list.addAll(getNearbyEntitiesByType(world, Pillager.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Evoker.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, EvokerFangs.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Vex.class, loc, xRadius, yRadius, zRadius));
			return list;
		}
		if(clazz == Animals.class) {
			Collection<Entity> list = new ArrayList<>();
			list.addAll(getNearbyEntitiesByType(world, Cow.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Pig.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Sheep.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Chicken.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Horse.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, Donkey.class, loc, xRadius, yRadius, zRadius));
			list.addAll(getNearbyEntitiesByType(world, MushroomCow.class, loc, xRadius, yRadius, zRadius));
			return list;
		}
		return getNearbyEntitiesByTypeUnsafe(world, clazz, loc, xRadius, yRadius, zRadius);
	}
	
	private static final Collection<? extends Entity> getEntitiesByTypeIn(Chunk chunk, Class<? extends Entity> clazz) {
		if(clazz == Monster.class) {
			Collection<Entity> list = new ArrayList<>();
			list.addAll(getEntitiesByTypeIn(chunk, Giant.class));
			list.addAll(getEntitiesByTypeIn(chunk, Zombie.class));
			list.addAll(getEntitiesByTypeIn(chunk, Creeper.class));
			list.addAll(getEntitiesByTypeIn(chunk, Skeleton.class));
			list.addAll(getEntitiesByTypeIn(chunk, Spider.class));
			list.addAll(getEntitiesByTypeIn(chunk, ZombieVillager.class));
			list.addAll(getEntitiesByTypeIn(chunk, Slime.class));
			list.addAll(getEntitiesByTypeIn(chunk, Enderman.class));
			list.addAll(getEntitiesByTypeIn(chunk, EnderDragon.class));
			list.addAll(getEntitiesByTypeIn(chunk, Witch.class));
			list.addAll(getEntitiesByTypeIn(chunk, Husk.class));
			list.addAll(getEntitiesByTypeIn(chunk, Stray.class));
			
			list.addAll(getEntitiesByTypeIn(chunk, Wither.class));
			list.addAll(getEntitiesByTypeIn(chunk, WitherSkeleton.class));
			
			list.addAll(getEntitiesByTypeIn(chunk, MagmaCube.class));
			list.addAll(getEntitiesByTypeIn(chunk, Ghast.class));
			list.addAll(getEntitiesByTypeIn(chunk, Blaze.class));
			list.addAll(getEntitiesByTypeIn(chunk, PigZombie.class));
			
			list.addAll(getEntitiesByTypeIn(chunk, Illager.class));
			//list.addAll(getEntitiesByTypeIn(chunk, Pillager.class));
			list.addAll(getEntitiesByTypeIn(chunk, Evoker.class));
			list.addAll(getEntitiesByTypeIn(chunk, EvokerFangs.class));
			list.addAll(getEntitiesByTypeIn(chunk, Vex.class));
			return list;
		}
		if(clazz == Animals.class) {
			Collection<Entity> list = new ArrayList<>();
			list.addAll(getEntitiesByTypeIn(chunk, Cow.class));
			list.addAll(getEntitiesByTypeIn(chunk, Pig.class));
			list.addAll(getEntitiesByTypeIn(chunk, Sheep.class));
			list.addAll(getEntitiesByTypeIn(chunk, Chicken.class));
			list.addAll(getEntitiesByTypeIn(chunk, Horse.class));
			list.addAll(getEntitiesByTypeIn(chunk, Donkey.class));
			list.addAll(getEntitiesByTypeIn(chunk, MushroomCow.class));
			return list;
		}
		return getEntitiesByTypeInUnsafe(chunk, clazz);
	}
	
	@SuppressWarnings("unchecked")
	private static final <T extends Entity> Collection<T> getEntitiesByTypeInUnsafe(Chunk chunk, Class<? extends T> clazz) {
		try {
			Collection<T> list = new ArrayList<>();
			for(Entity entity : chunk.getEntities()) {
				if(entity != null && clazz.isAssignableFrom(entity.getClass())) {
					list.add((T) entity);
				}
			}
			return list;
		} catch(NoSuchElementException | NullPointerException | ArrayIndexOutOfBoundsException e) {//Asynchronous API access results in these
			return getEntitiesByTypeInUnsafe(chunk, clazz);
		}
	}
	
	/** @param location The location to use
	 * @param spots The collection of spots to iterate through
	 * @return The spot closest to the given location */
	public static final int[] getSpotNearest(Location location, Collection<int[]> spots) {
		int[] closestSpot = null;
		for(int[] spot : spots) {
			if(closestSpot == null) {
				closestSpot = spot;
				continue;
			}
			if(location.distance(new Location(location.getWorld(), spot[0], spot[1], spot[2])) < location.distance(new Location(location.getWorld(), closestSpot[0], closestSpot[1], closestSpot[2]))) {
				closestSpot = spot;
			}
		}
		return closestSpot;
	}
	
	/** @param material The material to check
	 * @param clazz The entity type of the animal, may be
	 *            <b><code>null</code></b>
	 * @return Whether or not animals may spawn on top of a block with the given
	 *         material type */
	public static final boolean canAnimalSpawnOn(Material material, Class<? extends Creature> clazz) {
		switch(material) {
		case GRASS:
		case GRASS_PATH:
			return clazz == null ? true : clazz != MushroomCow.class && clazz != Wolf.class;
		case MYCELIUM:
			return clazz == null ? true : clazz == MushroomCow.class;
		case BONE_BLOCK:
			return clazz == null ? false : clazz == Wolf.class;
			//$CASES-OMITTED$
		default:
			return false;
		}
	}
	
	/** @param material The material to check
	 * @return Whether or not hostile mobs may spawn on top of a block with the
	 *         given
	 *         material type */
	public static final boolean canMobSpawnOn(Material material) {
		return !(material.name().contains("CHEST") || material.name().contains("HOPPER") || material.name().contains("STEP") || material.name().contains("SLAB") || material.name().contains("LEAVES") || material.name().contains("GLASS") || material.name().contains("GLOWSTONE") || material.name().contains("LAMP") || material.name().contains("LANTERN"));
	}
	
	/** @param material The material to check
	 * @return Whether or not hostile mobs may spawn inside of a block with the
	 *         given
	 *         material type */
	public static final boolean canMobSpawnIn(Material material) {
		switch(material) {
		case AIR:
		case TORCH:
		case SIGN:
		case WALL_SIGN:
		case LEGACY_SIGN_POST:
		case SNOW:
		case LADDER:
		case VINE:
		case COBWEB:
		case TALL_GRASS:
		case LEGACY_YELLOW_FLOWER:
		case LEGACY_RED_ROSE:
		case LEGACY_SAPLING:
		case BROWN_MUSHROOM:
		case RED_MUSHROOM:
		case LEGACY_CARPET:
		case LEGACY_DIODE_BLOCK_OFF:
		case LEGACY_DIODE_BLOCK_ON:
		case REPEATER:
		case REDSTONE_WIRE:
		case LEGACY_REDSTONE_TORCH_OFF:
		case LEGACY_REDSTONE_TORCH_ON:
		case REDSTONE_TORCH:
		case LEVER:
		case LEGACY_WOOD_PLATE:
		case STONE_PRESSURE_PLATE:
		case HEAVY_WEIGHTED_PRESSURE_PLATE:
		case LIGHT_WEIGHTED_PRESSURE_PLATE:
		case LEGACY_REDSTONE_COMPARATOR_OFF:
		case LEGACY_REDSTONE_COMPARATOR_ON:
		case COMPARATOR:
		case STONE_BUTTON:
		case WATER:
			return true;
			//$CASES-OMITTED$
		default:
			if(material.name().contains("_CARPET") || material.name().contains("_PRESSURE_PLATE") || material.name().contains("_BUTTON")) {
				return true;
			}
			return false;
		}
	}
	
	/** @param clazz The entity type to use
	 * @return A list of integer arrays representing block locations that the
	 *         given entity type may spawn at */
	public final Collection<int[]> getSpawnableBlocksFor(Class<? extends Creature> clazz) {
		Collection<int[]> spots = new ArrayList<>();
		final World world = GeneratorMain.getSkyworld();
		final int[] minXZMaxXZ = this.getBounds();
		if(Monster.class.isAssignableFrom(clazz)) {
			for(int x = minXZMaxXZ[0]; x <= minXZMaxXZ[2]; x++) {
				for(int y = 0; y < world.getMaxHeight(); y++) {
					for(int z = minXZMaxXZ[1]; z <= minXZMaxXZ[3]; z++) {
						int[] chunkXZ = Main.getWorldToChunkCoords(x, z);
						if(world.isChunkLoaded(chunkXZ[0], chunkXZ[1])) {
							try {
								Block block = world.getBlockAt(x, y, z);
								Block up1 = block == null ? null : block.getRelative(BlockFace.UP, 1);
								Block up2 = up1 == null ? null : up1.getRelative(BlockFace.UP, 1);
								if(block != null && up1 != null && up2 != null) {
									if(!canMobSpawnOn(block.getType())) {
										continue;
									}
									if(canMobSpawnIn(up1.getType()) && canMobSpawnIn(up2.getType())) {
										int lightLevel = up1.getLightFromBlocks();
										if(lightLevel <= 7 && block.getType().isSolid()) {
											Location spot = up1.getLocation();
											spots.add(new int[] {spot.getBlockX(), spot.getBlockY(), spot.getBlockZ()});
										}
									}
								}
							} catch(IllegalStateException e) {
								if(!e.getMessage().toLowerCase().contains("async")) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		} else if(Animals.class.isAssignableFrom(clazz)) {
			for(int x = minXZMaxXZ[0]; x <= minXZMaxXZ[2]; x++) {
				for(int y = 0; y < world.getMaxHeight(); y++) {
					for(int z = minXZMaxXZ[1]; z <= minXZMaxXZ[3]; z++) {
						int[] chunkXZ = Main.getWorldToChunkCoords(x, z);
						if(world.isChunkLoaded(chunkXZ[0], chunkXZ[1])) {
							try {
								Block block = world.getBlockAt(x, y, z);
								Block up1 = block == null ? null : block.getRelative(BlockFace.UP, 1);
								Block up2 = up1 == null ? null : up1.getRelative(BlockFace.UP, 1);
								if(block != null && up1 != null && up2 != null) {
									if(!canMobSpawnOn(block.getType())) {
										continue;
									}
									if(canAnimalSpawnOn(block.getType(), clazz) && canMobSpawnIn(up1.getType()) && canMobSpawnIn(up2.getType())) {
										int lightLevel = up1.getLightFromBlocks();
										if(lightLevel >= 8 && block.getType().isSolid()) {
											Location spot = up1.getLocation();
											spots.add(new int[] {spot.getBlockX(), spot.getBlockY(), spot.getBlockZ()});
										}
									}
								}
							} catch(IllegalStateException e) {
								if(!e.getMessage().toLowerCase().contains("async")) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}// else {
		//Main.console.sendMessage(ChatColor.YELLOW + "[EnteisSkyblock] Unimplemented island spawn class: " + clazz.getSimpleName() + "...");
		//}
		return spots;
	}
	
	/** The per-chunk spawn limit for hostile mobs. Note that most islands will
	 * have 72 chunks. *////** The per-Island spawn limit for hostile mobs */
	public static volatile int hostilesPerChunk = 3;//public static volatile int hostilesPerIsland = 150;
	/** The per-Island spawn limit for animals */
	public static volatile int animalsPerIsland = 50;
	/** The per-Island spawn limit for wolves */
	public static volatile int wolvesPerIsland = 12;
	
	/** @param island The Island on which hostile mobs will attempt to be
	 *            spawned
	 * @return The number of hostile mobs that will be spawned asynchronously */
	public static final int spawnHostileMobsOn(Island island) {
		return spawnHostileMobsOn(island, false, true);
	}
	
	/** @param island The Island on which hostile mobs will attempt to be
	 *            spawned
	 * @param ignoreCramming Whether or not a potential new spawn spot should be
	 *            checked to see if there are already mobs of the same type
	 *            nearby
	 * @param honorSpawnLimit Whether or not the Island spawn limit for hostile
	 *            mobs should be honored
	 * @return The number of hostile mobs that will be spawned asynchronously */
	public static final int spawnHostileMobsOn(Island island, boolean ignoreCramming, boolean honorSpawnLimit) {
		final int hostilesPerIsland = honorSpawnLimit ? (island.getChunks().size() * hostilesPerChunk) : 10000;//Island.hostilesPerIsland : 10000;
		final HashMap<double[], Class<? extends Monster>> spawnsToPerform = new HashMap<>();//int spawned = 0;
		World world = GeneratorMain.getSkyworld();
		int[] minXZMaxXZ = island.getBounds();
		double xSize = (minXZMaxXZ[2] - minXZMaxXZ[0]) / 1.95;
		double zSize = (minXZMaxXZ[3] - minXZMaxXZ[1]) / 1.95;
		final int hostilesOnIsland = getNearbyEntitiesByType(world, Monster.class, island.getLocation(), xSize, world.getMaxHeight() * 2.0, zSize).size();
		final int animalsOnIsland = getNearbyEntitiesByType(world, Animals.class, island.getLocation(), xSize, world.getMaxHeight() * 2.0, zSize).size();
		if(hostilesOnIsland >= hostilesPerIsland) {
			return spawnsToPerform.size();
		}
		if(island.areAnyMembersPresent()) {
			@SuppressWarnings("unchecked")
			Class<? extends Monster>[] classes = new Class[] {//
					Creeper.class,//
					Creeper.class,//
					Zombie.class,//
					ZombieVillager.class,//
					Skeleton.class,//
					Skeleton.class,//
					Spider.class,//
					Spider.class,//
					Enderman.class,//
					Enderman.class,//
					Witch.class,//
					Witch.class,//
					Blaze.class};
			ArrayList<Chunk> fullChunks = new ArrayList<>();
			for(int[] spot : island.getSpawnableBlocksFor(Monster.class)) {
				Block block = world.getBlockAt(spot[0], spot[1], spot[2]);
				if(fullChunks.contains(block.getChunk())) {
					continue;
				}
				int hostilesInChunk = getEntitiesByTypeIn(block.getChunk(), Monster.class).size();
				boolean crammingCheck = ignoreCramming ? true : hostilesInChunk < hostilesPerChunk;//getNearbyEntitiesByType(world, Monster.class, block.getLocation(), 6).size() < 12;
				if(!crammingCheck) {
					fullChunks.add(block.getChunk());
					continue;
				}
				//boolean crammingCheckLight = ignoreCramming ? true : getNearbyEntitiesByType(world, Monster.class, block.getLocation(), 16).size() < 16;
				if(getNearbyEntitiesByType(world, HumanEntity.class, block.getLocation(), 24).size() == 0 && getNearbyEntitiesByType(world, HumanEntity.class, block.getLocation(), 128).size() >= 1) {
					boolean canSpawn = false;
					if(world.getTime() >= 13000 && world.getTime() <= 23000) {//Night time
						canSpawn = crammingCheck;
					} else {
						canSpawn = block.getLightFromSky() <= 7 && crammingCheck;//block.getLightFromSky() <= 7 && crammingCheckLight;
					}
					if(canSpawn) {
						Class<? extends Monster> clazz = classes[Main.random.nextInt(classes.length)];
						if(checkBlockFor(clazz, block)) {
							if(clazz == Witch.class) {
								if(Main.random.nextInt(6) != 4) {
									clazz = Husk.class;
								}
							}
							if(clazz == Witch.class) {
								if(Main.random.nextInt(12) == 8) {
									clazz = Stray.class;
								}
							}
							if(clazz == ZombieVillager.class) {
								if(Main.random.nextInt(6) != 2) {
									clazz = Zombie.class;
								}
							}
							hostilesInChunk++;
							crammingCheck = ignoreCramming ? true : hostilesInChunk < hostilesPerChunk;
							if(crammingCheck) {
								Location spawn = block.getLocation().add(0.5, 0, 0.5);
								spawnsToPerform.put(new double[] {spawn.getX(), spawn.getY(), spawn.getZ()}, clazz);
							} else {
								fullChunks.add(block.getChunk());
								continue;
							}
						}
					}
				}
			}
		}
		if(!state[0] || spawnsToPerform.isEmpty()) {
			return 0;
		}
		//Main.scheduler.runTask(Main.plugin, () -> {
		int currentCount = hostilesOnIsland;
		for(Entry<double[], Class<? extends Monster>> entry : spawnsToPerform.entrySet()) {
			if(currentCount >= hostilesPerIsland) {
				break;
			}
			double[] loc = entry.getKey();
			Monster monster = world.spawn(new Location(world, loc[0], loc[1], loc[2]), entry.getValue());
			currentCount++;
			if(monster.getType() == EntityType.ZOMBIE_VILLAGER) {
				monster.setRemoveWhenFarAway(false);
			}
		}
		for(Player player : island.getMembersOnIsland()) {
			updateScoreBoardFor(player, currentCount, animalsOnIsland);
		}
		//});
		return spawnsToPerform.size();
	}
	
	/** @param island The Island on which animals will attempt to be spawned
	 * @return The number of animals that will be spawned */
	public static final int spawnAnimalsOn(final Island island) {
		return spawnAnimalsOn(island, false, true);
	}
	
	private static final void spawnWolvesOn(final Island island) {
		final ArrayList<double[]> spawnsToPerform = new ArrayList<>();
		World world = GeneratorMain.getSkyworld();
		int[] minXZMaxXZ = island.getBounds();
		double xSize = (minXZMaxXZ[2] - minXZMaxXZ[0]) / 1.95;
		double zSize = (minXZMaxXZ[3] - minXZMaxXZ[1]) / 1.95;
		int wolvesOnIsland = getNearbyEntitiesByType(world, Wolf.class, island.getLocation(), xSize, world.getMaxHeight() * 2.0, zSize).size();
		if(island.areAnyMembersPresent() && !(world.getTime() >= 13000 && world.getTime() <= 23000)) {
			for(int[] spot : island.getSpawnableBlocksFor(Wolf.class)) {
				wolvesOnIsland = getNearbyEntitiesByType(world, Wolf.class, island.getLocation(), xSize, world.getMaxHeight() * 2.0, zSize).size();
				if(wolvesOnIsland >= wolvesPerIsland) {
					return;
				}
				Block block = world.getBlockAt(spot[0], spot[1], spot[2]);
				if(getNearbyEntitiesByType(world, HumanEntity.class, block.getLocation(), 128).size() >= 1) {
					if(block.getLightFromSky() >= 8 || block.getLightFromBlocks() >= 8) {
						if(checkBlockFor(Wolf.class, block)) {
							Location spawn = block.getLocation().add(0.5, 0, 0.5);
							spawnsToPerform.add(new double[] {spawn.getX(), spawn.getY(), spawn.getZ()});
						}
					}
				}
			}
		}
		if(!state[0] || spawnsToPerform.isEmpty()) {
			return;
		}
		final int _wolvesOnIsland = wolvesOnIsland;
		//Main.scheduler.runTask(Main.plugin, () -> {
		int currentCount = _wolvesOnIsland;
		for(double[] loc : spawnsToPerform) {
			if(currentCount >= wolvesPerIsland) {
				//Main.console.sendMessage("Too many wolves on island already!");
				break;
			}
			Wolf wolf = world.spawn(new Location(world, loc[0], loc[1], loc[2]), Wolf.class);
			currentCount++;
			AttributeInstance atin = wolf.getAttribute(Attribute.GENERIC_MAX_HEALTH);
			double newMax = atin.getValue() * 2.0;
			atin.setBaseValue(newMax);
			wolf.setHealth(newMax);
			wolf.setRemoveWhenFarAway(false);
		}
		//});
	}
	
	/** @param island The Island on which animals will attempt to be spawned
	 * @param ignoreCramming Whether or not a potential new spawn spot should be
	 *            checked to see if there are already mobs of the same type
	 *            nearby
	 * @param honorSpawnLimit Whether or not the Island spawn limit for animals
	 *            should be honored
	 * @return The number of animals that will be spawned */
	public static final int spawnAnimalsOn(final Island island, boolean ignoreCramming, boolean honorSpawnLimit) {
		spawnWolvesOn(island);
		final int animalsPerIsland = honorSpawnLimit ? Island.animalsPerIsland : 3334;
		final HashMap<double[], Class<? extends Animals>> spawnsToPerform = new HashMap<>();//int spawned = 0;
		World world = GeneratorMain.getSkyworld();
		int[] minXZMaxXZ = island.getBounds();
		double xSize = (minXZMaxXZ[2] - minXZMaxXZ[0]) / 1.95;
		double zSize = (minXZMaxXZ[3] - minXZMaxXZ[1]) / 1.95;
		final int hostilesOnIsland = getNearbyEntitiesByType(world, Monster.class, island.getLocation(), xSize, world.getMaxHeight() * 2.0, zSize).size();
		final int animalsOnIsland = getNearbyEntitiesByType(world, Animals.class, island.getLocation(), xSize, world.getMaxHeight() * 2.0, zSize).size();
		if(island.areAnyMembersPresent() && !(world.getTime() >= 13000 && world.getTime() <= 23000)) {
			if(animalsOnIsland >= animalsPerIsland) {
				return spawnsToPerform.size();
			}
			@SuppressWarnings("unchecked")
			Class<? extends Animals>[] classes = new Class[] {//
					Cow.class,//
					Pig.class,//
					Sheep.class,//
					Chicken.class,//
					Horse.class,//
					Donkey.class,//
					MushroomCow.class};
			for(int[] spot : island.getSpawnableBlocksFor(Animals.class)) {
				Block block = world.getBlockAt(spot[0], spot[1], spot[2]);
				Block floor = block.getRelative(BlockFace.DOWN, 1);
				boolean crammingCheck = ignoreCramming ? true : getNearbyEntitiesByType(world, Animals.class, block.getLocation(), 12).size() < 12;
				if(getNearbyEntitiesByType(world, HumanEntity.class, block.getLocation(), 128).size() >= 1 && crammingCheck) {
					if(block.getLightFromSky() >= 8 || block.getLightFromBlocks() >= 8) {
						Class<? extends Animals> clazz = classes[Main.random.nextInt(classes.length)];
						if(clazz != MushroomCow.class && floor.getType() == Material.MYCELIUM) {
							continue;
						}
						if(clazz == MushroomCow.class && floor.getBiome() != Biome.MUSHROOM_FIELDS && floor.getBiome() != Biome.MUSHROOM_FIELD_SHORE) {
							clazz = Cow.class;
						}
						if(checkBlockFor(clazz, block)) {
							Location spawn = block.getLocation().add(0.5, 0, 0.5);
							spawnsToPerform.put(new double[] {spawn.getX(), spawn.getY(), spawn.getZ()}, clazz);
						}
					}
				}
			}
		}
		if(!state[0] || spawnsToPerform.isEmpty()) {
			return 0;
		}
		//Main.scheduler.runTask(Main.plugin, () -> {
		int currentCount = animalsOnIsland;
		for(Entry<double[], Class<? extends Animals>> entry : spawnsToPerform.entrySet()) {
			if(currentCount >= animalsPerIsland) {
				break;
			}
			double[] loc = entry.getKey();
			Location location = new Location(world, loc[0], loc[1], loc[2]);
			boolean crammingCheck = ignoreCramming ? true : getNearbyEntitiesByType(world, Animals.class, location, 12).size() < 12;
			if(!crammingCheck) {
				continue;
			}
			Class<? extends Animals> spawn = entry.getValue();
			Animals animal = world.spawn(location, spawn);
			currentCount++;
			animal.setCustomName(spawn.getSimpleName().replace("MushroomCow", "MooShroom"));
			animal.setCustomNameVisible(false);
			if(spawn == Horse.class || spawn == Donkey.class) {
				AbstractHorse horse = (AbstractHorse) animal;
				int rand = Main.random.nextInt(256);
				if(rand == 137 || rand == 186) {// 2/256 chance
					horse.getInventory().setSaddle(new ItemStack(Material.SADDLE, 1));
					if(rand == 186 && animal instanceof ChestedHorse) {// 1/256 chance
						((ChestedHorse) animal).setCarryingChest(true);
					}
					if(rand == 137 && horse.getInventory() instanceof HorseInventory) {// 1/256 chance
						((HorseInventory) horse.getInventory()).setArmor(new ItemStack(Material.DIAMOND_HORSE_ARMOR));
					}
				}
			}
		}
		for(Player player : island.getMembersOnIsland()) {
			updateScoreBoardFor(player, hostilesOnIsland, currentCount);
		}
		//});
		return spawnsToPerform.size();
	}
	
	/** @param player The player whose mob tracking scoreboard will be
	 *            updated
	 * @throws IllegalArgumentException Thrown if the given player is not a
	 *             member of this Island */
	public final void updateScoreboardFor(Player player) throws IllegalArgumentException {
		if(!this.isMember(player)) {
			throw new IllegalArgumentException("Player \"" + (player == null ? "null" : player.getName()) + "\" is not a member of this Island!");
		}
		final World world = GeneratorMain.getSkyworld();
		final int[] minXZMaxXZ = this.getBounds();
		final double xSize = (minXZMaxXZ[2] - minXZMaxXZ[0]) / 1.95;
		final double zSize = (minXZMaxXZ[3] - minXZMaxXZ[1]) / 1.95;
		Thread async = new Thread(() -> {
			final int hostilesOnIsland = getNearbyEntitiesByType(world, Monster.class, this.getLocation(), xSize, world.getMaxHeight() * 2.0, zSize).size();
			final int animalsOnIsland = getNearbyEntitiesByType(world, Animals.class, this.getLocation(), xSize, world.getMaxHeight() * 2.0, zSize).size();
			Main.scheduler.runTask(Main.plugin, () -> {
				updateScoreBoardFor(player, hostilesOnIsland, animalsOnIsland);
			});
		}, "Async Island MobTracker Scoreboard Updater Thread");
		async.setDaemon(true);
		async.start();
	}
	
	protected static final void updateScoreBoardFor(Player player, int monsters, int animals) {
		if(!isInSkyworld(player)) {
			Scoreboard check = player.getScoreboard();
			if(check != null && check.getObjective("MobTracker") != null) {
				player.setScoreboard(Main.server.getScoreboardManager().getNewScoreboard());
			}
			return;
		}
		Scoreboard sb = player.getScoreboard();
		sb = sb == null ? Main.server.getScoreboardManager().getNewScoreboard() : sb;
		Objective obj = sb.getObjective("MobTracker");
		obj = obj == null ? sb.registerNewObjective("MobTracker", "tracker") : obj;
		obj.setDisplayName(ChatColor.GOLD + "Island Mobs");
		obj.getScore(ChatColor.YELLOW + "Hostiles:").setScore(monsters);
		obj.getScore(ChatColor.GREEN + "Animals:").setScore(animals);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		player.setScoreboard(sb);
	}
	
	protected static final boolean checkBlockFor(Class<? extends LivingEntity> clazz, Block block) {
		if(Fish.class.isAssignableFrom(clazz)) {
			return block.getType() == Material.WATER;
		}
		if(!canMobSpawnIn(block.getType())) {
			return false;
		}
		if((clazz == Enderman.class || clazz == Endermite.class || clazz == Shulker.class) && (block.getBiome() != Biome.THE_END && block.getBiome() != Biome.END_BARRENS && block.getBiome() != Biome.END_HIGHLANDS && block.getBiome() != Biome.END_MIDLANDS && block.getBiome() != Biome.SMALL_END_ISLANDS)) {
			if(random(Main.random, 0, 9) > 3) {//70% chance of failure
				return false;
			}
		}
		if(clazz == Blaze.class && block.getBiome() != Biome.NETHER) {
			return false;
		}
		Block up = block.getRelative(BlockFace.UP, 1);
		if(up != null) {
			if(!canMobSpawnIn(up.getType())) {
				return false;
			}
			if(clazz == Enderman.class) {
				Block up1 = up.getRelative(BlockFace.UP, 1);
				if(up1 != null && !canMobSpawnIn(up1.getType())) {
					return false;
				}
			}
		}
		Block down = block.getRelative(BlockFace.DOWN, 1);
		if(down == null || !down.getType().isSolid() || !canMobSpawnOn(down.getType())) {
			return false;
		}
		if(clazz == Wolf.class) {
			return down.getType() == Material.BONE_BLOCK;
		}
		if(clazz == Spider.class) {
			Block left = block.getRelative(BlockFace.WEST, 1);
			Block right = block.getRelative(BlockFace.EAST, 1);
			Block front = block.getRelative(BlockFace.NORTH, 1);
			Block rear = block.getRelative(BlockFace.SOUTH, 1);
			if(left != null && right != null && front != null && rear != null) {
				Block frontLeft = left.getRelative(BlockFace.NORTH, 1);
				Block frontRight = right.getRelative(BlockFace.NORTH, 1);
				Block rearLeft = left.getRelative(BlockFace.SOUTH, 1);
				Block rearRight = right.getRelative(BlockFace.SOUTH, 1);
				if(frontLeft != null && frontRight != null && rearLeft != null && rearRight != null) {
					return left.getType().isTransparent() && right.getType().isTransparent() && front.getType().isTransparent() && rear.getType().isTransparent() && frontLeft.getType().isTransparent() && frontRight.getType().isTransparent() && rearLeft.getType().isTransparent() && rearRight.getType().isTransparent();
				}
			}
			return false;
		}
		return true;
	}
	
	private volatile long createTime;
	private volatile long lastRestartTime;
	private volatile int timesRestarted = 0;
	private volatile boolean isTestIsland = false;
	private volatile String islandType = "normal";
	
	private final int x;
	private final int z;
	
	private volatile UUID owner;
	private volatile String ownerName;
	protected final ConcurrentLinkedDeque<UUID> members = new ConcurrentLinkedDeque<>();
	protected final ConcurrentHashMap<UUID, String> membersToWipeInv = new ConcurrentHashMap<>();
	protected final ConcurrentLinkedDeque<UUID> trustedPlayers = new ConcurrentLinkedDeque<>();
	
	protected final ConcurrentLinkedDeque<UUID> visitingPlayers = new ConcurrentLinkedDeque<>();
	
	private volatile int memberLimit = 4;
	
	protected final ConcurrentHashMap<UUID, Long> memberInvitations = new ConcurrentHashMap<>();
	protected final ConcurrentHashMap<UUID, Long> memberJoinRequests = new ConcurrentHashMap<>();
	
	private volatile double level = 0x0.0p0;
	private volatile long lastLevelCalculation = -1L;
	
	private volatile Biome biome;
	private volatile boolean isLocked = false;
	private volatile boolean pvpEnabled = false;
	
	private volatile double spawnX = 0.5;
	private volatile double spawnY = 5.0;
	private volatile double spawnZ = -2.0;
	
	private volatile double warpX = Integer.MIN_VALUE;
	private volatile double warpY = Integer.MIN_VALUE;
	private volatile double warpZ = Integer.MIN_VALUE;
	private volatile float warpYaw = 0.0F;
	private volatile float warpPitch = 0.0F;
	
	private volatile boolean spawnMobs = true, spawnAnimals = false;
	
	protected final ConcurrentHashMap<String, Location> memberHomes = new ConcurrentHashMap<>();
	
	protected final ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> memberCompletedChallenges = new ConcurrentHashMap<>();
	
	private volatile int netherPortalOrientation = -1;
	private volatile int netherPortalX = Integer.MIN_VALUE;
	private volatile int netherPortalY = Integer.MIN_VALUE;
	private volatile int netherPortalZ = Integer.MIN_VALUE;
	
	private boolean isOwnersMainIsland = false;
	
	public final Island setUncompleted(OfflinePlayer member, Challenge challenge) {
		return member == null || challenge == null ? this : this.setUncompleted(member.getUniqueId(), challenge.getName());
	}
	
	/** @param member The member whose given challenge is about to be
	 *            un-completed
	 * @param challengeName The name of the challenge that the member will no
	 *            longer have completed
	 * @return This Island */
	public final Island setUncompleted(OfflinePlayer member, String challengeName) {
		return member == null || challengeName == null || challengeName.trim().isEmpty() ? this : this.setUncompleted(member.getUniqueId(), challengeName);
	}
	
	/** @param member The member whose given challenge is about to be
	 *            un-completed
	 * @param challenge The challenge that the member will no longer have
	 *            completed
	 * @return This Island */
	public final Island setUncompleted(UUID member, Challenge challenge) {
		return member == null || challenge == null ? this : this.setUncompleted(member, challenge.getName());
	}
	
	/** @param member The member who just completed a challenge
	 * @param challenge The challenge that the member just completed
	 * @return This Island */
	public final Island setCompleted(OfflinePlayer member, Challenge challenge) {
		return member == null || challenge == null ? this : this.setCompleted(member.getUniqueId(), challenge.getName());
	}
	
	/** @param member The member who just completed a challenge
	 * @param challengeName The name of the challenge that the member just
	 *            completed
	 * @return This Island */
	public final Island setCompleted(OfflinePlayer member, String challengeName) {
		return member == null || challengeName == null || challengeName.trim().isEmpty() ? this : this.setCompleted(member.getUniqueId(), challengeName);
	}
	
	/** @param member The member who just completed a challenge
	 * @param challenge The challenge that the member just completed
	 * @return This Island */
	public final Island setCompleted(UUID member, Challenge challenge) {
		return member == null || challenge == null ? this : this.setCompleted(member, challenge.getName());
	}
	
	/** @param member The member whose given challenge is about to be
	 *            un-completed
	 * @param challengeName The name of the challenge that the member will no
	 *            longer have completed
	 * @return This Island */
	public final Island setUncompleted(UUID member, String challengeName) {
		if(this.hasMemberCompleted(member, challengeName)) {
			ConcurrentHashMap<String, Integer> completedChallenges = get(member, this.memberCompletedChallenges);
			completedChallenges.put(challengeName, Integer.valueOf(0));
			return this;
		}
		/*if(this.isMember(member) && challengeName != null && !challengeName.trim().isEmpty()) {
			ConcurrentHashMap<String, Integer> completedChallenges = get(member, this.memberCompletedChallenges);
			if(completedChallenges == null) {
				completedChallenges = new ConcurrentHashMap<>();
				put(member, completedChallenges, this.memberCompletedChallenges);
			}
			completedChallenges.put(challengeName, Integer.valueOf(0));
		}*/
		return this;
	}
	
	/** @param member The member who just completed a challenge
	 * @param challengeName The name of the challenge that the member just
	 *            completed
	 * @return This Island */
	public final Island setCompleted(UUID member, String challengeName) {
		if(this.hasMemberCompleted(member, challengeName)) {
			ConcurrentHashMap<String, Integer> completedChallenges = get(member, this.memberCompletedChallenges);
			completedChallenges.put(challengeName, Integer.valueOf(completedChallenges.get(challengeName).intValue() + 1));
			return this;
		}
		if(this.isMember(member) && challengeName != null && !challengeName.trim().isEmpty()) {
			ConcurrentHashMap<String, Integer> completedChallenges = get(member, this.memberCompletedChallenges);
			if(completedChallenges == null) {
				completedChallenges = new ConcurrentHashMap<>();
				put(member, completedChallenges, this.memberCompletedChallenges);
			}
			completedChallenges.put(challengeName, Integer.valueOf(1));
			/*OfflinePlayer player = Main.server.getOfflinePlayer(member);
			Challenge challenge = Challenge.getChallengeByName(challengeName);
			if(player.isOnline() && challenge != null) {
				Main.server.broadcast(ChatColor.WHITE + player.getPlayer().getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " has just completed the \"" + ChatColor.WHITE + challenge.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "\" challenge!", Server.BROADCAST_CHANNEL_USERS);
			}*/
		}
		return this;
	}
	
	/** @param member The member to check
	 * @param challenge The challenge to check
	 * @return Whether or not the specified member has completed the specified
	 *         challenge on this Island */
	public final boolean hasMemberCompleted(OfflinePlayer member, Challenge challenge) {
		return member != null && challenge != null ? this.hasMemberCompleted(member.getUniqueId(), challenge.getName()) : false;
	}
	
	/** @param member The member to check
	 * @param challengeName The name of the challenge to check
	 * @return Whether or not the specified member has completed the specified
	 *         challenge on this Island */
	public final boolean hasMemberCompleted(OfflinePlayer member, String challengeName) {
		return member != null && challengeName != null ? this.hasMemberCompleted(member.getUniqueId(), challengeName) : false;
	}
	
	/** @param member The member to check
	 * @param challenge The challenge to check
	 * @return Whether or not the specified member has completed the specified
	 *         challenge on this Island */
	public final boolean hasMemberCompleted(UUID member, Challenge challenge) {
		return member != null && challenge != null ? this.hasMemberCompleted(member, challenge.getName()) : false;
	}
	
	/** @param member The member to check
	 * @param challengeName The name of the challenge to check
	 * @return Whether or not the specified member has completed the specified
	 *         challenge on this Island */
	public final boolean hasMemberCompleted(UUID member, String challengeName) {
		if(!this.isMember(member)) {
			put(member, null, this.memberCompletedChallenges);
			return false;
		}
		if(Challenge.getChallengeByName(challengeName) == null) {
			return false;
		}
		ConcurrentHashMap<String, Integer> completedChallenges = get(member, this.memberCompletedChallenges);
		if(completedChallenges == null) {
			completedChallenges = new ConcurrentHashMap<>();
			put(member, completedChallenges, this.memberCompletedChallenges);
			return false;
		}
		for(Entry<String, Integer> completedChallenge : completedChallenges.entrySet()) {
			if(completedChallenge.getKey().equalsIgnoreCase(challengeName) && completedChallenge.getValue().intValue() >= 1) {
				return true;
			}
		}
		return false;
	}
	
	/** @param member The member in question
	 * @param challenge The challenge in question
	 * @return The total number of times that the given player has completed the
	 *         given challenge, or <tt>-1</tt> if either parameter is
	 *         <tt><b>null</b></tt>, the player isn't a member of this island */
	public final int getNumTimesChallengeCompletedBy(OfflinePlayer member, Challenge challenge) {
		return member == null || challenge == null ? -1 : this.getNumTimesChallengeCompletedBy(member.getUniqueId(), challenge.getName());
	}
	
	/** @param member The member in question
	 * @param challengeName The challenge in question
	 * @return The total number of times that the given player has completed the
	 *         given challenge, or <tt>-1</tt> if either parameter is
	 *         <tt><b>null</b></tt>, the player isn't a member of this island,
	 *         or the given challenge name is invalid */
	public final int getNumTimesChallengeCompletedBy(OfflinePlayer member, String challengeName) {
		return member == null || challengeName == null || challengeName.trim().isEmpty() ? -1 : this.getNumTimesChallengeCompletedBy(member.getUniqueId(), challengeName);
	}
	
	/** @param member The member in question
	 * @param challenge The challenge in question
	 * @return The total number of times that the given player has completed the
	 *         given challenge, or <tt>-1</tt> if either parameter is
	 *         <tt><b>null</b></tt>, the player isn't a member of this island */
	public final int getNumTimesChallengeCompletedBy(UUID member, Challenge challenge) {
		return member == null || challenge == null ? -1 : this.getNumTimesChallengeCompletedBy(member, challenge.getName());
	}
	
	/** @param member The member in question
	 * @param challengeName The challenge in question
	 * @return The total number of times that the given player has completed the
	 *         given challenge, or <tt>-1</tt> if either parameter is
	 *         <tt><b>null</b></tt>, the player isn't a member of this island,
	 *         or the given challenge name is invalid */
	public final int getNumTimesChallengeCompletedBy(UUID member, String challengeName) {
		if(!this.isMember(member)) {
			put(member, null, this.memberCompletedChallenges);
			return -1;
		}
		if(Challenge.getChallengeByName(challengeName) == null) {
			return -1;
		}
		ConcurrentHashMap<String, Integer> completedChallenges = get(member, this.memberCompletedChallenges);
		if(completedChallenges == null) {
			completedChallenges = new ConcurrentHashMap<>();
			put(member, completedChallenges, this.memberCompletedChallenges);
			return 0;
		}
		for(Entry<String, Integer> completedChallenge : completedChallenges.entrySet()) {
			if(completedChallenge.getKey().equalsIgnoreCase(challengeName)) {
				return completedChallenge.getValue().intValue();
			}
		}
		return 0;
	}
	
	/** Adds 1 to the total number of times that the given player has completed
	 * the given challenge on this Island.
	 * 
	 * @param member The member in question
	 * @param challenge The challenge in question
	 * @return This Island */
	public final Island incrementNumTimesChallengeCompletedBy(OfflinePlayer member, Challenge challenge) {
		return member == null || challenge == null ? this : this.incrementNumTimesChallengeCompletedBy(member.getUniqueId(), challenge.getName());
	}
	
	public final Island incrementNumTimesChallengeCompletedBy(OfflinePlayer member, String challengeName) {
		return member == null || challengeName == null || challengeName.trim().isEmpty() ? this : this.incrementNumTimesChallengeCompletedBy(member.getUniqueId(), challengeName);
	}
	
	public final Island incrementNumTimesChallengeCompletedBy(UUID member, Challenge challenge) {
		return member == null || challenge == null ? this : this.incrementNumTimesChallengeCompletedBy(member, challenge.getName());
	}
	
	public final Island incrementNumTimesChallengeCompletedBy(UUID member, String challengeName) {
		return this.setNumTimesChallengeCompletedBy(member, challengeName, this.getNumTimesChallengeCompletedBy(member, challengeName) + 1);
	}
	
	public final Island decrementNumTimesChallengeCompletedBy(OfflinePlayer member, Challenge challenge) {
		return member == null || challenge == null ? this : this.decrementNumTimesChallengeCompletedBy(member.getUniqueId(), challenge.getName());
	}
	
	public final Island decrementNumTimesChallengeCompletedBy(OfflinePlayer member, String challengeName) {
		return member == null || challengeName == null || challengeName.trim().isEmpty() ? this : this.decrementNumTimesChallengeCompletedBy(member.getUniqueId(), challengeName);
	}
	
	public final Island decrementNumTimesChallengeCompletedBy(UUID member, Challenge challenge) {
		return member == null || challenge == null ? this : this.decrementNumTimesChallengeCompletedBy(member, challenge.getName());
	}
	
	/** @param member
	 * @param challengeName
	 * @return */
	public final Island decrementNumTimesChallengeCompletedBy(UUID member, String challengeName) {
		return this.setNumTimesChallengeCompletedBy(member, challengeName, this.getNumTimesChallengeCompletedBy(member, challengeName) - 1);
	}
	
	/** @param member The member in question
	 * @param challenge The challenge in question
	 * @param timesCompleted The new number of times that the given player will
	 *            have completed the given challenge
	 * @return This Island */
	public final Island setNumTimesChallengeCompletedBy(OfflinePlayer member, Challenge challenge, int timesCompleted) {
		return member == null || challenge == null ? this : this.setNumTimesChallengeCompletedBy(member.getUniqueId(), challenge.getName(), timesCompleted);
	}
	
	/** @param member The member in question
	 * @param challengeName The name of the challenge in question
	 * @param timesCompleted The new number of times that the given player will
	 *            have completed the given challenge
	 * @return This Island */
	public final Island setNumTimesChallengeCompletedBy(OfflinePlayer member, String challengeName, int timesCompleted) {
		return member == null || challengeName == null || challengeName.trim().isEmpty() ? this : this.setNumTimesChallengeCompletedBy(member.getUniqueId(), challengeName, timesCompleted);
	}
	
	/** @param member The member in question
	 * @param challenge The challenge in question
	 * @param timesCompleted The new number of times that the given player will
	 *            have completed the given challenge
	 * @return This Island */
	public final Island setNumTimesChallengeCompletedBy(UUID member, Challenge challenge, int timesCompleted) {
		return member == null || challenge == null ? this : this.setNumTimesChallengeCompletedBy(member, challenge.getName(), timesCompleted);
	}
	
	/** @param member The member in question
	 * @param challengeName The name of the challenge in question
	 * @param timesCompleted The new number of times that the given player will
	 *            have completed the given challenge
	 * @return This Island */
	public final Island setNumTimesChallengeCompletedBy(UUID member, String challengeName, int timesCompleted) {
		if(!this.isMember(member)) {
			put(member, null, this.memberCompletedChallenges);
			return this;
		}
		if(Challenge.getChallengeByName(challengeName) == null) {
			return this;
		}
		ConcurrentHashMap<String, Integer> completedChallenges = get(member, this.memberCompletedChallenges);
		if(completedChallenges == null) {
			completedChallenges = new ConcurrentHashMap<>();
			put(member, completedChallenges, this.memberCompletedChallenges);
		}
		completedChallenges.put(challengeName, Integer.valueOf(timesCompleted));
		return this;
	}
	
	/** @param member The member whose home will be returned
	 * @return The given member's home(if they don't have one, it is set to the
	 *         island spawn location), or <b><code>null</code></b> if the given
	 *         player is not a member */
	public final Location getHomeFor(UUID member) {
		if(!this.isMember(member)) {
			return null;
		}
		Location home = get(member, this.memberHomes);
		if(home == null || !this.isOnIsland(home)) {
			home = this.getSpawnLocation();
			put(member, home, this.memberHomes);
		}
		return home;
	}
	
	/** <b>Note:</b> This method will silently fail if the given location is not
	 * on this Island in the skyworld or one of its' dimensions.
	 * 
	 * @param member The member whose home will be set
	 * @param location The location of the home to set
	 * @return This Island */
	public final Island setHomeFor(UUID member, Location location) {
		if(!this.isMember(member) || !this.isOnIsland(location)) {
			return this;
		}
		put(member, location, this.memberHomes);
		return this;
	}
	
	/** @return The nether portal's orientation(0 = x-axis-aligned, 1 =
	 *         z-axis-aligned, -1 = indeterminate) */
	public final int getNetherPortalOrientation() {
		return this.netherPortalOrientation;
	}
	
	/** @return Whether or not the portal exists along the x axis */
	public final boolean isNetherPortalXAligned() {
		return this.netherPortalOrientation == 0;
	}
	
	/** @return Whether or not the portal exists along the z axis */
	public final boolean isNetherPortalZAligned() {
		return this.netherPortalOrientation == 1;
	}
	
	/** @return The location of this Island's nether portal, if it has
	 *         one. */
	public final Location getNetherPortal() {
		if(this.netherPortalX == Integer.MIN_VALUE || this.netherPortalY == Integer.MIN_VALUE || this.netherPortalZ == Integer.MIN_VALUE) {
			return null;
		}
		return Main.getNetherPortal(new Location(GeneratorMain.getSkyworld(), this.netherPortalX, this.netherPortalY, this.netherPortalZ), true);
	}
	
	/** @return The location of this Island's nether portal in the nether,
	 *         if it has
	 *         one. */
	public final Location getSkyNetherPortal() {
		int[] bounds = this.getBounds();
		for(int x = bounds[0]; x <= bounds[2]; x++) {
			for(int y = 0; y < GeneratorMain.getSkyworldNether().getMaxHeight(); y++) {
				for(int z = bounds[1]; z <= bounds[3]; z++) {
					Location portal = Main.getNetherPortal(new Location(GeneratorMain.getSkyworldNether(), x, y, z), false);
					if(portal != null) {
						return portal;
					}
				}
			}
		}
		return null;
	}
	
	/** @param portal The portal's location
	 * @return This Island */
	public final Island setNetherPortal(Location portal) {
		if(portal != null && portal.getWorld() != GeneratorMain.getSkyworld()) {
			return this;
		}
		portal = portal == null ? null : Main.findNetherPortal(portal, 6);
		if(portal == null) {
			this.netherPortalOrientation = -1;
			this.netherPortalX = Integer.MIN_VALUE;
			this.netherPortalY = Integer.MIN_VALUE;
			this.netherPortalZ = Integer.MIN_VALUE;
		} else {
			this.netherPortalOrientation = Main.getPortalOrientation(portal);
			this.netherPortalX = portal.getBlockX();
			this.netherPortalY = portal.getBlockY();
			this.netherPortalZ = portal.getBlockZ();
		}
		return this;
	}
	
	private final void copy(Island copy) {
		this.createTime = copy.createTime;
		this.lastRestartTime = copy.lastRestartTime;
		this.timesRestarted = copy.timesRestarted;
		this.isTestIsland = copy.isTestIsland;
		this.islandType = copy.islandType;
		this.owner = copy.owner;
		this.ownerName = copy.ownerName;
		this.members.clear();
		for(UUID member : copy.members) {
			this.members.add(member);
		}
		this.membersToWipeInv.clear();
		for(Entry<UUID, String> member : copy.membersToWipeInv.entrySet()) {
			this.membersToWipeInv.put(member.getKey(), member.getValue());
		}
		this.memberLimit = copy.memberLimit;
		this.memberInvitations.clear();
		this.memberInvitations.putAll(copy.memberInvitations);
		this.memberJoinRequests.clear();
		this.memberJoinRequests.putAll(copy.memberJoinRequests);
		this.level = copy.level;
		this.lastLevelCalculation = copy.lastLevelCalculation;
		this.biome = copy.biome;
		this.isLocked = copy.isLocked;
		this.pvpEnabled = copy.pvpEnabled;
		this.spawnX = copy.spawnX;
		this.spawnY = copy.spawnY;
		this.spawnZ = copy.spawnZ;
		this.warpY = copy.warpY;
		this.warpZ = copy.warpZ;
		this.warpZ = copy.warpZ;
		this.spawnMobs = copy.spawnMobs;
		this.spawnAnimals = copy.spawnAnimals;
		this.memberHomes.clear();
		this.memberHomes.putAll(copy.memberHomes);
		this.memberCompletedChallenges.clear();
		this.memberCompletedChallenges.putAll(copy.memberCompletedChallenges);
		this.netherPortalOrientation = copy.netherPortalOrientation;
		this.netherPortalX = copy.netherPortalX;
		this.netherPortalY = copy.netherPortalY;
		this.netherPortalZ = copy.netherPortalZ;
		this.isOwnersMainIsland = copy.isOwnersMainIsland;//hrmm. well, this method is really only used for saving and loading, so ehh...
	}
	
	/** Copies the given island onto this one
	 * 
	 * @param copy The Island to copy */
	private Island(Island copy) {
		this.x = copy.x;
		this.z = copy.z;
		this.copy(copy);
		islands.remove(this);
	}
	
	/** Creates a new Island.
	 * 
	 * @param x The x Island number<br>
	 *            Note that this is <b>not</b> a world coordinate!<br>
	 *            To get the world coordinate, multiply this number by the
	 *            island range.
	 * @param z The z Island number<br>
	 *            Note that this is <b>not</b> a world coordinate!<br>
	 *            To get the world coordinate, multiply this number by the
	 *            island range. */
	protected Island(int x, int z) {
		for(Island island : islands) {
			if(island.x == x && island.z == z) {
				throw new IllegalStateException("Cannot create an Island with duplicate coordinate IDs!");
			}
		}
		this.x = x;
		this.z = z;
		this.biome = Biome.OCEAN;
		islands.add(this);
	}
	
	/** @return This Island's identifier */
	public final String getID() {
		return Integer.toString(this.x) + "_" + Integer.toString(this.z);
	}
	
	/** @return The Island save folder */
	public static final File getSaveFolder() {
		File folder = new File(Main.getPlugin().getDataFolder(), "Islands");
		folder.mkdirs();
		return folder;
	}
	
	/** @return This Island's save file */
	public final File getSaveFile() {
		return new File(getSaveFolder(), this.getID() + ext);
	}
	
	/** Deletes this Island's save file.
	 * 
	 * @return This Island */
	public final Island deleteSaveFile() {
		this.getSaveFile().delete();
		return this;
	}
	
	@Deprecated
	public final void unregister() {
		islands.remove(this);
	}
	
	/** @param <T> The type of the value to get
	 * @param key The key to get
	 * @param map The map to get from
	 * @return The value stored in the map, or <b><code>null</code></b> if no
	 *         value was found */
	public static final <T> T get(UUID key, Map<String, T> map) {
		return key == null ? null : map.get(key.toString());
	}
	
	/** @param <T> The type of the value to put
	 * @param key The to use when putting
	 * @param value The value to put
	 * @param map The map to put in
	 * @return The value previously stored in the map with the key, or
	 *         <b><code>null</code></b> if there was no value */
	public static final <T> T put(UUID key, T value, Map<String, T> map) {
		if(key == null) {
			return null;
		}
		if(value == null) {
			return map.remove(key.toString());
		}
		return map.put(key.toString(), value);
	}
	
	/** Saves this Island's data to disk.
	 * 
	 * @return Whether or not the save was successful */
	public final boolean save() {
		Main.console.sendMessage("Debug: Saving island " + this.getID() + " to file \"" + this.getSaveFile().getAbsolutePath() + "\"...");
		try(PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.getSaveFile()), StandardCharsets.UTF_8), true)) {
			out.println("creationTime=" + Long.toString(this.createTime == 0 ? System.currentTimeMillis() : this.createTime));
			out.println("lastRestartTime=" + Long.toString(this.lastRestartTime));
			out.println("timesRestarted=" + Integer.toString(this.timesRestarted));
			out.println("isTestIsland=" + Boolean.toString(this.isTestIsland));
			out.println("islandType=" + ((this.islandType == null || this.islandType.isEmpty()) ? "normal" : this.islandType));
			out.println("owner=" + (this.owner == null ? "" : this.owner.toString()));
			this.ownerName = this.getOwnerName();
			out.println("ownerName=" + (this.ownerName == null ? "<unknown>" : this.ownerName));
			out.print("members=");
			int i = 1;
			int size = this.members.size();
			for(UUID member : this.members) {
				out.print(member.toString() + (i == size ? "" : ","));
				i++;
			}
			out.println();
			out.print("membersToWipeInv=");
			i = 1;
			size = this.membersToWipeInv.size();
			for(Entry<UUID, String> entry : this.membersToWipeInv.entrySet()) {
				UUID member = entry.getKey();
				String wipeMsg = entry.getValue();
				out.print(member.toString() + "|" + wipeMsg + (i == size ? "" : ","));
				i++;
			}
			out.println();
			out.print("trustedPlayers=");
			i = 1;
			size = this.trustedPlayers.size();
			for(UUID player : this.trustedPlayers) {
				out.print(player.toString() + (i == size ? "" : ","));
				i++;
			}
			out.println();
			out.println("memberLimit=" + size + "/" + Integer.toString(this.memberLimit, 10));
			out.print("memberHomes=");
			i = 1;
			size = this.getMembers().size();
			for(UUID member : this.getMembers()) {
				Location home = this.getHomeFor(member);
				out.print(member.toString() + ":" + (home.getWorld() == GeneratorMain.getSkyworld() ? "0" : (home.getWorld() == GeneratorMain.getSkyworldNether() ? "1" : "2")) + "|" + locationToString(home) + (i == size ? "" : ","));
				i++;
			}
			out.println();
			out.println("");
			out.print("memberCompletedChallenges=");
			i = 1;
			size = this.getMembers().size();
			for(UUID member : this.getMembers()) {
				ConcurrentHashMap<String, Integer> memberChallenges = get(member, this.memberCompletedChallenges);
				if(memberChallenges == null) {
					memberChallenges = new ConcurrentHashMap<>();
					put(member, memberChallenges, this.memberCompletedChallenges);
				}
				out.print(member.toString() + ":");
				int j = 1;
				int _size = memberChallenges.size();
				for(Entry<String, Integer> entry : memberChallenges.entrySet()) {
					String challenge = entry.getKey();
					Integer timesCompleted = entry.getValue();
					out.print(challenge + ";" + timesCompleted.toString() + (j == _size ? "" : "|"));
					j++;
				}
				out.print(i == size ? "" : ",");
				i++;
			}
			out.println();
			out.println("");
			out.print("memberJoinRequests=");
			Set<Entry<UUID, Long>> entries = this.memberJoinRequests.entrySet();
			i = 1;
			size = entries.size();
			for(Entry<UUID, Long> request : entries) {
				if(request.getValue() == null) {
					continue;
				}
				out.print(request.getKey().toString() + ":" + request.getValue().toString() + (i == size ? "" : ","));
				i++;
			}
			out.println();
			out.print("memberInvitations=");
			entries = this.memberInvitations.entrySet();
			i = 1;
			size = entries.size();
			for(Entry<UUID, Long> invite : entries) {
				if(invite.getValue() == null) {
					continue;
				}
				out.print(invite.getKey().toString() + ":" + invite.getValue().toString() + (i == size ? "" : ","));
				i++;
			}
			out.println();
			out.println("");
			out.println("level=" + Double.toString(this.level));
			out.println("lastLevelCalculation=" + Long.toString(this.lastLevelCalculation, 10));
			out.println("biome=" + this.biome.name());
			out.println("isLocked=" + this.isLocked);
			out.println("pvpEnabled=" + this.pvpEnabled);
			out.println("spawnX=" + Double.toString(this.spawnX));
			out.println("spawnY=" + Double.toString(this.spawnY));
			out.println("spawnZ=" + Double.toString(this.spawnZ));
			out.println("warpX=" + Double.toString(this.warpX));
			out.println("warpY=" + Double.toString(this.warpY));
			out.println("warpZ=" + Double.toString(this.warpZ));
			out.println("warpYaw=" + Float.toString(this.warpYaw));
			out.println("warpPitch=" + Float.toString(this.warpPitch));
			out.println("spawnMobs=" + this.spawnMobs);
			out.println("spawnAnimals=" + this.spawnAnimals);
			out.println("netherPortalOrientation=" + Integer.toString(this.netherPortalOrientation, 10));
			out.println("netherPortalX=" + Integer.toString(this.netherPortalX, 10));
			out.println("netherPortalY=" + Integer.toString(this.netherPortalY, 10));
			out.println("netherPortalZ=" + Integer.toString(this.netherPortalZ, 10));
			return true;
		} catch(Throwable e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static final String locationToString(Location location) {
		StringBuilder sb = new StringBuilder();
		if(location != null) {
			sb.append(Double.toString(location.getX()));
			sb.append("|");
			sb.append(Double.toString(location.getY()));
			sb.append("|");
			sb.append(Double.toString(location.getZ()));
			sb.append("|");
			sb.append(Float.toString(location.getYaw()));
			sb.append("|");
			sb.append(Float.toString(location.getPitch()));
		} else {
			sb.append("0.0|0.0|0.0|0.0|0.0");
		}
		return sb.toString();
	}
	
	private static final boolean isUUID(String s) {
		try {
			UUID.fromString(s).toString();
			return true;
		} catch(IllegalArgumentException ignored) {
			return false;
		}
	}
	
	private static final boolean isInt(String s) {
		try {
			Integer.parseInt(s, 10);
			return true;
		} catch(NumberFormatException ignored) {
			return false;
		}
	}
	
	private static final boolean isLong(String s) {
		try {
			Long.parseLong(s, 10);
			return true;
		} catch(NumberFormatException ignored) {
			return false;
		}
	}
	
	/** Tests various save/load methods
	 * 
	 * @param args Program command line arguments */
	public static final void main(String[] args) {
		String[] lines = new String[] {//
				"memberCompletedChallenges=91c2ca97-7a9f-4833-b66f-e39c9b66e690:challenge1|challenge2|challenge3",//
				"memberHomes=91c2ca97-7a9f-4833-b66f-e39c9b66e690:0|269.4891945407352|69.0|-2.520413457755905|-0.45017055|2.099991,91c2ca97-7a9f-4833-b66f-e39c9b66e690:0|269.4891945407352|69.0|-2.520413457755905|-0.45017055|2.099991",//
		};
		for(String line : lines) {
			String pname;
			String value = "";
			{
				String[] split = line.split(Pattern.quote("="));
				pname = split[0];
				for(int i = 1; i < split.length; i++) {
					value += split[i];
				}
				if(value.contains("#")) {
					value = value.substring(0, value.indexOf("#"));
				}
				value = value.trim();
			}
			if(pname.equalsIgnoreCase("memberHomes")) {//memberUUID:worldID|x|y|z|yaw|pitch,memberUUID:worldID|x|y|z|yaw|pitch,memberUUID:worldID|x|y|z|yaw|pitch
				if(value.contains("|") && value.contains(":")) {
					for(String m : value.split(Pattern.quote(","))) {
						String[] split = m.split(Pattern.quote(":"));
						String memberStr = split[0];
						if(Main.isUUID(memberStr)) {
							UUID member = UUID.fromString(memberStr);
							String homeStr = split[1];
							String[] values = homeStr.split(Pattern.quote("|"));
							boolean isValid = true;
							for(String val : values) {
								isValid &= Main.isFloat(val);
							}
							isValid &= values.length == 6;//worldID|x|y|z|yaw|pitch
							if(!isValid) {
								System.out.println("Home for member \"" + member.toString() + "\" was set to the island's spawn location.");
								continue;
							}
							int id = Integer.parseInt(values[0], 10);
							double x = Double.parseDouble(values[1]);
							double y = Double.parseDouble(values[2]);
							double z = Double.parseDouble(values[3]);
							float yaw = Float.parseFloat(values[4]);
							float pitch = Float.parseFloat(values[5]);
							System.out.println("Home for member \"" + member.toString() + "\" was set to: " + (id == 0 ? "skyworld" : (id == 1 ? "skyworld_nether" : "skyworld_the_end")) + " at " + x + "," + y + "," + z + " (" + yaw + " / " + pitch + ")");
						} else {
							System.err.println("\"" + memberStr + "\" is not a valid UUID!");
						}
					}
				} else {
					System.err.println("Line did not contain a comma, pipe character, or colon!");
				}
			} else if(pname.equalsIgnoreCase("memberCompletedChallenges")) {//memberUUID:challenge1|challenge2|challenge3,memberUUID:challenge1|challenge2|challenge3
				if(value.contains(":")) {
					for(String m : value.split(Pattern.quote(","))) {
						String[] split = m.split(Pattern.quote(":"));
						String memberStr = split[0];
						if(Main.isUUID(memberStr)) {
							UUID member = UUID.fromString(memberStr);
							System.out.print("Member \"" + member.toString() + "\"'s challenges: ");
							if(split.length > 1) {
								for(String challengeName : split[1].split(Pattern.quote("|"))) {
									if(challengeName.trim().isEmpty()) {
										continue;
									}
									System.out.print(challengeName + " ");
								}
							}
							System.out.println();
						}
					}
				}
			} else {
				System.err.println("Unknown pname: \"" + pname + "\"!");
			}
		}
		System.out.println("Scale factor of " + (Main.materialLevelDropOff * 20.0) + ": " + scaleFactor(Math.round(Main.materialLevelDropOff * 20.0)));
		System.out.println("Scale factor of 96751: " + scaleFactor(96751));
		System.out.println("Scale factor of " + (Main.materialLevelDropOff * 16.0) + ": " + scaleFactor(Math.round(Main.materialLevelDropOff * 16.0)));
		System.out.println("Scale factor of " + (Main.materialLevelDropOff * 8.0) + ": " + scaleFactor(Math.round(Main.materialLevelDropOff * 8.0)));
		System.out.println("Scale factor of " + (Main.materialLevelDropOff * 4.0) + ": " + scaleFactor(Math.round(Main.materialLevelDropOff * 4.0)));
		System.out.println("Scale factor of " + (Main.materialLevelDropOff * 2.0) + ": " + scaleFactor(Math.round(Main.materialLevelDropOff * 2.0)));
		System.out.println("Scale factor of " + (Main.materialLevelDropOff + 10.0) + ": " + scaleFactor(Math.round(Main.materialLevelDropOff + 10.0)));
		System.out.println("Scale factor of " + Main.materialLevelDropOff + ": " + scaleFactor(Math.round(Main.materialLevelDropOff)));
		System.out.println("Scale factor of 46: " + scaleFactor(46));
		System.out.println("Scale factor of 1: " + scaleFactor(1));
		System.out.println("Scale factor of 0: " + scaleFactor(0));
		
		System.out.println(Main.limitDecimalToNumberOfPlaces(calculateLevel(5000.0, 653.0, 101 * 101, 0), 8));
		System.out.println(Main.limitDecimalToNumberOfPlaces(calculateLevel(0.01, 653.0, 1, 0), 8));
	}
	
	/** Loads this Island's data from disk.
	 * 
	 * @return Whether or not the data was loaded successfully */
	public final boolean load() {
		Island dupe = new Island(this);
		Main.console.sendMessage("Debug: Loading island " + this.getID() + " from file \"" + this.getSaveFile().getAbsolutePath() + "\"...");
		try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(this.getSaveFile()), StandardCharsets.UTF_8))) {
			String line;
			int lineNum = 0;
			while((line = in.readLine()) != null) {
				lineNum++;
				line = line.trim();
				if(line.isEmpty() || line.startsWith("#") || !line.contains("=")) {
					continue;
				}
				String pname;
				String value = "";
				{
					String[] split = line.split(Pattern.quote("="));
					pname = split[0];
					for(int i = 1; i < split.length; i++) {
						value += split[i];
					}
					if(value.contains("#")) {
						value = value.substring(0, value.indexOf("#"));
					}
					value = value.trim();
				}
				if(value.isEmpty()) {
					continue;
				}
				if(pname.equalsIgnoreCase("creationTime")) {
					dupe.createTime = Long.parseLong(value);
				} else if(pname.equalsIgnoreCase("lastRestartTime")) {
					dupe.lastRestartTime = Long.parseLong(value);
				} else if(pname.equalsIgnoreCase("timesRestarted")) {
					dupe.timesRestarted = Integer.parseInt(value);
				} else if(pname.equalsIgnoreCase("isTestIsland")) {
					dupe.isTestIsland = Boolean.parseBoolean(value);
				} else if(pname.equalsIgnoreCase("islandType")) {
					dupe.islandType = value;
				} else if(pname.equalsIgnoreCase("owner")) {
					dupe.owner = UUID.fromString(value);
				} else if(pname.equalsIgnoreCase("ownerName")) {
					dupe.ownerName = value.equals("null") || value.equals("<unknown>") ? (dupe.owner != null ? Main.server.getOfflinePlayer(dupe.owner).getName() : "<unknown>") : value;
				} else if(pname.equalsIgnoreCase("members")) {
					String[] split = value.split(Pattern.quote(","));
					for(String s : split) {
						s = s.trim();
						if(isUUID(s)) {
							dupe.members.add(UUID.fromString(s));
						}
					}
				} else if(pname.equalsIgnoreCase("membersToWipeInv")) {
					String[] split = value.split(Pattern.quote(","));
					for(String s : split) {
						s = s.trim();
						if(s.contains("|")) {
							String[] entry = s.split(Pattern.quote("|"));
							s = entry[0];
							String wipeMsg = Main.stringArrayToString(entry, 1, '|');
							if(isUUID(s)) {
								dupe.membersToWipeInv.put(UUID.fromString(s), wipeMsg);
							}
						}
					}
				} else if(pname.equalsIgnoreCase("trustedPlayers")) {
					String[] split = value.split(Pattern.quote(","));
					for(String s : split) {
						s = s.trim();
						if(isUUID(s)) {
							dupe.trustedPlayers.add(UUID.fromString(s));
						}
					}
				} else if(pname.equalsIgnoreCase("memberLimit")) {
					if(value.contains("/")) {
						value = value.substring(value.indexOf("/") + 1);
					}
					dupe.memberLimit = Integer.parseInt(value, 10);
				} else if(pname.equalsIgnoreCase("memberHomes")) {//memberUUID:worldID|x|y|z|yaw|pitch,memberUUID:worldID|x|y|z|yaw|pitch,memberUUID:worldID|x|y|z|yaw|pitch
					if(value.contains("|") && value.contains(":")) {
						for(String m : value.split(Pattern.quote(","))) {
							String[] split = m.split(Pattern.quote(":"));
							String memberStr = split[0];
							if(Main.isUUID(memberStr)) {
								UUID member = UUID.fromString(memberStr);
								String homeStr = split[1];
								String[] values = homeStr.split(Pattern.quote("|"));
								boolean isValid = true;
								for(String val : values) {
									isValid &= Main.isFloat(val);
								}
								isValid &= values.length == 6;//worldID|x|y|z|yaw|pitch
								if(!isValid) {
									put(member, dupe.getSpawnLocation(), dupe.memberHomes);
									continue;
								}
								int id = Integer.parseInt(values[0], 10);
								double x = Double.parseDouble(values[1]);
								double y = Double.parseDouble(values[2]);
								double z = Double.parseDouble(values[3]);
								float yaw = Float.parseFloat(values[4]);
								float pitch = Float.parseFloat(values[5]);
								dupe.setHomeFor(member, new Location(id == 0 ? GeneratorMain.getSkyworld() : (id == 1 ? GeneratorMain.getSkyworldNether() : GeneratorMain.getSkyworldTheEnd()), x, y, z, yaw, pitch));
							}
						}
					}
				} else if(pname.equalsIgnoreCase("memberCompletedChallenges")) {//memberUUID:challenge1|challenge2|challenge3,memberUUID:challenge1|challenge2|challenge3
					if(value.contains(":")) {
						for(String m : value.split(Pattern.quote(","))) {
							String[] split = m.split(Pattern.quote(":"));
							String memberStr = split[0];
							if(Main.isUUID(memberStr)) {
								UUID member = UUID.fromString(memberStr);
								ConcurrentHashMap<String, Integer> completedChallenges = get(member, dupe.memberCompletedChallenges);
								if(completedChallenges == null) {
									completedChallenges = new ConcurrentHashMap<>();
									put(member, completedChallenges, dupe.memberCompletedChallenges);
								}
								if(split.length > 1) {
									for(String challengeName : split[1].split(Pattern.quote("|"))) {
										if(challengeName.trim().isEmpty()) {
											continue;
										}
										Integer completedCount = Integer.valueOf(1);
										if(challengeName.contains(";")) {
											String[] timesCompleted = challengeName.split(Pattern.quote(";"));
											if(timesCompleted.length >= 2) {
												challengeName = timesCompleted[0];
												if(Main.isInt(timesCompleted[1])) {
													completedCount = Integer.valueOf(timesCompleted[1]);
												}
											} else {
												continue;
											}
										}
										completedChallenges.put(challengeName, completedCount);
									}
								}
							}
						}
					}
				} else if(pname.equalsIgnoreCase("memberJoinRequests")) {
					String[] split = value.split(Pattern.quote(","));
					for(String s : split) {
						s = s.trim();
						if(!s.contains(":")) {
							continue;
						}
						String[] request = s.split(Pattern.quote(":"));
						if(!isUUID(request[0]) || !isLong(request[1])) {
							continue;
						}
						dupe.memberJoinRequests.put(UUID.fromString(request[0]), Long.valueOf(request[1], 10));
					}
				} else if(pname.equalsIgnoreCase("memberInvitations")) {
					String[] split = value.split(Pattern.quote(","));
					for(String s : split) {
						s = s.trim();
						if(!s.contains(":")) {
							continue;
						}
						String[] invite = s.split(Pattern.quote(":"));
						if(!isUUID(invite[0]) || !isLong(invite[1])) {
							continue;
						}
						dupe.memberInvitations.put(UUID.fromString(invite[0]), Long.valueOf(invite[1], 10));
					}
				} else if(pname.equalsIgnoreCase("level")) {
					dupe.level = Double.parseDouble(value);
				} else if(pname.equalsIgnoreCase("lastLevelCalculation")) {
					dupe.lastLevelCalculation = Long.parseLong(value, 10);
				} else if(pname.equalsIgnoreCase("biome")) {
					Biome check = Biome.valueOf(value.toUpperCase());
					dupe.biome = check == null ? dupe.biome : check;
				} else if(pname.equalsIgnoreCase("isLocked")) {
					dupe.isLocked = value.equalsIgnoreCase("true");
				} else if(pname.equalsIgnoreCase("pvpEnabled")) {
					dupe.pvpEnabled = value.equalsIgnoreCase("true");
				} else if(pname.equalsIgnoreCase("spawnX")) {
					dupe.spawnX = Double.parseDouble(value);
				} else if(pname.equalsIgnoreCase("spawnY")) {
					dupe.spawnY = Double.parseDouble(value);
				} else if(pname.equalsIgnoreCase("spawnZ")) {
					dupe.spawnZ = Double.parseDouble(value);
				} else if(pname.equalsIgnoreCase("warpX")) {
					dupe.warpX = Double.parseDouble(value);
				} else if(pname.equalsIgnoreCase("warpY")) {
					dupe.warpY = Double.parseDouble(value);
				} else if(pname.equalsIgnoreCase("warpZ")) {
					dupe.warpZ = Double.parseDouble(value);
				} else if(pname.equalsIgnoreCase("warpYaw")) {
					dupe.warpYaw = Float.parseFloat(value);
				} else if(pname.equalsIgnoreCase("warpPitch")) {
					dupe.warpPitch = Float.parseFloat(value);
				} else if(pname.equalsIgnoreCase("spawnMobs")) {
					dupe.spawnMobs = value.equalsIgnoreCase("true");
				} else if(pname.equalsIgnoreCase("spawnAnimals")) {
					dupe.spawnAnimals = value.equalsIgnoreCase("true");
				} else if(pname.equalsIgnoreCase("netherPortalOrientation")) {
					dupe.netherPortalOrientation = Integer.parseInt(value, 10);
				} else if(pname.equalsIgnoreCase("netherPortalX")) {
					dupe.netherPortalX = Integer.parseInt(value, 10);
				} else if(pname.equalsIgnoreCase("netherPortalY")) {
					dupe.netherPortalY = Integer.parseInt(value, 10);
				} else if(pname.equalsIgnoreCase("netherPortalZ")) {
					dupe.netherPortalZ = Integer.parseInt(value, 10);
				} else {
					Main.console.sendMessage(ChatColor.DARK_RED + "Ignoring line " + ChatColor.WHITE + lineNum + ChatColor.DARK_RED + " of island " + ChatColor.WHITE + this.getID() + ChatColor.DARK_RED + "'s save file: Unknown parameter: \"" + ChatColor.WHITE + pname + ChatColor.DARK_RED + "\": " + ChatColor.WHITE + line);
				}
			}
		} catch(Throwable e) {
			System.err.print(ChatColor.DARK_RED + "An error occurred while reading from island " + ChatColor.WHITE + this.getID() + ChatColor.DARK_RED + "'s save file: ");
			e.printStackTrace();
			return false;
		}
		this.copy(dupe);
		return true;
	}
	
	@Deprecated
	public final void setType(String type) {
		this.islandType = type;
	}
	
	public final String getType() {
		return this.islandType;
	}
	
	/** Sends a message to all of this Island's members
	 * 
	 * @param message The message to send
	 * @return This Island */
	public final Island sendMessage(String message) {
		for(UUID member : this.getMembers()) {
			OfflinePlayer player = Main.server.getOfflinePlayer(member);
			if(player.isOnline()) {
				player.getPlayer().sendMessage(ChatColor.BLUE + "[Island]: " + ChatColor.WHITE + message);
			}
		}
		return this;
	}
	
	/** @return This island's current level */
	public final double getLevel() {
		return this.level;
	}
	
	/** Calculates this Island's level
	 * 
	 * @return This island's current level */
	public final double calculateLevel() { //XXX calculateLevel
		double level = 0x0.0p0;
		Map<Material, Long> map = new HashMap<>();
		int[] bounds = this.getBounds();
		for(int x = bounds[0]; x <= bounds[2]; x++) {
			for(int y = 0; y < GeneratorMain.getSkyworld().getMaxHeight(); y++) {
				for(int z = bounds[1]; z <= bounds[3]; z++) {
					Block block = GeneratorMain.getSkyworld().getBlockAt(x, y, z);
					if(block != null) {
						countMaterials(block, map);
					}
				}
			}
		}
		if(Main.checkEntities) {
			for(Entity entity : GeneratorMain.getSkyworld().getNearbyEntities(this.getLocation(), GeneratorMain.island_Range / 2, GeneratorMain.getSkyworld().getMaxHeight() / 2, GeneratorMain.island_Range / 2)) {
				if(entity instanceof Player) {
					Player player = (Player) entity;
					if(!this.isMember(player) || (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE)) {
						continue;
					}
				}
				if(entity instanceof InventoryHolder && !(entity instanceof Monster)) {
					InventoryHolder invHolder = (InventoryHolder) entity;
					Inventory inv = invHolder.getInventory();
					if(inv != null) {
						for(ItemStack item : inv.getContents()) {
							if(item != null) {
								countMaterials(item, map, invHolder);
							}
						}
					}
				}
			}
		}
		for(Entry<Material, Long> entry : map.entrySet()) {
			if(entry.getKey() == Material.AIR) {
				continue;
			}
			level = Island.calculateLevel(entry.getKey(), entry.getValue().longValue(), level);
		}
		this.level = level;
		this.lastLevelCalculation = System.currentTimeMillis();
		return level;
	}
	
	public final Island sendDebugMsg(String message) {
		for(UUID member : this.getMembers()) {
			OfflinePlayer player = Main.server.getOfflinePlayer(member);
			if(player.isOnline()) {
				if(player.getPlayer().hasPermission("skyblock.dev") && Main.getDebugMode(player.getPlayer())) {
					player.getPlayer().sendMessage(ChatColor.BLUE + "[Island] [DEBUG] " + ChatColor.WHITE + message);
				}
			}
		}
		return this;
	}
	
	/** @return Whether or not non-member players are prohibited from
	 *         entering this Island */
	public final boolean isLocked() {
		return this.isLocked;
	}
	
	/** @param locked Whether or not to prohibit non-member players entry
	 *            onto this Island
	 * @return This Island */
	public final Island setLocked(boolean locked) {
		this.isLocked = locked;
		return this.update();
	}
	
	/** @return Whether or not Player-Versus-Player is enabled on this
	 *         Island */
	public final boolean allowPVP() {
		return this.pvpEnabled;
	}
	
	/** @param pvpAllowed Whether or not to enable Player-Versus-Player on
	 *            this Island
	 * @return This Island */
	public final Island setPVPAllowed(boolean pvpAllowed) {
		this.pvpEnabled = pvpAllowed;
		return this.update();
	}
	
	/** @param loc The location to check
	 * @return Whether or not the given location is on this Island */
	public final boolean isOnIsland(Location loc) {
		int[] bounds = this.getBounds();
		/*Player p = Main.server.getPlayer(this.getOwner());
		if(p != null && p.getName().equals("Brian_Entei")) {
			p.sendMessage("loc != null: " + (loc != null) + "; Island.isInSkyworld(loc): " + Island.isInSkyworld(loc) + "; loc.getBlockX(" + (loc == null ? Double.NaN : loc.getBlockX()) + ") >= bounds[0]: " + (loc == null ? false : loc.getBlockX() >= bounds[0])//
					+ ";\nloc.getBlockZ(" + (loc == null ? Double.NaN : loc.getBlockZ()) + ") >= bounds[1]: " + (loc == null ? false : loc.getBlockZ() >= bounds[1])//
					+ ";\nloc.getBlockX(" + (loc == null ? Double.NaN : loc.getBlockX()) + ") <= bounds[2]:" + (loc == null ? false : loc.getBlockX() <= bounds[2])//
					+ ";\nloc.getBlockZ(" + (loc == null ? Double.NaN : loc.getBlockZ()) + ") <= bounds[3]:" + (loc == null ? false : loc.getBlockZ() <= bounds[3]) + ";");
		}*/
		return loc != null && loc.getWorld() == GeneratorMain.getSkyworld() && loc.getBlockX() >= bounds[0] && loc.getBlockZ() >= bounds[1] && loc.getBlockX() <= bounds[2] && loc.getBlockZ() <= bounds[3];
	}
	
	/** @param player The player to check
	 * @return Whether or not the given player is on or close to this
	 *         Island */
	public final boolean isPlayerOnIsland(Player player) {
		Location loc = player.getLocation();
		return this.isOnIsland(loc);
	}
	
	/** @return A list containing all players that are on or close to this
	 *         Island. */
	public List<Player> getPlayersOnIsland() {
		ArrayList<Player> players = new ArrayList<>();
		for(Player player : Main.server.getOnlinePlayers()) {
			if(this.isPlayerOnIsland(player)) {
				players.add(player);
			}
		}
		return players;
	}
	
	/** @return Whether or not any members of this Island are on this Island */
	public final boolean areAnyMembersPresent() {
		for(Player player : this.getPlayersOnIsland()) {
			if(this.isMember(player)) {
				return true;
			}
		}
		return false;
	}
	
	/** @return The member limit of this Island. Once this many players have
	 *         joined the Island as members, any additional attempts to join
	 *         by other players will not be successful. */
	public final int getMemberLimit() {
		return this.memberLimit;
	}
	
	/** @param memberLimit The member limit to impose upon this
	 *            Island(default is <b>{@code 4}</b>)
	 * @return This Island */
	public final Island setMemberLimit(int memberLimit) {
		this.memberLimit = memberLimit;
		return this;
	}
	
	/** @param player The player to check for
	 * @return Whether or not the given player has been invited to this
	 *         Island */
	public final boolean hasInvitationFor(OfflinePlayer player) {
		return this.hasInvitationFor(player.getUniqueId());
	}
	
	/** @param player The player to check for
	 * @return Whether or not the given player has been invited to this
	 *         Island */
	public final boolean hasInvitationFor(UUID player) {
		for(UUID invited : this.memberInvitations.keySet()) {
			if(invited.toString().equals(player.toString())) {
				Long value = this.memberInvitations.get(invited);
				if(value == null || System.currentTimeMillis() >= value.longValue()) {
					this.memberInvitations.remove(invited);
					return false;
				}
				return true;
			}
		}
		return false;
	}
	
	/** @return Whether or not any island members have invited other players to
	 *         join the island */
	public final boolean hasAnyInvitations() {
		return !this.memberInvitations.isEmpty();
	}
	
	/** @param player The player to check for
	 * @return Whether or not the given player requested to join this
	 *         Island */
	public final boolean hasJoinRequestFrom(OfflinePlayer player) {
		return this.hasJoinRequestFrom(player.getUniqueId());
	}
	
	/** @param player The player to check for
	 * @return Whether or not the given player requested to join this
	 *         Island */
	public final boolean hasJoinRequestFrom(UUID player) {
		for(UUID joinRequester : this.memberJoinRequests.keySet()) {
			if(joinRequester.toString().equals(player.toString())) {
				Long value = this.memberJoinRequests.get(joinRequester);
				if(value == null || System.currentTimeMillis() >= value.longValue()) {
					this.memberJoinRequests.remove(joinRequester);
					return false;
				}
				return true;
			}
		}
		return false;
	}
	
	/** @return A list of player UUIDs who have requested to join this island */
	public final List<OfflinePlayer> getOnlineJoinRequests() {
		List<OfflinePlayer> joinRequestingPlayers = new ArrayList<>();
		for(Entry<UUID, Long> entry : this.memberJoinRequests.entrySet()) {
			Long value = entry.getValue();
			if(value == null || System.currentTimeMillis() >= value.longValue()) {
				this.memberJoinRequests.remove(entry.getKey());
				continue;
			}
			OfflinePlayer requester = Main.server.getOfflinePlayer(entry.getKey());
			if(requester != null && requester.isOnline()) {
				joinRequestingPlayers.add(requester);
			}
		}
		return joinRequestingPlayers;
	}
	
	/** @return Whether or not any players are requesting to join this island */
	public final boolean hasAnyJoinRequests() {
		return !this.memberJoinRequests.isEmpty();
	}
	
	/** Join request result types.
	 *
	 * @author Brian_Entei */
	public static enum JoinRequestResult {
		/** The join request matched an invitation and was accepted. */
		JOINED,
		/** The join request was sent. */
		REQUEST_ADDED,
		/** The island cannot handle any more members. */
		ISLAND_FULL,
		/** The invitation failed to send. Does the target exist? */
		FAILURE,
		/** The join request was previously sent. */
		ALREADY_REQUESTED;
	}
	
	/** Invitation send result types.
	 * 
	 * @author Brian_Entei */
	public static enum InvitationResult {
		/** The invitation was sent. */
		SENT,
		/** The invitation matched a join request and was accepted. */
		JOINED,
		/** The invitation was previously sent. */
		ALREADY_SENT,
		/** The island cannot handle any more members. */
		ISLAND_FULL,
		/** The invitation failed to send. Does the target exist? */
		FAILURE;
	}
	
	/** @param inviter The player doing the inviting
	 * @param player The player being invited
	 * @return The result of this invitation */
	public final InvitationResult sendInvitation(Player inviter, UUID player) {
		if(player == null) {
			return InvitationResult.FAILURE;
		}
		OfflinePlayer check = Main.server.getOfflinePlayer(player);
		if(check == null) {
			return InvitationResult.FAILURE;
		}
		if(this.members.size() >= this.memberLimit) {
			return InvitationResult.ISLAND_FULL;
		}
		if(this.hasJoinRequestFrom(player)) {
			this.memberJoinRequests.remove(player);
			this.addMember(player);
			return InvitationResult.JOINED;
		}
		if(this.hasInvitationFor(player)) {
			if(check.isOnline()) {
				check.getPlayer().sendMessage(ChatColor.WHITE + inviter.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " would like for you to join their island!");
				check.getPlayer().sendMessage(ChatColor.GREEN + "To accept, type \"" + ChatColor.WHITE + "/island join " + inviter.getName() + ChatColor.GREEN + "\".");
			}
			inviter.sendMessage(ChatColor.GREEN + "Your invitation was already sent. Please give the player up to a day before inviting them again.");
			this.sendMessage(ChatColor.GREEN + "Invitation re-sent to player \"" + ChatColor.WHITE + (check.isOnline() ? check.getPlayer().getDisplayName() : check.getName()) + ChatColor.RESET + ChatColor.GREEN + "\" by " + ChatColor.WHITE + inviter.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + ".");
			return InvitationResult.ALREADY_SENT;
		}
		this.memberInvitations.put(player, Long.valueOf(System.currentTimeMillis() + Math.round(Main.DAY)));
		if(check.isOnline()) {
			check.getPlayer().sendMessage(ChatColor.WHITE + inviter.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " would like for you to join their island!");
			check.getPlayer().sendMessage(ChatColor.GREEN + "To accept, type \"" + ChatColor.WHITE + "/island join " + inviter.getName() + ChatColor.GREEN + "\".");
		}
		inviter.sendMessage(ChatColor.GREEN + "Your invitation was sent successfully. Please give the player up to a day before inviting them again.");
		this.sendMessage(ChatColor.GREEN + "Invitation sent to player \"" + ChatColor.WHITE + (check.isOnline() ? check.getPlayer().getDisplayName() : check.getName()) + ChatColor.RESET + ChatColor.GREEN + "\" by " + ChatColor.WHITE + inviter.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + ".");
		return InvitationResult.SENT;
	}
	
	/** @param invited The player whose invitation is being declined
	 * @return This Island */
	public final Island removeInvitation(UUID invited) {
		if(!this.hasInvitationFor(invited)) {
			return this;
		}
		this.memberInvitations.remove(invited);
		OfflinePlayer p = Main.server.getOfflinePlayer(invited);
		this.sendMessage(ChatColor.YELLOW + "Player \"" + ChatColor.WHITE + (p.isOnline() ? p.getPlayer().getDisplayName() : p.getName()) + ChatColor.RESET + ChatColor.YELLOW + "\" has declined the invitation to join this island.");
		return this;
	}
	
	/** @param player The player requesting to join
	 * @return The join request result */
	public final JoinRequestResult addJoinRequest(UUID player) {
		if(player == null) {
			return JoinRequestResult.FAILURE;
		}
		OfflinePlayer check = Main.server.getOfflinePlayer(player);
		if(check == null) {
			return JoinRequestResult.FAILURE;
		}
		if(this.members.size() >= this.memberLimit) {
			return JoinRequestResult.ISLAND_FULL;
		}
		if(this.hasInvitationFor(player)) {
			this.memberInvitations.remove(player);
			this.addMember(player);
			return JoinRequestResult.JOINED;
		}
		if(this.hasJoinRequestFrom(player)) {
			this.sendMessage(ChatColor.WHITE + (check.isOnline() ? check.getPlayer().getDisplayName() + ChatColor.RESET : check.getName()) + ChatColor.RESET + ChatColor.GREEN + " would like to join the island.");
			this.sendMessage(ChatColor.GREEN + "To accept, type \"" + ChatColor.WHITE + "/island invite " + check.getName() + ChatColor.GREEN + "\".");
			if(check.isOnline()) {
				check.getPlayer().sendMessage(ChatColor.GREEN + "Your request was already sent. Please wait up to a day to be accepted before trying again.");
			}
			return JoinRequestResult.ALREADY_REQUESTED;
		}
		this.memberJoinRequests.put(player, Long.valueOf(System.currentTimeMillis() + Math.round(Main.DAY)));
		this.sendMessage(ChatColor.WHITE + (check.isOnline() ? check.getPlayer().getDisplayName() + ChatColor.RESET : check.getName()) + ChatColor.RESET + ChatColor.GREEN + " would like to join the island.");
		this.sendMessage(ChatColor.GREEN + "To accept, type \"" + ChatColor.WHITE + "/island invite " + check.getName() + ChatColor.GREEN + "\".");
		if(check.isOnline()) {
			check.getPlayer().sendMessage(ChatColor.GREEN + "Your request was sent successfully. Please wait up to a day to be accepted before trying again.");
		}
		return JoinRequestResult.REQUEST_ADDED;
	}
	
	/** Attempts to add the given player as a member to this Island.<br>
	 * If this Island has hit its' {@link #getMemberLimit() member limit},
	 * then the player will not be added.
	 * 
	 * @param player The player to add to this Island
	 * @return Whether or not the player was added */
	public final boolean addMember(OfflinePlayer player) {
		return this.addMember(player.getUniqueId());
	}
	
	/** Attempts to add the given player as a member to this Island.<br>
	 * If this Island has hit its' {@link #getMemberLimit() member limit},
	 * then the player will not be added.
	 * 
	 * @param member The player to add to this Island
	 * @return Whether or not the player was added */
	public final boolean addMember(UUID member) {
		return this.addMember(member, false);
	}
	
	public final boolean addMember(UUID member, boolean force) {
		if(!force && this.members.size() >= this.memberLimit) {
			return false;
		}
		for(UUID check : this.members) {
			if(check.toString().equals(member.toString())) {
				return true;
			}
		}
		if(member != this.owner) {
			this.members.add(member);
		}
		this.update();
		OfflinePlayer player = Main.server.getOfflinePlayer(member);
		if(player.isOnline()) {
			player.getPlayer().setBedSpawnLocation(this.getSpawnLocation(), true);
		}
		return true;
	}
	
	/** Revokes the given player's membership to this Island.
	 * 
	 * @param player The player to remove from this Island
	 * @return Whether or not the player was removed */
	public final boolean removeMember(OfflinePlayer player) {
		return player == null ? false : this.removeMember(player.getUniqueId());
	}
	
	/** Revokes the given player's membership to this Island.
	 * 
	 * @param member The player to remove from this Island
	 * @return Whether or not the player was removed */
	public final boolean removeMember(UUID member) {
		if(member == null) {
			return false;
		}
		this.wipeMembersInventory(member, ChatColor.YELLOW + "You have been kicked from the island.");
		boolean removed = this.members.remove(member);
		this.update();
		OfflinePlayer player = Main.server.getOfflinePlayer(member);
		if(player.isOnline()) {
			player.getPlayer().setBedSpawnLocation(GeneratorMain.getSkyworldSpawnLocation(), true);
		}
		return removed;
	}
	
	/** @param player The player to check
	 * @return Whether or not the given player is a member of this Island */
	public final boolean isMember(OfflinePlayer player) {
		if(player == null) {
			return false;
		}
		for(UUID member : this.members) {
			if(player.getUniqueId().toString().equals(member.toString())) {
				return true;
			}
		}
		return this.isOwner(player);
	}
	
	/** @param player The UUID of the player to check
	 * @return Whether or not the given player is a member of this Island */
	public final boolean isMember(UUID player) {
		if(player == null) {
			return false;
		}
		for(UUID member : this.members) {
			if(player.toString().equals(member.toString())) {
				return true;
			}
		}
		return this.isOwner(player);
	}
	
	/** Marks the given player as trusted on this Island.
	 * 
	 * @param player The player to add to this Island
	 * @return This Island */
	public final Island addTrusted(OfflinePlayer player) {
		return this.addTrusted(player.getUniqueId());
	}
	
	/** Marks the given player as trusted on this Island.
	 * 
	 * @param trusted The player to add to this Island
	 * @return This Island */
	public final Island addTrusted(UUID trusted) {
		if(this.isMember(trusted)) {
			return this;
		}
		if(!this.isTrusted(trusted)) {
			this.trustedPlayers.add(trusted);
			this.update();
		}
		return this;
	}
	
	/** Removes the trusted status of the given player for this Island.
	 * 
	 * @param player The player
	 * @return This Island */
	public final Island removeTrusted(OfflinePlayer player) {
		return player == null ? this : this.removeTrusted(player.getUniqueId());
	}
	
	/** Removes the trusted status of the given player for this Island.
	 * 
	 * @param member The player
	 * @return This Island */
	public final Island removeTrusted(UUID member) {
		if(member == null || this.isMember(member)) {
			return this;
		}
		if(this.isTrusted(member)) {
			this.trustedPlayers.remove(member);
			this.update();
		}
		return this;
	}
	
	/** @param player The player to check
	 * @return Whether or not the given player is trusted on this Island */
	public final boolean isTrusted(OfflinePlayer player) {
		if(player == null) {
			return false;
		}
		for(UUID trusted : this.trustedPlayers) {
			if(player.getUniqueId().toString().equals(trusted.toString())) {
				return true;
			}
		}
		return this.isMember(player);
	}
	
	/** @param player The UUID of the player to check
	 * @return Whether or not the given player is trusted on this Island */
	public final boolean isTrusted(UUID player) {
		if(player == null) {
			return false;
		}
		for(UUID trusted : this.trustedPlayers) {
			if(player.toString().equals(trusted.toString())) {
				return true;
			}
		}
		return this.isMember(player);
	}
	
	/** @param player The player
	 * @return The Island that the given player is on, or
	 *         <b><code>null</code></b> of they aren't on one */
	public static final Island getIslandPlayerIsVisiting(Player player) {
		if(player == null || !isInSkyworld(player.getLocation())) {
			return null;
		}
		for(Island island : islands) {
			if(island.isVisiting(player)) {
				return island;
			}
		}
		return null;
	}
	
	/** @param player The player to check
	 * @return Whether or not the given player is visiting this Island */
	public final boolean isVisiting(Player player) {
		for(UUID visitor : this.visitingPlayers) {
			OfflinePlayer check = Main.server.getOfflinePlayer(visitor);
			if(check == null || !check.isOnline()) {
				this.visitingPlayers.remove(visitor);
				continue;
			}
			if(check.getUniqueId().toString().equals(player.getUniqueId().toString())) {
				return true;
			}
		}
		return false;
	}
	
	/** @param visitor The player to mark as visiting this Island
	 * @return This Island */
	public final Island addVisitor(Player visitor) {
		removeVisitor(visitor);
		this.visitingPlayers.add(visitor.getUniqueId());
		return this;
	}
	
	/** @param player The player to mark as not visiting any Island */
	public static final void removeVisitor(Player player) {
		for(Island island : islands) {
			for(UUID visitor : island.visitingPlayers) {
				if(visitor.toString().equals(player.getUniqueId().toString())) {
					island.visitingPlayers.remove(visitor);
				}
			}
		}
	}
	
	/** @param player The player to check
	 * @return Whether or not the given player is this Island's owner */
	public final boolean isOwner(OfflinePlayer player) {
		if(player == null) {
			return false;
		}
		if(this.owner != null) {
			return this.owner.toString().equals(player.getUniqueId().toString());
		}
		return false;
	}
	
	/** @param player The UUID of the player to check
	 * @return Whether or not the given player is this Island's owner */
	public final boolean isOwner(UUID player) {
		if(player == null) {
			return false;
		}
		if(this.owner != null) {
			return this.owner.toString().equals(player.toString());
		}
		return false;
	}
	
	/** @return The UUID of the owner of this Island */
	public final UUID getOwner() {
		return this.owner;
	}
	
	/** @return This Island's owner's skull item */
	public final ItemStack getOwnerSkull() {
		return this.getOwnerSkull(null);
	}
	
	/** @param displayName The name to display on the item
	 * @return This Island's owner's skull item, named with the given name */
	public final ItemStack getOwnerSkull(String displayName) {
		if(this.owner == null) {
			return null;
		}
		OfflinePlayer player = Main.server.getOfflinePlayer(this.owner);
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) Main.server.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
		meta.setOwningPlayer(player);
		if(displayName != null) {
			meta.setDisplayName(displayName);
		}
		skull.setItemMeta(meta);
		return skull;
	}
	
	/** @return This Island's owner's name(may contain chat color) */
	public final String getOwnerName() {
		if(this.owner != null) {
			OfflinePlayer owner = Main.server.getOfflinePlayer(this.owner);
			if(owner.isOnline()) {
				this.ownerName = owner.getPlayer().getDisplayName();
			} else {
				this.ownerName = owner.getName();
			}
			if(this.ownerName == null || this.ownerName.equals("null")) {
				this.ownerName = "<unknown>";
			}
			return this.ownerName;
		}
		this.ownerName = "no one";
		return this.ownerName;
	}
	
	/** @return This Island's owner's raw name */
	public final String getOwnerNamePlain() {
		return this.owner == null ? this.getOwnerName() : Main.server.getOfflinePlayer(this.owner).getName();
	}
	
	/** @param owner The UUID of the player who will own this Island
	 * @return This Island */
	public final Island setOwner(UUID owner) {
		if(owner == null) {
			String oldOwner = this.getOwnerNamePlain();
			this.owner = null;
			this.ownerName = "no one";
			this.isTestIsland = false;
			Main.getPluginLogger().info("The island at ".concat(this.getID()).concat(" is now orphaned. It used to belong to \"").concat(oldOwner == null ? "<no one>" : oldOwner).concat("\"."));
			return this;
		}
		OfflinePlayer player = Main.server.getOfflinePlayer(owner);
		return this.setOwner(player);
	}
	
	/** @param player The player who will own this Island
	 * @return This Island */
	public final Island setOwner(OfflinePlayer player) {
		if((this.owner == null ? "" : this.owner.toString()).equals(player.getUniqueId().toString())) {
			return this.update();
		}
		this.isTestIsland = false;
		UUID oldOwner = this.owner;
		this.owner = player.getUniqueId();
		this.ownerName = player.getName();
		this.members.remove(this.owner);
		if(oldOwner != null) {
			this.members.addFirst(oldOwner);
		}
		if(player.isOnline()) {
			player.getPlayer().setBedSpawnLocation(this.getSpawnLocation(), true);
		}
		return this.update();
	}
	
	@Deprecated
	public final Island setOwnerName(String name) {
		this.ownerName = name == null ? this.ownerName : name;
		return this;
	}
	
	/** @param owner The UUID of the player who will own this Island
	 * @return This Island */
	public final Island setTestOwner(UUID owner) {
		OfflinePlayer player = Main.server.getOfflinePlayer(owner);
		return this.setTestOwner(player);
	}
	
	/** @param player The player who will own this Island
	 * @return This Island */
	public final Island setTestOwner(OfflinePlayer player) {
		if((this.owner == null ? "" : this.owner.toString()).equals(player.getUniqueId().toString())) {
			return this.update();
		}
		this.isTestIsland = true;
		UUID oldOwner = this.owner;
		this.owner = player.getUniqueId();
		this.ownerName = player.getName();
		this.members.remove(this.owner);
		if(oldOwner != null) {
			this.members.addFirst(oldOwner);
		}
		return this.update();
	}
	
	/** @return Whether or not this Island is a testing Island */
	public final boolean isTestIsland() {
		return this.isTestIsland && this.owner != null;
	}
	
	/** @param check The location to check
	 * @return Whether or not the given location is within the skyworld's spawn
	 *         area. */
	public static final boolean isWithinSpawnArea(Location check) {
		if(isInSkyworld(check)) {
			double x = 0, z = 0;
			if(check.getX() < 0) {
				x = Math.abs(check.getX()) - 1;
			} else {
				x = check.getX();
			}
			if(check.getZ() < 0) {
				z = Math.abs(check.getZ()) - 1;
			} else {
				z = check.getZ();
			}
			return x <= GeneratorMain.getSpawnRegion() && z <= GeneratorMain.getSpawnRegion();
		}
		return false;
	}
	
	/** @return Whether or not this island is within the skyworld's spawn
	 *         area. */
	public final boolean isWithinSpawnArea() {
		return isWithinSpawnArea(this.getLocation());
	}
	
	/** @return This Island's world location */
	public final Location getLocation() {
		return new Location(GeneratorMain.getSkyworld(), this.x * GeneratorMain.island_Range, GeneratorMain.island_Height, this.z * GeneratorMain.island_Range);
	}
	
	/** @return This Island's warp location, if it has one set, or its'
	 *         default spawn location otherwise */
	public final Location getWarpLocation() {
		if(this.warpX == Integer.MIN_VALUE || this.warpY == Integer.MIN_VALUE || this.warpZ == Integer.MIN_VALUE) {
			return this.getSpawnLocation();
		}
		return new Location(GeneratorMain.getSkyworld(), this.warpX, this.warpY, this.warpZ, this.warpYaw, this.warpPitch);
	}
	
	/** <b>Note:</b> This will fail silently if the location is
	 * <b></code>null</code></b> or not on this Island
	 * 
	 * @param location The new warp location for this Island
	 * @return This Island */
	public final Island setWarpLocation(Location location) {
		if(this.isOnIsland(location)) {
			this.warpX = location.getX();
			this.warpY = location.getY();
			this.warpZ = location.getZ();
			this.warpYaw = location.getYaw();
			this.warpPitch = location.getPitch();
		}
		return this;
	}
	
	/** @return This Island's default spawn location */
	public final Location getSpawnLocation() {
		Location loc = this.getLocation();
		return new Location(GeneratorMain.getSkyworld(), loc.getBlockX() + this.spawnX, loc.getBlockY() + this.spawnY, loc.getBlockZ() + this.spawnZ);
	}
	
	/** @return A list of this Island's members, including the owner */
	public final List<UUID> getMembers() {
		ArrayList<UUID> members = new ArrayList<>(this.members);
		if(this.owner != null) {
			members.add(this.owner);
		}
		return members;
	}
	
	/** @return A list of this Island's online members that are currently on
	 *         this Island */
	public final List<Player> getMembersOnIsland() {
		List<Player> list = new ArrayList<>();
		for(UUID member : this.getMembers()) {
			OfflinePlayer player = Main.server.getOfflinePlayer(member);
			if(player.isOnline() && this.isPlayerOnIsland(player.getPlayer())) {
				list.add(player.getPlayer());
			}
		}
		return list;
	}
	
	/** @return A list of this Island's online members that are not currently on
	 *         this Island */
	public final List<Player> getMembersNotOnIsland() {
		List<Player> list = new ArrayList<>();
		for(UUID member : this.getMembers()) {
			OfflinePlayer player = Main.server.getOfflinePlayer(member);
			if(player.isOnline() && !this.isPlayerOnIsland(player.getPlayer())) {
				list.add(player.getPlayer());
			}
		}
		return list;
	}
	
	/** @return An array containing this Island's member names(not including the
	 *         owner's name) */
	public final String[] getMemberNames() {
		int length = 0;
		for(UUID member : this.members) {
			OfflinePlayer m = Main.server.getPlayer(member);
			if(m != null) {
				length++;
			}
		}
		String[] names = new String[length];
		int i = 0;
		for(UUID member : this.members) {
			OfflinePlayer m = Main.server.getPlayer(member);
			if(m != null) {
				names[i++] = m.isOnline() ? m.getPlayer().getDisplayName() : m.getName();
			}
		}
		return names;
	}
	
	/** @return A list of this Island's trusted players */
	public final List<UUID> getTrustedPlayers() {
		return new ArrayList<>(this.trustedPlayers);
	}
	
	/** @return An array containing this Island's trusted player names */
	public final String[] getTrustedPlayerNames() {
		int length = 0;
		for(UUID trusted : this.trustedPlayers) {
			OfflinePlayer m = Main.server.getOfflinePlayer(trusted);
			if(m != null) {
				length++;
			}
		}
		String[] names = new String[length];
		int i = 0;
		for(UUID trusted : this.trustedPlayers) {
			OfflinePlayer m = Main.server.getOfflinePlayer(trusted);
			if(m != null) {
				names[i++] = m.isOnline() ? m.getPlayer().getDisplayName() : m.getName();
			}
		}
		return names;
	}
	
	/** @return Whether or not this Island has reached its capacity of
	 *         members */
	public final boolean hasReachedMemberCapacity() {
		return this.members.size() >= this.memberLimit;
	}
	
	/** @return Whether or not this island has hostile mob spawning enabled */
	public final boolean isMobSpawningEnabled() {
		return this.spawnMobs;
	}
	
	/** @param spawnMobs Whether or not this island will have hostile mob
	 *            spawning enabled
	 * @return This Island */
	public final Island setMobSpawningEnabled(boolean spawnMobs) {
		this.spawnMobs = spawnMobs;
		return this;
	}
	
	/** @return Whether or not this island has animal spawning enabled */
	public final boolean isAnimalSpawningEnabled() {
		return this.spawnAnimals;
	}
	
	/** @param spawnAnimals Whether or not this island will have animal spawning
	 *            enabled
	 * @return This Island */
	public final Island setAnimalSpawningEnabled(boolean spawnAnimals) {
		this.spawnAnimals = spawnAnimals;
		return this;
	}
	
	/** @param environment The dimension to get the region for
	 * @return This Island's WorldGuard region, if one has been created
	 *         with {@link #update()} beforehand */
	public final Object getRegion(Environment environment) {
		if(Main.getWorldGuard() == null) {
			Main.getPlugin().getLogger().warning("WorldGuard not detected! Unable to obtain region information for island (".concat(this.getID()).concat(")..."));
			return null;
		}
		if(Main.getWorldEdit() == null) {
			Main.getPlugin().getLogger().warning("WorldEdit not detected! Unable to obtain region information for island (".concat(this.getID()).concat(")..."));
			return null;
		}
		return this._getRegion(environment);
	}
	
	private final Object _getRegion(Environment environment) {
		if(Main.getWorldGuard() == null) {
			Main.getPlugin().getLogger().warning("WorldGuard not detected! Unable to obtain region information for island (".concat(this.getID()).concat(")..."));
			return null;
		}
		if(Main.getWorldEdit() == null) {
			Main.getPlugin().getLogger().warning("WorldEdit not detected! Unable to obtain region information for island (".concat(this.getID()).concat(")..."));
			return null;
		}
		try {
			com.sk89q.worldguard.protection.managers.RegionManager rm = (com.sk89q.worldguard.protection.managers.RegionManager) Main.getRegionManagerFor(environment == Environment.NORMAL ? GeneratorMain.getSkyworld() : environment == Environment.NETHER ? GeneratorMain.getSkyworldNether() : GeneratorMain.getSkyworldTheEnd());
			return rm.getRegion(this.getID());
		} catch(NoClassDefFoundError ex) {
			ex.printStackTrace(System.err);
			System.err.flush();
			return null;
		}
	}
	
	/** @return This Island's bounds. All values are inclusive.
	 *         <b>{@code [minX, minZ, maxX, maxZ]}</b> */
	public final int[] getBounds() {
		if(GeneratorMain.enableIslandBorders) {
			final int range = GeneratorMain.island_Range;
			final int border = GeneratorMain.islandBorderSize;
			int x = this.x * range;
			int z = this.z * range;
			int minX = (x - (range / 2)) + 2 + (x <= 0 ? (border / 2) : 0);//(range / 2) - 2);
			int minZ = (z - (range / 2)) + 2 + (z <= 0 ? (border / 2) : 0);//(range / 2) - 2
			int maxX = x + ((range / 2) - (border - 1));//(range / 2) - 4
			int maxZ = z + ((range / 2) - (border - 1));//(range / 2) - 4
			return new int[] {minX, minZ, maxX, maxZ};
		}
		//Negative/positive insensitive code! Yay, finally -_-
		final int range = GeneratorMain.island_Range;
		final int border = GeneratorMain.islandBorderSize;
		int x = this.x * range;
		int z = this.z * range;
		
		int diameter = range - border;
		int radius = diameter / 2;
		radius += radius % 2 == 0 ? 0 : 1;
		
		int minX = x - radius;
		int minZ = z - radius;
		int maxX = x + radius;
		int maxZ = z + radius;
		return new int[] {minX, minZ, maxX, maxZ};
	}
	
	/** Updates(or creates and updates if it doesn't exist) this Island's
	 * WorldGuard region, then updates any other Island-related data
	 * 
	 * @return This Island */
	public final Island update() {
		for(UUID joinRequester : this.memberJoinRequests.keySet()) {
			this.hasJoinRequestFrom(joinRequester);
		}
		for(UUID invited : this.memberInvitations.keySet()) {
			this.hasInvitationFor(invited);
		}
		Island.getMainIslandFor(this.getOwner(), true);//(Updates island main owner states)
		return this;//this.updateRegion(Environment.NORMAL).updateRegion(Environment.NETHER).updateRegion(Environment.THE_END);
	}
	
	private final Island deleteAllRegions() {
		return this.deleteRegion(Environment.NORMAL).deleteRegion(Environment.NETHER).deleteRegion(Environment.THE_END);
	}
	
	private final Island deleteRegion(Environment environment) {
		if(Main.getWorldGuard() == null) {
			Main.getPlugin().getLogger().warning("WorldGuard not detected! Not deleting region for island (".concat(this.getID()).concat(")..."));
			return this;
		}
		if(Main.getWorldEdit() == null) {
			Main.getPlugin().getLogger().warning("WorldEdit not detected! Not deleting region for island (".concat(this.getID()).concat(")..."));
			return this;
		}
		return this._deleteRegion(environment);
	}
	
	private final Island _deleteRegion(Environment environment) {
		if(Main.getWorldGuard() == null) {
			Main.getPlugin().getLogger().warning("WorldGuard not detected! Not deleting region for island (".concat(this.getID()).concat(")..."));
			return this;
		}
		if(Main.getWorldEdit() == null) {
			Main.getPlugin().getLogger().warning("WorldEdit not detected! Not deleting region for island (".concat(this.getID()).concat(")..."));
			return this;
		}
		try {
			com.sk89q.worldguard.protection.regions.ProtectedRegion region = (com.sk89q.worldguard.protection.regions.ProtectedRegion) this.getRegion(environment);
			World world = environment == Environment.NORMAL ? GeneratorMain.getSkyworld() : (environment == Environment.NETHER ? GeneratorMain.getSkyworldNether() : GeneratorMain.getSkyworldTheEnd());
			if(region != null) {
				com.sk89q.worldguard.protection.managers.RegionManager rm = (com.sk89q.worldguard.protection.managers.RegionManager) Main.getRegionManagerFor(world);
				rm.removeRegion(region.getId());
			}
		} catch(NoClassDefFoundError ex) {
			ex.printStackTrace(System.err);
			System.err.flush();
		}
		return this;
	}
	
	private final Island updateRegion(Environment environment) {
		if(Main.getWorldGuard() == null) {
			Main.getPlugin().getLogger().warning("WorldGuard not detected! Not updating region for island (".concat(this.getID()).concat(")..."));
			return this;
		}
		if(Main.getWorldEdit() == null) {
			Main.getPlugin().getLogger().warning("WorldEdit not detected! Not updating region for island (".concat(this.getID()).concat(")..."));
			return this;
		}
		return this._updateRegion(environment);
	}
	
	private final Island _updateRegion(Environment environment) {
		if(Main.getWorldGuard() == null) {
			Main.getPlugin().getLogger().warning("WorldGuard not detected! Not updating region for island (".concat(this.getID()).concat(")..."));
			return this;
		}
		if(Main.getWorldEdit() == null) {
			Main.getPlugin().getLogger().warning("WorldEdit not detected! Not updating region for island (".concat(this.getID()).concat(")..."));
			return this;
		}
		try {
			com.sk89q.worldguard.protection.regions.ProtectedRegion region = (com.sk89q.worldguard.protection.regions.ProtectedRegion) this.getRegion(environment);
			World world = environment == Environment.NORMAL ? GeneratorMain.getSkyworld() : (environment == Environment.NETHER ? GeneratorMain.getSkyworldNether() : GeneratorMain.getSkyworldTheEnd());
			if(region == null) {
				com.sk89q.worldguard.protection.managers.RegionManager rm = (com.sk89q.worldguard.protection.managers.RegionManager) Main.getRegionManagerFor(world);
				if(rm == null) {
					Main.getPlugin().getLogger().warning("WorldGuard region manager is null! Not updating region for island (".concat(this.getID()).concat(")..."));
					return this;
				}
				int[] bounds = this.getBounds();
				int minX = bounds[0];
				int minZ = bounds[1];
				int maxX = bounds[2];
				int maxZ = bounds[3];
				com.sk89q.worldedit.BlockVector min = new com.sk89q.worldedit.BlockVector(minX, 0, minZ);
				com.sk89q.worldedit.BlockVector max = new com.sk89q.worldedit.BlockVector(maxX, world.getMaxHeight(), maxZ);
				region = new com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion(this.getID(), min, max);
				rm.addRegion(region);
			}
			com.sk89q.worldguard.domains.DefaultDomain members = new com.sk89q.worldguard.domains.DefaultDomain();
			for(UUID member : this.getMembers()) {
				members.addPlayer(member);
			}
			for(UUID trusted : this.trustedPlayers) {
				members.addPlayer(trusted);
			}
			region.setMembers(members);
			//region.setFlag(DefaultFlag.GREET_MESSAGE, "&aYou are entering &f" + this.ownerName + "&a's " + (environment == Environment.NORMAL ? "island" : (environment == Environment.NETHER ? "nether area" : "end area")) + ".");
			//region.setFlag(DefaultFlag.FAREWELL_MESSAGE, "&aYou are leaving &f" + this.ownerName + "&a's " + (environment == Environment.NORMAL ? "island" : (environment == Environment.NETHER ? "nether area" : "end area")) + ".");
			
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.BUILD, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.DAMAGE_ANIMALS, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENDERDRAGON_BLOCK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.CHEST_ACCESS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.CHORUS_TELEPORT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.CREEPER_EXPLOSION, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);//(Blocked via event handler in Main class)
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.DAMAGE_ANIMALS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.DESTROY_VEHICLE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENDER_BUILD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENDERDRAGON_BLOCK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENDERPEARL, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENTITY_ITEM_FRAME_DESTROY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENTITY_PAINTING_DESTROY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.ENTRY, this.isLocked ? com.sk89q.worldguard.protection.flags.StateFlag.State.DENY : com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.EXIT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.EXIT_VIA_TELEPORT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.EXP_DROPS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.FALL_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.FIRE_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.FIREWORK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.GHAST_FIREBALL, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.GRASS_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.ICE_FORM, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.ICE_MELT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.INTERACT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.ITEM_DROP, this.isLocked ? com.sk89q.worldguard.protection.flags.StateFlag.State.DENY : com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.ITEM_PICKUP, this.isLocked ? com.sk89q.worldguard.protection.flags.StateFlag.State.DENY : com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.LAVA_FIRE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.LAVA_FLOW, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.LEAF_DECAY, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.LIGHTER, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.LIGHTNING, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.MOB_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.MOB_SPAWNING, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.MUSHROOMS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.MYCELIUM_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.OTHER_EXPLOSION, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.PASSTHROUGH, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.PISTONS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.PLACE_VEHICLE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.POTION_SPLASH, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.PVP, this.pvpEnabled ? (this.isLocked ? com.sk89q.worldguard.protection.flags.StateFlag.State.DENY : com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW) : com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.RIDE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.SLEEP, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.SNOW_FALL, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.SNOW_MELT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.SOIL_DRY, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.TNT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.USE, this.isLocked ? com.sk89q.worldguard.protection.flags.StateFlag.State.DENY : com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.VINE_GROWTH, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.WATER_FLOW, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			region.setFlag(com.sk89q.worldguard.protection.flags.Flags.WITHER_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
			
			region.setPriority(100);
		} catch(NoClassDefFoundError ex) {
			ex.printStackTrace(System.err);
			System.err.flush();
		}
		return this;
	}
	
	/** @return A list of all of the chunks that contain this Island's
	 *         blocks */
	public final List<Chunk> getChunks() {
		ArrayList<Chunk> chunks = new ArrayList<>();
		int[] bounds = this.getBounds();
		int minX = bounds[0];
		int minZ = bounds[1];
		int maxX = bounds[2];
		int maxZ = bounds[3];
		for(int x = minX; x <= maxX; x++) {
			for(int z = minZ; z <= maxZ; z++) {
				try {
					Chunk chunk = Main.getChunkAtWorldCoords(GeneratorMain.getSkyworld(), x, z);
					if(!chunks.contains(chunk)) {
						chunks.add(chunk);
					}
				} catch(Throwable ex) {
					if(Bukkit.isPrimaryThread()) {
						int[] xz = Main.getWorldToChunkCoords(x, z);
						Main.getPlugin().getLogger().warning("Unable to get chunk at " + xz[0] + " " + xz[1] + ": " + ex.getClass().getName() + ": " + ex.getMessage());
						Throwable e = ex;
						while(e.getCause() != null) {
							e = e.getCause();
							Main.getPlugin().getLogger().warning("Caused by: " + e.getClass().getName() + ": " + e.getMessage());
						}
					}
					continue;
				}
			}
		}
		return chunks;
	}
	
	/** Deletes all of the blocks contained within this Island, then
	 * generates a new one from the configured schematic.
	 * 
	 * @param teleportMembersToIsland Whether or not the members of this island
	 *            should be teleported to the island's spawn location(only if
	 *            they're in one of the skyworlds)
	 * 
	 * @return This Island */
	public final Island generateSchematicIsland(boolean teleportMembersToIsland) {
		return this.generateSchematicIsland(true, false, teleportMembersToIsland);
	}
	
	public final Island generateSchematicIsland(boolean deleteBlocks, boolean restoreBiome, final boolean teleportMembersToIsland) {
		if(this.isWithinSpawnArea() && !this.isTestIsland) {
			throw new IllegalStateException(ChatColor.DARK_RED + "Cannot generate an island in the spawn area!");
		}
		this.islandType = "schematic";
		World world = GeneratorMain.getSkyworld();
		//int X = this.x * GeneratorMain.island_Range;
		//int Z = this.z * GeneratorMain.island_Range;
		this.deleteBlocks(false);
		Plugin check = Main.getWorldEdit();
		if(check != null) {
			File schem = new File(GeneratorMain.getSchematicsFolder(), GeneratorMain.island_schematic);
			if(!schem.exists() && !GeneratorMain.island_schematic.endsWith(".schematic") && !GeneratorMain.island_schematic.endsWith(".schem")) {
				schem = new File(GeneratorMain.getSchematicsFolder(), GeneratorMain.island_schematic.concat(".schem"));
			}
			if(!schem.exists() && !GeneratorMain.island_schematic.endsWith(".schematic") && !GeneratorMain.island_schematic.endsWith(".schem")) {
				schem = new File(GeneratorMain.getSchematicsFolder(), GeneratorMain.island_schematic.concat(".schematic"));
			}
			if(schem.exists()) {
				if(WorldEditUtils.pasteSchematicFromFile(world, schem, this.getLocation().toVector())) {
					Location location = this.getLocation().add(GeneratorMain.schematic_chest_offsetX, GeneratorMain.schematic_chest_offsetY, GeneratorMain.schematic_chest_offsetZ);
					Block block = location.getBlock();
					final int[] tid = {-1, 0, 120};//20 * 6 = 120; Six second maximum wait time for FastAsyncWorldEdit to finish pasting the schematic
					final ConcurrentLinkedDeque<String> teleportedPlayers = new ConcurrentLinkedDeque<>();
					final boolean[] teleportPlayersArray = new boolean[] {teleportMembersToIsland};
					final boolean[] taskDisabled = {false};
					final Runnable task = () -> {
						if(taskDisabled[0] || tid[1] >= tid[2]) {
							Main.scheduler.cancelTask(tid[0]);
							taskDisabled[0] = true;
							return;
						}
						if(block.getState() instanceof Chest) {
							Chest chest = (Chest) block.getState();
							if(GeneratorMain.overwrite_schematic_chest_items) {
								chest.getInventory().clear();
								chest.update(true, true);
								setChestContents(chest.getBlockInventory());
								setChestContents(chest.getInventory());
								taskDisabled[0] = true;
							}
							if(!taskDisabled[0]) {
								Main.scheduler.cancelTask(tid[0]);
								taskDisabled[0] = true;
							}
						} else if(GeneratorMain.overwrite_schematic_chest_items && tid[1] >= tid[2]) {
							Main.plugin.getLogger().warning("\n"//
									.concat(" /!\\  Failed to locate the starting chest for schematic island at (").concat(this.getLocation().toVector().toString()).concat(")!\n")//
									.concat("/___\\ The island probably won't have any starting items!"));
							Main.server.broadcast(ChatColor.RED.toString().concat("[Entei's Skyblock] Failed to locate the starting chest for schematic island at (").concat(this.getLocation().toVector().toString()).concat(")!"), Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
							Main.server.broadcast(ChatColor.RED.toString().concat("[Entei's Skyblock] The newly generated island won't have any starting items!"), Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
							Main.server.broadcast(ChatColor.GREEN.toString().concat("[Entei's Skyblock] You have permission to remedy this issue. Please teleport to the island using \"/iw ").concat(this.getOwnerNamePlain()).concat("\", locate and look at the starting chest, then type \"").concat(ChatColor.WHITE.toString()).concat("/dev regenChest").concat(ChatColor.GREEN.toString()).concat("\"."), "skyblock.admin");
							if(Main.server.getOfflinePlayer(this.getOwner()).isOnline()) {
								Main.server.getPlayer(this.getOwner()).sendMessage(ChatColor.YELLOW.toString().concat("[Entei's Skyblock] Failed to generate the starting chest items. Please contact a staff member for assistance if they have not contacted you within 5 minutes."));
							}
							if(!taskDisabled[0]) {
								Main.scheduler.cancelTask(tid[0]);
								taskDisabled[0] = true;
							}
							return;
						}
						if(taskDisabled[0] || tid[1] >= tid[2]) {
							if(teleportPlayersArray[0]) {
								for(UUID uuid : this.getMembers()) {
									OfflinePlayer member = Main.server.getOfflinePlayer(uuid);
									if(member.isOnline() && Island.isInSkyworld(member.getPlayer())) {
										if(!teleportedPlayers.contains(member.getUniqueId().toString())) {
											Main.safeTeleport(member.getPlayer(), this.getSpawnLocation());
											teleportedPlayers.add(member.getUniqueId().toString());
										}
									} else if(member.isOnline()) {
										member.getPlayer().sendMessage(ChatColor.GREEN.toString().concat("Your new island has finished generating, but you weren't in the skyworld when it finished. Type \"").concat(ChatColor.WHITE.toString()).concat("/island home").concat(ChatColor.GREEN.toString()).concat("\" to start playing on your new island!"));
									}
								}
								teleportPlayersArray[0] = false;
							}
							if(!taskDisabled[0]) {
								Main.scheduler.cancelTask(tid[0]);
								taskDisabled[0] = true;
							}
						}
						tid[1]++;
					};
					if(block.getState() instanceof Chest) {//Schematic was pasted really quickly(or plain old WorldEdit was used)
						tid[1] = tid[2];
						task.run();
						this.save();
						return this;
					}
					if(teleportMembersToIsland) {
						for(UUID uuid : this.getMembers()) {
							OfflinePlayer member = Main.server.getOfflinePlayer(uuid);
							if(member.isOnline()) {// && Island.isInSkyworld(member.getPlayer())) {
								member.getPlayer().sendMessage(ChatColor.GREEN.toString().concat("Your new island at ").concat(this.getID()).concat(" is currently being generated! When it finishes, you will be automatically teleported to it."));
							}
						}
					}
					Main.scheduler.runTaskTimer(Main.getPlugin(), task, 0L, 5L);//Start the task immediately(delay = 0), and repeat the task to check for the chest once every 5 ticks, or every 1/4 of a second(5 * 4 = 20; 20 ticks = 1 second)
					this.save();
					return this;
				}
			} else {
				if(GeneratorMain.island_schematic.equals("none")) {
					Main.plugin.getLogger().warning("\n"//
							.concat(" /!\\  A schematic island was supposed to generate,\n")//
							.concat("/___\\ but no schematic has been set!"));
				} else {
					Main.plugin.getLogger().warning("\n"//
							.concat(" /!\\  A schematic island was supposed to generate,\n")//
							.concat("/___\\ but the schematic \"").concat(GeneratorMain.island_schematic).concat("\" does not exist!"));
				}
			}
		} else {
			Main.plugin.getLogger().warning("\n"//
					.concat(" /!\\  Failed to find an instance of WorldEdit!\n")//
					.concat("/___\\ Unable to use schematics!"));
		}
		Main.plugin.getLogger().warning("\n"//
				.concat(" /!\\  Failed to generate island from schematic \"").concat(GeneratorMain.island_schematic).concat("\" at (").concat(this.getLocation().toVector().toString()).concat(")!\n")//
				.concat("/___\\ Using default hard-coded normal island instead..."));
		if(!GeneratorMain.enableIslandBorders) {
			Main.plugin.getLogger().warning("       Island bridge generation is turned off. Since the schematic failed to paste, the above island may not have any bridges if the schematic is supposed to contain them.");
		}
		this.generateIsland();
		this.islandType = "schematic";
		if(teleportMembersToIsland) {
			for(UUID uuid : this.getMembers()) {
				OfflinePlayer member = Main.server.getOfflinePlayer(uuid);
				if(member.isOnline() && this.isPlayerOnIsland(member.getPlayer())) {
					Main.safeTeleport(member.getPlayer(), this.getSpawnLocation());
				}
			}
		}
		this.save();
		return this;
	}
	
	/** Deletes all of the blocks contained within this Island, then
	 * generates a blank new one.
	 * 
	 * @return This Island */
	public final Island generateIsland() {
		return this.generateIsland(true, false);
	}
	
	public final Island generateIsland(boolean deleteBlocks, boolean restoreBiome) {
		if(this.isWithinSpawnArea() && !this.isTestIsland) {
			throw new IllegalStateException(ChatColor.DARK_RED + "Cannot generate an island in the spawn area!");
		}
		this.islandType = "normal";
		World world = GeneratorMain.getSkyworld();
		int X = this.x * GeneratorMain.island_Range;
		int Z = this.z * GeneratorMain.island_Range;
		if(deleteBlocks) {
			this.deleteBlocks(restoreBiome);
		}
		world.getBlockAt(X, GeneratorMain.island_Height, Z).setType(Material.OBSIDIAN, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 1, Z).setType(Material.SAND, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 2, Z).setType(Material.SAND, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 3, Z).setType(Material.SAND, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 5, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 6, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 11, Z).setType(Material.OAK_LEAVES, false);
		
		//Leaves Layer 1
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 11, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 11, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 11, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 11, Z - 1).setType(Material.OAK_LEAVES, false);
		
		//Leaves Layer 2
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 10, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 10, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 10, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 10, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 10, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 10, Z - 1).setType(Material.OAK_LEAVES, false);
		
		//Leaves Layer 3
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z - 1).setType(Material.OAK_LEAVES, false);
		
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 9, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 9, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 9, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 9, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 9, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 9, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 9, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 9, Z + 1).setType(Material.OAK_LEAVES, false);
		
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z - 2).setType(Material.OAK_LEAVES, false);
		
		//Leaves Layer 4
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z + 1).setType(Material.OAK_LEAVES, false);//This one seems to not get set...
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z - 1).setType(Material.OAK_LEAVES, false);
		
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z + 2).setType(Material.OAK_LEAVES, false);
		
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z - 2).setType(Material.OAK_LEAVES, false);
		
		//Dirt Layer 1
		
		world.getBlockAt(X, GeneratorMain.island_Height + 1, Z - 1).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 1, Z + 1).setType(Material.DIRT, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 1, Z).setType(Material.DIRT, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 1, Z).setType(Material.DIRT, false);
		
		//Dirt Layer 2
		
		world.getBlockAt(X, GeneratorMain.island_Height + 2, Z - 1).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 2, Z + 1).setType(Material.DIRT, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 2, Z + 1).setType(Material.DIRT, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 2, Z - 1).setType(Material.DIRT, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 2, Z + 1).setType(Material.DIRT, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 2, Z - 1).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 2, Z + 2).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 2, Z - 2).setType(Material.DIRT, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 2, Z).setType(Material.DIRT, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 2, Z).setType(Material.DIRT, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 2, Z).setType(Material.DIRT, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 2, Z).setType(Material.DIRT, false);
		
		//Dirt Layer 3
		
		world.getBlockAt(X, GeneratorMain.island_Height + 3, Z - 1).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 3, Z + 1).setType(Material.DIRT, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 3, Z).setType(Material.DIRT, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 3, Z).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 3, Z - 2).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 3, Z + 2).setType(Material.DIRT, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 3, Z).setType(Material.DIRT, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 3, Z).setType(Material.DIRT, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 3, Z - 2).setType(Material.DIRT, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 3, Z + 2).setType(Material.DIRT, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 3, Z - 2).setType(Material.DIRT, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 3, Z + 2).setType(Material.DIRT, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 3, Z + 1).setType(Material.DIRT, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 3, Z + 1).setType(Material.DIRT, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 3, Z - 1).setType(Material.DIRT, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 3, Z - 1).setType(Material.DIRT, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 3, Z + 1).setType(Material.DIRT, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 3, Z - 1).setType(Material.DIRT, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 3, Z + 1).setType(Material.DIRT, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 3, Z - 1).setType(Material.DIRT, false);
		world.getBlockAt(X + 3, GeneratorMain.island_Height + 3, Z).setType(Material.DIRT, false);
		world.getBlockAt(X - 3, GeneratorMain.island_Height + 3, Z).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 3, Z + 3).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 3, Z - 3).setType(Material.DIRT, false);
		
		//Grass Layer 1
		
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z - 2).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z + 2).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z - 2).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z + 2).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z - 2).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z + 2).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 4, Z + 2).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 4, Z - 2).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 4, Z + 2).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 4, Z - 2).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 3, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 3, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 3, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 3, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 3, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 3, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z + 3).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z + 3).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z + 3).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z - 3).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z - 3).setType(Material.GRASS_BLOCK, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z - 3).setType(Material.GRASS_BLOCK, false);
		
		//Chest 1
		
		generateChestAt(world, X, GeneratorMain.island_Height + 5, Z - 1);
		this.save();
		return this.setBiome(this.biome);
	}
	
	/** Deletes all of the blocks contained within this Island, then
	 * generates a blank new square one.
	 * 
	 * @return This Island */
	public final Island generateSquareIsland() {
		return this.generateSquareIsland(true, false);
	}
	
	public final Island generateSquareIsland(boolean deleteBlocks, boolean restoreBiome) {
		if(this.isWithinSpawnArea() && !this.isTestIsland) {
			throw new IllegalStateException(ChatColor.DARK_RED + "Cannot generate an island in the spawn area!");
		}
		this.islandType = "square";
		World world = GeneratorMain.getSkyworld();
		this.spawnX = 0.5;
		this.spawnY = 3;
		this.spawnZ = -1.5;
		
		int X = this.x * GeneratorMain.island_Range;
		int Z = this.z * GeneratorMain.island_Range;
		if(deleteBlocks) {
			this.deleteBlocks(restoreBiome);
		}
		world.getBlockAt(X, GeneratorMain.island_Height, Z).setType(Material.OBSIDIAN, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 1, Z).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 2, Z).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 3, Z).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 5, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 6, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z).setType(Material.OAK_LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z).setType(Material.OAK_LEAVES, false);
		
		//Leaves Layer 1
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 10, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 10, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z - 1).setType(Material.OAK_LEAVES, false);
		
		//Leaves Layer 2
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z - 1).setType(Material.OAK_LEAVES, false);
		
		//Leaves Layer 3
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z - 1).setType(Material.OAK_LEAVES, false);
		
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z + 1).setType(Material.OAK_LEAVES, false);
		
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z - 2).setType(Material.OAK_LEAVES, false);
		
		//Leaves Layer 4
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 7, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 7, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 7, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 7, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 7, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 7, Z - 1).setType(Material.OAK_LEAVES, false);
		
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 7, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 7, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 7, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 7, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 7, Z - 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 7, Z).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 7, Z + 1).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 7, Z + 2).setType(Material.OAK_LEAVES, false);
		
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 7, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 7, Z + 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 7, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z - 2).setType(Material.OAK_LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 7, Z - 2).setType(Material.OAK_LEAVES, false);
		
		//Dirt
		for(int i = 0; i < 4; i++) {
			world.getBlockAt(X - 2, GeneratorMain.island_Height + i, Z + 1).setType(i == 3 ? Material.GRASS_BLOCK : Material.DIRT, false);
			world.getBlockAt(X - 1, GeneratorMain.island_Height + i, Z + 1).setType(Material.DIRT, false);
			/**/world.getBlockAt(X, GeneratorMain.island_Height + i, Z + 1).setType(Material.DIRT, false);
			world.getBlockAt(X + 1, GeneratorMain.island_Height + i, Z + 1).setType(Material.DIRT, false);
			world.getBlockAt(X + 2, GeneratorMain.island_Height + i, Z + 1).setType(i == 3 ? Material.GRASS_BLOCK : Material.DIRT, false);
		}
		for(int i = 0; i < 4; i++) {
			world.getBlockAt(X - 2, GeneratorMain.island_Height + i, Z).setType(i == 3 ? Material.GRASS_BLOCK : Material.DIRT, false);
			world.getBlockAt(X - 1, GeneratorMain.island_Height + i, Z).setType(i == 3 ? Material.GRASS_BLOCK : Material.DIRT, false);
			//world.getBlockAt(X, GeneratorMain.island_Height + i, Z).setType(i == 3 ? Material.GRASS_BLOCK : Material.DIRT, false);
			world.getBlockAt(X + 1, GeneratorMain.island_Height + i, Z).setType(i == 3 ? Material.GRASS_BLOCK : Material.DIRT, false);
			world.getBlockAt(X + 2, GeneratorMain.island_Height + i, Z).setType(i == 3 ? Material.GRASS_BLOCK : Material.DIRT, false);
		}
		for(int i = 0; i < 4; i++) {
			for(int j = 1; j < 3; j++) {
				world.getBlockAt(X - 2, GeneratorMain.island_Height + i, Z - j).setType(i == 3 ? Material.GRASS_BLOCK : Material.DIRT, false);
				world.getBlockAt(X - 1, GeneratorMain.island_Height + i, Z - j).setType(i == 3 ? Material.GRASS_BLOCK : Material.DIRT, false);
				if(i < 3) {
					world.getBlockAt(X, GeneratorMain.island_Height + i, Z - j).setType(i == 2 ? Material.GRASS_BLOCK : Material.DIRT, false);
				}
				world.getBlockAt(X + 1, GeneratorMain.island_Height + i, Z - j).setType(i == 3 ? Material.GRASS_BLOCK : Material.DIRT, false);
				world.getBlockAt(X + 2, GeneratorMain.island_Height + i, Z - j).setType(i == 3 ? Material.GRASS_BLOCK : Material.DIRT, false);
			}
		}
		
		//Sand 1
		
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z + 1).setType(Material.SAND, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z + 1).setType(Material.SAND, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z + 1).setType(Material.SAND, false);
		
		//Chest 1
		generateChestAt(world, X, GeneratorMain.island_Height + 3, Z - 1);
		this.save();
		return this.setBiome(this.biome);
	}
	
	/** Generates an island starter chest at the specified location
	 * 
	 * @param world The world to generate in
	 * @param x The x coordinate to generate at
	 * @param y The y coordinate to generate at
	 * @param z The z coordinate to generate at */
	public static final void generateChestAt(World world, int x, int y, int z) {
		generateChestAt(new Location(world, x, y, z, 0.0f, 0.0f));
	}
	
	/** Generates an island starter chest at the specified location
	 * 
	 * @param location The location to generate the chest at */
	public static final void generateChestAt(Location location) {
		location.getBlock().setType(Material.CHEST, false);
		Chest chest = (Chest) location.getBlock().getState();
		setChestContents(chest.getBlockInventory());
		setChestContents(chest.getInventory());
	}
	
	/** @param inv The inventory whose contents will be set to that of an
	 *            island's starting chest */
	public static final void setChestContents(Inventory inv) {
		inv.clear();
		inv.setItem(0, new ItemStack(Material.ICE, 2));
		inv.setItem(1, new ItemStack(Material.LAVA_BUCKET, 1));
		inv.setItem(2, new ItemStack(Material.RED_MUSHROOM, 1));
		inv.setItem(3, new ItemStack(Material.BROWN_MUSHROOM, 1));
		inv.setItem(4, new ItemStack(Material.SUGAR_CANE, 1));
		inv.setItem(5, new ItemStack(Material.MELON_SLICE, 1));//Material.MELON, 1));
		inv.setItem(6, new ItemStack(Material.PUMPKIN_SEEDS, 1));
		inv.setItem(7, new ItemStack(Material.CACTUS, 1));
		inv.setItem(8, new ItemStack(Material.BEETROOT_SEEDS, 1));
	}
	
	/** @return Whether or not the restart cooldown has passed. */
	public final boolean canRestart() {
		return System.currentTimeMillis() >= this.getNextRestartTime();
	}
	
	/** @return The next time that this Island can be restarted */
	public final long getNextRestartTime() {
		return this.lastRestartTime + Math.round(Main.DAY);
	}
	
	/** @return This Island */
	public final Island restart(boolean teleportMembersToIsland) {
		return this.restart(true, teleportMembersToIsland);
	}
	
	public final Island restart(boolean wipeMembersInventories, boolean teleportMembersToIsland) {
		return this.restart(wipeMembersInventories, true, false, teleportMembersToIsland);
	}
	
	public final Island restart(boolean wipeMembersInventories, boolean deleteBlocks, boolean restoreBiome, boolean teleportMembersToIsland) {
		if(wipeMembersInventories) {
			this.wipeMembersInventories(ChatColor.YELLOW + "The island you are a member of has been restarted.");
		}
		if(this.islandType == null || this.islandType.trim().isEmpty()) {
			this.islandType = "normal";
		}
		if(teleportMembersToIsland && !this.islandType.equalsIgnoreCase("schematic")) {
			for(UUID uuid : this.getMembers()) {
				OfflinePlayer member = Main.server.getOfflinePlayer(uuid);
				if(member.isOnline() && this.isPlayerOnIsland(member.getPlayer())) {
					Main.safeTeleport(member.getPlayer(), this.getSpawnLocation());
				}
			}
		}
		if(this.islandType.equalsIgnoreCase("normal") || (this.islandType.equalsIgnoreCase("schematic") && GeneratorMain.island_schematic.equalsIgnoreCase("none"))) {
			String type = this.islandType;
			this.generateIsland(deleteBlocks, restoreBiome);
			this.islandType = type;
		} else if(this.islandType.equalsIgnoreCase("square")) {
			this.generateSquareIsland(deleteBlocks, restoreBiome);
		} else if(this.islandType.equalsIgnoreCase("schematic")) {
			this.generateSchematicIsland(deleteBlocks, restoreBiome, teleportMembersToIsland);
		} else {
			this.generateIsland(deleteBlocks, restoreBiome);
		}
		this.lastRestartTime = System.currentTimeMillis();
		return this;
	}
	
	/** Deletes this Island's blocks, WorldGuard regions, and save file. The
	 * Island will no longer exist unless it is recreated. */
	public final void deleteCompletely() {
		islands.remove(this.deleteIsland().deleteAllRegions().wipeAllFields().deleteSaveFile());
	}
	
	/** Deletes this island's blocks. This method does not wipe member
	 * inventories or remove any regions.
	 * 
	 * @param restoreBiome Whether or not the biome should be set to OCEAN
	 * 
	 * @return This Island */
	public final Island deleteBlocks(boolean restoreBiome) {
		World world = GeneratorMain.getSkyworld();
		int[] bounds = this.getBounds();
		int minX = bounds[0];
		int minZ = bounds[1];
		int maxX = bounds[2];
		int maxZ = bounds[3];
		for(int x = minX; x <= maxX; x++) {
			for(int y = 0; y < world.getMaxHeight(); y++) {
				for(int z = minZ; z <= maxZ; z++) {
					Block block = world.getBlockAt(x, y, z);
					BlockState state = block.getState();
					if(state instanceof Chest) {
						((Chest) state).getInventory().clear();
						state.update(true, true);
						((Chest) state).getBlockInventory().clear();
					} else if(state instanceof InventoryHolder) {
						((InventoryHolder) state).getInventory().clear();//Prevent chests, etc. from dropping items, as they might land on the blocks that are about to be generated
						state.update(true, true);
					}
					block.setType(Material.AIR, false);
					if(restoreBiome && y == 0) {
						block.setBiome(Biome.OCEAN);
					}
				}
			}
		}
		if(restoreBiome) {
			this.biome = Biome.OCEAN;
		}
		return this;
	}
	
	/** Deletes all of the blocks contained within this Island, as well as its
	 * members' inventories.<br>
	 * Use with care.
	 * 
	 * @return This Island */
	public final Island deleteIsland() {
		this.sendMessage(ChatColor.RED.toString().concat("The island you were a member of(ID: ").concat(this.getID()).concat(") has just been deleted."));
		for(Player player : this.getPlayersOnIsland()) {
			if(!this.isMember(player)) {
				this.sendMessage(ChatColor.RED.toString().concat("The island you were just visiting has been deleted."));
			}
			if(!player.isFlying()) {
				Main.safeTeleport(player.getPlayer(), GeneratorMain.getSkyworldSpawnLocation());
			}
		}
		this.deleteBlocks(true);
		/*this.deleteAllRegions();
		islands.remove(this);
		FileDeleteStrategy.FORCE.deleteQuietly(this.getSaveFile());*/
		
		if(this.owner != null) {
			this.members.add(this.owner);
		}
		ArrayList<UUID> members = new ArrayList<>(this.members);
		this.wipeAllFields();
		this.members.addAll(members);
		this.owner = null;
		this.ownerName = "no one";
		this.wipeMembersInventories(ChatColor.YELLOW + "The island has been deleted.");
		this.update();
		return this.refreshChunks();
	}
	
	protected final Island wipeAllFields() {
		islands.remove(this);
		Island dud = new Island(this.x, this.z);
		islands.remove(dud);
		this.copy(dud);
		islands.add(this);
		return this;
	}
	
	/** <b>Note:</b> This method does not check if the given member is actually
	 * a member of this Island, use with care.<br>
	 * <br>
	 * Attempts to wipe the inventory of the given member. If they are not
	 * online, or are not currently in the skyworld or one of its dimensions,
	 * then they are marked as needing their inventories wiped the next time
	 * they are in the skyworld
	 * 
	 * @param member The member whose inventory will be wiped
	 * @param msg The message to send to the affected players
	 * @return This Island */
	public final Island wipeMembersInventory(UUID member, String msg) {
		/*if(!this.isMember(member)) {
			this.membersToWipeInv.remove(member);
			return this;
		}*/
		Player player = Main.server.getPlayer(member);
		if(player != null && isInSkyworld(player) && player.getGameMode() == GameMode.SURVIVAL) {
			player.closeInventory();
			player.getInventory().clear();
			player.getEnderChest().clear();
			player.setExp(0);
			player.setLevel(0);
			player.sendMessage(msg);
			this.membersToWipeInv.remove(member);
			if(this.owner == null) {
				this.members.remove(member);
			}
		} else {
			this.membersToWipeInv.put(member, msg);
		}
		return this;
	}
	
	/** Searches for and wipes the inventories of any members whose
	 * inventories have been marked for clearing. Their inventories are only
	 * cleared if they are currently in the {@link GeneratorMain#getSkyworld()
	 * skyworld}.
	 * 
	 * @return This Island */
	public final Island wipeMembersInventoriesIfRequired() {
		for(Entry<UUID, String> entry : this.membersToWipeInv.entrySet()) {
			UUID member = entry.getKey();
			String msg = entry.getValue();
			/*if(!this.isMember(member)) {
				this.membersToWipeInv.remove(member);
				continue;
			}*/
			Player player = Main.server.getPlayer(member);
			if(player != null && isInSkyworld(player) && player.getGameMode() == GameMode.SURVIVAL) {
				player.closeInventory();
				player.getInventory().clear();
				player.getEnderChest().clear();
				player.setExp(0);
				player.setLevel(0);
				player.sendMessage(msg);
				this.membersToWipeInv.remove(member);
				if(this.owner == null) {
					this.members.remove(member);
				}
			} else {
				this.membersToWipeInv.put(member, msg);
			}
			this.memberCompletedChallenges.remove(member.toString());
		}
		return this;
	}
	
	/** Searches for and wipes the inventories of all of this Island's
	 * members, including the owner. If a player is not online, or is not in
	 * the {@link GeneratorMain#getSkyworld() skyworld}, then their inventories
	 * are
	 * marked
	 * as needing to be cleared the next time they log in and travel to the
	 * {@link GeneratorMain#getSkyworld() skyworld}.
	 * 
	 * @param msg The message to send to the affected players
	 * 
	 * @return This Island */
	public final Island wipeMembersInventories(String msg) {
		for(UUID member : this.getMembers()) {
			Player player = Main.server.getPlayer(member);
			if(player != null && isInSkyworld(player) && player.getGameMode() == GameMode.SURVIVAL) {
				player.closeInventory();
				if(!player.hasPermission("skyblock.admin")) {
					player.getInventory().clear();
					player.getEnderChest().clear();
					player.setExp(0);
					player.setLevel(0);
				}
				player.sendMessage(msg);
				this.membersToWipeInv.remove(member);
				if(this.owner == null) {
					this.members.remove(member);
				}
			} else {
				this.membersToWipeInv.put(member, msg);
			}
		}
		this.memberCompletedChallenges.clear();
		return this.wipeMembersInventoriesIfRequired();
	}
	
	/** @return This Island's biome */
	public final Biome getBiome() {
		return this.biome;
	}
	
	/** Sets the given area in the given world to the given biome, and attempts
	 * to update the change for online players.
	 * 
	 * @param bounds The area to set <b>{@code [minX, minZ, maxX, maxZ]}</b>
	 * @param world The world to set in
	 * @param biome The new biome of the area to set */
	@SuppressWarnings("deprecation")
	public static final void setBiome(int[] bounds, World world, Biome biome) {
		biome.name();
		int minX = bounds[0];
		int minZ = bounds[1];
		int maxX = bounds[2];
		int maxZ = bounds[3];
		ArrayList<Chunk> chunks = new ArrayList<>();
		for(int x = minX; x <= maxX; x++) {
			//for(int y = 0; y < world.getMaxHeight(); y++) {
			for(int z = minZ; z <= maxZ; z++) {
				Chunk chunk = Main.getChunkAtWorldCoords(GeneratorMain.getSkyworld(), x, z);
				if(!chunks.contains(chunk)) {
					chunks.add(chunk);
				}
				world.getBlockAt(x, 0/*y*/, z).setBiome(biome);
			}
			//}
		}
		for(Chunk chunk : chunks) {
			world.refreshChunk(chunk.getX(), chunk.getZ());
		}
		int x = maxX - (minX < 0 ? -minX : minX);
		int y = world.getMaxHeight() / 2;
		int z = maxZ - (minZ < 0 ? -minZ : minZ);
		for(Player player : Main.server.getOnlinePlayers()) {
			if(player.getWorld().equals(GeneratorMain.getSkyworld())) {
				Location loc = new Location(world, x, y, z);
				player.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
			}
		}
	}
	
	/** @param biome The new Biome for this Island(may not be null)
	 * @return This Island */
	public final Island setBiome(Biome biome) {
		biome.name();
		this.biome = biome;
		World world = GeneratorMain.getSkyworld();
		int[] bounds = this.getBounds();
		int minX = bounds[0];
		int minZ = bounds[1];
		int maxX = bounds[2];
		int maxZ = bounds[3];
		for(int x = minX; x <= maxX; x++) {
			//for(int y = 0; y < world.getMaxHeight(); y++) {
			for(int z = minZ; z <= maxZ; z++) {
				world.getBlockAt(x, 0/*y*/, z).setBiome(this.biome);
			}
			//}
		}
		return this.refreshChunks();
	}
	
	/** Resends this Island's chunks to all clients.
	 * 
	 * @return This Island
	 * 
	 * @deprecated This method is not guaranteed to work suitably across all
	 *             client implementations. */
	@Deprecated
	public final Island refreshChunks() {
		for(Chunk chunk : this.getChunks()) {
			GeneratorMain.getSkyworld().refreshChunk(chunk.getX(), chunk.getZ());
			/*for(Player player : Main.server.getOnlinePlayers()) {
				if(player.getWorld().equals(GeneratorMain.getSkyworld())) {
					for(int x = 0; x < 16; x++) {
						for(int y = 0; y < GeneratorMain.getSkyworld().getMaxHeight(); y++) {
							for(int z = 0; z < 16; z++) {
								Block b = chunk.getBlock(x, y, z);
								player.sendBlockChange(b.getLocation(), b.getType(), b.getData());
							}
						}
					}
				}
			}*/
		}
		for(Player player : Main.server.getOnlinePlayers()) {
			if(player.getWorld().equals(GeneratorMain.getSkyworld())) {
				Location loc = this.getLocation();
				player.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
			}
		}
		return this;
	}
	
	/** @param player The player to check
	 * @return Whether or not the player is in the
	 *         {@link GeneratorMain#getSkyworld()
	 *         skyworld} or one of its' dimensions */
	public static final boolean isInSkyworld(Player player) {
		return isSkyworld(player.getWorld());
	}
	
	/** @param location The location to check
	 * @return Whether or not the location is in the
	 *         {@link GeneratorMain#getSkyworld()
	 *         skyworld} or one of its' dimensions */
	public static final boolean isInSkyworld(Location location) {
		return isSkyworld(location == null ? null : location.getWorld());
	}
	
	/** @param world The world to check
	 * @return Whether or not the given world is the
	 *         {@link GeneratorMain#getSkyworld()
	 *         skyworld} or one of its' dimensions */
	public static final boolean isSkyworld(World world) {
		return world == null ? false : (world.getName().equalsIgnoreCase(GeneratorMain.getSkyworld().getName()) || (world.getName().equalsIgnoreCase(GeneratorMain.getSkyworld().getName() + "_nether") && world.getEnvironment() == Environment.NETHER) || (world.getName().equalsIgnoreCase(GeneratorMain.getSkyworld().getName() + "_the_end") && world.getEnvironment() == Environment.THE_END));
	}
	
	/** @param count The number of materials counted so far
	 * @return The count times a scale factor of the count.
	 * @author Augies */
	public static final strictfp double scaleFactor(long count) {
		//final double e = 2.71828182846;
		/*double scale = 2.0 / (1.0 + Math.pow(e, ((count - 1.0) / Main.materialLevelDropOff)));
		return count * scale;*/
		/*final double n = Main.materialLevelDropOff;
		double r = Math.log(3.0) / (n - 1.0);
		return Math.min(count * (2.0 / (1.0 + Math.pow(e, ((count - 1.0) * r)))), n);*/
		/*final double n = Main.materialLevelDropOff;
		double b = Math.log(0.5) / (n - 1.0);
		return Math.min(count * (Math.pow(e, (b * (count - 1.0)))), n);*/
		final double n = Main.materialLevelDropOff;//5000.0
		double r = Math.log(n) / Math.log(n / 2.0); //r is log base config/2 of config
		return Math.pow(count, 1.0 / r); //r root of count
	}
	
	public static final strictfp double calculateLevel(double blockLevel, double divisor, long blockCount, double existingLevel) {
		double scale = scaleFactor(blockCount);
		return existingLevel + ((blockLevel / divisor) * scale);
	}
	
	public static final strictfp double calculateLevel(double blockLevel, long blockCount, double existingLevel) {
		return calculateLevel(blockLevel, Main.getIslandLevelDivisor(), blockCount, existingLevel);
	}
	
	public static final strictfp double calculateLevel(Material material, long blockCount, double existingLevel) {
		return calculateLevel(Main.getLevelFor(material), Main.getIslandLevelDivisor(), blockCount, existingLevel);
	}
	
	/** @param block The block to count
	 * @param map The map to store the results in */
	public static final void countMaterials(Block block, Map<Material, Long> map) {
		Long current = map.get(block.getType());
		if(current == null) {
			current = Long.valueOf(0);
		}
		double check = Main.getLevelFor(block.getType());
		if(check < 0x0.0p0) {
			ItemStack item;
			try {
				@SuppressWarnings("deprecation")
				ItemStack iKnowItsDeprecatedShutUp = new ItemStack(block.getType(), 1, (short) 0, Byte.valueOf(block.getData()));
				item = iKnowItsDeprecatedShutUp;
			} catch(IllegalArgumentException ex) {
				@SuppressWarnings("deprecation")
				ItemStack iKnowItsDeprecatedShutUp = new ItemStack(block.getType(), 1, (short) 0);
				item = iKnowItsDeprecatedShutUp;
			}
			
			String name = Main.getItemName(item);
			Main.console.sendMessage("Found illegal block on skyworld island: " + name + " (" + block.getLocation().toVector() + ")");
		}
		map.put(block.getType(), Long.valueOf(current.longValue() + 1));
		if(Main.getPlugin().getMaterialConfig().getBoolean("checkContainers", true)) {
			if(block.getState() instanceof InventoryHolder) {
				InventoryHolder invHolder = (InventoryHolder) block.getState();
				Inventory inv = invHolder.getInventory();
				if(inv != null) {
					for(ItemStack item : inv.getContents()) {
						if(item == null) {
							continue;
						}
						countMaterials(item, map, invHolder);
					}
				}
			}
		}
	}
	
	/** @param item The item to count
	 * @param map The map to store the results in
	 * @param holder The inventory holder carrying/containing the item, or
	 *            <code><b>null</b></code> if the item isn't in an inventory
	 *            holder's inventory */
	public static final void countMaterials(ItemStack item, Map<Material, Long> map, InventoryHolder holder) {
		if(!Main.getPlugin().getMaterialConfig().getBoolean("checkItemStacks", true)) {
			return;
		}
		Long current = map.get(item.getType());
		if(current == null) {
			current = Long.valueOf(0);
		}
		double check = Main.getLevelFor(item.getType());
		if(check < 0x0.0p0) {
			Container container = holder instanceof Container ? (Container) holder : null;
			Entity entity = holder instanceof Entity ? (Entity) holder : null;
			if(container != null) {
				Main.console.sendMessage("Found illegal ItemStack on skyworld island: " + Main.getItemName(item) + " inside container \"" + Main.getItemName(container.getBlock()) + "\" at: (" + container.getLocation().toVector().toString() + ")");
			} else if(entity != null) {
				Main.console.sendMessage("Found illegal ItemStack on skyworld island: " + Main.getItemName(item) + " inside entity \"" + entity.getName() + "\"(" + entity.getClass().getSimpleName() + ")'s inventory at: " + Main.vectorToString(entity.getLocation().toVector()));
			} else {
				Main.console.sendMessage("Found illegal ItemStack on skyworld island" + (holder == null ? "(no container or entity information was provided)" : "") + ": " + Main.getItemName(item) + (holder != null ? " inside inventory holder \"" + holder.getInventory().getTitle() + "\"(" + holder.getClass().getSimpleName() + ")'s inventory" : ""));
			}
		}
		map.put(item.getType(), Long.valueOf(current.longValue() + 1));
		if(Main.getPlugin().getMaterialConfig().getBoolean("checkContainers", true)) {
			if(item.getItemMeta() instanceof BlockStateMeta) {
				BlockStateMeta im = (BlockStateMeta) item.getItemMeta();
				if(im.getBlockState() instanceof InventoryHolder) {
					InventoryHolder invHolder = (InventoryHolder) im.getBlockState();
					Inventory inv = invHolder.getInventory();
					if(inv != null) {
						for(ItemStack i : inv.getContents()) {
							if(i == null) {
								continue;
							}
							countMaterials(i, map, invHolder);
						}
					}
				}
			}
		}
	}
	
}
