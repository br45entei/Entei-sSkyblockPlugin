package com.gmail.br45entei.enteisSkyblock.main;

import com.gmail.br45entei.enteisSkyblock.challenge.Challenge;
import com.gmail.br45entei.enteisSkyblockGenerator.main.GeneratorMain;

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

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.EvokerFangs;
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
		//if(spawnTID != -1) {
		//	Main.scheduler.cancelTask(spawnTID);
		//	spawnTID = -1;
		//}
		state[0] = false;
		while(spawnThread != null && spawnThread.isAlive()) {
			try {
				Thread.sleep(10L);
			} catch(InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		spawnThread = null;
	}
	
	protected static final boolean[] state = new boolean[] {true};
	protected static volatile Thread spawnThread = null;
	
	/** @return Whether or not the spawn task is running */
	public static final boolean isSpawnTaskRunning() {
		return spawnThread != null && spawnThread.isAlive();//spawnTID == -1 ? false : Main.scheduler.isCurrentlyRunning(spawnTID);
	}
	
	//@SuppressWarnings("deprecation")
	protected static final void startSpawnTask() {
		stopSpawnTask();
		state[0] = true;
		final boolean[] lastTaskType = new boolean[1];
		//spawnTID = Main.scheduler.scheduleAsyncRepeatingTask(Main.plugin, () -> {
		spawnThread = new Thread(() -> {
			while(state[0]) {
				for(Island island : islands) {
					if(island.getOwner() == null || island.getMembersOnIsland().isEmpty()) {
						continue;
					}
					//Main.console.sendMessage(ChatColor.WHITE + "Attempting to spawn on island \"" + island.getID() + "(" + island.getOwnerName() + ChatColor.RESET + ChatColor.WHITE + ")\"...");
					if(lastTaskType[0]) {
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
					} else {
						//int spawned = //
						spawnHostileMobsOn(island);
						//if(Main.server.getOfflinePlayer(island.getOwner()).isOnline()) {
						//	Main.server.getPlayer(island.getOwner()).sendMessage("Spawned " + spawned + " hostile mobs.");
						//}
					}
				}
				lastTaskType[0] = !lastTaskType[0];
				String tickSpeed = GeneratorMain.getSkyworld().getGameRuleValue("randomTickSpeed");
				if(!Main.isInt(tickSpeed)) {
					tickSpeed = "20";
					GeneratorMain.getSkyworld().setGameRuleValue("randomTickSpeed", tickSpeed);
				}
				try {
					Thread.sleep(Math.round(Math.min(1000.0, Math.max(10.0, spawnRate * (1.0 / Integer.parseInt(tickSpeed))))));
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}, "Entei's Skyblock Mob Spawning Thread");
		spawnThread.setDaemon(true);
		spawnThread.start();
		//}, 40L, 300L);// 300 / 20 = 15 seconds
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
	public static final void updateAllRegions() {
		for(Island island : islands) {
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
		while(Math.abs(x) < 2 && Math.abs(z) < 2) {
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
		int[] id = nextRandomID(0, 101);
		int i = 0;
		Island randomlyChosen;
		while((randomlyChosen = getIfExists(id[0], id[1])) != null) {
			if(randomlyChosen.getOwner() == null) {
				break;
			}
			id = nextRandomID(0, 101 + i);
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
		int z = 2;
		int lastZ = 2;
		int dir = 0;//0=right,1=down,2=left,3=up,4=right_check_x_equals_0
		while(true) {
			boolean exists = getIfExists(x, z) != null;
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
	
	/** @param player The player whose Island will be searched for
	 * @return The player's Island, if they were a member of(or owned)
	 *         one */
	public static final Island getIslandFor(OfflinePlayer player) {
		return player == null ? null : getIslandFor(player.getUniqueId());
	}
	
	/** @param player The UUID of the player whose Island will be searched
	 *            for
	 * @return The player's Island, if they were a member of(or owned)
	 *         one */
	public static final Island getIslandFor(UUID player) {
		for(Island island : islands) {
			if(island.isTestIsland) {
				continue;
			}
			for(UUID member : island.getMembers()) {
				if(member.toString().equals(player.toString())) {
					return island;
				}
			}
		}
		return null;
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
		if(Island.isInSkyworld(location)) {
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
		if(location.getWorld() != GeneratorMain.getSkyworld()) {
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
		case MYCEL:
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
		case SIGN_POST:
		case SNOW:
		case LADDER:
		case VINE:
		case WEB:
		case LONG_GRASS:
		case YELLOW_FLOWER:
		case RED_ROSE:
		case SAPLING:
		case BROWN_MUSHROOM:
		case RED_MUSHROOM:
		case CARPET:
		case DIODE_BLOCK_OFF:
		case DIODE_BLOCK_ON:
		case REDSTONE_WIRE:
		case REDSTONE_TORCH_OFF:
		case REDSTONE_TORCH_ON:
		case LEVER:
		case WOOD_PLATE:
		case STONE_PLATE:
		case GOLD_PLATE:
		case IRON_PLATE:
		case REDSTONE_COMPARATOR_OFF:
		case REDSTONE_COMPARATOR_ON:
		case WOOD_BUTTON:
		case STONE_BUTTON:
			return true;
		//$CASES-OMITTED$
		default:
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
					Witch.class};
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
		Main.scheduler.runTask(Main.plugin, () -> {
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
		});
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
		Main.scheduler.runTask(Main.plugin, () -> {
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
		});
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
						if(clazz != MushroomCow.class && floor.getType() == Material.MYCEL) {
							continue;
						}
						if(clazz == MushroomCow.class && floor.getBiome() != Biome.MUSHROOM_ISLAND && floor.getBiome() != Biome.MUSHROOM_ISLAND_SHORE) {
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
		Main.scheduler.runTask(Main.plugin, () -> {
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
							((HorseInventory) horse.getInventory()).setArmor(new ItemStack(Material.DIAMOND_BARDING));
						}
					}
				}
			}
			for(Player player : island.getMembersOnIsland()) {
				updateScoreBoardFor(player, hostilesOnIsland, currentCount);
			}
		});
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
		if(!canMobSpawnIn(block.getType())) {
			return false;
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
	
	protected final ConcurrentHashMap<String, Location> memberHomes = new ConcurrentHashMap<>();
	
	protected final ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> memberCompletedChallenges = new ConcurrentHashMap<>();
	
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
	
	/** @param member The member who just completed a challenge
	 * @param challengeName The name of the challenge that the member just
	 *            completed
	 * @return This Island */
	public final Island setCompleted(UUID member, String challengeName) {
		if(this.hasMemberCompleted(member, challengeName)) {
			return this;
		}
		if(this.isMember(member) && challengeName != null && !challengeName.trim().isEmpty()) {
			ConcurrentLinkedDeque<String> completedChallenges = get(member, this.memberCompletedChallenges);
			if(completedChallenges == null) {
				completedChallenges = new ConcurrentLinkedDeque<>();
				put(member, completedChallenges, this.memberCompletedChallenges);
			}
			completedChallenges.add(challengeName);
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
		ConcurrentLinkedDeque<String> completedChallenges = get(member, this.memberCompletedChallenges);
		if(completedChallenges == null) {
			completedChallenges = new ConcurrentLinkedDeque<>();
			put(member, completedChallenges, this.memberCompletedChallenges);
			return false;
		}
		for(String completedChallenge : completedChallenges) {
			if(completedChallenge.equalsIgnoreCase(challengeName)) {
				return true;
			}
		}
		return false;
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
	
	private volatile int netherPortalOrientation = -1;
	private volatile int netherPortalX = Integer.MIN_VALUE;
	private volatile int netherPortalY = Integer.MIN_VALUE;
	private volatile int netherPortalZ = Integer.MIN_VALUE;
	
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
		return Main.getNetherPortal(new Location(GeneratorMain.getSkyworld(), this.netherPortalX, this.netherPortalY, this.netherPortalZ));
	}
	
	/** @return The location of this Island's nether portal in the nether,
	 *         if it has
	 *         one. */
	public final Location getSkyNetherPortal() {
		int[] bounds = this.getBounds();
		for(int x = bounds[0]; x <= bounds[2]; x++) {
			for(int y = 0; y < GeneratorMain.getSkyworldNether().getMaxHeight(); y++) {
				for(int z = bounds[1]; z <= bounds[3]; z++) {
					Location portal = Main.getNetherPortal(new Location(GeneratorMain.getSkyworldNether(), x, y, z));
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
		this.memberHomes.clear();
		this.memberHomes.putAll(copy.memberHomes);
		this.memberCompletedChallenges.clear();
		this.memberCompletedChallenges.putAll(copy.memberCompletedChallenges);
		this.netherPortalOrientation = copy.netherPortalOrientation;
		this.netherPortalX = copy.netherPortalX;
		this.netherPortalY = copy.netherPortalY;
		this.netherPortalZ = copy.netherPortalZ;
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
		this.biome = Biome.FOREST;
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
	
	/** @param <T> The type of the value to get
	 * @param key The key to get
	 * @param map The map to get from
	 * @return The value stored in the map, or <b><code>null</code></b> if no
	 *         value was found */
	public static final <T> T get(UUID key, Map<String, T> map) {
		return map.get(key.toString());
	}
	
	/** @param <T> The type of the value to put
	 * @param key The to use when putting
	 * @param value The value to put
	 * @param map The map to put in
	 * @return The value previously stored in the map with the key, or
	 *         <b><code>null</code></b> if there was no value */
	public static final <T> T put(UUID key, T value, Map<String, T> map) {
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
			out.println("creationTime=" + Long.toString(this.createTime, 10));
			out.println("lastRestartTime=" + Long.toString(this.lastRestartTime, 10));
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
				ConcurrentLinkedDeque<String> memberChallenges = get(member, this.memberCompletedChallenges);
				if(memberChallenges == null) {
					memberChallenges = new ConcurrentLinkedDeque<>();
					put(member, memberChallenges, this.memberCompletedChallenges);
				}
				out.print(member.toString() + ":");
				int j = 1;
				int _size = memberChallenges.size();
				for(String challenge : memberChallenges) {
					out.print(challenge + (j == _size ? "" : "|"));
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
					dupe.createTime = Long.parseLong(value, 10);
				} else if(pname.equalsIgnoreCase("lastRestartTime")) {
					dupe.lastRestartTime = Long.parseLong(value, 10);
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
								ConcurrentLinkedDeque<String> completedChallenges = get(member, dupe.memberCompletedChallenges);
								if(completedChallenges == null) {
									completedChallenges = new ConcurrentLinkedDeque<>();
									put(member, completedChallenges, dupe.memberCompletedChallenges);
								}
								if(split.length > 1) {
									for(String challengeName : split[1].split(Pattern.quote("|"))) {
										if(challengeName.trim().isEmpty()) {
											continue;
										}
										completedChallenges.add(challengeName);
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
	public final double calculateLevel() {
		double level = 0x0.0p0;
		int[] bounds = this.getBounds();
		for(int x = bounds[0]; x <= bounds[2]; x++) {
			for(int y = 0; y < GeneratorMain.getSkyworld().getMaxHeight(); y++) {
				for(int z = bounds[1]; z <= bounds[3]; z++) {
					Block block = GeneratorMain.getSkyworld().getBlockAt(x, y, z);
					if(block != null) {
						double l = getLevelFor(block);
						level += l;
						if(l < 0x0.0p0) {
							@SuppressWarnings("deprecation")
							String name = Main.getItemName(new ItemStack(block.getType(), 1, (short) 0, Byte.valueOf(block.getData())));
							Main.console.sendMessage("Found illegal block on skyworld island(if block is a container, block contains illegal item(s)): " + name + " (" + x + "," + y + "," + z + ")");
						}
					}
				}
			}
		}
		for(Entity entity : GeneratorMain.getSkyworld().getNearbyEntities(this.getLocation(), GeneratorMain.island_Range / 2, GeneratorMain.getSkyworld().getMaxHeight() / 2, GeneratorMain.island_Range / 2)) {
			if(entity instanceof InventoryHolder && !(entity instanceof Monster)) {
				InventoryHolder invHolder = (InventoryHolder) entity;
				Inventory inv = invHolder.getInventory();
				if(inv != null) {
					for(ItemStack item : inv.getContents()) {
						if(item == null) {
							continue;
						}
						double l = getLevelFor(item);
						level += l;
						if(l < 0x0.0p0) {
							Main.console.sendMessage("Found illegal ItemStack on skyworld island: " + Main.getItemName(item));
						}
					}
				}
			}
		}
		this.level = level;
		this.lastLevelCalculation = System.currentTimeMillis();
		return level;
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
		return loc != null && Island.isInSkyworld(loc) && loc.getBlockX() >= bounds[0] && loc.getBlockZ() >= bounds[1] && loc.getBlockX() <= bounds[2] && loc.getBlockZ() <= bounds[3];
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
		if(this.members.size() >= this.memberLimit) {
			return false;
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
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) Main.server.getItemFactory().getItemMeta(Material.SKULL_ITEM);
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
		OfflinePlayer player = Main.server.getOfflinePlayer(owner);
		return this.setOwner(player);
	}
	
	/** @param player The player who will own this Island
	 * @return This Island */
	public final Island setOwner(OfflinePlayer player) {
		if((this.owner == null ? "" : this.owner.toString()).equals(player.getUniqueId().toString())) {
			return this.update();
		}
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
				x = check.getX() - 1;
			}
			if(check.getZ() < 0) {
				z = Math.abs(check.getZ()) - 1;
			} else {
				z = check.getZ() - 1;
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
	
	/** @param environment The dimension to get the region for
	 * @return This Island's WorldGuard region, if one has been created
	 *         with {@link #update()} beforehand */
	public final com.sk89q.worldguard.protection.regions.ProtectedRegion getRegion(Environment environment) {
		com.sk89q.worldguard.bukkit.WorldGuardPlugin wg = com.sk89q.worldguard.bukkit.WorldGuardPlugin.inst();
		com.sk89q.worldguard.protection.managers.RegionManager rm = wg.getRegionManager(environment == Environment.NORMAL ? GeneratorMain.getSkyworld() : environment == Environment.NETHER ? GeneratorMain.getSkyworldNether() : GeneratorMain.getSkyworldTheEnd());
		return rm.getRegion(this.getID());
	}
	
	/** @return This Island's bounds.
	 *         <b>{@code [minX, minZ, maxX, maxZ]}</b> */
	public final int[] getBounds() {
		int x = this.x * GeneratorMain.island_Range;
		int z = this.z * GeneratorMain.island_Range;
		int minX = x - ((GeneratorMain.island_Range / 2) - 2);
		int minZ = z - ((GeneratorMain.island_Range / 2) - 2);
		int maxX = x + ((GeneratorMain.island_Range / 2) - 4);
		int maxZ = z + ((GeneratorMain.island_Range / 2) - 4);
		if(minX < 0) {
			minX++;
		}
		if(minZ < 0) {
			minZ++;
		}
		if(maxX < 0) {
			maxX++;
		}
		if(maxZ < 0) {
			maxZ++;
		}
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
		return this.updateRegion(Environment.NORMAL).updateRegion(Environment.NETHER).updateRegion(Environment.THE_END);
	}
	
	private final Island deleteAllRegions() {
		return this.deleteRegion(Environment.NORMAL).deleteRegion(Environment.NETHER).deleteRegion(Environment.THE_END);
	}
	
	private final Island deleteRegion(Environment environment) {
		if(Main.getWorldGuard() == null) {
			return this;
		}
		com.sk89q.worldguard.protection.regions.ProtectedRegion region = this.getRegion(environment);
		World world = environment == Environment.NORMAL ? GeneratorMain.getSkyworld() : (environment == Environment.NETHER ? GeneratorMain.getSkyworldNether() : GeneratorMain.getSkyworldTheEnd());
		if(region != null) {
			com.sk89q.worldguard.bukkit.WorldGuardPlugin wg = com.sk89q.worldguard.bukkit.WorldGuardPlugin.inst();
			com.sk89q.worldguard.protection.managers.RegionManager rm = wg.getRegionManager(world);
			rm.removeRegion(region.getId());
		}
		return this;
	}
	
	private final Island updateRegion(Environment environment) {
		if(Main.getWorldGuard() == null) {
			return this;
		}
		com.sk89q.worldguard.protection.regions.ProtectedRegion region = this.getRegion(environment);
		World world = environment == Environment.NORMAL ? GeneratorMain.getSkyworld() : (environment == Environment.NETHER ? GeneratorMain.getSkyworldNether() : GeneratorMain.getSkyworldTheEnd());
		if(region == null) {
			com.sk89q.worldguard.bukkit.WorldGuardPlugin wg = com.sk89q.worldguard.bukkit.WorldGuardPlugin.inst();
			com.sk89q.worldguard.protection.managers.RegionManager rm = wg.getRegionManager(world);
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
		
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.BUILD, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.DAMAGE_ANIMALS, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.CHEST_ACCESS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.CHORUS_TELEPORT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.CREEPER_EXPLOSION, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);//(Blocked via event handler in Main class)
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.DAMAGE_ANIMALS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.DESTROY_VEHICLE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENDER_BUILD, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENDERPEARL, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENTITY_ITEM_FRAME_DESTROY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENTITY_PAINTING_DESTROY, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ENTRY, this.isLocked ? com.sk89q.worldguard.protection.flags.StateFlag.State.DENY : com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.EXIT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.EXIT_VIA_TELEPORT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.EXP_DROPS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.FALL_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.FIRE_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.FIREWORK_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.GHAST_FIREBALL, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.GRASS_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ICE_FORM, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ICE_MELT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.INTERACT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ITEM_DROP, this.isLocked ? com.sk89q.worldguard.protection.flags.StateFlag.State.DENY : com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.ITEM_PICKUP, this.isLocked ? com.sk89q.worldguard.protection.flags.StateFlag.State.DENY : com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.LAVA_FIRE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.LAVA_FLOW, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.LEAF_DECAY, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.LIGHTER, com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.LIGHTNING, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MOB_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MOB_SPAWNING, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MUSHROOMS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.MYCELIUM_SPREAD, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.OTHER_EXPLOSION, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.PASSTHROUGH, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.PISTONS, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.PLACE_VEHICLE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.POTION_SPLASH, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.PVP, this.pvpEnabled ? (this.isLocked ? com.sk89q.worldguard.protection.flags.StateFlag.State.DENY : com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW) : com.sk89q.worldguard.protection.flags.StateFlag.State.DENY);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.RIDE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.SLEEP, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.SNOW_FALL, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.SNOW_MELT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.SOIL_DRY, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.TNT, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.USE, this.isLocked ? com.sk89q.worldguard.protection.flags.StateFlag.State.DENY : com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.VINE_GROWTH, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.WATER_FLOW, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		region.setFlag(com.sk89q.worldguard.protection.flags.DefaultFlag.WITHER_DAMAGE, com.sk89q.worldguard.protection.flags.StateFlag.State.ALLOW);
		
		region.setPriority(100);
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
					int[] xz = Main.getWorldToChunkCoords(x, z);
					Main.getPlugin().getLogger().warning("Unable to get chunk at " + xz[0] + " " + xz[1] + ": " + ex.getClass().getName() + ": " + ex.getMessage());
					Throwable e = ex;
					while(e.getCause() != null) {
						e = e.getCause();
						Main.getPlugin().getLogger().warning("Caused by: " + e.getClass().getName() + ": " + e.getMessage());
					}
					continue;
				}
			}
		}
		return chunks;
	}
	
	/** Deletes all of the blocks contained within this Island, then
	 * generates a blank new one.
	 * 
	 * @return This Island */
	public final Island generateIsland() {
		if(this.isWithinSpawnArea()) {
			throw new IllegalStateException(ChatColor.DARK_RED + "Cannot generate an island in the spawn area!");
		}
		this.islandType = "normal";
		World world = GeneratorMain.getSkyworld();
		int X = this.x * GeneratorMain.island_Range;
		int Z = this.z * GeneratorMain.island_Range;
		int[] bounds = this.getBounds();
		int minX = bounds[0];
		int minZ = bounds[1];
		int maxX = bounds[2];
		int maxZ = bounds[3];
		for(int x = minX; x <= maxX; x++) {
			for(int y = 0; y < world.getMaxHeight(); y++) {
				for(int z = minZ; z <= maxZ; z++) {
					world.getBlockAt(x, y, z).setType(Material.AIR, false);
				}
			}
		}
		world.getBlockAt(X, GeneratorMain.island_Height, Z).setType(Material.OBSIDIAN, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 1, Z).setType(Material.SAND, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 2, Z).setType(Material.SAND, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 3, Z).setType(Material.SAND, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 5, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 6, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 11, Z).setType(Material.LEAVES, false);
		
		//Leaves Layer 1
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 11, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 11, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 11, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 11, Z - 1).setType(Material.LEAVES, false);
		
		//Leaves Layer 2
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 10, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 10, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 10, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 10, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 10, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 10, Z - 1).setType(Material.LEAVES, false);
		
		//Leaves Layer 3
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z - 1).setType(Material.LEAVES, false);
		
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 9, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 9, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 9, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 9, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 9, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 9, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 9, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 9, Z + 1).setType(Material.LEAVES, false);
		
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z - 2).setType(Material.LEAVES, false);
		
		//Leaves Layer 4
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z - 1).setType(Material.LEAVES, false);
		
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z + 2).setType(Material.LEAVES, false);
		
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z - 2).setType(Material.LEAVES, false);
		
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
		
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z - 2).setType(Material.GRASS, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z + 2).setType(Material.GRASS, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z - 2).setType(Material.GRASS, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z + 2).setType(Material.GRASS, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z - 2).setType(Material.GRASS, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z + 2).setType(Material.GRASS, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 4, Z + 2).setType(Material.GRASS, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 4, Z - 2).setType(Material.GRASS, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 4, Z + 2).setType(Material.GRASS, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 4, Z - 2).setType(Material.GRASS, false);
		world.getBlockAt(X + 3, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS, false);
		world.getBlockAt(X + 3, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS, false);
		world.getBlockAt(X + 3, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS, false);
		world.getBlockAt(X - 3, GeneratorMain.island_Height + 4, Z).setType(Material.GRASS, false);
		world.getBlockAt(X - 3, GeneratorMain.island_Height + 4, Z - 1).setType(Material.GRASS, false);
		world.getBlockAt(X - 3, GeneratorMain.island_Height + 4, Z + 1).setType(Material.GRASS, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z + 3).setType(Material.GRASS, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z + 3).setType(Material.GRASS, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z + 3).setType(Material.GRASS, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z - 3).setType(Material.GRASS, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z - 3).setType(Material.GRASS, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z - 3).setType(Material.GRASS, false);
		
		//Chest 1
		
		generateChestAt(world, X, GeneratorMain.island_Height + 5, Z - 1);
		return this.setBiome(this.biome);
	}
	
	/** Deletes all of the blocks contained within this Island, then
	 * generates a blank new square one.
	 * 
	 * @return This Island */
	public final Island generateSquareIsland() {
		if(this.isWithinSpawnArea()) {
			throw new IllegalStateException(ChatColor.DARK_RED + "Cannot generate an island in the spawn area!");
		}
		this.islandType = "square";
		World world = GeneratorMain.getSkyworld();
		this.spawnX = 0.5;
		this.spawnY = 3;
		this.spawnZ = -1.5;
		
		int X = this.x * GeneratorMain.island_Range;
		int Z = this.z * GeneratorMain.island_Range;
		int[] bounds = this.getBounds();
		int minX = bounds[0];
		int minZ = bounds[1];
		int maxX = bounds[2];
		int maxZ = bounds[3];
		for(int x = minX; x <= maxX; x++) {
			for(int y = 0; y < world.getMaxHeight(); y++) {
				for(int z = minZ; z <= maxZ; z++) {
					world.getBlockAt(x, y, z).setType(Material.AIR, false);
				}
			}
		}
		world.getBlockAt(X, GeneratorMain.island_Height, Z).setType(Material.OBSIDIAN, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 1, Z).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 2, Z).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 3, Z).setType(Material.DIRT, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 5, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 6, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z).setType(Material.LOG, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z).setType(Material.LEAVES, false);
		
		//Leaves Layer 1
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 10, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 10, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 10, Z - 1).setType(Material.LEAVES, false);
		
		//Leaves Layer 2
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 9, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 9, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 9, Z - 1).setType(Material.LEAVES, false);
		
		//Leaves Layer 3
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z - 1).setType(Material.LEAVES, false);
		
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 8, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 8, Z + 1).setType(Material.LEAVES, false);
		
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 8, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 8, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 8, Z - 2).setType(Material.LEAVES, false);
		
		//Leaves Layer 4
		
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 7, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 7, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 7, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 7, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 7, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 7, Z - 1).setType(Material.LEAVES, false);
		
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 7, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 7, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 7, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X + 2, GeneratorMain.island_Height + 7, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 7, Z - 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 7, Z).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 7, Z + 1).setType(Material.LEAVES, false);
		world.getBlockAt(X - 2, GeneratorMain.island_Height + 7, Z + 2).setType(Material.LEAVES, false);
		
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 7, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 7, Z + 2).setType(Material.LEAVES, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 7, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 7, Z - 2).setType(Material.LEAVES, false);
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 7, Z - 2).setType(Material.LEAVES, false);
		
		//Dirt
		for(int i = 0; i < 4; i++) {
			world.getBlockAt(X - 2, GeneratorMain.island_Height + i, Z + 1).setType(i == 3 ? Material.GRASS : Material.DIRT, false);
			world.getBlockAt(X - 1, GeneratorMain.island_Height + i, Z + 1).setType(Material.DIRT, false);
			/**/world.getBlockAt(X, GeneratorMain.island_Height + i, Z + 1).setType(Material.DIRT, false);
			world.getBlockAt(X + 1, GeneratorMain.island_Height + i, Z + 1).setType(Material.DIRT, false);
			world.getBlockAt(X + 2, GeneratorMain.island_Height + i, Z + 1).setType(i == 3 ? Material.GRASS : Material.DIRT, false);
		}
		for(int i = 0; i < 4; i++) {
			world.getBlockAt(X - 2, GeneratorMain.island_Height + i, Z).setType(i == 3 ? Material.GRASS : Material.DIRT, false);
			world.getBlockAt(X - 1, GeneratorMain.island_Height + i, Z).setType(i == 3 ? Material.GRASS : Material.DIRT, false);
			//world.getBlockAt(X, GeneratorMain.island_Height + i, Z).setType(i == 3 ? Material.GRASS : Material.DIRT, false);
			world.getBlockAt(X + 1, GeneratorMain.island_Height + i, Z).setType(i == 3 ? Material.GRASS : Material.DIRT, false);
			world.getBlockAt(X + 2, GeneratorMain.island_Height + i, Z).setType(i == 3 ? Material.GRASS : Material.DIRT, false);
		}
		for(int i = 0; i < 4; i++) {
			for(int j = 1; j < 3; j++) {
				world.getBlockAt(X - 2, GeneratorMain.island_Height + i, Z - j).setType(i == 3 ? Material.GRASS : Material.DIRT, false);
				world.getBlockAt(X - 1, GeneratorMain.island_Height + i, Z - j).setType(i == 3 ? Material.GRASS : Material.DIRT, false);
				if(i < 3) {
					world.getBlockAt(X, GeneratorMain.island_Height + i, Z - j).setType(i == 2 ? Material.GRASS : Material.DIRT, false);
				}
				world.getBlockAt(X + 1, GeneratorMain.island_Height + i, Z - j).setType(i == 3 ? Material.GRASS : Material.DIRT, false);
				world.getBlockAt(X + 2, GeneratorMain.island_Height + i, Z - j).setType(i == 3 ? Material.GRASS : Material.DIRT, false);
			}
		}
		
		//Sand 1
		
		world.getBlockAt(X - 1, GeneratorMain.island_Height + 4, Z + 1).setType(Material.SAND, false);
		world.getBlockAt(X, GeneratorMain.island_Height + 4, Z + 1).setType(Material.SAND, false);
		world.getBlockAt(X + 1, GeneratorMain.island_Height + 4, Z + 1).setType(Material.SAND, false);
		
		//Chest 1
		generateChestAt(world, X, GeneratorMain.island_Height + 3, Z - 1);
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
		Inventory inv = chest.getInventory();
		inv.setItem(0, new ItemStack(Material.ICE, 2));
		inv.setItem(1, new ItemStack(Material.LAVA_BUCKET, 1));
		inv.setItem(2, new ItemStack(Material.RED_MUSHROOM, 1));
		inv.setItem(3, new ItemStack(Material.BROWN_MUSHROOM, 1));
		inv.setItem(4, new ItemStack(Material.SUGAR_CANE, 1));
		inv.setItem(5, new ItemStack(Material.MELON, 1));
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
	public final Island restart() {
		this.wipeMembersInventories(ChatColor.YELLOW + "The island you are a member of has been restarted.");
		if(this.islandType == null || this.islandType.trim().isEmpty()) {
			this.islandType = "normal";
		}
		if(this.islandType.equalsIgnoreCase("normal")) {
			this.generateIsland();
		} else if(this.islandType.equalsIgnoreCase("square")) {
			this.generateIsland();
		} else {
			this.generateIsland();
		}
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
	 * @return This Island */
	public final Island deleteBlocks() {
		World world = GeneratorMain.getSkyworld();
		int[] bounds = this.getBounds();
		int minX = bounds[0];
		int minZ = bounds[1];
		int maxX = bounds[2];
		int maxZ = bounds[3];
		for(int x = minX; x <= maxX; x++) {
			for(int y = 0; y < world.getMaxHeight(); y++) {
				for(int z = minZ; z <= maxZ; z++) {
					world.getBlockAt(x, y, z).setType(Material.AIR, false);
					world.getBlockAt(x, y, z).setBiome(Biome.OCEAN);
				}
			}
		}
		return this;
	}
	
	/** Deletes all of the blocks contained within this Island, as well as its
	 * members' inventories.<br>
	 * Use with care.
	 * 
	 * @return This Island */
	public final Island deleteIsland() {
		this.deleteBlocks();
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
	
	/** @param block The block to check
	 * @return The level of the block and any items/blocks that it may
	 *         contain */
	public static final double getLevelFor(Block block) {
		@SuppressWarnings("deprecation")
		double level = getLevelFor(new ItemStack(block.getType(), 1, (short) 0, Byte.valueOf(block.getData())));
		if(block.getState() instanceof InventoryHolder) {
			InventoryHolder invHolder = (InventoryHolder) block.getState();
			Inventory inv = invHolder.getInventory();
			if(inv != null) {
				for(ItemStack item : inv.getContents()) {
					if(item == null) {
						continue;
					}
					double l = getLevelFor(item);
					level += l;
					if(l < 0x0.0p0) {
						Main.console.sendMessage("Found illegal ItemStack on skyworld island: " + Main.getItemName(item));
					}
				}
			}
		}
		return level;
	}
	
	/** @param item The item to check
	 * @return The level of the item and any items/blocks that it may
	 *         contain */
	public static final double getLevelFor(ItemStack item) {
		double level = 0x0.0p0;
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
						level += getLevelFor(i);
					}
				}
			}
		}
		switch(item.getType()) {
		case ACACIA_DOOR:
			level += 0.001;
			break;
		case ACACIA_DOOR_ITEM:
			level += 0.001;
			break;
		case ACACIA_FENCE:
			level += 0.001;
			break;
		case ACACIA_FENCE_GATE:
			level += 0.001;
			break;
		case ACACIA_STAIRS:
			level += 0.001;
			break;
		case ACTIVATOR_RAIL:
			level += 0.001;
			break;
		case AIR:
			level += 0.0;
			break;
		case ANVIL:
			level += 0.001;
			break;
		case APPLE:
			level += 0.001;
			break;
		case ARMOR_STAND:
			level += 0.001;
			break;
		case ARROW:
			level += 0.001;
			break;
		case BAKED_POTATO:
			level += 0.001;
			break;
		case BANNER:
			level += 0.001;
			break;
		case BARRIER:
			level += 0.001;
			break;
		case BEACON:
			level += 0.001;
			break;
		case BED:
			level += 0.001;
			break;
		case BEDROCK:
			level += -99999999.9999999999999999;
			break;
		case BED_BLOCK:
			level += 0.001;
			break;
		case BEETROOT:
			level += 0.001;
			break;
		case BEETROOT_BLOCK:
			level += 0.001;
			break;
		case BEETROOT_SEEDS:
			level += 0.001;
			break;
		case BEETROOT_SOUP:
			level += 0.001;
			break;
		case BIRCH_DOOR:
			level += 0.001;
			break;
		case BIRCH_DOOR_ITEM:
			level += 0.001;
			break;
		case BIRCH_FENCE:
			level += 0.001;
			break;
		case BIRCH_FENCE_GATE:
			level += 0.001;
			break;
		case BIRCH_WOOD_STAIRS:
			level += 0.001;
			break;
		case BLACK_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case BLACK_SHULKER_BOX:
			level += 0.001;
			break;
		case BLAZE_POWDER:
			level += 0.001;
			break;
		case BLAZE_ROD:
			level += 0.001;
			break;
		case BLUE_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case BLUE_SHULKER_BOX:
			level += 0.001;
			break;
		case BOAT:
			level += 0.001;
			break;
		case BOAT_ACACIA:
			level += 0.001;
			break;
		case BOAT_BIRCH:
			level += 0.001;
			break;
		case BOAT_DARK_OAK:
			level += 0.001;
			break;
		case BOAT_JUNGLE:
			level += 0.001;
			break;
		case BOAT_SPRUCE:
			level += 0.001;
			break;
		case BONE:
			level += 0.001;
			break;
		case BONE_BLOCK:
			level += 0.001;
			break;
		case BOOK:
			level += 0.001;
			break;
		case BOOKSHELF:
			level += 0.001;
			break;
		case BOOK_AND_QUILL:
			level += 0.001;
			break;
		case BOW:
			level += 0.001;
			break;
		case BOWL:
			level += 0.001;
			break;
		case BREAD:
			level += 0.001;
			break;
		case BREWING_STAND:
			level += 0.001;
			break;
		case BREWING_STAND_ITEM:
			level += 0.001;
			break;
		case BRICK:
			level += 0.001;
			break;
		case BRICK_STAIRS:
			level += 0.001;
			break;
		case BROWN_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case BROWN_MUSHROOM:
			level += 0.001;
			break;
		case BROWN_SHULKER_BOX:
			level += 0.001;
			break;
		case BUCKET:
			level += 0.001;
			break;
		case BURNING_FURNACE:
			level += 0.001;
			break;
		case CACTUS:
			level += 0.001;
			break;
		case CAKE:
			level += 0.001;
			break;
		case CAKE_BLOCK:
			level += 0.001;
			break;
		case CARPET:
			level += 0.001;
			break;
		case CARROT:
			level += 0.001;
			break;
		case CARROT_ITEM:
			level += 0.001;
			break;
		case CARROT_STICK:
			level += 0.001;
			break;
		case CAULDRON:
			level += 0.001;
			break;
		case CAULDRON_ITEM:
			level += 0.001;
			break;
		case CHAINMAIL_BOOTS:
			level += 0.001;
			break;
		case CHAINMAIL_CHESTPLATE:
			level += 0.001;
			break;
		case CHAINMAIL_HELMET:
			level += 0.001;
			break;
		case CHAINMAIL_LEGGINGS:
			level += 0.001;
			break;
		case CHEST:
			level += 0.001;
			break;
		case CHORUS_FLOWER:
			level += 0.001;
			break;
		case CHORUS_FRUIT:
			level += 0.001;
			break;
		case CHORUS_FRUIT_POPPED:
			level += 0.001;
			break;
		case CHORUS_PLANT:
			level += 0.001;
			break;
		case CLAY:
			level += 0.001;
			break;
		case CLAY_BALL:
			level += 0.001;
			break;
		case CLAY_BRICK:
			level += 0.001;
			break;
		case COAL:
			level += 0.001;
			break;
		case COAL_BLOCK:
			level += 0.001;
			break;
		case COAL_ORE:
			level += 0.001;
			break;
		case COBBLESTONE:
			level += 0.001;
			break;
		case COBBLESTONE_STAIRS:
			level += 0.00075;
			break;
		case COBBLE_WALL:
			level += 0.001;
			break;
		case COCOA:
			level += 0.001;
			break;
		case COMMAND:
			level += -99999999.9999999999999999;
			break;
		case COMMAND_CHAIN:
			level += -99999999.9999999999999999;
			break;
		case COMMAND_MINECART:
			level += -99999999.9999999999999999;
			break;
		case COMMAND_REPEATING:
			level += -99999999.9999999999999999;
			break;
		case COMPASS:
			level += 0.001;
			break;
		case CONCRETE:
			level += 0.001;
			break;
		case CONCRETE_POWDER:
			level += 0.001;
			break;
		case COOKED_BEEF:
			level += 0.001;
			break;
		case COOKED_CHICKEN:
			level += 0.001;
			break;
		case COOKED_FISH:
			level += 0.001;
			break;
		case COOKED_MUTTON:
			level += 0.001;
			break;
		case COOKED_RABBIT:
			level += 0.001;
			break;
		case COOKIE:
			level += 0.001;
			break;
		case CROPS:
			level += 0.001;
			break;
		case CYAN_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case CYAN_SHULKER_BOX:
			level += 0.001;
			break;
		case DARK_OAK_DOOR:
			level += 0.001;
			break;
		case DARK_OAK_DOOR_ITEM:
			level += 0.001;
			break;
		case DARK_OAK_FENCE:
			level += 0.001;
			break;
		case DARK_OAK_FENCE_GATE:
			level += 0.001;
			break;
		case DARK_OAK_STAIRS:
			level += 0.001;
			break;
		case DAYLIGHT_DETECTOR:
			level += 0.001;
			break;
		case DAYLIGHT_DETECTOR_INVERTED:
			level += 0.001;
			break;
		case DEAD_BUSH:
			level += 0.001;
			break;
		case DETECTOR_RAIL:
			level += 0.001;
			break;
		case DIAMOND:
			level += 0.001;
			break;
		case DIAMOND_AXE:
			level += 0.001;
			break;
		case DIAMOND_BARDING:
			level += 0.001;
			break;
		case DIAMOND_BLOCK:
			level += 0.001;
			break;
		case DIAMOND_BOOTS:
			level += 0.001;
			break;
		case DIAMOND_CHESTPLATE:
			level += 0.001;
			break;
		case DIAMOND_HELMET:
			level += 0.001;
			break;
		case DIAMOND_HOE:
			level += 0.001;
			break;
		case DIAMOND_LEGGINGS:
			level += 0.001;
			break;
		case DIAMOND_ORE:
			level += 0.001;
			break;
		case DIAMOND_PICKAXE:
			level += 0.001;
			break;
		case DIAMOND_SPADE:
			level += 0.001;
			break;
		case DIAMOND_SWORD:
			level += 0.001;
			break;
		case DIODE:
			level += 0.001;
			break;
		case DIODE_BLOCK_OFF:
			level += 0.001;
			break;
		case DIODE_BLOCK_ON:
			level += 0.001;
			break;
		case DIRT:
			level += 0.001;
			break;
		case DISPENSER:
			level += 0.001;
			break;
		case DOUBLE_PLANT:
			level += 0.001;
			break;
		case DOUBLE_STEP:
			level += 0.001;
			break;
		case DOUBLE_STONE_SLAB2:
			level += 0.001;
			break;
		case DRAGONS_BREATH:
			level += 0.001;
			break;
		case DRAGON_EGG:
			level += 500.0;
			break;
		case DROPPER:
			level += 0.001;
			break;
		case EGG:
			level += 0.001;
			break;
		case ELYTRA:
			level += 0.001;
			break;
		case EMERALD:
			level += 30.0;
			break;
		case EMERALD_BLOCK:
			level += 270.0;
			break;
		case EMERALD_ORE:
			level += 45.0;
			break;
		case EMPTY_MAP:
			level += 0.001;
			break;
		case ENCHANTED_BOOK:
			level += 0.001;
			break;
		case ENCHANTMENT_TABLE:
			level += 0.001;
			break;
		case ENDER_CHEST:
			level += 0.001;
			break;
		case ENDER_PEARL:
			level += 0.001;
			break;
		case ENDER_PORTAL:
			level += 0.001;
			break;
		case ENDER_PORTAL_FRAME:
			level += 0.001;
			break;
		case ENDER_STONE:
			level += 0.01;
			break;
		case END_BRICKS:
			level += 0.001;
			break;
		case END_CRYSTAL:
			level += 0.001;
			break;
		case END_GATEWAY:
			level += 0.001;
			break;
		case END_ROD:
			level += 0.001;
			break;
		case EXPLOSIVE_MINECART:
			level += 0.001;
			break;
		case EXP_BOTTLE:
			level += -99999999.9999999999999999;
			break;
		case EYE_OF_ENDER:
			level += 0.001;
			break;
		case FEATHER:
			level += 0.001;
			break;
		case FENCE:
			level += 0.001;
			break;
		case FENCE_GATE:
			level += 0.001;
			break;
		case FERMENTED_SPIDER_EYE:
			level += 0.001;
			break;
		case FIRE:
			level += 0.001;
			break;
		case FIREBALL:
			level += 0.001;
			break;
		case FIREWORK:
			level += 0.001;
			break;
		case FIREWORK_CHARGE:
			level += 0.001;
			break;
		case FISHING_ROD:
			level += 0.001;
			break;
		case FLINT:
			level += 0.001;
			break;
		case FLINT_AND_STEEL:
			level += 0.001;
			break;
		case FLOWER_POT:
			level += 0.001;
			break;
		case FLOWER_POT_ITEM:
			level += 0.001;
			break;
		case FROSTED_ICE:
			level += 0.001;
			break;
		case FURNACE:
			level += 0.001;
			break;
		case GHAST_TEAR:
			level += 0.001;
			break;
		case GLASS:
			level += 0.001;
			break;
		case GLASS_BOTTLE:
			level += 0.001;
			break;
		case GLOWING_REDSTONE_ORE:
			level += 0.001;
			break;
		case GLOWSTONE:
			level += 0.01;
			break;
		case GLOWSTONE_DUST:
			level += 0.0025;
			break;
		case GOLDEN_APPLE:
			level += 0.001;
			break;
		case GOLDEN_CARROT:
			level += 0.001;
			break;
		case GOLD_AXE:
			level += 0.001;
			break;
		case GOLD_BARDING:
			level += 0.001;
			break;
		case GOLD_BLOCK:
			level += 0.001;
			break;
		case GOLD_BOOTS:
			level += 0.001;
			break;
		case GOLD_CHESTPLATE:
			level += 0.001;
			break;
		case GOLD_HELMET:
			level += 0.001;
			break;
		case GOLD_HOE:
			level += 0.001;
			break;
		case GOLD_INGOT:
			level += 0.001;
			break;
		case GOLD_LEGGINGS:
			level += 0.001;
			break;
		case GOLD_NUGGET:
			level += 0.001;
			break;
		case GOLD_ORE:
			level += 0.001;
			break;
		case GOLD_PICKAXE:
			level += 0.001;
			break;
		case GOLD_PLATE:
			level += 0.001;
			break;
		case GOLD_RECORD:
			level += 0.001;
			break;
		case GOLD_SPADE:
			level += 0.001;
			break;
		case GOLD_SWORD:
			level += 0.001;
			break;
		case GRASS:
			level += 0.001;
			break;
		case GRASS_PATH:
			level += 0.001;
			break;
		case GRAVEL:
			level += 0.001;
			break;
		case GRAY_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case GRAY_SHULKER_BOX:
			level += 0.001;
			break;
		case GREEN_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case GREEN_RECORD:
			level += 0.001;
			break;
		case GREEN_SHULKER_BOX:
			level += 0.001;
			break;
		case GRILLED_PORK:
			level += 0.001;
			break;
		case HARD_CLAY:
			level += 0.001;
			break;
		case HAY_BLOCK:
			level += 0.001;
			break;
		case HOPPER:
			level += 0.001;
			break;
		case HOPPER_MINECART:
			level += 0.001;
			break;
		case HUGE_MUSHROOM_1:
			level += 0.001;
			break;
		case HUGE_MUSHROOM_2:
			level += 0.001;
			break;
		case ICE:
			level += 0.001;
			break;
		case INK_SACK:
			level += 0.001;
			break;
		case IRON_AXE:
			level += 0.001;
			break;
		case IRON_BARDING:
			level += 0.001;
			break;
		case IRON_BLOCK:
			level += 0.001;
			break;
		case IRON_BOOTS:
			level += 0.001;
			break;
		case IRON_CHESTPLATE:
			level += 0.001;
			break;
		case IRON_DOOR:
			level += 0.001;
			break;
		case IRON_DOOR_BLOCK:
			level += 0.001;
			break;
		case IRON_FENCE:
			level += 0.001;
			break;
		case IRON_HELMET:
			level += 0.001;
			break;
		case IRON_HOE:
			level += 0.001;
			break;
		case IRON_INGOT:
			level += 0.001;
			break;
		case IRON_LEGGINGS:
			level += 0.001;
			break;
		case IRON_NUGGET:
			level += 0.001;
			break;
		case IRON_ORE:
			level += 0.001;
			break;
		case IRON_PICKAXE:
			level += 0.001;
			break;
		case IRON_PLATE:
			level += 0.001;
			break;
		case IRON_SPADE:
			level += 0.001;
			break;
		case IRON_SWORD:
			level += 0.001;
			break;
		case IRON_TRAPDOOR:
			level += 0.001;
			break;
		case ITEM_FRAME:
			level += 0.001;
			break;
		case JACK_O_LANTERN:
			level += 0.001;
			break;
		case JUKEBOX:
			level += 0.001;
			break;
		case JUNGLE_DOOR:
			level += 0.001;
			break;
		case JUNGLE_DOOR_ITEM:
			level += 0.001;
			break;
		case JUNGLE_FENCE:
			level += 0.001;
			break;
		case JUNGLE_FENCE_GATE:
			level += 0.001;
			break;
		case JUNGLE_WOOD_STAIRS:
			level += 0.001;
			break;
		case KNOWLEDGE_BOOK:
			level += 0.001;
			break;
		case LADDER:
			level += 0.001;
			break;
		case LAPIS_BLOCK:
			level += 0.001;
			break;
		case LAPIS_ORE:
			level += 0.001;
			break;
		case LAVA:
			level += 0.001;
			break;
		case LAVA_BUCKET:
			level += 0.001;
			break;
		case LEASH:
			level += 0.001;
			break;
		case LEATHER:
			level += 0.001;
			break;
		case LEATHER_BOOTS:
			level += 0.001;
			break;
		case LEATHER_CHESTPLATE:
			level += 0.001;
			break;
		case LEATHER_HELMET:
			level += 0.001;
			break;
		case LEATHER_LEGGINGS:
			level += 0.001;
			break;
		case LEAVES:
			level += 0.001;
			break;
		case LEAVES_2:
			level += 0.001;
			break;
		case LEVER:
			level += 0.001;
			break;
		case LIGHT_BLUE_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case LIGHT_BLUE_SHULKER_BOX:
			level += 0.001;
			break;
		case LIME_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case LIME_SHULKER_BOX:
			level += 0.001;
			break;
		case LINGERING_POTION:
			level += 0.001;
			break;
		case LOG:
			level += 0.001;
			break;
		case LOG_2:
			level += 0.001;
			break;
		case LONG_GRASS:
			level += 0.001;
			break;
		case MAGENTA_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case MAGENTA_SHULKER_BOX:
			level += 0.001;
			break;
		case MAGMA:
			level += 0.001;
			break;
		case MAGMA_CREAM:
			level += 0.001;
			break;
		case MAP:
			level += 0.001;
			break;
		case MELON:
			level += 0.001;
			break;
		case MELON_BLOCK:
			level += 0.001;
			break;
		case MELON_SEEDS:
			level += 0.001;
			break;
		case MELON_STEM:
			level += 0.001;
			break;
		case MILK_BUCKET:
			level += 0.001;
			break;
		case MINECART:
			level += 0.001;
			break;
		case MOB_SPAWNER:
			level += 500.0;
			break;
		case MONSTER_EGG:
			level += 0.001;
			break;
		case MONSTER_EGGS:
			level += 0.001;
			break;
		case MOSSY_COBBLESTONE:
			level += 0.0015;
			break;
		case MUSHROOM_SOUP:
			level += 0.001;
			break;
		case MUTTON:
			level += 0.001;
			break;
		case MYCEL:
			level += 0.001;
			break;
		case NAME_TAG:
			level += 0.001;
			break;
		case NETHERRACK:
			level += 0.001;
			break;
		case NETHER_BRICK:
			level += 0.001;
			break;
		case NETHER_BRICK_ITEM:
			level += 0.001;
			break;
		case NETHER_BRICK_STAIRS:
			level += 0.001;
			break;
		case NETHER_FENCE:
			level += 0.001;
			break;
		case NETHER_STALK:
			level += 0.001;
			break;
		case NETHER_STAR:
			level += 0.001;
			break;
		case NETHER_WARTS:
			level += 0.001;
			break;
		case NETHER_WART_BLOCK:
			level += 0.001;
			break;
		case NOTE_BLOCK:
			level += 0.001;
			break;
		case OBSERVER:
			level += 0.001;
			break;
		case OBSIDIAN:
			level += 0.001;
			break;
		case ORANGE_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case ORANGE_SHULKER_BOX:
			level += 0.001;
			break;
		case PACKED_ICE:
			level += 0.001;
			break;
		case PAINTING:
			level += 0.001;
			break;
		case PAPER:
			level += 0.001;
			break;
		case PINK_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case PINK_SHULKER_BOX:
			level += 0.001;
			break;
		case PISTON_BASE:
			level += 0.001;
			break;
		case PISTON_EXTENSION:
			level += 0.001;
			break;
		case PISTON_MOVING_PIECE:
			level += 0.0;
			break;
		case PISTON_STICKY_BASE:
			level += 0.001;
			break;
		case POISONOUS_POTATO:
			level += 0.001;
			break;
		case PORK:
			level += 0.001;
			break;
		case PORTAL:
			level += 0.001;
			break;
		case POTATO:
			level += 0.001;
			break;
		case POTATO_ITEM:
			level += 0.001;
			break;
		case POTION:
			level += 0.001;
			break;
		case POWERED_MINECART:
			level += 0.001;
			break;
		case POWERED_RAIL:
			level += 0.001;
			break;
		case PRISMARINE:
			level += 0.001;
			break;
		case PRISMARINE_CRYSTALS:
			level += 0.001;
			break;
		case PRISMARINE_SHARD:
			level += 0.001;
			break;
		case PUMPKIN:
			level += 0.001;
			break;
		case PUMPKIN_PIE:
			level += 0.001;
			break;
		case PUMPKIN_SEEDS:
			level += 0.001;
			break;
		case PUMPKIN_STEM:
			level += 0.001;
			break;
		case PURPLE_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case PURPLE_SHULKER_BOX:
			level += 0.001;
			break;
		case PURPUR_BLOCK:
			level += 0.001;
			break;
		case PURPUR_DOUBLE_SLAB:
			level += 0.001;
			break;
		case PURPUR_PILLAR:
			level += 0.001;
			break;
		case PURPUR_SLAB:
			level += 0.001;
			break;
		case PURPUR_STAIRS:
			level += 0.001;
			break;
		case QUARTZ:
			level += 0.001;
			break;
		case QUARTZ_BLOCK:
			level += 0.001;
			break;
		case QUARTZ_ORE:
			level += 0.001;
			break;
		case QUARTZ_STAIRS:
			level += 0.001;
			break;
		case RABBIT:
			level += 0.001;
			break;
		case RABBIT_FOOT:
			level += 0.001;
			break;
		case RABBIT_HIDE:
			level += 0.001;
			break;
		case RABBIT_STEW:
			level += 0.001;
			break;
		case RAILS:
			level += 0.001;
			break;
		case RAW_BEEF:
			level += 0.001;
			break;
		case RAW_CHICKEN:
			level += 0.001;
			break;
		case RAW_FISH:
			level += 0.001;
			break;
		case RECORD_10:
			level += 0.001;
			break;
		case RECORD_11:
			level += 0.001;
			break;
		case RECORD_12:
			level += 0.001;
			break;
		case RECORD_3:
			level += 0.001;
			break;
		case RECORD_4:
			level += 0.001;
			break;
		case RECORD_5:
			level += 0.001;
			break;
		case RECORD_6:
			level += 0.001;
			break;
		case RECORD_7:
			level += 0.001;
			break;
		case RECORD_8:
			level += 0.001;
			break;
		case RECORD_9:
			level += 0.001;
			break;
		case REDSTONE:
			level += 0.001;
			break;
		case REDSTONE_BLOCK:
			level += 0.009;
			break;
		case REDSTONE_COMPARATOR:
			level += 0.001;
			break;
		case REDSTONE_COMPARATOR_OFF:
			level += 0.001;
			break;
		case REDSTONE_COMPARATOR_ON:
			level += 0.001;
			break;
		case REDSTONE_LAMP_OFF:
			level += 0.001;
			break;
		case REDSTONE_LAMP_ON:
			level += 0.001;
			break;
		case REDSTONE_ORE:
			level += 0.004;
			break;
		case REDSTONE_TORCH_OFF:
			level += 0.001;
			break;
		case REDSTONE_TORCH_ON:
			level += 0.001;
			break;
		case REDSTONE_WIRE:
			level += 0.001;
			break;
		case RED_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case RED_MUSHROOM:
			level += 0.001;
			break;
		case RED_NETHER_BRICK:
			level += 0.001;
			break;
		case RED_ROSE:
			level += 0.001;
			break;
		case RED_SANDSTONE:
			level += 0.001;
			break;
		case RED_SANDSTONE_STAIRS:
			level += 0.001;
			break;
		case RED_SHULKER_BOX:
			level += 0.001;
			break;
		case ROTTEN_FLESH:
			level += 0.001;
			break;
		case SADDLE:
			level += 0.001;
			break;
		case SAND:
			level += 0.001;
			break;
		case SANDSTONE:
			level += 0.001;
			break;
		case SANDSTONE_STAIRS:
			level += 0.001;
			break;
		case SAPLING:
			level += 0.001;
			break;
		case SEA_LANTERN:
			level += 0.001;
			break;
		case SEEDS:
			level += 0.001;
			break;
		case SHEARS:
			level += 0.001;
			break;
		case SHIELD:
			level += 0.001;
			break;
		case SHULKER_SHELL:
			level += 0.001;
			break;
		case SIGN:
			level += 0.001;
			break;
		case SIGN_POST:
			level += 0.001;
			break;
		case SILVER_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case SILVER_SHULKER_BOX:
			level += 0.001;
			break;
		case SKULL:
			level += 0.001;
			break;
		case SKULL_ITEM:
			level += 0.001;
			break;
		case SLIME_BALL:
			level += 0.001;
			break;
		case SLIME_BLOCK:
			level += 0.001;
			break;
		case SMOOTH_BRICK:
			level += 0.008;
			break;
		case SMOOTH_STAIRS:
			level += 0.006;
			break;
		case SNOW:
			level += 0.001;
			break;
		case SNOW_BALL:
			level += 0.00025;
			break;
		case SNOW_BLOCK:
			level += 0.008;
			break;
		case SOIL:
			level += 0.001;
			break;
		case SOUL_SAND:
			level += 0.01;
			break;
		case SPECKLED_MELON:
			level += 0.01;
			break;
		case SPECTRAL_ARROW:
			level += 0.001;
			break;
		case SPIDER_EYE:
			level += 0.001;
			break;
		case SPLASH_POTION:
			level += 0.001;
			break;
		case SPONGE:
			level += 0.001;
			break;
		case SPRUCE_DOOR:
			level += 0.001;
			break;
		case SPRUCE_DOOR_ITEM:
			level += 0.001;
			break;
		case SPRUCE_FENCE:
			level += 0.001;
			break;
		case SPRUCE_FENCE_GATE:
			level += 0.001;
			break;
		case SPRUCE_WOOD_STAIRS:
			level += 0.001;
			break;
		case STAINED_CLAY:
			level += 0.001;
			break;
		case STAINED_GLASS:
			level += 0.001;
			break;
		case STAINED_GLASS_PANE:
			level += 0.001;
			break;
		case STANDING_BANNER:
			level += 0.001;
			break;
		case STATIONARY_LAVA:
			level += 0.001;
			break;
		case STATIONARY_WATER:
			level += 0.001;
			break;
		case STEP:
			level += 0.001;
			break;
		case STICK:
			level += 0.001;
			break;
		case STONE:
			level += 0.002;
			break;
		case STONE_AXE:
			level += 0.001;
			break;
		case STONE_BUTTON:
			level += 0.001;
			break;
		case STONE_HOE:
			level += 0.001;
			break;
		case STONE_PICKAXE:
			level += 0.001;
			break;
		case STONE_PLATE:
			level += 0.001;
			break;
		case STONE_SLAB2:
			level += 0.001;
			break;
		case STONE_SPADE:
			level += 0.001;
			break;
		case STONE_SWORD:
			level += 0.001;
			break;
		case STORAGE_MINECART:
			level += 0.001;
			break;
		case STRING:
			level += 0.001;
			break;
		case STRUCTURE_BLOCK:
			level += 0.001;
			break;
		case STRUCTURE_VOID:
			level += 0.0;
			break;
		case SUGAR:
			level += 0.001;
			break;
		case SUGAR_CANE:
			level += 0.001;
			break;
		case SUGAR_CANE_BLOCK:
			level += 0.001;
			break;
		case SULPHUR:
			level += 0.001;
			break;
		case THIN_GLASS:
			level += 0.001;
			break;
		case TIPPED_ARROW:
			level += 0.001;
			break;
		case TNT:
			level += 0.001;
			break;
		case TORCH:
			level += 0.001;
			break;
		case TOTEM:
			level += 0.001;
			break;
		case TRAPPED_CHEST:
			level += 0.001;
			break;
		case TRAP_DOOR:
			level += 0.001;
			break;
		case TRIPWIRE:
			level += 0.001;
			break;
		case TRIPWIRE_HOOK:
			level += 0.001;
			break;
		case VINE:
			level += 0.001;
			break;
		case WALL_BANNER:
			level += 0.001;
			break;
		case WALL_SIGN:
			level += 0.001;
			break;
		case WATCH:
			level += 0.001;
			break;
		case WATER:
			level += 0.001;
			break;
		case WATER_BUCKET:
			level += 0.001;
			break;
		case WATER_LILY:
			level += 0.001;
			break;
		case WEB:
			level += 0.001;
			break;
		case WHEAT:
			level += 0.001;
			break;
		case WHITE_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case WHITE_SHULKER_BOX:
			level += 0.001;
			break;
		case WOOD:
			level += 0.001;
			break;
		case WOODEN_DOOR:
			level += 0.001;
			break;
		case WOOD_AXE:
			level += 0.001;
			break;
		case WOOD_BUTTON:
			level += 0.001;
			break;
		case WOOD_DOOR:
			level += 0.001;
			break;
		case WOOD_DOUBLE_STEP:
			level += 0.001;
			break;
		case WOOD_HOE:
			level += 0.001;
			break;
		case WOOD_PICKAXE:
			level += 0.001;
			break;
		case WOOD_PLATE:
			level += 0.001;
			break;
		case WOOD_SPADE:
			level += 0.001;
			break;
		case WOOD_STAIRS:
			level += 0.001;
			break;
		case WOOD_STEP:
			level += 0.001;
			break;
		case WOOD_SWORD:
			level += 0.001;
			break;
		case WOOL:
			level += 0.001;
			break;
		case WORKBENCH:
			level += 0.001;
			break;
		case WRITTEN_BOOK:
			level += 0.001;
			break;
		case YELLOW_FLOWER:
			level += 0.001;
			break;
		case YELLOW_GLAZED_TERRACOTTA:
			level += 0.001;
			break;
		case YELLOW_SHULKER_BOX:
			level += 0.001;
			break;
		default:
			level += 0.0;
			break;
		}
		return level;
	}
	
}
