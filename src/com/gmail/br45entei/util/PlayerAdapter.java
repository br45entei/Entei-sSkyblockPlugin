package com.gmail.br45entei.util;

import com.gmail.br45entei.enteisSkyblock.main.Main;
import com.gmail.br45entei.enteisSkyblockGenerator.main.GeneratorMain;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.FluidCollisionMode;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

/** Class used to spoof player objects in event listeners
 *
 * @author Brian_Entei */
@SuppressWarnings("deprecation")
public class PlayerAdapter implements Player {
	
	private final UUID uuid;
	private final String name;
	private final int entityID = new Random().nextInt(Short.MAX_VALUE * 16);
	private volatile GameMode gameMode = GameMode.SURVIVAL;
	private volatile World world;
	private volatile double x = 0, y = 65, z = 0;
	private volatile float yaw = 90.0f, pitch = 0.0f;
	private int foodLevel = 20;
	private float saturation = 20;
	private float exhaustion = 0;
	private int level = 0;
	private int exp = 0;
	private boolean isSprinting = false;
	private boolean isSneaking = false;
	private boolean whitelisted = false;
	private boolean op = false;
	private ConcurrentHashMap<Player, Boolean> hiddenPlayers;
	private float flySpeed = 1.2f;
	private float walkSpeed = 0.8f;
	private boolean isFlying;
	private boolean allowFlight;
	private EntityDamageEvent lastDamageCause;
	private boolean invunerable;
	private boolean glowing;
	private boolean isSilent;
	private boolean hasGravity;
	private int vz;
	private int vy;
	private int vx;
	private Location bedSpawn;
	
	public PlayerAdapter(UUID uuid, String name, Location location, GameMode gameMode) {
		this.uuid = uuid;
		this.name = name;
		this.world = location.getWorld();
		this.world.toString();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public PlayerInventory getInventory() {
		return new PlayerInventory() {
			
			@Override
			public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setMaxStackSize(int size) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setContents(ItemStack[] items) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public void remove(ItemStack item) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void remove(Material material) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public ListIterator<ItemStack> iterator(int index) {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public ListIterator<ItemStack> iterator() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public List<HumanEntity> getViewers() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public InventoryType getType() {
				return InventoryType.PLAYER;
			}
			
			@Override
			public String getTitle() {
				return this.getTitle();
			}
			
			@Override
			public ItemStack[] getStorageContents() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public int getSize() {
				// TODO Auto-generated method stub (42!)
				return 36;
			}
			
			@Override
			public String getName() {
				return "container.inventory";
			}
			
			@Override
			public int getMaxStackSize() {
				return 64;
			}
			
			@Override
			public Location getLocation() {
				return null;
			}
			
			@Override
			public ItemStack getItem(int index) {
				return null;
			}
			
			@Override
			public ItemStack[] getContents() {
				// TODO Auto-generated method stub (42!)
				return new ItemStack[this.getSize()];
			}
			
			@Override
			public int firstEmpty() {
				return -1;
			}
			
			@Override
			public int first(ItemStack item) {
				return -1;
			}
			
			@Override
			public int first(Material material) throws IllegalArgumentException {
				return -1;
			}
			
			@Override
			public boolean containsAtLeast(ItemStack item, int amount) {
				// TODO Auto-generated method stub (42!)
				return false;
			}
			
			@Override
			public boolean contains(ItemStack item, int amount) {
				// TODO Auto-generated method stub (42!)
				return false;
			}
			
			@Override
			public boolean contains(Material material, int amount) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				return false;
			}
			
			@Override
			public boolean contains(ItemStack item) {
				// TODO Auto-generated method stub (42!)
				return false;
			}
			
			@Override
			public boolean contains(Material material) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				return false;
			}
			
			@Override
			public void clear(int index) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void clear() {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public void setLeggings(ItemStack leggings) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setItemInOffHand(ItemStack item) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setItemInMainHand(ItemStack item) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setItemInHand(ItemStack stack) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setItem(int index, ItemStack item) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setHelmet(ItemStack helmet) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setHeldItemSlot(int slot) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setExtraContents(ItemStack[] items) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setChestplate(ItemStack chestplate) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setBoots(ItemStack boots) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setArmorContents(ItemStack[] items) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public ItemStack getLeggings() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public ItemStack getItemInOffHand() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public ItemStack getItemInMainHand() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public ItemStack getItemInHand() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public HumanEntity getHolder() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public ItemStack getHelmet() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public int getHeldItemSlot() {
				// TODO Auto-generated method stub (42!)
				return 0;
			}
			
			@Override
			public ItemStack[] getExtraContents() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public ItemStack getChestplate() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public ItemStack getBoots() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public ItemStack[] getArmorContents() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
		};
	}
	
	@Override
	public Inventory getEnderChest() {
		return new Inventory() {
			
			@Override
			public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setMaxStackSize(int size) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void setContents(ItemStack[] items) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public void remove(ItemStack item) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void remove(Material material) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public ListIterator<ItemStack> iterator(int index) {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public ListIterator<ItemStack> iterator() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public List<HumanEntity> getViewers() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public InventoryType getType() {
				return InventoryType.ENDER_CHEST;
			}
			
			@Override
			public String getTitle() {
				return this.getTitle();
			}
			
			@Override
			public ItemStack[] getStorageContents() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public int getSize() {
				return 27;
			}
			
			@Override
			public String getName() {
				return "container.enderchest";
			}
			
			@Override
			public int getMaxStackSize() {
				return 64;
			}
			
			@Override
			public Location getLocation() {
				return null;
			}
			
			@Override
			public ItemStack getItem(int index) {
				return null;
			}
			
			@Override
			public ItemStack[] getContents() {
				// TODO Auto-generated method stub (42!)
				return new ItemStack[this.getSize()];
			}
			
			@Override
			public int firstEmpty() {
				return -1;
			}
			
			@Override
			public int first(ItemStack item) {
				return -1;
			}
			
			@Override
			public int first(Material material) throws IllegalArgumentException {
				return -1;
			}
			
			@Override
			public boolean containsAtLeast(ItemStack item, int amount) {
				// TODO Auto-generated method stub (42!)
				return false;
			}
			
			@Override
			public boolean contains(ItemStack item, int amount) {
				// TODO Auto-generated method stub (42!)
				return false;
			}
			
			@Override
			public boolean contains(Material material, int amount) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				return false;
			}
			
			@Override
			public boolean contains(ItemStack item) {
				// TODO Auto-generated method stub (42!)
				return false;
			}
			
			@Override
			public boolean contains(Material material) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				return false;
			}
			
			@Override
			public void clear(int index) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public void clear() {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
			@Override
			public void setItem(int index, ItemStack item) {
				// TODO Auto-generated method stub (42!)
				
			}
			
			@Override
			public HumanEntity getHolder() {
				// TODO Auto-generated method stub (42!)
				return null;
			}
			
		};
	}
	
	@Override
	public MainHand getMainHand() {
		return MainHand.RIGHT;
	}
	
	@Override
	public boolean setWindowProperty(Property prop, int value) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public InventoryView getOpenInventory() {
		return new InventoryView() {
			
			@Override
			public InventoryType getType() {
				return InventoryType.PLAYER;
			}
			
			@Override
			public Inventory getTopInventory() {
				return new Inventory() {
					
					@Override
					public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
						// TODO Auto-generated method stub (42!)
						
					}
					
					@Override
					public void setMaxStackSize(int size) {
						// TODO Auto-generated method stub (42!)
						
					}
					
					@Override
					public void setContents(ItemStack[] items) throws IllegalArgumentException {
						// TODO Auto-generated method stub (42!)
						
					}
					
					@Override
					public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
						// TODO Auto-generated method stub (42!)
						return null;
					}
					
					@Override
					public void remove(ItemStack item) {
						// TODO Auto-generated method stub (42!)
						
					}
					
					@Override
					public void remove(Material material) throws IllegalArgumentException {
						// TODO Auto-generated method stub (42!)
						
					}
					
					@Override
					public ListIterator<ItemStack> iterator(int index) {
						// TODO Auto-generated method stub (42!)
						return null;
					}
					
					@Override
					public ListIterator<ItemStack> iterator() {
						// TODO Auto-generated method stub (42!)
						return null;
					}
					
					@Override
					public List<HumanEntity> getViewers() {
						// TODO Auto-generated method stub (42!)
						return null;
					}
					
					@Override
					public InventoryType getType() {
						return InventoryType.CRAFTING;
					}
					
					@Override
					public String getTitle() {
						return this.getTitle();
					}
					
					@Override
					public ItemStack[] getStorageContents() {
						// TODO Auto-generated method stub (42!)
						return null;
					}
					
					@Override
					public int getSize() {
						// TODO Auto-generated method stub (42!)
						return 9;
					}
					
					@Override
					public String getName() {
						return "container.crafting";
					}
					
					@Override
					public int getMaxStackSize() {
						return 64;
					}
					
					@Override
					public Location getLocation() {
						return null;
					}
					
					@Override
					public ItemStack getItem(int index) {
						return null;
					}
					
					@Override
					public ItemStack[] getContents() {
						// TODO Auto-generated method stub (42!)
						return new ItemStack[this.getSize()];
					}
					
					@Override
					public int firstEmpty() {
						return -1;
					}
					
					@Override
					public int first(ItemStack item) {
						return -1;
					}
					
					@Override
					public int first(Material material) throws IllegalArgumentException {
						return -1;
					}
					
					@Override
					public boolean containsAtLeast(ItemStack item, int amount) {
						// TODO Auto-generated method stub (42!)
						return false;
					}
					
					@Override
					public boolean contains(ItemStack item, int amount) {
						// TODO Auto-generated method stub (42!)
						return false;
					}
					
					@Override
					public boolean contains(Material material, int amount) throws IllegalArgumentException {
						// TODO Auto-generated method stub (42!)
						return false;
					}
					
					@Override
					public boolean contains(ItemStack item) {
						// TODO Auto-generated method stub (42!)
						return false;
					}
					
					@Override
					public boolean contains(Material material) throws IllegalArgumentException {
						// TODO Auto-generated method stub (42!)
						return false;
					}
					
					@Override
					public void clear(int index) {
						// TODO Auto-generated method stub (42!)
						
					}
					
					@Override
					public void clear() {
						// TODO Auto-generated method stub (42!)
						
					}
					
					@Override
					public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {
						// TODO Auto-generated method stub (42!)
						return null;
					}
					
					@Override
					public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
						// TODO Auto-generated method stub (42!)
						return null;
					}
					
					@Override
					public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
						// TODO Auto-generated method stub (42!)
						return null;
					}
					
					@Override
					public void setItem(int index, ItemStack item) {
						// TODO Auto-generated method stub (42!)
						
					}
					
					@Override
					public HumanEntity getHolder() {
						// TODO Auto-generated method stub (42!)
						return null;
					}
					
				};
			}
			
			@Override
			public HumanEntity getPlayer() {
				return PlayerAdapter.this;
			}
			
			@Override
			public Inventory getBottomInventory() {
				return PlayerAdapter.this.getInventory();
			}
		};
	}
	
	@Override
	public InventoryView openInventory(Inventory inventory) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public InventoryView openWorkbench(Location location, boolean force) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public InventoryView openEnchanting(Location location, boolean force) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void openInventory(InventoryView inventory) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public InventoryView openMerchant(Villager trader, boolean force) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public InventoryView openMerchant(Merchant merchant, boolean force) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public ItemStack getItemInHand() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setItemInHand(ItemStack item) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public ItemStack getItemOnCursor() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setItemOnCursor(ItemStack item) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean hasCooldown(Material material) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public int getCooldown(Material material) {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void setCooldown(Material material, int ticks) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean isSleeping() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public int getSleepTicks() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public Location getBedSpawnLocation() {
		// TODO Auto-generated method stub (42!)
		return this.bedSpawn;
	}
	
	@Override
	public void setBedSpawnLocation(Location location) {
		location.getWorld().toString();
		this.bedSpawn = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}
	
	@Override
	public void setBedSpawnLocation(Location location, boolean force) {
		this.setBedSpawnLocation(location);
	}
	
	@Override
	public boolean sleep(Location location, boolean force) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void wakeup(boolean setSpawnLocation) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Location getBedLocation() {
		// TODO Auto-generated method stub (42!)
		return this.bedSpawn;
	}
	
	@Override
	public GameMode getGameMode() {
		return this.gameMode;
	}
	
	@Override
	public void setGameMode(GameMode mode) {
		mode.toString();
		this.gameMode = mode;
	}
	
	@Override
	public boolean isBlocking() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean isHandRaised() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public int getExpToLevel() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public boolean discoverRecipe(NamespacedKey recipe) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public int discoverRecipes(Collection<NamespacedKey> recipes) {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public boolean undiscoverRecipe(NamespacedKey recipe) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public int undiscoverRecipes(Collection<NamespacedKey> recipes) {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public Entity getShoulderEntityLeft() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setShoulderEntityLeft(Entity entity) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Entity getShoulderEntityRight() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setShoulderEntityRight(Entity entity) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public double getEyeHeight() {
		// TODO Auto-generated method stub (42!)
		return 1.75;
	}
	
	@Override
	public double getEyeHeight(boolean ignorePose) {
		// TODO Auto-generated method stub (42!)
		return 1.75;
	}
	
	@Override
	public Location getEyeLocation() {
		return new Location(this.world, this.x, this.y + this.getEyeHeight(), this.z, this.yaw, this.pitch);
	}
	
	@Override
	public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public Block getTargetBlockExact(int maxDistance) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public Block getTargetBlockExact(int maxDistance, FluidCollisionMode fluidCollisionMode) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public RayTraceResult rayTraceBlocks(double maxDistance) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public RayTraceResult rayTraceBlocks(double maxDistance, FluidCollisionMode fluidCollisionMode) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public int getRemainingAir() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void setRemainingAir(int ticks) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public int getMaximumAir() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void setMaximumAir(int ticks) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public int getMaximumNoDamageTicks() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void setMaximumNoDamageTicks(int ticks) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public double getLastDamage() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void setLastDamage(double damage) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public int getNoDamageTicks() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void setNoDamageTicks(int ticks) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Player getKiller() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public boolean addPotionEffect(PotionEffect effect) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean addPotionEffect(PotionEffect effect, boolean force) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean addPotionEffects(Collection<PotionEffect> effects) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean hasPotionEffect(PotionEffectType type) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public PotionEffect getPotionEffect(PotionEffectType type) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void removePotionEffect(PotionEffectType type) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public boolean hasLineOfSight(Entity other) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean getRemoveWhenFarAway() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void setRemoveWhenFarAway(boolean remove) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public EntityEquipment getEquipment() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setCanPickupItems(boolean pickup) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean getCanPickupItems() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean isLeashed() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public Entity getLeashHolder() throws IllegalStateException {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public boolean setLeashHolder(Entity holder) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean isGliding() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void setGliding(boolean gliding) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean isSwimming() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void setSwimming(boolean swimming) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean isRiptiding() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void setAI(boolean ai) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean hasAI() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void setCollidable(boolean collidable) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean isCollidable() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public AttributeInstance getAttribute(Attribute attribute) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void damage(double amount) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void damage(double amount, Entity source) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public double getHealth() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void setHealth(double health) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public double getMaxHealth() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void setMaxHealth(double health) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void resetMaxHealth() {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Location getLocation() {
		return new Location(this.world, this.x, this.y + this.getEyeHeight(), this.z, this.yaw, this.pitch);
	}
	
	@Override
	public Location getLocation(Location loc) {
		loc.setWorld(this.getWorld());
		loc.setPitch(this.pitch);
		loc.setYaw(this.yaw);
		loc.setX(this.x);
		loc.setY(this.y);
		loc.setZ(this.z);
		return loc;
	}
	
	@Override
	public void setVelocity(Vector velocity) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Vector getVelocity() {
		// TODO Auto-generated method stub (42!)
		return new Vector(this.vx, this.vy, this.vz);
	}
	
	@Override
	public double getHeight() {
		return this.getEyeHeight() + 0.25;
	}
	
	@Override
	public double getWidth() {
		// TODO Auto-generated method stub (42!)
		return 0.725;
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(-this.getWidth(), 0, -this.getWidth(), this.getWidth(), this.getHeight(), this.getWidth());
	}
	
	@Override
	public boolean isOnGround() {
		// TODO Auto-generated method stub (42!)
		return !this.isFlying;
	}
	
	@Override
	public World getWorld() {
		return this.world;
	}
	
	@Override
	public boolean teleport(Location location) {
		location.getWorld().toString();
		this.world = location.getWorld();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
		return true;
	}
	
	@Override
	public boolean teleport(Location location, TeleportCause cause) {
		return this.teleport(location);
	}
	
	@Override
	public boolean teleport(Entity destination) {
		return this.teleport(destination.getLocation());
	}
	
	@Override
	public boolean teleport(Entity destination, TeleportCause cause) {
		return this.teleport(destination.getLocation());
	}
	
	@Override
	public List<Entity> getNearbyEntities(double x, double y, double z) {
		return new ArrayList<>();
	}
	
	@Override
	public int getEntityId() {
		return this.entityID;
	}
	
	@Override
	public int getFireTicks() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public int getMaxFireTicks() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void setFireTicks(int ticks) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void remove() {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub (42!)
		return true;
	}
	
	@Override
	public Server getServer() {
		return Main.server;
	}
	
	@Override
	public boolean isPersistent() {
		// TODO Auto-generated method stub (42!)
		return true;
	}
	
	@Override
	public void setPersistent(boolean persistent) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Entity getPassenger() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public boolean setPassenger(Entity passenger) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public List<Entity> getPassengers() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public boolean addPassenger(Entity passenger) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean removePassenger(Entity passenger) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub (42!)
		return true;
	}
	
	@Override
	public boolean eject() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public float getFallDistance() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void setFallDistance(float distance) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setLastDamageCause(EntityDamageEvent event) {
		this.lastDamageCause = event;
	}
	
	@Override
	public EntityDamageEvent getLastDamageCause() {
		return this.lastDamageCause;
	}
	
	@Override
	public UUID getUniqueId() {
		return this.uuid;
	}
	
	@Override
	public int getTicksLived() {
		return new Long(GeneratorMain.getSkyworld().getFullTime()).intValue();
	}
	
	@Override
	public void setTicksLived(int value) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void playEffect(EntityEffect type) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}
	
	@Override
	public boolean isInsideVehicle() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean leaveVehicle() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public Entity getVehicle() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setCustomNameVisible(boolean flag) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean isCustomNameVisible() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void setGlowing(boolean flag) {
		this.glowing = flag;
	}
	
	@Override
	public boolean isGlowing() {
		// TODO Auto-generated method stub (42!)
		return this.glowing;
	}
	
	@Override
	public void setInvulnerable(boolean flag) {
		this.invunerable = flag;
	}
	
	@Override
	public boolean isInvulnerable() {
		// TODO Auto-generated method stub (42!)
		return this.invunerable;
	}
	
	@Override
	public boolean isSilent() {
		// TODO Auto-generated method stub (42!)
		return this.isSilent;
	}
	
	@Override
	public void setSilent(boolean flag) {
		this.isSilent = flag;
	}
	
	@Override
	public boolean hasGravity() {
		return this.hasGravity;
	}
	
	@Override
	public void setGravity(boolean gravity) {
		this.hasGravity = gravity;
	}
	
	@Override
	public int getPortalCooldown() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void setPortalCooldown(int cooldown) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Set<String> getScoreboardTags() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public boolean addScoreboardTag(String tag) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean removeScoreboardTag(String tag) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public PistonMoveReaction getPistonMoveReaction() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public BlockFace getFacing() {
		return BlockFace.WEST;//TODO add all the facings based on yaw and pitch
	}
	
	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public List<MetadataValue> getMetadata(String metadataKey) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public boolean hasMetadata(String metadataKey) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void sendMessage(String message) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void sendMessage(String[] messages) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean isPermissionSet(String name) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean isPermissionSet(Permission perm) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean hasPermission(String name) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean hasPermission(Permission perm) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void recalculatePermissions() {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public boolean isOp() {
		return this.op;
	}
	
	@Override
	public void setOp(boolean value) {
		this.op = value;
	}
	
	@Override
	public String getCustomName() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setCustomName(String name) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public boolean isConversing() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void acceptConversationInput(String input) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean beginConversation(Conversation conversation) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void abandonConversation(Conversation conversation) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean isOnline() {
		// TODO Auto-generated method stub (42!)
		return true;
	}
	
	@Override
	public boolean isBanned() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean isWhitelisted() {
		// TODO Auto-generated method stub (42!)
		return this.whitelisted;
	}
	
	@Override
	public void setWhitelisted(boolean value) {
		this.whitelisted = value;
	}
	
	@Override
	public Player getPlayer() {
		return this;
	}
	
	@Override
	public long getFirstPlayed() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public long getLastPlayed() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public boolean hasPlayedBefore() {
		return true;
	}
	
	@Override
	public Map<String, Object> serialize() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void sendPluginMessage(Plugin source, String channel, byte[] message) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Set<String> getListeningPluginChannels() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setDisplayName(String name) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public String getPlayerListName() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setPlayerListName(String name) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public String getPlayerListHeader() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public String getPlayerListFooter() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setPlayerListHeader(String header) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setPlayerListFooter(String footer) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setPlayerListHeaderFooter(String header, String footer) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setCompassTarget(Location loc) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Location getCompassTarget() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public InetSocketAddress getAddress() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void sendRawMessage(String message) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void kickPlayer(String message) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void chat(String msg) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean performCommand(String command) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public boolean isSneaking() {
		// TODO Auto-generated method stub (42!)
		return this.isSneaking;
	}
	
	@Override
	public void setSneaking(boolean sneak) {
		this.isSneaking = sneak;
	}
	
	@Override
	public boolean isSprinting() {
		// TODO Auto-generated method stub (42!)
		return this.isSprinting;
	}
	
	@Override
	public void setSprinting(boolean sprinting) {
		this.isSprinting = sprinting;
	}
	
	@Override
	public void saveData() {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void loadData() {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setSleepingIgnored(boolean isSleeping) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean isSleepingIgnored() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void playNote(Location loc, byte instrument, byte note) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void playNote(Location loc, Instrument instrument, Note note) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void playSound(Location location, Sound sound, float volume, float pitch) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void playSound(Location location, String sound, float volume, float pitch) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void stopSound(Sound sound) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void stopSound(String sound) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void stopSound(Sound sound, SoundCategory category) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void stopSound(String sound, SoundCategory category) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void playEffect(Location loc, Effect effect, int data) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public <T> void playEffect(Location loc, Effect effect, T data) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void sendBlockChange(Location loc, Material material, byte data) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void sendBlockChange(Location loc, BlockData block) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void sendMap(MapView map) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void updateInventory() {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void awardAchievement(Achievement achievement) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void removeAchievement(Achievement achievement) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean hasAchievement(Achievement achievement) {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public int getStatistic(Statistic statistic) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setPlayerTime(long time, boolean relative) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public long getPlayerTime() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public long getPlayerTimeOffset() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public boolean isPlayerTimeRelative() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void resetPlayerTime() {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setPlayerWeather(WeatherType type) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public WeatherType getPlayerWeather() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void resetPlayerWeather() {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void giveExp(int amount) {
		if(amount > 0) {
			this.exp += amount;
			while(this.exp >= 100) {
				this.exp -= 100;
				this.level++;
			}
		} else {
			this.exp -= amount;
			while(this.exp < 0) {
				this.exp += 100;
				this.level--;
			}
		}
	}
	
	@Override
	public void giveExpLevels(int amount) {
		this.level += amount;
	}
	
	@Override
	public float getExp() {
		// TODO Auto-generated method stub (42!)
		return this.exp / 100.0f;
	}
	
	@Override
	public void setExp(float exp) {
		int amount = Math.round(exp * 100.0f);
		this.exp = 0;
		this.giveExp(amount);
	}
	
	@Override
	public int getLevel() {
		return this.level;
	}
	
	@Override
	public void setLevel(int level) {
		this.level = level;
	}
	
	@Override
	public int getTotalExperience() {
		return this.level + this.exp;
	}
	
	@Override
	public void setTotalExperience(int exp) {
		this.level = 0;
		this.exp = 0;
		this.giveExp(exp);
	}
	
	@Override
	public float getExhaustion() {
		// TODO Auto-generated method stub (42!)
		return this.exhaustion;
	}
	
	@Override
	public void setExhaustion(float value) {
		this.exhaustion = value;
	}
	
	@Override
	public float getSaturation() {
		return this.saturation;
	}
	
	@Override
	public void setSaturation(float value) {
		this.saturation = value;
	}
	
	@Override
	public int getFoodLevel() {
		return this.foodLevel;
	}
	
	@Override
	public void setFoodLevel(int value) {
		this.foodLevel = value;
	}
	
	@Override
	public boolean getAllowFlight() {
		return this.allowFlight;
	}
	
	@Override
	public void setAllowFlight(boolean flight) {
		this.allowFlight = flight;
	}
	
	@Override
	public void hidePlayer(Player player) {
		ConcurrentHashMap<Player, Boolean> map = this.hiddenPlayers;
		map.put(player, Boolean.TRUE);
	}
	
	@Override
	public void hidePlayer(Plugin plugin, Player player) {
		ConcurrentHashMap<Player, Boolean> map = this.hiddenPlayers;
		map.put(player, Boolean.TRUE);
	}
	
	@Override
	public void showPlayer(Player player) {
		ConcurrentHashMap<Player, Boolean> map = this.hiddenPlayers;
		map.remove(player);
	}
	
	@Override
	public void showPlayer(Plugin plugin, Player player) {
		ConcurrentHashMap<Player, Boolean> map = this.hiddenPlayers;
		map.remove(player);
	}
	
	@Override
	public boolean canSee(Player player) {
		ConcurrentHashMap<Player, Boolean> map = this.hiddenPlayers;
		return map.get(player) != Boolean.TRUE;
	}
	
	@Override
	public boolean isFlying() {
		return this.isFlying;
	}
	
	@Override
	public void setFlying(boolean value) {
		this.isFlying = value;
	}
	
	@Override
	public void setFlySpeed(float value) throws IllegalArgumentException {
		this.flySpeed = value;
	}
	
	@Override
	public void setWalkSpeed(float value) throws IllegalArgumentException {
		this.walkSpeed = value;
	}
	
	@Override
	public float getFlySpeed() {
		return this.flySpeed;
	}
	
	@Override
	public float getWalkSpeed() {
		return this.walkSpeed;
	}
	
	@Override
	public void setTexturePack(String url) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setResourcePack(String url) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setResourcePack(String url, byte[] hash) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Scoreboard getScoreboard() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public boolean isHealthScaled() {
		// TODO Auto-generated method stub (42!)
		return false;
	}
	
	@Override
	public void setHealthScaled(boolean scale) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void setHealthScale(double scale) throws IllegalArgumentException {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public double getHealthScale() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public Entity getSpectatorTarget() {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public void setSpectatorTarget(Entity entity) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void sendTitle(String title, String subtitle) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void resetTitle() {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void spawnParticle(Particle particle, Location location, int count) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public AdvancementProgress getAdvancementProgress(Advancement advancement) {
		// TODO Auto-generated method stub (42!)
		return null;
	}
	
	@Override
	public int getClientViewDistance() {
		// TODO Auto-generated method stub (42!)
		return 0;
	}
	
	@Override
	public String getLocale() {
		return Locale.ENGLISH.toString();
	}
	
	@Override
	public void updateCommands() {
		// TODO Auto-generated method stub (42!)
		
	}
	
	@Override
	public Spigot spigot() {
		return new Spigot();
	}
	
}
