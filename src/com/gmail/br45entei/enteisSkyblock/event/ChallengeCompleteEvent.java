package com.gmail.br45entei.enteisSkyblock.event;

import com.gmail.br45entei.enteisSkyblock.challenge.Challenge;
import com.gmail.br45entei.enteisSkyblock.main.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/** Custom event that is fired before a player completes an Island challenge
 * 
 * @author Brian_Entei */
public class ChallengeCompleteEvent extends ChallengeEvent implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	private transient volatile boolean isCancelled = false;
	
	/** @param challenge The challenge that was completed
	 * @param who The player who completed the challenge */
	public ChallengeCompleteEvent(Challenge challenge, Player who) {
		super(challenge, who);
	}
	
	/** @param player The player that just completed a challenge
	 * @param challenge The challenge that the player just completed
	 * @return The event that was fired */
	public static final ChallengeCompleteEvent fire(Player player, Challenge challenge) {
		ChallengeCompleteEvent event = new ChallengeCompleteEvent(challenge, player);
		Main.pluginMgr.callEvent(event);
		return event;
	}
	
	/** @see org.bukkit.event.Event#getHandlers() */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	/** @return The handlers */
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
	}
	
}
