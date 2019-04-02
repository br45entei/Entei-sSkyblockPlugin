package com.gmail.br45entei.enteisSkyblock.event;

import com.gmail.br45entei.enteisSkyblock.challenge.Challenge;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/** @author Brian_Entei */
public class ChallengeTierCompleteEvent extends ChallengeEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	private final int tier;
	private final boolean minimumOrComplete;
	
	/** @param challenge The challenge that is involved in this event
	 * @param minimumOrComplete Whether or not the given player completed all of
	 *            the challenges in the tier, or just the minimum amount
	 *            required to unlock the next tier
	 * @param who The player who is involved in this event */
	public ChallengeTierCompleteEvent(Challenge challenge, boolean minimumOrComplete, Player who) {
		super(challenge, who);
		this.tier = challenge.getDifficulty();
		this.minimumOrComplete = minimumOrComplete;
	}
	
	/** @return The tier of the challenge that is involved in this event */
	public int getTier() {
		return this.tier;
	}
	
	/** @return Whether or not the player who is involved in this event
	 *         completed the entire tier of challenges, or just the minimum
	 *         amount of them required to unlock the next tier */
	public boolean didPlayerCompleteEntireTier() {
		return !this.minimumOrComplete;
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
	
}
