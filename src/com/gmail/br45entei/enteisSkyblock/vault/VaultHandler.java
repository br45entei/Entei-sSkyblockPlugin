package com.gmail.br45entei.enteisSkyblock.vault;

import com.gmail.br45entei.enteisSkyblock.main.Main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

/** @author br45e */
public class VaultHandler {
	
	private static volatile int tid = -1;
	
	private static volatile net.milkbowl.vault.permission.Permission perms;
	private static volatile net.milkbowl.vault.chat.Chat chat;
	private static volatile net.milkbowl.vault.economy.Economy eco;
	
	/** @return A reference to the Vault plugin, if loaded, or
	 *         <b><code>null</code></b> otherwise */
	public static final net.milkbowl.vault.Vault getVault() {
		for(Plugin plugin : Bukkit.getServer().getPluginManager().getPlugins()) {
			if(plugin instanceof net.milkbowl.vault.Vault) {
				return (net.milkbowl.vault.Vault) plugin;
			}
		}
		return null;
	}
	
	/** @return Whether or not {@link #getVault()} returns anything */
	public static final boolean isVaultPresent() {
		return getVault() != null;
	}
	
	/** @throws ClassNotFoundException Thrown if vault is not loaded */
	public static final void setupVault() throws ClassNotFoundException {
		if(isVaultPresent()) {
			RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			if(rsp != null) {
				perms = rsp.getProvider();
			}
			RegisteredServiceProvider<net.milkbowl.vault.chat.Chat> rsc = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
			if(rsc != null) {
				chat = rsc.getProvider();
			}
			RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rse = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if(rse != null) {
				eco = rse.getProvider();
			}
		}
		if(tid == -1) {
			tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
				@Override
				public final void run() {
					try {
						setupVault();
					} catch(ClassNotFoundException e) {
					}
				}
			}, 100L, 120 * 20);//Every 2 minutes
		}
	}
	
	/** Called when the plugin is being disabled */
	public static final void onDisable() {
		if(tid != -1) {
			Bukkit.getScheduler().cancelTask(tid);
			tid = -1;
		}
	}
	
	/** @return The Vault PermissionsHandler */
	public static final net.milkbowl.vault.permission.Permission getPermissionsHandler() {
		return perms;
	}
	
	/** @return The Vault ChatHandler */
	public static final net.milkbowl.vault.chat.Chat getChatHandler() {
		return chat;
	}
	
	/** @return The Vault EconomyHandler */
	public static final net.milkbowl.vault.economy.Economy getEconomyHandler() {
		return eco;
	}
	
}
