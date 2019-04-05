package com.gmail.br45entei.enteisSkyblock.main;

import com.drtshock.playervaults.vaultmanagement.Base64Serialization;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/** @author Brian_Entei */
public abstract class InventoryGUI implements Listener {
	
	private static final ConcurrentLinkedDeque<InventoryGUI> instances = new ConcurrentLinkedDeque<>();
	private static final ConcurrentHashMap<UUID, InventoryGUI> perPlayerInstances = new ConcurrentHashMap<>();
	
	/** @param args Program command line arguments */
	public static final void main(String[] args) {
		System.out.println(nextMultipleOf9(7));
		System.out.println(nextMultipleOf9(11));
		System.out.println(nextMultipleOf9(55));
	}
	
	private static volatile InventoryGUI listener;
	
	private static final void registerListeners() {
		if(listener == null) {
			new InventoryOwner(null).toString();//Load the class so that java.lang.NoClassDefFoundError: com/gmail/br45entei/enteisSkyblock/main/InventoryGUI$InventoryOwner is not thrown in onDisable...
			new InventoryGUI() {
				@Override
				public void onClick(int slot, Player player, Inventory inventory) {
				}
			}.toString();
		}
	}
	
	protected volatile String title;
	protected volatile int size;
	protected volatile boolean allowEditing;
	protected volatile boolean removeOnClose;
	
	protected transient volatile int currentPage = 1;
	protected volatile Sound openSound = null, closeSound = null,
			clickSound = null;
	
	protected transient volatile ClickAction clickAction = null;
	
	protected final Plugin plugin;
	
	protected transient volatile Inventory inventory = null;
	
	protected final ConcurrentHashMap<Integer, ItemStack> slots = new ConcurrentHashMap<>();
	
	protected InventoryGUI() {
		if(listener != null) {
			throw new IllegalStateException("Cannot instantiate more than one listener!");
		}
		listener = this;
		this.title = "NULL";
		this.size = 0;
		this.allowEditing = false;
		this.removeOnClose = true;
		this.plugin = Main.plugin;
		Bukkit.getServer().getPluginManager().registerEvents(listener, Main.plugin);
	}
	
	/** @param title The title to use
	 * @param size The size of the inventory */
	public InventoryGUI(String title, int size) {
		this(title, size, false);
	}
	
	/** @param title The title to use
	 * @param size The size of the inventory
	 * @param plugin The plugin that will be using this InventoryGUI */
	public InventoryGUI(String title, int size, Plugin plugin) {
		this(title, size, false, plugin);
	}
	
	/** @param title The title to use
	 * @param size The size of the inventory
	 * @param allowEditing Whether or not players will be able to edit the
	 *            inventory */
	public InventoryGUI(String title, int size, boolean allowEditing) {
		this(title, size, allowEditing, true, Main.plugin);
	}
	
	/** @param title The title to use
	 * @param size The size of the inventory
	 * @param allowEditing Whether or not players will be able to edit the
	 *            inventory
	 * @param plugin The plugin that will be using this InventoryGUI */
	public InventoryGUI(String title, int size, boolean allowEditing, Plugin plugin) {
		this(title, size, allowEditing, true, plugin);
	}
	
	/** @param title The title to use
	 * @param size The size of the inventory
	 * @param allowEditing Whether or not players will be able to edit the
	 *            inventory
	 * @param removeOnClose Whether or not this InventoryGUI is only going to be
	 *            used once, and should be removed after the player is done
	 *            using it */
	public InventoryGUI(String title, int size, boolean allowEditing, boolean removeOnClose) {
		this(title, size, allowEditing, removeOnClose, Main.plugin);
	}
	
	/** @param title The title to use
	 * @param size The size of the inventory
	 * @param allowEditing Whether or not players will be able to edit the
	 *            inventory
	 * @param removeOnClose Whether or not this InventoryGUI is only going to be
	 *            used once, and should be removed after the player is done
	 *            using it
	 * @param plugin The plugin that will be using this InventoryGUI */
	public InventoryGUI(String title, int size, boolean allowEditing, boolean removeOnClose, Plugin plugin) {
		registerListeners();
		this.title = title;
		this.size = size;
		this.allowEditing = allowEditing;
		this.removeOnClose = removeOnClose;
		this.plugin = plugin;
		instances.add(this);
	}
	
	private static final UUID getFromMap(UUID key) {
		for(UUID uuid : perPlayerInstances.keySet()) {
			if(uuid.toString().equals(key.toString())) {
				return uuid;
			}
		}
		return key;
	}
	
	/** @param player The player
	 * @return The InventoryGUI for the given player */
	public static final InventoryGUI getStorageChestForPlayer(OfflinePlayer player) {
		return getStorageChestForPlayer(player, null);
	}
	
	/** @param player The player
	 * @param action The ClickAction that will be used if the InventoryGUI has
	 *            not been created yet
	 * @return The InventoryGUI for the given player */
	public static final InventoryGUI getStorageChestForPlayer(OfflinePlayer player, ClickAction action) {
		return getStorageChestForPlayer(player.getUniqueId(), action);
	}
	
	/** @param player The player
	 * @return The InventoryGUI for the given player */
	public static final InventoryGUI getStorageChestForPlayer(UUID player) {
		return getStorageChestForPlayer(player, null);
	}
	
	/** @param player The player
	 * @param action The ClickAction that will be used if the InventoryGUI has
	 *            not been created yet
	 * @return The InventoryGUI for the given player */
	public static final InventoryGUI getStorageChestForPlayer(UUID player, ClickAction action) {
		player = getFromMap(player);
		InventoryGUI gui = perPlayerInstances.get(player);
		if(gui == null) {
			gui = new InventoryGUI(ChatColor.GOLD + "Storage Chest", 54, true, false) {
				@Override
				public void onClick(int slot, Player player, Inventory inventory) {
				}
			}.setDefaultOpenSound().setDefaultCloseSound();
			perPlayerInstances.put(player, gui);
			load(player, gui);
		}
		return gui;
	}
	
	public static final File getPerPlayerInventoriesSaveFolder() {
		return new File(Main.plugin.getDataFolder().getAbsolutePath().concat(File.separator).concat("Inventories"));
	}
	
	private static final void load(UUID player, InventoryGUI gui) {
		File folder = getPerPlayerInventoriesSaveFolder();
		if(!folder.exists()) {
			folder.mkdirs();
			save(player, gui);
			return;
		}
		File file = new File(folder, player.toString() + ".inv");
		if(!file.exists()) {
			save(player, gui);
			return;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try(FileInputStream in = new FileInputStream(file)) {
			int len;
			byte[] b = new byte[4096];
			while((len = in.read(b)) != -1) {
				baos.write(b, 0, len);
			}
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
		Inventory inventory = Base64Serialization.fromBase64(new String(baos.toByteArray(), StandardCharsets.UTF_8));
		gui.size = inventory.getSize();
		for(int i = 0; i < inventory.getSize(); i++) {
			gui.setSlotIcon(i, inventory.getItem(i));
		}
		gui.updateInventory();
	}
	
	private static final void save(UUID player, InventoryGUI gui) {
		File folder = getPerPlayerInventoriesSaveFolder();
		if(!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(folder, player.toString() + ".inv");
		String data = Base64Serialization.toBase64(gui.open());
		try(FileOutputStream out = new FileOutputStream(file)) {
			out.write(data.getBytes(StandardCharsets.UTF_8));
			out.flush();
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
		gui.updateInventory();
	}
	
	public static final void loadPlayersInventory(OfflinePlayer player) {
		loadPlayersInventory(player.getUniqueId());
	}
	
	public static final void loadPlayersInventory(UUID player) {
		File folder = getPerPlayerInventoriesSaveFolder();
		File file = new File(folder, player.toString().concat(".inv"));
		if(file.isFile()) {
			InventoryGUI gui = getStorageChestForPlayer(player);
			load(player, gui);
		}
	}
	
	public static final void savePlayersInventory(UUID player) {
		InventoryGUI gui = getStorageChestForPlayer(player);
		save(player, gui);
	}
	
	/** Load all per-player inventories from file. */
	public static final void loadPerPlayerInventories() {
		File folder = getPerPlayerInventoriesSaveFolder();
		if(!folder.exists()) {
			folder.mkdirs();
			return;
		}
		for(String fileName : folder.list()) {
			if(fileName.endsWith(".inv")) {
				String name = FilenameUtils.getBaseName(fileName);
				if(Main.isUUID(name)) {
					File file = new File(folder, fileName);
					if(file.isFile()) {
						UUID player = UUID.fromString(name);
						InventoryGUI gui = getStorageChestForPlayer(player);
						load(player, gui);
					}
				}
			}
		}
	}
	
	/** Save all per-player inventories to file. */
	public static final void savePerPlayerInventories() {
		File folder = new File(Main.plugin.getDataFolder().getAbsolutePath().concat(File.separator).concat("Inventories"));
		if(!folder.exists()) {
			folder.mkdirs();
		}
		for(Entry<UUID, InventoryGUI> entry : perPlayerInstances.entrySet()) {
			UUID player = entry.getKey();
			InventoryGUI gui = entry.getValue();
			save(player, gui);
		}
	}
	
	/** @return This InventoryGUI's size, adjusted to be a multiple of 9 between
	 *         9 and 54 inclusive. */
	public final int getSize() {
		return Math.min(54, nextMultipleOf9(this.size));
	}
	
	/** @return Whether or not players can edit the inventory */
	public final boolean allowEditing() {
		return this.allowEditing;
	}
	
	/** @param allowEditing Whether or not players can edit the inventory
	 * @return This InventoryGUI */
	public final InventoryGUI setAllowEditing(boolean allowEditing) {
		this.allowEditing = allowEditing;
		return this;
	}
	
	/** @param i The integer
	 * @return The integer, incremented until it is equal to the next multiple
	 *         of 9. If the given integer was already a multiple of 9, it is
	 *         returned. */
	public static final int nextMultipleOf9(int i) {
		while(i % 9 != 0 || i < 0) {
			i++;
		}
		return i;
	}
	
	/** @param slot The slot to get
	 * @param createIfNull Whether or not an ItemStack of stone should be
	 *            created if no item was found for the given slot
	 * @return The ItemStack for the given slot, or <code>null</code> if none
	 *         was found and <code>createIfNull</code> is false */
	public ItemStack getSlotIcon(int slot, boolean createIfNull) {
		Integer key = Integer.valueOf(slot);
		ItemStack icon = this.slots.get(key);
		if(icon == null && createIfNull) {
			icon = new ItemStack(Material.STONE);
			this.slots.put(key, icon);
		}
		return icon;
	}
	
	/** @param slot The slot to get
	 * @param createIfNull Whether or not an ItemStack of stone should be
	 *            created if no item was found for the given slot
	 * @return The ItemMeta for the given slot, or <code>null</code> if none was
	 *         found and <code>createIfNull</code> is false */
	public ItemMeta getSlotMeta(int slot, boolean createIfNull) {
		ItemStack icon = this.getSlotIcon(slot, createIfNull);
		if(icon != null) {
			if(icon.hasItemMeta()) {
				return icon.getItemMeta();
			}
			ItemMeta meta = Bukkit.getServer().getItemFactory().getItemMeta(icon.getType());
			icon.setItemMeta(meta);
			return meta;
		}
		return null;
	}
	
	/** <b>Note:</b>&nbsp;This method will fail silently if there is no
	 * ItemStack in the given slot.
	 * 
	 * @param slot The slot to modify
	 * @param icon The Material to use as an icon
	 * @return This InventoryGUI */
	public InventoryGUI setSlotIcon(int slot, Material icon) {
		icon = icon == null ? Material.AIR : icon;
		ItemStack item = this.getSlotIcon(slot, false);
		if(item != null) {
			item.setType(icon);
		}
		return this;
	}
	
	/** @param slot The slot to modify
	 * @param icon The ItemStack to use in the given slot
	 * @return This InventoryGUI */
	public InventoryGUI setSlotIcon(int slot, ItemStack icon) {
		Integer key = Integer.valueOf(slot);
		if(icon == null) {
			this.slots.remove(key);
		} else {
			this.slots.put(key, icon);
		}
		return this;
	}
	
	/** <b>Note:</b>&nbsp;This method will fail silently if there is no
	 * ItemStack in the given slot.
	 * 
	 * @param slot The slot to modify
	 * @param title The title to set for the ItemStack in the given slot
	 * @return This InventoryGUI */
	public InventoryGUI setSlotTitle(int slot, String title) {
		title = title == null ? "" : title;
		ItemStack icon = this.getSlotIcon(slot, false);
		if(icon != null) {
			ItemMeta meta = this.getSlotMeta(slot, true);
			meta.setDisplayName(title);
			icon.setItemMeta(meta);
		}
		return this;
	}
	
	/** <b>Note:</b>&nbsp;This method will fail silently if there is no
	 * ItemStack in the given slot.
	 * 
	 * @param slot The slot to modify
	 * @param lores The lore to set for the ItemStack in the given slot
	 * @return This InventoryGUI */
	public InventoryGUI setSlotLore(int slot, String... lores) {
		ItemStack icon = this.getSlotIcon(slot, false);
		if(icon != null) {
			ItemMeta meta = this.getSlotMeta(slot, true);
			if(lores == null) {
				meta.setLore(null);
				icon.setItemMeta(meta);
				return this;
			}
			ArrayList<String> list = new ArrayList<>();
			for(String lore : lores) {
				list.add(lore);
			}
			meta.setLore(list);
			icon.setItemMeta(meta);
		}
		return this;
	}
	
	/** <b>Note:</b>&nbsp;This method will fail silently if there is no
	 * ItemStack in the given slot.
	 * 
	 * @param slot The slot to modify
	 * @param lores The lore to append onto the ItemStack's existing lore
	 * @return This InventoryGUI */
	public InventoryGUI appendSlotLore(int slot, String... lores) {
		ItemStack icon = this.getSlotIcon(slot, false);
		if(icon != null) {
			ItemMeta meta = this.getSlotMeta(slot, true);
			List<String> list = meta.hasLore() ? meta.getLore() : new ArrayList<>();
			lores = lores == null ? new String[0] : lores;
			for(String lore : lores) {
				list.add(lore);
			}
			meta.setLore(list);
			icon.setItemMeta(meta);
		}
		return this;
	}
	
	/** @param slot The slot to modify
	 * @param icon The Material to use as an icon
	 * @param title The title to set for the ItemStack in the given slot
	 * @param lores The lore to append onto the ItemStack's existing lore
	 * @return This InventoryGUI */
	public InventoryGUI setSlot(int slot, Material icon, String title, Collection<String> lores) {
		return this.setSlot(slot, icon, title, lores.toArray(new String[lores.size()]));
	}
	
	/** @param slot The slot to modify
	 * @param icon The Material to use as an icon
	 * @param title The title to set for the ItemStack in the given slot
	 * @param lores The lore to append onto the ItemStack's existing lore
	 * @return This InventoryGUI */
	public InventoryGUI setSlot(int slot, Material icon, String title, String... lores) {
		ItemStack item = this.getSlotIcon(slot, true);
		item.setType(icon);
		ItemMeta meta = this.getSlotMeta(slot, true);
		meta.setDisplayName(title);
		List<String> list = meta.hasLore() ? meta.getLore() : new ArrayList<>();
		lores = lores == null ? new String[0] : lores;
		for(String lore : lores) {
			list.add(lore);
		}
		meta.setLore(list);
		item.setItemMeta(meta);
		return this;
	}
	
	public void updateInventory() {
		if(this.inventory == null) {
			InventoryOwner owner = new InventoryOwner(this);
			this.inventory = Bukkit.getServer().createInventory(owner, this.getSize(), this.title);
			owner.setInventory(this.inventory);
		}
		this.inventory.clear();
		for(Entry<Integer, ItemStack> entry : this.slots.entrySet()) {
			int slot = entry.getKey().intValue();
			if(slot >= this.inventory.getSize() || slot < 0) {
				continue;
			}
			this.inventory.setItem(slot, entry.getValue());
		}
	}
	
	public void updateFromInventory(Inventory inv) {
		inv.toString();
		for(int i = 0; i < inv.getSize(); i++) {
			this.setSlotIcon(i, inv.getItem(i));
		}
		if(this.inventory != null && inv != this.inventory) {
			this.updateInventory();
		}
	}
	
	/** @return The Inventory representing this InventoryGUI's contents. The
	 *         same Inventory will be returned each time this method is called,
	 *         and is updated to reflect the contents of this InventoryGUI. */
	public Inventory open() {
		this.updateInventory();
		return this.inventory;
	}
	
	/** Opens an inventory window with this inventory on the top and
	 * the player's inventory on the bottom.
	 * 
	 * @param player The player who will view the inventory
	 * @return The newly opened inventory view */
	public InventoryView show(Player player) {
		return player.openInventory(this.open());
	}
	
	/** @return The sound that is played when this InventoryGUI is opened for a
	 *         player */
	public final Sound getOpenSound() {
		return this.openSound;
	}
	
	/** @return The sound that is played when a player closes this
	 *         InventoryGUI */
	public final Sound getCloseSound() {
		return this.closeSound;
	}
	
	/** @return The sound that is played when a player clicks on an ItemStack
	 *         in this InventoryGUI */
	public final Sound getClickSound() {
		return this.clickSound;
	}
	
	/** @param open The sound that will be played when this InventoryGUI is
	 *            opened for a player
	 * @param close The sound that will be played when a player closes this
	 *            InventoryGUI
	 * @param click The sound that will be played when a player clicks on an
	 *            ItemStack in this InventoryGUI
	 * @return This InventoryGUI */
	public final InventoryGUI setSounds(Sound open, Sound close, Sound click) {
		this.openSound = open;
		this.closeSound = close;
		this.clickSound = click;
		return this;
	}
	
	/** Sets this InventoryGUI's sounds to:<br>
	 * <br>
	 * <code>Sound.BLOCK_CHEST_OPEN Sound.BLOCK_CHEST_CLOSE Sound.UI_BUTTON_CLICK</code>
	 * 
	 * @return This InventoryGUI */
	public final InventoryGUI setDefaultSounds() {
		return this.setDefaultOpenSound().setDefaultCloseSound().setDefaultClickSound();
	}
	
	/** Sets this InventoryGUI's open sound to
	 * <code>Sound.BLOCK_CHEST_OPEN</code>
	 * 
	 * @return This InventoryGUI */
	public final InventoryGUI setDefaultOpenSound() {
		return this.setOpenSound(Sound.BLOCK_CHEST_OPEN);
	}
	
	/** @param sound The sound that will be played when this InventoryGUI is
	 *            opened for a player
	 * @return This InventoryGUI */
	public final InventoryGUI setOpenSound(Sound sound) {
		this.openSound = sound;
		return this;
	}
	
	/** Sets this InventoryGUI's close sound to
	 * <code>Sound.BLOCK_CHEST_CLOSE</code>
	 * 
	 * @return This InventoryGUI */
	public final InventoryGUI setDefaultCloseSound() {
		return this.setCloseSound(Sound.BLOCK_CHEST_CLOSE);
	}
	
	/** @param sound The sound that will be played when a player closes this
	 *            InventoryGUI
	 * @return This InventoryGUI */
	public final InventoryGUI setCloseSound(Sound sound) {
		this.closeSound = sound;
		return this;
	}
	
	/** Sets this InventoryGUI's click sound to
	 * <code>Sound.UI_BUTTON_CLICK</code>
	 * 
	 * @return This InventoryGUI */
	public final InventoryGUI setDefaultClickSound() {
		return this.setClickSound(Sound.UI_BUTTON_CLICK);
	}
	
	/** @param sound The sound that will be played when a player clicks on an
	 *            ItemStack in this
	 *            InventoryGUI
	 * @return This InventoryGUI */
	public final InventoryGUI setClickSound(Sound sound) {
		this.clickSound = sound;
		return this;
	}
	
	/** @param slot The slot that was clicked
	 * @param player The player that clicked the slot
	 * @param inventory The inventory that was clicked */
	public abstract void onClick(int slot, Player player, Inventory inventory);
	
	/** @param action The ClickAction to set
	 * @return This InventoryGUI */
	public final InventoryGUI setClickAction(ClickAction action) {
		this.clickAction = action;
		return this;
	}
	
	private static final Field getDeclaredField(Class<?> clazz, String name) throws NoSuchFieldException {
		for(Field field : clazz.getDeclaredFields()) {
			if(field.getName().equals(name)) {
				return field;
			}
		}
		throw new NoSuchFieldException(name);
	}
	
	/** Copies the given InventoryGUI object's properties onto this one
	 * 
	 * @param obj The instance of InventoryGUI whose class was loaded with a
	 *            different {@link ClassLoader}
	 * @param clazz The class that was loaded with a different
	 *            {@link ClassLoader}
	 * @return Any exception thrown while performing the copy operation */
	@SuppressWarnings("unchecked")
	public final Throwable copyFrom(Object obj, Class<?> clazz) {
		//Field[] declaredFields = clazz.getDeclaredFields();
		//System.out.println(clazz.getName() + "'s field count: " + declaredFields.length);
		//for(Field field : declaredFields) {
		//	System.out.println(field.getType().getName() + " " + field.getName());
		//}
		try {
			Field title = getDeclaredField(clazz, "title");
			title.setAccessible(true);
			Field size = getDeclaredField(clazz, "size");
			size.setAccessible(true);
			Field openSound = getDeclaredField(clazz, "openSound");
			openSound.setAccessible(true);
			Field closeSound = getDeclaredField(clazz, "closeSound");
			closeSound.setAccessible(true);
			Field clickSound = getDeclaredField(clazz, "clickSound");
			clickSound.setAccessible(true);
			Field allowEditing = getDeclaredField(clazz, "allowEditing");
			allowEditing.setAccessible(true);
			Field removeOnClose = getDeclaredField(clazz, "removeOnClose");
			removeOnClose.setAccessible(true);
			Field slots = getDeclaredField(clazz, "slots");
			slots.setAccessible(true);
			
			this.title = (String) title.get(obj);
			this.size = ((Integer) size.get(obj)).intValue();
			this.openSound = (Sound) openSound.get(obj);
			this.closeSound = (Sound) closeSound.get(obj);
			this.clickSound = (Sound) clickSound.get(obj);
			this.allowEditing = ((Boolean) allowEditing.get(obj)).booleanValue();
			this.removeOnClose = ((Boolean) removeOnClose.get(obj)).booleanValue();
			this.slots.clear();
			this.slots.putAll((ConcurrentHashMap<Integer, ItemStack>) slots.get(obj));
			
			Field clickAction = getDeclaredField(clazz, "clickAction");
			clickAction.setAccessible(true);
			final Object clickObj = clickAction.get(obj);//ClickAction.class
			this.clickAction = (slot, player, inventory) -> {
				try {
					Method onClick = clickObj.getClass().getDeclaredMethod("onClick", int.class, Player.class, Inventory.class);
					onClick.invoke(clickObj, Integer.valueOf(slot), player, inventory);
				} catch(Throwable ignored) {
				}
			};
			return null;
		} catch(Throwable e) {
			return e;
		}
	}
	
	protected static final void cancelAndCloseClickEvent(InventoryClickEvent event) {
		final Player clicker = (Player) event.getWhoClicked();
		if(event.getView().getCursor() != null) {
			for(ItemStack leftover : clicker.getInventory().addItem(event.getView().getCursor()).values()) {
				clicker.getWorld().dropItem(clicker.getLocation(), leftover);
			}
			event.getView().setCursor(null);
		}
		event.setCancelled(true);
		clicker.closeInventory();
	}
	
	/** @param view The InventoryView to test
	 * @return Whether or not the InventoryView's top inventory is a Storage
	 *         Chest GUI that was opened before this plugin was unloaded and
	 *         reloaded */
	@SuppressWarnings("deprecation")//Why are inventory.getTitle() and inventory.getName() deprecated? Dumb.
	public static final boolean isOldChestView(InventoryView view) {
		Inventory inv = view.getTopInventory();
		if(inv.getTitle().equals(ChatColor.GOLD + "Storage Chest") && inv.getSize() == 54) {
			if(inv.getHolder() != null && !(inv.getHolder() instanceof InventoryOwner) && inv.getHolder().getClass().getName().equals(InventoryOwner.class.getName())) {
				return true;
			}
		}
		return false;
	}
	
	/** @param player The player whose old Storage Chest inventory will be fixed
	 * @return Whether or not it was fixed */
	public static final boolean fixPlayersOldChestView(Player player) {
		final Inventory inv = player.getOpenInventory().getTopInventory();
		InventoryGUI gui;
		if(inv.getHolder() != null && !(inv.getHolder() instanceof InventoryOwner) && inv.getHolder().getClass().getName().equals(InventoryOwner.class.getName())) {
			InventoryHolder oldOwner = inv.getHolder();
			try {
				Class<?> clazz = oldOwner.getClass();
				Field creator = getDeclaredField(clazz, "creator");
				creator.setAccessible(true);
				final Object guiObj = creator.get(oldOwner);
				Class<?> guiClazz = creator.getType();
				if(guiObj == null || guiClazz == null) {
					//System.err.print("Failed to hax into old loaded class objects: oldInventoryOwner's creator field value is null!");
					getStorageChestForPlayer(player).show(player);
					return false;
				}
				gui = new InventoryGUI("", 0) {
					@Override
					public void onClick(int slot, Player player, Inventory inventory) {
						try {
							Method onClick = guiObj.getClass().getDeclaredMethod("onClick", int.class, Player.class, Inventory.class);
							onClick.invoke(guiObj, Integer.valueOf(slot), player, inventory);
						} catch(Throwable ignored) {
						}
					}
				};
				Throwable trouble = gui.copyFrom(guiObj, guiClazz);
				if(trouble != null) {
					throw trouble;
				}
				perPlayerInstances.put(getFromMap(player.getUniqueId()), gui);
				gui.show(player);
				System.gc();
				return false;
			} catch(Throwable ignored) {
				//System.err.print("Failed to hax into old loaded class objects: ");
				//ignored.printStackTrace(System.err);
				getStorageChestForPlayer(player).show(player);
				return false;
			}
		} else if(!(inv.getHolder() instanceof InventoryOwner)) {
			return false;
		} else {
			gui = ((InventoryOwner) inv.getHolder()).creator;
		}
		if(gui.title.equals(ChatColor.GOLD + "Storage Chest") && gui.size == 54 && !perPlayerInstances.containsValue(gui)) {
			perPlayerInstances.put(getFromMap(player.getUniqueId()), gui);
		}
		System.gc();
		return true;
	}
	
	/** @param event This event is called when a player clicks a slot in an
	 *            inventory.
	 *            <p>
	 *            Because InventoryClickEvent occurs within a modification of
	 *            the Inventory,
	 *            not all Inventory related methods are safe to use.
	 *            <p>
	 *            The following should never be invoked by an EventHandler for
	 *            InventoryClickEvent using the HumanEntity or InventoryView
	 *            associated with
	 *            this event:
	 *            <ul>
	 *            <li>{@link HumanEntity#closeInventory()}
	 *            <li>{@link HumanEntity#openInventory(Inventory)}
	 *            <li>{@link HumanEntity#openWorkbench(Location, boolean)}
	 *            <li>{@link HumanEntity#openEnchanting(Location, boolean)}
	 *            <li>{@link InventoryView#close()}
	 *            </ul>
	 *            To invoke one of these methods, schedule a task using
	 *            {@link BukkitScheduler#runTask(Plugin, Runnable)}, which will
	 *            run the task
	 *            on the next tick. Also be aware that this is not an exhaustive
	 *            list, and
	 *            other methods could potentially create issues as well.
	 *            <p>
	 *            Assuming the EntityHuman associated with this event is an
	 *            instance of a
	 *            Player, manipulating the MaxStackSize or contents of an
	 *            Inventory will
	 *            require an Invocation of {@link Player#updateInventory()}.
	 *            <p>
	 *            Modifications to slots that are modified by the results of
	 *            this
	 *            InventoryClickEvent can be overwritten. To change these slots,
	 *            this event
	 *            should be cancelled and all desired changes to the inventory
	 *            applied.
	 *            Alternatively, scheduling a task using
	 *            {@link BukkitScheduler#runTask(
	 *            Plugin, Runnable)}, which would execute the task on the next
	 *            tick, would
	 *            work as well. */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public static final void onInventoryClickEvent(InventoryClickEvent event) {
		final Inventory inv = event.getView().getTopInventory();
		if(!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		final Player clicker = ((Player) event.getWhoClicked());
		if(inv.getHolder() != null && !(inv.getHolder() instanceof InventoryOwner) && inv.getHolder().getClass().getName().equals(InventoryOwner.class.getName())) {
			cancelAndCloseClickEvent(event);
			if(!fixPlayersOldChestView(clicker)) {
				return;
			}
		}
		if(!(inv.getHolder() instanceof InventoryOwner)) {
			return;
		}
		InventoryGUI gui = ((InventoryOwner) inv.getHolder()).creator;
		if(gui.title.equals(ChatColor.GOLD + "Storage Chest") && gui.size == 54 && !perPlayerInstances.containsValue(gui)) {
			perPlayerInstances.put(getFromMap(clicker.getUniqueId()), gui);
		}
		final boolean top = event.getClick().isShiftClick() || event.getRawSlot() < event.getView().getTopInventory().getSize();
		if(top && !gui.allowEditing) {
			event.setCancelled(true);
		}
		Bukkit.getScheduler().runTaskLater(gui.plugin, () -> {
			//if(event.getRawSlot() < event.getView().getTopInventory().getSize() && gui.getSlotIcon(event.getRawSlot(), false) != null) {
			if(event.getRawSlot() < event.getView().getTopInventory().getSize() && gui.getSlotIcon(event.getRawSlot(), false) != null && gui.getSlotIcon(event.getRawSlot(), false).getType() != Material.AIR) {
				clicker.updateInventory();
				if(gui.clickAction != null) {
					gui.clickAction.onClick(event.getSlot(), clicker, inv);
				} else {
					gui.onClick(event.getSlot(), clicker, inv);
				}
				if(gui.clickSound != null) {
					clicker.playSound(clicker.getLocation(), gui.clickSound, 3f, 1f);
				}
			}
			if(top && gui.allowEditing) {
				gui.updateFromInventory(inv);
			}
		}, 2L);
	}
	
	/** @param event Represents a player related inventory event */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public static final void onInventoryOpenEvent(InventoryOpenEvent event) {
		Inventory inv = event.getView().getTopInventory();
		if(!(inv.getHolder() instanceof InventoryOwner)) {
			return;
		}
		InventoryGUI gui = ((InventoryOwner) inv.getHolder()).creator;
		if(gui.openSound != null) {
			((Player) event.getPlayer()).getLocation().getWorld().playSound(event.getPlayer().getLocation(), gui.openSound, 3f, 1f);
		}
	}
	
	/** @param event Represents a player related inventory event */
	@EventHandler(priority = EventPriority.HIGHEST)
	public static final void onInventoryCloseEvent(InventoryCloseEvent event) {
		Inventory inv = event.getView().getTopInventory();
		if(!(inv.getHolder() instanceof InventoryOwner)) {
			return;
		}
		InventoryGUI gui = ((InventoryOwner) inv.getHolder()).creator;
		if(gui.closeSound != null) {
			((Player) event.getPlayer()).getLocation().getWorld().playSound(event.getPlayer().getLocation(), gui.closeSound, 3f, 1f);
		}
		if(gui.removeOnClose) {
			instances.remove(gui);
		}
	}
	
	/** @param gui The gui to use
	 * @param inventory The open inventory to update */
	public static final void setInventoryWithIslandJoinPage(InventoryGUI gui, Inventory inventory) {
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
		int i = 0;
		int j = 0;
		int targetI = (gui.currentPage - 1) * 54;
		if(gui.currentPage == 1) {
			setMainMenuSign(inventory, 8);
		}
		for(Island is : joinable) {
			if(j < targetI) {
				j++;
				continue;
			}
			if(gui.currentPage == 1 && i == 8) {
				//Do nothing. reserved for main menu sign.
			} else if(i % 53 == 0 && gui.currentPage < pages) {
				ItemStack paper = new ItemStack(Material.PAPER);
				ItemMeta meta = Main.server.getItemFactory().getItemMeta(Material.PAPER);
				String title = ChatColor.DARK_GRAY + "Next page";
				List<String> lore = new ArrayList<>();
				lore.add(ChatColor.GRAY + "Click to view the next");
				lore.add(ChatColor.GRAY + "page.");
				meta.setDisplayName(title);
				meta.setLore(lore);
				paper.setItemMeta(meta);
				inventory.setItem(i, paper);
			} else if(i % 45 == 0 && gui.currentPage > 1) {
				ItemStack paper = new ItemStack(Material.PAPER);
				ItemMeta meta = Main.server.getItemFactory().getItemMeta(Material.PAPER);
				String title = ChatColor.DARK_GRAY + "Previous page";
				List<String> lore = new ArrayList<>();
				lore.add(ChatColor.GRAY + "Click to view the previous");
				lore.add(ChatColor.GRAY + "page.");
				meta.setDisplayName(title);
				meta.setLore(lore);
				paper.setItemMeta(meta);
				inventory.setItem(i, paper);
			} else {
				inventory.setItem(i, is.getOwnerSkull(is.getOwnerName() + ChatColor.RESET + ChatColor.DARK_GREEN + "'s Island"));
			}
			i++;
			j++;
		}
	}
	
	/** @param gui The gui that will be updated
	 * @param slot The slot to set the main menu sign in */
	public static final void setMainMenuSign(InventoryGUI gui, int slot) {
		ItemStack mainMenu = new ItemStack(Material.SIGN);
		ItemMeta meta = Main.server.getItemFactory().getItemMeta(Material.SIGN);
		meta.setDisplayName(ChatColor.DARK_GREEN + "Main menu");
		meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to go back to the", ChatColor.GRAY + "main island menu."));
		mainMenu.setItemMeta(meta);
		gui.setSlotIcon(slot, mainMenu);
	}
	
	/** @param inv The inventory that will be updated
	 * @param slot The slot to set the main menu sign in */
	public static final void setMainMenuSign(Inventory inv, int slot) {
		ItemStack mainMenu = new ItemStack(Material.SIGN);
		ItemMeta meta = Main.server.getItemFactory().getItemMeta(Material.SIGN);
		meta.setDisplayName(ChatColor.DARK_GREEN + "Main menu");
		meta.setLore(Arrays.asList(ChatColor.GRAY + "Click to go back to the", ChatColor.GRAY + "main island menu."));
		mainMenu.setItemMeta(meta);
		inv.setItem(slot, mainMenu);
	}
	
	public static final void setPageButton(Inventory inv, int slot, boolean nextOrPrev, int currentPage) {
		int page = currentPage + (nextOrPrev ? 1 : -1);
		ItemStack mainMenu = new ItemStack(Material.PAPER);
		ItemMeta meta = Main.server.getItemFactory().getItemMeta(Material.PAPER);
		meta.setDisplayName(ChatColor.DARK_AQUA.toString().concat(nextOrPrev ? "Next" : "Previous").concat(" page"));
		meta.setLore(Arrays.asList(//
				ChatColor.AQUA.toString().concat("Click to go ").concat(!nextOrPrev ? "back " : "").concat("to page ").concat(Integer.toString(page)).concat("."),//
				ChatColor.AQUA.toString().concat("You are currently on page: ").concat(Integer.toString(currentPage)).concat(".")//
		));
		meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
		meta.addItemFlags(ItemFlag.values());
		mainMenu.setItemMeta(meta);
		inv.setItem(slot, mainMenu);
	}
	
	private static final class InventoryOwner implements InventoryHolder {
		
		public final InventoryGUI creator;
		public volatile Inventory inventory;
		
		public InventoryOwner(InventoryGUI creator) {
			this.creator = creator;
		}
		
		public InventoryOwner setInventory(Inventory inventory) {
			this.inventory = inventory;
			return this;
		}
		
		@SuppressWarnings("unused")
		public final boolean isMyInventory(InventoryView view) {
			return view.getTopInventory().getHolder() == this || (view.getTopInventory().getHolder() instanceof InventoryOwner && ((InventoryOwner) view.getTopInventory().getHolder()).creator == this.creator);
		}
		
		/** @see org.bukkit.inventory.InventoryHolder#getInventory() */
		@Override
		public Inventory getInventory() {
			return this.inventory;
		}
		
	}
	
	/** Allows for assigning a click action to an InventoryGUI
	 *
	 * @author Brian_Entei */
	public static interface ClickAction {
		
		/** @param slot The slot that was clicked
		 * @param player The player that clicked the slot
		 * @param inventory The inventory that was clicked */
		public void onClick(int slot, Player player, Inventory inventory);
		
	}
	
}
