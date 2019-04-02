package com.gmail.br45entei.enteisSkyblock.event;

import com.gmail.br45entei.enteisSkyblock.challenge.Challenge;
import com.gmail.br45entei.enteisSkyblock.main.Island;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

/** @author Brian_Entei */
public abstract class ChallengeEvent extends PlayerEvent {
	
	private final Challenge challenge;
	private final Island island;
	
	/** @param challenge The challenge involved in this event
	 * @param who The player who is involved in this event */
	public ChallengeEvent(Challenge challenge, Player who) {
		super(who);
		this.challenge = challenge;
		this.island = Island.getMainIslandFor(who);
	}
	
	/** @return The challenge that is involved in this event */
	public final Challenge getChallenge() {
		return this.challenge;
	}
	
	/** @return The Island involved in this event */
	public final Island getIsland() {
		return this.island;
	}
	
}
